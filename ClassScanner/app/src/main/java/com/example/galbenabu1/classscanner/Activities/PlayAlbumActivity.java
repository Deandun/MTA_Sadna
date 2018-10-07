package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.galbenabu1.classscanner.Activities.Helpers.PlayAlbumManager;
import com.example.galbenabu1.classscanner.R;

import java.util.List;

import Logic.Models.Album;
import Logic.Models.PictureAudioData;

public class PlayAlbumActivity extends Activity {

    private static final String ALBUM_DATA = "album_data";
    private static final String IS_PRIVATE_ALBUM = "is_private_album";
    private static final String TAG = "PlayAlbumActivity";

    private boolean mIsPrivateAlbum;
    private Album mAlbum;
    private int mShownImages = 0;

    private ImageView mivDisplayedImage;
    private ProgressBar mpbPlayAlbumProgress;
    private Button mbtnPlayButton;
    private Button mbtnStopButton;

    private PlayAlbumManager mPlayAlbumManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_album);

        Log.e(TAG, "onCreate >>");

        this.mIsPrivateAlbum = getIntent().getExtras().getBoolean(IS_PRIVATE_ALBUM);
        this.mAlbum = getIntent().getExtras().getParcelable(ALBUM_DATA);

        this.bindUI();
        this.init();
    }

    private void bindUI() {
        this.mivDisplayedImage = findViewById(R.id.ivDisplayCurrentImage);
        this.mpbPlayAlbumProgress = findViewById(R.id.pbAlbumPresentation);
        this.mbtnPlayButton = findViewById(R.id.btnStartPlayingAlbum);
        this.mbtnStopButton = findViewById(R.id.btnStopPlayingAlbum);
    }

    private void init() {
        this.mbtnPlayButton.setEnabled(false); // Set play button to false until the play album manager is ready.
        this.mPlayAlbumManager = new PlayAlbumManager(this.mAlbum, this::onPlayAlbumManagerReady);
    }

    public void onStart(View v) {
        Log.e(TAG, "onStart >>");
        this.mPlayAlbumManager.start(this::onUpdateNextImage);
    }

    private void onUpdateNextImage(Bitmap imageBitmap) {
        this.mShownImages++;
        if(imageBitmap != null) {
            Log.e(TAG, "Showing next image");
            this.mivDisplayedImage.setImageBitmap(imageBitmap);
        } else {
            Log.e(TAG, "Next image to present is null.");
        }

        int totalImages = this.mAlbum.getM_Pictures().size();
        float ratio = ((float)this.mShownImages / (float)totalImages);
        float progress =  ratio * 100;
        Log.e(TAG, "Updating progress to: " + progress);
        this.mpbPlayAlbumProgress.setProgress((int)progress);
    }

    private void onSetNewProgress(int newProgress) {

    }

    private void onPlayAlbumManagerReady() {
        this.mbtnPlayButton.setEnabled(true);
    }
}
