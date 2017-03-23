package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 24/2/17.
 */

public class ACViewCommunityInfobean extends Basebean implements Serializable {
    String viewcommunity_profilepic=null;
    String viewcommunity_type=null;
    String viewcommunity_address=null;
    String viewcommunity_phone=null;
    String viewcommunity_fax=null;
    String viewcommunity_email=null;
    String viewcommunity_yearbuilt=null;
    String viewcommunity_description=null;

    public ArrayList<String> getStringArrayList_images() {
        return stringArrayList_images;
    }

    public void setStringArrayList_images(ArrayList<String> stringArrayList_images) {
        this.stringArrayList_images = stringArrayList_images;
    }

    ArrayList<String> stringArrayList_images=new ArrayList<>();
    public String getViewcommunity_homeno() {
        return viewcommunity_homeno;
    }

    public void setViewcommunity_homeno(String viewcommunity_homeno) {
        this.viewcommunity_homeno = viewcommunity_homeno;
    }

    String viewcommunity_homeno=null;
    String viewcommunity_apartmentname=null;


    public ACViewCommunityInfobean() {
    }

    public String getViewcommunity_profilepic() {
        return viewcommunity_profilepic;
    }

    public void setViewcommunity_profilepic(String viewcommunity_profilepic) {
        this.viewcommunity_profilepic = viewcommunity_profilepic;
    }

    public String getViewcommunity_type() {
        return viewcommunity_type;
    }

    public void setViewcommunity_type(String viewcommunity_type) {
        this.viewcommunity_type = viewcommunity_type;
    }

    public String getViewcommunity_address() {
        return viewcommunity_address;
    }

    public void setViewcommunity_address(String viewcommunity_address) {
        this.viewcommunity_address = viewcommunity_address;
    }

    public String getViewcommunity_phone() {
        return viewcommunity_phone;
    }

    public void setViewcommunity_phone(String viewcommunity_phone) {
        this.viewcommunity_phone = viewcommunity_phone;
    }

    public String getViewcommunity_fax() {
        return viewcommunity_fax;
    }

    public void setViewcommunity_fax(String viewcommunity_fax) {
        this.viewcommunity_fax = viewcommunity_fax;
    }

    public String getViewcommunity_email() {
        return viewcommunity_email;
    }

    public void setViewcommunity_email(String viewcommunity_email) {
        this.viewcommunity_email = viewcommunity_email;
    }

    public String getViewcommunity_yearbuilt() {
        return viewcommunity_yearbuilt;
    }

    public void setViewcommunity_yearbuilt(String viewcommunity_yearbuilt) {
        this.viewcommunity_yearbuilt = viewcommunity_yearbuilt;
    }

    public String getViewcommunity_description() {
        return viewcommunity_description;
    }

    public void setViewcommunity_description(String viewcommunity_description) {
        this.viewcommunity_description = viewcommunity_description;
    }


    public String getViewcommunity_apartmentname() {
        return viewcommunity_apartmentname;
    }

    public void setViewcommunity_apartmentname(String viewcommunity_apartmentname) {
        this.viewcommunity_apartmentname = viewcommunity_apartmentname;
    }
}
