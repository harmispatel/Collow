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
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.FormsAndDocsbean;
import com.app.collow.beans.Imagebean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.PropertyCategoriesbean;
import com.app.collow.beans.PropertyTypesbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.GridSpacingItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
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
import java.util.HashMap;

/**
 * Created by Harmis on 08/02/17.
 */


/**
 * Created by Harmis on 07/02/17.
 */

public class CreateFormAndDocumentActivity extends BaseActivity {
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    BaseEdittext edittext_createclassification_title = null;
    BaseEdittext edittext_createclassification_description = null;
    BaseTextview baseTextview_left_side = null;
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    ArrayList<Imagebean> bitmapArrayList_uploading = new ArrayList<>();
    UploadImageAdapter uploadImageAdapter = null;
    String communityID = null;
    private Intent pictureActionIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(CreateFormAndDocumentActivity.this, getApplicationContext(), getIntent().getExtras(), CreateFormAndDocumentActivity.class.getClass());
            commonSession = new CommonSession(CreateFormAndDocumentActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
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
            baseTextview_header_title.setText(getResources().getString(R.string.create_news_annnoucement));
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            baseTextview_left_side.setText(getResources().getString(R.string.cancel));
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });

            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
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

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.formanddocx_create_new, null);

            edittext_createclassification_title = (BaseEdittext) view_home.findViewById(R.id.edittext_createclassification_title);
            edittext_createclassification_description = (BaseEdittext) view_home.findViewById(R.id.edittext_createclassification_description);


              frameLayout_container.addView(view_home);











        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    private void setupEvents() {
        try {

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
    }


    public void createNews() {

        try {
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.CREATE_CLASSIFIED.getScrenIndex(), requestParametersbean);

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
                                    CommonMethods.customToastMessage(getResources().getString(R.string.create_formsanddocuments_done), CreateFormAndDocumentActivity.this);

                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateFormAndDocumentActivity.this);

                                }
                                FormsAndDocsbean formsAndDocsbean = new FormsAndDocsbean();


                                JSONObject json_single_classified = jsonObject_main.getJSONObject(JSONCommonKeywords.Propertydetail);


                                if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.id))

                                {
                                }


                                if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.Image))

                                {
                                    ArrayList<String> images_of_classified = new ArrayList<>();
                                    if (CommonMethods.checkJSONArrayHasData(json_single_classified, JSONCommonKeywords.Image)) {
                                        JSONArray jsonArray_images = json_single_classified.getJSONArray(JSONCommonKeywords.Image);
                                        for (int j = 0; j < jsonArray_images.length(); j++) {

                                            if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                                                images_of_classified.add(jsonArray_images.getString(j));
                                            }
                                        }


                                    }


                                    if ((FormsAndDocsMainActivity.formsanddocsbeanlist != null)) {
                                        FormsAndDocsMainActivity.formsanddocsbeanlist.add(formsAndDocsbean);
                                        if(FormsAndDocsMainActivity.formsanddocsAdapter!=null)
                                        {
                                            FormsAndDocsMainActivity.formsanddocsAdapter.notifyItemInserted(0);
                                            if(FormsAndDocsMainActivity.mRecyclerView!=null)
                                            {
                                                FormsAndDocsMainActivity.mRecyclerView.smoothScrollToPosition(0);

                                            }

                                        }

                                        finish();

                                    }


                                } else {

                                }


                            } else {
                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {
                                    CommonMethods.customToastMessage(getResources().getString(R.string.create_formsanddocuments_failed), CreateFormAndDocumentActivity.this);
                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateFormAndDocumentActivity.this);

                                }

                            }


                        } catch (Exception e) {
                            CommonMethods.customToastMessage(e.getMessage(), CreateFormAndDocumentActivity.this);

                        }

                    } else {
                        if (responcebean.getErrorMessage() == null) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.create_formsanddocuments_failed), CreateFormAndDocumentActivity.this);
                        } else {
                            CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateFormAndDocumentActivity.this);

                        }
                    }


                }
            }, null, getApplicationContext(), URLs.CREATE_FORM_AND_DOCX, jsonObjectGetPostParameterEachScreen, ScreensEnums.CREATE_FORM_AND_DOCUMENTS.getScrenIndex(), SignInActivity.class.getClass());

            passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());
            passParameterbean.setBitmapArrayList_mutliple_image(bitmapArrayList_uploading);
            passParameterbean.setNoNeedToDisplayLoadingDialog(false);
            new RequestToServer(passParameterbean, null).execute();
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
                        CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateFormAndDocumentActivity.this);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateFormAndDocumentActivity.this);

                }
            } else if (requestCode == CAMERA_REQUEST) {
                if (resultCode == RESULT_OK) {

                    try {

                        Bundle extras = data.getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");


                        if (bitmap != null) {




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
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateFormAndDocumentActivity.this);

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
        public void onBindViewHolder(UploadImageViewHolder holder, int position) {


            try {
                final Imagebean imagebean = bitmapArrayList_uploading.get(position);

                holder.imageview_delete_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Imagebean imagebean1 = (Imagebean) v.getTag();
                        bitmapArrayList_uploading.remove(imagebean1.getBitmap());
                        if (bitmapArrayList_uploading.size() == 0) {

                        }
                        uploadImageAdapter = new UploadImageAdapter(CreateFormAndDocumentActivity.this);

                    }
                });

                if (imagebean.isPlusIconNeedToEnable()) {

                    holder.view_main.setTag(imagebean);
                    holder.imageview_delete_icon.setVisibility(View.GONE);
                    holder.imageview_singe_only.setImageResource(R.drawable.plus_icon);

                } else {
                    holder.imageview_singe_only.setImageBitmap(imagebean.getBitmap());

                }
                holder.imageview_delete_icon.setTag(imagebean);


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

        public UploadImageViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            view_main = itemView;
            imageview_singe_only = (ImageView) itemView.findViewById(R.id.imageview_single_only);
            imageview_delete_icon = (ImageView) itemView.findViewById(R.id.close_dialog);
        }

        @Override
        public void onClick(View view) {
            if (bitmapArrayList_uploading.size() >= 12) {
                CommonMethods.customToastMessage(getResources().getString(R.string.image_upload_limit_corss_message_all_files), CreateFormAndDocumentActivity.this);
            } else {
                Imagebean imagebean = (Imagebean) view.getTag();
                if (imagebean.isPlusIconNeedToEnable()) {
                    startDialog();
                }
            }

        }
    }


}
