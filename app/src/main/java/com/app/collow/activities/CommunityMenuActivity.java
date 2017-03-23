package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;

/**
 * Created by Harmis on 31/01/17.
 */

public class CommunityMenuActivity extends BaseActivity implements SetupViewInterface {

    public static CommunityMenuActivity communitySearchByNameActivity = null;

    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    int current_start = 0;

    private float mSpeed = 1f;

    LinearLayout linearlayout_community_feed=null;
    LinearLayout linearlayout_community_classified=null;
    LinearLayout linearlayout_community_news=null;
    LinearLayout linearlayout_community_chat=null;
    LinearLayout linearlayout_community_events=null;
    LinearLayout linearlayout_community_gallery=null;
    LinearLayout linearlayout_community_polls=null;
    LinearLayout linearlayout_community_forms=null;
    LinearLayout linearlayout_community_info=null;
    LinearLayout linearlayout_community_feedcount=null;
    LinearLayout linearlayout_community_classifiedcount=null;
    LinearLayout linearlayout_community_newscount=null;
    LinearLayout linearlayout_community_chatcount=null;
    LinearLayout linearlayout_community_eventscount=null;
    LinearLayout linearlayout_community_gallerycount=null;
    LinearLayout linearlayout_community_pollscount=null;
    LinearLayout linearlayout_community_formscount=null;
    LinearLayout linearlayout_community_infocount=null;
    ImageView imageview_community_favouriteunfavourite=null;
    Button button_community_claim=null;
    public static CommunityMenuActivity communitiyMenuActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            communitiyMenuActivity=this;
            retryParameterbean = new RetryParameterbean(CommunityMenuActivity.this, getApplicationContext(), getIntent().getExtras(), CommunityMenuActivity.class.getClass());


            communitySearchByNameActivity = this;
            searchbeanArrayList_new.clear();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    public void setupHeaderView() {
        try {
            View headerview = getLayoutInflater().inflate(R.layout.header, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerview.setLayoutParams(layoutParams);
            baseTextview_header_title = (BaseTextview) headerview.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.results));

            imageView_left_menu = (ImageView) headerview.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) headerview.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);


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
            setSupportActionBar(toolbar_header);
            toolbar_header.addView(headerview);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.community_menu, null);



            linearlayout_community_feed=(LinearLayout)view_home.findViewById(R.id.linearlayout_community_feed);
            linearlayout_community_feedcount=(LinearLayout)findViewById(R.id.linearlayout_community_feedcount);
            linearlayout_community_classified=(LinearLayout)findViewById(R.id.linearlayout_community_classified);
            linearlayout_community_classifiedcount=(LinearLayout)findViewById(R.id.linearlayout_community_classifiedcount);
            linearlayout_community_news=(LinearLayout)findViewById(R.id.linearlayout_community_news);
            linearlayout_community_newscount=(LinearLayout)findViewById(R.id.linearlayout_community_newscount);
            linearlayout_community_chat=(LinearLayout)findViewById(R.id.linearlayout_community_chat);
            linearlayout_community_chatcount=(LinearLayout)findViewById(R.id.linearlayout_community_chatcount);
            linearlayout_community_events=(LinearLayout)findViewById(R.id.linearlayout_community_events);
            linearlayout_community_eventscount=(LinearLayout)findViewById(R.id.linearlayout_community_eventcount);
            linearlayout_community_gallery=(LinearLayout)findViewById(R.id.linearlayout_community_gallery);
            linearlayout_community_gallerycount=(LinearLayout)findViewById(R.id.linearlayout_community_gallerycount);
            linearlayout_community_polls=(LinearLayout)findViewById(R.id.linearlayout_community_polls);
            linearlayout_community_pollscount=(LinearLayout)findViewById(R.id.linearlayout_community_pollscount);
            linearlayout_community_forms=(LinearLayout)findViewById(R.id.linearlayout_community_forms);
            linearlayout_community_formscount=(LinearLayout)findViewById(R.id.linearlayout_community_formscount);
            linearlayout_community_info=(LinearLayout)findViewById(R.id.linearlayout_community_info );
            linearlayout_community_infocount=(LinearLayout)findViewById(R.id.linearlayout_community_infocount);
            imageview_community_favouriteunfavourite=(ImageView)findViewById(R.id.imageview_community_favouriteunfavourite);
            button_community_claim=(Button)findViewById(R.id.button_community_claimcommunity);

            frameLayout_container.addView(view_home);


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    private void setupEvents() {

    }

    @Override
    public void setupUI(Responcebean responcebean) {
        linearlayout_community_feed.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_feedcount.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });
        linearlayout_community_classified.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });
        linearlayout_community_classifiedcount.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_news.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });
        linearlayout_community_newscount.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_chat.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_chatcount.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_events.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_eventscount.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_gallery.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_gallerycount.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_polls.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_pollscount.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_forms.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_formscount.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });

        linearlayout_community_info.setOnClickListener(new MyOnClickListener(CommunityMenuActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {

                }
                else
                {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow),CommunityMenuActivity.this);
                }
            }
        });




    }
}