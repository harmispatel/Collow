package com.app.collow.beansgenerate;

import com.app.collow.beans.CollowMessagebean;
import com.app.collow.beans.Eventbean;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.JSONCommonKeywords;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Harmis on 01/03/17.
 */

public class CallowMessageBuild {
    public static CollowMessagebean getMessagebeanFromJSON(JSONObject jsonObject_single_message) {
        CollowMessagebean collowMessagebean = new CollowMessagebean();
        try {
            if (CommonMethods.handleKeyInJSON(jsonObject_single_message, JSONCommonKeywords.MessageId)) {
                collowMessagebean.setMessage_id(jsonObject_single_message.getString(JSONCommonKeywords.MessageId));
            }
            if (CommonMethods.handleKeyInJSON(jsonObject_single_message, JSONCommonKeywords.MessageType)) {
                collowMessagebean.setMessage_type(jsonObject_single_message.getString(JSONCommonKeywords.MessageType));
            }
            if (CommonMethods.handleKeyInJSON(jsonObject_single_message, JSONCommonKeywords.MessageText)) {
                collowMessagebean.setMessage_text(jsonObject_single_message.getString(JSONCommonKeywords.MessageText));
            }

            if (CommonMethods.handleKeyInJSON(jsonObject_single_message, JSONCommonKeywords.Type)) {
                collowMessagebean.setCommunityType(jsonObject_single_message.getString(JSONCommonKeywords.Type));
            }

            if (CommonMethods.handleKeyInJSON(jsonObject_single_message, JSONCommonKeywords.CommunityId)) {
                collowMessagebean.setCommunity_id(jsonObject_single_message.getString(JSONCommonKeywords.CommunityId));
            }
            if (CommonMethods.handleKeyInJSON(jsonObject_single_message, JSONCommonKeywords.Date)) {
                collowMessagebean.setDate(jsonObject_single_message.getString(JSONCommonKeywords.Date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return collowMessagebean;
    }
    }
