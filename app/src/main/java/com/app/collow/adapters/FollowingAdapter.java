package com.app.collow.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.activities.FollowingActivity;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Followingbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.MyCommomMethod;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.SelectableRoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;



/**
 * Created by harmis on 12/1/17.
 */
public class FollowingAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    CommonSession commonSession = null;
    int selectedPos = -1;
    String isHomeCom = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public FollowingAdapter(RecyclerView recyclerView, Activity activity) {
        this.activity = activity;
        commonSession = new CommonSession(activity);
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
                R.layout.following_single_raw, parent, false);

        vh = new FollowingViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FollowingViewHolder) {

            final Followingbean followingbean = FollowingActivity.followingbeanArrayList.get(position);

            if (CommonMethods.isTextAvailable(followingbean.getCommunityName())) {
                ((FollowingViewHolder) holder).baseTextview_community_name.setText(followingbean.getCommunityName());

            }

            if (CommonMethods.isTextAvailable(followingbean.getFullAddress())) {
                ((FollowingViewHolder) holder).baseTextview_community_address.setText(followingbean.getFullAddress());
            }
            if (CommonMethods.isTextAvailable(followingbean.getIsHomeCommunity())) {
                isHomeCom = followingbean.getIsHomeCommunity();
                ((FollowingViewHolder) holder).imageview_following_heart.setId(Integer.parseInt(isHomeCom));

            }

            if (CommonMethods.isTextAvailable(followingbean.getIsFollowedCommunity())) {
                String follow = followingbean.getIsFollowedCommunity();
                if (!TextUtils.isEmpty(follow) && follow.equalsIgnoreCase("1")) {
                    // isFollowed=true;
                    ((FollowingViewHolder) holder).imageview_following_heart.setImageResource(R.drawable.select_heart);
                    ((FollowingViewHolder) holder).imageview_following_heart.setVisibility(View.VISIBLE);
                    ((FollowingViewHolder) holder).imageview_following_heart.setTag("1");

                } else if (!TextUtils.isEmpty(follow) && follow.equalsIgnoreCase("0")) {
                    //isFollowed=false;
                    ((FollowingViewHolder) holder).imageview_following_heart.setImageResource(R.drawable.unselect_heart);
                    ((FollowingViewHolder) holder).imageview_following_heart.setVisibility(View.VISIBLE);
                } else {
                    ((FollowingViewHolder) holder).imageview_following_heart.setVisibility(View.GONE);
                }
            }


            if (CommonMethods.isImageUrlValid(followingbean.getCommuntiyImageURL())) {
                Picasso.with(activity)
                        .load(followingbean.getCommuntiyImageURL())
                        .into((((FollowingViewHolder) holder).selectableRoundedImageView_following), new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                ((FollowingViewHolder) holder).selectableRoundedImageView_following.setImageResource(R.drawable.defualt_square);
                            }
                        });
            } else {
                ((FollowingViewHolder) holder).selectableRoundedImageView_following.setImageResource(R.drawable.defualt_square);
            }


            ((FollowingViewHolder) holder).imageView_following_ring.setTag(followingbean);

            ((FollowingViewHolder) holder).imageView_following_ring.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {


                        Followingbean followingbean1_local = (Followingbean) v.getTag();


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            ((FollowingViewHolder) holder).imageview_following_heart.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {

                        if (((FollowingViewHolder) holder).imageView_following_home.getDrawable().getConstantState().equals
                                (activity.getResources().getDrawable(R.drawable.homeselected).getConstantState())) {
                            CommonMethods.customToastMessage(activity.getResources().getString(R.string.warn_unfollow), activity);
                        } else if (((FollowingViewHolder) holder).imageView_following_home.getDrawable().getConstantState().equals
                                (activity.getResources().getDrawable(R.drawable.homeunselected).getConstantState())) {

                            String isHomeCommunity = String.valueOf(((FollowingViewHolder) holder).imageview_following_heart.getId());
                            Log.e("ad", ">" + isHomeCommunity);

                            // if (!TextUtils.isEmpty(isHomeCommunity) && isHomeCommunity.equalsIgnoreCase("0")) {

                            if (!TextUtils.isEmpty(v.getTag().toString()) && v.getTag().toString().equalsIgnoreCase("1")) {
                                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                                        activity);
                                myAlertDialog.setMessage(activity.getResources().getString(R.string.delete_message_a));

                                myAlertDialog.setPositiveButton(activity.getResources().getString(R.string.logout_ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {

                                                try {
                                                    selectedPos = position;
                                                    new UnFollowCommunity(followingbean).execute();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                                myAlertDialog.setNegativeButton(activity.getResources().getString(R.string.logout_cancel),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {


                                            }
                                        });
                                myAlertDialog.show();

                            }
                            Log.e("ad", ">" + "no");
                        } else {
                            Log.e("ad", ">" + "no idea");
                        }
                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            ((FollowingViewHolder) holder).imageView_following_home.setTag(followingbean);
            ((FollowingViewHolder) holder).relativeLayout_home_defualt_click.setTag(followingbean);

            ((FollowingViewHolder) holder).imageView_following_home.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        Followingbean followingbean1_local = (Followingbean) v.getTag();
                        // MyUtils.openCommunityFeedActivity(activity, ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex(),followingbean1_local.getCommunityID(),followingbean1_local.getCommunityName(),is);
                        MyUtils.makeAsHomeCommunity(activity, followingbean1_local.getCommunityID(), ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex());

                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });

            ((FollowingViewHolder) holder).relativeLayout_home_defualt_click.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        Followingbean followingbean1_local = (Followingbean) v.getTag();
                        // MyUtils.openCommunityFeedActivity(activity, ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex(),followingbean1_local.getCommunityID(),followingbean1_local.getCommunityName(),is);
                        MyUtils.makeAsHomeCommunity(activity, followingbean1_local.getCommunityID(), ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex());
                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });


            ((FollowingViewHolder) holder).follow_item_relative_layout.setTag(followingbean);

            ((FollowingViewHolder) holder).follow_item_relative_layout.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        if (isAvailableInternet()) {
                            Followingbean followingbean1_local = (Followingbean) v.getTag();
                            MyUtils.openCommunityFeedActivity(activity, ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex(), followingbean1_local.getCommunityID(), followingbean1_local.getCommunityName(), followingbean1_local.getCommunityAccessbean());

                        } else {
                            showInternetNotfoundMessage();
                        }


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });


            CommunityAccessbean communityAccessbean = followingbean.getCommunityAccessbean();
            if (communityAccessbean != null) {
                if (communityAccessbean.isHomeDefualtCommunity()) {
                    ((FollowingViewHolder) holder).imageView_following_home.setImageResource(R.drawable.homeselected);
                } else {
                    ((FollowingViewHolder) holder).imageView_following_home.setImageResource(R.drawable.homeunselected);

                }
            }


        } else {
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return FollowingActivity.followingbeanArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class FollowingViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_community_name = null, baseTextview_community_address = null;
        SelectableRoundedImageView selectableRoundedImageView_following = null;
        ImageView imageView_following_home = null, imageView_following_ring = null, imageview_following_heart = null;
        RelativeLayout relativeLayout_home_defualt_click = null;
        RelativeLayout follow_item_relative_layout = null;


        public FollowingViewHolder(View v) {
            super(v);

            baseTextview_community_name = (BaseTextview) v.findViewById(R.id.textview_following_name);
            baseTextview_community_address = (BaseTextview) v.findViewById(R.id.textview_following_full_address);
            selectableRoundedImageView_following = (SelectableRoundedImageView) v.findViewById(R.id.imageview_following_image);

            imageView_following_home = (ImageView) v.findViewById(R.id.imageview_inside_following_imageview);
            imageView_following_ring = (ImageView) v.findViewById(R.id.imageview_following_ring);
            imageview_following_heart = (ImageView) v.findViewById(R.id.imageview_following_heart);
            imageview_following_heart.setVisibility(View.GONE);
            relativeLayout_home_defualt_click = (RelativeLayout) v.findViewById(R.id.relativelayout_for_home_defualt_following);
            follow_item_relative_layout = (RelativeLayout) v.findViewById(R.id.follow_item_relative_layout);


        }


    }

    public class UnFollowCommunity extends AsyncTask<Void, Void, String> {
        Followingbean followingbean;

        public UnFollowCommunity(Followingbean followingbean) {
            this.followingbean = followingbean;
        }

        @Override
        protected String doInBackground(Void... params) {
            String responce = null;
            MyCommomMethod commomMethod = new MyCommomMethod(activity);

            responce = commomMethod.unfollowCommunity(followingbean);
            return responce;
        }

        @Override
        protected void onPostExecute(String responce) {
            super.onPostExecute(responce);

            if (responce.equalsIgnoreCase("1")) {


                FollowingActivity.followingbeanArrayList.remove(selectedPos);
                notifyItemRemoved(selectedPos);
                notifyItemRangeChanged(selectedPos, FollowingActivity.followingbeanArrayList.size());

                notifyDataSetChanged();

            } else {

            }
        }
    }


}