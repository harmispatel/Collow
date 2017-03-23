package com.app.collow.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.collow.R;
import com.app.collow.adapters.ImageSlideAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Chatbean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by harmis on 27/2/17.
 */

public class ChatDetailActivity extends BaseActivity {
    public static BaseTextview baseTextview_chat_details_comments = null;
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    BaseTextview baseTextview_chat_title, baseTextview_chat_time, baseTextview_chat_description;
    Chatbean chatbean = null;
    LinearLayout linearLayout_likes = null, linearLayout_comments = null, linearLayout_views = null;
    BaseTextview baseTextview_chat_likes = null;
    BaseTextview baseTextview_chat_views = null;

    ViewPager viewPager_images = null;
    ImageView imageView_like = null;

    ImageView left_nav, right_nav;
    ArrayList<String> stringArrayList_url = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            retryParameterbean = new RetryParameterbean(ChatDetailActivity.this, getApplicationContext(), getIntent().getExtras(), ChatDetailActivity.class.getClass());
            commonSession = new CommonSession(ChatDetailActivity.this);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                chatbean = (Chatbean) bundle.getSerializable(BundleCommonKeywords.KEY_NEWS_BEAN);
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
            baseTextview_chat_title.setText(getResources().getString(R.string.chat_detail));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageView_right_menu.setImageResource(R.drawable.shareicon);

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
                    if (chatbean != null) {
                        if (chatbean.getChat_title() != null) {
                            MyUtils.shareIntent(ChatDetailActivity.this, chatbean.getChat_title());

                        }
                    }


                }
            });

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.chatdetail, null);
            baseTextview_chat_title = (BaseTextview) view_home.findViewById(R.id.textview_chatdetail_title);
            baseTextview_chat_time = (BaseTextview) view_home.findViewById(R.id.textview_chatdetail_date_time);
            baseTextview_chat_description = (BaseTextview) view_home.findViewById(R.id.textview_chatdetail_description);
            baseTextview_chat_likes = (BaseTextview) view_home.findViewById(R.id.textview_likes_counts);
            baseTextview_chat_details_comments = (BaseTextview) view_home.findViewById(R.id.textview_comments_counts);
            baseTextview_chat_views = (BaseTextview) view_home.findViewById(R.id.textview_view_counts);

            linearLayout_likes = (LinearLayout) view_home.findViewById(R.id.layout_likes);
            linearLayout_comments = (LinearLayout) view_home.findViewById(R.id.layout_comments);
            linearLayout_views = (LinearLayout) view_home.findViewById(R.id.layout_views);

            left_nav = (ImageView) view_home.findViewById(R.id.left_nav);
            right_nav = (ImageView) view_home.findViewById(R.id.right_nav);
            viewPager_images = (ViewPager) view_home.findViewById(R.id.viewpager);


            imageView_like = (ImageView) view_home.findViewById(R.id.imageview_likes);


            frameLayout_container.addView(view_home);

            //  getNewsListing(0);
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    private void setupEvents() {
        try {
            if (chatbean != null) {

                linearLayout_likes.setTag(String.valueOf(chatbean.getPosition()));
                linearLayout_likes.setOnClickListener(new MyOnClickListener(ChatDetailActivity.this) {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (isAvailableInternet()) {

                                likeItemhandler();


                            } else {
                                showInternetNotfoundMessage();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                linearLayout_comments.setTag(String.valueOf(chatbean.getPosition()));
                linearLayout_comments.setOnClickListener(new MyOnClickListener(ChatDetailActivity.this) {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (isAvailableInternet()) {
                                int position = Integer.parseInt(v.getTag().toString());
                                Chatbean chatbean1 = ChatMainActivity.chatbeanArrayList.get(position);
                                MyUtils.openCommentswActivity(ChatDetailActivity.this, ScreensEnums.CHAT.getScrenIndex(), chatbean1.getActivityID(), chatbean1.getPosition(), "chat", chatbean1.getChat_title());

                            } else {
                                showInternetNotfoundMessage();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                if (chatbean.isLiked()) {
                    imageView_like.setImageResource(R.drawable.like_blue);
                } else {
                    imageView_like.setImageResource(R.drawable.unlike_blue);


                }


                SocialOptionbean socialOptionbean = chatbean.getSocialOptionbean();
                MyUtils.handleSocialOption(ChatDetailActivity.this, socialOptionbean, baseTextview_chat_likes, baseTextview_chat_details_comments, baseTextview_chat_views);


                ///likes count

                SpannableStringBuilder builder = new SpannableStringBuilder();

                if (CommonMethods.isTextAvailable(chatbean.getChat_title())) {
                    baseTextview_chat_title.setText(chatbean.getChat_title());
                    baseTextview_header_title.setText(chatbean.getChat_title());
                    SpannableString title = new SpannableString(chatbean.getChat_title());
                    title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, title.length(), 0);
                    builder.append(title);

                }

                if (CommonMethods.isTextAvailable(chatbean.getChat_time())) {
                    baseTextview_chat_time.setText(chatbean.getChat_time());

                    SpannableString str2 = new SpannableString(chatbean.getChat_time());
                    str2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.left_Menu_text_color_gray)), 0, str2.length(), 0);
                    str2.setSpan(new RelativeSizeSpan(0.7f), 0, str2.length(), 0); // set size

                    builder.append(" ");

                    builder.append(str2);
                }


                baseTextview_chat_title.setText(builder, BaseTextview.BufferType.SPANNABLE);


                if (CommonMethods.isTextAvailable(chatbean.getChat_description())) {
                    MyUtils.handleAndRedirectToReadMore(ChatDetailActivity.this, baseTextview_chat_description, 7, getResources().getString(R.string.more_text), chatbean.getChat_description());

                }


            }

            left_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tab = viewPager_images.getCurrentItem();
                    if (tab == 0) {
                        left_nav.setVisibility(View.GONE);
                        viewPager_images.setCurrentItem(tab);
                    } else if (tab > 0) {
                        tab--;
                        viewPager_images.setCurrentItem(tab);
                        left_nav.setVisibility(View.VISIBLE);
                        if (tab == 0) {
                            left_nav.setVisibility(View.GONE);
                            viewPager_images.setCurrentItem(tab);
                        }
                    }

                    if (tab == stringArrayList_url.size() - 1) {
                        right_nav.setVisibility(View.GONE);
                    } else {
                        right_nav.setVisibility(View.VISIBLE);
                    }

                }
            });

            // Images right navigatin
            right_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int tab = viewPager_images.getCurrentItem();
                    tab++;
                    viewPager_images.setCurrentItem(tab);

                    if (tab == stringArrayList_url.size() - 1) {
                        right_nav.setVisibility(View.GONE);
                    } else {
                        right_nav.setVisibility(View.VISIBLE);
                        if (tab == stringArrayList_url.size() - 1) {
                            right_nav.setVisibility(View.GONE);
                        }
                    }


                    if (tab > 0) {

                        left_nav.setVisibility(View.VISIBLE);

                    } else if (tab == 0) {
                        left_nav.setVisibility(View.GONE);
                    }
                }
            });

            viewPager_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    if (stringArrayList_url.size() == 1)

                    {
                        left_nav.setVisibility(View.GONE);
                        right_nav.setVisibility(View.GONE);
                    } else if (position == 0) {
                        left_nav.setVisibility(View.GONE);
                        right_nav.setVisibility(View.VISIBLE);
                    } else if (position == stringArrayList_url.size() - 1) {
                        left_nav.setVisibility(View.VISIBLE);
                        right_nav.setVisibility(View.GONE);
                    } else {
                        left_nav.setVisibility(View.VISIBLE);
                        right_nav.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


            stringArrayList_url = chatbean.getStringArrayList_fileURLs();
            if (stringArrayList_url.size() != 0) {
                ImageSlideAdapter imageSlideAdapter = new ImageSlideAdapter(ChatDetailActivity.this, stringArrayList_url, ScreensEnums.CHAT.getScrenIndex());
                viewPager_images.setAdapter(imageSlideAdapter);
                viewPager_images.setCurrentItem(0);
                viewPager_images.setOffscreenPageLimit(stringArrayList_url.size());

            }
            leftRigtButtonHandler();


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }


    public void leftRigtButtonHandler() {
        if (stringArrayList_url.size() == 1)

        {
            left_nav.setVisibility(View.GONE);
            right_nav.setVisibility(View.GONE);
        } else if (stringArrayList_url.size() == 0)

        {
            left_nav.setVisibility(View.GONE);
            right_nav.setVisibility(View.GONE);
        } else if (stringArrayList_url.size() == 2)

        {
            left_nav.setVisibility(View.GONE);
            right_nav.setVisibility(View.VISIBLE);
        } else {
            left_nav.setVisibility(View.VISIBLE);
            right_nav.setVisibility(View.VISIBLE);
        }
    }

    private void likeItemhandler() {


        try {
            RequestParametersbean requestParametersbean = new RequestParametersbean();
            requestParametersbean.setActivityId(chatbean.getActivityID());
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setLiketype("news");
            if (chatbean.getLikeOrDislikeFlag().equals("0")) {
                chatbean.setLikeOrDislikeFlag("1");
            } else if (chatbean.getLikeOrDislikeFlag().equals("1")) {
                chatbean.setLikeOrDislikeFlag("0");
            } else {
                chatbean.setLikeOrDislikeFlag("0");

            }
            requestParametersbean.setLike(chatbean.getLikeOrDislikeFlag());


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


                                    if (chatbean.isLiked()) {
                                        chatbean.setLiked(false);
                                        imageView_like.setImageResource(R.drawable.unlike_blue);
                                    } else {
                                        imageView_like.setImageResource(R.drawable.like_blue);
                                        chatbean.setLiked(true);


                                    }

                                    SocialOptionbean socialOptionbean = chatbean.getSocialOptionbean();
                                    socialOptionbean.setLike(Integer.parseInt(totallikes));
                                    MyUtils.handleSocialOption(ChatDetailActivity.this, socialOptionbean, baseTextview_chat_likes, baseTextview_chat_details_comments, baseTextview_chat_views);

                                    ChatMainActivity.chatbeanArrayList.set(chatbean.getPosition(), chatbean);
                                    if (ChatMainActivity.chatAdapter != null) {
                                        ChatMainActivity.chatAdapter.notifyItemChanged(chatbean.getPosition());
                                    }
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, ChatDetailActivity.this, ChatDetailActivity.this, URLs.LIKEACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.LIKES.getScrenIndex(), ChatDetailActivity.class.getClass());
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}