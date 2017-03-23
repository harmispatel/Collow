package com.app.collow.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.CreatePollsActivity;
import com.app.collow.activities.SplashActvitiy;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.PollOptionbean;
import com.app.collow.utils.CommonMethods;

/**
 * Created by Harmis on 09/02/17.
 */

public class CreatePollsOptionsAdapter extends RecyclerView.Adapter {


    public CreatePollsOptionsAdapter() {


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        View vh = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.polls_create_new_single_raw, parent, false);


        return new PollsOptionsViewHolder(vh);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        PollOptionbean pollOptionbean = CreatePollsActivity.pollOptionbeanArrayList.get(position);

        if(pollOptionbean.isNeedToCreateOptions())
        {
            ((PollsOptionsViewHolder) holder).baseEdittext_options.setHint(pollOptionbean.getHint_title());
        }

        int colorRandom= SplashActvitiy.integerArrayList_colors.get(CommonMethods.randInt(0,SplashActvitiy.integerArrayList_colors.size()-1));
        ((PollsOptionsViewHolder) holder).view_line.setBackgroundColor(colorRandom);

        if(pollOptionbean.isEmptyOption())

        {
            ((PollsOptionsViewHolder) holder).baseTextview_empty_marker.setVisibility(View.VISIBLE);
        }
        else
        {
            ((PollsOptionsViewHolder) holder).baseTextview_empty_marker.setVisibility(View.GONE);

        }



    }


    @Override
    public int getItemCount() {
        return CreatePollsActivity.pollOptionbeanArrayList.size();
    }


    //
    public static class PollsOptionsViewHolder extends RecyclerView.ViewHolder {
        BaseEdittext baseEdittext_options = null;
        ImageView imageView_circle = null;
        View view_line = null;
        BaseTextview baseTextview_empty_marker=null;


        public PollsOptionsViewHolder(View v) {
            super(v);

            baseEdittext_options = (BaseEdittext) v.findViewById(R.id.edittext_create_polls_question);
            imageView_circle = (ImageView) v.findViewById(R.id.imageview_create_polls_circle_selected);
            view_line = v.findViewById(R.id.view_verticle_line_create_polls);
            baseTextview_empty_marker= (BaseTextview) v.findViewById(R.id.basetextview_need_to_diplay_empty_marker);

        }
    }

}