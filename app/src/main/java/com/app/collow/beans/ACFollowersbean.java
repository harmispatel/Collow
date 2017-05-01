package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 8/2/17.
 */

public class ACFollowersbean extends Basebean implements Serializable {
    String foloowers_profilepic=null;
    String followers_name=null;
    String followers_address=null;
    String followers_type=null;
    String communityID=null;
    String followersUserID=null;

    public int getPosition_main_item() {
        return position_main_item;
    }

    public void setPosition_main_item(int position_main_item) {
        this.position_main_item = position_main_item;
    }

    int position_main_item=0;

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    boolean isApproved=false;



    public String getFollowersUserID() {
        return followersUserID;
    }

    public void setFollowersUserID(String followersUserID) {
        this.followersUserID = followersUserID;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }


    public ACFollowersbean() {
    }

    public String getFoloowers_profilepic() {
        return foloowers_profilepic;
    }

    public void setFoloowers_profilepic(String foloowers_profilepic) {
        this.foloowers_profilepic = foloowers_profilepic;
    }

    public String getFollowers_name() {
        return followers_name;
    }

    public void setFollowers_name(String followers_name) {
        this.followers_name = followers_name;
    }

    public String getFollowers_address() {
        return followers_address;
    }

    public void setFollowers_address(String followers_address) {
        this.followers_address = followers_address;
    }

    public String getFollowers_type() {
        return followers_type;
    }

    public void setFollowers_type(String followers_type) {
        this.followers_type = followers_type;
    }
}
