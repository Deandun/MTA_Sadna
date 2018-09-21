package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.galbenabu1.classscanner.Activities.Enums.eShowCoursesOptions;
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
    private static final String SHOW_COURSES_OPTIONS = "show_courses_options";

    private List<Course> mCoursesList = new ArrayList<>();
    private RecyclerView mCoursesRecycleView;
    private CoursesAdapter mCoursesAdapter;
    private eShowCoursesOptions mShowCoursesOptions;

    private DBManager mDBManager = new DBManager();
    private MyFunction<Course, Boolean> mCourseFilterFunction;
    private MyConsumer<List<Course>> mOnFinishFetchingCourses = (fetchedCourseList) -> {
        Log.e(TAG, "Finished fetching courses for option: " + this.mShowCoursesOptions.name());
        this.mCoursesList.addAll(fetchedCourseList);
        this.setUI();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_courses);
        Log.e(TAG, "onCreate >>");
        this.mShowCoursesOptions = (eShowCoursesOptions) getIntent().getExtras().getSerializable(SHOW_COURSES_OPTIONS);

        this.bindUI();
        this.initCoursesAdapter();
        this.handleShowCoursesOption();

        Log.e(TAG, "onCreate <<");
    }

    private void handleShowCoursesOption() {
        this.mOnFinishFetchingCourses = (courseList) -> {
            Log.e(TAG, "onFinishedFetchingCourses >>");

            this.mCoursesList.addAll(courseList);
            this.setUI();
        };

       switch(this.mShowCoursesOptions) {
           case ShowCoursesTheCurrentUserIsIn:
               //TODO: figure out if the user is a member of the current course.
               this.mCourseFilterFunction =
                       (course) -> course.getCreatorID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid());
               break;
           case ShowSearchedCourses:
               this.mCourseFilterFunction = (course) -> true; // Show all courses - filter nothing.
       }

        this.fetchCourses();
    }

    // UI

    private void bindUI() {
        this.mCoursesRecycleView = findViewById(R.id.my_courses_recyclerView);
        this.mCoursesRecycleView.setHasFixedSize(true);
        this.mCoursesRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.mCoursesRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUI() {
        if(this.mShowCoursesOptions.equals(eShowCoursesOptions.ShowSearchedCourses)) {
            //TODO: set course filtering UI so the user can search for courses.
        }
        this.mCoursesRecycleView.getAdapter().notifyDataSetChanged(); // Call this function when UI changes need to occur
    }

    // Data

    private void initCoursesAdapter() {
        this.mCoursesList.clear();
        this.mCoursesAdapter = new CoursesAdapter(this.mCoursesList);
        this.mCoursesRecycleView.setAdapter(this.mCoursesAdapter);
    }

    private void fetchCourses(){
        // TODO: For search courses options - implement the filtering functionality and UI. When the search button is clicked - fetchCourses
        mDBManager.fetchFilteredCourses(this.mCourseFilterFunction, this.mOnFinishFetchingCourses);
    }
}
