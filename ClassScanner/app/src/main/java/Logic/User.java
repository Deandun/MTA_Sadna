package Logic;

import java.util.List;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

//TODO: Add parcelable implementation
public class User {

    private String m_Id;
    private String m_UserName;
    private String m_Mail;
    private String m_NickName;
    private List <String> m_CoursesId;

    public User(String m_UserName, String m_Mail) {
        this.m_UserName = m_UserName;
        this.m_Mail = m_Mail;
    }

    public String getId() {
        return m_Id;
    }

    public void setId(String m_Id) {
        this.m_Id = m_Id;
    }

    public String getUserName() {
        return m_UserName;
    }

    public void setUserName(String m_UserName) {
        this.m_UserName = m_UserName;
    }

    public String getMail() {
        return m_Mail;
    }

    public void setMail(String m_Mail) {
        this.m_Mail = m_Mail;
    }

    public String getNickName() {
        return m_NickName;
    }

    public void setNickName(String m_NickName) {
        this.m_NickName = m_NickName;
    }

    public List<String> getCoursesId() {
        return m_CoursesId;
    }

    public void setCoursesId(List<String> m_CoursesId) {
        this.m_CoursesId = m_CoursesId;
    }
}
