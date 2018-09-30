package com.example.galbenabu1.classscanner.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galbenabu1.classscanner.R;

import Logic.Models.PictureAudioData;

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "PhotoViewHolder";
    private TextView mtvTitle;
    private ImageView mivPhoto;
    private PictureAudioData mSelectedPhoto;

    public PhotoViewHolder(View view) {
        super(view);

        mtvTitle = view.findViewById(R.id.tv_photo_title);
        mivPhoto = view.findViewById(R.id.iv_photo);
        mivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "CardView.onClick() >> Photo: " + mSelectedPhoto.toString());

                Context context = view.getContext();
                //Intent intent = new Intent(context, DareDetailsActivity.class);
                //intent.putExtra("course", mSelectedCourse);
                //context.startActivity(intent);
            }
        });
    }

    public TextView getTvTitle() {
        return mtvTitle;
    }

    public void setTvTitle(TextView title) {
        this.mtvTitle = title;
    }

    public ImageView getIvPhoto() {
        return mivPhoto;
    }

    public void setIvPhoto(ImageView photo) {
        this.mivPhoto = photo;
    }

    public PictureAudioData getSelectedPhoto() {
        return mSelectedPhoto;
    }

    public void setSelectedPhoto(PictureAudioData selectedPhoto) {
        this.mSelectedPhoto = selectedPhoto;
    }
}


