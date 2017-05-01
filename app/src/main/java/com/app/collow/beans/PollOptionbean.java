package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Harmis on 09/02/17.
 */

public class PollOptionbean extends Basebean implements Serializable {

    public int getOptionID() {
        return optionID;
    }

    public void setOptionID(int optionID) {
        this.optionID = optionID;
    }

    int optionID=0;
    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isSelectedOptions() {
        return isSelectedOptions;
    }

    public void setSelectedOptions(boolean selectedOptions) {
        isSelectedOptions = selectedOptions;
    }

    String option=null;

    public String getEachOptionPerecentage() {
        return eachOptionPerecentage;
    }

    public void setEachOptionPerecentage(String eachOptionPerecentage) {
        this.eachOptionPerecentage = eachOptionPerecentage;
    }

    String eachOptionPerecentage="0";


    public String getEachOptionVotes() {
        return eachOptionVotes;
    }

    public void setEachOptionVotes(String eachOptionVotes) {
        this.eachOptionVotes = eachOptionVotes;
    }

    String eachOptionVotes="0";

    public String getHint_title() {
        return hint_title;
    }

    public void setHint_title(String hint_title) {
        this.hint_title = hint_title;
    }

    String hint_title=null;

    boolean isSelectedOptions=false;

    public boolean isAdminForThisPoll() {
        return isAdminForThisPoll;
    }

    public void setAdminForThisPoll(boolean adminForThisPoll) {
        isAdminForThisPoll = adminForThisPoll;
    }

    boolean isAdminForThisPoll=false;

    public boolean isNeedToCreateOptions() {
        return isNeedToCreateOptions;
    }

    public void setNeedToCreateOptions(boolean needToCreateOptions) {
        isNeedToCreateOptions = needToCreateOptions;
    }

    boolean isNeedToCreateOptions=false;

    public boolean isEmptyOption() {
        return isEmptyOption;
    }

    public void setEmptyOption(boolean emptyOption) {
        isEmptyOption = emptyOption;
    }

    boolean isEmptyOption=false;

}
