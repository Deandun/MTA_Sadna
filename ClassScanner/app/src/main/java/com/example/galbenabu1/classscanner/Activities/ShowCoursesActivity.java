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
import Logic.Interfaces.MyFunction;

public class ShowCoursesActivity extends Activity {

    private static final String TAG = "ShowCoursesActivity";
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
        this.fetchCourses();
    }

    private void fetchCourses(){
        DBManager dbManager = new DBManager();
        MyFunction<Course, Boolean> courseFilterFunction;
        MyConsumer<List<Course>> onFinishFetchingCourses = (fetchedCourseList) -> {
            Log.e(TAG, "Finished fetching courses: ");
            this.mCoursesList.addAll(fetchedCourseList);
            this.setUI();
        };

        if (mIsMyCourses){
            // Only get courses that the current user is in.
            // TODO: figure out how to determine if the user is in a course. Check course for user IDs or check the user for CourseIDs
            courseFilterFunction =
                    (course) -> course.getCreatorID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid());

            dbManager.fetchFilteredCourses(courseFilterFunction, onFinishFetchingCourses);
        } else {
            courseFilterFunction = (course) -> true; // Return all courses
            //TODO: For now, we fetch all courses. In the future, add filtering abilities (filter courses by name/date/etc... decide as a team).
            dbManager.fetchFilteredCourses(courseFilterFunction, onFinishFetchingCourses);
        }
    }
}
