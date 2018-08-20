package Logic.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Helper class for DB manager - Generates database references
class FirebaseDBReferenceGenerator {
    private static final DatabaseReference sfDatabaseRoot = FirebaseDatabase.getInstance().getReference();

    static DatabaseReference getAllCoursesReference() {
        return sfDatabaseRoot.child(eFirebaseDBEntityTypes.Courses.getReferenceName());
    }

    static DatabaseReference getCourseReference(String courseID) {
        return getAllCoursesReference().child(courseID);
    }

    static DatabaseReference getAllUsersReference() {
        return sfDatabaseRoot.child(eFirebaseDBEntityTypes.Users.getReferenceName());
    }

    static DatabaseReference getUserReference(String userID) {
        return getAllUsersReference().child(userID);
    }

    static DatabaseReference getAllUserNotificationsReference() {
        return sfDatabaseRoot.child(eFirebaseDBEntityTypes.UserNotifications.getReferenceName());
    }

    static DatabaseReference getUserNotificationsReference(String userID) {
        return getAllUserNotificationsReference().child(userID);
    }

    static DatabaseReference getSuggestedCoursesReference() {
        return sfDatabaseRoot.child(eFirebaseDBEntityTypes.SuggestedCourses.getReferenceName());
    }

    static DatabaseReference getAllSharedAlbumsReference() {
        return getAlbumsReference().child(eFirebaseDBEntityTypes.SharedAlbums.getReferenceName());
    }

    static DatabaseReference getAllCourseSharedAlbumsReference(String courseID) {
        return getAllSharedAlbumsReference().child(courseID);
    }

    static DatabaseReference getSharedAlbumReference(String albumID, String courseID) {
        return getAllCourseSharedAlbumsReference(courseID).child(albumID);
    }

    static DatabaseReference getAllPrivateAlbumsReference() {
        return getAlbumsReference().child(eFirebaseDBEntityTypes.PrivateAlbums.getReferenceName());
    }

    static DatabaseReference getAllUserPrivateAlbumsReference(String userID) {
        return getAllPrivateAlbumsReference().child(userID);
    }

    static DatabaseReference getPrivateAlbumReference(String albumID, String userID) {
        return getAllUserPrivateAlbumsReference(userID).child(albumID);
    }

    private static DatabaseReference getAlbumsReference() {
        return sfDatabaseRoot.child(eFirebaseDBEntityTypes.Albums.getReferenceName());
    }

    static DatabaseReference getPrivateAlbumPictureReference(String albumID, String userID) {
        return getPrivateAlbumReference(albumID, userID).child(eFirebaseDBEntityTypes.Pictures.getReferenceName());
    }

    static DatabaseReference getSharedAlbumPictureReference(String albumID, String courseID) {
        return getSharedAlbumReference(albumID, courseID).child(eFirebaseDBEntityTypes.Pictures.getReferenceName());
    }
}
