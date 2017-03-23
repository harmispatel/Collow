package com.app.collow.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.setupUI.LikeDisLikeHandler;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.MyUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by harmis on 1/2/17.
 */

public class NewsDetailActivity extends BaseActivity {
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession=null;
    BaseTextview baseTextview_header_title=null;
    BaseTextview baseTextview_news_title,baseTextview_news_time,baseTextview_news_description;

    Newsbean newsbean=null;
    ImageView imageView_news_pic=null;
    LikeDisLikeHandler likeDisLikeHandler=null;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            retryParameterbean = new RetryParameterbean(NewsDetailActivity.this, getApplicationContext(), getIntent().getExtras(), NewsDetailActivity.class.getClass());
            commonSession = new CommonSession(NewsDetailActivity.this);

            Bundle bundle=getIntent().getExtras();
            if(bundle!=null)
            {
                newsbean = (Newsbean) bundle.getSerializable(BundleCommonKeywords.KEY_NEWS_BEAN);
            }

            likeDisLikeHandler=new LikeDisLikeHandler(NewsDetailActivity.this, ScreensEnums.NEWS_DETAILS.getScrenIndex());
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
            baseTextview_header_title= (BaseTextview) headerview.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText("News Title");

            imageView_left_menu = (ImageView) headerview.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) headerview.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageView_left_menu.setImageResource(R.drawable.left_arrow);
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();


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
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.news_detail, null);
            baseTextview_news_title= (BaseTextview) view_home.findViewById(R.id.textview_newsdetails_title);
            baseTextview_news_time= (BaseTextview) view_home.findViewById(R.id.textview_newsdetails_date_time);
            baseTextview_news_description= (BaseTextview) view_home.findViewById(R.id.textview_newsdetails_description);


          imageView_news_pic= (ImageView) view_home.findViewById(R.id.imageview_annoncementheading);





            frameLayout_container.addView(view_home);

            //  getNewsListing(0);
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    private void setupEvents() {
        try {
            if(newsbean!=null)
            {

                ///likes count
                likeDisLikeHandler.setupLikesAndDislikesUI(view_home,String.valueOf(newsbean.getLikeDisLikeCount()));




                if(CommonMethods.isTextAvailable(newsbean.getNews_title()))
                {
                    baseTextview_news_title.setText(newsbean.getNews_title());
                }

                if(CommonMethods.isTextAvailable(newsbean.getNews_time()))
                {
                    baseTextview_news_time.setText(newsbean.getNews_time());
                }


                if(CommonMethods.isTextAvailable(newsbean.getNews_description()))
                {
                    MyUtils.handleAndRedirectToReadMore(NewsDetailActivity.this,baseTextview_news_description,7,getResources().getString(R.string.more_text),newsbean.getNews_description());

                }

                if (CommonMethods.isImageUrlValid(newsbean.getNews_pic())) {

                    Picasso.with(NewsDetailActivity.this)
                            .load(newsbean.getNews_pic())
                            .into(imageView_news_pic, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {
                                    imageView_news_pic.setImageResource(R.drawable.defualt_square);

                                }
                            });

                } else {
                    imageView_news_pic.setImageResource(R.drawable.defualt_square);
                }

            }
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

}

