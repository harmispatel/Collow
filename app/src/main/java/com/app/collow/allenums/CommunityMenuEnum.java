package com.app.collow.allenums;

/**
 * Created by Harmis on 01/02/17.
 */

public enum CommunityMenuEnum {


    FEED(1),
    CLASSFIED(2),
    NEWS(3),
    CHAT(4),
    EVENTS(5),
    GALLERY(6),
    POLLS(7),
    FORMS(8),
    INFO(9);




    int menuIndex = 1;

    CommunityMenuEnum(int menuIndex) {
        this.menuIndex = menuIndex;
    }

    public int getScrenIndex() {

        return this.menuIndex;
    }


}