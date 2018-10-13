package Logic.Managers.AnalyticsManager.EventParams;

public class PictureEventParams {
    public PictureEventParams(String mPictureID) {
        this.mPictureID = mPictureID;
    }

    public PictureEventParams() { }

    private String mPictureID;

    public String getmPictureID() {
        return mPictureID;
    }
}
