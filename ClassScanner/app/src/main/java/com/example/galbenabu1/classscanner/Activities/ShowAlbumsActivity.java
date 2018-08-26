package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.galbenabu1.classscanner.Adapters.AlbumsAdapter;
import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import Logic.Album;
import Logic.Database.DBManager;
import Logic.Interfaces.MyConsumer;

public class ShowAlbumsActivity extends Activity {

    // If showing shared albums, need course ID to fetch them from DB.
    private static final String COURSE_ID_DATA = "course_id_data";
    private static final String SHOULD_SHOW_PRIVATE_ALBUMS_DATA = "should_show_private_albums";

    private static final String TAG = "ShowAlbumsActivity";

    private List<Album> mAlbumsList = new ArrayList<>();
    private RecyclerView mAlbumsRecycleView;
    private AlbumsAdapter mAlbumssAdapter;
    private boolean mShouldShowPrivateAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_albums);
        Log.e(TAG, "onCreate >>");

        mShouldShowPrivateAlbums = getIntent().getExtras().getBoolean(SHOULD_SHOW_PRIVATE_ALBUMS_DATA);
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

        fetchAlbumsFromDB();
    }

    private void fetchAlbumsFromDB() {
        DBManager dbManager = new DBManager();
        MyConsumer<List<Album>> onFinishFetchingAlbums = (fetchedAlbumList) -> {
            this.mAlbumsList.addAll(fetchedAlbumList);
            this.setUI();
        };

        if(mShouldShowPrivateAlbums) {
            dbManager.fetchUserPrivateAlbumsFromDB(FirebaseAuth.getInstance().getCurrentUser().getUid(), onFinishFetchingAlbums);
        } else {
            String courseID = getIntent().getExtras().getString(COURSE_ID_DATA);
            dbManager.fetchCourseSharedAlbumsFromDB(courseID, onFinishFetchingAlbums);
        }
    }
}
