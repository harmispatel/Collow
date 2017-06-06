package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 30/01/17.
 */

public class Statesbean extends  Basebean implements Serializable {

    String stateName=null;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    String stateCode=null;

}
