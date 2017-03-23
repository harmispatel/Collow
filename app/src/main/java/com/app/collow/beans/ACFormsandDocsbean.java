package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 23/2/17.
 */

public class ACFormsandDocsbean extends Basebean implements Serializable {

    String aclist_documentdate=null;

    public String getAclist_noofdownloads() {
        return aclist_noofdownloads;
    }

    public void setAclist_noofdownloads(String aclist_noofdownloads) {
        this.aclist_noofdownloads = aclist_noofdownloads;
    }

    String aclist_noofdownloads=null;
    String aclist_documenttiitle=null;

    public String getAclist_documentdescription() {
        return aclist_documentdescription;
    }

    public void setAclist_documentdescription(String aclist_documentdescription) {
        this.aclist_documentdescription = aclist_documentdescription;
    }

    String aclist_documentdescription=null;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public ArrayList<String> getStringArrayList_fileURLs() {
        return stringArrayList_fileURLs;
    }

    public void setStringArrayList_fileURLs(ArrayList<String> stringArrayList_fileURLs) {
        this.stringArrayList_fileURLs = stringArrayList_fileURLs;
    }

    boolean isLiked=false;
    ArrayList<String> stringArrayList_fileURLs=null;
    public ACFormsandDocsbean() {
    }


    public String getAclist_documentdate() {
        return aclist_documentdate;
    }

    public void setAclist_documentdate(String aclist_documentdate) {
        this.aclist_documentdate = aclist_documentdate;
    }



    public String getAclist_documenttiitle() {
        return aclist_documenttiitle;
    }

    public void setAclist_documenttiitle(String aclist_documenttiitle) {
        this.aclist_documenttiitle = aclist_documenttiitle;
    }
}
