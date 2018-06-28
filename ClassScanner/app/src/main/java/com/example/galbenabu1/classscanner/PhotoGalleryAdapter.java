package com.example.galbenabu1.classscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//TODO: add logs
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
    private static final String TAG = "PhotoGalleryAdapter";
    private ArrayList<Photo> mPhotoList;
    private Context mContext;

    public PhotoGalleryAdapter(ArrayList<Photo> photoList, Context context) {
        mPhotoList = photoList;
        mContext = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder() >> " + position);

        Photo photo = mPhotoList.get(position);

        holder.setSelectedPhoto(photo);
        holder.getTvTitle().setText(photo.getTitle());
        //holder.getIvPhoto().setImageBitmap(fetchImageBitmapFromDB(photo.getImagePath()));
        // For now, get dummy image
        holder.getIvPhoto().setImageDrawable(mContext.getResources().getDrawable(R.drawable.lazershark, null));

        Log.e(TAG, "onBindViewHolder() >> " + photo);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

//    Bitmap fetchImageBitmapFromDB(String imagePath) {
//        //TODO: fetch image from the actual image path in firebase storage
//    }
}