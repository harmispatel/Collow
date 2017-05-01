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
import com.app.collow.beans.Classifiedbean;
import com.app.collow.beans.Imagebean;
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

public class CreateClassifiedActivity extends BaseActivity implements SetupViewInterface {
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    public static BaseTextview baseTextview_property_type = null, baseTextview_property_category = null;
    BaseEdittext edittext_createclassification_title = null;
    BaseEdittext edittext_createclassification_description = null;
    BaseEdittext edittext_createclassification_price = null;
    BaseTextview baseTextview_left_side = null;
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageView_plus_icon_only;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    ArrayList<Imagebean> bitmapArrayList_uploading = new ArrayList<>();
    RecyclerView recyclerView_grid_classfied_images = null;
    UploadImageAdapter uploadImageAdapter = null;
    RelativeLayout relativeLayout_upload_image_plus_view = null;
    ProgressBar progressBar_property_type = null, progressBar_property_category = null;
    RelativeLayout relativeLayout_property_type = null, relativeLayout_property_category = null;
    HashMap<String, String> stringHashMap_property_types = new HashMap<>();
    HashMap<String, String> stringHashMap_property_categories = new HashMap<>();
    MyButtonView myButtonView_create_classfied = null;
    String communityID = null;
    private Intent pictureActionIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retryParameterbean = new RetryParameterbean(CreateClassifiedActivity.this, getApplicationContext(), getIntent().getExtras(), CreateClassifiedActivity.class.getClass());
        commonSession = new CommonSession(CreateClassifiedActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
        }
        setupHeaderView();
        findViewByIDs();
        setupEvents();

    }


    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.new_lsiting));
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
            view_home = getLayoutInflater().inflate(R.layout.classfied_create_new, null);

            edittext_createclassification_title = (BaseEdittext) view_home.findViewById(R.id.edittext_createclassification_title);
            edittext_createclassification_price = (BaseEdittext) view_home.findViewById(R.id.edittext_createclassification_price);
            edittext_createclassification_description = (BaseEdittext) view_home.findViewById(R.id.edittext_createclassification_description);


            imageView_plus_icon_only = (ImageView) view_home.findViewById(R.id.imageview_upload_image_plus_icon);
            relativeLayout_upload_image_plus_view = (RelativeLayout) view_home.findViewById(R.id.relativelayout_upload_create_classfied_images);
            frameLayout_container.addView(view_home);


            progressBar_property_type = (ProgressBar) view_home.findViewById(R.id.progress_create_classfied_property_type);
            baseTextview_property_type = (BaseTextview) view_home.findViewById(R.id.communtitytextview_create_classfied_property_type);
            relativeLayout_property_type = (RelativeLayout) view_home.findViewById(R.id.relativelayout_select_create_classfied_property_type);


            progressBar_property_category = (ProgressBar) view_home.findViewById(R.id.progress_create_classfied_property_category);
            baseTextview_property_category = (BaseTextview) view_home.findViewById(R.id.communtitytextview_create_classfied_property_categoty);
            relativeLayout_property_category = (RelativeLayout) view_home.findViewById(R.id.relativelayout_select_create_classfied_property_category);

            myButtonView_create_classfied = (MyButtonView) view_home.findViewById(R.id.mybuttonview_create_classfied);
            //  getNewsListing(0);


            recyclerView_grid_classfied_images = (RecyclerView) view_home.findViewById(R.id.recycler_view_grid_create_classfied);
            recyclerView_grid_classfied_images.setHasFixedSize(true);
            // Disabled nested scrolling since Parent scrollview will scroll the content.
            recyclerView_grid_classfied_images.setNestedScrollingEnabled(false);
            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
            recyclerView_grid_classfied_images.setLayoutManager(gaggeredGridLayoutManager);


            int spanCount = 3; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = false;
            recyclerView_grid_classfied_images.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


            uploadImageAdapter = new UploadImageAdapter(this);
            recyclerView_grid_classfied_images.setAdapter(uploadImageAdapter);
            relativeLayout_upload_image_plus_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDialog();
                }
            });
            imageView_plus_icon_only.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDialog();
                }
            });


            getPropertyTypes();
            getPropertyCategories();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    private void setupEvents() {
        try {
            relativeLayout_property_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stringHashMap_property_types.size() == 0) {
                        getPropertyTypes();
                    } else {


                        Intent intent = new Intent(CreateClassifiedActivity.this, SelectPropertyTypeOrCategoryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(BundleCommonKeywords.KEY_CLASSIFIED_PROPERTY_TYPE_OR_CATEGORY, ScreensEnums.PROPERTY_TYPE_GET.getScrenIndex());
                        bundle.putSerializable(BundleCommonKeywords.KEY_CLASSIFIED_PROPERTY_TYPE_OR_CATEGORY_DATA, stringHashMap_property_types);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }


                }
            });
            relativeLayout_property_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stringHashMap_property_categories.size() == 0) {
                        getPropertyCategories();

                    } else {
                        Intent intent = new Intent(CreateClassifiedActivity.this, SelectPropertyTypeOrCategoryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(BundleCommonKeywords.KEY_CLASSIFIED_PROPERTY_TYPE_OR_CATEGORY, ScreensEnums.PROPERTY_CATEGORY_GET.getScrenIndex());
                        bundle.putSerializable(BundleCommonKeywords.KEY_CLASSIFIED_PROPERTY_TYPE_OR_CATEGORY_DATA, stringHashMap_property_categories);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }


                }
            });
            myButtonView_create_classfied.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (TextUtils.isEmpty(edittext_createclassification_title.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_title), CreateClassifiedActivity.this);
                        } else if (!CommonMethods.isMorethenThreeCharacters(edittext_createclassification_title.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), CreateClassifiedActivity.this);

                        } else if (TextUtils.isEmpty(edittext_createclassification_description.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.description), CreateClassifiedActivity.this);
                        } else if (!CommonMethods.isMorethenThreeCharacters(edittext_createclassification_description.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), CreateClassifiedActivity.this);

                        } else if (TextUtils.isEmpty(edittext_createclassification_price.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_price), CreateClassifiedActivity.this);
                        } else if (baseTextview_property_type.getText().toString().equals(getResources().getString(R.string.select_propert_type))) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.select_propert_type), CreateClassifiedActivity.this);

                        } else if (baseTextview_property_type.getText().toString().equals(getResources().getString(R.string.select_category))) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.select_category), CreateClassifiedActivity.this);

                        } else if (bitmapArrayList_uploading.size() == 0) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.upload_images_at_least_one), CreateClassifiedActivity.this);

                        } else {

                            requestParametersbean.setTitle(edittext_createclassification_title.getText().toString());
                            requestParametersbean.setDescripton(edittext_createclassification_description.getText().toString());
                            requestParametersbean.setPrice(edittext_createclassification_price.getText().toString());
                            requestParametersbean.setType(baseTextview_property_type.getTag().toString());
                            requestParametersbean.setCategory(baseTextview_property_category.getTag().toString());
                            requestParametersbean.setCommunityID(communityID);
                            requestParametersbean.setUserId(commonSession.getLoggedUserID());
                            requestParametersbean.setImgCount(String.valueOf(bitmapArrayList_uploading.size() - 1));
                            requestParametersbean.setBitmapArrayList_mutliple_image(bitmapArrayList_uploading);


                            ///make request here
                            createClassiFied();
                        }
                    } catch (Exception e) {
                        new BaseException(e, false, retryParameterbean);
                    }

                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
    }


    public void createClassiFied() {

        try {
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.CREATE_CLASSIFIED.getScrenIndex(), requestParametersbean);

            PassParameterbean passParameterbean = new PassParameterbean(this, CreateClassifiedActivity.this, getApplicationContext(), URLs.CREATECLASSIFIED, jsonObjectGetPostParameterEachScreen, ScreensEnums.CREATE_CLASSIFIED.getScrenIndex(), SignInActivity.class.getClass());
            passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());
            passParameterbean.setBitmapArrayList_mutliple_image(bitmapArrayList_uploading);
            passParameterbean.setNoNeedToDisplayLoadingDialog(false);
            passParameterbean.setForImageUploadingCustomKeyword(false);
            passParameterbean.setForImageUploadingCustomKeywordName(JSONCommonKeywords.PropertyImage);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }


    public void getPropertyTypes() {

        try {

            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {

                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.typeList)) {
                                    JSONArray jsonArray_typelisting = jsonObject_main.getJSONArray(JSONCommonKeywords.typeList);
                                    for (int i = 0; i < jsonArray_typelisting.length(); i++) {

                                        JSONObject jsonObject_single_type = jsonArray_typelisting.getJSONObject(i);

                                        PropertyTypesbean propertyTypesbean = new PropertyTypesbean();

                                        if (jsonObject_single_type.has(JSONCommonKeywords.typeCode)) {
                                            propertyTypesbean.setTypesCode(jsonObject_single_type.getString(JSONCommonKeywords.typeCode));

                                        }

                                        if (jsonObject_single_type.has(JSONCommonKeywords.StatetypeName)) {
                                            propertyTypesbean.setTypeName(jsonObject_single_type.getString(JSONCommonKeywords.StatetypeName));

                                        }

                                        stringHashMap_property_types.put(propertyTypesbean.getTypesCode(), propertyTypesbean.getTypeName());


                                    }
                                }


                            } else {

                            }


                            progressBar_property_type.setVisibility(View.GONE);


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar_property_type.setVisibility(View.GONE);
                            CommonMethods.customToastMessage(e.getMessage(), CreateClassifiedActivity.this);

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GETPROPERTYTYPES, null, ScreensEnums.PROPERTY_TYPE_GET.getScrenIndex(), SignInActivity.class.getClass());

            progressBar_property_type.setVisibility(View.VISIBLE);
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);

            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }

    public void getPropertyCategories() {
        try {
            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {

                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.categoryList)) {
                                    JSONArray jsonArray_typelisting = jsonObject_main.getJSONArray(JSONCommonKeywords.categoryList);
                                    for (int i = 0; i < jsonArray_typelisting.length(); i++) {

                                        JSONObject jsonObject_single_category = jsonArray_typelisting.getJSONObject(i);

                                        PropertyCategoriesbean propertyCategoriesbean = new PropertyCategoriesbean();

                                        if (jsonObject_single_category.has(JSONCommonKeywords.typeCode)) {
                                            propertyCategoriesbean.setTypeCode(jsonObject_single_category.getString(JSONCommonKeywords.typeCode));

                                        }

                                        if (jsonObject_single_category.has(JSONCommonKeywords.categoryName)) {
                                            propertyCategoriesbean.setCategoryName(jsonObject_single_category.getString(JSONCommonKeywords.categoryName));

                                        }

                                        stringHashMap_property_categories.put(propertyCategoriesbean.getTypeCode(), propertyCategoriesbean.getCategoryName());


                                    }
                                }


                            } else {

                            }


                            progressBar_property_category.setVisibility(View.GONE);


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar_property_category.setVisibility(View.GONE);
                            CommonMethods.customToastMessage(e.getMessage(), CreateClassifiedActivity.this);

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GETPROPERTYCATEGORY, null, ScreensEnums.PROPERTY_CATEGORY_GET.getScrenIndex(), SignInActivity.class.getClass());

            progressBar_property_category.setVisibility(View.VISIBLE);
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);

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

                                recyclerView_grid_classfied_images.setVisibility(View.VISIBLE);
                                relativeLayout_upload_image_plus_view.setVisibility(View.GONE);


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
                        CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateClassifiedActivity.this);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateClassifiedActivity.this);

                }
            } else if (requestCode == CAMERA_REQUEST) {
                if (resultCode == RESULT_OK) {

                    try {

                        Bundle extras = data.getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");


                        if (bitmap != null) {

                            recyclerView_grid_classfied_images.setVisibility(View.VISIBLE);
                            relativeLayout_upload_image_plus_view.setVisibility(View.GONE);


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
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateClassifiedActivity.this);

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
                        CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_done), CreateClassifiedActivity.this);

                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateClassifiedActivity.this);

                    }
                    Classifiedbean classifiedbean = new Classifiedbean();


                    JSONObject json_single_classified = jsonObject_main.getJSONObject(JSONCommonKeywords.Propertydetail);


                    if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.id))

                    {
                        classifiedbean.setClassifiedID(json_single_classified.getString(JSONCommonKeywords.id));
                    }


                    if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.PropertyTitle))

                    {
                        classifiedbean.setClassified_name(json_single_classified.getString(JSONCommonKeywords.PropertyTitle));
                    }


                    if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.Description))

                    {
                        classifiedbean.setClassifiedDescription(json_single_classified.getString(JSONCommonKeywords.Description));
                    }

                    if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.Price))

                    {
                        classifiedbean.setClssified_price(json_single_classified.getString(JSONCommonKeywords.Price));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.Status))

                    {
                        classifiedbean.setStatus(json_single_classified.getString(JSONCommonKeywords.Status));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.PostedDate))

                    {
                        classifiedbean.setClassified_date(json_single_classified.getString(JSONCommonKeywords.PostedDate));
                    }
                    if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.OwnerId))

                    {
                        classifiedbean.setOwnerId(json_single_classified.getString(JSONCommonKeywords.OwnerId));
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

                            classifiedbean.setStringArrayList_images(images_of_classified);

                        }


                        if ((ClassifiedMainActivity.classifiedbeanArrayList != null)) {
                            ClassifiedMainActivity.classifiedbeanArrayList.add(0, classifiedbean);
                            ClassifiedMainActivity.classifiedAdapter.notifyItemInserted(0);
                            ClassifiedMainActivity.mRecyclerView.smoothScrollToPosition(0);
                            ClassifiedMainActivity.mRecyclerView.setVisibility(View.VISIBLE);
                            ClassifiedMainActivity.baseTextview_error.setVisibility(View.GONE);

                            finish();

                        }


                    } else {

                    }


                } else {
                    if (jsonObject_main.has(JSONCommonKeywords.message)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                    }


                    if (responcebean.getErrorMessage() == null) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_failed), CreateClassifiedActivity.this);
                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateClassifiedActivity.this);

                    }

                }


            } catch (Exception e) {
                CommonMethods.customToastMessage(e.getMessage(), CreateClassifiedActivity.this);

            }

        } else {
            if (responcebean.getErrorMessage() == null) {
                CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_failed), CreateClassifiedActivity.this);
            } else {
                CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateClassifiedActivity.this);

            }
        }


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
                            recyclerView_grid_classfied_images.setVisibility(View.GONE);
                            relativeLayout_upload_image_plus_view.setVisibility(View.VISIBLE);
                        }
                        uploadImageAdapter = new UploadImageAdapter(CreateClassifiedActivity.this);
                        recyclerView_grid_classfied_images.setAdapter(uploadImageAdapter);

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
                CommonMethods.customToastMessage(getResources().getString(R.string.image_upload_limit_corss_message), CreateClassifiedActivity.this);
            } else {
                Imagebean imagebean = (Imagebean) view.getTag();
                if (imagebean.isPlusIconNeedToEnable()) {
                    startDialog();
                }
            }

        }
    }


}
