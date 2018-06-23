package com.example.galbenabu1.classscanner;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Course implements Parcelable {
    private Date mCreationDate;
    private String mPublisherName;
    private String mCourseName;


    public Course(){
    }

    protected Course(Parcel in) {
        mPublisherName = in.readString();
        mCourseName = in.readString();
        long tempDate = in.readLong(); // Read date from parcel
        mCreationDate = tempDate == -1 ? null : new Date(tempDate);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPublisherName);
        dest.writeString(mCourseName);
        dest.writeLong(mCreationDate != null ? mCreationDate.getTime() : -1); // Write date to parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public Date getmCreationDate() {
        return mCreationDate;
    }

    public void setmCreationDate(Date date) {
        this.mCreationDate = date;
    }

    public String getPublisherName() {
        return mPublisherName;
    }

    public void setPublisherName(String publisherName) {
        this.mPublisherName = publisherName;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public void setCourseName(String courseName) {
        this.mCourseName = courseName;
    }

    @Override
    public String toString() {
        return "Course{" +
                "CreationDate=" + mCreationDate +
                ", PublisherName='" + mPublisherName + '\'' +
                ", CourseName='" + mCourseName + '\'' +
                '}';
    }
}
