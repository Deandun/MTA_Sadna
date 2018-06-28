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
        String namesArr[] = {"Album1", "Album2", "Album3", "Album3", "Album4"};
        long timeToDecrease = 10000;
        for(int i = 0; i < 15; i++) {
            Album album = new Album();
            album.setAlbumName("Dummy Album" + i);
            album.setPublisherName(namesArr[i % namesArr.length]);
            Date date = new Date();
            date.setTime(date.getTime() - timeToDecrease);
            album.setmCreationDate(date);
            timeToDecrease *= (i + 1);
            Log.e(TAG, "Album created: " + album.toString());
            mAlbumsList.add(album);
        }
    }
}
