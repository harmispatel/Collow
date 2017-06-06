package com.app.collow.beans;

/**
 * Created by harmis on 6/1/17.
 */

public class GettingStartedbean {
    String welcomeTitle=null;
    String details=null;

    public String getWelcomeTitle() {
        return welcomeTitle;
    }

    public void setWelcomeTitle(String welcomeTitle) {
        this.welcomeTitle = welcomeTitle;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    int imageResourceID=0;
}
