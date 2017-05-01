package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 16/02/17.
 */

public class ClaimCommunitybean extends   Basebean implements Serializable{


    String mgmtID=null;
    String mgmtName=null;

    public boolean isNeedToTrueIconDisplay() {
        return isNeedToTrueIconDisplay;
    }

    public void setNeedToTrueIconDisplay(boolean needToTrueIconDisplay) {
        isNeedToTrueIconDisplay = needToTrueIconDisplay;
    }

    public String getMgmtName() {
        return mgmtName;
    }

    public void setMgmtName(String mgmtName) {
        this.mgmtName = mgmtName;
    }

    public String getMgmtID() {
        return mgmtID;
    }

    public void setMgmtID(String mgmtID) {
        this.mgmtID = mgmtID;
    }

    boolean isNeedToTrueIconDisplay=false;
}
