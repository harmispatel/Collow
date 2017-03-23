package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 1/2/17.
 */

public class Classifiedbean extends Basebean implements Serializable {
    String classified_image=null;
    String classified_category=null;
    String classified_name=null;
    String clssified_price=null;
    String classified_date=null;

    public Classifiedbean() {
    }

    public String getClassified_image() {
        return classified_image;
    }

    public void setClassified_image(String classified_image) {
        this.classified_image = classified_image;
    }

    public String getClassified_category() {
        return classified_category;
    }

    public void setClassified_category(String classified_category) {
        this.classified_category = classified_category;
    }

    public String getClassified_name() {
        return classified_name;
    }

    public void setClassified_name(String classified_name) {
        this.classified_name = classified_name;
    }

    public String getClssified_price() {
        return clssified_price;
    }

    public void setClssified_price(String clssified_price) {
        this.clssified_price = clssified_price;
    }

    public String getClassified_date() {
        return classified_date;
    }

    public void setClassified_date(String classified_date) {
        this.classified_date = classified_date;
    }
}
