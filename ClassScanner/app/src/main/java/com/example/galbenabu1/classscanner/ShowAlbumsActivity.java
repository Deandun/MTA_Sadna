package com.example.galbenabu1.classscanner;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Logic.Album;

public class ShowAlbumsActivity extends Activity {

    private static final String TAG = "ShowAlbumsActivity";

    private List<Album> mAlbumsList = new ArrayList<>();
    private RecyclerView mAlbumsRecycleView;
    private AlbumsAdapter mAlbumssAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_albums);
        Log.e(TAG, "onCreate >>");

        bindUI();
        getAlbumsFromDB();

        Log.e(TAG, "onCreate <<");
    }

    // UI

    private void bindUI() {
        mAlbumsRecycleView = findViewById(R.id.course_info_recyclerView);
        mAlbumsRecycleView.setHasFixedSize(true);
        mAlbumsRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAlbumsRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUI() {
        // Album Recycler view
        mAlbumsRecycleView.getAdapter().notifyDataSetChanged();
    }

    // Data

    private void getAlbumsFromDB() {
        mAlbumsList.clear();
        mAlbumssAdapter = new AlbumsAdapter(mAlbumsList);
        mAlbumsRecycleView.setAdapter(mAlbumssAdapter);

        getAlbumsTemp(); // Need to get albums from the DB manager
        setUI();
    }

    //Temp method that creates dummy courses
    private void getAlbumsTemp() {
        long timeToDecrease = 10000;
        for(int i = 0; i < 15; i++) {
            Date date = new Date();
            date.setTime(date.getTime() - timeToDecrease);
            timeToDecrease *= (i + 1);
            Album album = new Album(Integer.toString(i), "dummy album " + i, date.toString());
            Log.e(TAG, "Album created: " + album.toString());
            mAlbumsList.add(album);
        }
    }
}
