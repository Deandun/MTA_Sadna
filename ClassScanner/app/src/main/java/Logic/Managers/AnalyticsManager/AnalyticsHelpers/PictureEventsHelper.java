package Logic.Managers.AnalyticsManager.AnalyticsHelpers;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class PictureEventsHelper {
    private String mUserID;
    private FirebaseAnalytics mFirebaseAnalytics;

    public void init(FirebaseAnalytics firebaseAnalytics, String userID) {
        this.mUserID = userID;
        this.mFirebaseAnalytics = firebaseAnalytics;
    }

    public void trackStartCroppingImage(String pictureID) {
        Bundle params = new Bundle();

        params.putString(ParamNames.PICTURE_ID, pictureID);

        logEvent(ePictureEventType.StartCropingImage.name(), params);
    }

    public void trackStartTakingPictures() {
        Bundle params = new Bundle();
        logEvent(ePictureEventType.StartTakingPictures.name(), params);
    }

    private void logEvent(String eventTypeString, Bundle params) {
        params.putString(ParamNames.USER_ID, this.mUserID);
        this.mFirebaseAnalytics.logEvent(eventTypeString, params);
    }

    private static class ParamNames {
        private static final String PICTURE_ID = "pictureID";
        public static final String USER_ID = "userID";
    }

    public enum ePictureEventType {
        StartCropingImage,
        StartTakingPictures,
    }
}
