package com.example.galbenabu1.classscanner.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.galbenabu1.classscanner.Adapters.AlbumsAdapter;
import com.example.galbenabu1.classscanner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Logic.Album;
import Logic.Database.DBManager;
import Logic.Interfaces.MyConsumer;

public class ShowAlbumsActivity extends Activity {

    // If showing shared albums, need course ID to fetch them from DB.
    private static final String COURSE_ID_DATA = "course_id_data"; // Course ID for fetching course albums (only relevant if shouldShowPrivateAlbums is false.
    private static final String SHOULD_SHOW_PRIVATE_ALBUMS_DATA = "should_show_private_albums"; // Showing private albums if true, shared albums if false.
    private static final String IS_SELECTING_ALBUMS = "is_selecting_albums"; // In an album selecting mode. Returns selected albums to previous activity.
    private static final String SELECTED_ALBUMS_DATA = "selected_albums_data";

    private static final String TAG = "ShowAlbumsActivity";

    private List<Album> mAlbumsList = new ArrayList<>();
    private Set<String> mSelectedAlbumIDsSet = new HashSet<>();
    private RecyclerView mAlbumsRecycleView;
    private AlbumsAdapter mAlbumssAdapter;
    private boolean mShouldShowPrivateAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_albums);
        Log.e(TAG, "onCreate >>");

        boolean isUserSelectingAlbumsForPreviousActivity = getIntent().getExtras().getBoolean(IS_SELECTING_ALBUMS);

        if(isUserSelectingAlbumsForPreviousActivity) {
            Button btnFinishedSelectingAlbums = findViewById(R.id.btnFinishSelectingAlbums);
            btnFinishedSelectingAlbums.setOnClickListener(this::onFinishSelectingAlbumsClick);
        }

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
        mAlbumssAdapter = new AlbumsAdapter(mAlbumsList, this::onItemLongClick);
        mAlbumsRecycleView.setAdapter(mAlbumssAdapter);
        fetchAlbumsFromDB();
    }

    private void fetchAlbumsFromDB() {
        Log.e(TAG, "fetchAlbumsFromDB >> Showing private albums: " + mShouldShowPrivateAlbums);

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

    public void onFinishSelectingAlbumsClick(View v) {
        Log.e(TAG, "onFinishSelectingAlbumsClick >> ");
        Intent resultIntent = new Intent();
        ArrayList<String> albumIDsList = new ArrayList<>(this.mSelectedAlbumIDsSet); // Convert set to list so it can be sent back to the previous activity.

        resultIntent.putStringArrayListExtra(SELECTED_ALBUMS_DATA, albumIDsList);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void onItemLongClick(Album selectedAlbum) {
        Log.e(TAG, "onItemLongClick >> For album with ID: " + selectedAlbum.getM_Id() + " And name: " + selectedAlbum.getM_AlbumName());
        this.mSelectedAlbumIDsSet.add(selectedAlbum.getM_Id());
    }
}
