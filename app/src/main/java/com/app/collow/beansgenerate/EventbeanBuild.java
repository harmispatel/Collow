package com.app.collow.beansgenerate;

import com.app.collow.beans.Eventbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.JSONCommonKeywords;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Harmis on 25/02/17.
 */

public class EventbeanBuild {


    public static Eventbean getEventbeanFromJSON(JSONObject jsonObject_single_event) {
        Eventbean eventbean = new Eventbean();


        try {
            if (jsonObject_single_event.has(JSONCommonKeywords.ActivityId)) {
                eventbean.setActivityID(jsonObject_single_event.getString(JSONCommonKeywords.ActivityId));
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.Title)) {
                eventbean.setEvent_title(jsonObject_single_event.getString(JSONCommonKeywords.Title));
            }

            if (jsonObject_single_event.has(JSONCommonKeywords.EventId)) {
                eventbean.setEvent_id(jsonObject_single_event.getString(JSONCommonKeywords.EventId));
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.Description)) {
                eventbean.setEvent_description(jsonObject_single_event.getString(JSONCommonKeywords.Description));
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.EventDate)) {



                if(CommonMethods.isTextAvailable(jsonObject_single_event.getString(JSONCommonKeywords.EventDate)))
                {
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                    Date dt1 = null;
                    try {
                        dt1 = format1.parse(jsonObject_single_event.getString(JSONCommonKeywords.EventDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar c = Calendar.getInstance();
                    c.setTime(dt1);
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    String weekDay = null;
                    if (Calendar.MONDAY == dayOfWeek) weekDay = "monday";
                    else if (Calendar.TUESDAY == dayOfWeek) weekDay = "tuesday";
                    else if (Calendar.WEDNESDAY == dayOfWeek) weekDay = "wednesday";
                    else if (Calendar.THURSDAY == dayOfWeek) weekDay = "thursday";
                    else if (Calendar.FRIDAY == dayOfWeek) weekDay = "friday";
                    else if (Calendar.SATURDAY == dayOfWeek) weekDay = "saturday";
                    else if (Calendar.SUNDAY == dayOfWeek) weekDay = "sunday";

                    SimpleDateFormat sdfmt2= new SimpleDateFormat("dd");
                    String strOutput = sdfmt2.format( dt1);

                    eventbean.setEvent_day_name(weekDay.toUpperCase());
                    eventbean.setEvent_day_date(strOutput);



                    eventbean.setEvent_date(jsonObject_single_event.getString(JSONCommonKeywords.EventDate));

                    String dateChanged = CommonMethods.changeDateFormat(eventbean.getEvent_date(), 1);
                    if (dateChanged != null) {
                        eventbean.setEvent_date(dateChanged);
                    }
                }





            }


            if (jsonObject_single_event.has(JSONCommonKeywords.EventStartTime)) {
                eventbean.setEvent_start_time(jsonObject_single_event.getString(JSONCommonKeywords.EventStartTime));
            }

            if (jsonObject_single_event.has(JSONCommonKeywords.EventEndTime)) {
                eventbean.setEvent_end_time(jsonObject_single_event.getString(JSONCommonKeywords.EventEndTime));
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.EventLocation)) {
                eventbean.setLocation(jsonObject_single_event.getString(JSONCommonKeywords.EventLocation));
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.ProfilePic)) {
                eventbean.setUserProfilePic(jsonObject_single_event.getString(JSONCommonKeywords.ProfilePic));
            }

            if (jsonObject_single_event.has(JSONCommonKeywords.eventTimeMode)) {
                eventbean.setEvent_time_mode(jsonObject_single_event.getInt(JSONCommonKeywords.eventTimeMode));
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.EventType)) {
                eventbean.setEventType(jsonObject_single_event.getString(JSONCommonKeywords.EventType));
            }

            if (jsonObject_single_event.has(JSONCommonKeywords.EventTypID)) {
                eventbean.setEventTypeID(jsonObject_single_event.getString(JSONCommonKeywords.EventTypID));
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.UserName)) {
                eventbean.setUsername(jsonObject_single_event.getString(JSONCommonKeywords.UserName));
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.CommunityName)) {
                eventbean.setCommunityName(jsonObject_single_event.getString(JSONCommonKeywords.CommunityName));
            }

            SocialOptionbean socialOptionbean = CommonMethods.getSocialOptionbean(jsonObject_single_event);

            if (socialOptionbean != null) {
                eventbean.setSocialOptionbean(socialOptionbean);
            }


            if (jsonObject_single_event.has(JSONCommonKeywords.isLiked)) {


                String islikedfeedString = jsonObject_single_event.getString(JSONCommonKeywords.isLiked);
                if (islikedfeedString == null || islikedfeedString.equals("")) {
                    eventbean.setEventLikedByMe(false);
                } else if (islikedfeedString.equals("1")) {
                    eventbean.setEventLikedByMe(true);

                } else {
                    eventbean.setEventLikedByMe(false);

                }


            }

            ArrayList<String> images_of_classified = new ArrayList<>();

            if (CommonMethods.handleKeyInJSON(jsonObject_single_event, JSONCommonKeywords.Image))

            {
                if (CommonMethods.checkJSONArrayHasData(jsonObject_single_event, JSONCommonKeywords.Image)) {
                    JSONArray jsonArray_images = jsonObject_single_event.getJSONArray(JSONCommonKeywords.Image);
                    for (int j = 0; j < jsonArray_images.length(); j++) {

                        if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                            images_of_classified.add(jsonArray_images.getString(j));
                        }
                    }

                    eventbean.setStringArrayList_images_url(images_of_classified);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return eventbean;
    }
}
