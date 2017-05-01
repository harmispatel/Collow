package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 01/02/17.
 */

public class Locationbean extends  Basebean implements Serializable{

    String city=null;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    String state=null;
    String country=null;
    boolean isSuccess=false;
}
