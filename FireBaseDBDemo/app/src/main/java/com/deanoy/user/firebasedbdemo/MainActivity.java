package com.deanoy.user.firebasedbdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.UploadTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnFailureListener;

public class MainActivity extends Activity {

    private DatabaseReference mDatabase;
    private ImageView imageView;
    private EditText mEditView;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference("images.jpg");
    private Boolean mShouldDisplayImage = false;

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

        // Read from the database
        mDatabase.child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Course course;
                Log.e("onDataChange", "Reading from DB...");
                for(DataSnapshot coursesSnapshot: dataSnapshot.getChildren()) {
                    course = coursesSnapshot.getValue(Course.class);
                    Log.e("onDataChange", "course points is: " + course.mCourseName);
                    mEditView.setText(course.getmCourseName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("onDBError", "Failed to read value.", error.toException());
            }
        });
    }

    private void uploadImage() {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        Log.e("onDataChange", "Reading from DB...");
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("onDataChange", "Failed reading from storage");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.e("onDataChange", "Successfully read " + downloadUrl.toString() + " from storage!");

                //Reading image but not displaying it. Possibly connected to imageView.setImageDrawable(null)
                if (mShouldDisplayImage) {
                    Log.e("onDataChange", "Display image");
                    imageView.setImageURI(null);
                    imageView.setImageURI(downloadUrl);
                } else {
                    imageView.setImageDrawable(null);
                    Log.e("onDataChange", "Remove image");
                }

                mShouldDisplayImage = !mShouldDisplayImage;
            }
        });
    }
}
