package Logic;

import java.util.Date;
import java.util.List;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

public class Course {
    String m_Id;
    String m_ManegerId; //maybe User?
    String m_CourseName;
    Date m_CreationDate;
    String m_Description;
    List<String> m_UsersId;
    List<String> m_AlbumsId;

    public Course(String m_ManegerId, String m_CourseName, Date m_CreationDate) {
        this.m_ManegerId = m_ManegerId;
        this.m_CourseName = m_CourseName;
        this.m_CreationDate = m_CreationDate;
    }

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
