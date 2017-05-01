package com.app.collow.beans;

import com.app.collow.utils.CommonKeywords;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 1/2/17.
 */

public class Eventbean extends Basebean implements Serializable {
    String event_date=null;
    String event_day_name=null;

    public String getEvent_day_date() {
        return event_day_date;
    }

    public void setEvent_day_date(String event_day_date) {
        this.event_day_date = event_day_date;
    }

    public String getEvent_day_name() {
        return event_day_name;
    }

    public void setEvent_day_name(String event_day_name) {
        this.event_day_name = event_day_name;
    }

    String event_day_date=null;

    String event_daytitle=null;
    String event_start_time=null;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }



    String eventType=null;

    public String getEventTypeID() {
        return eventTypeID;
    }

    public void setEventTypeID(String eventTypeID) {
        this.eventTypeID = eventTypeID;
    }

    String eventTypeID=null;

    public int getEvent_time_mode() {
        return event_time_mode;
    }

    public void setEvent_time_mode(int event_time_mode) {
        this.event_time_mode = event_time_mode;
    }

    int event_time_mode= CommonKeywords.EVENT_TIME_MODE_ALL_DAYS;

    public boolean isCustomDate() {
        return isCustomDate;
    }

    public void setCustomDate(boolean customDate) {
        isCustomDate = customDate;
    }

    boolean isCustomDate=false;

    public String getCommmunityID() {
        return commmunityID;
    }

    public void setCommmunityID(String commmunityID) {
        this.commmunityID = commmunityID;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    String commmunityID=null;
    String communityName=null;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    String location=null;

    public String getEvent_end_time() {
        return event_end_time;
    }

    public void setEvent_end_time(String event_end_time) {
        this.event_end_time = event_end_time;
    }

    public String getEvent_start_time() {
        return event_start_time;
    }

    public void setEvent_start_time(String event_start_time) {
        this.event_start_time = event_start_time;
    }

    String event_end_time=null;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    String event_id=null;

    public ArrayList<String> getStringArrayList_images_url() {
        return stringArrayList_images_url;
    }

    public void setStringArrayList_images_url(ArrayList<String> stringArrayList_images_url) {
        this.stringArrayList_images_url = stringArrayList_images_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    ArrayList<String> stringArrayList_images_url=null;
    String username=null;

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    String userProfilePic=null;

    public boolean isEventLikedByMe() {
        return isEventLikedByMe;
    }

    public void setEventLikedByMe(boolean eventLikedByMe) {
        isEventLikedByMe = eventLikedByMe;
    }

    boolean isEventLikedByMe=false;


    String event_title=null;

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    @Override
    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }

    @Override
    public void setServiceSuccess(boolean serviceSuccess) {
        isServiceSuccess = serviceSuccess;
    }

    @Override
    public String getLikeOrDislikeFlag() {
        return likeOrDislikeFlag;
    }

    @Override
    public void setLikeOrDislikeFlag(String likeOrDislikeFlag) {
        this.likeOrDislikeFlag = likeOrDislikeFlag;
    }

    @Override
    public SocialOptionbean getSocialOptionbean() {
        return socialOptionbean;
    }

    @Override
    public void setSocialOptionbean(SocialOptionbean socialOptionbean) {
        this.socialOptionbean = socialOptionbean;
    }

    String activityID=null;
    boolean isServiceSuccess=false;
    String likeOrDislikeFlag="0";
    SocialOptionbean socialOptionbean=null;

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    String event_description=null;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    boolean isLiked=false;
    public Eventbean() {
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }



    public String getEvent_daytitle() {
        return event_daytitle;
    }

    public void setEvent_daytitle(String event_daytitle) {
        this.event_daytitle = event_daytitle;
    }


    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }
}

