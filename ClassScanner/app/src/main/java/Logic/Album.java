package Logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

//TODO: Add missing fields to parcelable implementation
public class Album implements Parcelable {
    private String m_Id;
    private String m_AlbumName;
    private String m_CreationDate;
    private String m_Description;
    private int m_NumOfPictures;
    private List <PictureAudioData> m_Pictures;
    private List <PictureAudioData> m_Audio;

    public Album(String m_Id, String m_AlbumName, String m_CreationDate) {
        this.m_Id = m_Id;
        this.m_AlbumName = m_AlbumName;
        this.m_CreationDate = m_CreationDate;
    }

    protected Album(Parcel in) {
        m_Id = in.readString();
        m_AlbumName = in.readString();
        m_CreationDate = in.readString();
        m_Description = in.readString();
        m_NumOfPictures = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_Id);
        dest.writeString(m_AlbumName);
        dest.writeString(m_CreationDate);
        dest.writeString(m_Description);
        dest.writeInt(m_NumOfPictures);
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

    public String getAlbumName() {
        return m_AlbumName;
    }

    public void setAlbumName(String m_AlbumName) {
        this.m_AlbumName = m_AlbumName;
    }

    public String getCreationDate() {
        return m_CreationDate;
    }

    public String getDescription() {
        return m_Description;
    }

    public void setDescription(String m_Description) {
        this.m_Description = m_Description;
    }

    public int getNumOfPictures() {
        return m_NumOfPictures;
    }

    public void setNumOfPictures(int m_NumOfPictures) {
        this.m_NumOfPictures = m_NumOfPictures;
    }

    public List<PictureAudioData> getPictures() {
        return m_Pictures;
    }

    public void setPictures(List<PictureAudioData> m_Pictures) {
        this.m_Pictures = m_Pictures;
    }

    public List<PictureAudioData> getAudio() {
        return m_Audio;
    }

    public void setAudio(List<PictureAudioData> m_Audio) {
        this.m_Audio = m_Audio;
    }
}
