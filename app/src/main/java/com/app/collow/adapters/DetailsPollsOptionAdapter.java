package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.PollDetailsActivity;
import com.app.collow.activities.SplashActvitiy;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.PollOptionbean;
import com.app.collow.utils.CommonMethods;

import java.util.ArrayList;

/**
 * Created by harmis on 22/2/17.
 */

public class DetailsPollsOptionAdapter extends RecyclerView.Adapter {

    Activity activity = null;
    String communityID = null;
    public static ArrayList<PollOptionbean> pollOptionbeanArrayList=null;
    int indexFromWhichScreen;
    public DetailsPollsOptionAdapter(ArrayList<PollOptionbean> pollOptionbeanArrayList, int indexFromWhichScreen) {
 this.pollOptionbeanArrayList=pollOptionbeanArrayList;
this.indexFromWhichScreen=indexFromWhichScreen;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        View vh = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.polls_create_single_item, parent, false);


        return new PollsOptionsViewHolder(vh);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        PollOptionbean pollOptionbean = pollOptionbeanArrayList.get(position);

        if (pollOptionbean.isNeedToCreateOptions()) {
            ((PollsOptionsViewHolder) holder).textview_options.setHint(pollOptionbean.getHint_title());
        }

        int colorRandom= SplashActvitiy.integerArrayList_colors.get(CommonMethods.randInt(0,SplashActvitiy.integerArrayList_colors.size()-1));
        ((PollsOptionsViewHolder) holder).view_line.setBackgroundColor(colorRandom);


        if(indexFromWhichScreen== ScreensEnums.CREATE_POLLS.getScrenIndex())

        {
            if (pollOptionbean.isEmptyOption())

            {
                ((PollsOptionsViewHolder) holder).baseTextview_empty_marker.setVisibility(View.VISIBLE);
            } else {
                ((PollsOptionsViewHolder) holder).baseTextview_empty_marker.setVisibility(View.GONE);

            }

        }
        else if(indexFromWhichScreen==ScreensEnums.POLLS_DETAILS_FOR_USER.getScrenIndex())
        {
            if(pollOptionbean.isSelectedOptions())
            {
                ((PollsOptionsViewHolder) holder).imageView_circle.setBackgroundResource(R.drawable.poll_selected);

            }
            else
            {
                ((PollsOptionsViewHolder) holder).imageView_circle.setBackgroundResource(R.drawable.poll_unselected);

            }


            ((PollsOptionsViewHolder) holder).view_click.setTag(pollOptionbean);
            ((PollsOptionsViewHolder) holder).view_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PollOptionbean pollOptionbean1= (PollOptionbean) v.getTag();

                    for (int i = 0; i < pollOptionbeanArrayList.size(); i++) {

                        PollOptionbean pollOptionbean2=pollOptionbeanArrayList.get(i);
                        if(pollOptionbean2.getOptionID()==pollOptionbean1.getOptionID())
                        {
                            pollOptionbean2.setSelectedOptions(true);
                        }
                        else
                        {
                            pollOptionbean2.setSelectedOptions(false);
                        }

                        pollOptionbeanArrayList.remove(i);
                        pollOptionbeanArrayList.add(i,pollOptionbean2);

                    }
                    notifyDataSetChanged();


                }
            });


        }
        else if(indexFromWhichScreen==ScreensEnums.SUBMITTED_POLL.getScrenIndex())
        {

            if(pollOptionbean.isAdminForThisPoll())
            {
                if(CommonMethods.isTextAvailable(pollOptionbean.getEachOptionPerecentage()))
                {
                    ((PollsOptionsViewHolder) holder).textview_each_options_results_and_option_votes_if_admin.setText(pollOptionbean.getEachOptionVotes());
                }


                if(CommonMethods.isTextAvailable(pollOptionbean.getEachOptionPerecentage()))
                {
                    ((PollsOptionsViewHolder) holder).textview_each_options_results_if_admin.setText(pollOptionbean.getEachOptionPerecentage());
                }


            }
            else
            {
                ((PollsOptionsViewHolder) holder).imageView_circle.setVisibility(View.GONE);
                ((PollsOptionsViewHolder) holder).textview_each_options_results_if_admin.setVisibility(View.GONE);

                ((PollsOptionsViewHolder) holder).textview_each_options_results_and_option_votes_if_admin.setVisibility(View.VISIBLE);
                if(CommonMethods.isTextAvailable(pollOptionbean.getEachOptionPerecentage()))
                {
                    ((PollsOptionsViewHolder) holder).textview_each_options_results_and_option_votes_if_admin.setText(pollOptionbean.getEachOptionPerecentage());
                }
            }




        }
    }


    @Override
    public int getItemCount() {
        return pollOptionbeanArrayList.size();
    }


    public static class PollsOptionsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_circle = null;
        View view_line = null;
        BaseTextview baseTextview_empty_marker = null;
        BaseTextview textview_options = null;
        BaseTextview textview_each_options_results_and_option_votes_if_admin = null;
        BaseTextview textview_each_options_results_if_admin = null;

        View view_click = null;


        public PollsOptionsViewHolder(View v) {
            super(v);


            imageView_circle = (ImageView) v.findViewById(R.id.imageview_create_polls_circle_selected);
            textview_each_options_results_and_option_votes_if_admin= (BaseTextview) v.findViewById(R.id.textview_each_option_result);
            textview_each_options_results_if_admin= (BaseTextview) v.findViewById(R.id.basetextview_need_to_diplay_for_admin_results);

            textview_options = (BaseTextview) v.findViewById(R.id.textview_create_polls_question);

            view_line = v.findViewById(R.id.view_verticle_line_create_polls);
            baseTextview_empty_marker = (BaseTextview) v.findViewById(R.id.basetextview_need_to_diplay_empty_marker);
            view_click = v;

        }

    }

}