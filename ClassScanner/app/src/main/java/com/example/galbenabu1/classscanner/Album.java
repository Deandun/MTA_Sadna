package com.example.galbenabu1.classscanner;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Album implements Parcelable {
    private Date mCreationDate;
    private String mPublisherName;
    private String mAlbumName;

    public Album() {
    }

    protected Album(Parcel in) {
        mPublisherName = in.readString();
        mAlbumName = in.readString();
        long tempDate = in.readLong(); // Read date from parcel
        mCreationDate = tempDate == -1 ? null : new Date(tempDate);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPublisherName);
        dest.writeString(mAlbumName);
        dest.writeLong(mCreationDate != null ? mCreationDate.getTime() : -1); // Write date to parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
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
