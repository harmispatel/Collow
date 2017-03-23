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
