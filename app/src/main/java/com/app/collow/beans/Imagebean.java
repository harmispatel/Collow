package com.app.collow.beans;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Harmis on 04/02/17.
 */

public class Imagebean implements Serializable {

    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isPlusIconNeedToEnable() {
        return isPlusIconNeedToEnable;
    }

    public void setPlusIconNeedToEnable(boolean plusIconNeedToEnable) {
        isPlusIconNeedToEnable = plusIconNeedToEnable;
    }

    boolean isPlusIconNeedToEnable=false;

    public boolean isItComesFromServer() {
        return isItComesFromServer;
    }

    public void setItComesFromServer(boolean itComesFromServer) {
        isItComesFromServer = itComesFromServer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    boolean isItComesFromServer=false;
    String url=null;

}
