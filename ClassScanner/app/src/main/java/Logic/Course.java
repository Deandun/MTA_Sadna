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
    private String m_UserID; //maybe User?
    private String m_CourseName;
    private String mCreatorName;
    private Date m_CreationDate;
    private String m_Description;
    private List<String> m_UsersId;
    private List<String> m_AlbumsId;

    public Course(String courseID, String userID, String courseName, Date creationDate) {
        this.m_Id = courseID;
        this.m_UserID = userID;
        this.m_CourseName = courseName;
        this.m_CreationDate = creationDate;
    }

    protected Course(Parcel in) {
        m_Id = in.readString();
        m_UserID = in.readString();
        long tempDateAsLong = in.readLong();
        m_CreationDate = tempDateAsLong == -1 ? null : new Date(tempDateAsLong);
        m_CourseName = in.readString();
        mCreatorName = in.readString();
        m_Description = in.readString();
        m_UsersId = in.createStringArrayList();
        m_AlbumsId = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_Id);
        dest.writeString(m_UserID);
        dest.writeLong(m_CreationDate != null ? m_CreationDate.getTime() : -1);
        dest.writeString(m_CourseName);
        dest.writeString(mCreatorName);
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

    public String getID() { return this.m_Id; }

    public void setId(String id) {
        this.m_Id = id;
    }

    public String getManegerId() {
        return m_UserID;
    }

    public void setManegerId(String manegerId) {
        this.m_UserID = manegerId;
    }

    public String getCourseName() {
        return m_CourseName;
    }

    public void setCourseName(String courseName) {
        this.m_CourseName = courseName;
    }

    public Date getCreationDate() {
        return m_CreationDate;
    }

    public String getDescription() {
        return m_Description;
    }

    public void setDescription(String description) {
        this.m_Description = description;
    }

    public List<String> getUsersId() {
        return m_UsersId;
    }

    public void setUsersId(List<String> usersId) {
        this.m_UsersId = usersId;
    }

    public List<String> getAlbumsId() {
        return m_AlbumsId;
    }

    public void setAlbumsId(List<String> albumsId) {
        this.m_AlbumsId = albumsId;
    }

    public String getCreatorName(){ return this.mCreatorName; }

    public void setCreatorName(String creatorName) {
        this.mCreatorName = creatorName;
    }
}
