package com.app.collow.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.ACListEventAdapter;
import com.app.collow.adapters.ImageSlideAdapter;
import com.app.collow.allenums.ModificationOptions;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.ACFollowersbean;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Eventbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by harmis on 6/2/17.
 */

public class ACEventDetailsActivity extends BaseActivity {

    public static ACEventDetailsActivity acEventDetailsActivity=null;
    View view_home = null;
    BaseTextview textview_acviewevent_title= null;
    BaseTextview textview_acviewevent_date= null;
    BaseTextview textview_acviewevent_description= null;
    BaseTextview textview_acviewevent_time= null;
    BaseTextview  baseTextview_header_title=null;
    BaseTextview baseTextview_left_side=null;
    int current_start = 0;
    protected Handler handler;
    CommonSession commonSession = null;
    Eventbean eventbean=null;
    String communityID=null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null,imageview_right_foursquare=null;
    ImageView imageView_delete = null,imageView_view=null,imageView_edit=null,imageView_search=null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    ViewPager viewPager_images = null;
   ImageView left_nav, right_nav;
    ArrayList<String> strings_images_url_community=new ArrayList<>();
    int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(ACEventDetailsActivity.this, getApplicationContext(), getIntent().getExtras(), ACEventDetailsActivity.class.getClass());
            commonSession = new CommonSession(ACEventDetailsActivity.this);
            acEventDetailsActivity=this;
            Bundle bundle = getIntent().getExtras();
          if (bundle != null) {
              eventbean= (Eventbean) bundle.getSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN);
              communityID=bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
              position=bundle.getInt(BundleCommonKeywords.POSITION);
            }


            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title= (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.view_event));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageView_delete = (ImageView) toolbar_header.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.VISIBLE);
            imageView_view = (ImageView) toolbar_header.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.VISIBLE);
            imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.VISIBLE);
            imageview_right_foursquare= (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.GONE);

            imageView_search= (ImageView) toolbar_header.findViewById(R.id.imageview_search_items);
            imageView_search.setVisibility(View.GONE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);


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

            imageView_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogForConfirmDeleteEvent();
                }
            });

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {

            view_home = getLayoutInflater().inflate(R.layout.aclistevent_viewevent, null);

            textview_acviewevent_title=(BaseTextview) view_home.findViewById(R.id.viewevent_title);
            textview_acviewevent_date=(BaseTextview)view_home.findViewById(R.id.viewevent_date);
            textview_acviewevent_description=(BaseTextview)view_home.findViewById(R.id.viewevent_description);
            textview_acviewevent_time=(BaseTextview)view_home.findViewById(R.id.viewevent_time);


            left_nav = (ImageView) view_home.findViewById(R.id.left_nav);
            right_nav = (ImageView)view_home. findViewById(R.id.right_nav);
            viewPager_images = (ViewPager) view_home.findViewById(R.id.viewpager);
            frameLayout_container.addView(view_home);

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void setupEvents() {
        try {
            if(eventbean!=null)
            {
                if(CommonMethods.isTextAvailable(eventbean.getEvent_title()))
                {
                    textview_acviewevent_title.setText(eventbean.getEvent_title());
                }

                if(CommonMethods.isTextAvailable(eventbean.getEvent_date()))
                {
                    textview_acviewevent_date.setText(eventbean.getEvent_date());
                }
                if(CommonMethods.isTextAvailable(eventbean.getEvent_description()))
                {
                    textview_acviewevent_description.setText(eventbean.getEvent_description());
                }

                if(CommonMethods.isTextAvailable(eventbean.getEvent_title()))
                {
                    textview_acviewevent_title.setText(eventbean.getEvent_title());
                }

                if(eventbean.getEvent_time_mode()== CommonKeywords.EVENT_TIME_MODE_ALL_DAYS)
                {
                    textview_acviewevent_time.setText(getResources().getString(R.string.all_days));

                }
                else
                {
                    if (CommonMethods.isTextAvailable(eventbean.getEvent_start_time())) {

                        StringBuffer stringBuffer=new StringBuffer();

                        stringBuffer.append(eventbean.getEvent_start_time());
                        if (CommonMethods.isTextAvailable(eventbean.getEvent_end_time())) {
                            stringBuffer.append(" - ");

                            stringBuffer.append(eventbean.getEvent_end_time());


                        }

                        textview_acviewevent_time.setText(stringBuffer.toString());


                    }

                }






                strings_images_url_community=eventbean.getStringArrayList_images_url();
                if(strings_images_url_community!=null) {
                    if (strings_images_url_community.size() != 0)
                    {
                        ImageSlideAdapter imageSlideAdapter = new ImageSlideAdapter(ACEventDetailsActivity.this,strings_images_url_community,ScreensEnums.AC_VIEW_EVENT.getScrenIndex());
                        viewPager_images.setAdapter(imageSlideAdapter);
                        viewPager_images.setCurrentItem(0);
                        viewPager_images.setOffscreenPageLimit(strings_images_url_community.size());
                    }
                }




            }


            imageView_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ACEventDetailsActivity.this, CreateNewEventActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                    bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,eventbean);
                    bundle.putInt(BundleCommonKeywords.KEY_MODIFICATION_FORMAT, ModificationOptions.EDIT.getOperationIndex());
                    bundle.putInt(BundleCommonKeywords.POSITION, position);

                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            leftRigtButtonHandler();
            left_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tab = viewPager_images.getCurrentItem();
                    if (tab == 0) {
                        left_nav.setVisibility(View.GONE);
                        viewPager_images.setCurrentItem(tab);
                    }else if (tab > 0) {
                        tab--;
                        viewPager_images.setCurrentItem(tab);
                        left_nav.setVisibility(View.VISIBLE);
                        if (tab == 0) {
                            left_nav.setVisibility(View.GONE);
                            viewPager_images.setCurrentItem(tab);
                        }
                    }

                    if (tab == strings_images_url_community.size() - 1) {
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

                    if (tab == strings_images_url_community.size() - 1) {
                        right_nav.setVisibility(View.GONE);
                    } else {
                        right_nav.setVisibility(View.VISIBLE);
                        if (tab == strings_images_url_community.size() - 1) {
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

                    if(strings_images_url_community.size()==1)

                    {
                        left_nav.setVisibility(View.GONE);
                        right_nav.setVisibility(View.GONE);
                    }
                    else if(position==0)
                    {
                        left_nav.setVisibility(View.GONE);
                        right_nav.setVisibility(View.VISIBLE);
                    }
                    else if(position==strings_images_url_community.size()-1)
                    {
                        left_nav.setVisibility(View.VISIBLE);
                        right_nav.setVisibility(View.GONE);
                    }
                    else
                    {
                        left_nav.setVisibility(View.VISIBLE);
                        right_nav.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void leftRigtButtonHandler()
    {
        if(strings_images_url_community.size()==1)

        {
            left_nav.setVisibility(View.GONE);
            right_nav.setVisibility(View.GONE);
        }
        else if(strings_images_url_community.size()==0)

        {
            left_nav.setVisibility(View.GONE);
            right_nav.setVisibility(View.GONE);
        }
        else
        {
            left_nav.setVisibility(View.VISIBLE);
            right_nav.setVisibility(View.VISIBLE);
        }
    }

    private void showDialogForConfirmDeleteEvent() {

        // custom dialog
        try {
            final Dialog dialog = new Dialog(ACEventDetailsActivity.this, R.style.MyDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow()
                    .setLayout(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


            dialog.setContentView(R.layout.ac_followers_approve_reject_dialog);
            BaseTextview baseTextview_dialog_title= (BaseTextview) dialog.findViewById(R.id.textview_dialog_title);
            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);
            MyButtonView myButtonView_confirm = (MyButtonView) dialog.findViewById(R.id.mybuttonview_approve);
            MyButtonView myButtonView_cancel = (MyButtonView) dialog.findViewById(R.id.mybuttonview_reject);
            imageView_close_dialopg.setVisibility(View.GONE);

            myButtonView_confirm.setText(getResources().getString(R.string.confirm));
            myButtonView_cancel.setText(getResources().getString(R.string.logout_cancel));

            baseTextview_dialog_title.setVisibility(View.VISIBLE);
            baseTextview_dialog_title.setText(getResources().getString(R.string.delete_event_confirm));


            myButtonView_confirm.setOnClickListener(new MyOnClickListener(ACEventDetailsActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {

                        deleteEvent(dialog);


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            myButtonView_cancel.setOnClickListener(new MyOnClickListener(ACEventDetailsActivity.this) {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
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


    public void deleteEvent(final Dialog dialog)
    {
        requestParametersbean.setCommunityID(communityID);
        requestParametersbean.setUserId(commonSession.getLoggedUserID());
        requestParametersbean.setEventID(eventbean.getEvent_id());

        JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.DELETEEVENT.getScrenIndex(), requestParametersbean);


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
                                CommonMethods.customToastMessage(getResources().getString(R.string.event_deleted_successfully), ACEventDetailsActivity.this);

                            } else {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(), ACEventDetailsActivity.this);

                            }
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            //This event listing updating

                            finish();
                            if(ACListEventsMainActivity.acListEventAdapter!=null)
                            {

                                if(ACListEventsMainActivity.aceventbeanArrayList_main.size()==0)
                                {
                                    ACListEventsMainActivity.mRecyclerView.setVisibility(View.GONE);
                                    ACListEventsMainActivity.baseTextview_error.setVisibility(View.VISIBLE);
                                }
                                else {
                                    ACListEventsMainActivity.aceventbeanArrayList_main.remove(position);
                                    ACListEventsMainActivity.acListEventAdapter.notifyItemRemoved(position);
                                    ACListEventsMainActivity.acListEventAdapter.notifyItemRangeChanged(position,  ACListEventsMainActivity.aceventbeanArrayList_main.size());

                                }
                                if(ACListEventsMainActivity.aceventbeanArrayList_main.size()==0)
                                {
                                    ACListEventsMainActivity.mRecyclerView.setVisibility(View.GONE);
                                    ACListEventsMainActivity.baseTextview_error.setVisibility(View.VISIBLE);
                                }
                            }


                        } else {
                            if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                            }


                            if (responcebean.getErrorMessage() == null) {
                                CommonMethods.customToastMessage(getResources().getString(R.string.event_deleted_failed), ACEventDetailsActivity.this);

                            } else {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(), ACEventDetailsActivity.this);

                            }

                        }


                    } catch (Exception e) {
                        CommonMethods.customToastMessage(e.getMessage(), ACEventDetailsActivity.this);

                    }

                }


            }
        }, ACEventDetailsActivity.this, ACEventDetailsActivity.this, URLs.DELETEEVENT, jsonObjectGetPostParameterEachScreen, ScreensEnums.DELETEEVENT.getScrenIndex(), SignInActivity.class.getClass());
        new RequestToServer(passParameterbean, null).execute();
    }
}









