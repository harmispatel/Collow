package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by chinkal on 1/21/17.
 */

public class Loginbean extends  Basebean implements Serializable {
//Login

    String userid = null;
    String name = null;

    public boolean isHomeDefualtCommunity() {
        return isHomeDefualtCommunity;
    }

    public void setHomeDefualtCommunity(boolean homeDefualtCommunity) {
        isHomeDefualtCommunity = homeDefualtCommunity;
    }

    boolean isHomeDefualtCommunity=false;


    String first_name = null;
    String last_name = null;
    String profile_pic = null;


    String State = null;
    String Dob = null;


    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getInterest() {
        return Interest;
    }

    public void setInterest(String interest) {
        Interest = interest;
    }

    String Interest = null;


    public String getIscompletedprofile() {
        return iscompletedprofile;
    }

    public void setIscompletedprofile(String iscompletedprofile) {
        this.iscompletedprofile = iscompletedprofile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String email = null;
    String iscompletedprofile = null;


    String homeCommunityId = null;

    public String getHomeCommunityName() {
        return homeCommunityName;
    }

    public void setHomeCommunityName(String homeCommunityName) {
        this.homeCommunityName = homeCommunityName;
    }

    public boolean isUserAssociateWithTeam() {
        return isUserAssociateWithTeam;
    }

    public void setUserAssociateWithTeam(boolean userAssociateWithTeam) {
        isUserAssociateWithTeam = userAssociateWithTeam;
    }

    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    String homeCommunityName = null;
    boolean isUserAssociateWithTeam=false;

    public CommunityAccessbean getCommunityAccessbean() {
        return communityAccessbean;
    }

    public void setCommunityAccessbean(CommunityAccessbean communityAccessbean) {
        this.communityAccessbean = communityAccessbean;
    }

    CommunityAccessbean communityAccessbean=null;






}
