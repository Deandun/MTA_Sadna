package com.deanoy.user.firebasedbdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import com.google.firebase.storage.UploadTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends Activity {

    private DatabaseReference mDatabase;
    private ImageView imageView;
    private EditText mEditView;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference("images.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mEditView = findViewById(R.id.editView);
    }

    public void onClick(View v) {
        int coursePoints;
        Course course = new Course(4, mEditView.getText().toString());
        mDatabase.child("Courses").child(course.getmId()).setValue(course);
        this.uploadImage();

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

    private void uploadImage() {
        Log.e("uploadImage", "uploadImage 1>>");
        imageView.setDrawingCacheEnabled(true);
        Log.e("uploadImage", "uploadImage 2>>");

        imageView.buildDrawingCache();
        Log.e("uploadImage", "uploadImage 3>>");

        Bitmap bitmap = imageView.getDrawingCache();
        Log.e("uploadImage", "uploadImage 4>>");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Log.e("uploadImage", "uploadImage 5>>");

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        Log.e("uploadImage", "uploadImage 6>>");

        byte[] data = baos.toByteArray();
        Log.e("uploadImage", "Uploading the image now");
        UploadTask uploadTask = storageRef.putBytes(data);
    }
}
