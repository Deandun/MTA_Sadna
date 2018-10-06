package com.example.galbenabu1.classscanner.ViewHolders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galbenabu1.classscanner.Activities.AlbumInfoActivity;
import com.example.galbenabu1.classscanner.R;

import Logic.Models.Album;
import Logic.Interfaces.MyConsumer;

public class AlbumsViewHolder  extends RecyclerView.ViewHolder{

    private static final String TAG = "AlbumViewHolder";
    private static final String ALBUM_DATA = "album_data";

    private CardView mAlbumCardView;
    private TextView mCreatorName;
    private TextView mAlbumName;
    private TextView mCreationDate;
    private ImageView mAlbumImg;
    private Album mSelectedAlbum;
    private boolean mIsAlbumSelected;
    private MyConsumer<Album> mOnLongClickListener;
    private ImageView mCheckedSign;
    private boolean isAlbumChecked;

    public AlbumsViewHolder(View itemView) {
        super(itemView);

        this.isAlbumChecked = false;
        this.mAlbumCardView = itemView.findViewById(R.id.cvAlbum);
        this.mCreatorName = itemView.findViewById(R.id.tvAlbumPublisher);
        this.mAlbumName = itemView.findViewById(R.id.tvAlbumName);
        this.mCreationDate = itemView.findViewById(R.id.tvCreationDate);
        this.mAlbumImg = itemView.findViewById(R.id.ivAlbumImage);
        this.mIsAlbumSelected = false;
        this.mCheckedSign = itemView.findViewById(R.id.ivCheckedSign);

        this.mAlbumCardView.setOnClickListener(view -> {
            Log.e(TAG, "CardView.onClick() >> Album: " + mSelectedAlbum.toString());

            if(mIsAlbumSelected) {
                // If album is already selected, un-select it.
                mAlbumCardView.setCardBackgroundColor(Color.WHITE);
                mIsAlbumSelected = false;
            } else {
                // If album is not selected and clicked, open album info activity.
                Context context1 = view.getContext();
                Intent intent = new Intent(context1, AlbumInfoActivity.class);
                intent.putExtra(ALBUM_DATA, mSelectedAlbum);
                context1.startActivity(intent);
            }
        });

        mAlbumCardView.setOnLongClickListener(
                // Select album.
                view -> {
                    Log.e(TAG, "CardView.onLongClick() >> Album: " + mSelectedAlbum.toString());
                    mIsAlbumSelected = true;
                    mAlbumCardView.setCardBackgroundColor(Color.BLUE);
                    mOnLongClickListener.accept(mSelectedAlbum); // Send selected album using a callback.
                    //changeImageChceked();
                    return true; // Return true to signal that we have done what we need to do for the long press.
                }
        );
    }

    @SuppressLint("ResourceType")
    private void changeImageChceked(){
        this.mCheckedSign.setVisibility(View.INVISIBLE);
        if (isAlbumChecked){
            this.mCheckedSign.setImageResource(R.id.ivCheckedSign);
        }else{
          //  this.mCheckedSign.setImageResource(R.id.ivUnCheckedSign);
        }
        this.mCheckedSign.setVisibility(View.VISIBLE);
        isAlbumChecked = !isAlbumChecked;
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

    public CardView getAlbumCardView() {
        return this.mAlbumCardView;
    }

    public void setOnLongClickListener(MyConsumer<Album> onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

}