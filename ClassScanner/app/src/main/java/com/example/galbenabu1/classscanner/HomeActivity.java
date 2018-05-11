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
         //   mtvGreeting.setText("Hello " + userName);
        }
        Log.e(TAG, "onCreate <<");
    }

    //on click signature
    public void onSignoutClick(View v) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void onTmpTakePicClick(View v) {
        Intent intent = new Intent(HomeActivity.this, TakePicActivity.class);
        startActivity(intent);
    }
}
