package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.galbenabu1.classscanner.R;

public class PlayAlbumActivity extends Activity {

    private ImageView mivDisplayedImage;
    private ProgressBar mpbPlayAlbumProgress;
    private Button mbtnPlayButton;
    private Button mbtnStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_album);

        this.bindUI();
    }

    private void bindUI() {
        this.mivDisplayedImage = findViewById(R.id.ivDisplayCurrentImage);
        this.mpbPlayAlbumProgress = findViewById(R.id.pbAlbumPresentation);
        this.mbtnPlayButton = findViewById(R.id.btnStartPlayingAlbum);
        this.mbtnStopButton = findViewById(R.id.btnStopPlayingAlbum);
    }

    

}
