package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.galbenabu1.classscanner.Activities.Enums.eShowCoursesOptions;
import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;

import Logic.Album;
import Logic.Database.DBManager;
import Logic.PictureAudioData;

public class CreateAlbumActivity extends Activity {

    private final static String TAG = "CreateAlbumActivity";
    private final static String NEW_ALBUM_PICTURE_AUDIO_DATA = "new_album_picture_audio_data";
    private final static String NEW_ALBUM_ID = "new_album_id";

    // New album data
    private List<PictureAudioData> mPictureAudioDataCollection;
    private String mNewAlbumID;

    // UI.
    private EditText metAlbumName;
    private EditText metAlbumDescription;

    private DBManager mDBManager = new DBManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);
        this.getNewPictureAudioDataAndAlbumID();
        this.bindUI();
    }

    private void getNewPictureAudioDataAndAlbumID() {
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        mPictureAudioDataCollection = (List<PictureAudioData>) extras.get(NEW_ALBUM_PICTURE_AUDIO_DATA);
        mNewAlbumID = (String)extras.get(NEW_ALBUM_ID);
    }

    private void bindUI() {
        this.metAlbumName = findViewById(R.id.etCreateAlbumName);
        this.metAlbumDescription = findViewById(R.id.etCreateAlbumDescription);
    }

    public void onFinishCreatingAlbumClick(View v) {
        Log.e(TAG, "onFinishCreatingAlbum >>");

        String albumName = metAlbumName.getText().toString();
        String albumDescription = metAlbumDescription.getText().toString();

        Album newAlbum = new Album(mNewAlbumID, albumName, Calendar.getInstance().getTime());
        newAlbum.setM_Description(albumDescription);
        newAlbum.setM_NumOfPictures(mPictureAudioDataCollection.size());
        newAlbum.setM_Pictures(mPictureAudioDataCollection);
        //TODO: set audio

        mDBManager.addAlbumDetailsToDB(newAlbum, FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Return to home screen
        Intent homeIntent = new Intent(CreateAlbumActivity.this, HomeActivity.class);
        startActivity(homeIntent);
    }

    public void onAbortAlbumCreationClick(View v) {
        Log.e(TAG, "onAbortAlbumCreationClick >>");
        this.mDBManager.removeAlbumFromDB(this.mNewAlbumID, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                true, this.mPictureAudioDataCollection);
        finish();
    }
}
