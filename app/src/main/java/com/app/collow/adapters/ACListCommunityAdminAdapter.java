package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.activities.ACAdminListingActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACListCommunityAdminbean;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harmis on 4/2/17.
 */

public class ACListCommunityAdminAdapter extends RecyclerView.Adapter {



    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Activity activity=null;


    public ACListCommunityAdminAdapter(Activity activity,RecyclerView recyclerView) {
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
                    R.layout.aclist_community_admin_single_item, parent, false);

            vh = new ACAdminViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            ACListCommunityAdminbean acListCommunityAdminbean = ACAdminListingActivity.acListCommunityAdminbeanArrayList.get(position);
        
           if(CommonMethods.isTextAvailable(acListCommunityAdminbean.getAcadmin_name()))
           {
               ((ACAdminViewHolder) holder).baseTextview_aclistadmin_name.setText(acListCommunityAdminbean.getAcadmin_name());
           }

        if(CommonMethods.isTextAvailable(acListCommunityAdminbean.getAcadmin_eamil()))
        {
            ((ACAdminViewHolder) holder).baseTextview_aclistadmin_email.setText(acListCommunityAdminbean.getAcadmin_eamil());
        }



        if (CommonMethods.isImageUrlValid(acListCommunityAdminbean.getAcadmin_userprofilepic())) {

            Picasso.with(activity)
                    .load(acListCommunityAdminbean.getAcadmin_userprofilepic())
                    .into(((ACAdminViewHolder) holder).imageview_aclistadmin_profilepic, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            ((ACAdminViewHolder) holder).imageview_aclistadmin_profilepic.setImageResource(R.drawable.defualt_square);

                        }
                    });

        } else {
            ((ACAdminViewHolder) holder).imageview_aclistadmin_profilepic.setImageResource(R.drawable.defualt_square);
        }




    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return ACAdminListingActivity.acListCommunityAdminbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ACAdminViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_aclistadmin_name = null;
        BaseTextview baseTextview_aclistadmin_email = null;

        ImageView imageview_aclistadmin_profilepic = null;


        public ACAdminViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_aclistadmin_name = (BaseTextview) v.findViewById(R.id.textview_aclist_adminname);
            baseTextview_aclistadmin_email = (BaseTextview) v.findViewById(R.id.textview_aclist_adminemail);

            imageview_aclistadmin_profilepic = (ImageView) v.findViewById(R.id.imageview_aclist_adminprofilepic);



        }
    }



}
