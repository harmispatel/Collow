package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 12/1/17.
 */

public class Followingbean extends Basebean implements Serializable {


    String communityID = null;
    String communtiyImageURL = null;
    String communityName = null;
    String latitude = null;
    String longitude = null;
    String address = null;
    String city = null;
    String state = null;
    String isFollowedCommunity = null;
    String IsHomeCommunity = null;
    String country = null;
    CommunityAccessbean communityAccessbean = null;
    String fullAddress = null;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommuntiyImageURL() {
        return communtiyImageURL;
    }

    public void setCommuntiyImageURL(String communtiyImageURL) {
        this.communtiyImageURL = communtiyImageURL;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public CommunityAccessbean getCommunityAccessbean() {
        return communityAccessbean;
    }

    public void setCommunityAccessbean(CommunityAccessbean communityAccessbean) {
        this.communityAccessbean = communityAccessbean;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getIsFollowedCommunity() {
        return isFollowedCommunity;
    }

    public void setIsFollowedCommunity(String isFollowedCommunity) {
        this.isFollowedCommunity = isFollowedCommunity;
    }

    public String getIsHomeCommunity() {
        return IsHomeCommunity;
    }

    public void setIsHomeCommunity(String isHomeCommunity) {
        IsHomeCommunity = isHomeCommunity;
    }
}
