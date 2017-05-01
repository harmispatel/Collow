package com.app.collow.beansgenerate;

import android.app.Activity;

import com.app.collow.beans.PollOptionbean;
import com.app.collow.beans.Pollsbean;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.JSONCommonKeywords;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Harmis on 27/02/17.
 */

public class PollsbeanBuild {

    public static Pollsbean getPollsbean(JSONObject jsonObject_single_poll, Activity activity) {
        Pollsbean pollsbean = new Pollsbean();

        try {
            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.PollID)) {
                pollsbean.setId(jsonObject_single_poll.getString(JSONCommonKeywords.PollID));
            }


            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.Polltitle)) {
                pollsbean.setTitle(jsonObject_single_poll.getString(JSONCommonKeywords.Polltitle));
            }


            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.PollDate)) {
                pollsbean.setCreatedDate(jsonObject_single_poll.getString(JSONCommonKeywords.PollDate));

                if (CommonMethods.isTextAvailable(pollsbean.getCreatedDate())) {
                    String date = CommonMethods.dateConversionInAgo(activity, pollsbean.getCreatedDate());

                    pollsbean.setCreatedDate(date);
                }
            }


            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.ActivityId)) {
                pollsbean.setActivityID(jsonObject_single_poll.getString(JSONCommonKeywords.ActivityId));
            }


            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.TotalViews)) {
                pollsbean.setTotalViews(jsonObject_single_poll.getString(JSONCommonKeywords.TotalViews));
            }


            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.PollOptions)) {

                if (CommonMethods.checkJSONArrayHasData(jsonObject_single_poll, JSONCommonKeywords.PollOptions)) {
                    JSONArray jsonArray_poll_options = jsonObject_single_poll.getJSONArray(JSONCommonKeywords.PollOptions);

                    ArrayList<PollOptionbean> pollOptionbeanArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray_poll_options.length(); i++) {
                        JSONObject jsonObject_single_options = jsonArray_poll_options.getJSONObject(i);
                        PollOptionbean pollOptionbean = new PollOptionbean();
                        if (CommonMethods.handleKeyInJSON(jsonObject_single_options, JSONCommonKeywords.PollOptionId)) {
                            pollOptionbean.setOptionID(Integer.parseInt(jsonObject_single_options.getString(JSONCommonKeywords.PollOptionId)));
                        }

                        if (CommonMethods.handleKeyInJSON(jsonObject_single_options, JSONCommonKeywords.PollOptionText)) {
                            pollOptionbean.setOption(jsonObject_single_options.getString(JSONCommonKeywords.PollOptionText));
                        }

                        if (CommonMethods.handleKeyInJSON(jsonObject_single_options, JSONCommonKeywords.PollOptionVote)) {
                            pollOptionbean.setEachOptionVotes(jsonObject_single_options.getString(JSONCommonKeywords.PollOptionVote));
                        }
                        if (CommonMethods.handleKeyInJSON(jsonObject_single_options, JSONCommonKeywords.PollOptionPercantage)) {
                            pollOptionbean.setEachOptionPerecentage(jsonObject_single_options.getString(JSONCommonKeywords.PollOptionPercantage) + " " + "%");
                        }


                        pollOptionbeanArrayList.add(pollOptionbean);
                    }
                    pollsbean.setPollOptionbeanArrayList(pollOptionbeanArrayList);
                }
            }


            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.PollVotes)) {
                pollsbean.setPollVotes(jsonObject_single_poll.getString(JSONCommonKeywords.PollVotes));
            }
            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.isVoted)) {

                String isVotedString = jsonObject_single_poll.getString(JSONCommonKeywords.isVoted);
                if (isVotedString == null || isVotedString.equals("") || isVotedString.equals("0")) {
                    pollsbean.setVotedByLoggedUser(false);
                } else if (isVotedString.equals("1")) {
                    pollsbean.setVotedByLoggedUser(true);
                } else {
                    pollsbean.setVotedByLoggedUser(false);
                }


            }

            if (CommonMethods.handleKeyInJSON(jsonObject_single_poll, JSONCommonKeywords.isAdminForThisPoll)) {

                String isadmin = jsonObject_single_poll.getString(JSONCommonKeywords.isAdminForThisPoll);
                if (isadmin == null || isadmin.equals("") || isadmin.equals("0")) {
                    pollsbean.setAdminForThisPoll(false);
                } else if (isadmin.equals("1")) {
                    pollsbean.setAdminForThisPoll(true);

                } else {
                    pollsbean.setAdminForThisPoll(false);

                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return pollsbean;
    }
}
