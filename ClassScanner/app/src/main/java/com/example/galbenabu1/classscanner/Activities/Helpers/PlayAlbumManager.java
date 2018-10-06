package com.example.galbenabu1.classscanner.Activities.Helpers;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Logic.Database.DBManager;
import Logic.Interfaces.MyConsumer;
import Logic.Models.Album;
import Logic.Models.PictureAudioData;

public class PlayAlbumManager {
    private static final int NUM_OF_PICTURES_TO_FETCH = 5;
    private static final int MINIMUM_NUMBER_OF_PICTURES_BEFORE_FETCH = 3;

    // Models
    private final boolean mIsPrivateAlbum;
    private Album mAlbum;
    private PictureAudioData mAudio;
    private List<PictureAudioData> mPictureList;
    private int mCurrentDisplayedPictureDataIndex;
    private List<byte[]> mCachedImageByteArrayList;
    private int mShownImagesCounter = 0;

    // DB
    private DBManager mDBManager;

    // Timer
    private MyConsumer<byte[]> mOnUpdateNextPicture;
    private Timer mShowNextPictueTimer;
    private ShowNextPictureTask mShowNextPictureTask;

    //todo: figure out how to hold actual audio data

    public PlayAlbumManager(Album album, boolean isPrivateAlbum) {
        this.mIsPrivateAlbum = isPrivateAlbum;
        this.mAlbum = album;
        this.mAudio = this.mAlbum.getM_Audio();
        this.mPictureList = this.mAlbum.getM_Pictures();
        this.mDBManager = new DBManager();
        this.init();
    }

    public void init() {
        //todo: reset audio recording to start.
        this.mCachedImageByteArrayList.clear();
        this.mCurrentDisplayedPictureDataIndex = 1; // First image is displayed automatically. Start index from 1.
        this.mShowNextPictueTimer = new Timer();

        this.performPicturesFetchFromIndex(0);
    }

    public void start(MyConsumer<byte[]> onNextPictureUpdate) {
        this.init();
        this.mShowNextPictureTask = new ShowNextPictureTask(this::updateNextImage);
        this.mOnUpdateNextPicture = onNextPictureUpdate;
        this.startPlayingRecording();

        byte[] firstImageByteArray = this.getNextImageByteArray();
        this.updateNextImage(firstImageByteArray);
    }

    public void stop() {
        //TODO: stop recording.
    }

    private void startPlayingRecording() {
        //TODO:
    }

    private void updateNextImage(byte[] imageToShow) {
        // Show current image.
        this.mOnUpdateNextPicture.accept(imageToShow);

        this.nextImageShown(); // Update indexes and counters.
        this.fetchImagesIfNeeded();

        // Set next image to display.
        byte[] nextImageByteArray = this.getNextImageByteArray();
        this.mShowNextPictureTask.setNextImageData(nextImageByteArray);

        // Set timer.
        long updateNextImageDelay = this.getNextImageDelay();
        this.mShowNextPictueTimer.schedule(this.mShowNextPictureTask, updateNextImageDelay);
    }

    private void nextImageShown() {
        this.mShownImagesCounter++;
        this.mCurrentDisplayedPictureDataIndex++;
    }

    private void fetchImagesIfNeeded() {
        if(this.mShownImagesCounter == MINIMUM_NUMBER_OF_PICTURES_BEFORE_FETCH) {
            this.mShownImagesCounter = 0;
            this.performPicturesFetchFromIndex(this.mCurrentDisplayedPictureDataIndex);
        }
    }

    public void jumpToPositionInRecording(double progressValue) {
        //TODO Optional
    }

    private void performPicturesFetchFromIndex(int indexToFetchFrom) {
        this.mDBManager.fetchImages(this.mAlbum, this.mPictureList, this.mIsPrivateAlbum, indexToFetchFrom, NUM_OF_PICTURES_TO_FETCH, this::onFetchedImagesSuccess);
    }

    private void onFetchedImagesSuccess(List<byte[]> picturesData) {
        this.mCachedImageByteArrayList = picturesData;
    }

    private byte[] getNextImageByteArray() {
        byte[] nextImageByteArray = this.mCachedImageByteArrayList.get(0);
        this.mCachedImageByteArrayList.remove(nextImageByteArray);

        return nextImageByteArray;
    }

    // return the diff between the currently displayed image creation date and the next image's creation date.
    private long getNextImageDelay() {
        long currentImageCreationDate = this.mPictureList.get(this.mCurrentDisplayedPictureDataIndex).getM_CreationDate().getTime();
        long nextImageCreationDate = this.mPictureList.get(this.mCurrentDisplayedPictureDataIndex + 1).getM_CreationDate().getTime();

        return nextImageCreationDate - currentImageCreationDate;
    }

    private class ShowNextPictureTask extends TimerTask {
        private MyConsumer<byte[]> mOnTimerFinished;
        private byte[] mImageData;

        public ShowNextPictureTask(MyConsumer<byte[]> onTimerFinished) {
            this.mOnTimerFinished = onTimerFinished;
        }

        public void setNextImageData(byte[] mImageData) {
            this.mImageData = mImageData;
        }

        @Override
        public void run() {
            this.mOnTimerFinished.accept(this.mImageData);
        }


    }
}
