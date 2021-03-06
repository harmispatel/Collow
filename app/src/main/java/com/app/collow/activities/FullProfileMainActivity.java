package com.app.collow.activities;

import android.app.AlertDialog;
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
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.allenums.ModificationOptions;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.ACViewCommunityInfobean;
import com.app.collow.beans.Imagebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.app.collow.activities.CreateNewEventActivity.CAMERA_REQUEST;
import static com.app.collow.activities.CreateNewEventActivity.GALLERY_PICTURE;

public class FullProfileMainActivity extends BaseActivity implements SetupViewInterface {
    private static final int RC_SIGN_IN = 9001;
    public static ArrayList<ACViewCommunityInfobean> communitybeanArrayList = new ArrayList<>();
    public static Bitmap bitmap;
    public static RecyclerView mRecyclerView;
    protected Handler handler;
    View view_home = null;
    boolean isComeForeditinfo = false;
    int modifiy_format_index = 0;
    BaseTextview baseTextview_header_title = null;
    MyButtonView button_editinfo_save = null;
    ImageView imageview_viewcommunityinfo_profilepic;
    BaseEdittext edittext_viewcommunityinfo_email;
    BaseEdittext edittext_viewcommunityinfo_flatno;
    BaseEdittext edittext_viewcommunityinfo_flattype;
    BaseEdittext edittext_viewcommunityinfo_contactno;
    BaseEdittext edittext_viewcommunityinfo_address;
    BaseEdittext edittext_viewcommunityinfo_yearbuilt;
    BaseEdittext edittext_viewcommunityinfo_fax;
    BaseEdittext edittext_viewcommunityinfo_description;
    ArrayList<Imagebean> bitmapArrayList_uploading = new ArrayList<>();
    BaseTextview baseTextview_left_side = null;
    BaseEdittext edittext_viewcommunityinfo_apartmentname;
    CommonSession commonSession = null;
    ACViewCommunityInfobean acviewCommunityInfobean = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    private Intent pictureActionIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(FullProfileMainActivity.this, getApplicationContext(), getIntent().getExtras(), FullProfileMainActivity.class.getClass());
            commonSession = new CommonSession(FullProfileMainActivity.this);
            if (modifiy_format_index == ModificationOptions.VIEW.getOperationIndex()) {
                isComeForeditinfo = false;
            } else {
                isComeForeditinfo = true;
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

            if (isComeForeditinfo) {
                baseTextview_header_title.setText(getResources().getString(R.string.edit_info));
                baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);
                baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);
                imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
                imageView_edit.setVisibility(View.VISIBLE);
                imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
                imageview_right_foursquare.setVisibility(View.VISIBLE);


            } else {
                baseTextview_header_title.setText(getResources().getString(R.string.view_info));
                baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);
                baseTextview_left_side.setText(getResources().getString(R.string.cancel));
                baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);


            }

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageView_delete = (ImageView) toolbar_header.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.GONE);
            imageView_view = (ImageView) toolbar_header.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.GONE);
            imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.GONE);
            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.GONE);


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
            view_home = getLayoutInflater().inflate(R.layout.full_profile_view, null);

            imageview_viewcommunityinfo_profilepic = (ImageView) view_home.findViewById(R.id.imageview_viewcommunityinfo_profilepic);
            edittext_viewcommunityinfo_email = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_email);
            edittext_viewcommunityinfo_flatno = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_flatno);
            edittext_viewcommunityinfo_flattype = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_flattype);
            edittext_viewcommunityinfo_contactno = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_phone);
            edittext_viewcommunityinfo_address = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_address);
            edittext_viewcommunityinfo_yearbuilt = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_yearbuilt);
            edittext_viewcommunityinfo_fax = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_fax);
            edittext_viewcommunityinfo_description = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_description);
            edittext_viewcommunityinfo_apartmentname = (BaseEdittext) view_home.findViewById(R.id.edittext_viewcommunityinfo_apartmentname);
            button_editinfo_save = (MyButtonView) view_home.findViewById(R.id.mybuttonview_editinfo_save);

            imageView_edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    Intent launchActivity1 = new Intent(FullProfileMainActivity.this, FullProfileMainActivity.class);
                    startActivity(launchActivity1);

                }
            });
            if (isComeForeditinfo) {
                button_editinfo_save.setVisibility(View.VISIBLE);

            } else {
                button_editinfo_save.setVisibility(View.GONE);

            }

            button_editinfo_save.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {


                    try {

                        requestParametersbean.setUserId(commonSession.getLoggedUserID());
                        if (bitmapArrayList_uploading.size() == 0) {

                        } else {
                            int size = bitmapArrayList_uploading.size() - 1;
                            requestParametersbean.setImgCount(String.valueOf(size));

                        }


                        //saveInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
            if (acviewCommunityInfobean != null) {
                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_apartmentname())) {
                    edittext_viewcommunityinfo_apartmentname.setText(acviewCommunityInfobean.getViewcommunity_apartmentname());
                }
                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_email())) {

                    edittext_viewcommunityinfo_email.setText(acviewCommunityInfobean.getViewcommunity_email());


                }
                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_homeno())) {

                    edittext_viewcommunityinfo_flatno.setText(acviewCommunityInfobean.getViewcommunity_homeno());


                }
                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_type())) {

                    edittext_viewcommunityinfo_flattype.setText(acviewCommunityInfobean.getViewcommunity_type());


                }
                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_address())) {

                    edittext_viewcommunityinfo_address.setText(acviewCommunityInfobean.getViewcommunity_address());


                }
                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_fax())) {

                    edittext_viewcommunityinfo_fax.setText(acviewCommunityInfobean.getViewcommunity_fax());


                }

                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_description())) {

                    edittext_viewcommunityinfo_description.setText(acviewCommunityInfobean.getViewcommunity_description());


                }
                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_phone())) {

                    edittext_viewcommunityinfo_contactno.setText(acviewCommunityInfobean.getViewcommunity_phone());


                }
                if (CommonMethods.isTextAvailable(acviewCommunityInfobean.getViewcommunity_yearbuilt())) {

                    edittext_viewcommunityinfo_yearbuilt.setText(acviewCommunityInfobean.getViewcommunity_yearbuilt());


                }

            }
        }
    }

    // this method is used for login user
    public void getACViewCommunityInfo() {
        try {


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.USER_FULL_PROFILE_VIEW.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, FullProfileMainActivity.this, getApplicationContext(), URLs.GET_FULL_PROFILE, jsonObjectGetPostParameterEachScreen, ScreensEnums.USER_FULL_PROFILE_VIEW.getScrenIndex(), FullProfileMainActivity.class.getClass());

            passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);

            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

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
                        CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_done), FullProfileMainActivity.this);

                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), FullProfileMainActivity.this);

                    }
                    ACViewCommunityInfobean communityinfobean = new ACViewCommunityInfobean();


                    JSONObject json_single_community = jsonObject_main.getJSONObject(JSONCommonKeywords.Email);


                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.Reminderscount))

                    {
                        communityinfobean.setViewcommunity_homeno(json_single_community.getString(JSONCommonKeywords.Reminderscount));
                    }


                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.Address))

                    {
                        communityinfobean.setViewcommunity_address(json_single_community.getString(JSONCommonKeywords.Address));
                    }

                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.Type))

                    {
                        communityinfobean.setViewcommunity_type(json_single_community.getString(JSONCommonKeywords.Type));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.Phone))

                    {
                        communityinfobean.setViewcommunity_phone(json_single_community.getString(JSONCommonKeywords.Phone));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.Fax))

                    {
                        communityinfobean.setViewcommunity_fax(json_single_community.getString(JSONCommonKeywords.Fax));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.Description))

                    {
                        communityinfobean.setViewcommunity_description(json_single_community.getString(JSONCommonKeywords.Description));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.YearsBuilt))

                    {
                        communityinfobean.setViewcommunity_description(json_single_community.getString(JSONCommonKeywords.YearsBuilt));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.Appartment))

                    {
                        communityinfobean.setViewcommunity_description(json_single_community.getString(JSONCommonKeywords.Appartment));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_community, JSONCommonKeywords.Image))

                    {
                        ArrayList<String> images_of_classified = new ArrayList<>();
                        if (CommonMethods.checkJSONArrayHasData(json_single_community, JSONCommonKeywords.Image)) {
                            JSONArray jsonArray_images = json_single_community.getJSONArray(JSONCommonKeywords.Image);
                            for (int j = 0; j < jsonArray_images.length(); j++) {

                                if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                                    images_of_classified.add(jsonArray_images.getString(j));
                                }
                            }

                            communityinfobean.setStringArrayList_images(images_of_classified);

                        }


                        if ((FullProfileMainActivity.communitybeanArrayList != null)) {
                            FullProfileMainActivity.communitybeanArrayList.add(0, communityinfobean);

                            FullProfileMainActivity.mRecyclerView.smoothScrollToPosition(0);
                            finish();

                        }


                    } else {

                    }


                } else {
                    if (jsonObject_main.has(JSONCommonKeywords.message)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                    }


                    if (responcebean.getErrorMessage() == null) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_failed), FullProfileMainActivity.this);
                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), FullProfileMainActivity.this);

                    }

                }


            } catch (Exception e) {
                CommonMethods.customToastMessage(e.getMessage(), FullProfileMainActivity.this);

            }

        } else {
            if (responcebean.getErrorMessage() == null) {
                CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_failed), FullProfileMainActivity.this);
            } else {
                CommonMethods.customToastMessage(responcebean.getErrorMessage(), FullProfileMainActivity.this);

            }
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


                        imageview_viewcommunityinfo_profilepic.setVisibility(View.GONE);
                        circularImageView_profile_pic.setVisibility(View.VISIBLE);

                        if (bitmap != null) {
                            circularImageView_profile_pic.setImageBitmap(bitmap);

                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), FullProfileMainActivity.this);
                }
            } else if (resultCode == RESULT_CANCELED) {
                CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), FullProfileMainActivity.this);

            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {

                try {

                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");


                    imageview_viewcommunityinfo_profilepic.setVisibility(View.GONE);
                    circularImageView_profile_pic.setVisibility(View.VISIBLE);
                    if (bitmap != null) {
                        circularImageView_profile_pic.setImageBitmap(bitmap);

                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), FullProfileMainActivity.this);

            }
        } else {


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
