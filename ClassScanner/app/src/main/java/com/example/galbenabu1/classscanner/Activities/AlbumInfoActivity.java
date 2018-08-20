package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.galbenabu1.classscanner.Adapters.PhotoGalleryAdapter;
import com.example.galbenabu1.classscanner.R;

import java.util.ArrayList;
import java.util.Date;

import Logic.Album;
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

        for(int i = 0; i < 15; i++) {
            Date date = new Date();
            date.setTime(date.getTime() - timeToDecrease);
            timeToDecrease *= (i + 1);
            PictureAudioData photo = new PictureAudioData(Integer.toString(i), date, "dummy photo " + i, "dummy path");
            mAlbumPhotosList.add(photo);
        }
    }
}