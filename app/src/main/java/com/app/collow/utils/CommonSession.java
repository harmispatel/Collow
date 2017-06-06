package com.app.collow.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CommonSession {

    private static final String COMPARESESSION_PREFENCE_NAME = "collow_my_common_session";
    private static final String LOGGED_USER_USERID = "collow_userID";
    private static final String LOGIN_JSON_CONTENT = "login_json_content";
    private static final String USER_PROFILE_COMPLETED = "user_profile_completed";
    private static final String USER_IS_ADMIN_OR_NOT = "user_is_admin_or_not";
    private static final String USER_IS_FOLLOW_OR_NOT = "user_is_follow_or_not";

    private static final String IF_USER_HAS_SET_HOME_COMMUNITY = "user_set_home_community";

    private static final String STATE_NAME = "state_name";
    private static final String STATE_ID = "state_id";

    private static final String COMMUNITY_TYPE_NAME = "community_type";
    private static final String COMMUNITY_TYPE_ID = "community_id";

    private static final String CITY_NAME = "city_name";
    private static final String CURRENT_COMMUNITY_ID = "current_community_id";
    private static final String SEARCH_COMMUNITY = "search_community";
    private static final String USER_ASSOCIATE_WITH_ANY_TEAM = "user_associate_with_any_team";

    private SharedPreferences sharedPref;
    private Editor editor;


    // constructor

    @SuppressLint("CommitPrefEdits")
    public CommonSession(Context activity) {
        sharedPref = activity.getSharedPreferences(
                COMPARESESSION_PREFENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

    }

    @SuppressLint("CommitPrefEdits")
    public CommonSession(Activity mfragmentactivity) {
        sharedPref = mfragmentactivity.getSharedPreferences(
                COMPARESESSION_PREFENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

    }


    // device width or height



    // lSearchCom
    public String getSearchCom() {
        return sharedPref.getString(SEARCH_COMMUNITY, null);
    }

    public void storeSearchCom(String commu) {
        try {
            editor.putString(SEARCH_COMMUNITY, commu);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetSearchCom() {
        try {
            editor.putString(SEARCH_COMMUNITY, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // login user email
    public String getLoggedUserID() {
        return sharedPref.getString(LOGGED_USER_USERID, null);
    }

    public void storeLoggedUserID(String user_email) {
        try {
            editor.putString(LOGGED_USER_USERID, user_email);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLoggedUserID() {
        try {
            editor.putString(LOGGED_USER_USERID, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // login user user name
    public String getLoginJsonContent() {
        return sharedPref.getString(LOGIN_JSON_CONTENT, null);
    }

    public void storeLoginJsonCOntent(String logincontent) {
        try {
            editor.putString(LOGIN_JSON_CONTENT, logincontent);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLoginJsonCOntent() {
        try {
            editor.putString(LOGIN_JSON_CONTENT, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






//this user had completed rest of profile flag

    public boolean getUserProfileCompleted() {
        return sharedPref.getBoolean(USER_PROFILE_COMPLETED, false);
    }

    public void storeUserProfileCompleted(boolean isProfileCompleted) {
        try {
            editor.putBoolean(USER_PROFILE_COMPLETED, isProfileCompleted);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetUserProfileCompleted() {
        try {
            editor.putBoolean(USER_PROFILE_COMPLETED, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //this flag checked user is now admin or normal user
    public boolean isUserAdminNow() {
        return sharedPref.getBoolean(USER_IS_ADMIN_OR_NOT, false);
    }

    public void storeisUserAdminNow(boolean isProfileCompleted) {
        try {
            editor.putBoolean(USER_IS_ADMIN_OR_NOT, isProfileCompleted);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetisUserAdminNow() {
        try {
            editor.putBoolean(USER_IS_ADMIN_OR_NOT, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    public boolean isUserFollow() {
        return sharedPref.getBoolean(USER_IS_FOLLOW_OR_NOT, false);
    }

    public void storeUserFollow(boolean isFollow) {
        try {
            editor.putBoolean(USER_IS_FOLLOW_OR_NOT, isFollow);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetisUserFollow() {
        try {
            editor.putBoolean(USER_IS_FOLLOW_OR_NOT, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //state
    public String getStateName() {
        return sharedPref.getString(STATE_NAME, "");
    }

    public void storeStateName(String stateName) {
        try {
            editor.putString(STATE_NAME, stateName);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetStateName() {
        try {
            editor.putString(STATE_NAME, "");
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //state id
    public String getStateId() {
        return sharedPref.getString(STATE_ID, "");
    }

    public void storeStateId(String stateID) {
        try {
            editor.putString(STATE_ID, stateID);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetStateId() {
        try {
            editor.putString(STATE_ID, "");
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //user associate any with team
    public boolean isUserAssociateWithTeam() {
        return sharedPref.getBoolean(USER_ASSOCIATE_WITH_ANY_TEAM, false);
    }

    public void storeUserAssociateWithTeam(boolean isUserAssociateWithTeam) {
        try {
            editor.putBoolean(USER_ASSOCIATE_WITH_ANY_TEAM, isUserAssociateWithTeam);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetUserAssociateWithTeam() {
        try {
            editor.putBoolean(USER_ASSOCIATE_WITH_ANY_TEAM, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    //community id


    public String getCommunityTypeId() {
        return sharedPref.getString(COMMUNITY_TYPE_ID, "");
    }

    public void storeCommunityTypeId(String community_type_id) {
        try {
            editor.putString(COMMUNITY_TYPE_ID, community_type_id);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCommunityTypeId() {
        try {
            editor.putString(COMMUNITY_TYPE_ID, "");
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //community type

    public String getCommunityTypeName() {
        return sharedPref.getString(COMMUNITY_TYPE_NAME, "");
    }

    public void storeCommunityTypeName(String community_type_name) {
        try {
            editor.putString(COMMUNITY_TYPE_NAME, community_type_name);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCommunityTypeName() {
        try {
            editor.putString(COMMUNITY_TYPE_NAME, "");
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //city
    public String getCityName() {
        return sharedPref.getString(CITY_NAME, "");
    }

    public void storeCityName(String cityName) {
        try {
            editor.putString(CITY_NAME, cityName);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCityName() {
        try {
            editor.putString(CITY_NAME, "");
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //current community id

    public String getCurrentCommunityId() {
        return sharedPref.getString(CURRENT_COMMUNITY_ID, null);
    }

    public void storeCurrentCommunityId(String current_communityID) {
        try {
            editor.putString(CURRENT_COMMUNITY_ID, current_communityID);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCurrentCommunityId() {
        try {
            editor.putString(CURRENT_COMMUNITY_ID, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    public boolean isUserSetHomeCommunity() {
        return sharedPref.getBoolean(IF_USER_HAS_SET_HOME_COMMUNITY, false);
    }

    public void storeUserSetHomeCommunity(boolean isUserSetHomeCommunity) {
        try {
            editor.putBoolean(IF_USER_HAS_SET_HOME_COMMUNITY, isUserSetHomeCommunity);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetUserSetHomeCommunity() {
        try {
            editor.putBoolean(IF_USER_HAS_SET_HOME_COMMUNITY, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
