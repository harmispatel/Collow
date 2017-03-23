package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 1/2/17.
 */

public class FormsAndDocsbean extends Basebean implements Serializable {
    String formsanddocs_date=null;
    String formsanddocs_category=null;
    String formsanddocs_download=null;


    public FormsAndDocsbean() {
    }

    public String getFormsanddocs_date() {
        return formsanddocs_date;
    }

    public void setFormsanddocs_date(String formsanddocs_date) {
        this.formsanddocs_date = formsanddocs_date;
    }

    public String getFormsanddocs_category() {
        return formsanddocs_category;
    }

    public void setFormsanddocs_category(String formsanddocs_category) {
        this.formsanddocs_category = formsanddocs_category;
    }

    public String getFormsanddocs_download() {
        return formsanddocs_download;
    }

    public void setFormsanddocs_download(String formsanddocs_download) {
        this.formsanddocs_download = formsanddocs_download;
    }
}
