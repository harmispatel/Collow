package com.app.collow.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.SelectableRoundedImageView;
import com.app.collow.utils.URLs;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.collow.utils.CommonMethods.isImageUrlValid;

/**
 * Created by harmis on 1/2/17.
 */

public class ClassifiedDetailActivity extends BaseActivity {
    public static final int MAX_LINES = 3;
    BaseTextview textview_classifieddetail_status;
    BaseTextview textview_classifieddetail_name;
    BaseTextview textview_classifieddetail_price;
    BaseTextview textview_classifieddetail_description;
    Button button_classifieddetail_contact;
    Button button_classifieddetail_iaminterested;
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    View view_home = null;
    BaseTextview baseTextview_left_side = null;
    BaseTextview baseTextview_header_title = null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    Classifiedbean classifiedbean = null;
    String communityID = null;
    CommonSession commonSession = null;
    ViewPager viewPager_create_classified_slider = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(ClassifiedDetailActivity.this, getApplicationContext(), getIntent().getExtras(), ClassifiedDetailActivity.class.getClass());
            commonSession = new CommonSession(this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                classifiedbean = (Classifiedbean) bundle.getSerializable(BundleCommonKeywords.KEY_CLASSIFIED_BEAN);
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                MyUtils.markAsViewed(ClassifiedDetailActivity.this, classifiedbean.getClassifiedID(), CommonKeywords.TYPE_FEED_GALLERY, ScreensEnums.CLASSIFIED.getScrenIndex(),classifiedbean.getPosition(),requestParametersbean,null);

            }
            setupHeaderView();
            findViewbyIds();
            setupEvents();

