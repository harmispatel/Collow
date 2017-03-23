package com.app.collow.beans;

import java.io.Serializable;

import javax.crypto.SecretKey;

/**
 * Created by harmis on 23/2/17.
 */

public class ACCommunityFeaturebean extends Basebean implements Serializable {
    String title=null;
    Boolean Flag=false;

    public ACCommunityFeaturebean() {

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getFlag() {
        return Flag;
    }

    public void setFlag(Boolean flag) {
        Flag = flag;
    }
}
