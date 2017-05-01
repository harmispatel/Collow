package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 1/2/17.
 */

public class Classifiedbean extends Basebean implements Serializable {
    String classified_category=null;
    String classified_name=null;
    String clssified_price=null;
    String classified_date=null;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    String ownerId=null;
    String classifiedID=null;

    String status=null;
    ArrayList<String> stringArrayList_images=new ArrayList<>();

    public String getClassifiedDescription() {
        return classifiedDescription;
    }

    public void setClassifiedDescription(String classifiedDescription) {
        this.classifiedDescription = classifiedDescription;
    }

    String classifiedDescription=null;


    public String getClassifiedID() {
        return classifiedID;
    }

    public void setClassifiedID(String classifiedID) {
        this.classifiedID = classifiedID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




    public ArrayList<String> getStringArrayList_images() {
        return stringArrayList_images;
    }

    public void setStringArrayList_images(ArrayList<String> stringArrayList_images) {
        this.stringArrayList_images = stringArrayList_images;
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
