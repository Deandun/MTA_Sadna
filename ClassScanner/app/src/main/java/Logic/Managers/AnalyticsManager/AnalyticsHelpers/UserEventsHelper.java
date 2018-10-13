package Logic.Managers.AnalyticsManager.AnalyticsHelpers;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class UserEventsHelper {
    private String mUserID;
    private FirebaseAnalytics mFirebaseAnalytics;

    public void init(FirebaseAnalytics firebaseAnalytics, String userID) {
        this.mUserID = userID;
        this.mFirebaseAnalytics = firebaseAnalytics;
    }

    public void trackViewNotifications(int numberOfNotifications) {
        Bundle params = new Bundle();

        params.putInt(ParamNames.NUMBER_OF_NOTIFICATIONS, numberOfNotifications);

        logEvent(eUserEventType.ViewNotifications.name(), params);
    }

    public void trackClearNotifications(int numberOfNotifications) {
        Bundle params = new Bundle();

        params.putInt(ParamNames.NUMBER_OF_NOTIFICATIONS, numberOfNotifications);

        logEvent(eUserEventType.ClearNotifications.name(), params);
    }

    private void logEvent(String eventTypeString, Bundle params) {
        params.putString(ParamNames.USER_ID, this.mUserID);
        this.mFirebaseAnalytics.logEvent(eventTypeString, params);
    }

    private static class ParamNames {
        private static final String NUMBER_OF_NOTIFICATIONS = "numberOfNotifications";
        private static final String USER_ID = "userID";
    }

    public enum eUserEventType {
        ViewNotifications,
        ClearNotifications,
    }
}
