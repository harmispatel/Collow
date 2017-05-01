package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 1/2/17.
 */

public class Pollsbean extends Basebean implements Serializable{

    String id=null;
    String title=null;
    String createdDate=null;
    String activityID=null;
    boolean isVotedByLoggedUser=false;

    public boolean isAdminForThisPoll() {
        return isAdminForThisPoll;
    }

    public void setAdminForThisPoll(boolean adminForThisPoll) {
        isAdminForThisPoll = adminForThisPoll;
    }

    boolean isAdminForThisPoll=false;

    public boolean isVotedByLoggedUser() {
        return isVotedByLoggedUser;
    }

    public void setVotedByLoggedUser(boolean votedByLoggedUser) {
        isVotedByLoggedUser = votedByLoggedUser;
    }


    public ArrayList<PollOptionbean> getPollOptionbeanArrayList() {
        return pollOptionbeanArrayList;
    }

    public void setPollOptionbeanArrayList(ArrayList<PollOptionbean> pollOptionbeanArrayList) {
        this.pollOptionbeanArrayList = pollOptionbeanArrayList;
    }

    ArrayList<PollOptionbean> pollOptionbeanArrayList=new ArrayList<>();

    public String getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(String totalViews) {
        this.totalViews = totalViews;
    }

    public String getPollVotes() {
        return pollVotes;
    }

    public void setPollVotes(String pollVotes) {
        this.pollVotes = pollVotes;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String totalViews="0";
    String pollVotes="0";

    public Pollsbean() {
    }


}
