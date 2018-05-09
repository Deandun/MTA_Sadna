package Logic;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

public class PictureAudioData {
    String m_Id;
    String m_CreationDate;
    String m_Description;
    String m_Path;

    public PictureAudioData(String m_Id, String m_CreationDate, String m_Description, String m_Path) {
        this.m_Id = m_Id;
        this.m_CreationDate = m_CreationDate;
        this.m_Description = m_Description;
        this.m_Path = m_Path;
    }

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
}
