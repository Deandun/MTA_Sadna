package com.example.galbenabu1.classscanner.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import Logic.Managers.LoggedInUserDetailsManager;
import Logic.Models.Course;
import Logic.Database.DBManager;

public class CourseInfoActivity extends AppCompatActivity {

    private static final String SHOULD_SHOW_PRIVATE_ALBUMS_DATA = "should_show_private_albums"; // Send this flag to show albums activity so it will fetch shared albums.
    private static final String COURSE_ID_DATA = "course_id_data"; // Send the course ID to show albums activity to show the courses albums.
    private static final String COURSE_DATA = "course_data";
    private static final String TAG = "CourseInfoActivity";
    private static final String IS_SELECTING_ALBUMS = "is_selecting_albums";
    private final static int SELECT_ALBUMS_CODE = 100; // Code to identify that the user has selected album IDs in the returning intent

    private DBManager mDBManager = new DBManager();

    // Course info
    private Course mCourse;
    private TextView mtvCourseName;
    private TextView mtvCourseCreationDate;
    private TextView mtvCourseCreatorName;
    private Button mbtnAddCourseAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        Log.e(TAG, "onCreate >>");

        mCourse = getIntent().getExtras().getParcelable(COURSE_DATA);
        bindUI();
        setUI();

        Log.e(TAG, "onCreate <<");
    }

    // UI

    private void bindUI() {
        this.mtvCourseName = findViewById(R.id.tv_course_name);
        this.mtvCourseCreationDate = findViewById(R.id.tv_course_creation_date);
        this.mtvCourseCreatorName = findViewById(R.id.tv_course_publisher_name);
        this.mbtnAddCourseAlbum = findViewById(R.id.btnAddCourseAlbum);
    }

    private void setUI() {
        // Course info
        this.mtvCourseName.setText("Course name: " + mCourse.getCourseName());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(mCourse.getCreationDate());
        this.mtvCourseCreationDate.setText("Created at:" + dateStr);

        this.mtvCourseCreatorName.setText("Created by: " + mCourse.getCreatorName());
        this.setActionButtonUI();
    }

    private void setActionButtonUI() {
        // Add albums to course
        if (isUserJoinedCourse()){ //check if this user joined course
            this.mbtnAddCourseAlbum.setText("Add Albums");
        }else{
            this.mbtnAddCourseAlbum.setText("Join Course");
        }
    }

    public void onDisplayCourseAlbumsClick(View v){
        Log.e(TAG, "onShowCoursesClick >>");

        Intent showAlbumsIntent = new Intent(getApplicationContext(), ShowAlbumsActivity.class);
        showAlbumsIntent.putExtra(SHOULD_SHOW_PRIVATE_ALBUMS_DATA, false); // Should fetch shared albums and not private ones.
        showAlbumsIntent.putExtra(COURSE_ID_DATA, this.mCourse.getID());
        startActivity(showAlbumsIntent);

        Log.e(TAG, "onShowCoursesClick <<");
    }

    public void onCreateInfoBackButtonClick(View v){
        finish();
    }

    public void onActionButtonClick(View v) {
        Log.e(TAG, "onActionButtonClick >>");

        if (isUserJoinedCourse()){
            addAlbumsToExistCourse();
        }else{
            joinCourse();
        }

        Log.e(TAG, "onActionButtonClick <<");
    }
    private void joinCourse(){
        LoggedInUserDetailsManager.addCourseIDToUser(this.mCourse.getID());
        this.mDBManager.userJoinsCourse(LoggedInUserDetailsManager.getsLoggedInUser(), this.mCourse.getID());
        this.setActionButtonUI();
        Toast.makeText(this, "Successfully joined to " + this.mCourse.getCourseName() + " course!", Toast.LENGTH_SHORT).show();
    }

    private void addAlbumsToExistCourse(){
        List<String> albumsIdList = this.mCourse.getM_AlbumIds();
        if(albumsIdList != null) {
            albumsIdList.clear();
        }

        Intent chooseAlbumsIntent = new Intent(this.getApplicationContext(), ShowAlbumsActivity.class);
        chooseAlbumsIntent.putExtra(SHOULD_SHOW_PRIVATE_ALBUMS_DATA, true);
        chooseAlbumsIntent.putExtra(IS_SELECTING_ALBUMS, true);
        startActivityForResult(chooseAlbumsIntent, SELECT_ALBUMS_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult >>");

        String SELECTED_ALBUM_IDS_DATA = "selected_albums_data";

        if (requestCode == SELECT_ALBUMS_CODE && resultCode == RESULT_OK) {
            this.mCourse.getM_AlbumIds().addAll(data.getExtras().getStringArrayList(SELECTED_ALBUM_IDS_DATA));
            Log.e(TAG, "onActivityResult >> received album IDs: " + this.mCourse.getM_AlbumIds());
            this.mDBManager.addAlbumsToExistsCourse(this.mCourse);
            this.mDBManager.moveAlbumIDsFromPrivateToSharedHelper(this.mCourse.getID(), this.mCourse.getCreatorID(),
                    this.mCourse.getM_AlbumIds());
        }

        Log.e(TAG, "onActivityResult <<");
    }


    private boolean isUserJoinedCourse() {
        return this.mCourse.getCreatorID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ||
                LoggedInUserDetailsManager.getsLoggedInUser().getM_CourseIds().contains(this.mCourse.getID());
    }
}
