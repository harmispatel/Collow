package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 11/2/17.
 */

public class MgmtTeamListingbean extends Basebean implements Serializable {
    String mgmtmanagecommunity_name = null;
    String mgmtmanagecommunity_address = null;
    String mgmtmanagecommunity_teamprofilepic = null;

    public MgmtTeamListingbean() {
    }

    public String getMgmtmanagecommunity_name() {
        return mgmtmanagecommunity_name;
    }

    public void setMgmtmanagecommunity_name(String mgmtmanagecommunity_name) {
        this.mgmtmanagecommunity_name = mgmtmanagecommunity_name;
    }

    public String getMgmtmanagecommunity_address() {
        return mgmtmanagecommunity_address;
    }

    public void setMgmtmanagecommunity_address(String mgmtmanagecommunity_address) {
        this.mgmtmanagecommunity_address = mgmtmanagecommunity_address;
    }

    public String getMgmtmanagecommunity_teamprofilepic() {
        return mgmtmanagecommunity_teamprofilepic;
    }

    public void setMgmtmanagecommunity_teamprofilepic(String mgmtmanagecommunity_teamprofilepic) {
        this.mgmtmanagecommunity_teamprofilepic = mgmtmanagecommunity_teamprofilepic;
    }

    public boolean isCommunity() {
        return isCommunity;
    }

    public void setCommunity(boolean community) {
        isCommunity = community;
    }

    boolean isCommunity=false;
}
