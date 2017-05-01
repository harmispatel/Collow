package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 4/2/17.
 */

public class ACListCommunityAdminbean extends Basebean implements Serializable {
    String acadmin_name = null;
    String acadmin_eamil = null;

    public String getAcadmin_userprofilepic() {
        return acadmin_userprofilepic;
    }

    public void setAcadmin_userprofilepic(String acadmin_userprofilepic) {
        this.acadmin_userprofilepic = acadmin_userprofilepic;
    }

    public String getAcadmin_eamil() {
        return acadmin_eamil;
    }

    public void setAcadmin_eamil(String acadmin_eamil) {
        this.acadmin_eamil = acadmin_eamil;
    }

    public String getAcadmin_name() {
        return acadmin_name;
    }

    public void setAcadmin_name(String acadmin_name) {
        this.acadmin_name = acadmin_name;
    }

    String acadmin_userprofilepic = null;
}

