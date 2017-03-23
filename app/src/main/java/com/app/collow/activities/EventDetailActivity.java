package com.app.collow.activities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.collow.R;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Eventbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.MyUtils;


/**
 * Created by harmis on 1/2/17.
 */

public class EventDetailActivity extends BaseActivity {
    //public static BaseTextview baseTextview_events_details_comments = null;
    View view_home = null;
    //header iterms

    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    BaseTextview baseTextview_events_title, baseTextview_events_time, baseTextview_events_description;
    Eventbean eventbean = null;
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    LinearLayout linearLayout_likes = null, linearLayout_comments = null, linearLayout_views = null;
    BaseTextview baseTextview_events_likes = null;
    BaseTextview baseTextview_events_views = null;
    BaseTextview baseTextview_events_comments = null;
    Activity activity=null;
    ImageView imageView_like = null;
    BaseTextview baseTextview_left_side = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            retryParameterbean = new RetryParameterbean(EventDetailActivity.this, getApplicationContext(), getIntent().getExtras(), EventDetailActivity.class.getClass());
            commonSession = new CommonSession(EventDetailActivity.this);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                eventbean = (Eventbean) bundle.getSerializable(BundleCommonKeywords.KEY_NEWS_BEAN);
            }

            setupHeaderView();
            findViewByIDs();
            setupEvents();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }


    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.events));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageView_delete = (ImageView) toolbar_header.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.GONE);
            imageView_view = (ImageView) toolbar_header.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.GONE);
            imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.GONE);
            imageView_search = (ImageView) toolbar_header.findViewById(R.id.imageview_search_items);
            imageView_search.setVisibility(View.GONE);
            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);
            //baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);

            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }
            };
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.RIGHT);


                }
            });

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.event_detail, null);
            baseTextview_events_title = (BaseTextview) view_home.findViewById(R.id.textview_eventdetail_title);
            baseTextview_events_time = (BaseTextview) view_home.findViewById(R.id.textview_eventdetail_date_time);
            baseTextview_events_description = (BaseTextview) view_home.findViewById(R.id.textview_eventdetail_description);

            baseTextview_events_likes = (BaseTextview) view_home.findViewById(R.id.textview_likes_counts);
            baseTextview_events_comments = (BaseTextview) view_home.findViewById(R.id.textview_comments_counts);
            baseTextview_events_views= (BaseTextview) view_home.findViewById(R.id.textview_view_counts);

            linearLayout_likes = (LinearLayout) view_home.findViewById(R.id.layout_likes);
            linearLayout_comments = (LinearLayout) view_home.findViewById(R.id.layout_comments);
            linearLayout_views = (LinearLayout) view_home.findViewById(R.id.layout_views);




            imageView_like = (ImageView) view_home.findViewById(R.id.imageview_likes);


            frameLayout_container.addView(view_home);

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    private void setupEvents() {
        try {
            if (eventbean != null) {

                linearLayout_likes.setTag(String.valueOf(eventbean.getPosition()));
                linearLayout_likes.setOnClickListener(new MyOnClickListener(EventDetailActivity.this) {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (isAvailableInternet()) {

                               // likeItemhandler();


                            } else {
                                showInternetNotfoundMessage();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                linearLayout_comments.setOnClickListener(new MyOnClickListener(EventDetailActivity.this) {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (isAvailableInternet()) {
                                MyUtils.openCommentswActivity(EventDetailActivity.this, ScreensEnums.EVENTDETAIl.getScrenIndex(), eventbean.getActivityID(), eventbean.getPosition(),CommonKeywords.TYPE_FEED_EVENT, eventbean.getEvent_title());
                            } else {
                                showInternetNotfoundMessage();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                if (eventbean.isLiked()) {
                    imageView_like.setImageResource(R.drawable.like_blue);
                } else {
                    imageView_like.setImageResource(R.drawable.unlike_blue);


                }


                SocialOptionbean socialOptionbean = eventbean.getSocialOptionbean();
                MyUtils.handleSocialOption(EventDetailActivity.this, socialOptionbean, baseTextview_events_likes, baseTextview_events_comments, baseTextview_events_views);


                ///likes count

                SpannableStringBuilder builder = new SpannableStringBuilder();

                if (CommonMethods.isTextAvailable(eventbean.getEvent_title())) {
                    baseTextview_events_title.setText(eventbean.getEvent_title());
                    baseTextview_header_title.setText(eventbean.getEvent_title());
                    SpannableString title = new SpannableString(eventbean.getEvent_title());
                    title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, title.length(), 0);
                    builder.append(title);

                }




                if(eventbean.getEvent_time_mode()== CommonKeywords.EVENT_TIME_MODE_CUSTOM_TIME)
                {
                    if (CommonMethods.isTextAvailable(eventbean.getEvent_start_time())) {

                        StringBuffer stringBuffer=new StringBuffer();

                        stringBuffer.append(eventbean.getEvent_start_time());
                        if (CommonMethods.isTextAvailable(eventbean.getEvent_end_time())) {
                            stringBuffer.append("-");

                            stringBuffer.append(eventbean.getEvent_end_time());


                        }

                        baseTextview_events_time.setText(stringBuffer.toString());


                    }
                }
                else if(eventbean.getEvent_time_mode()== CommonKeywords.EVENT_TIME_MODE_ALL_DAYS)
                {
                    baseTextview_events_time.setText(activity.getResources().getString(R.string.all_days));
                }




                baseTextview_events_title.setText(builder, BaseTextview.BufferType.SPANNABLE);


                if (CommonMethods.isTextAvailable(eventbean.getEvent_description())) {
                    MyUtils.handleAndRedirectToReadMore(EventDetailActivity.this, baseTextview_events_description, 7, getResources().getString(R.string.more_text), eventbean.getEvent_description());

                }







            }
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }
    }




}
