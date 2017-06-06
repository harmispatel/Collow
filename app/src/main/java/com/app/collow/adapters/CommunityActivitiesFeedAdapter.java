package com.app.collow.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.activities.CommunityActivitiesFeedActivitiy;
import com.app.collow.activities.CommunityInformationActivity;
import com.app.collow.activities.FeedsUserProfileActivity;
import com.app.collow.activities.GalleryMainActivity;
import com.app.collow.allenums.CommunityInformationScreenEnum;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityActivitiesFeedbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by harmis on 12/1/17.
 */
public class CommunityActivitiesFeedAdapter extends RecyclerView.Adapter {


    CommonSession commonSession = null;
    Activity activity = null;
    String fromWhichActivity = "";

    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public CommunityActivitiesFeedAdapter(RecyclerView recyclerView, Activity activity, String fromWhichActivity) {
        this.activity = activity;
        this.fromWhichActivity = fromWhichActivity;
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
                R.layout.communtiy_activities_feed_dynamical, parent, false);

        vh = new CommunityActivitiesFeedViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final CommunityActivitiesFeedbean communityActivitiesFeedbean = CommunityActivitiesFeedActivitiy.communityActivitiesFeedbeanArrayList.get(position);
        final ArrayList<String> stringArrayList = communityActivitiesFeedbean.getStringArrayList_images_url();

