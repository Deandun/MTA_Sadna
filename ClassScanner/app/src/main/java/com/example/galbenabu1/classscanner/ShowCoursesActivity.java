package com.example.galbenabu1.classscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Logic.Course;

public class ShowCoursesActivity extends Activity {

    private static final String TAG = "MyCoursesActivity";
    private static final String IS_MY_COURSES = "is_my_courses";

    private List<Course> mCoursesList = new ArrayList<>();
    private RecyclerView mCoursesRecycleView;
    private CoursesAdapter mCoursesAdapter;
    private boolean mIsMyCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_courses);
        Log.e(TAG, "onCreate >>");
        mIsMyCourses = getIntent().getExtras().getBoolean(IS_MY_COURSES);

        bindUI();
        getCoursesFromDB();

        Log.e(TAG, "onCreate <<");
    }

    // UI

    private void bindUI() {
        mCoursesRecycleView = findViewById(R.id.my_courses_recyclerView);
        mCoursesRecycleView.setHasFixedSize(true);
        mCoursesRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCoursesRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUI() {
        //TODO: If mIsMyCourses, do not allow filtering by creater name. Else, allow it
        mCoursesRecycleView.getAdapter().notifyDataSetChanged(); // Call this function when UI changes need to occur
    }

    // Data

    private void getCoursesFromDB() {
        mCoursesList.clear();
        mCoursesAdapter = new CoursesAdapter(mCoursesList);
        mCoursesRecycleView.setAdapter(mCoursesAdapter);

        //TODO: If mIsMyCourses, fetch only user's courses from DB. Else (showing all courses), fetch all courses
        getCoursesTemp(); //TODO: Need to get courses from the DB manager instead of using this temp function
        setUI();
    }

    //Temp method that creates dummy courses
    private void getCoursesTemp() {
        long timeToDecrease = 10000;
        for(int i = 0; i < 15; i++) {
            Date date = new Date();
            date.setTime(date.getTime() - timeToDecrease);
            timeToDecrease *= (i + 1);
            Course course = new Course(Integer.toString(i), "dummy course" + i, date);
            Log.e(TAG, "Course created: " + course.toString());
            mCoursesList.add(course);
        }
    }
}
