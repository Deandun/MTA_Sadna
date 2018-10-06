package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.galbenabu1.classscanner.Activities.Enums.eShowCoursesOptions;
import com.example.galbenabu1.classscanner.Adapters.CoursesAdapter;
import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import Logic.Managers.LoggedInUserDetailsManager;
import Logic.Models.Course;
import Logic.Database.DBManager;
import Logic.Interfaces.MyConsumer;
import Logic.Interfaces.MyFunction;

public class ShowCoursesActivity extends Activity {

    private static final String TAG = "ShowCoursesActivity";
    private static final String SHOW_COURSES_OPTIONS = "show_courses_options";

    private Button mbtnSearch;
    private EditText metSearchedCourseName;

    private List<Course> mFetchedCourses = new ArrayList<>();
    private List<Course> mCoursesListToDisplay = new ArrayList<>();
    private RecyclerView mCoursesRecycleView;
    private CoursesAdapter mCoursesAdapter;
    private eShowCoursesOptions mShowCoursesOptions;

    private DBManager mDBManager = new DBManager();

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
        MyFunction<Course, Boolean> courseFilterFunction;
        // Set the way the courses are filtered.
        switch (this.mShowCoursesOptions) {
            case ShowCoursesTheCurrentUserIsIn:
                courseFilterFunction =
                        (course) -> {
                            boolean isCourseCreator = course.getCreatorID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            boolean isCourseMember = LoggedInUserDetailsManager.doesUserContainCourseID(course.getID());
                            // Return true if the user is the course creator or a member
                            return isCourseCreator || isCourseMember;
                        };
                this.fetchFilteredCourses(courseFilterFunction);
                break;
            case ShowSearchedCourses:
                courseFilterFunction = (course) -> true; // Fetch all courses - filter nothing.
                this.fetchFilteredCourses(courseFilterFunction);
                break;
            case ShowSuggestedCourses:
                this.fetchSuggestedCourses();
                break;
        }

    }

    private void onFinishedFetchingCouress(List<Course> courseList) {
        Log.e(TAG, "onFinishedFetchingCourses >>");
        this.mFetchedCourses.clear();
        this.mFetchedCourses.addAll(courseList);
        this.setUI();
    }

    // UI

    private void bindUI() {
        this.mbtnSearch = findViewById(R.id.btnSearchCourses);
        this.metSearchedCourseName = findViewById(R.id.etCourseNameToSearch);

        // Recycle view
        this.mCoursesRecycleView = findViewById(R.id.my_courses_recyclerView);
        this.mCoursesRecycleView.setHasFixedSize(true);
        this.mCoursesRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.mCoursesRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUI() {
        String searchedCourseName = this.metSearchedCourseName.getText().toString();

        this.mCoursesListToDisplay.clear();

        if (searchedCourseName.isEmpty()) {
            // Nothing searched. Display all fetched courses.
            this.mCoursesListToDisplay.addAll(this.mFetchedCourses);
        } else {
            filterCourses(searchedCourseName);
        }

        this.mCoursesRecycleView.getAdapter().notifyDataSetChanged(); // Call this function when UI changes need to occur
    }

    // Data

    private void initCoursesAdapter() {
        this.mCoursesListToDisplay.clear();
        this.mCoursesAdapter = new CoursesAdapter(this.mCoursesListToDisplay);
        this.mCoursesRecycleView.setAdapter(this.mCoursesAdapter);
    }

    private void fetchFilteredCourses(MyFunction<Course, Boolean> courseFilterFunction) {
        this.mDBManager.fetchFilteredCourses(courseFilterFunction, this::onFinishedFetchingCouress);
    }

    private void fetchSuggestedCourses() {
        this.mDBManager.fetchSuggestedCourses(this::onFinishedFetchingCouress);
    }

    private void filterCourses(String searchedCourseName) {

        for (Course course : this.mFetchedCourses) {
            if(course.getCourseName().contains(searchedCourseName)) {
                this.mCoursesListToDisplay.add(course);
            }
        }
    }

    public void onSearchBtnClick(View v) {
        Log.e(TAG, "onSearchClick >>");

        this.setUI();
    }

    public void onShowCoursesBackButtonClick(View v){
        finish();
    }
}