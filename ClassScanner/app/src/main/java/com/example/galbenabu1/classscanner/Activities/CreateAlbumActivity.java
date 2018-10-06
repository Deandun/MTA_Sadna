package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Logic.Managers.LoggedInUserDetailsManager;
import Logic.Models.Album;
import Logic.Database.DBManager;
import Logic.Models.PictureAudioData;

public class CreateAlbumActivity extends Activity {

    private final static String TAG = "CreateAlbumActivity";
    private final static String NEW_ALBUM_PICTURE_AUDIO_DATA = "new_album_picture_audio_data";
    private final static String NEW_ALBUM_ID = "new_album_id";

    // New album data
    private List<PictureAudioData> mPictureAudioDataCollection;
    private String mNewAlbumID;

    // UI.
    private EditText mAlbumCreator;
    private EditText mAlbumCreatoionDate;
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
        this.setUI();
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
        this.mAlbumCreatoionDate = findViewById(R.id.etAlbumDate);
        this.mAlbumCreator = findViewById(R.id.etAlbumCreatorName);
    }

    private void setUI() {
        this.mAlbumCreator.setText(LoggedInUserDetailsManager.getsLoggedInUser().getM_UserName());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        this.mAlbumCreatoionDate.setText(dateFormat.format(date));
    }

    public void onFinishCreatingAlbumClick(View v) {
        Log.e(TAG, "onFinishCreatingAlbum >>");

        String albumName = metAlbumName.getText().toString();
        String albumDescription = metAlbumDescription.getText().toString();
        String albumCreatorName = this.mAlbumCreator.getText().toString();

        Album newAlbum = new Album(mNewAlbumID, albumName, new Date(), albumCreatorName);

        newAlbum.setM_Description(albumDescription);
        newAlbum.setM_NumOfPictures(mPictureAudioDataCollection.size());
        newAlbum.setM_Pictures(mPictureAudioDataCollection);

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
