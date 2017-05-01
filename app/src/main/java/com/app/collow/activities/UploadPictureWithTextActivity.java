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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Imagebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Harmis on 13/02/17.
 */

public class UploadPictureWithTextActivity extends BaseActivity {
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    public static UploadPictureWithTextActivity uploadPictureWithTextActivity = null;
    protected Handler handler;
    View view_home = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    CommunityAccessbean communityAccessbean = null;
    String communityID = null;
    BaseEdittext baseEdittext_post_message;
    MyButtonView myButtonView_send_message = null;
    ImageView imageView_upload_plus = null;
    String activityID, feedCategoryType = null;
    ArrayList<Imagebean> imagebeanArrayList = new ArrayList<>();
    private Intent pictureActionIntent = null;
    BaseTextview baseTextview_left_side = null;
    ImageView imageView_comment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        try {
            uploadPictureWithTextActivity = this;
            retryParameterbean = new RetryParameterbean(UploadPictureWithTextActivity.this, getApplicationContext(), getIntent().getExtras(), UploadPictureWithTextActivity.class.getClass());
            commonSession = new CommonSession(UploadPictureWithTextActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                activityID = bundle.getString(BundleCommonKeywords.KEY_ACTIVITY_ID);
                feedCategoryType = bundle.getString(BundleCommonKeywords.KEY_FEED_CATEGORY);
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
            baseTextview_header_title.setText(getResources().getString(R.string.upload_small));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);
            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    MyUtils.openCommunityMenu(UploadPictureWithTextActivity.this, communityID, baseTextview_header_title.getText().toString(), communityAccessbean);


                }
            });

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.upload_picure_with_text, null);
            baseEdittext_post_message = (BaseEdittext) view_home.findViewById(R.id.edittext_message_text);
            myButtonView_send_message = (MyButtonView) view_home.findViewById(R.id.mybuttonview_post_comment_with_image);

            imageView_upload_plus = (ImageView) view_home.findViewById(R.id.imageview_comment_plus_icon);
            imageView_comment= (ImageView) view_home.findViewById(R.id.imageview_comment);
            imageView_upload_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagebeanArrayList.clear();
                    startDialog();
                }
            });
            imageView_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagebeanArrayList.clear();
                    startDialog();
                }
            });
            frameLayout_container.addView(view_home);


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void setupEvents() {
        try {
            myButtonView_send_message.setOnClickListener(new MyOnClickListener(UploadPictureWithTextActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {

                        boolean isProceedText=true,isProceedImage=true;
                        if(imagebeanArrayList.size()==0)
                        {
                            isProceedImage=false;
                        }

                        if(TextUtils.isEmpty(baseEdittext_post_message.getText().toString()))
                        {
                            isProceedText=false;

                        }

                        if(isProceedText||isProceedImage)
                        {
                            sendMessage();

                        }
                        else
                        {
                            CommonMethods.customToastMessage(getResources().getString(R.string.upload_iamge_or_write_text),UploadPictureWithTextActivity.this);
                        }


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
    }


    // this method is used for login user
    public void sendMessage() {
        try {

            requestParametersbean.setPostText(baseEdittext_post_message.getText().toString());
            requestParametersbean.setType(feedCategoryType);
            requestParametersbean.setActivityId(activityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.SEND_COMMENT.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(CommentListingActivity.setupViewInterface, UploadPictureWithTextActivity.this, getApplicationContext(), URLs.COMMENT_ACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.SEND_COMMENT.getScrenIndex(), UploadPictureWithTextActivity.class.getClass());

            passParameterbean.setForImageUploadingCustomKeyword(false);
            passParameterbean.setForImageUploadingCustomKeywordName(JSONCommonKeywords.CommentAttach);
            passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());
            passParameterbean.setBitmapArrayList_mutliple_image(imagebeanArrayList);
            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
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


                                Imagebean imagebean = new Imagebean();
                                imagebean.setBitmap(bitmap);
                                imagebeanArrayList.add(imagebean);

                                imageView_upload_plus.setVisibility(View.GONE);
                                imageView_comment.setVisibility(View.VISIBLE);
                                imageView_comment.setImageBitmap(bitmap);


                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else {
                        CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), UploadPictureWithTextActivity.this);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), UploadPictureWithTextActivity.this);

                }
            } else if (requestCode == CAMERA_REQUEST) {
                if (resultCode == RESULT_OK) {

                    try {

                        Bundle extras = data.getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");


                        if (bitmap != null) {

                            Imagebean imagebean = new Imagebean();
                            imagebean.setBitmap(bitmap);
                            imagebeanArrayList.add(imagebean);
                            imageView_upload_plus.setVisibility(View.GONE);
                            imageView_comment.setVisibility(View.VISIBLE);
                            imageView_comment.setImageBitmap(bitmap);
                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), UploadPictureWithTextActivity.this);

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
}
