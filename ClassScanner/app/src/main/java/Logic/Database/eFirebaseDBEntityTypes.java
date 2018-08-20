package Logic.Database;

public enum eFirebaseDBEntityTypes {
    Courses("Courses"),
    Users("Users"),
    Albums("Albums"),
    Pictures("Pictures"),
    SharedAlbums("SharedAlbums"),
    PrivateAlbums("PrivateAlbums"),
    UserNotifications("UserNotifications"),
    SuggestedCourses("SuggestedCourses");

    private String mReferenceName;

    String getReferenceName() {
        return mReferenceName;
    }

    eFirebaseDBEntityTypes(String referenceName) {
        mReferenceName = referenceName;
    }
}
