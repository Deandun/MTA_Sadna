package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.galbenabu1.classscanner.Adapters.CoursesAdapter;
import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Logic.Course;
import Logic.Database.DBManager;
import Logic.Interfaces.MyConsumer;

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
        this.mIsMyCourses = getIntent().getExtras().getBoolean(IS_MY_COURSES);

        bindUI();
        getCoursesFromDB();

        Log.e(TAG, "onCreate <<");
    }

    // UI

    private void bindUI() {
        this.mCoursesRecycleView = findViewById(R.id.my_courses_recyclerView);
        this.mCoursesRecycleView.setHasFixedSize(true);
        this.mCoursesRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.mCoursesRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUI() {
        this.mCoursesRecycleView.getAdapter().notifyDataSetChanged(); // Call this function when UI changes need to occur
    }

    // Data

    private void getCoursesFromDB() {
        this.mCoursesList.clear();
        this.mCoursesAdapter = new CoursesAdapter(this.mCoursesList);
        this.mCoursesRecycleView.setAdapter(this.mCoursesAdapter);

        //TODO: If mIsMyCourses, fetch only user's courses from DB. Else (showing all courses), fetch all courses
        fetchCourses();
        //getCoursesTemp(); //TODO: Need to get courses from the DB manager instead of using this temp function
    }

    private void fetchCourses(){
      //  if (mIsMyCourses){ //TODO
            DBManager dbManager = new DBManager();
            MyConsumer<List<Course>> onFinishFetchingCourses = (fetchedCourseList) -> {
                this.mCoursesList.addAll(fetchedCourseList);
                this.setUI();
            };
            //get all courses that relative to current user
            dbManager.fetchUserCoursesFromDB(FirebaseAuth.getInstance().getCurrentUser().getUid(), onFinishFetchingCourses);
        //}
    }

    //Temp method that creates dummy courses
    private void getCoursesTemp() {
        long timeToDecrease = 10000;
        for(int i = 0; i < 15; i++) {
            Date date = new Date();
            date.setTime(date.getTime() - timeToDecrease);
            timeToDecrease *= (i + 1);
            Course course = new Course("1", Integer.toString(i), "dummy course" + i, date);
            Log.e(TAG, "Course created: " + course.toString());
            mCoursesList.add(course);
        }
    }
}
