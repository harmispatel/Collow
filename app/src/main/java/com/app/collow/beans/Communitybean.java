package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by chinkal on 1/24/17.
 */

public class Communitybean implements Serializable {

    String communityID=null;

    public boolean isCommunitySelected() {
        return isCommunitySelected;
    }

    public void setCommunitySelected(boolean communitySelected) {
        isCommunitySelected = communitySelected;
    }

    boolean isCommunitySelected=false;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    String communityName=null;

    public int getColorcode() {
        return colorcode;
    }

    public void setColorcode(int colorcode) {
        this.colorcode = colorcode;
    }

    int colorcode=0;
}
