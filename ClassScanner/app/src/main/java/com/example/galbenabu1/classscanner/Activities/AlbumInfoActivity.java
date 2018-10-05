package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.galbenabu1.classscanner.Adapters.PhotoGalleryAdapter;
import com.example.galbenabu1.classscanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import Logic.Album;
import Logic.Database.DBManager;
import Logic.PictureAudioData;

public class AlbumInfoActivity extends Activity {
    private static final String TAG = "AlbumInfoActivity";
    private static final String ALBUM_DATA = "album_data";

    private Album mAlbum;
    private ArrayList<PictureAudioData> mAlbumPhotosList = new ArrayList<PictureAudioData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);
        Log.e(TAG, "onCreate >>");

        mAlbum = getIntent().getExtras().getParcelable(ALBUM_DATA);
        Log.e(TAG, "onCreate >> album: " + mAlbum.toString());

        RecyclerView recyclerView = findViewById(R.id.image_gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        fetchPhotoListFromDB();
        PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(mAlbumPhotosList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        Log.e(TAG, "onCreate <<");
    }

    private void fetchPhotoListFromDB(){
        //TODO: fetch Photos from db, not the actual images.
        long timeToDecrease = 10000;
        
        ////


        DBManager tt = new DBManager();
        DatabaseReference xc =tt.getPrivateAlbumPictures(FirebaseAuth.getInstance().getCurrentUser().getUid(), mAlbum.getM_Id(), mAlbum.getM_Pictures().get(1).getM_Id());


        String picId= mAlbum.getM_Pictures().get(1).getM_Id();
        String albumId= mAlbum.getM_Id();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String albumPath= "Albums/privateAlbums/"+userId+"/"+albumId+"/"+picId+".jpeg";


        if (xc==null)
        {

        }


        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(xc.toString().substring(xc.toString().lastIndexOf("com") + 4));
String location = xc.toString().substring(xc.toString().lastIndexOf("com") + 4);

/////////////////

        FirebaseStorage storage;
        StorageReference ref;
        storage = FirebaseStorage.getInstance();
        ref = storage.getReference().child(albumPath);

        FirebaseAuth.getInstance().getCurrentUser().getUid();


        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                if (uri==null){

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        final File localFile;
        try {
            localFile = File.createTempFile("Images", "jpg");

            ;



            final StorageTask<FileDownloadTask.TaskSnapshot> taskSnapshotStorageTask = ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        /////////////////////
















        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                if(uri==null)
                {

                }
            }}
        );
        
        ///

        for(int i = 0; i < 15; i++) {
            Date date = new Date();
            date.setTime(date.getTime() - timeToDecrease);
            timeToDecrease *= (i + 1);
            PictureAudioData photo = new PictureAudioData(Integer.toString(i), date, "dummy photo " + i, location);
            mAlbumPhotosList.add(photo);
        }
    }
}