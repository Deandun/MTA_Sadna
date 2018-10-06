package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

    //TODO: get album and is private album from previous activity.
    private boolean mIsPrivateAlbum;
    private Album mAlbum;

    private ImageView mivDisplayedImage;
    private ProgressBar mpbPlayAlbumProgress;
    private Button mbtnPlayButton;
    private Button mbtnStopButton;

    private PlayAlbumManager mPlayAlbumManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_album);

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
        this.mPlayAlbumManager = new PlayAlbumManager(this.mAlbum, this.mIsPrivateAlbum);
    }

    public void onStart(View v) {
        this.mPlayAlbumManager.start(this::onUpdateNextImage);
    }

    private void onUpdateNextImage(byte[] imageByteArray) {
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        this.mivDisplayedImage.setImageBitmap(imageBitmap);
    }
}
