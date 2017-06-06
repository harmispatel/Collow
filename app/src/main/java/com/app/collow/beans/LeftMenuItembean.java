package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 25/01/17.
 */

public class LeftMenuItembean implements Serializable {

    public String getLeftMenuItemName() {
        return leftMenuItemName;
    }

    public void setLeftMenuItemName(String leftMenuItemName) {
        this.leftMenuItemName = leftMenuItemName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    String leftMenuItemName=null;
    int position=0;


    public LeftMenuItembean(String leftMenuItemName)
    {
        this.leftMenuItemName=leftMenuItemName;
    }
}
