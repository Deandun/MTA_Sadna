package com.example.galbenabu1.classscanner.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Matrix;

import com.example.galbenabu1.classscanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by galbenabu1 on 25/08/2018.
 */


import com.fenchtose.nocropper.CropperView;


public class CropImageActivity extends AppCompatActivity {

    Button btnCrop, btnToggleGesture;
    ImageView btnSnap, btnRotate;
    CropperView cropperView;
    Bitmap mBitmap;
    boolean isSnappedtoCenter = false;
    private FirebaseStorage storage;
    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        initViews();
        storage = FirebaseStorage.getInstance();
        ref = storage.getReference().child("Albums/DummyAlbum/finger-frame-15923929.jpg");

        getImageByPathAndBitmap();

        btnToggleGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean enabled = cropperView.isGestureEnabled();
                enabled = !enabled;
                cropperView.setGestureEnabled(enabled);
                Toast.makeText(getBaseContext(), "Gesture : " + (enabled ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
            }
        });

        btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSnappedtoCenter)
                    cropperView.cropToCenter();
                else
                    cropperView.fitToCenter();
                isSnappedtoCenter = !isSnappedtoCenter;

            }
        });

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setImageBitmap(rotateBitmap(mBitmap, 90));
            }
        });
    }

    private void getImageByPathAndBitmap()
    {
        try {
            final File localFile = File.createTempFile("Images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    cropperView.setImageBitmap(my_image);

                    btnCrop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cropImage();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap rotateBitmap(Bitmap mBitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
    }

    private void cropImage() {
        mBitmap = cropperView.getCroppedBitmap();
        if (mBitmap != null)
            cropperView.setImageBitmap(mBitmap);
    }

    private void initViews() {
        btnCrop = (Button) findViewById(R.id.btnCrop);
        btnToggleGesture = (Button) findViewById(R.id.btnToggleGesture);
        btnSnap = (ImageView) findViewById(R.id.snap_button);
        btnRotate = (ImageView) findViewById(R.id.rotate_button);
        cropperView = (CropperView) findViewById(R.id.imageView1);
    }
}


//
//    private ImageView imageView;
//    private Button gallery;
//    private static final String TAG = "CropImageActivity";
//    //private Toolbar toolbar;
//    private Uri uri;
//    private Intent CamIntent, GalIntent, CropIntent;
//    private final int RequestPermissionCode = 1;
//    private DisplayMetrics displayMetrics;
//    private int width, height;
//    //private FirebaseDatabase database = FirebaseDatabase.getInstance();
//    //private DatabaseReference refToImages = FirebaseStorage.getReference("images");
//    //final StorageReference photoRef = refToImages.child(selectedImageUri.getLastPathSegment());
//    private FirebaseApp app;
//    private FirebaseStorage storage;
//    private StorageReference storageRef;
//    private static final int REQUEST_CAMERA_PERMISSION_RESULT = 0;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_crop_image);
//
//        imageView = (ImageView) findViewById(R.id.imageView);
//        gallery = (Button) findViewById(R.id.btn_gallery);
//
//        app = FirebaseApp.getInstance();
////        database = FirebaseDatabase.getInstance(app);
//        storage = FirebaseStorage.getInstance(app);
////        databaseRef = database.getReference("PrivateAlbums");
//
//    }
//
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.cropping_menu,menu);
////        return true;
////    }
//
//
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
//////        if(item.getItemId() == R.id.btn_camera)
//////            CameraOpen();
////        if(item.getItemId() == R.id.btn_gallery)
////            //GalleryOpen();
////        return true;
////    }
//
//    //we need to open the storage of the current class of this course
//    public void onOpenGalleryClicked(View v) {
//        Log.e(TAG, "onOpenGalleryClicked >>");
//        openCropingTest();
//    }
//
//    private void openCropingTest() {
//        Log.e(TAG, "openCropingTest >> before get ref");
//        storageRef = storage.getReference();
//        Log.e(TAG, "openCropingTest >> after get ref");
//        StorageReference spaceRef = storageRef.child("Albums/DummyAlbum/finger-frame-15923929.jpg");
//        Log.e(TAG, "openCropingTest >> after spaceRef");
//        CropImage(Uri.parse(storageRef.getPath()));
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        Uri selectedImageUri = data.getData();
////        storageRef = storage.getReference("images");
////        final StorageReference photoRef = storageRef.child(selectedImageUri.getLastPathSegment());
//
//        storageRef = storage.getReference();
//        StorageReference spaceRef = storageRef.child("Albums/DummyAlbum/finger-frame-15923929.jpg");
//        CropImage(spaceRef.getDownloadUrl().getResult());
//    }
//
//    private void CropImage(Uri image) {
//        try {
//
//            Log.e(TAG, "CropImage >>");
//            CropIntent = new Intent("com.android.camera.action.CROP");
//            CropIntent.setDataAndType(image, "image/*");
//            CropIntent.putExtra("crop", "true");
//            CropIntent.putExtra("outputX", 180);
//            CropIntent.putExtra("outputY", 180);
//            CropIntent.putExtra("aspectX", 3);
//            CropIntent.putExtra("aspectY", 4);
//            CropIntent.putExtra("scaleUpIfNeeded", true);
//            CropIntent.putExtra("return-data", true);
//            Log.e(TAG, "CropImage >> before start activity");
//            startActivityForResult(CropIntent, 1);
//        } catch (ActivityNotFoundException ex) {
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CAMERA_PERMISSION_RESULT) {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getApplicationContext(), "App cannot run without camera services", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


