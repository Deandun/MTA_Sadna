package com.example.galbenabu1.classscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseInfoActivity extends AppCompatActivity {

    private static final String TAG = "CourseInfoActivity";
    private static final String COURSE_DATA = "course_data";

    // Course info
    Course mCourse = new Course();
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
        mtvCourseCreationDate.setText("Created at:" + mCourse.getmCreationDate().toString());
        mtvCourseCreaterName.setText("Created by: " + mCourse.getPublisherName());
    }

    public void onShowMyAlbumsClick(View v) {
        Log.e(TAG, "onShowCoursesClick >>");

        //TODO: send parameters to ShowAlbumsActivity to indicate we want it to show this course's albums
        Intent showAlbumsIntent = new Intent(getApplicationContext(), ShowAlbumsActivity.class);
        startActivity(showAlbumsIntent);

        Log.e(TAG, "onShowCoursesClick <<");
    }
}
