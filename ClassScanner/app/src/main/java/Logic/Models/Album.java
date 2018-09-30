package Logic.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

//TODO: Add missing fields to parcelable implementation
public class Album implements Parcelable {
    private String m_Id;
    private String m_AlbumName;
    private Date m_CreationDate;
    private String m_Description;
    private int m_NumOfPictures;
    private List <PictureAudioData> m_Pictures;
    private List <PictureAudioData> m_Audio;

    public Album() {}

    public Album(String m_Id, String m_AlbumName, Date m_CreationDate) {
        this.m_Id = m_Id;
        this.m_AlbumName = m_AlbumName;
        this.m_CreationDate = m_CreationDate;
    }

    protected Album(Parcel in) {
        m_Id = in.readString();
        m_AlbumName = in.readString();
        long tempDateAsLong = in.readLong();
        m_CreationDate = tempDateAsLong == -1 ? null : new Date(tempDateAsLong);
        m_Description = in.readString();
        m_NumOfPictures = in.readInt();

        m_Pictures = in.readArrayList(PictureAudioData.class.getClassLoader());
        if(m_Pictures == null) {
            m_Pictures = new ArrayList<>();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_Id);
        dest.writeString(m_AlbumName);
        dest.writeLong(m_CreationDate != null ? m_CreationDate.getTime() : -1);
        dest.writeString(m_Description);
        dest.writeInt(m_NumOfPictures);
        dest.writeList(m_Pictures);
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

    public void setM_Id(String m_Id) {
        this.m_Id = m_Id;
    }

    public String getM_AlbumName() {
        return m_AlbumName;
    }

    public void setM_AlbumName(String m_AlbumName) {
        this.m_AlbumName = m_AlbumName;
    }

    public Date getM_CreationDate() {
        return m_CreationDate;
    }

    public void setM_CreationDate(Date m_CreationDate) {
        this.m_CreationDate = m_CreationDate;
    }

    public String getM_Description() {
        return m_Description;
    }

    public void setM_Description(String m_Description) {
        this.m_Description = m_Description;
    }

    public int getM_NumOfPictures() {
        return m_NumOfPictures;
    }

    public void setM_NumOfPictures(int m_NumOfPictures) {
        this.m_NumOfPictures = m_NumOfPictures;
    }

    public List<PictureAudioData> getM_Pictures() {
        return m_Pictures;
    }

    public void setM_Pictures(List<PictureAudioData> m_Pictures) {
        this.m_Pictures = m_Pictures;
    }

    public List<PictureAudioData> getM_Audio() {
        return m_Audio;
    }

    public void setM_Audio(List<PictureAudioData> m_Audio) {
        this.m_Audio = m_Audio;
    }

    public String getM_Id() {
        return m_Id;
    }
}
