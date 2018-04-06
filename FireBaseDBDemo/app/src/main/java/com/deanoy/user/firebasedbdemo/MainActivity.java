package com.deanoy.user.firebasedbdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {

    private DatabaseReference mDatabase;
    private EditText mEditView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mEditView = findViewById(R.id.editView);

    }

    public void onClick(View v) {
        int coursePoints;
        Course course = new Course(4, mEditView.getText().toString());
        mDatabase.child("Courses").child(course.getmId()).setValue(course);

        // Read from the database - Doesn't work on current JSON structure in online DB
//        mDatabase.child("Courses").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                Course value = dataSnapshot.getValue(Course.class);
//                Log.d("onDataChange", "Value is: " + value.mCoursePoints);
//                mEditView.setText(Integer.toString(value.getmCoursePoints()));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("onDBError", "Failed to read value.", error.toException());
//            }
//        });
    }
}
