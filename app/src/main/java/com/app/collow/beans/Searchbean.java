package com.app.collow.beans;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Harmis on 26/01/17.
 */

public class Searchbean implements Serializable {

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    String communityName=null;
    String latitude="0.0";
    String longitude="0.0";

    public String getCommuntiyImageURL() {
        return communtiyImageURL;
    }

    public void setCommuntiyImageURL(String communtiyImageURL) {
        this.communtiyImageURL = communtiyImageURL;
    }

    String communtiyImageURL=null;
    String communityID=null;

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }





    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

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

    public String getPageToken() {
        return pageToken;
    }

    public void setPageToken(String pageToken) {
        this.pageToken = pageToken;
    }

    private String pageToken = "";

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    String address=null;
    String city=null;
    String state=null;

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    String placeid="";

    String country=null;

    String searchText="";

    public CommunityAccessbean getCommunityAccessbean() {
        return communityAccessbean;
    }

    public void setCommunityAccessbean(CommunityAccessbean communityAccessbean) {
        this.communityAccessbean = communityAccessbean;
    }

    CommunityAccessbean communityAccessbean=null;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {

        this.searchText = searchText;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCityText() {
        return cityText;
    }

    public void setCityText(String cityText) {
        this.cityText = cityText;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    public String getCommunityTypeID() {
        return communityTypeID;
    }

    public void setCommunityTypeID(String communityTypeID) {
        this.communityTypeID = communityTypeID;
    }

    public String getCommunityTypeText() {
        return communityTypeText;
    }

    public void setCommunityTypeText(String communityTypeText) {
        this.communityTypeText = communityTypeText;
    }

    public String getIsCommuntiySearchByName() {
        return isCommuntiySearchByName;
    }

    public void setIsCommuntiySearchByName(String isCommuntiySearchByName) {
        this.isCommuntiySearchByName = isCommuntiySearchByName;
    }

    public String getCurrentlat() {
        return currentlat;
    }

    public void setCurrentlat(String currentlat) {
        this.currentlat = currentlat;
    }

    public String getCurrentlong() {
        return currentlong;
    }

    public void setCurrentlong(String currentlong) {
        this.currentlong = currentlong;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String cityID="";
    String cityText="";
    String stateID="";
    String stateText="";
    String communityTypeID="";
    String communityTypeText="";
    int start_limit=0;
    String isCommuntiySearchByName="";
    String currentlat="";
    String currentlong="";
    String user_id="";







  public  Searchbean(String communityName,String a)
    {
        this.communityName=communityName;

    }
    public  Searchbean()
    {
        this.communityName=communityName;

    }





    public int getStart_limit() {
        return start_limit;
    }

    public void setStart_limit(int start_limit) {
        this.start_limit = start_limit;
    }







    // {"":"1","":"23.72","":"72.25","SearchText":"communtiysearch","CityID":"1",
    // "CityText":"","StateID":"id","StateText":"text","CommunityTypeID":"1","CommunityTypeText":"text","StartLimit":"0"}
}
