package com.example.galbenabu1.classscanner;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyCoursesActivity extends Activity {

    private static final String TAG = "MyCoursesActivity";

    private List<Course> mCoursesList = new ArrayList<>();
    private RecyclerView mCoursesRecycleView;
    private CoursesAdapter mCoursesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        Log.e(TAG, "onCreate >>");

        bindUI();
        setUI();
        getCoursesFromDB();

        Log.e(TAG, "onCreate <<");
    }

    // UI

    private void bindUI() {
        mCoursesRecycleView = findViewById(R.id.recyclerView);
        mCoursesRecycleView.setHasFixedSize(true);
        mCoursesRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCoursesRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    // Call this function when UI changes need to occur
    private void setUI() {
        mCoursesRecycleView.getAdapter().notifyDataSetChanged();
    }

    // Data

    private void getCoursesFromDB() {
        mCoursesList.clear();
        mCoursesAdapter = new CoursesAdapter(mCoursesList);
        mCoursesRecycleView.setAdapter(mCoursesAdapter);

        getCoursesTemp(); // Need to get courses from the DB manager
        setUI();
    }

    //Temp method that creates dummy courses
    private void getCoursesTemp() {
        String namesArr[] = {"Jerry", "Tom", "Chen", "Noy Toy", "Tal Galya"};
        long timeToDecrease = 10000;
        for(int i = 0; i < 15; i++) {
            Course course = new Course();
            course.setCourseName("Dummy course" + i);
            course.setPublisherName(namesArr[i % namesArr.length]);
            Date date = new Date();
            date.setTime(date.getTime() - timeToDecrease);
            course.setmCreationDate(date);
            timeToDecrease *= (i + 1);
            Log.e(TAG, "Course created: " + course.toString());
            mCoursesList.add(course);
        }
    }
}
