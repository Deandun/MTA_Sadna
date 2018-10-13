package Logic.Managers.AnalyticsManager.AnalyticsHelpers;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import Logic.Models.Album;

public class AlbumEventsHelper {
    private String mUserID;
    private FirebaseAnalytics mFirebaseAnalytics;

    public void init(FirebaseAnalytics firebaseAnalytics, String userID) {
        this.mUserID = userID;
        this.mFirebaseAnalytics = firebaseAnalytics;
    }

    public void trackViewCourseAlbums(String courseID, int numberOfAlbums) {
        Bundle params = new Bundle();

        params.putString(ParamNames.COURSE_ID, courseID);
        params.putInt(ParamNames.NUMBER_OF_ALBUMS, numberOfAlbums);

        logEvent(eAlbumEventType.ViewCourseAlbums.name(), params);
    }

    public void trackViewPrivateAlbums(int numberOfAlbums) {
        Bundle params = new Bundle();

        params.putInt(ParamNames.NUMBER_OF_ALBUMS, numberOfAlbums);

        logEvent(eAlbumEventType.ViewPrivateAlbums.name(), params);
    }

    public void trackAlbumPresentationStarted(Album presentedAlbum) {
        Bundle params = new Bundle();

        params.putInt(ParamNames.NUMBER_OF_PICTURES, presentedAlbum.getM_Pictures().size());
        params.putString(ParamNames.ALBUM_NAME, presentedAlbum.getM_AlbumName());

        logEvent(eAlbumEventType.ViewAlbumPresentation.name(), params);
    }

    public void trackAlbumCreated(Album createdAlbum) {
        Bundle params = new Bundle();

        params.putInt(ParamNames.NUMBER_OF_PICTURES, createdAlbum.getM_Pictures().size());
        params.putString(ParamNames.ALBUM_NAME, createdAlbum.getM_AlbumName());

        logEvent(eAlbumEventType.AlbumCreated.name(), params);
    }

    public void trackViewAlbumImages(Album album) {
        Bundle params = new Bundle();

        params.putInt(ParamNames.NUMBER_OF_PICTURES, album.getM_Pictures().size());
        params.putString(ParamNames.ALBUM_NAME, album.getM_AlbumName());

        logEvent(eAlbumEventType.AlbumCreated.name(), params);
    }

    private void logEvent(String eventTypeString, Bundle params) {
        params.putString(ParamNames.USER_ID, this.mUserID);
        this.mFirebaseAnalytics.logEvent(eventTypeString, params);
    }

    private static class ParamNames {
        private static final String USER_ID = "userID";
        public static final String COURSE_ID = "courseID";
        public static final String NUMBER_OF_ALBUMS = "numberOfDisplayedAlbums";
        public static final String ALBUM_NAME = "albumName";
        public static final String NUMBER_OF_PICTURES = "numberOfPictures";
    }

    public enum eAlbumEventType {
        ViewCourseAlbums,
        ViewPrivateAlbums,
        ViewAlbumPresentation,
        AlbumCreated,
        ViewAlbumImages,
    }
}
