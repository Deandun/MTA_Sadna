package Logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

public class Course implements Parcelable {
    private String m_Id;
    private String m_ManegerId; //maybe User?
    private String m_CourseName;
    private Date m_CreationDate;
    private String m_Description;
    private List<String> m_UsersId;
    private List<String> m_AlbumsId;

    public Course(String m_ManegerId, String m_CourseName, Date m_CreationDate) {
        this.m_ManegerId = m_ManegerId;
        this.m_CourseName = m_CourseName;
        this.m_CreationDate = m_CreationDate;
    }

    protected Course(Parcel in) {
        m_Id = in.readString();
        m_ManegerId = in.readString();
        long tempDateAsLong = in.readLong();
        m_CreationDate = tempDateAsLong == -1 ? null : new Date(tempDateAsLong);
        m_CourseName = in.readString();
        m_Description = in.readString();
        m_UsersId = in.createStringArrayList();
        m_AlbumsId = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_Id);
        dest.writeString(m_ManegerId);
        dest.writeLong(m_CreationDate != null ? m_CreationDate.getTime() : -1);
        dest.writeString(m_CourseName);
        dest.writeString(m_Description);
        dest.writeStringList(m_UsersId);
        dest.writeStringList(m_AlbumsId);
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

    public String getM_Id() {
        return m_Id;
    }

    public void setId(String m_Id) {
        this.m_Id = m_Id;
    }

    public String getManegerId() {
        return m_ManegerId;
    }

    public void setManegerId(String m_ManegerId) {
        this.m_ManegerId = m_ManegerId;
    }

    public String getCourseName() {
        return m_CourseName;
    }

    public void setCourseName(String m_CourseName) {
        this.m_CourseName = m_CourseName;
    }

    public Date getCreationDate() {
        return m_CreationDate;
    }

    public String getDescription() {
        return m_Description;
    }

    public void setDescription(String m_Description) {
        this.m_Description = m_Description;
    }

    public List<String> getUsersId() {
        return m_UsersId;
    }

    public void setUsersId(List<String> m_UsersId) {
        this.m_UsersId = m_UsersId;
    }

    public List<String> getAlbumsId() {
        return m_AlbumsId;
    }

    public void setAlbumsId(List<String> m_AlbumsId) {
        this.m_AlbumsId = m_AlbumsId;
    }
}
