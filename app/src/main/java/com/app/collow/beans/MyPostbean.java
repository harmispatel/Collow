package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 25/01/17.
 */

public class MyPostbean extends Basebean implements Serializable{


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isSelected=false;

    public boolean isNeedToVisisbleNow() {
        return isNeedToVisisbleNow;
    }

    public void setNeedToVisisbleNow(boolean needToVisisbleNow) {
        isNeedToVisisbleNow = needToVisisbleNow;
    }

    boolean isNeedToVisisbleNow=false;


    public String getFeedid() {
        return feedid;
    }

    public void setFeedid(String feedid) {
        this.feedid = feedid;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String feedid=null;
    String communityID=null;
    String activityId=null;
    String category=null;
    String title=null;
    String newsDate=null;
    String image=null;




}
