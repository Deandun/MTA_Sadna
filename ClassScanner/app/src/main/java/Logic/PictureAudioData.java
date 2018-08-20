package Logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

//TODO: Add missing fields to parcelable implementation
public class PictureAudioData implements Parcelable {
    private String m_Id;
    private Date m_CreationDate;
    private String m_Description;
    private String m_Path;

    public PictureAudioData() {}

    public PictureAudioData(String m_Id, Date m_CreationDate, String m_Description, String m_Path) {
        this.m_Id = m_Id;
        this.m_CreationDate = m_CreationDate;
        this.m_Description = m_Description;
        this.m_Path = m_Path;
    }

    protected PictureAudioData(Parcel in) {
        m_Id = in.readString();
        long tempDateAsLong = in.readLong();
        m_CreationDate = tempDateAsLong == -1 ? null : new Date(tempDateAsLong);
        m_Description = in.readString();
        m_Path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_Id);
        dest.writeLong(m_CreationDate != null ? m_CreationDate.getTime() : -1);
        dest.writeString(m_Description);
        dest.writeString(m_Path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PictureAudioData> CREATOR = new Creator<PictureAudioData>() {
        @Override
        public PictureAudioData createFromParcel(Parcel in) {
            return new PictureAudioData(in);
        }

        @Override
        public PictureAudioData[] newArray(int size) {
            return new PictureAudioData[size];
        }
    };

    public String getM_Description() {
        return m_Description;
    }

    public void setM_Description(String m_Description) {
        this.m_Description = m_Description;
    }

    public String getM_Path() {
        return m_Path;
    }

    public void setM_Path(String m_Path) {
        this.m_Path = m_Path;
    }

    public void setM_Id(String m_Id) {
        this.m_Id = m_Id;
    }

    public void setM_CreationDate(Date m_CreationDate) {
        this.m_CreationDate = m_CreationDate;
    }

    public String getM_Id() {
        return m_Id;
    }

    public Date getM_CreationDate() {
        return m_CreationDate;
    }
}
