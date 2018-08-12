package com.example.galbenabu1.classscanner;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import Logic.Album;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsViewHolder> {
    private final String TAG = "AlbumsAdapter";

    private List<Album> mAlbumsList;

    public AlbumsAdapter(List<Album> albumsList) {
        mAlbumsList = albumsList;
    }

    @Override
    public AlbumsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e(TAG, "onCreateViewHolder() >>");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);

        Log.e(TAG, "onCreateViewHolder() <<");
        return new AlbumsViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(AlbumsViewHolder holder, int position) {

        Log.e(TAG, "onBindViewHolder() >> " + position);

        Album album = mAlbumsList.get(position);

        // bind Album data to it's view items
        holder.setSelectedAlbum(album);
        holder.getAlbumName().setText("Name: " + album.getAlbumName());
        holder.getCreatorName().setText("Publisher: " + album.getAlbumName());
        holder.getCreationDate().setText("Creation date: " + album.getCreationDate());


        Log.e(TAG, "onBindViewHolder() << " + position);
    }

    @Override
    public int getItemCount() {
        return mAlbumsList.size();
    }
}
