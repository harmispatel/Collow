package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.MgmtCommuityAndAdminbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;

import java.util.List;

/**
 * Created by harmis on 11/2/17.
 */

public class MgmtSelectTeamAdapter extends RecyclerView.Adapter {


    private List<MgmtCommuityAndAdminbean> mgmtteamList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    Activity activity=null;
    public MgmtSelectTeamAdapter(Activity activity, RecyclerView recyclerView) {
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

            vh = new mgmtManageTeamViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MgmtCommuityAndAdminbean singleStudent = (MgmtCommuityAndAdminbean) mgmtteamList.get(position);
            ((mgmtManageTeamViewHolder) holder).baseTextview_mgmtmanageteam_name.setText(String.valueOf("Position" + position
            ));

            if (CommonMethods.isTextAvailable(singleStudent.getMessage())) {
                ((mgmtManageTeamViewHolder) holder).baseTextview_mgmtmanageteam_name.setText(singleStudent.getMessage());
            }



    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return mgmtteamList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class mgmtManageTeamViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_mgmtmanageteam_name = null;
        BaseTextview baseTextview_mgmtmanageteam_address= null;


        ImageView imageview_mgmtmanageteam_profilepic=null;


        public mgmtManageTeamViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

           // baseTextview_mgmtmanageteam_name = (BaseTextview) v.findViewById(R.id.textview_mgmtmanageteam_teamname);
          //  baseTextview_mgmtmanageteam_address = (BaseTextview) v.findViewById(R.id.textview_mgmtmanageteam_teamaddress);

           // imageview_mgmtmanageteam_profilepic=(ImageView)v.findViewById(R.id.imageview_mgmtmanageteam_teamprofilepic);
         /*   v.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "OnClick :" + student.getName() + " \n " + student.getEmailId(),
                            Toast.LENGTH_SHORT).show();

                }
            });*/

        }
    }



}
