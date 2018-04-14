package com.example.galbenabu1.classscanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = "UploadActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //declare variables
    private ImageView ivImage;
    private Button btnUpload,btnNext,btnBack;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_layout);
        ivImage = (ImageView) findViewById(R.id.ivPic);
        btnBack = (Button) findViewById(R.id.btnBackImage);
        btnNext = (Button) findViewById(R.id.btnNextImage);
        btnUpload = (Button) findViewById(R.id.btnUploadImage);

        mStorageRef = FirebaseStorage.getInstance().getReference("images.jpg");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Log.d(TAG, "onClick: Back an Image.");
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Log.d(TAG, "onClick: Next Image.");
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: Uploading Image.");
                uploadImage();
            }
        });

    }

    // Temp code, will be replaced with DeaNoy's Camera2 API functionality in a different activity
    public void onTakePictureClick(View v) {
        Intent takePictureIntent = new Intent(UploadActivity.this, TakePicActivity.class);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Retrieve the captured image
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            byte[] imageBytes = (byte[]) extras.getByteArray("data");

            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ivImage.setImageBitmap(imageBitmap);
        }
    }
    private void uploadImage() {
        //Prepare image to be uploaded
        Log.e(TAG, "Preparing image for upload");
        ivImage.setDrawingCacheEnabled(true);
        ivImage.buildDrawingCache();
        Bitmap bitmap = ivImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload image
        Log.e(TAG, "Uploading image...");
        UploadTask uploadTask = mStorageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e(TAG, "Failed reading from storage.");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.e(TAG, "Successfully read " + downloadUrl.toString() + " from storage!");
                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}