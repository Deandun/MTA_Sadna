package Logic.Database;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import Logic.Album;
import Logic.Course;
import Logic.Interfaces.MyConsumer;
import Logic.Interfaces.MyFunction;
import Logic.PictureAudioData;

import static Logic.Database.KeysForDBModels.*;

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

    public void updatingUserDetails(Map<String, Object> userDetails) {
        String key = mDatabase.child("Users").push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, userDetails);
        childUpdates.put("/user-posts/" + userDetails.containsKey("id") + "/" + key, userDetails);

        mDatabase.updateChildren(childUpdates);
    }

    public List<Album> getAlbumsList() {//Parcel in) { //noy - TEMP

        List<Album> albums = new ArrayList<>();
        //DatabaseReference DB = mDatabase.getDatabase().getReference("/Albums");
        mStorageRef = FirebaseStorage.getInstance().getReference("/Albums");
        Log.e(TAG, "Got albums " + mStorageRef.getName());
        Log.e(TAG, "Got dummyalbum = " + mStorageRef.child("DummyAlbum"));

        mStorageRef.getName();
        List a = new ArrayList();
        a.add(mStorageRef);
        Log.e(TAG, "******************DEBUG: size = " + a.size());

        return albums;
    }

    public void uploadImageToPrivateAlbum(Bitmap imageBitmap, String userId, String albumID, MyConsumer<PictureAudioData> uploadImageSuccess, Runnable uploadImageFailure) {
        Log.e(TAG, "Writing picture data to DB...");
        PictureAudioData pictureData = writeImageDataToPrivateAlbumsAndGetKey(userId, albumID);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        // Upload image
        Log.e(TAG, "Uploading image to DB...");
        StorageReference imageRef = mStorageRef.child("Albums").child("privateAlbums").child(userId).child(albumID).child(pictureData.getM_Id());

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            Log.e(TAG, "Failed reading from storage.");
            uploadImageFailure.run();
        }).addOnSuccessListener(taskSnapshot -> {
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            Log.e(TAG, "Successfully read " + downloadUrl.toString() + " from storage!");
            pictureData.setM_Path(downloadUrl.getPath());
            uploadImageSuccess.accept(pictureData);
        });
    }

    private PictureAudioData writeImageDataToPrivateAlbumsAndGetKey(String userId, String albumID) {
        PictureAudioData pictureData = new PictureAudioData();
        DatabaseReference privateAlbumRef = FirebaseDBReferenceGenerator.getPrivateAlbumReference(albumID, userId);
        String pictureKey = privateAlbumRef.push().getKey();

        pictureData.setM_Id(pictureKey);
        pictureData.setM_CreationDate(Calendar.getInstance().getTime());
        privateAlbumRef.child(pictureKey).setValue(pictureData);

        return pictureData;
    }

    public String getNewAlbumID(String userID) {
        DatabaseReference userPrivateAlbumsRef = FirebaseDBReferenceGenerator.getAllUserPrivateAlbumsReference(userID);

        return userPrivateAlbumsRef.push().getKey();
    }

    public void removeAlbumFromDB(String albumID, String userID, boolean isPrivateAlbum, Collection<PictureAudioData> pictureDataCollection) {
        Log.e(TAG, "Removing album with ID: " + albumID + " from DB");

        DatabaseReference albumRef;

        if (isPrivateAlbum) {
            albumRef = FirebaseDBReferenceGenerator.getPrivateAlbumReference(albumID, userID);
        } else {
            albumRef = FirebaseDBReferenceGenerator.getSharedAlbumReference(albumID, userID);
        }

        this.removeAlbumFromStorage(albumID, userID, isPrivateAlbum, pictureDataCollection);


        albumRef.removeValue(
                (error, dbRef) -> {
                    String errorMsg = error == null ? "" : " received code: " + error.getCode() +
                            ". and message: " + error.getMessage();
                    Log.e(TAG, "Removing album with ID: " + albumID + errorMsg);
                }
        );
    }

    // Delete the album's pictures.
    //Can't delete folder from storage - Have to delete pictures one by one.
    private void removeAlbumFromStorage(String albumID, String userID, boolean isPrivateAlbum, Collection<PictureAudioData> pictureDataCollection) {
        Log.e(TAG, "Removing album with ID: " + albumID + " from storage");
        String albumTypeString = isPrivateAlbum ? "privateAlbums" : "sharedAlbums";
        StorageReference albumRef = mStorageRef.child("Albums/").child(albumTypeString).child(userID).child(albumID);

        for (PictureAudioData pictureData : pictureDataCollection) {
            StorageReference pictureRef = albumRef.child(pictureData.getM_Id());
            pictureRef.delete().addOnSuccessListener(
                    (aVoid) -> Log.e(TAG, "Successfully deleted picture with ID: " + pictureData.getM_Id())
            ).addOnFailureListener(
                    (exception) -> Log.e(TAG, "failed to delete picture with ID: " + pictureData.getM_Id() + System.lineSeparator() +
                            "Error message: " + exception.getMessage())
            );
        }
    }

    public void removePicturesFromDB(String albumID, String userID, boolean isPrivateAlbum, Collection<PictureAudioData> pictureCollections) {
        Log.e(TAG, "Removing pictures from DB");

        DatabaseReference picturesRef;

        if (isPrivateAlbum) {
            picturesRef = FirebaseDBReferenceGenerator.getPrivateAlbumPictureReference(albumID, userID);
        } else {
            picturesRef = FirebaseDBReferenceGenerator.getSharedAlbumPictureReference(albumID, userID);
        }

        this.removePicturesFromStorage(albumID, userID, isPrivateAlbum, pictureCollections);

        Map<String, Object> deletionMap = new HashMap<>();

        for (PictureAudioData pictureData : pictureCollections) {
            deletionMap.put(pictureData.getM_Id(), null);
        }

        picturesRef.updateChildren(deletionMap).addOnSuccessListener(
                (aVoid -> Log.e(TAG, "Pictures deleted successfully"))
        ).addOnFailureListener(
                (exception) -> Log.e(TAG, "Failed deleting pictures from DB with message: " + exception.getMessage())
        );
    }

    private void removePicturesFromStorage(String albumID, String userID, boolean isPrivateAlbum, Collection<PictureAudioData> pictureCollection) {
        Log.e(TAG, "Removing picture with ID: " + albumID + " from storage");
        String albumTypeString = isPrivateAlbum ? "privateAlbums" : "sharedAlbums";
        StorageReference albumRef = mStorageRef.child("Albums/").child(albumTypeString).child(userID).child(albumID);

        for (PictureAudioData pictureData : pictureCollection) {
            albumRef.child(pictureData.getM_Id()).delete().addOnSuccessListener(
                    (aVoid) -> Log.e(TAG, "Successfully deleted picture with ID: " + pictureData.getM_Id() + " from storage.")
            ).addOnFailureListener(
                    (exception) -> Log.e(TAG, "Failed to delete picture with ID: " + pictureData.getM_Id() + " from storage." + System.lineSeparator() +
                            "Error message: " + exception.getMessage())
            );
        }
    }

    public void addAlbumDetailsToDB(Album newAlbum, String userID) {
        Log.e(TAG, "Adding new album details with ID: " + newAlbum.getM_Id() + " to DB");

        DatabaseReference privateAlbumRef = FirebaseDBReferenceGenerator.getPrivateAlbumReference(newAlbum.getM_Id(), userID);

        privateAlbumRef.setValue(newAlbum).addOnSuccessListener(
                (aVoid) -> Log.e(TAG, "Successfully added album details for album with ID: " + newAlbum.getM_Id())
        ).addOnFailureListener(
                (exception) -> Log.e(TAG, "failed to add album details for album with ID: " + newAlbum.getM_Id() + System.lineSeparator() +
                        "Error message: " + exception.getMessage())
        );

    }

    public void fetchUserPrivateAlbumsFromDB(String userID, MyConsumer<List<Album>> onFinishConsumer) {
        Log.e(TAG, "Fetching private albums for user with ID: " + userID);
        DatabaseReference userPrivateAlbumsRef = FirebaseDBReferenceGenerator.getAllUserPrivateAlbumsReference(userID);

        this.fetchAlbums(userPrivateAlbumsRef, userID, true, onFinishConsumer);
    }

    public void fetchCourseSharedAlbumsFromDB(String courseID, MyConsumer<List<Album>> onFinishFetchingAlbums) {
        Log.e(TAG, "Fetching private albums for user with ID: " + courseID);
        DatabaseReference courseSharedAlbumsRef = FirebaseDBReferenceGenerator.getAllCourseSharedAlbumsReference(courseID);

        fetchAlbums(courseSharedAlbumsRef, courseID, false, onFinishFetchingAlbums);
    }

    private void fetchAlbums(DatabaseReference albumsRef, String albumHolderID, boolean isPrivateAlbums, MyConsumer<List<Album>> onFinishFetchingAlbums) {
        String albumTypeString = isPrivateAlbums ? eFirebaseDBEntityTypes.PrivateAlbums.getReferenceName() :
                eFirebaseDBEntityTypes.SharedAlbums.getReferenceName();

        albumsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Received " + albumTypeString + " for album holder with ID: " + albumHolderID);
                List<Album> albumList = new ArrayList<>();
                Album album;

                for (DataSnapshot albumSnapshot : dataSnapshot.getChildren()) {
                    album = albumSnapshot.getValue(Album.class);
                    albumList.add(album);
                }

                onFinishFetchingAlbums.accept(albumList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed fetching " + albumTypeString + " for user with ID: " + albumHolderID);
            }
        });
    }

    public void fetchFilteredCourses(MyFunction<Course, Boolean> filterFunction, MyConsumer<List<Course>> onFinishFetchingCourses) {
        Log.e(TAG, "Fetching filtered courses");
        DatabaseReference courseRef = FirebaseDBReferenceGenerator.getAllCoursesReference();

        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Fetched courses from DB");
                List<Course> courseList = new ArrayList<>();
                Course course;

                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    course = courseSnapshot.getValue(Course.class);
                    if (filterFunction.apply(course)) {
                        courseList.add(course);
                    }
                }

                onFinishFetchingCourses.accept(courseList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed fetching courses");
            }
        });
    }

    public void addAlbumsToExistsCourse(Course courseToUpdate) {
        Log.e(TAG, "Adding Albums to course: " + courseToUpdate.getID() + " to DB");

        DatabaseReference courseRef = FirebaseDBReferenceGenerator.getCourseReference(courseToUpdate.getID());
        courseRef.child(ALBUMS_ID).setValue(courseToUpdate.getM_AlbumIds());

        Log.e(TAG, "Added to DB albums to course: " + courseToUpdate.getID());
    }

    public void addNewCourseDetailsToDBAndSetCourseID(Course newCourse) {
        Log.e(TAG, "Adding new course details with ID: " + newCourse.getID() + " to DB");

        // Set new course ID.
        DatabaseReference privateCourseRef = FirebaseDBReferenceGenerator.getAllCoursesReference().push();
        String courseID = privateCourseRef.getKey();

        newCourse.setId(courseID);

        privateCourseRef.setValue(newCourse).addOnSuccessListener(
                (aVoid) -> {
                    Log.e(TAG, "Successfully added course details for course with ID: " + newCourse.getID());
                    // Remove album IDs from private albums to shared albums.
                    this.moveAlbumIDsFromPrivateToShared(newCourse.getID(), newCourse.getCreatorID(), newCourse.getM_AlbumIds());
                }
        ).addOnFailureListener(
                (exception) -> Log.e(TAG, "failed to add course details for course with ID: " + newCourse.getID() + System.lineSeparator() +
                        "Error message: " + exception.getMessage())
        );
    }

    private void moveAlbumIDsFromPrivateToShared(String courseID, String albumCreatorUserID, List<String> albumIDs) {
        Log.e(TAG, "Removing album IDs " + albumIDs + " From private albums of user with iD: " + albumCreatorUserID + " To shared albums of course with ID: " + courseID);
        DatabaseReference userPrivateAlbumsRef = FirebaseDBReferenceGenerator.getAllUserPrivateAlbumsReference(albumCreatorUserID);
        DatabaseReference courseSharedAlbumsRef = FirebaseDBReferenceGenerator.getAllCourseSharedAlbumsReference(courseID);

        for (String albumID : albumIDs) {
            userPrivateAlbumsRef.child(albumID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Album currentAlbum = dataSnapshot.getValue(Album.class);
                    if (currentAlbum == null) {
                        // Album does not exist in DB. Might be because this function is called again after removing the album the first time.
                        return;
                    }
                    Log.e(TAG, "Finished fetching album with ID: " + currentAlbum.getM_Id());

                    // Remove album from private albums
                    userPrivateAlbumsRef.child(currentAlbum.getM_Id()).removeValue(
                            (error, dbRef) -> {
                                String errorMsg = error == null ? "" : " Error message: " + error.getMessage();
                                Log.e(TAG, "Finished removing album with ID: " + albumID + errorMsg);
                            }
                    );

                    // Add album to shared albums.
                    courseSharedAlbumsRef.child(currentAlbum.getM_Id()).setValue(currentAlbum);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void moveAlbumIDsFromPrivateToSharedHelper(String courseID, String albumCreatorUserID, List<String> albumIDs) {
        moveAlbumIDsFromPrivateToShared(courseID, albumCreatorUserID, albumIDs);
    }
}