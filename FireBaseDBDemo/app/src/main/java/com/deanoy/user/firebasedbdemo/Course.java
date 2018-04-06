package com.deanoy.user.firebasedbdemo;

/**
 * Created by user on 05-Apr-18.
 */

public class Course {
    private static int sAvailableCourseID = 0;
    public String mId;
    public int mCoursePoints;
    public String mCourseName;

    public Course() {}

    public String getmId() {
        return mId;
    }

    public int getmCoursePoints() {
        return mCoursePoints;
    }

    public String getmCourseName() {
        return mCourseName;
    }

    public Course(int coursePoints, String courseName) {
        mId = Integer.toString(sAvailableCourseID);
        sAvailableCourseID++;
        mCoursePoints = coursePoints;
        mCourseName = courseName;
    }
}
