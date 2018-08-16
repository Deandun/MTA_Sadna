package Logic;

import android.os.Parcel;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
