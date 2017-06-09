package com.app.collow.beans;

import com.app.collow.adapters.ImageSlideAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Harmis on 30/01/17.
 */

public class CommunityActivitiesFeedbean extends Basebean implements Serializable {
    String userprofilepic = null;
    String username = null;
    String userid = null;
    String useremail = null;
    String posttime = null;
    String postcontent = null;
    String downloadcount = null;
    String feedcategory = null;
    String activityID = null;


    public CommunityActivitiesFeedbean getCommunityActivitiesFeedbean() {
        return communityActivitiesFeedbean;
    }

    public void setCommunityActivitiesFeedbean(CommunityActivitiesFeedbean communityActivitiesFeedbean) {
        this.communityActivitiesFeedbean = communityActivitiesFeedbean;
    }

    CommunityActivitiesFeedbean communityActivitiesFeedbean=null;

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    String communityID = null;

    public ImageSlideAdapter getImageSlideAdapter() {
        return imageSlideAdapter;
    }

    public void setImageSlideAdapter(ImageSlideAdapter imageSlideAdapter) {
        this.imageSlideAdapter = imageSlideAdapter;
    }

    ImageSlideAdapter imageSlideAdapter=null;

    public boolean isLikedFeed() {
        return isLikedFeed;
    }

    public void setLikedFeed(boolean likedFeed) {
        isLikedFeed = likedFeed;
    }

    boolean isLikedFeed=false;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    String communityName = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title = null;

    public ArrayList<String> getStringArrayList_images_url() {
        return stringArrayList_images_url;
    }

    public void setStringArrayList_images_url(ArrayList<String> stringArrayList_images_url) {
        this.stringArrayList_images_url = stringArrayList_images_url;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    ArrayList<String> stringArrayList_images_url=null;

    public CommunityActivitiesFeedbean() {
    }

    public String getDownloadcount() {
        return downloadcount;
    }

    public void setDownloadcount(String downloadcount) {
        this.downloadcount = downloadcount;
    }

    public String getUserprofilepic() {
        return userprofilepic;
    }

    public void setUserprofilepic(String userprofilepic) {
        this.userprofilepic = userprofilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }


    public String getUserId() {
        return userid;
    }

    public void setUserId(String userid) {
        this.userid = userid;
    }

    public String getUserEmail() {
        return useremail;
    }

    public void setUserEmail(String useremail) {
        this.useremail = useremail;
    }

    public String getFeedcategory() {
        return feedcategory;
    }

    public void setFeedcategory(String feedcategory) {
        this.feedcategory = feedcategory;
    }
}

