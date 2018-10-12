package Logic.Managers.AnalyticsManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import Logic.Managers.AnalyticsManager.AnalyticsHelpers.CourseEventsHelper;
import Logic.Managers.AnalyticsManager.EventParams.AlbumEventParams;
import Logic.Managers.AnalyticsManager.EventParams.CourseEventParams;
import Logic.Managers.AnalyticsManager.EventParams.PictureEventParams;
import Logic.Managers.AnalyticsManager.EventParams.UserEventParams;

public class AnalyticsManager {
    private static String TAG = "AnalyticsManager";

    private static final AnalyticsManager ourInstance = new AnalyticsManager();

    private static FirebaseAnalytics mFirebaseAnalytics;
    private static CourseEventsHelper mCourseEventsHelper;


    public static AnalyticsManager getInstance() {
        return ourInstance;
    }

    private AnalyticsManager() {
        mCourseEventsHelper = new CourseEventsHelper();
    }

    public void init(Context context) {
        Log.e(TAG, "Initializing");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        mCourseEventsHelper.init(mFirebaseAnalytics, userID);
    }

    public void trackSearchEvent(String searchedString, int numberOfMatches) {
        Log.e(TAG, "Tracking search event for search: " + searchedString);

        final String NUMBER_OF_MATCHES_PARAM = "numberOfMatches";

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchedString);
        params.putInt(NUMBER_OF_MATCHES_PARAM, numberOfMatches);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,params);
    }

    public void trackCourseEvent(eCourseEventType eventType, CourseEventParams courseEventParams) {
        Log.e(TAG, "Tracking event " + eventType.name());

        switch (eventType) {
            case ViewSuggestedCourses:
                mCourseEventsHelper.trackViewedSuggestedCourses(courseEventParams.getmNumberOfCoursesDisplayed());
                break;
            case ViewMyCourses:
                mCourseEventsHelper.trackViewMyCourses(courseEventParams.getmNumberOfCoursesDisplayed());
                break;
            case ViewAllCourses:
                mCourseEventsHelper.trackViewAllCourses(courseEventParams.getmNumberOfCoursesDisplayed());
                break;
            case CourseCreated:
                mCourseEventsHelper.trackCourseCreated(courseEventParams.getmCourse());
                break;
            case AddAlbumsToCourse:
                mCourseEventsHelper.trackAddAlbumsToCourse(courseEventParams.getmCourse(), courseEventParams.getmNumberOfAddedAlbums());
                break;
        }
    }

    public void trackAlbumEvent(eAlbumEventType eventType, AlbumEventParams albumEventParams) {
        Log.e(TAG, "Tracking event " + eventType.name());

        switch (eventType) {
            case ViewCourseAlbums:
                break;
            case ViewPrivateAlbums:
                break;
            case ViewAlbumPresentation:
                break;
            case AlbumCreated:
                break;
            case ViewAlbumImages:
                break;
        }
    }

    public void trackUserEvent(eUserEventType eventType, UserEventParams userEventParams) {
        Log.e(TAG, "Tracking event " + eventType.name());

        switch (eventType) {
            case ViewNotifications:
                break;
            case ClearNotifications:
                break;
        }
    }

    public void trackPictureEvent(ePictureEventType eventType, PictureEventParams pictureEventParams) {
        Log.e(TAG, "Tracking event " + eventType.name());

        switch (eventType) {
            case StartCropingImage:
                break;
            case StartTakingPictures:
                break;
            case CropImage:
                break;
        }
    }

    public enum eCourseEventType {
        ViewSuggestedCourses,
        ViewMyCourses,
        ViewAllCourses,
        CourseCreated,
        AddAlbumsToCourse,
    }

    public enum eAlbumEventType {
        ViewCourseAlbums,
        ViewPrivateAlbums,
        ViewAlbumPresentation,
        AlbumCreated,
        ViewAlbumImages,
    }

    public enum eUserEventType {
        ViewNotifications,
        ClearNotifications,
    }

    public enum ePictureEventType {
        StartCropingImage,
        StartTakingPictures,
        CropImage,
    }
}
