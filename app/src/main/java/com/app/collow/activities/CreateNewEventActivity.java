package com.app.collow.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ModificationOptions;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.EventTypebean;
import com.app.collow.beans.Eventbean;
import com.app.collow.beans.Imagebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beansgenerate.EventbeanBuild;
import com.app.collow.commondialogs.MyDatePicker;
import com.app.collow.commondialogs.MyTimePicker;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.httprequests.MyDatePickerInterface;
import com.app.collow.recyledecor.GridSpacingItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateNewEventActivity extends BaseActivity implements SetupViewInterface, MyDatePickerInterface {

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    public static BaseTextview baseTextview_event_type = null;
    public ArrayList<Imagebean> deletedImagesContainer = new ArrayList<>();
    protected Handler handler;
    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    BaseTextview textview_acnewevent_date = null;
    BaseTextview textview_acnewevent_starttime = null;
    BaseTextview textview_acnewevent_endtime = null;
    BaseEdittext edittext_acnewevent_eventlocation = null;
    BaseEdittext edittext_acnewevent_description = null;
    BaseEdittext edittext_acnewevent_title = null;
    BaseTextview baseTextview_left_side = null;
    MyButtonView button_acnewevent_save = null;
    CommonSession commonSession = null;
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageView_plus_icon_only_event;
    //header iterms
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    ArrayList<Imagebean> bitmapArrayList_uploading = new ArrayList<>();
    RecyclerView recyclerView_grid_event_images = null;
    UploadImageAdapter uploadImageAdapter = null;
    RelativeLayout relativeLayout_upload_image_plus_view_event = null;
    String communityID = null;
    String communityText = null;
    int modifiy_format_index = 0;
    Eventbean eventbean = null;
    boolean isComeForNewEvent = false;
    BaseTextview baseTextview_all_days = null, baseTextview_custom_date = null;
    int evenTimeMode = CommonKeywords.EVENT_TIME_MODE_ALL_DAYS;
    ProgressBar progressBar_event = null;
    RelativeLayout relativeLayout_event_type = null;
    int position = 0;
    private Intent pictureActionIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(CreateNewEventActivity.this, getApplicationContext(), getIntent().getExtras(), CreateNewEventActivity.class.getClass());
            commonSession = new CommonSession(CreateNewEventActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityText = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);
                requestParametersbean.setCommunityID(communityID);
                modifiy_format_index = bundle.getInt(BundleCommonKeywords.KEY_MODIFICATION_FORMAT);
                eventbean = (Eventbean) bundle.getSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN);
                if (modifiy_format_index == ModificationOptions.ADD.getOperationIndex()) {
                    isComeForNewEvent = true;
                } else {
                    isComeForNewEvent = false;
                    position = bundle.getInt(BundleCommonKeywords.POSITION);

                }
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

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);

            if (isComeForNewEvent) {
                baseTextview_header_title.setText(getResources().getString(R.string.new_event));

            } else {
                baseTextview_header_title.setText(getResources().getString(R.string.edit_event));

            }

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);


            baseTextview_left_side.setText(getResources().getString(R.string.cancel));
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
            drawerLayout.setDrawerListener(drawerListener);
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.activity_acnew_event_main, null);

            baseTextview_all_days = (BaseTextview) view_home.findViewById(R.id.basetextview_all_days);
            baseTextview_custom_date = (BaseTextview) view_home.findViewById(R.id.basetextview_custom_date);

            edittext_acnewevent_title = (BaseEdittext) view_home.findViewById(R.id.edittext_acnewevent_eventtitle);
            textview_acnewevent_date = (BaseTextview) view_home.findViewById(R.id.textview_acnewevent_date);
            textview_acnewevent_date.setTag(CommonKeywords.NO);


            textview_acnewevent_starttime = (BaseTextview) view_home.findViewById(R.id.textview_acnewevent_starttime);
            textview_acnewevent_endtime = (BaseTextview) view_home.findViewById(R.id.textview_acnewevent_endtime);
            textview_acnewevent_starttime.setTag(CommonKeywords.NO);
            textview_acnewevent_endtime.setTag(CommonKeywords.NO);

            edittext_acnewevent_eventlocation = (BaseEdittext) view_home.findViewById(R.id.edittext_acnewevent_eventlocation);
            edittext_acnewevent_description = (BaseEdittext) view_home.findViewById(R.id.edittext_acnewevent_description);
            button_acnewevent_save = (MyButtonView) view_home.findViewById(R.id.button_acnewevent_save);

            imageView_plus_icon_only_event = (ImageView) view_home.findViewById(R.id.imageview_upload_image_plus_icon_event);
            relativeLayout_upload_image_plus_view_event = (RelativeLayout) view_home.findViewById(R.id.relativelayout_upload_create_event_images);


            progressBar_event = (ProgressBar) view_home.findViewById(R.id.progress_bar);
            baseTextview_event_type = (BaseTextview) view_home.findViewById(R.id.basetextview_select_event_type);
            EventTypebean eventTypebean = new EventTypebean();
            eventTypebean.setTypeName(CommonKeywords.NO.toString());


            baseTextview_event_type.setTag(eventTypebean);
            relativeLayout_event_type = (RelativeLayout) view_home.findViewById(R.id.relativelayout_select_event_type);
            relativeLayout_event_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CreateNewEventActivity.this, SelectEventTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.ACNEWEVENT.getScrenIndex());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });

            if (isComeForNewEvent) {
                button_acnewevent_save.setText(getResources().getString(R.string.createevent));

            } else {
                button_acnewevent_save.setText(getResources().getString(R.string.edit_event));

            }


            button_acnewevent_save.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {


                    try {
                        requestParametersbean.setTitle(edittext_acnewevent_title.getText().toString());
                        String tag = textview_acnewevent_date.getTag().toString();
                        if (tag.equals(CommonKeywords.NO)) {

                        } else {
                            requestParametersbean.setDate(textview_acnewevent_date.getTag().toString());

                        }


                        requestParametersbean.setStartTime(textview_acnewevent_starttime.getText().toString());

                        requestParametersbean.setEndTime(textview_acnewevent_endtime.getText().toString());
                        requestParametersbean.setLocation(edittext_acnewevent_eventlocation.getText().toString());
                        requestParametersbean.setDescripton(edittext_acnewevent_description.getText().toString());

                        requestParametersbean.setUserId(commonSession.getLoggedUserID());
                        if (bitmapArrayList_uploading.size() == 0) {

                        } else {
                            int size = bitmapArrayList_uploading.size() - 1;
                            requestParametersbean.setImgCount(String.valueOf(size));
                        }
                        if (baseTextview_event_type.getText().equals(getResources().getString(R.string.select_event_type))) {

                        } else {
                            EventTypebean eventTypebean = (EventTypebean) baseTextview_event_type.getTag();
                            if (CommonMethods.isTextAvailable(eventTypebean.getTypeName())) {
                                if (eventTypebean.getTypeName().equals(CommonKeywords.NO)) {

                                } else {
                                    requestParametersbean.setTypeID(eventTypebean.getTypeID());

                                }
                            }

                        }


                        createNewEvent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            recyclerView_grid_event_images = (RecyclerView) view_home.findViewById(R.id.recycler_view_grid_create_event);
            recyclerView_grid_event_images.setHasFixedSize(true);
            // Disabled nested scrolling since Parent scrollview will scroll the content.
            recyclerView_grid_event_images.setNestedScrollingEnabled(false);
            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
            recyclerView_grid_event_images.setLayoutManager(gaggeredGridLayoutManager);


            int spanCount = 3; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = false;
            recyclerView_grid_event_images.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


            uploadImageAdapter = new UploadImageAdapter(this);
            recyclerView_grid_event_images.setAdapter(uploadImageAdapter);
            relativeLayout_upload_image_plus_view_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDialog();
                }
            });
            imageView_plus_icon_only_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDialog();
                }
            });


            frameLayout_container.addView(view_home);

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void startDialog() {
        try {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
            myAlertDialog.setTitle(getResources().getString(R.string.upload_pictures_title));
            myAlertDialog.setMessage(getResources().getString(R.string.how_do_you_want_set));

            myAlertDialog.setPositiveButton(getResources().getString(R.string.gallery),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            pictureActionIntent = new Intent(
                                    Intent.ACTION_GET_CONTENT, null);
                            pictureActionIntent.setType("image/*");
                            pictureActionIntent.putExtra("return-data", true);
                            startActivityForResult(pictureActionIntent,
                                    GALLERY_PICTURE);
                        }
                    });

            myAlertDialog.setNegativeButton(getResources().getString(R.string.camera),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                            }

                        }
                    });
            myAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setupEvents() {

        if (modifiy_format_index == ModificationOptions.EDIT.getOperationIndex()) {
            if (eventbean != null) {


                requestParametersbean.setEventID(eventbean.getEvent_id());

                if (CommonMethods.isTextAvailable(eventbean.getEventType())) {
                    baseTextview_event_type.setText(eventbean.getEventType());

                    EventTypebean eventTypebean = new EventTypebean();
                    eventTypebean.setTypeID(eventbean.getEventTypeID());
                    eventTypebean.setTypeName(eventbean.getEventType());
                    baseTextview_event_type.setTag(eventTypebean);
                }


                if (CommonMethods.isTextAvailable(eventbean.getEvent_title())) {
                    edittext_acnewevent_title.setText(eventbean.getEvent_title());
                }
                if (CommonMethods.isTextAvailable(eventbean.getEvent_date())) {

                    textview_acnewevent_date.setText(eventbean.getEvent_date());


                }
                if (CommonMethods.isTextAvailable(eventbean.getEvent_start_time())) {

                    textview_acnewevent_starttime.setText(eventbean.getEvent_start_time());


                }
                if (CommonMethods.isTextAvailable(eventbean.getEvent_end_time())) {

                    textview_acnewevent_endtime.setText(eventbean.getEvent_end_time());


                }
                if (CommonMethods.isTextAvailable(eventbean.getEvent_description())) {

                    edittext_acnewevent_description.setText(eventbean.getEvent_description());


                }
                if (CommonMethods.isTextAvailable(eventbean.getLocation())) {

                    edittext_acnewevent_eventlocation.setText(eventbean.getLocation());


                }


                if (CommonMethods.isTextAvailable(eventbean.getEvent_date())) {
                    textview_acnewevent_date.setText(eventbean.getEvent_date());
                }
                ArrayList<String> stringArrayList = eventbean.getStringArrayList_images_url();

                if (stringArrayList == null) {
                    recyclerView_grid_event_images.setVisibility(View.GONE);
                    relativeLayout_upload_image_plus_view_event.setVisibility(View.VISIBLE);

                } else {
                    if (stringArrayList.size() == 0) {
                        recyclerView_grid_event_images.setVisibility(View.GONE);
                        relativeLayout_upload_image_plus_view_event.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView_grid_event_images.setVisibility(View.VISIBLE);
                        relativeLayout_upload_image_plus_view_event.setVisibility(View.GONE);


                        for (int i = 0; i < stringArrayList.size(); i++) {

                            String url = stringArrayList.get(i);
                            Imagebean imagebean = new Imagebean();
                            imagebean.setUrl(url);
                            imagebean.setItComesFromServer(true);
                            bitmapArrayList_uploading.add(imagebean);

                        }

                        bitmapArrayList_uploading.add(bitmapArrayList_uploading.size(), getDefualPlusbean());


                        if (uploadImageAdapter != null) {
                            uploadImageAdapter.notifyDataSetChanged();
                        }


                    }

                    if (eventbean.getEvent_time_mode() == CommonKeywords.EVENT_TIME_MODE_CUSTOM_TIME) {
                        baseTextview_custom_date.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_selected), null, null, null);
                        baseTextview_all_days.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_unselected), null, null, null);
                        evenTimeMode = CommonKeywords.EVENT_TIME_MODE_CUSTOM_TIME;
                        textview_acnewevent_date.setVisibility(View.VISIBLE);

                    } else if (eventbean.getEvent_time_mode() == CommonKeywords.EVENT_TIME_MODE_ALL_DAYS) {
                        baseTextview_custom_date.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_unselected), null, null, null);
                        baseTextview_all_days.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_selected), null, null, null);
                        evenTimeMode = CommonKeywords.EVENT_TIME_MODE_ALL_DAYS;
                        textview_acnewevent_date.setVisibility(View.GONE);


                    }
                }


            }


        } else {
            textview_acnewevent_date.setVisibility(View.GONE);

            baseTextview_custom_date.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_unselected), null, null, null);
            baseTextview_all_days.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_selected), null, null, null);

        }
        baseTextview_all_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview_acnewevent_date.setVisibility(View.GONE);

                evenTimeMode = CommonKeywords.EVENT_TIME_MODE_ALL_DAYS;

                baseTextview_all_days.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_selected), null, null, null);
                baseTextview_custom_date.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_unselected), null, null, null);


            }
        });
        baseTextview_custom_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview_acnewevent_date.setVisibility(View.VISIBLE);
                baseTextview_custom_date.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_selected), null, null, null);
                baseTextview_all_days.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dot_unselected), null, null, null);
                evenTimeMode = CommonKeywords.EVENT_TIME_MODE_CUSTOM_TIME;

            }
        });
        baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textview_acnewevent_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new MyDatePicker();

                Bundle bundle = new Bundle();
                bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.ACNEWEVENT.getScrenIndex());
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "NewEvent");
            }
        });


        textview_acnewevent_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new MyTimePicker();

                Bundle bundle = new Bundle();
                bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ModificationOptions.START_TIME.getOperationIndex());
                bundle.putBoolean(BundleCommonKeywords.KEY_NEED_24_HOURS_TIME_FORMAT, true);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "StartTime");
            }
        });
        textview_acnewevent_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new MyTimePicker();

                Bundle bundle = new Bundle();
                bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ModificationOptions.END_TIME.getOperationIndex());
                bundle.putBoolean(BundleCommonKeywords.KEY_NEED_24_HOURS_TIME_FORMAT, true);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "EndTime");
            }
        });
    }


    // this method is used for login user
    public void createNewEvent() {
        try {


            String url = "";
            if (!isComeForNewEvent) {
                if (deletedImagesContainer.size() != 0) {
                    JSONArray jsonArray_delete_images = new JSONArray();
                    for (int i = 0; i < deletedImagesContainer.size(); i++) {
                        Imagebean imagebean = deletedImagesContainer.get(i);
                        if (imagebean.isItComesFromServer()) {
                            jsonArray_delete_images.put(imagebean.getUrl());
                        }
                    }
                    requestParametersbean.setJsonArray_deleted_files_url(jsonArray_delete_images);
                }


                ArrayList<Imagebean> imagebeanArrayList_need_added = new ArrayList<>();
                if (bitmapArrayList_uploading.size() != 0) {
                    for (int i = 0; i < bitmapArrayList_uploading.size(); i++) {
                        Imagebean imagebean = bitmapArrayList_uploading.get(i);
                        if (imagebean.getBitmap() != null) {
                            imagebeanArrayList_need_added.add(imagebean);
                        }
                    }
                    bitmapArrayList_uploading.clear();
                    bitmapArrayList_uploading = imagebeanArrayList_need_added;
                    requestParametersbean.setBitmapArrayList_mutliple_image(imagebeanArrayList_need_added);
                    requestParametersbean.setImgCount(String.valueOf(imagebeanArrayList_need_added.size()));

                }
                requestParametersbean.setEventID(eventbean.getEvent_id());
                requestParametersbean.setActivityId(eventbean.getActivityID());
                url = URLs.EDITEVENT;
            } else {

                url = URLs.CREATEEVENT;

            }


            requestParametersbean.setEvent_time_mode(evenTimeMode);


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACNEWEVENT.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CreateNewEventActivity.this, getApplicationContext(), url, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACNEWEVENT.getScrenIndex(), CreateNewEventActivity.class.getClass());
            passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());
            passParameterbean.setBitmapArrayList_mutliple_image(bitmapArrayList_uploading);
            passParameterbean.setForImageUploadingCustomKeyword(false);
            passParameterbean.setForImageUploadingCustomKeywordName(JSONCommonKeywords.EventImage);
            passParameterbean.setNoNeedToDisplayLoadingDialog(false);
            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    @Override
    public void setupUI(Responcebean responcebean) {

        try {
            if (responcebean.isServiceSuccess()) {

                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                    //parse here data of following


                    if (CommonMethods.handleKeyInJSON(jsonObject_main, JSONCommonKeywords.event)) {
                        JSONObject jsonObject_event = jsonObject_main.getJSONObject(JSONCommonKeywords.event);
                        if (jsonObject_main.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                        }


                        if (responcebean.getErrorMessage() == null) {
                            if (isComeForNewEvent) {
                                CommonMethods.customToastMessage(getResources().getString(R.string.event_created), CreateNewEventActivity.this);

                            } else {
                                CommonMethods.customToastMessage(getResources().getString(R.string.event_updated), CreateNewEventActivity.this);

                            }

                        } else {
                            CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateNewEventActivity.this);

                        }

                        Eventbean eventbean = EventbeanBuild.getEventbeanFromJSON(jsonObject_event);


                        if (isComeForNewEvent) {
                            if ((ACListEventsMainActivity.aceventbeanArrayList_main != null)) {
                                ACListEventsMainActivity.aceventbeanArrayList_main.add(0, eventbean);

                                ACListEventsMainActivity.acListEventAdapter.notifyItemInserted(0);
                                ACListEventsMainActivity.mRecyclerView.smoothScrollToPosition(0);
                                ACListEventsMainActivity.mRecyclerView.setVisibility(View.VISIBLE);
                                ACListEventsMainActivity.baseTextview_error.setVisibility(View.GONE);

                                finish();

                            }

                        } else {


                            ACListEventsMainActivity.aceventbeanArrayList_main.set(eventbean.getPosition(), eventbean);
                            ACListEventsMainActivity.acListEventAdapter.notifyItemChanged(eventbean.getPosition());
                            ACListEventsMainActivity.mRecyclerView.smoothScrollToPosition(eventbean.getPosition());

                            if (ACEventDetailsActivity.acEventDetailsActivity != null) {
                                ACEventDetailsActivity.acEventDetailsActivity.finish();
                            }
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(CreateNewEventActivity.this, ACEventDetailsActivity.class);
                            bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN, eventbean);
                            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                            bundle.putInt(BundleCommonKeywords.POSITION, position);

                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();


                        }


                    }

                } else {
                    if (jsonObject_main.has(JSONCommonKeywords.message)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                    }


                    if (responcebean.getErrorMessage() == null) {

                        if (isComeForNewEvent) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.event_created_failed), CreateNewEventActivity.this);

                        } else {
                            CommonMethods.customToastMessage(getResources().getString(R.string.event_updated_failed), CreateNewEventActivity.this);

                        }

                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateNewEventActivity.this);

                    }

                }
            } else {
                if (isComeForNewEvent) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.event_created_failed), CreateNewEventActivity.this);

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.event_updated_failed), CreateNewEventActivity.this);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
            CommonMethods.customToastMessage(e.getMessage(), CreateNewEventActivity.this);

        }


    }


    @Override
    public void selectedDate(String selectedDate, Date date) {


        textview_acnewevent_date.setText(selectedDate);
        SimpleDateFormat sdfmt2 = new SimpleDateFormat("yyyy-MM-dd");
        String strOutput = sdfmt2.format(date);
        textview_acnewevent_date.setTag(strOutput);
    }

    @Override
    public void selectedDate(String selectedDate, Date date, int index) {

    }

    @Override
    public void selectedTime(int hourOfDay, int minute, int forwhichFieldNeedToSet, boolean is24HOursFormat) {


        if (forwhichFieldNeedToSet == ModificationOptions.START_TIME.getOperationIndex()) {
            String append = hourOfDay + "," + minute;
            textview_acnewevent_starttime.setTag(append);


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            if (is24HOursFormat) {
                set24hourStartTime(calendar);
            } else {
                Date start_date = calendar.getTime();
                // Get time from date
                SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
                String displayValue = timeFormatter.format(calendar.getTime());


                if (textview_acnewevent_endtime.getText().equals(getResources().getString(R.string.endtime))) {

                } else {

                    String gettag = textview_acnewevent_endtime.getTag().toString();
                    if (gettag.equals(CommonKeywords.NO)) {
                        textview_acnewevent_starttime.setText(displayValue);

                    } else {
                        String items[] = gettag.split(",");
                        Calendar calendar_end_time = Calendar.getInstance();
                        calendar_end_time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(items[0]));
                        calendar_end_time.set(Calendar.MINUTE, Integer.parseInt(items[1]));

                        Date endtime_date = calendar_end_time.getTime();


                        int value = start_date.compareTo(endtime_date);
                        if (value == 0) {

                            CommonMethods.customToastMessage(getResources().getString(R.string.start_time_end_time_not_same), CreateNewEventActivity.this);

                        } else if (!start_date.before(endtime_date)) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.start_time_should_be_before_end_time), CreateNewEventActivity.this);
                        } else {
                            textview_acnewevent_starttime.setText(displayValue);

                        }

                    }
                }


            }


        } else if (forwhichFieldNeedToSet == ModificationOptions.END_TIME.getOperationIndex()) {
            String append = hourOfDay + "," + minute;
            textview_acnewevent_endtime.setTag(append);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);


            if (is24HOursFormat) {
                set24hourEndTime(calendar);


            } else {

            }
        }

    }


    public void set24hourStartTime(Calendar calendar) {
        Date start_date = calendar.getTime();
        // Get time from date
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        String displayValue = timeFormatter.format(calendar.getTime());

        if (textview_acnewevent_endtime.getText().equals(getResources().getString(R.string.endtime))) {
            textview_acnewevent_starttime.setText(displayValue);

        } else {

            String gettag = textview_acnewevent_endtime.getTag().toString();
            if (gettag.equals(CommonKeywords.NO)) {
                textview_acnewevent_starttime.setText(displayValue);

            } else {
                String items[] = gettag.split(",");
                Calendar calendar_end_time = Calendar.getInstance();
                calendar_end_time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(items[0]));
                calendar_end_time.set(Calendar.MINUTE, Integer.parseInt(items[1]));

                Date endtime_date = calendar_end_time.getTime();


                int value = start_date.compareTo(endtime_date);
                if (value == 0) {

                    CommonMethods.customToastMessage(getResources().getString(R.string.start_time_end_time_not_same), CreateNewEventActivity.this);

                } else if (!start_date.before(endtime_date)) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.start_time_should_be_before_end_time), CreateNewEventActivity.this);
                } else {
                    textview_acnewevent_starttime.setText(displayValue);

                }

            }
        }
    }

    public void set24hourEndTime(Calendar calendar) {
        // Get time from date
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        String displayValue = timeFormatter.format(calendar.getTime());
        if (textview_acnewevent_starttime.getText().equals(getResources().getString(R.string.starttime))) {
            textview_acnewevent_endtime.setText(displayValue);

        } else {
            Date end_date_time = calendar.getTime();
            // Get time from date
            String gettag = textview_acnewevent_starttime.getTag().toString();
            if (gettag.equals(CommonKeywords.NO)) {
                textview_acnewevent_endtime.setText(displayValue);

            } else {
                String items[] = gettag.split(",");
                Calendar calendar_start_time = Calendar.getInstance();
                calendar_start_time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(items[0]));
                calendar_start_time.set(Calendar.MINUTE, Integer.parseInt(items[1]));

                Date start_date = calendar_start_time.getTime();

                int value = start_date.compareTo(end_date_time);
                if (value == 0) {

                    CommonMethods.customToastMessage(getResources().getString(R.string.start_time_end_time_not_same), CreateNewEventActivity.this);

                } else if (!end_date_time.after(start_date)) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.end_time_should_be_after_start_time), CreateNewEventActivity.this);
                } else {
                    textview_acnewevent_endtime.setText(displayValue);

                }

            }
        }
    }
    /*String date = "03/26/2012 11:00:00";
    String dateafter = "03/26/2012 11:59:00";
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm:ss");
    Date convertedDate = new Date();
    Date convertedDate2 = new Date();
    try {
        convertedDate = dateFormat.parse(date);
        convertedDate2 = dateFormat.parse(dateafter);
        if (convertedDate2.after(convertedDate)) {
            txtView.setText("true");
        } else {
            txtView.setText("false");
        }
    } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        try {
            if (requestCode == GALLERY_PICTURE) {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the Image from data

                        try {
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            // Get the cursor
                            Cursor cursor = getContentResolver()
                                    .query(selectedImage, filePathColumn, null,
                                            null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor
                                    .getColumnIndex(filePathColumn[0]);


                            Bitmap bitmap = decodeAndResizeFile(new File(
                                    cursor.getString(columnIndex)));
                            cursor.close();

                            if (bitmap != null) {

                                recyclerView_grid_event_images.setVisibility(View.VISIBLE);
                                relativeLayout_upload_image_plus_view_event.setVisibility(View.GONE);


                                if (bitmapArrayList_uploading.size() > 1) {
                                    //remove top layout
                                    Imagebean imagebean = bitmapArrayList_uploading.get(bitmapArrayList_uploading.size() - 1);
                                    bitmapArrayList_uploading.remove(imagebean);
                                } else {

                                }

                                Imagebean imagebean = new Imagebean();
                                imagebean.setBitmap(bitmap);
                                bitmapArrayList_uploading.add(imagebean);
                                bitmapArrayList_uploading.add(bitmapArrayList_uploading.size(), getDefualPlusbean());

                                if (uploadImageAdapter != null) {
                                    uploadImageAdapter.notifyDataSetChanged();
                                }
                            }


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else {
                        CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateNewEventActivity.this);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateNewEventActivity.this);

                }
            } else if (requestCode == CAMERA_REQUEST) {
                if (resultCode == RESULT_OK) {

                    try {

                        Bundle extras = data.getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");


                        if (bitmap != null) {

                            recyclerView_grid_event_images.setVisibility(View.VISIBLE);
                            relativeLayout_upload_image_plus_view_event.setVisibility(View.GONE);


                            if (bitmapArrayList_uploading.size() > 1) {
                                //remove top layout
                                Imagebean imagebean = bitmapArrayList_uploading.get(bitmapArrayList_uploading.size() - 1);
                                bitmapArrayList_uploading.remove(imagebean);
                            } else {

                            }

                            Imagebean imagebean = new Imagebean();
                            imagebean.setBitmap(bitmap);
                            bitmapArrayList_uploading.add(imagebean);
                            bitmapArrayList_uploading.add(bitmapArrayList_uploading.size(), getDefualPlusbean());

                            if (uploadImageAdapter != null) {
                                uploadImageAdapter.notifyDataSetChanged();
                            }
                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateNewEventActivity.this);

                }


            }
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    public Bitmap decodeAndResizeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    Imagebean getDefualPlusbean() {
        Imagebean imagebean = new Imagebean();
        imagebean.setPlusIconNeedToEnable(true);
        return imagebean;
    }


    public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageViewHolder> {

        private Context context;

        public UploadImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public UploadImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.only_imageview_with_close_icon, null);
            UploadImageViewHolder rcv = new UploadImageViewHolder(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(final UploadImageViewHolder holder, int position) {


            try {
                final Imagebean imagebean = bitmapArrayList_uploading.get(position);
                holder.imageview_delete_icon.setTag(imagebean);

                holder.imageview_delete_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Imagebean imagebean1 = (Imagebean) v.getTag();
                        if (imagebean1.isItComesFromServer()) {
                            bitmapArrayList_uploading.remove(imagebean1);
                            deletedImagesContainer.add(imagebean1);


                        } else {

                        }
                        if (bitmapArrayList_uploading.size() == 0) {
                            recyclerView_grid_event_images.setVisibility(View.GONE);
                            relativeLayout_upload_image_plus_view_event.setVisibility(View.VISIBLE);
                        }
                        if (uploadImageAdapter != null) {
                            uploadImageAdapter.notifyDataSetChanged();
                        }

                    }
                });

                //if need to create new event at that time call this code
                if (isComeForNewEvent) {
                    if (imagebean.isPlusIconNeedToEnable()) {

                        holder.view_main.setTag(imagebean);
                        holder.imageview_delete_icon.setVisibility(View.GONE);
                        holder.imageview_singe_only.setImageResource(R.drawable.plus_icon);

                    } else {
                        holder.imageview_singe_only.setImageBitmap(imagebean.getBitmap());

                    }
                }
                //if need to edit event image call this becuase we need to display image from url.
                else {


                    if (imagebean.isItComesFromServer()) {
                        holder.imageview_delete_icon.setVisibility(View.VISIBLE);

                        if (CommonMethods.isImageUrlValid(imagebean.getUrl())) {
                            holder.progressBar.setVisibility(View.VISIBLE);


                            Picasso.with(CreateNewEventActivity.this)
                                    .load(imagebean.getUrl())
                                    .into(holder.imageview_singe_only, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            holder.progressBar.setVisibility(View.GONE);

                                        }

                                        @Override
                                        public void onError() {
                                            holder.imageview_singe_only.setImageResource(R.drawable.defualt_square);
                                            holder.progressBar.setVisibility(View.GONE);

                                        }
                                    });


                        } else {
                            holder.imageview_singe_only.setImageResource(R.drawable.defualt_square);
                        }
                    } else {
                        if (imagebean.isPlusIconNeedToEnable()) {

                            holder.view_main.setTag(imagebean);
                            holder.imageview_delete_icon.setVisibility(View.GONE);
                            holder.imageview_singe_only.setImageResource(R.drawable.plus_icon);

                        } else {
                            holder.imageview_singe_only.setImageBitmap(imagebean.getBitmap());

                        }
                    }
                }


            } catch (Resources.NotFoundException e) {
                CommonMethods.displayLog("error", e.getMessage());

            }

        }

        @Override
        public int getItemCount() {
            return bitmapArrayList_uploading.size();
        }
    }

    public class UploadImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageview_singe_only = null, imageview_delete_icon = null;
        View view_main = null;
        ProgressBar progressBar = null;

        public UploadImageViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            view_main = itemView;
            imageview_singe_only = (ImageView) itemView.findViewById(R.id.imageview_single_only);
            imageview_delete_icon = (ImageView) itemView.findViewById(R.id.close_dialog);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }

        @Override
        public void onClick(View view) {
            if (bitmapArrayList_uploading.size() >= 12) {
                CommonMethods.customToastMessage(getResources().getString(R.string.image_upload_limit_corss_message), CreateNewEventActivity.this);
            } else {
                Imagebean imagebean = (Imagebean) view.getTag();
                if (imagebean.isPlusIconNeedToEnable()) {
                    startDialog();
                }
            }

        }
    }
}
