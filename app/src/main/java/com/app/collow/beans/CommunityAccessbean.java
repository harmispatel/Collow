package com.app.collow.beans;

import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;

import java.io.Serializable;

/**
 * Created by Harmis on 04/02/17.
 */
//This class handle flag weather user is normal user or admin

public class CommunityAccessbean extends  Basebean implements Serializable {


    boolean isCommunityFollowed=false;

    public boolean isHomeDefualtCommunity() {
        return isHomeDefualtCommunity;
    }

    public void setHomeDefualtCommunity(boolean homeDefualtCommunity) {
        isHomeDefualtCommunity = homeDefualtCommunity;
    }

    public boolean isCommunityClaimedAndApproved() {
        return isCommunityClaimedAndApproved;
    }

    public void setCommunityClaimedAndApproved(boolean communityClaimedAndApproved) {
        isCommunityClaimedAndApproved = communityClaimedAndApproved;
    }

    public boolean isCommunityFollowed() {
        return isCommunityFollowed;
    }

    public void setCommunityFollowed(boolean communityFollowed) {
        isCommunityFollowed = communityFollowed;
    }

    boolean isCommunityClaimedAndApproved =false;
    boolean isHomeDefualtCommunity=false;

    public boolean isClaimedCommunityRequestSent() {
        return isClaimedCommunityRequestSent;
    }

    public void setClaimedCommunityRequestSent(boolean claimedCommunityRequestSent) {
        isClaimedCommunityRequestSent = claimedCommunityRequestSent;
    }

    boolean isClaimedCommunityRequestSent=false;



}
