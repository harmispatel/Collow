package com.app.collow.collowinterfaces;

import com.app.collow.beans.Responcebean;

/**
 * Created by chinkal on 1/19/17.
 */

public interface Login {


    public Responcebean doLogin();

    public Responcebean getProfilePicByEmailID(String email);

}

