package Logic;

import java.util.List;

/**
 * Created by galbenabu1 on 08/05/2018.
 */

public class Album {
    String m_Id;
    String m_AlbumName;
    String m_CreationDate;
    String m_Description;
    int m_NumOfPictures;
    List <PictureAudioData> m_Pictures;
    List <PictureAudioData> m_Audio;

    public Album(String m_Id, String m_AlbumName, String m_CreationDate) {
        this.m_Id = m_Id;
        this.m_AlbumName = m_AlbumName;
        this.m_CreationDate = m_CreationDate;
    }

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
