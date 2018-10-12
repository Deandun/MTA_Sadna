package com.example.galbenabu1.classscanner.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.galbenabu1.classscanner.R;
import com.fenchtose.nocropper.CropperView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import Logic.Models.Album;


/**
 * Created by chen capon on 11/10/2018.
 */


public class ViewImageActivity extends AppCompatActivity {

    private FloatingActionButton btnDownload;
    private FloatingActionButton btnEdit;
    private FloatingActionButton btnDelete;
    private ConstraintLayout totalView;
    private ImageView imageView;
    private Bitmap mBitmap;
    private String path;
    private Album album;

    private FirebaseStorage storage;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        initViews();

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference().child(path);

        getImageByPathAndBitmap();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(imageView.getContext(), CropImageActivity.class);
                String strName = null;
                intent.putExtra("PATH", path);
                intent.putExtra("ALBUM",album);
                imageView.getContext().startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(imageView.getContext(), CropImageActivity.class);
                String strName = null;
                intent.putExtra("PATH", path);
                intent.putExtra("ALBUM",album);
                imageView.getContext().startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
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
                    imageView.setImageBitmap(mBitmap);


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

    private void initViews() {
        btnDelete = (FloatingActionButton) findViewById(R.id.delete_button);
        btnDownload = (FloatingActionButton) findViewById(R.id.download_button);
        btnEdit = (FloatingActionButton) findViewById(R.id.edit_button);

        imageView = (ImageView) findViewById(R.id.picture_view);
        totalView = (ConstraintLayout) findViewById(R.id.total_view);

        if (getIntent().hasExtra("PATH")) {
            Bundle extras = getIntent().getExtras();
            path = extras.getString("PATH");
        }

        if (getIntent().hasExtra("ALBUM")) {
            Bundle extras = getIntent().getExtras();
            album = (Album)extras.getParcelable("ALBUM");
        }
    }

}



