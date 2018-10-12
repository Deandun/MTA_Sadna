package com.example.galbenabu1.classscanner.Activities.Helpers;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Logic.Database.DBManager;
import Logic.Interfaces.MyConsumer;
import Logic.Models.Album;
import Logic.Models.PictureAudioData;

public class PlayAlbumManager {
    private static final String TAG = "PlayAlbumManagers";

    // Models
    private Album mAlbum;
    private PictureAudioData mAudio;
    private List<PictureAudioData> mPictureList;
    private int mCurrentDisplayedPictureDataIndex;

    // DB
    private DBManager mDBManager;

    // Timer
    private Timer mShowNextPictueTimer;
    private ShowNextPictureTask mShowNextPictureTask;

    private MyConsumer<Bitmap> mOnUpdateNextImage;
    private Bitmap mNextImageBitmap;

    // Audio
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private int mRecordingProgress;

    private boolean mIsPresentationInProgress = false;

    public PlayAlbumManager(Album album, MyConsumer<Bitmap> onUpdateNextImage) {
        this.mOnUpdateNextImage = onUpdateNextImage;
        this.mAlbum = album;
        this.mAudio = this.mAlbum.getM_Audio();
        this.mPictureList = this.mAlbum.getM_Pictures();
        this.mDBManager = new DBManager();

        this.init(0, 0);
    }

    public void init(int indexOfImageToShow, int recordingProgress) {
        this.mCurrentDisplayedPictureDataIndex = indexOfImageToShow;
        this.fetchNextImage();
        this.mRecordingProgress = recordingProgress;
    }

    public void start() {
        this.mShowNextPictueTimer = new Timer();
        this.startPlayingRecording();
    }

    public void jumpTo(int indexOfImageToShow, int recordingProgressValue) {
        this.init(indexOfImageToShow, recordingProgressValue);
    }

    public void stop() {
        this.mIsPresentationInProgress = false;

        if(this.mShowNextPictueTimer != null) {
            this.mShowNextPictueTimer.cancel(); // Cancel timer to show next picture
        }

        if (mMediaPlayer.isPlaying()) {
            Log.e(TAG, "Stop presentation >> Stop the media player");
            //Stop the media player
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
    }

    public void reset() {
        init(0, 0);
        this.stop();
    }

    private void startPlayingRecording() {
        this.mDBManager.fetchRecordingDataSource(this.mAudio.getM_Id(), this::onFetchedRecordingDataSource);
    }

    private void onFetchedRecordingDataSource(Uri dataSourceUri) {
        Log.e(TAG, "onFetchedRecordingDataSource >> "+ dataSourceUri.toString());

        try {
            this.startRecording(dataSourceUri);
        } catch (IOException e) {
            Log.w(TAG, "Error playing recording >> error:" + e.getMessage());
        }

        this.updateNextImage(this.mNextImageBitmap);
    }

    private void startRecording(Uri dataSourceUri) throws IOException {
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(dataSourceUri.toString());
        mMediaPlayer.prepare(); // might take long! (for buffering, etc)

        if(this.mRecordingProgress != 0) {
            int milisecondToJumpToInRecording = this.getProgressInMilisecondsForRecording();
            this.mMediaPlayer.seekTo(milisecondToJumpToInRecording);
        }

        mMediaPlayer.start();
    }

    private int getProgressInMilisecondsForRecording() {
        return (int)(((float)this.mRecordingProgress / 100.0) * this.mMediaPlayer.getDuration());
    }

    private void updateNextImage(Bitmap imageToShow) {
        // Show current image.
        Log.e(TAG, "Showing image at index " + (this.mCurrentDisplayedPictureDataIndex));
        this.mOnUpdateNextImage.accept(imageToShow);
        this.mCurrentDisplayedPictureDataIndex++; // inc index

        // Continue updating images only if there are more images to show.
        if(this.mPictureList.size() > this.mCurrentDisplayedPictureDataIndex) {
            this.mShowNextPictureTask = new ShowNextPictureTask(this::updateNextImage);

            // Set next image to display.
            this.mShowNextPictureTask.setNextImageData(this.mNextImageBitmap);

            // Set timer.
            long updateNextImageDelay = this.getNextImageDelay();
            Log.e(TAG, "Setting timer with delay: " + updateNextImageDelay / 1000);

            // Begin fetching next image.
            this.fetchNextImage();

            // Start timer until showing next image.
            this.mShowNextPictueTimer.schedule(this.mShowNextPictureTask, updateNextImageDelay);
        }
    }


    private void fetchNextImage() {
        Log.e(TAG, "Fetching at index " + this.mCurrentDisplayedPictureDataIndex);
        this.mDBManager.fetchImage(this.mPictureList.get(this.mCurrentDisplayedPictureDataIndex), this::onFetchedImagesSuccess);
    }

    private void onFetchedImagesSuccess(Bitmap imageBitmap) {
        Log.e(TAG, "Finished fetching image bitmap.");

        this.mNextImageBitmap = imageBitmap;

        if(!this.mIsPresentationInProgress) {
            Log.e(TAG, "Manager is now ready.");

            // First fetch.
            this.mOnUpdateNextImage.accept(imageBitmap);
            this.mIsPresentationInProgress = true;
        }
    }

    // return the diff between the currently displayed image creation date and the next image's creation date.
    private long getNextImageDelay() {
        long currentImageCreationDate = this.mPictureList.get(this.mCurrentDisplayedPictureDataIndex - 1).getM_CreationDate().getTime();
        long nextImageCreationDate = this.mPictureList.get(this.mCurrentDisplayedPictureDataIndex).getM_CreationDate().getTime();

        return nextImageCreationDate - currentImageCreationDate;
    }

    private class ShowNextPictureTask extends TimerTask {
        private MyConsumer<Bitmap> mOnTimerFinished;
        private Bitmap mImageData;

        public ShowNextPictureTask(MyConsumer<Bitmap> onTimerFinished) {
            this.mOnTimerFinished = onTimerFinished;
        }

        public void setNextImageData(Bitmap mImageData) {
            this.mImageData = mImageData;
        }

        @Override
        public void run() {
            this.mOnTimerFinished.accept(this.mImageData);
        }
    }
}
