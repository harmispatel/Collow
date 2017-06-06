package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by chinkal on 1/24/17.
 */

public class Interestbean implements Serializable {

    String interestID=null;

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public String getInterestID() {
        return interestID;
    }

    public void setInterestID(String interestID) {
        this.interestID = interestID;
    }

    String interestName=null;
    public int getColorcode() {
        return colorcode;
    }

    public void setColorcode(int colorcode) {
        this.colorcode = colorcode;
    }

    int colorcode=0;

    public boolean isInterestSelected() {
        return isInterestSelected;
    }

    public void setInterestSelected(boolean interestSelected) {
        isInterestSelected = interestSelected;
    }

    boolean isInterestSelected=false;
}
