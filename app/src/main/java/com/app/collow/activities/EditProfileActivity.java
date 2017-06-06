package com.app.collow.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.CommunitiesListAdapter;
import com.app.collow.adapters.InterestsAdapter;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.CommnityAutoCompleteTextview;
import com.app.collow.baseviews.InterestAutoCompleteTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.Communitybean;
import com.app.collow.beans.Interestbean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.Statesbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.commondialogs.MyDatePicker;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.httprequests.MyDatePickerInterface;
import com.app.collow.recyledecor.SpacesItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.ConnectionDetector;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/**
 * Created by harmis on 12/1/17.
 */

public class EditProfileActivity extends BaseActivity implements SetupViewInterface, MyDatePickerInterface {
    //Edit profile

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    public static BaseTextview baseTextview_state = null;
    public static EditProfileActivity editProfileActivity = null;
    public static Bitmap bitmap;
    public static CommnityAutoCompleteTextview commnityAutoCompleteTextview_editprofile_select_home_communtiy = null;
    public static InterestAutoCompleteTextview interestAutoCompleteTextview_select_your_interest = null;
    public static ArrayList<Communitybean> communitybeanArrayList = new ArrayList<>();
    public static HashMap<String, Statesbean> stringStatesbeanHashMap = new HashMap<>();
    public static ArrayList<Interestbean> interestbeanArrayList = new ArrayList<>();
    CommonSession commonSession = null;
    MyButtonView myButtonView_save = null;
    BaseEdittext editText_editprofile_changepassword = null;
    BaseEdittext edittext_editprofile_firstname = null;
    BaseEdittext edittext_editprofile_lastname = null;
    BaseEdittext edittext_editprofile_email = null;
    BaseEdittext editText_editprofile_city = null;
    BaseTextview baseTextview_left_side = null;
    BaseTextview baseTextview_date_of_birth = null;
    RelativeLayout relativeLayout_select_home_community = null;
    CircularImageView circularImageView_profile_edit_profile = null;
    RelativeLayout relativelayout_select_your_interests = null;
    RelativeLayout relativelayout_select_state = null;
    ImageView imageView_date_of_birth = null;
    ImageView imageView_select_state = null;
    ImageView imageView_select_communtiy = null, imageview_select_interest_new = null;
    ProgressBar progressBar_states = null, progressBar_communties = null, progressBar_interests = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    Loginbean loginbean = null;
    ConnectionDetector connectionDetector = null;
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    BaseTextview baseTextview_header_title = null;
    View view_home = null;
    int current_start_communities = 0, current_start_interests = 0;
    CommunitiesListAdapter communitiesListAdapter = null;
    InterestsAdapter interestsAdapter = null;
    public static boolean isCommunityDialogOpened = false, isInterestedDialogOpened = false;
    private Intent pictureActionIntent = null;
   public static BaseTextview baseTextview_select_community=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            super.onCreate(savedInstanceState);
            connectionDetector = new ConnectionDetector(EditProfileActivity.this);
            commonSession = new CommonSession(EditProfileActivity.this);
            retryParameterbean = new RetryParameterbean(EditProfileActivity.this, getApplicationContext(), getIntent().getExtras(), EditProfileActivity.class.getClass());
            editProfileActivity = this;
            loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            stringStatesbeanHashMap = SplashActvitiy.stringStatesbeanHashMap_globle;
            communitybeanArrayList.clear();


            if (connectionDetector.isConnectingToInternet())

            {
                getCommunitiesFromServer(1, 0);

            } else {
                progressBar_communties.setVisibility(View.GONE);
            }

            if (connectionDetector.isConnectingToInternet())

