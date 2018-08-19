package Logic;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Logic.Interfaces.ObjectUploadSuccessListener;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

public class DBManager {
    private static String TAG = "DATABASE";

    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private List<Album> mAlbumNameList = new ArrayList<>();

    //private classScannetGlideModule mGlideModule;

    public DBManager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void updatingUserDetails(Map<String, Object> userDetails)
    {
        String key = mDatabase.child("Users").push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, userDetails);
        childUpdates.put("/user-posts/" + userDetails.containsKey("id") + "/" + key, userDetails);

        mDatabase.updateChildren(childUpdates);
    }

    public List<Album> getAlbumsList(){//Parcel in) { //noy - TEMP

        List<Album> albums = new ArrayList<>();
        //DatabaseReference DB = mDatabase.getDatabase().getReference("/Albums");
        mStorageRef = FirebaseStorage.getInstance().getReference("/Albums");
        Log.e(TAG, "Got albums " + mStorageRef.getName());
        Log.e(TAG, "Got dummyalbum = " + mStorageRef.child("DummyAlbum"));

        mStorageRef.getName();
        List a = new ArrayList();
        a.add(mStorageRef);
        Log.e(TAG, "******************DEBUG: size = " + a.size());
       // Album album = new Album(in);
       // albums.add(Album.CREATOR.createFromParcel(in));

        return albums;
    }

    public void uploadImageToPrivateAlbum(Bitmap imageBitmap, String userId, String albumID, ObjectUploadSuccessListener<PictureAudioData> uploadImageSuccess, Runnable uploadImageFailure) {
        Log.e(TAG, "Writing picture data to DB...");
        PictureAudioData pictureData = writeImageDataToPrivateAlbumsAndGetKey(userId, albumID);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        // Upload image
        Log.e(TAG, "Uploading image to DB...");
        StorageReference imageRef = mStorageRef.child("privateAlbums").child(userId).child(albumID).child(pictureData.getM_Id());

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            Log.e(TAG, "Failed reading from storage.");
            uploadImageFailure.run();
        }).addOnSuccessListener(taskSnapshot -> {
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            Log.e(TAG, "Successfully read " + downloadUrl.toString() + " from storage!");
            pictureData.setM_Path(downloadUrl.getPath());
            uploadImageSuccess.onUploadSuccess(pictureData);
        });
    }

    private PictureAudioData writeImageDataToPrivateAlbumsAndGetKey(String userId, String albumID) {
        PictureAudioData pictureData = new PictureAudioData();
        DatabaseReference privateAlbumRef = mDatabase.child("Albums").child("PrivateAlbums").child(userId).child(albumID).child("pictures");
        String pictureKey = privateAlbumRef.push().getKey();

        pictureData.setM_Id(pictureKey);
        pictureData.setM_CreationDate(Calendar.getInstance().getTime());
        privateAlbumRef.child(pictureKey).setValue(pictureData);

        return pictureData;
    }

    public String getNewAlbumID(String userID) {
        DatabaseReference privateAlbumRef = mDatabase.child("Albums").child("PrivateAlbums").child(userID);

        return privateAlbumRef.push().getKey();
    }

    public void removePicturesFromDB(Collection<PictureAudioData> pictureCollections) {

    }

/*  public List<Album> getAlbumsList (Parcel in) {

        List<Album> albums = new ArrayList<>();

        Album album = new Album(in);
        albums.add(Album.CREATOR.createFromParcel(in));

        return albums;
    }*/
    /*
    @GlideModule
    public class classScannetGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }*/
}
