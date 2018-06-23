package com.example.galbenabu1.classscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity";
    private static final String IS_MY_COURSES = "is_my_courses";

    private TextView mtvGreeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "onCreate >>");
        mtvGreeting = findViewById(R.id.tvGreeting);
        setContentView(R.layout.activity_home);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            //mtvGreeting.setText("Hello " + userName);
        }

        Log.e(TAG, "onCreate <<");
    }

    public void onSignoutClick(View v) {
        Log.e(TAG, "onSignoutClick >>");
        FirebaseAuth.getInstance().signOut();
        finish();
        Log.e(TAG, "onSignoutClick <<");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG, "onBackPressed >>");
        FirebaseAuth.getInstance().signOut();
        finish();
        Log.e(TAG, "onBackPressed <<");
    }

    public void onTmpTakePicClick(View v) {
        Log.e(TAG, "onTmpTakePicClick >>");
        Intent intent = new Intent(HomeActivity.this, TakePicActivity.class);
        startActivity(intent);
        Log.e(TAG, "onTmpTakePicClick <<");
    }

    public void onViewMyCoursesClick(View v) {
        Log.e(TAG, "onViewMyCoursesClick >>");
        Intent intent = new Intent(HomeActivity.this, ShowCoursesActivity.class);
        intent.putExtra(IS_MY_COURSES, true);
        startActivity(intent);
        Log.e(TAG, "onViewMyCoursesClick <<");
    }

    public void onShowPrivateAlbumsClick(View v) {
        Log.e(TAG, "onShowPrivateAlbumsClick >>");
        //TODO: indicate to ShowAlbumsActivity that we want to display the user's private albums only
        Intent intent = new Intent(HomeActivity.this, ShowAlbumsActivity.class);
        startActivity(intent);
        Log.e(TAG, "onShowPrivateAlbumsClick <<");
    }
}
