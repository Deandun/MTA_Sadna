package com.example.galbenabu1.classscanner.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.galbenabu1.classscanner.R;

import Logic.Course;

public class CourseInfoActivity extends AppCompatActivity {

    private static final String SHOULD_SHOW_PRIVATE_ALBUMS_DATA = "should_show_private_albums"; // Send this flag to show albums activity so it will fetch shared albums.
    private static final String COURSE_ID_DATA = "course_id_data"; // Send the course ID to show albums activity to show the courses albums.
    private static final String TAG = "CourseInfoActivity";
    private static final String COURSE_DATA = "course_data";

    // Course info
    Course mCourse;
    private TextView mtvCourseName;
    private TextView mtvCourseCreationDate;
    private TextView mtvCourseCreaterName;

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
        mtvCourseName = findViewById(R.id.tv_course_name);
        mtvCourseCreationDate = findViewById(R.id.tv_course_creation_date);
        mtvCourseCreaterName = findViewById(R.id.tv_course_publisher_name);
    }

    private void setUI() {
        // Course info
        mtvCourseName.setText(mCourse.getCourseName());
        mtvCourseCreationDate.setText("Created at:" + mCourse.getCreationDate());
        mtvCourseCreaterName.setText("Created by: " + mCourse.getCourseName());
    }

    public void onShowMyAlbumsClick(View v) {
        Log.e(TAG, "onShowCoursesClick >>");

        //TODO: send parameters to ShowAlbumsActivity to indicate we want it to show this course's albums

        Intent showAlbumsIntent = new Intent(getApplicationContext(), ShowAlbumsActivity.class);
        showAlbumsIntent.putExtra(SHOULD_SHOW_PRIVATE_ALBUMS_DATA, false); // Should fetch shared albums and not private ones.
        showAlbumsIntent.putExtra(COURSE_ID_DATA, this.mCourse.getID());
        startActivity(showAlbumsIntent);

        Log.e(TAG, "onShowCoursesClick <<");
    }
}
