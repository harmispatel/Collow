package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 31/1/17.
 */

public class Gallerybean    implements Serializable{
    String galleryID=null;
    String userName=null;
    String title=null;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String userID=null;

    public SocialOptionbean getSocialOptionbean() {
        return socialOptionbean;
    }

    public void setSocialOptionbean(SocialOptionbean socialOptionbean) {
        this.socialOptionbean = socialOptionbean;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    SocialOptionbean socialOptionbean=null;
    int position=0;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    String createdDate=null;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    String profilePic=null;

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    String activityID=null;

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    String communityID=null;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGalleryID() {
        return galleryID;
    }

    public void setGalleryID(String galleryID) {
        this.galleryID = galleryID;
    }

    public boolean isLikedByLoggedUser() {
        return isLikedByLoggedUser;
    }

    public void setLikedByLoggedUser(boolean likedByLoggedUser) {
        isLikedByLoggedUser = likedByLoggedUser;
    }

    String albumName=null;
    boolean isLikedByLoggedUser=false;



    public ArrayList<String> getStringArrayList_images_urls() {
        return stringArrayList_images_urls;
    }

    public void setStringArrayList_images_urls(ArrayList<String> stringArrayList_images_urls) {
        this.stringArrayList_images_urls = stringArrayList_images_urls;
    }

    ArrayList<String> stringArrayList_images_urls=new ArrayList<>();


    public Gallerybean() {
    }



}
