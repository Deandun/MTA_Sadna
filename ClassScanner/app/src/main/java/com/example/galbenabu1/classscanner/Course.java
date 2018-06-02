package com.example.galbenabu1.classscanner;

import java.util.Date;

public class Course {
    private Date mCreationDate;
    private String mPublisherName;
    private String mCourseName;

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
