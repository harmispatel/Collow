package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 27/2/17.
 */

public class Chatbean extends Basebean implements Serializable{
    String chat_userprofilepic=null;
    String chat_username=null;
    String chat_time=null;
    String chat_title=null;
    String chat_description=null;
    String message=null;
    int position=0;
    String likeOrDislikeFlag="0";
    boolean isServiceSuccess=false;
    boolean isLiked=false;
    String activityID=null;
    String communityName=null;
    ArrayList<String> stringArrayList_fileURLs=null;
    String chat_id=null;
    String chat_pic=null;
    SocialOptionbean socialOptionbean=null;

    public ArrayList<String> getStringArrayList_images() {
        return stringArrayList_images;
    }

    public void setStringArrayList_images(ArrayList<String> stringArrayList_images) {
        this.stringArrayList_images = stringArrayList_images;
    }

    ArrayList<String> stringArrayList_images=new ArrayList<>();
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    String ownerId=null;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status=null;
    public String getChat_category() {
        return chat_category;
    }

    public void setChat_category(String chat_category) {
        this.chat_category = chat_category;
    }

    public String getChat_price() {
        return chat_price;
    }

    public void setChat_price(String chat_price) {
        this.chat_price = chat_price;
    }

    String chat_category=null;
    String chat_price=null;




    public String getChat_userprofilepic() {
        return chat_userprofilepic;
    }

    public void setChat_userprofilepic(String chat_userprofilepic) {
        this.chat_userprofilepic = chat_userprofilepic;
    }

    public String getChat_username() {
        return chat_username;
    }

    public void setChat_username(String chat_username) {
        this.chat_username = chat_username;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
    }

    public String getChat_title() {
        return chat_title;
    }

    public void setChat_title(String chat_title) {
        this.chat_title = chat_title;
    }

    public String getChat_description() {
        return chat_description;
    }

    public void setChat_description(String chat_description) {
        this.chat_description = chat_description;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
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
    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }

    @Override
    public void setServiceSuccess(boolean serviceSuccess) {
        isServiceSuccess = serviceSuccess;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public ArrayList<String> getStringArrayList_fileURLs() {
        return stringArrayList_fileURLs;
    }

    public void setStringArrayList_fileURLs(ArrayList<String> stringArrayList_fileURLs) {
        this.stringArrayList_fileURLs = stringArrayList_fileURLs;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getChat_pic() {
        return chat_pic;
    }

    public void setChat_pic(String chat_pic) {
        this.chat_pic = chat_pic;
    }

    @Override
    public SocialOptionbean getSocialOptionbean() {
        return socialOptionbean;
    }

    @Override
    public void setSocialOptionbean(SocialOptionbean socialOptionbean) {
        this.socialOptionbean = socialOptionbean;
    }
}
