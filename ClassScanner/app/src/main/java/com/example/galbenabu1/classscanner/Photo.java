package com.example.galbenabu1.classscanner;

import java.util.Date;

public class Photo {
    private String mImagePath;
    private String mTitle;
    private Date mCreationDate;


    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String ImagePath) {
        this.mImagePath = ImagePath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        this.mTitle = Title;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(Date CreationDate) {
        this.mCreationDate = CreationDate;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mImagePath='" + mImagePath + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mCreationDate=" + mCreationDate +
                '}';
    }
}
