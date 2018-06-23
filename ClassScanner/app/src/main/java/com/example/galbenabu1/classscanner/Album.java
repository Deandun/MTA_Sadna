package com.example.galbenabu1.classscanner;

import java.util.Date;

public class Album {
    private Date mCreationDate;
    private String mPublisherName;
    private String mAlbumName;


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

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        this.mAlbumName = albumName;
    }

    @Override
    public String toString() {
        return "Album{" +
                "CreationDate=" + mCreationDate +
                ", PublisherName='" + mPublisherName + '\'' +
                ", AlbumName='" + mAlbumName + '\'' +
                '}';
    }
}
