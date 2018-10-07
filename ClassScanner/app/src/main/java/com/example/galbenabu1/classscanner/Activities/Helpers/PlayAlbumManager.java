package com.example.galbenabu1.classscanner.Activities.Helpers;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Logic.Database.DBManager;
import Logic.Interfaces.MyConsumer;
import Logic.Models.Album;
import Logic.Models.PictureAudioData;

public class PlayAlbumManager {
    private static final int NUM_OF_PICTURES_TO_INITIALY_FETCH = 5;
    private static final int NUM_OF_PICTURES_TO_FETCH = 2;
    private static final int MINIMUM_NUMBER_OF_PICTURES_BEFORE_FETCH = 3;
    private static final String TAG = "PlayAlbumManagers";

    // Models
    private Album mAlbum;
    private PictureAudioData mAudio;
    private List<PictureAudioData> mPictureList;
    private int mCurrentDisplayedPictureDataIndex;
    private List<Bitmap> mCachedImageBitmapList;

    // DB
    private DBManager mDBManager;

    // Timer
    private MyConsumer<Bitmap> mOnUpdateNextPicture;
    private Timer mShowNextPictueTimer;
    private ShowNextPictureTask mShowNextPictureTask;

    private Runnable mOnReady;

    //todo: figure out how to hold actual audio data

    public PlayAlbumManager(Album album, Runnable onReady) {
        this.mOnReady = onReady;
        this.mAlbum = album;
        this.mAudio = this.mAlbum.getM_Audio();
        this.mPictureList = this.mAlbum.getM_Pictures();
        this.mDBManager = new DBManager();
        this.mCachedImageBitmapList = new ArrayList<>();
        this.init();
    }

    public void init() {
        //todo: reset audio recording to start.
        this.mCurrentDisplayedPictureDataIndex = 0;
        this.mShowNextPictueTimer = new Timer();
        this.performPicturesFetchFromIndex(0, NUM_OF_PICTURES_TO_INITIALY_FETCH);
    }

    public void start(MyConsumer<Bitmap> onNextPictureUpdate) {
        this.init();
        this.mOnUpdateNextPicture = onNextPictureUpdate;
        this.startPlayingRecording();

        Bitmap firstImageBitmap = this.getNextImageBitmap();
        this.updateNextImage(firstImageBitmap);
    }

    public void stop() {
        //TODO: stop recording.
        this.mCachedImageBitmapList.clear();
    }

    private void startPlayingRecording() {
        //TODO:
    }

    private void updateNextImage(Bitmap imageToShow) {
        // Show current image.
        Log.e(TAG, "Showing next image.");
        this.mOnUpdateNextPicture.accept(imageToShow);

        // Continue updating images only if there are more images to show.
        if(this.mPictureList.size() > this.mCurrentDisplayedPictureDataIndex + 1) {
            this.nextImageShown(); // Update indexes and counters.
            this.fetchImagesIfNeeded();

            this.mShowNextPictureTask = new ShowNextPictureTask(this::updateNextImage);

            // Set next image to display.
            Bitmap nextImageBitmap = this.getNextImageBitmap();
            this.mShowNextPictureTask.setNextImageData(nextImageBitmap);

            // Set timer.
            long updateNextImageDelay = this.getNextImageDelay();
            Log.e(TAG, "Setting timer with delay: " + updateNextImageDelay / 1000);
            this.mShowNextPictueTimer.schedule(this.mShowNextPictureTask, updateNextImageDelay);
        }
    }

    private void nextImageShown() {
        this.mCurrentDisplayedPictureDataIndex++;
        Log.e(TAG, "Currently displayed picture index: " + this.mCurrentDisplayedPictureDataIndex);
    }

    private void fetchImagesIfNeeded() {
        Log.e(TAG, "In fetch if needed.");
        //Check if currently displayed picture index and cache size are less than the total pictures size.
        boolean isThereMoreImagesToFetch = this.mCurrentDisplayedPictureDataIndex + this.mCachedImageBitmapList.size() < this.mPictureList.size();
        if(this.mCachedImageBitmapList.size() == MINIMUM_NUMBER_OF_PICTURES_BEFORE_FETCH && isThereMoreImagesToFetch) {
            int imagesToFetchStartIndex = this.mCachedImageBitmapList.size() + this.mCurrentDisplayedPictureDataIndex;
            int imagesToFetchEndIndex = this.mCurrentDisplayedPictureDataIndex + this.mCachedImageBitmapList.size() + NUM_OF_PICTURES_TO_FETCH;
            this.performPicturesFetchFromIndex(imagesToFetchStartIndex, imagesToFetchEndIndex);
        }
    }

    public void jumpToPositionInRecording(double progressValue) {
        //TODO Optional
    }

    private void performPicturesFetchFromIndex(int startIndex, int endIndex) {
        List<PictureAudioData> dataOfPicturesToFetch = this.mPictureList.subList(startIndex, Math.min(endIndex, this.mPictureList.size()));
        Log.e(TAG, "Fetching from index " + startIndex + " to index: " + Math.min(endIndex, this.mPictureList.size()));
        this.mDBManager.fetchImages(dataOfPicturesToFetch, this::onFetchedImagesSuccess);
    }

    private void onFetchedImagesSuccess(List<Bitmap> picturesData) {
        Log.e(TAG, "Finished fetching " + picturesData.size() + " image bitmaps.");
        this.mCachedImageBitmapList.addAll(picturesData);

        if(this.mOnReady != null) {
            // First fetch.
            this.mOnReady.run();
            this.mOnReady = null;
        }
    }

    private Bitmap getNextImageBitmap() {
        Bitmap nextImageBitmap = this.mCachedImageBitmapList.get(0);
        this.mCachedImageBitmapList.remove(0);

        return nextImageBitmap;
    }

    // return the diff between the currently displayed image creation date and the next image's creation date.
    private long getNextImageDelay() {
        long currentImageCreationDate = this.mPictureList.get(this.mCurrentDisplayedPictureDataIndex - 1).getM_CreationDate().getTime();
        long nextImageCreationDate = this.mPictureList.get(this.mCurrentDisplayedPictureDataIndex).getM_CreationDate().getTime();

        return nextImageCreationDate - currentImageCreationDate;
    }

    public int getNumberOfShownImages() {
        return this.mPictureList.size();
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