        if (communityActivitiesFeedbean != null) {
            ((CommunityActivitiesFeedViewHolder) holder).layout_right_activity.setTag(communityActivitiesFeedbean);
            ((CommunityActivitiesFeedViewHolder) holder).image_userprofilepic.setTag(communityActivitiesFeedbean);
            ((CommunityActivitiesFeedViewHolder) holder).textview_username.setTag(communityActivitiesFeedbean);

//            ((CommunityActivitiesFeedViewHolder) holder).image_userprofilepic.setTag(communityActivitiesFeedbean.getUserId());
//            ((CommunityActivitiesFeedViewHolder) holder).textview_username.setTag(communityActivitiesFeedbean.getUserId());

            if (CommonMethods.isTextAvailable(communityActivitiesFeedbean.getFeedcategory())) {

                ((CommunityActivitiesFeedViewHolder) holder).textview_commnitycategory.setText(communityActivitiesFeedbean.getFeedcategory());

                MyUtils.setBackgroundColorBasedOnCategoryName(activity, ((CommunityActivitiesFeedViewHolder) holder).imageView_dot, communityActivitiesFeedbean.getFeedcategory());


            }

            if (communityActivitiesFeedbean.isLikedFeed()) {
                ((CommunityActivitiesFeedViewHolder) holder).imageView_liked_disliked_icon.setImageResource(R.drawable.like_blue);
            } else {
                ((CommunityActivitiesFeedViewHolder) holder).imageView_liked_disliked_icon.setImageResource(R.drawable.unlike_blue);


            }

            if (CommonMethods.isImageUrlValid(communityActivitiesFeedbean.getUserprofilepic())) {
                Picasso.with(activity)
                        .load(communityActivitiesFeedbean.getUserprofilepic())
                        .into((((CommunityActivitiesFeedViewHolder) holder).image_userprofilepic), new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                ((CommunityActivitiesFeedViewHolder) holder).image_userprofilepic.setImageResource(R.drawable.user_defualt_icon);
                            }
                        });
            } else {
                ((CommunityActivitiesFeedViewHolder) holder).image_userprofilepic.setImageResource(R.drawable.user_defualt_icon);

            }

            if (CommonMethods.isTextAvailable(communityActivitiesFeedbean.getTitle())) {
                ((CommunityActivitiesFeedViewHolder) holder).textview_posttitle.setVisibility(View.VISIBLE);
                ((CommunityActivitiesFeedViewHolder) holder).textview_posttitle.setText(communityActivitiesFeedbean.getTitle());

            } else {
                ((CommunityActivitiesFeedViewHolder) holder).textview_posttitle.setVisibility(View.GONE);
            }


            ///likes count

            SpannableStringBuilder builder = new SpannableStringBuilder();

            if (CommonMethods.isTextAvailable(communityActivitiesFeedbean.getUsername())) {
                ((CommunityActivitiesFeedViewHolder) holder).textview_username.setText(communityActivitiesFeedbean.getUsername());
            }

            if (CommonMethods.isTextAvailable(communityActivitiesFeedbean.getPosttime())) {
                ((CommunityActivitiesFeedViewHolder) holder).textview_communityactivity_date.setText(communityActivitiesFeedbean.getPosttime());
            }


            if (CommonMethods.isTextAvailable(communityActivitiesFeedbean.getPostcontent())) {
                ((CommunityActivitiesFeedViewHolder) holder).textview_postcontent.setVisibility(View.VISIBLE);
                MyUtils.handleAndRedirectToReadMore(activity, ((CommunityActivitiesFeedViewHolder) holder).textview_postcontent, 5, activity.getResources().getString(R.string.more_text), communityActivitiesFeedbean.getPostcontent());
            } else {
                ((CommunityActivitiesFeedViewHolder) holder).textview_postcontent.setVisibility(View.GONE);
            }
            SocialOptionbean socialOptionbean = communityActivitiesFeedbean.getSocialOptionbean();
            MyUtils.handleSocialOption(activity, socialOptionbean, ((CommunityActivitiesFeedViewHolder) holder).baseTextview_news_likes, ((CommunityActivitiesFeedViewHolder) holder).baseTextview_news_comments, ((CommunityActivitiesFeedViewHolder) holder).baseTextview_news_views);


        }


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((CommunityActivitiesFeedViewHolder) holder).view.setLayoutParams(layoutParams);


        ImageSlideAdapter imageSlideAdapter = communityActivitiesFeedbean.getImageSlideAdapter();
        if (imageSlideAdapter == null) {
            ((CommunityActivitiesFeedViewHolder) holder).linearLayout_images_feed_main.setVisibility(View.GONE);

        } else {
            ((CommunityActivitiesFeedViewHolder) holder).linearLayout_images_feed_main.setVisibility(View.VISIBLE);
            ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.setAdapter(imageSlideAdapter);
        }


        if (stringArrayList != null) {
            if (stringArrayList.size() == 1)

            {
                ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.GONE);
                ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.GONE);
            } else if (stringArrayList.size() == 0)

            {
                ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.GONE);
                ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.GONE);
            } else {
                ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.GONE);
                ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.VISIBLE);
            }
        }

        //image
        ((CommunityActivitiesFeedViewHolder) holder).image_userprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileDialog(v);
                // userProfileDetil(v);
            }
        });
        ((CommunityActivitiesFeedViewHolder) holder).textview_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // userProfileDetil(v);
            }
        });


        ((CommunityActivitiesFeedViewHolder) holder).left_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.getCurrentItem();
                if (tab == 0) {
                    ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.GONE);
                    ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.setCurrentItem(tab);
                } else if (tab > 0) {
                    tab--;
                    ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.setCurrentItem(tab);
                    ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.VISIBLE);
                    if (tab == 0) {
                        ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.GONE);
                        ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.setCurrentItem(tab);
                    }
                }

                if (tab == stringArrayList.size() - 1) {
                    ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.GONE);
                } else {
                    ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.VISIBLE);
                }

            }
        });

        // Images right navigatin
        ((CommunityActivitiesFeedViewHolder) holder).right_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int tab = ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.getCurrentItem();
                tab++;
                ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.setCurrentItem(tab);

                if (tab == stringArrayList.size() - 1) {
                    ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.GONE);
                } else {
                    ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.VISIBLE);
                    if (tab == stringArrayList.size() - 1) {
                        ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.GONE);
                    }
                }


                if (tab > 0) {

                    ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.VISIBLE);

                } else if (tab == 0) {
                    ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.GONE);
                }
            }
        });

        ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ((CommunityActivitiesFeedViewHolder) holder).viewPager_for_images.setId(position);


                if (stringArrayList.size() == 1)

                {
                    ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.GONE);
                    ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.GONE);
                } else if (position == 0) {
                    ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.GONE);
                    ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.VISIBLE);
                } else if (position == stringArrayList.size() - 1) {
                    ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.VISIBLE);
                    ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.GONE);
                } else {
                    ((CommunityActivitiesFeedViewHolder) holder).left_nav.setVisibility(View.VISIBLE);
                    ((CommunityActivitiesFeedViewHolder) holder).right_nav.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        ((CommunityActivitiesFeedViewHolder) holder).linearLayout_likes.setTag(String.valueOf(communityActivitiesFeedbean.getPosition()));
        ((CommunityActivitiesFeedViewHolder) holder).linearLayout_likes.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                try {
                    if (isAvailableInternet()) {


                        int position = Integer.parseInt(v.getTag().toString());

                        likeItemhandler(position);


                    } else {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        ((CommunityActivitiesFeedViewHolder) holder).linearLayout_comments.setTag(communityActivitiesFeedbean);
        ((CommunityActivitiesFeedViewHolder) holder).linearLayout_comments.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                try {
                    if (isAvailableInternet()) {

                        CommunityActivitiesFeedbean communityActivitiesFeedbean1 = (CommunityActivitiesFeedbean) v.getTag();
                        MyUtils.openCommentswActivity(activity, ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex(), communityActivitiesFeedbean1.getActivityID(), communityActivitiesFeedbean1.getPosition(), communityActivitiesFeedbean1.getFeedcategory(), communityActivitiesFeedbean1.getTitle());

                    } else {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        ((CommunityActivitiesFeedViewHolder) holder).linearLayout_views.setTag(communityActivitiesFeedbean);
        ((CommunityActivitiesFeedViewHolder) holder).linearLayout_views.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                try {
                    if (isAvailableInternet()) {

                        /*CommunityActivitiesFeedbean communityActivitiesFeedbean1 = (CommunityActivitiesFeedbean) v.getTag();
                        RequestParametersbean requestParametersbean=new RequestParametersbean();

                        requestParametersbean.setCommunityID(communityActivitiesFeedbean1.getCommunityID());
                        MyUtils.markAsViewed(activity, communityActivitiesFeedbean1.getActivityID(),communityActivitiesFeedbean1.getFeedcategory(), ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex(),communityActivitiesFeedbean1.getPosition(),requestParametersbean,communityActivitiesFeedbean1.getCommunityID());
*/

                        try {
                            CommunityActivitiesFeedbean communityActivitiesFeedbean1 = (CommunityActivitiesFeedbean) v.getTag();
                            Intent intent = new Intent(activity, CommunityInformationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
                            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityActivitiesFeedbean1.getCommunityID());

                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        ((CommunityActivitiesFeedViewHolder) holder).layout_right_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommunityActivitiesFeedbean communityActivitiesFeedbeanLocal = (CommunityActivitiesFeedbean) v.getTag();


                String mCategory = communityActivitiesFeedbeanLocal.getFeedcategory();


                if (!TextUtils.isEmpty(mCategory)) {
                    if (fromWhichActivity.equalsIgnoreCase("home")) {
                        if (mCategory.equalsIgnoreCase("gallery")) {
                            if (GalleryMainActivity.galleryMainActivity != null) {
                                GalleryMainActivity.galleryMainActivity.finish();
                            }
                            Intent intent = new Intent(activity, GalleryMainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
                            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityActivitiesFeedbeanLocal.getCommunityID());
                            //bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);
                            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, communityActivitiesFeedbeanLocal.getCommunityName());

                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    }
                }
            }
        });

    }

    private void showProfileDialog(View view) {

        CommunityActivitiesFeedbean feedbean = (CommunityActivitiesFeedbean) view.getTag();
        final Dialog dialogProfile = new Dialog(activity);
        Window window = dialogProfile.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialogProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProfile.setCancelable(true);
        dialogProfile.setContentView(R.layout.feed_user_profile_dialog);

        CircularImageView circularImageView_profile_edit_profile = null;
        BaseEdittext edittext_editprofile_firstname = null;
        BaseEdittext edittext_editprofile_lastname = null;
        BaseEdittext edittext_editprofile_email = null;
        ProgressBar profile_pgb=null;

        circularImageView_profile_edit_profile = (CircularImageView) dialogProfile.findViewById(R.id.profile_image);
        circularImageView_profile_edit_profile.setVisibility(View.VISIBLE);
        edittext_editprofile_firstname = (BaseEdittext) dialogProfile.findViewById(R.id.edittext_editprofile_firstname);
        edittext_editprofile_lastname = (BaseEdittext) dialogProfile.findViewById(R.id.edittext_editprofile_lastname);
        edittext_editprofile_email = (BaseEdittext) dialogProfile.findViewById(R.id.edittext_editprofile_email);
        profile_pgb = (ProgressBar) dialogProfile.findViewById(R.id.profile_pgb);

        String fullname = feedbean.getUsername().trim();
        String[] parts = fullname.split(" ");
        String fname = parts[0]; // 004
        String lname = parts[1]; // 034556
        if (!TextUtils.isEmpty(fname)) {
            edittext_editprofile_firstname.setVisibility(View.VISIBLE);
            edittext_editprofile_firstname.setText(fname);
        } else {
            edittext_editprofile_firstname.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(lname)) {
            edittext_editprofile_lastname.setVisibility(View.VISIBLE);
            edittext_editprofile_lastname.setText(lname);
        } else {
            edittext_editprofile_lastname.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(feedbean.getUserEmail())) {
            edittext_editprofile_email.setVisibility(View.VISIBLE);
            edittext_editprofile_email.setText(feedbean.getUserEmail());
        } else {
            edittext_editprofile_email.setVisibility(View.INVISIBLE);
        }
    
        String image = feedbean.getUserprofilepic();

        if (image == null || image.equalsIgnoreCase("")) {
            circularImageView_profile_edit_profile.setImageResource(R.drawable.user_defualt_icon);
            profile_pgb.setVisibility(View.GONE);
        } else {
            final ProgressBar finalProfile_pgb = profile_pgb;
            final CircularImageView finalCircularImageView_profile_edit_profile = circularImageView_profile_edit_profile;
            Picasso.with(activity)
                    .load(image)
                    .into(circularImageView_profile_edit_profile, new Callback() {
                        @Override
                        public void onSuccess() {
                            finalProfile_pgb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            finalCircularImageView_profile_edit_profile.setImageResource(R.drawable.user_defualt_icon);
                            finalProfile_pgb.setVisibility(View.GONE);
                        }
                    });
        }
       
        dialogProfile.show();
    }

    private void userProfileDetil(View v) {

        String userId = v.getTag().toString();
        if (!TextUtils.isEmpty(userId)) {
            if (FeedsUserProfileActivity.feedsUserProfileActivity != null) {
                FeedsUserProfileActivity.feedsUserProfileActivity.finish();
            }
            Intent intent = new Intent(activity, FeedsUserProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(BundleCommonKeywords.KEY_ID, userId);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        } else {
            Log.e("my", " feed adapter - userid =" + userId);
        }
    }


    private void likeItemhandler(final int position) {


        try {
            final CommunityActivitiesFeedbean communityActivitiesFeedbean = CommunityActivitiesFeedActivitiy.communityActivitiesFeedbeanArrayList.get(position);
            RequestParametersbean requestParametersbean = new RequestParametersbean();
            requestParametersbean.setActivityId(communityActivitiesFeedbean.getActivityID());
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setLiketype(communityActivitiesFeedbean.getFeedcategory());

            if (communityActivitiesFeedbean.isLikedFeed()) {
                requestParametersbean.setLike("0");

            } else {
                requestParametersbean.setLike("1");

            }


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.LIKES.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {
                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                                String totallikes = jsonObject_main.getString(JSONCommonKeywords.TotalLike);
                                if (totallikes == null || totallikes.equals("")) {

                                } else {
                                    if (communityActivitiesFeedbean.isLikedFeed()) {
                                        communityActivitiesFeedbean.setLikedFeed(false);
                                    } else {
                                        communityActivitiesFeedbean.setLikedFeed(true);
                                    }
                                    SocialOptionbean socialOptionbean = communityActivitiesFeedbean.getSocialOptionbean();
                                    socialOptionbean.setLike(Integer.parseInt(totallikes));
                                    CommunityActivitiesFeedActivitiy.communityActivitiesFeedbeanArrayList.set(communityActivitiesFeedbean.getPosition(), communityActivitiesFeedbean);
                                    notifyItemChanged(communityActivitiesFeedbean.getPosition());
                                }

                            } else {
                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.message)) {
                                    CommonMethods.customToastMessage(jsonObject_main.getString(JSONCommonKeywords.message), activity);
                                } else {
                                    CommonMethods.customToastMessage(activity.getResources().getString(R.string.something_wrong), activity);

                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, activity, activity, URLs.LIKEACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.LIKES.getScrenIndex(), CommunityActivitiesFeedActivitiy.class.getClass());
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return CommunityActivitiesFeedActivitiy.communityActivitiesFeedbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class CommunityActivitiesFeedViewHolder extends RecyclerView.ViewHolder {

        BaseTextview baseTextview_post_text = null;
        LinearLayout linearLayout_images_feed_main = null;
        ViewPager viewPager_for_images = null;
        ImageView left_nav = null, right_nav = null;
        View view = null;
        CircularImageView image_userprofilepic;
        BaseTextview textview_username;
        BaseTextview textview_postcontent;
        BaseTextview textview_posttitle;

        BaseTextview textview_commnitycategory;

        LinearLayout linearLayout_likes = null, linearLayout_comments = null, linearLayout_views = null;
        BaseTextview baseTextview_news_likes = null;
        BaseTextview textview_communityactivity_date = null;
        BaseTextview baseTextview_news_comments = null;
        BaseTextview baseTextview_news_views = null;
        ImageView imageView_dot = null;
        ImageView imageView_liked_disliked_icon = null;
        RelativeLayout layout_right_activity = null;

        public CommunityActivitiesFeedViewHolder(View v) {
            super(v);
            image_userprofilepic = (CircularImageView) v.findViewById(R.id.imageview_communityactivity_profileimage);
            textview_username = (BaseTextview) v.findViewById(R.id.textview_communityactivity_username);
            textview_postcontent = (BaseTextview) v.findViewById(R.id.textview_communityactivity_text_post);
            textview_communityactivity_date = (BaseTextview) v.findViewById(R.id.textview_communityactivity_date);
            textview_posttitle = (BaseTextview) v.findViewById(R.id.textview_communityactivity_title);
            textview_commnitycategory = (BaseTextview) v.findViewById(R.id.textview_communityactivity_communitycategory);
            imageView_dot = (ImageView) v.findViewById(R.id.imageview_communityactivity_communitycategory);

            baseTextview_post_text = (BaseTextview) v.findViewById(R.id.textview_communityactivity_text_post);
            linearLayout_images_feed_main = (LinearLayout) v.findViewById(R.id.linerlayout_feed_images_main_layout);
            viewPager_for_images = (ViewPager) v.findViewById(R.id.viewpager);
            left_nav = (ImageView) v.findViewById(R.id.left_nav);
            view = v;
            right_nav = (ImageView) v.findViewById(R.id.right_nav);

            baseTextview_news_likes = (BaseTextview) v.findViewById(R.id.textview_likes_counts);
            baseTextview_news_comments = (BaseTextview) v.findViewById(R.id.textview_comments_counts);
            baseTextview_news_views = (BaseTextview) v.findViewById(R.id.textview_view_counts);

            linearLayout_likes = (LinearLayout) v.findViewById(R.id.layout_likes);
            linearLayout_comments = (LinearLayout) v.findViewById(R.id.layout_comments);
            linearLayout_views = (LinearLayout) v.findViewById(R.id.layout_views);
            imageView_liked_disliked_icon = (ImageView) v.findViewById(R.id.imageview_likes);
            layout_right_activity = (RelativeLayout) v.findViewById(R.id.layout_right_activity);
        }
    }

}