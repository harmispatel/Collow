package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 8/2/17.
 */

public class ACFAQbean extends Basebean implements Serializable {
    String acfaq_image=null;
    String acfaq_faqdate=null;
    String acfaq_faqtime=null;
    String acfaq_faqtiitle=null;

    public ACFAQbean() {
    }

    public String getAcfaq_image() {
        return acfaq_image;
    }

    public void setAcfaq_image(String acfaq_image) {
        this.acfaq_image = acfaq_image;
    }

    public String getAcfaq_faqdate() {
        return acfaq_faqdate;
    }

    public void setAcfaq_faqdate(String acfaq_faqdate) {
        this.acfaq_faqdate = acfaq_faqdate;
    }

    public String getAcfaq_faqtime() {
        return acfaq_faqtime;
    }

    public void setAcfaq_faqtime(String acfaq_faqtime) {
        this.acfaq_faqtime = acfaq_faqtime;
    }

    public String getAcfaq_faqtiitle() {
        return acfaq_faqtiitle;
    }

    public void setAcfaq_faqtiitle(String acfaq_faqtiitle) {
        this.acfaq_faqtiitle = acfaq_faqtiitle;
    }
}
