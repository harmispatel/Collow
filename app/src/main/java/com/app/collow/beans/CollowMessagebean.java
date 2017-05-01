package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 20/02/17.
 */

public class CollowMessagebean extends  Basebean implements Serializable {


    String username=null;
    String profilepic=null;
    String content=null;

    public boolean isReadMessage() {
        return isReadMessage;
    }

    public void setReadMessage(boolean readMessage) {
        isReadMessage = readMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    boolean isReadMessage;
    String message_id=null;
    String message_text=null;
    String message_type=null;
    String activityID=null;
    String communityName=null;
    String communityType=null;

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    String community_id=null;
    String date=null;
    boolean isalreadyread=false;
    public boolean isalreadyread() {
        return isalreadyread;
    }

    public void setIsalreadyread(boolean isalreadyread) {
        this.isalreadyread = isalreadyread;
    }






    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
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

    public String getCommunityType() {
        return communityType;
    }

    public void setCommunityType(String communityType) {
        this.communityType = communityType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
