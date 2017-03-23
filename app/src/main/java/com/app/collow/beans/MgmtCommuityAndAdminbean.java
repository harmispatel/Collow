package com.app.collow.beans;

import java.io.Serializable;

import javax.crypto.SecretKey;

/**
 * Created by harmis on 11/2/17.
 */

public class MgmtCommuityAndAdminbean extends Basebean implements Serializable {
    String mgmtmanageteam_name = null;
    String mgmtmanageteam_address = null;
    String mgmtmanageteamn_teamprofilepic = null;

    public MgmtCommuityAndAdminbean() {
    }

    public String getMgmtmanageteam_name() {
        return mgmtmanageteam_name;
    }

    public void setMgmtmanageteam_name(String mgmtmanageteam_name) {
        this.mgmtmanageteam_name = mgmtmanageteam_name;
    }

    public String getMgmtmanageteam_address() {
        return mgmtmanageteam_address;
    }

    public void setMgmtmanageteam_address(String mgmtmanageteam_address) {
        this.mgmtmanageteam_address = mgmtmanageteam_address;
    }

    public String getMgmtmanageteamn_teamprofilepic() {
        return mgmtmanageteamn_teamprofilepic;
    }

    public void setMgmtmanageteamn_teamprofilepic(String mgmtmanageteamn_teamprofilepic) {
        this.mgmtmanageteamn_teamprofilepic = mgmtmanageteamn_teamprofilepic;
    }
}
