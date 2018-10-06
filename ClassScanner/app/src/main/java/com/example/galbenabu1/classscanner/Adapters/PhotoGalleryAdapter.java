package com.example.galbenabu1.classscanner.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.galbenabu1.classscanner.ViewHolders.PhotoViewHolder;
import com.example.galbenabu1.classscanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;

import Logic.Models.PictureAudioData;

//TODO: add logs
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
    private static final String TAG = "PhotoGalleryAdapter";
    private ArrayList<PictureAudioData> mPhotoList;
    private Context mContext;

    public PhotoGalleryAdapter(ArrayList<PictureAudioData> photoList, Context context) {
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

        PictureAudioData photo = mPhotoList.get(position);


        holder.setSelectedPhoto(photo);
        holder.getTvTitle().setText(photo.getM_Description());
        holder.getIvPhoto().setImageBitmap(fetchImageBitmapFromDB(photo.getM_Path(), photo.getM_Id()));

        Log.e(TAG, "onBindViewHolder() >> " + photo);
    }

    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap=null;
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }}

    private Bitmap fetchImageBitmapFromDB(String storagePath, String pictureName) {
        final Bitmap my_image_res = null;


            FirebaseStorage storage;
            StorageReference ref;
            storage = FirebaseStorage.getInstance();
            ref = storage.getReference().child(storagePath);

        String FileLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderLocation= FileLocation + "/classScanner";
        FileLocation += "/classScanner/"+pictureName+".jpg";

        File directory = new File(folderLocation);
        directory.mkdirs();

            final File localFile = new File(FileLocation);
//TODO: write to log
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("FOUND PICTURE");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("FAILED FINDING PICTURE");
                }
            });

            return getBitmap(FileLocation);
        }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

//    Bitmap fetchImageBitmapFromDB(String imagePath) {
//        //TODO: fetch image from the actual image path in firebase storage
//    }
}
