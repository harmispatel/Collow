package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 1/2/17.
 */

public class FormsAndDocsbean extends Basebean implements Serializable {
    String formsanddocs_date=null;

    public ArrayList<String> getStringArrayList_fileURLs() {
        return stringArrayList_fileURLs;
    }

    public void setStringArrayList_fileURLs(ArrayList<String> stringArrayList_fileURLs) {
        this.stringArrayList_fileURLs = stringArrayList_fileURLs;
    }
    boolean isLiked=false;
    ArrayList<String> stringArrayList_fileURLs=null;
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }


    public String getFormsanddocs_title() {
        return formsanddocs_title;
    }

    public void setFormsanddocs_title(String formsanddocs_title) {
        this.formsanddocs_title = formsanddocs_title;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    String activityID=null;
    String formsanddocs_title=null;
    String formsanddocs_download=null;

    public String getFormsanddocs_description() {
        return formsanddocs_description;
    }

    public void setFormsanddocs_description(String formsanddocs_description) {
        this.formsanddocs_description = formsanddocs_description;
    }

    String formsanddocs_description=null;


    public FormsAndDocsbean() {
    }

    public String getFormsanddocs_date() {
        return formsanddocs_date;
    }

    public void setFormsanddocs_date(String formsanddocs_date) {
        this.formsanddocs_date = formsanddocs_date;
    }



    public String getFormsanddocs_download() {
        return formsanddocs_download;
    }

    public void setFormsanddocs_download(String formsanddocs_download) {
        this.formsanddocs_download = formsanddocs_download;
    }
}
