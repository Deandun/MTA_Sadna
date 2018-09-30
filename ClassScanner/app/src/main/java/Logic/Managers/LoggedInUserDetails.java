package Logic.Managers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

import Logic.Database.DBManager;
import Logic.Models.User;

public class LoggedInUserDetails {
    private static final String TAG = "LoggedInUserDetails";
    private static final String UNKNOWN_USER_NAME = "Unknown";
    private static final String UNKNOWN_USER_MAIL = "";

    private LoggedInUserDetails() {

    }

    private static User sLoggedInUser;

    public static User getsLoggedInUser() {
        return sLoggedInUser;
    }

    public static void setsLoggedInUser(User loggedInUser) {
        sLoggedInUser = loggedInUser;
    }

    public static String getUserID() {
        return sLoggedInUser.getM_Id();
    }

    public static void initUserDetails(String uid) {
        DBManager dbManager = new DBManager();

        dbManager.fetchUserDetails(uid, LoggedInUserDetails::onFetchedUserSuccess, LoggedInUserDetails::onFetchedUserFailure);
    }

    private static void onFetchedUserSuccess(User userInfo) {
        Log.e(TAG, "Fetched user info for user: " + userInfo.getM_Id());
        setsLoggedInUser(userInfo);
    }

    private static void onFetchedUserFailure() {
        Log.e(TAG, "Failed fetching user info.");

        // We get here only if firebase succeeded in fetching user, but we did not manage to fetch user info from database.
        // Init unknown user.
        User unknownUser = new User(UNKNOWN_USER_NAME, UNKNOWN_USER_MAIL);
        unknownUser.setM_CourseIds(new ArrayList<>());

        setsLoggedInUser(unknownUser);
    }

    public static boolean doesUserContainCourseID(String courseID) {
        return sLoggedInUser.getM_CourseIds().contains(courseID);
    }
}
