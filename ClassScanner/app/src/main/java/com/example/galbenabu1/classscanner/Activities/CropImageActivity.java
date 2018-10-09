package com.example.galbenabu1.classscanner.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Matrix;
import com.example.galbenabu1.classscanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import com.fenchtose.nocropper.CropperView;

import Logic.Models.Album;


/**
 * Created by galbenabu1 on 25/08/2018.
 */


public class CropImageActivity extends AppCompatActivity {

    private Button btnCrop;
    private Button btnToggleGesture;
    private Button continueEditingBtn;
    private ImageView btnSnap;
    private ImageView btnRotate;
    private CropperView cropperView;
    private Bitmap mBitmap;
    private String path;
    private Album album;
    private boolean isSnappedtoCenter = false;
    private FirebaseStorage storage;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        initViews();

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference().child(path);

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
                mBitmap=cropperView.getCroppedBitmap();
                cropperView.setImageBitmap(rotateBitmap(mBitmap, 0));
                mBitmap=cropperView.getCroppedBitmap();
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
                    mBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    cropperView.setImageBitmap(mBitmap);

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
        matrix.postRotate(90);
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
        continueEditingBtn = (Button) findViewById(R.id.continueEditingBtn);
        btnSnap = (ImageView) findViewById(R.id.snap_button);
        btnRotate = (ImageView) findViewById(R.id.rotate_button);
        cropperView = (CropperView) findViewById(R.id.imageView1);

        if (getIntent().hasExtra("PATH")) {
            Bundle extras = getIntent().getExtras();
            path = extras.getString("PATH");
        }

        if (getIntent().hasExtra("ALBUM")) {
            Bundle extras = getIntent().getExtras();
            album = (Album)extras.getParcelable("ALBUM");
        }
    }

    public void onContinueEditingBtnClicked(View v)
    {
        Intent intent = new Intent(CropImageActivity.this, ImageEditingActivity.class);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
        intent.putExtra("IMAGE", bs.toByteArray());
        intent.putExtra("ALBUM", album);
        intent.putExtra("PATH", path);
        startActivity(intent);
    }
}