            setupUI();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }


    public void setupHeaderView() {
        try {
              baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            baseTextview_header_title.setSelected(true);
            baseTextview_header_title.setSingleLine(true);
            baseTextview_header_title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            baseTextview_header_title.setFocusable(true);
            baseTextview_header_title.setFocusableInTouchMode(true);
            baseTextview_header_title.requestFocus();
            baseTextview_header_title.setSingleLine(true);
            baseTextview_header_title.setSelected(true);
            baseTextview_header_title.setMarqueeRepeatLimit(-1);


            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);

            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    MyUtils.leftMenuUpdateDataifOpenedDrawer(ClassifiedDetailActivity.this,drawerLayout,circularImageView_profile_pic,baseTextview_left_menu_unread_message,retryParameterbean);
                }

                @Override
                public void onDrawerClosed(View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            };
            drawerLayout.setDrawerListener(drawerListener);

            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewbyIds() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.classified_detail, null);

            textview_classifieddetail_status = (BaseTextview) view_home.findViewById(R.id.textview_classifieddetail_status);
            textview_classifieddetail_name = (BaseTextview) view_home.findViewById(R.id.textview_classifieddetail_name);
            textview_classifieddetail_price = (BaseTextview) view_home.findViewById(R.id.textview_classifieddetail_price);
            textview_classifieddetail_description = (BaseTextview) view_home.findViewById(R.id.textview_classifieddetail_description);
            button_classifieddetail_contact = (Button) view_home.findViewById(R.id.button_classifieddetail_contact);
            button_classifieddetail_iaminterested = (Button) view_home.findViewById(R.id.button_classifieddetail_iminterested);


            viewPager_create_classified_slider = (ViewPager) view_home.findViewById(R.id.pager_feature_product);


            frameLayout_container.addView(view_home);

            textview_classifieddetail_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ClassifiedDetailActivity.this,ReadMoreTextActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_READ_MORE_TEXT,classifiedbean.getClassifiedDescription());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void setupUI() {
        try {

            if (classifiedbean != null) {

                baseTextview_header_title.setText(classifiedbean.getClassified_name());

                if (CommonMethods.isTextAvailable(classifiedbean.getStatus())) {
                    textview_classifieddetail_status.setText(getResources().getString(R.string.for_text) + " " + classifiedbean.getStatus());
                }

                if (CommonMethods.isTextAvailable(classifiedbean.getClassified_name())) {
                    textview_classifieddetail_name.setText(classifiedbean.getClassified_name());
                }

                if (CommonMethods.isTextAvailable(classifiedbean.getClassifiedDescription())) {


                    MyUtils.handleAndRedirectToReadMore(ClassifiedDetailActivity.this,textview_classifieddetail_description,3,getResources().getString(R.string.more_text),classifiedbean.getClassifiedDescription());
                }

                if (CommonMethods.isTextAvailable(classifiedbean.getClssified_price())) {
                    textview_classifieddetail_price.setText(classifiedbean.getClssified_price());
                }


                ArrayList<String> stringArrayList = classifiedbean.getStringArrayList_images();

                if (stringArrayList.size() == 0) {
                    stringArrayList.add(null);
                    ClassiFiedDetaislImageAdapter classiFiedDetaislImageAdapter = new ClassiFiedDetaislImageAdapter(ClassifiedDetailActivity.this, stringArrayList);
                    viewPager_create_classified_slider.setAdapter(classiFiedDetaislImageAdapter);
                } else {
                    viewPager_create_classified_slider.setClipToPadding(false);
                    viewPager_create_classified_slider.setPageMargin(24);
                    viewPager_create_classified_slider.setPadding(48, 8, 48, 8);

                    ClassiFiedDetaislImageAdapter classiFiedDetaislImageAdapter = new ClassiFiedDetaislImageAdapter(ClassifiedDetailActivity.this, stringArrayList);
                    viewPager_create_classified_slider.setAdapter(classiFiedDetaislImageAdapter);

                }

            }
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void setupEvents() {
        try {

            button_classifieddetail_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendContactUsRequestDialog();
                }
            });

            button_classifieddetail_iaminterested.setOnClickListener(new MyOnClickListener(ClassifiedDetailActivity.this) {

                @Override
                public void onClick(View v) {
                    if(isAvailableInternet())
                    {
                        sentIMInterestRequest();

                    }
                    else
                    {
                        showInternetNotfoundMessage();
                    }
                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    public void sentContactRequest(final Dialog dialog) {
        try {
            requestParametersbean.setOwnerId(classifiedbean.getOwnerId());
            requestParametersbean.setMenuId(classifiedbean.getClassifiedID());
            requestParametersbean.setMenuType("property");

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.CONTACT_CLASSIFIED.getScrenIndex(), requestParametersbean);


            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {


                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {
                                    CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_done), ClassifiedDetailActivity.this);

                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), ClassifiedDetailActivity.this);

                                }
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }


                            } else {
                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {
                                    CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_failed), ClassifiedDetailActivity.this);

                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), ClassifiedDetailActivity.this);

                                }

                            }


                        } catch (Exception e) {
                            CommonMethods.customToastMessage(e.getMessage(), ClassifiedDetailActivity.this);

                        }

                    }


                }
            }, ClassifiedDetailActivity.this, getApplicationContext(), URLs.CONTACTOWNER, jsonObjectGetPostParameterEachScreen, ScreensEnums.CONTACT_CLASSIFIED.getScrenIndex(), SignInActivity.class.getClass());


            new RequestToServer(passParameterbean, retryParameterbean).execute();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }

    public void sentIMInterestRequest() {
        try {
            requestParametersbean.setOwnerId(classifiedbean.getOwnerId());
            requestParametersbean.setMenuId(classifiedbean.getClassifiedID());
            requestParametersbean.setMenuType("property");
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());
            requestParametersbean.setUser_email(loginbean.getEmail());
            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setPropertyTitle(classifiedbean.getClassified_name());
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.IMINTERESTED_CLASSIFIED.getScrenIndex(), requestParametersbean);

            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {


                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {
                                    sendIMInterestedDialog(getResources().getString(R.string.imrequest_sent_successfully));
                                } else {
                                    sendIMInterestedDialog(responcebean.getErrorMessage());

                                }


                            } else {
                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {
                                    sendIMInterestedDialog(getResources().getString(R.string.imrequest_sent_failed));
                                } else {
                                    sendIMInterestedDialog(responcebean.getErrorMessage());

                                }


                            }


                        } catch (Exception e) {
                            CommonMethods.customToastMessage(e.getMessage(), ClassifiedDetailActivity.this);

                        }

                    }


                }
            }, ClassifiedDetailActivity.this, getApplicationContext(), URLs.CLASSIFIEDINTEREST, jsonObjectGetPostParameterEachScreen, ScreensEnums.IMINTERESTED_CLASSIFIED.getScrenIndex(), SignInActivity.class.getClass());


            new RequestToServer(passParameterbean, retryParameterbean).execute();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }


    public void sendContactUsRequestDialog() {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(ClassifiedDetailActivity.this, R.style.DialogCustomTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.classisfied_dialog);

            RelativeLayout relativeLayout_contact_us_container = (RelativeLayout) dialog.findViewById(R.id.contact_us_layout);

            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);
            final BaseEdittext baseEdittext_message = (BaseEdittext) dialog.findViewById(R.id.edittext_classfied_contact_message);
            BaseTextview baseTextview_message = (BaseTextview) dialog.findViewById(R.id.textview_im_interest_messssage);
            relativeLayout_contact_us_container.setVisibility(View.VISIBLE);
            baseTextview_message.setVisibility(View.GONE);
            MyButtonView myButtonView_send = (MyButtonView) dialog.findViewById(R.id.mybuttonview_send_message);

            myButtonView_send.setOnClickListener(new MyOnClickListener(ClassifiedDetailActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        if (TextUtils.isEmpty(baseEdittext_message.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_message), ClassifiedDetailActivity.this);
                        } else if (!CommonMethods.isMorethenThreeCharacters(baseEdittext_message.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), ClassifiedDetailActivity.this);

                        } else {
                            requestParametersbean.setMessage(baseEdittext_message.getText().toString());
                            requestParametersbean.setUserId(commonSession.getLoggedUserID());
                            requestParametersbean.setMenuId("");
                            requestParametersbean.setMenuType("");
                            requestParametersbean.setCommunityID(communityID);
                            sentContactRequest(dialog);

                        }
                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }

    }

    public void sendIMInterestedDialog(String message) {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(ClassifiedDetailActivity.this, R.style.DialogCustomTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.classisfied_dialog);

            RelativeLayout relativeLayout_contact_us_container = (RelativeLayout) dialog.findViewById(R.id.contact_us_layout);
            BaseTextview baseTextview_message = (BaseTextview) dialog.findViewById(R.id.textview_im_interest_messssage);
            relativeLayout_contact_us_container.setVisibility(View.GONE);
            baseTextview_message.setVisibility(View.VISIBLE);
            baseTextview_message.setText(message);
            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);

            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }

    }

    public class ClassiFiedDetaislImageAdapter extends PagerAdapter {

        Activity activity = null;
        ArrayList<String> strings_urls = null;

        public ClassiFiedDetaislImageAdapter(Activity mactivity, ArrayList<String> strings_urls) {
            activity = mactivity;
            this.strings_urls = strings_urls;
        }


        @Override
        public int getCount() {
            return strings_urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            try {
                Context context = activity;

                view = LayoutInflater.from(context).inflate(R.layout.only_imageview_with_cardview, null);

                final SelectableRoundedImageView imageView_large = (SelectableRoundedImageView) view.findViewById(R.id.imageview);
                String url = strings_urls.get(position);


                if (isImageUrlValid(url)) {

                    Picasso.with(activity)
                            .load(url)
                            .into(imageView_large, new Callback() {
                                @Override
                                public void onSuccess() {
                                    imageView_large.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError() {
                                }
                            });

                } else {

                }


                (container).addView(view, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);
        }
    }
}