            {
                getInterestFromServer(1, 0);

            } else {
                progressBar_interests.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.displayLog("error", e.getMessage());
        }


    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.edit_profile));

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

            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
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
            e.printStackTrace();
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

    private void findViewByIDs() {
        try {
            //Sign in findviewby id
            view_home = getLayoutInflater().inflate(R.layout.edit_profile, null);

            frameLayout_container.addView(view_home);
            circularImageView_profile_edit_profile = (CircularImageView) view_home.findViewById(R.id.profile_image);
            circularImageView_profile_edit_profile.setVisibility(View.VISIBLE);
            edittext_editprofile_firstname = (BaseEdittext) view_home.findViewById(R.id.edittext_editprofile_firstname);
            edittext_editprofile_lastname = (BaseEdittext) view_home.findViewById(R.id.edittext_editprofile_lastname);
            edittext_editprofile_email = (BaseEdittext) view_home.findViewById(R.id.edittext_editprofile_email);
            editText_editprofile_city = (BaseEdittext) view_home.findViewById(R.id.edittext_editprofile_city);
            editText_editprofile_changepassword = (BaseEdittext) view_home.findViewById(R.id.edittext_editprofile_change_password);

            commnityAutoCompleteTextview_editprofile_select_home_communtiy = (CommnityAutoCompleteTextview) view_home.findViewById(R.id.communtitytextview_editprofile_select_home_communtiy);
            interestAutoCompleteTextview_select_your_interest = (InterestAutoCompleteTextview) view_home.findViewById(R.id.communtitytextview_editprofile_select_your_interest);


            baseTextview_select_community= (BaseTextview) view_home.findViewById(R.id.basetextview_select_home_communtiy);

            // commnityAutoCompleteTextview_editprofile_select_home_communtiy.setInputType(InputType.TYPE_NULL);
            //  commnityAutoCompleteTextview_editprofile_select_home_communtiy.requestFocus();
            relativeLayout_select_home_community = (RelativeLayout) view_home.findViewById(R.id.relativelayout_select_home_communtiy);
            relativelayout_select_your_interests = (RelativeLayout) view_home.findViewById(R.id.relativelayout_select_your_interests);
            relativelayout_select_state = (RelativeLayout) view_home.findViewById(R.id.relativelayout_select_state);


            //Combo textviews
            baseTextview_date_of_birth = (BaseTextview) view_home.findViewById(R.id.textview_editprofile_date_of_birth);
            baseTextview_state = (BaseTextview) view_home.findViewById(R.id.textview_editprofile_state);


            //Combo imageviews
            imageView_date_of_birth = (ImageView) view_home.findViewById(R.id.imageview_editprofile_date_of_birth);
            imageView_select_state = (ImageView) view_home.findViewById(R.id.imageview_editprofile_select_state);
            imageView_select_communtiy = (ImageView) view_home.findViewById(R.id.imageview_select_community);
            imageview_select_interest_new = (ImageView) view_home.findViewById(R.id.imageview_select_interest_new);


            progressBar_states = (ProgressBar) view_home.findViewById(R.id.progress_bar_states);
            progressBar_communties = (ProgressBar) view_home.findViewById(R.id.progress_get_communties);
            progressBar_interests = (ProgressBar) view_home.findViewById(R.id.progress_get_interesets);


            myButtonView_save = (MyButtonView) view_home.findViewById(R.id.mybuttonview_editprofile_save);
        } catch (Resources.NotFoundException e) {
            CommonMethods.displayLog("error", e.getMessage());

        }

    }


    private void setupEvents() {
        try {
            final Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());
            if (loginbean != null) {
                if (CommonMethods.isImageUrlValid(loginbean.getProfile_pic())) {
                    CommonMethods.displayLog("Image", loginbean.getProfile_pic());
                    Picasso.with(this)
                            .load(loginbean.getProfile_pic())
                            .into(circularImageView_profile_edit_profile, new Callback() {
                                @Override
                                public void onSuccess() {
                                    CommonMethods.displayLog("Success", "Image done");
                                }

                                @Override
                                public void onError() {
                                    circularImageView_profile_edit_profile.setImageResource(R.drawable.user_defualt_icon);
                                }
                            });
                }


                Communitybean communitybean =null;

                if (loginbean.getHomeCommunityName() != null) {
                    communitybean = new Communitybean();
                    communitybean.setCommunityName(loginbean.getHomeCommunityName());
                    baseTextview_select_community.setText(loginbean.getHomeCommunityName());

                }

                if (loginbean.getHomeCommunityId() != null) {
                    communitybean.setCommunityID(loginbean.getHomeCommunityId());
                }
                if(communitybean!=null)
                {
                    baseTextview_select_community.setTag(communitybean);
                    commnityAutoCompleteTextview_editprofile_select_home_communtiy.addObject(communitybean);

                }


            }


            circularImageView_profile_edit_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDialog();
                }
            });
            baseTextview_date_of_birth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new MyDatePicker();

                    newFragment.show(getSupportFragmentManager(), "DatePicker");
                }
            });
            imageView_date_of_birth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new MyDatePicker();

                    newFragment.show(getSupportFragmentManager(), "DatePicker");
                }
            });
            baseTextview_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (stringStatesbeanHashMap.size() == 0) {
                        getStatesListing();
                    } else {
                        Intent intent = new Intent(EditProfileActivity.this, SelectStateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("states", stringStatesbeanHashMap);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }


                }
            });
            imageView_select_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stringStatesbeanHashMap.size() == 0) {
                        getStatesListing();
                    } else {
                        Intent intent = new Intent(EditProfileActivity.this, SelectStateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("states", stringStatesbeanHashMap);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                }
            });


            commnityAutoCompleteTextview_editprofile_select_home_communtiy.setCursorVisible(false);
            interestAutoCompleteTextview_select_your_interest.setCursorVisible(false);

            commnityAutoCompleteTextview_editprofile_select_home_communtiy.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {


                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }

                        if (connectionDetector.isConnectingToInternet())

                        {
                            if (communitybeanArrayList.size() == 0) {
                                finish();
                                startActivity(getIntent());
                            } else {
                                showDialogForSelectCommunities();

                            }


                        } else {
                            commnityAutoCompleteTextview_editprofile_select_home_communtiy.clearFocus();
                            commnityAutoCompleteTextview_editprofile_select_home_communtiy.setFocusable(false);
                            CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), EditProfileActivity.this);
                        }


                    }

                    return false;
                }
            });

            interestAutoCompleteTextview_select_your_interest.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }

                        if (connectionDetector.isConnectingToInternet())

                        {
                            if (interestbeanArrayList.size() == 0) {
                                finish();
                                startActivity(getIntent());
                            } else {
                                showDialogForSelectInterests();

                            }


                        } else {
                            interestAutoCompleteTextview_select_your_interest.clearFocus();
                            interestAutoCompleteTextview_select_your_interest.setFocusable(false);
                            CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), EditProfileActivity.this);
                        }

                    }
                    return false;
                }
            });


            baseTextview_select_community.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        if (connectionDetector.isConnectingToInternet())

                        {
                            if (communitybeanArrayList.size() == 0) {
                                getCommunitiesFromServer(2, 0);
                            } else {
                                showDialogForSelectCommunities();
                            }

                        } else {
                            CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), EditProfileActivity.this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
            imageView_select_communtiy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        if (connectionDetector.isConnectingToInternet())

                        {
                            if (communitybeanArrayList.size() == 0) {
                                getCommunitiesFromServer(2, 0);
                            } else {
                                showDialogForSelectCommunities();
                            }

                        } else {
                            CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), EditProfileActivity.this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


            imageview_select_interest_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (connectionDetector.isConnectingToInternet())

                        {
                            if (interestbeanArrayList.size() == 0) {
                                getInterestFromServer(2, 0);
                            } else {
                                showDialogForSelectInterests();


                            }
                        } else {
                            CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), EditProfileActivity.this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            if (loginbean.getEmail() != null) {
                edittext_editprofile_email.setText(loginbean.getEmail());
                edittext_editprofile_email.setEnabled(false);
            }
            if (loginbean.getFirst_name() != null) {
                edittext_editprofile_firstname.setText(loginbean.getFirst_name());
            }

            if (loginbean.getLast_name() != null) {
                edittext_editprofile_lastname.setText(loginbean.getLast_name());
            }


            myButtonView_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (loginbean != null) {
                        requestParametersbean.setUserId(loginbean.getUserid());
                        requestParametersbean.setUser_email(loginbean.getEmail());


                    }


                    requestParametersbean.setFirst_name(edittext_editprofile_firstname.getText().toString());

                    requestParametersbean.setLast_name(edittext_editprofile_lastname.getText().toString());
                    requestParametersbean.setCity(editText_editprofile_city.getText().toString());

                    if (TextUtils.isEmpty(baseTextview_state.getText().toString())) {


                    } else {
                        Objects objects = (Objects) baseTextview_state.getTag();
                        if (objects != null) {
                            requestParametersbean.setStateText(baseTextview_state.getText().toString());
                            requestParametersbean.setState(baseTextview_state.getTag().toString());

                        }
                    }

                    requestParametersbean.setDob(baseTextview_date_of_birth.getText().toString());

                 //   List<Communitybean> communitybeanList = commnityAutoCompleteTextview_editprofile_select_home_communtiy.getObjects();
                    List<Communitybean> communitybeanList = new ArrayList<Communitybean>();

                    Object object=baseTextview_select_community.getTag();

                    if(object!=null)
                    {
                        Communitybean communitybean= (Communitybean) object;
                        communitybeanList.add(communitybean);
                    }

                        if (communitybeanList.size() != 0) {
                            List<String> stringList_community_ids = new ArrayList<String>();
                            List<String> stringList_community_texts = new ArrayList<String>();

                            for (int i = 0; i < communitybeanList.size(); i++) {

                                stringList_community_ids.add(communitybeanList.get(i).getCommunityID());
                                stringList_community_texts.add(communitybeanList.get(i).getCommunityName());

                            }
                            CommonMethods.displayLog("ids", stringList_community_ids.toString());

                            String communityIDs = CommonMethods.getStringWithCommaFromList(stringList_community_ids);
                            String communityTexts = CommonMethods.getStringWithCommaFromList(stringList_community_texts);

                            requestParametersbean.setCommunity(communityIDs);
                            requestParametersbean.setCommunityText(communityTexts);

                        }










                    List<Interestbean> interestbeanList = interestAutoCompleteTextview_select_your_interest.getObjects();


                    if (interestbeanList != null) {
                        if (interestbeanList.size() != 0) {
                            List<String> stringList_interest_ids = new ArrayList<String>();
                            List<String> stringList_interest_textss = new ArrayList<String>();

                            for (int i = 0; i < interestbeanList.size(); i++) {
                                stringList_interest_ids.add(interestbeanList.get(i).getInterestID());
                                stringList_interest_textss.add(interestbeanList.get(i).getInterestName());

                            }

                            String interestIDs = CommonMethods.getStringWithCommaFromList(stringList_interest_ids);
                            String interestNamess = CommonMethods.getStringWithCommaFromList(stringList_interest_textss);

                            requestParametersbean.setInterest(interestIDs);
                            requestParametersbean.setInterestText(interestNamess);

                        }
                    }


                    JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.SAVE_MORE_INFORMATION.getScrenIndex(), requestParametersbean);
                    saveMoreInformation(URLs.SAVEMOREINFORMATION, jsonObjectGetPostParameterEachScreen, ScreensEnums.SAVE_MORE_INFORMATION.getScrenIndex());


                }
            });
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }
    }


    public void saveMoreInformation(String url, JSONObject jsonObjectGetPostParameterEachScreen, int index) {


        PassParameterbean passParameterbean = new PassParameterbean(this, EditProfileActivity.this, getApplicationContext(), url, jsonObjectGetPostParameterEachScreen, index, EditProfileActivity.class.getClass());
        passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());
        passParameterbean.setBitmap(bitmap);
        passParameterbean.setForImageUploadingCustomKeywordName("profile_pic");
        new RequestToServer(passParameterbean, retryParameterbean).execute();
    }


    @Override
    public void setupUI(Responcebean responcebean) {

        boolean isLoginSuccess = false;
        try {
            if (responcebean.isServiceSuccess()) {


                JSONObject jsonObject_main = null;
                jsonObject_main = new JSONObject(responcebean.getResponceContent());

                if (jsonObject_main.has(JSONCommonKeywords.success)) {
                    int success = jsonObject_main.getInt(JSONCommonKeywords.success);
                    if (success == 1) {
                        JSONObject jsonObject_profile = jsonObject_main.getJSONObject(JSONCommonKeywords.Profile);


                        Loginbean loginbean = new Loginbean();


                        if (jsonObject_profile.has(JSONCommonKeywords.userid)) {
                            isLoginSuccess = true;

                            loginbean.setUserid(jsonObject_profile.getString(JSONCommonKeywords.userid));
                        }

                        if (jsonObject_profile.has(JSONCommonKeywords.name)) {

                            loginbean.setName(jsonObject_profile.getString(JSONCommonKeywords.name));
                        }
                        if (jsonObject_profile.has(JSONCommonKeywords.FirstName)) {

                            loginbean.setFirst_name(jsonObject_profile.getString(JSONCommonKeywords.FirstName));
                        }

                        if (jsonObject_profile.has(JSONCommonKeywords.LastName)) {

                            loginbean.setLast_name(jsonObject_profile.getString(JSONCommonKeywords.LastName));
                        }
                        if (jsonObject_profile.has(JSONCommonKeywords.IscompletedProfile)) {

                            loginbean.setIscompletedprofile(jsonObject_profile.getString(JSONCommonKeywords.IscompletedProfile));
                        }
                        if (jsonObject_profile.has(JSONCommonKeywords.Email)) {

                            loginbean.setEmail(jsonObject_profile.getString(JSONCommonKeywords.Email));
                        }
                        if (jsonObject_profile.has(JSONCommonKeywords.ProfilePic)) {

                            loginbean.setProfile_pic(jsonObject_profile.getString(JSONCommonKeywords.ProfilePic));
                        }

                        if (jsonObject_profile.has(JSONCommonKeywords.State)) {

                            loginbean.setState(jsonObject_profile.getString(JSONCommonKeywords.State));
                        }
                        if (jsonObject_profile.has(JSONCommonKeywords.Dob)) {

                            loginbean.setDob(jsonObject_profile.getString(JSONCommonKeywords.Dob));
                        }
                        if (jsonObject_profile.has(JSONCommonKeywords.Interest)) {

                            loginbean.setInterest(jsonObject_profile.getString(JSONCommonKeywords.Interest));
                        }
                        if (loginbean.getIscompletedprofile() == null || loginbean.getIscompletedprofile().equals("")) {
                            commonSession.storeUserProfileCompleted(false);
                        } else if (loginbean.getIscompletedprofile().equals("1")) {
                            commonSession.storeUserProfileCompleted(true);

                        } else {
                            commonSession.storeUserProfileCompleted(false);

                        }


                        //this is convert loginbean object to json and storing in session
                        CommonMethods.convertLoginbeanToJSON(getApplicationContext(), loginbean);
                        if (jsonObject_main.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                        } else {
                            responcebean.setErrorMessage(getResources().getString(R.string.your_profile_save_successfully));
                        }
                    } else {
                        if (jsonObject_main.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                        }
                    }
                } else {
                    if (jsonObject_main.has(JSONCommonKeywords.Comments)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.Comments));
                    }
                }


                if (isLoginSuccess) {
                    if (responcebean.getErrorMessage() != null) {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), EditProfileActivity.this);

                    }

                } else {
                    if (responcebean.getErrorMessage() != null) {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), EditProfileActivity.this);

                    }
                }
            } else {

                if (responcebean.getErrorMessage() != null) {
                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), EditProfileActivity.this);

                }
            }
        } catch (Exception e) {
            if (e instanceof JSONException) {
                CommonMethods.customToastMessage(getResources().getString(R.string.html_comming), EditProfileActivity.this);
            }
            CommonMethods.displayLog("error", e.getMessage());


        }


    }


    public void showDialogForSelectCommunities() {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(EditProfileActivity.this, R.style.DialogCustomTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.edit_profile_dialog_main);

            final Handler handler;
            handler = new Handler();

            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);

            RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            recyclerView.setLayoutManager(gaggeredGridLayoutManager);


            int spanCount = 2; // 3 columns
            int spacing = 10; // 50px
            boolean includeEdge = false;
            recyclerView.addItemDecoration(new SpacesItemDecoration(10));

            isCommunityDialogOpened = true;
            communitiesListAdapter = new CommunitiesListAdapter(recyclerView, EditProfileActivity.this,dialog);
            recyclerView.setAdapter(communitiesListAdapter);
            communitiesListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (isCommunityDialogOpened) {
                                current_start_communities += 100;
                                requestParametersbean.setStart_limit(current_start_communities);
                                getCommunitiesFromServer(1, current_start_communities);
                            }


                        }
                    }, 2000);

                }
            });
            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    isCommunityDialogOpened = false;

                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }

    }

    public void showDialogForSelectInterests() {
        try {
            // custom dialog
            final Dialog dialog = new Dialog(EditProfileActivity.this, R.style.DialogCustomTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.edit_profile_dialog_main);

            final Handler handler;
            handler = new Handler();
            RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            recyclerView.setLayoutManager(gaggeredGridLayoutManager);


            int spanCount = 2; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = false;
            recyclerView.addItemDecoration(new SpacesItemDecoration(10));


            isInterestedDialogOpened = true;
            interestsAdapter = new InterestsAdapter(recyclerView, EditProfileActivity.this);
            recyclerView.setAdapter(interestsAdapter);
            interestsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (isInterestedDialogOpened) {
                                current_start_interests += 100;
                                requestParametersbean.setStart_limit(current_start_interests);
                                getInterestFromServer(1, current_start_interests);

                            }


                        }
                    }, 2000);

                }
            });


            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);

            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInterestedDialogOpened = false;
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


    @Override
    public void selectedDate(String selectedDate, Date date) {
        baseTextview_date_of_birth.setText(selectedDate);

    }

    @Override
    public void selectedDate(String selectedDate, Date date, int index) {

    }

    @Override
    public void selectedTime(int hourOfDay, int minute, int forwhichFieldNeedToSet, boolean is24HOursFormat) {

    }


    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    // this method is used for getcommunties
    public void getCommunitiesFromServer(final int index, final int startLimit) {
        try {

            RequestParametersbean requestParametersbean = new RequestParametersbean();
            requestParametersbean.setStart_limit(startLimit);
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.GET_COMMUNTIES.getScrenIndex(), requestParametersbean);

            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {

                                ArrayList<Communitybean> communitybeanArrayList_local = new ArrayList<>();
                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.CommunityList)) {
                                    JSONArray jsonArray_communties = jsonObject_main.getJSONArray(JSONCommonKeywords.CommunityList);
                                    for (int i = 0; i < jsonArray_communties.length(); i++) {

                                        JSONObject jsonObject_single = jsonArray_communties.getJSONObject(i);

                                        Communitybean communitybean = new Communitybean();

                                        if (jsonObject_single.has(JSONCommonKeywords.id)) {
                                            communitybean.setCommunityID(jsonObject_single.getString(JSONCommonKeywords.id));

                                        }

                                        if (jsonObject_single.has(JSONCommonKeywords.CommunityName)) {
                                            communitybean.setCommunityName(jsonObject_single.getString(JSONCommonKeywords.CommunityName));

                                        }

                                        communitybeanArrayList_local.add(communitybean);


                                    }
                                }


                                if (startLimit == 0) {
                                    communitybeanArrayList = communitybeanArrayList_local;
                                    if (communitiesListAdapter != null) {
                                        communitiesListAdapter.notifyDataSetChanged();

                                    }

                                } else {

                                    int start = communitybeanArrayList.size();

                                    for (int i = 0; i < communitybeanArrayList_local.size(); i++) {

                                        communitybeanArrayList.add(start, communitybeanArrayList_local.get(i));
                                        communitiesListAdapter.notifyItemInserted(communitybeanArrayList.size());
                                        start++;

                                    }
                                    communitiesListAdapter.setLoaded();

                                }

                                if (index == 2) {
                                    showDialogForSelectCommunities();
                                }


                            } else {

                            }


                            progressBar_communties.setVisibility(View.GONE);


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar_communties.setVisibility(View.GONE);
                            CommonMethods.customToastMessage(e.getMessage(), EditProfileActivity.this);

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GET_COMMUNITIES, jsonObjectGetPostParameterEachScreen, ScreensEnums.GET_COMMUNTIES.getScrenIndex(), SignInActivity.class.getClass());

            progressBar_communties.setVisibility(View.VISIBLE);

            new RequestToServer(passParameterbean, null).execute();


        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }
    }

    // this method is used for getinterest
    public void getInterestFromServer(final int index, final int startLimit) {
        try {


            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {


                                ArrayList<Interestbean> interestbeen_local = new ArrayList<>();
                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.InterestList)) {
                                    JSONArray jsonArray_interests = jsonObject_main.getJSONArray(JSONCommonKeywords.InterestList);
                                    for (int i = 0; i < jsonArray_interests.length(); i++) {

                                        JSONObject jsonObject_single = jsonArray_interests.getJSONObject(i);

                                        Interestbean interestbean = new Interestbean();

                                        if (jsonObject_single.has(JSONCommonKeywords.id)) {
                                            interestbean.setInterestID(jsonObject_single.getString(JSONCommonKeywords.id));

                                        }

                                        if (jsonObject_single.has(JSONCommonKeywords.InterestName)) {
                                            interestbean.setInterestName(jsonObject_single.getString(JSONCommonKeywords.InterestName));

                                        }

                                        interestbeen_local.add(interestbean);


                                    }


                                    if (startLimit == 0) {
                                        interestbeanArrayList = interestbeen_local;
                                        if (interestsAdapter != null) {
                                            interestsAdapter.notifyDataSetChanged();

                                        }

                                    } else {

                                        int start = interestbeanArrayList.size();

                                        for (int i = 0; i < interestbeen_local.size(); i++) {

                                            interestbeanArrayList.add(start, interestbeen_local.get(i));
                                            interestsAdapter.notifyItemInserted(communitybeanArrayList.size());
                                            start++;

                                        }
                                        interestsAdapter.setLoaded();

                                    }

                                    if (index == 2) {
                                        showDialogForSelectInterests();
                                    }
                                }


                            } else {

                            }


                            progressBar_interests.setVisibility(View.GONE);


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar_interests.setVisibility(View.GONE);
                            CommonMethods.customToastMessage(e.getMessage(), EditProfileActivity.this);

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GET_INTERESTS, null, ScreensEnums.GET_INTERESTS.getScrenIndex(), SignInActivity.class.getClass());

            progressBar_interests.setVisibility(View.VISIBLE);

            new RequestToServer(passParameterbean, null).execute();


        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }
    }

    // this method is used for login user
    public void getStatesListing() {
        try {


            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (jsonObject_main.has(JSONCommonKeywords.StateList)) {
                                JSONArray jsonArray_statelist = jsonObject_main.getJSONArray(JSONCommonKeywords.StateList);
                                if (jsonArray_statelist == null || jsonArray_statelist.equals("")) {

                                } else {
                                    if (jsonArray_statelist.length() != 0) {
                                        for (int i = 0; i < jsonArray_statelist.length(); i++) {

                                            JSONObject jsonObject_single = jsonArray_statelist.getJSONObject(i);

                                            String statecode = jsonObject_single.getString(JSONCommonKeywords.stateCode);
                                            String statename = jsonObject_single.getString(JSONCommonKeywords.StateName);
                                            Statesbean statesbean = new Statesbean();
                                            statesbean.setStateCode(statecode);
                                            statesbean.setStateName(statename);
                                            stringStatesbeanHashMap.put(statecode, statesbean);
                                        }
                                    }
                                }
                            }
                            progressBar_states.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar_states.setVisibility(View.GONE);

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GET_STATES_LISTING, null, ScreensEnums.LOGIN.getScrenIndex(), SignInActivity.class.getClass());

            progressBar_states.setVisibility(View.VISIBLE);


            new RequestToServer(passParameterbean, null).execute();


        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


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


                        bitmap = decodeAndResizeFile(new File(
                                cursor.getString(columnIndex)));
                        cursor.close();


                        if (bitmap != null) {
                            circularImageView_profile_edit_profile.setImageBitmap(bitmap);

                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), EditProfileActivity.this);
                }
            } else if (resultCode == RESULT_CANCELED) {
                CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), EditProfileActivity.this);

            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {

                try {

                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");


                    if (bitmap != null) {
                        circularImageView_profile_edit_profile.setImageBitmap(bitmap);

                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), EditProfileActivity.this);

            }
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


}