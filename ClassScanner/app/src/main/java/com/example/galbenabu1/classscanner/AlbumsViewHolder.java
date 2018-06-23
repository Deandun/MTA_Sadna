package com.example.galbenabu1.classscanner;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumsViewHolder  extends RecyclerView.ViewHolder{
    private static final String TAG = "AlbumViewHolder";
    private CardView mAlbumCardView;
    private TextView mCreatorName;
    private TextView mAlbumName;
    private TextView mCreationDate;
    private ImageView mAlbumImg;
    private Album mSelectedAlbum;

    public AlbumsViewHolder(Context context, View itemView) {
        super(itemView);

        mAlbumCardView = itemView.findViewById(R.id.cvAlbum);
        mCreatorName = itemView.findViewById(R.id.tvPublisher);
        mAlbumName = itemView.findViewById(R.id.tvAlbumName);
        mCreationDate = itemView.findViewById(R.id.tvCreationDate);
        mAlbumImg = itemView.findViewById(R.id.ivAlbumImage);

        mAlbumCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "CardView.onClick() >> Album: " + mSelectedAlbum.toString());

                Context context = view.getContext();
                //Intent intent = new Intent(context, DareDetailsActivity.class);
                //intent.putExtra("course", mSelectedCourse);
                //context.startActivity(intent);
            }
        });
    }

    public Album getSelectedAlbum() {
        return mSelectedAlbum;
    }

    public void setSelectedAlbum(Album selectedAlbum) {
        this.mSelectedAlbum = selectedAlbum;
    }

    public TextView getCreatorName() {
        return mCreatorName;
    }

    public void setCreatorName(TextView creatorName) {
        this.mCreatorName = creatorName;
    }

    public TextView getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(TextView albumName) {
        this.mAlbumName = albumName;
    }

    public TextView getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(TextView creationDate) {
        this.mCreationDate = creationDate;
    }

    public ImageView getAlbumImg() {
        return mAlbumImg;
    }

    public void setAlbumImg(ImageView albumImg) {
        this.mAlbumImg = albumImg;
    }
}