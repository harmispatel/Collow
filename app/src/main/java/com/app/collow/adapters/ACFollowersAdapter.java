package com.app.collow.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.ACFollowerAwaitingManageActivity;
import com.app.collow.activities.CommunityActivitiesFeedActivitiy;
import com.app.collow.activities.SignInActivity;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.ACFollowersbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by harmis on 8/2/17.
 */

public class ACFollowersAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public ACFollowersAdapter(Activity mactivity, RecyclerView recyclerView) {
        activity = mactivity;
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
                R.layout.acfollowers_single_item, parent, false);

        vh = new ACFollowersViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


        ACFollowersbean acFollowersbean = ACFollowerAwaitingManageActivity.acfollowersbeanArrayList_filtered.get(position);

        if (CommonMethods.isTextAvailable(acFollowersbean.getFollowers_name())) {
            ((ACFollowersViewHolder) holder).baseTextview_followers_name.setText(acFollowersbean.getFollowers_name());

        }


        if (CommonMethods.isTextAvailable(acFollowersbean.getFollowers_address())) {
            ((ACFollowersViewHolder) holder).baseTextview_followers_address.setText(acFollowersbean.getFollowers_address());

        }


        if (CommonMethods.isImageUrlValid(acFollowersbean.getFoloowers_profilepic())) {
            Picasso.with(activity)
                    .load(acFollowersbean.getFoloowers_profilepic())
                    .into((((ACFollowersViewHolder) holder).imageview_followers_profilepic), new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            ((ACFollowersViewHolder) holder).imageview_followers_profilepic.setImageResource(R.drawable.defualt_square);
                        }
                    });
        } else {
            ((ACFollowersViewHolder) holder).imageview_followers_profilepic.setImageResource(R.drawable.defualt_square);
        }

        ((ACFollowersViewHolder) holder).view.setTag(acFollowersbean);

    }




    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return ACFollowerAwaitingManageActivity.acfollowersbeanArrayList_filtered.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ACFollowersViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_followers_name = null;
        BaseTextview baseTextview_followers_address = null;
        BaseTextview baseTextview_followers_type = null;

        CircularImageView imageview_followers_profilepic = null;
        View view = null;


        public ACFollowersViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_followers_name = (BaseTextview) v.findViewById(R.id.textview_acfollowers_name);
            baseTextview_followers_address = (BaseTextview) v.findViewById(R.id.textview_acfollowers_address);
            baseTextview_followers_type = (BaseTextview) v.findViewById(R.id.textview_acfollowers_type);
            imageview_followers_profilepic = (CircularImageView) v.findViewById(R.id.imageview_acfollowers_profilepic);
            view = v;

        }
    }


}