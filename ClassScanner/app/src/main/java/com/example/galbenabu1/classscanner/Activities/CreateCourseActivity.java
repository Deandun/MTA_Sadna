package com.example.galbenabu1.classscanner.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Logic.Album;
import Logic.Course;
import Logic.Database.DBManager;

public class CreateCourseActivity extends AppCompatActivity {

    private final static String TAG = "CreateCourseActivity";

    private static final String SHOULD_SHOW_PRIVATE_ALBUMS_DATA = "should_show_private_albums"; // Showing private albums if true, shared albums if false.
    private static final String IS_SELECTING_ALBUMS_FOR_COURSE = "is_selecting_albums"; // In an album selecting mode. Returns selected albums to previous activity.
    private static final String SELECTED_ALBUM_IDS_DATA = "selected_albums_data"; // The selected albums that return from show albums activity.
    private final static String NEW_ALBUM_DATA = "new_album_data";
    private final static int SELECT_ALBUMS_CODE = 100; // Code to identify that the user has selected album IDs in the returning intent

    private List<String> mAlbumIDCollection;

    // UI
    private EditText metCourseName;
    private EditText metCreatorName;
    private EditText metCreateDate;
    private EditText metCourseDescription;
    private DBManager mDBManager = new DBManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        this.getAlbumAndCourseID();
        this.bindUI();
        this.setUI();
    }

    private void setUI() {
        this.metCreatorName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    private void getAlbumAndCourseID() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //this.mAlbumsCollection = (List<Album>) extras.get(NEW_ALBUM_DATA);
    }

    private void bindUI(){
        this.metCourseName = findViewById(R.id.etCreateCourseName);
        this.metCourseDescription = findViewById(R.id.etCreateCouresDescription);
        this.metCreateDate = findViewById(R.id.etDate);
        this.metCreatorName = findViewById(R.id.etCreatorName);
    }


    public void onFinishCreatingCourseClick(View v) {
        Log.e(TAG, "onFinishCreatingCourse >>");

        Course newCourse = this.createNewCourse();

        this.mDBManager.addNewCourseDetailsToDBAndSetCourseID(newCourse);
        // Return to home screen
        Intent homeIntent = new Intent(CreateCourseActivity.this, HomeActivity.class);
        startActivity(homeIntent);

        Log.e(TAG, "onFinishCreatingCourse <<");

    }

    public void onChooseAlbumsClick(View v) {
        Log.e(TAG, "onChooseAlbumsClick >>");

        if(this.mAlbumIDCollection != null) {
            this.mAlbumIDCollection.clear(); // Reset previous albums collection before selecting new albums.
        }

        Intent chooseAlbumsIntent = new Intent(this.getApplicationContext(), ShowAlbumsActivity.class);
        chooseAlbumsIntent.putExtra(IS_SELECTING_ALBUMS_FOR_COURSE, true);
        chooseAlbumsIntent.putExtra(SHOULD_SHOW_PRIVATE_ALBUMS_DATA, true);
        startActivityForResult(chooseAlbumsIntent, SELECT_ALBUMS_CODE);

        Log.e(TAG, "onChooseAlbumsClick <<");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult >>");

        if(requestCode == SELECT_ALBUMS_CODE && resultCode == RESULT_OK) {
            this.mAlbumIDCollection = data.getExtras().getStringArrayList(SELECTED_ALBUM_IDS_DATA);
            Log.e(TAG, "onActivityResult >> received album IDs: " + mAlbumIDCollection);
        }

        Log.e(TAG, "onActivityResult <<");
    }

    private Course createNewCourse() {
        Course newCourse = new Course();

        String courseName = this.metCourseName.getText().toString();
        String courserDescription = this.metCourseDescription.getText().toString();
        String creatorID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //TODO: get user name from Current User.
        String courseCreator = this.metCreatorName.getText().toString();

        newCourse.setCreationDate(Calendar.getInstance().getTime());
        newCourse.setDescription(courserDescription);
        newCourse.setCourseName(courseName);
        newCourse.setCreatorName(courseCreator);
        newCourse.setCreatorID(creatorID);
        newCourse.setM_AlbumIds(this.mAlbumIDCollection != null ? this.mAlbumIDCollection : new ArrayList<>());

        return newCourse;
    }
}