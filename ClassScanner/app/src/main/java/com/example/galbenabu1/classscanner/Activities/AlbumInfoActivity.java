package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.galbenabu1.classscanner.Adapters.PhotoGalleryAdapter;
import com.example.galbenabu1.classscanner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import Logic.Models.Album;
import Logic.Models.PictureAudioData;

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
        long timeToDecrease = 10000;

        /////TODO: choose where to delete the folder

        String DirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        DirectoryPath += "/classScanner";
//        File dir = new File(DirectoryPath);
//        if (dir.isDirectory())
//        {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(dir, children[i]).delete();
//            }
//        }


        for(int i = 0; i < mAlbum.getM_Pictures().size(); i++) {
            Date date = new Date();
            date.setTime(date.getTime() - timeToDecrease);
            timeToDecrease *= (i + 1);
            PictureAudioData photo = new PictureAudioData(Integer.toString(i), date, "dummy photo " + i, "Images/"+mAlbum.getM_Pictures().get(i).getM_Id());
            mAlbumPhotosList.add(photo);
        }
    }

    public void onBackBtnClick(View v){
        //TODO: implement
    }
}