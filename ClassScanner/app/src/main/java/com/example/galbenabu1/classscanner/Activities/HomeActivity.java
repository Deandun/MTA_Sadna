package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.galbenabu1.classscanner.Activities.Enums.eShowCoursesOptions;
import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import Logic.Managers.LoggedInUserDetailsManager;

public class HomeActivity extends Activity {

    private static final String SHOULD_SHOW_PRIVATE_ALBUMS_DATA = "should_show_private_albums";
    private static final String TAG = "HomeActivity";
    // Decides which courses will be displayed in the ShowCoursesActivity
    private static final String SHOW_COURSES_OPTIONS = "show_courses_options";
    private TextView mtvGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "onCreate >>");
        mtvGreeting = findViewById(R.id.tvGreeting);
        setContentView(R.layout.activity_home);
        Log.e(TAG, "onCreate <<");
    }

    public void onSignoutClick(View v) {
        Log.e(TAG, "onSignoutClick >>");
        FirebaseAuth.getInstance().signOut();
        Intent mainViewIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainViewIntent);
        Log.e(TAG, "onSignoutClick <<");
    }

    public void onTmpTakePicClick(View v) {
        Log.e(TAG, "onTmpTakePicClick >>");
        Intent intent = new Intent(HomeActivity.this, TakePicActivity.class);
        startActivity(intent);
        Log.e(TAG, "onTmpTakePicClick <<");
    }

    public void onCreateNewCourseClick(View v){
        Log.e(TAG, "onCreateNewCourseClick >>");

        Intent intent = new Intent(HomeActivity.this, CreateCourseActivity.class);
        startActivity(intent);

        Log.e(TAG, "onCreateNewCourseClick <<");
    }

    public void onViewMyCoursesClick(View v) {
        Log.e(TAG, "onViewMyCoursesClick >>");
        Intent intent = new Intent(HomeActivity.this, ShowCoursesActivity.class);
        intent.putExtra(SHOW_COURSES_OPTIONS, eShowCoursesOptions.ShowCoursesTheCurrentUserIsIn);
        startActivity(intent);
        Log.e(TAG, "onViewMyCoursesClick <<");
    }

    public void onShowPrivateAlbumsClick(View v) {
        Log.e(TAG, "onShowPrivateAlbumsClick >>");

        Intent intent = new Intent(HomeActivity.this, ShowAlbumsActivity.class);
        intent.putExtra(SHOULD_SHOW_PRIVATE_ALBUMS_DATA, true);
        startActivity(intent);

        Log.e(TAG, "onShowPrivateAlbumsClick <<");
    }

    public void onSearchCoursesClick(View v) {
        Log.e(TAG, "onSearchCoursesClick >>");

        Intent intent = new Intent(HomeActivity.this, ShowCoursesActivity.class);
        intent.putExtra(SHOW_COURSES_OPTIONS, eShowCoursesOptions.ShowSearchedCourses);
        startActivity(intent);

        Log.e(TAG, "onSearchCoursesClick <<");
    }

    public void onSuggestedCoursesClick(View v) {
        Log.e(TAG, "onSuggestedCoursesClick >>");

        Intent intent = new Intent(HomeActivity.this, ShowCoursesActivity.class);
        intent.putExtra(SHOW_COURSES_OPTIONS, eShowCoursesOptions.ShowSuggestedCourses);
        startActivity(intent);

        Log.e(TAG, "onSuggestedCoursesClick <<");
    }
}
