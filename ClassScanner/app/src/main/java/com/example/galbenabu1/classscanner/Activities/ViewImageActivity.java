package com.example.galbenabu1.classscanner.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.galbenabu1.classscanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Logic.Database.DBManager;

import Logic.Models.Album;
import Logic.Services.CapturePhotoUtils;


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
    private String dbId;
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

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = imageView.getDrawingCache();
//                File root = Environment.getExternalStorageDirectory();
//                File file = new File(root.getAbsolutePath()+"/DCIM/Camera/img.jpg");
                try
                {
//                    file.createNewFile();
//                    FileOutputStream ostream = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "classImage" , "");
                   // CapturePhotoUtils.insertImage(getContentResolver(), bitmap, "classImage" , "");
                   //t ostream.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(imageView.getContext(), CropImageActivity.class);
                intent.putExtra("PATH", path);
                intent.putExtra("ALBUM",album);
                imageView.getContext().startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager dbmanager =new DBManager();
                String albumId=album.getM_Id();
                String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                String pictureDbId=dbId;
                //TODO: what's the '+ 7' for?
                String pictureId=path.substring(path.lastIndexOf("Images/") + 7);
                //todo: check if private album
                boolean isPrivateAlbum=true;
                dbmanager.removePictureFromDB(albumId,userId,pictureId,pictureDbId,isPrivateAlbum);

                album.deletePictureFromAlbum(Integer.getInteger(pictureDbId));

                Intent newIntent = new Intent(v.getContext(), AlbumInfoActivity.class);
                newIntent.putExtra("album_data", album);
                startActivity(newIntent);

            }
        });


    }



    private void getImageByPathAndBitmap() {
        String pictureId=path.substring(path.lastIndexOf("Images/") + 7);
        DBManager dbManager = new DBManager();
        dbManager.fetchImageFromStoragePath(pictureId,
        (bitmap) -> imageView.setImageBitmap(bitmap));
    }

    private void initViews() {

        btnDownload = (FloatingActionButton) findViewById(R.id.download_button);
        imageView = (ImageView) findViewById(R.id.picture_view);
        totalView = (ConstraintLayout) findViewById(R.id.total_view);
        btnDelete = (FloatingActionButton) findViewById(R.id.delete_button);
        btnEdit = (FloatingActionButton) findViewById(R.id.edit_button);

        if (getIntent().hasExtra("PATH")) {
            Bundle extras = getIntent().getExtras();
            path = extras.getString("PATH");
        }

        if (getIntent().hasExtra("ALBUM")) {
            Bundle extras = getIntent().getExtras();
            album = (Album)extras.getParcelable("ALBUM");
        }


        if (getIntent().hasExtra("DB_ID")) {
            Bundle extras = getIntent().getExtras();
            dbId = extras.getString("DB_ID");
        }

        if (!this.isUserTheCreator()) { //only creator user can edit/delete pictures
            hideCreatorOnlyButtons();
        }

    }

    private void hideCreatorOnlyButtons() {
        this.btnEdit.setVisibility(View.INVISIBLE);
        this.btnDelete.setVisibility(View.INVISIBLE);
    }

   private boolean isUserTheCreator() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid().equals(this.album.getM_AlbumCreatorId());
   }

}



