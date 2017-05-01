package com.app.collow.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.MgmtAdminAndCommunitiesListingofTeamActivity;
import com.app.collow.activities.MgmtTeamListingActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.MgmtTeamListingbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;

/**
 * Created by Harmis on 17/02/17.
 */

public class MgmtTeamListingAdapter extends RecyclerView.Adapter {


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Activity activity=null;


    public MgmtTeamListingAdapter(Activity activity, RecyclerView recyclerView) {
         this.activity=activity;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + CommonKeywords.VISIBLE_THRESHOLD)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.mgmt_team_single_item, parent, false);

        vh = new MgmtManageCommunityViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MgmtTeamListingbean mgmtTeamListingbean = MgmtTeamListingActivity.mgmtcommunitybeanArrayList.get(position);




        ((MgmtManageCommunityViewHolder) holder).baseTextview_mgmtmanagecommunity_name.setText(mgmtTeamListingbean.getMgmtmanagecommunity_name());
        ((MgmtManageCommunityViewHolder) holder).view.setTag(mgmtTeamListingbean);
        ((MgmtManageCommunityViewHolder) holder).view.setOnClickListener(new MyOnClickListener(activity) {

            @Override
            public void onClick(View v) {

                try {
                    if(isAvailableInternet())
                    {
                        MgmtTeamListingbean mgmtTeamListingbean1= (MgmtTeamListingbean) v.getTag();
                        Intent intent=new Intent(activity, MgmtAdminAndCommunitiesListingofTeamActivity.class);
                        mgmtTeamListingbean1.setMgmtmanagecommunity_name("Test");
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,mgmtTeamListingbean1);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                    else
                    {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return MgmtTeamListingActivity.mgmtcommunitybeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class MgmtManageCommunityViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_mgmtmanagecommunity_name = null;
        BaseTextview baseTextview_mgmtmanagecommunity_address= null;


        ImageView imageview_mgmtmanagecommunity_profilepic=null;
        View view=null;


        public MgmtManageCommunityViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);
            view=v;
            baseTextview_mgmtmanagecommunity_name = (BaseTextview) v.findViewById(R.id.textview_mgmtcommunity_communityname);
            baseTextview_mgmtmanagecommunity_address = (BaseTextview) v.findViewById(R.id.textview_mgmtcommunity_communityaddress);

            imageview_mgmtmanagecommunity_profilepic=(ImageView)v.findViewById(R.id.imageview_mgmtcommunity_communityprofilepic);


        }
    }



}