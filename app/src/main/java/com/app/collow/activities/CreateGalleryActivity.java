package com.app.collow.activities;

/**
 * Created by Harmis on 21/02/17.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.Gallerybean;
import com.app.collow.beans.Imagebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beansgenerate.GallerybeanBuild;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.SpacesItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class CreateGalleryActivity extends BaseActivity implements SetupViewInterface {
    // class variables
    private static final int REQUEST_CODE = 123;
    BaseEdittext edittext_create_gallery_title = null;
    BaseEdittext edittext_create_gallery_albumtitle = null;
    BaseTextview baseTextview_left_side = null;
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageView_plus_icon_only;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    ArrayList<Imagebean> bitmapArrayList_uploading = new ArrayList<>();
    RecyclerView recyclerView_grid_gallery_images = null;
    UploadImageAdapter uploadImageAdapter = null;
    RelativeLayout relativeLayout_upload_image_plus_view = null;
    MyButtonView myButtonView_create_gallery = null;
    String communityID = null;
    private ArrayList<String> mResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retryParameterbean = new RetryParameterbean(CreateGalleryActivity.this, getApplicationContext(), getIntent().getExtras(), CreateGalleryActivity.class.getClass());
        commonSession = new CommonSession(CreateGalleryActivity.this);
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
            baseTextview_header_title.setText(getResources().getString(R.string.create_gallery));
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
            view_home = getLayoutInflater().inflate(R.layout.gallery_create_new, null);

            edittext_create_gallery_title = (BaseEdittext) view_home.findViewById(R.id.edittext_create_gallery_title);
            edittext_create_gallery_albumtitle = (BaseEdittext) view_home.findViewById(R.id.edittext_create_album_title);


            imageView_plus_icon_only = (ImageView) view_home.findViewById(R.id.imageview_upload_image_plus_icon_gallery);
            relativeLayout_upload_image_plus_view = (RelativeLayout) view_home.findViewById(R.id.relativelayout_upload_create_images_gallery);
            frameLayout_container.addView(view_home);


            myButtonView_create_gallery = (MyButtonView) view_home.findViewById(R.id.mybuttonview_create_gallery);
            //  getNewsListing(0);


            recyclerView_grid_gallery_images = (RecyclerView) view_home.findViewById(R.id.recycler_view_grid_create_gallery);
            recyclerView_grid_gallery_images.setHasFixedSize(true);
            // Disabled nested scrolling since Parent scrollview will scroll the content.
            recyclerView_grid_gallery_images.setNestedScrollingEnabled(false);
            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
            recyclerView_grid_gallery_images.setLayoutManager(gaggeredGridLayoutManager);


            int spanCount = 3; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = false;
            //recyclerView_grid_gallery_images.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            recyclerView_grid_gallery_images.addItemDecoration(new SpacesItemDecoration(3));


            uploadImageAdapter = new UploadImageAdapter();
            recyclerView_grid_gallery_images.setAdapter(uploadImageAdapter);
            relativeLayout_upload_image_plus_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start multiple photos selector
                    Intent intent = new Intent(CreateGalleryActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 12);
// min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
            imageView_plus_icon_only.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start multiple photos selector
                    Intent intent = new Intent(CreateGalleryActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 12);
// min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    private void setupEvents() {
        try {

            myButtonView_create_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (TextUtils.isEmpty(edittext_create_gallery_title.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_title), CreateGalleryActivity.this);
                        } else {

                            requestParametersbean.setTitle(edittext_create_gallery_title.getText().toString());
                            requestParametersbean.setCommunityID(communityID);
                            requestParametersbean.setUserId(commonSession.getLoggedUserID());
                            if (bitmapArrayList_uploading.size() == 0) {
                                requestParametersbean.setImgCount(String.valueOf(0));
                                CommonMethods.customToastMessage(getResources().getString(R.string.selectphoto), CreateGalleryActivity.this);
                                return;

                            } else {
                                requestParametersbean.setImgCount(String.valueOf(bitmapArrayList_uploading.size() - 1));

                            }
                            requestParametersbean.setBitmapArrayList_mutliple_image(bitmapArrayList_uploading);

                            if(!TextUtils.isEmpty(edittext_create_gallery_albumtitle.getText().toString()))
                            {
                                requestParametersbean.setAlbumName(edittext_create_gallery_albumtitle.getText().toString());
                            }


                            ///make request here
                            createGallery();
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


    public void createGallery() {

        try {
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex
                    (ScreensEnums.CREATE_GALLERY.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CreateGalleryActivity.this, getApplicationContext(),
                    URLs.CREATEGALLERY, jsonObjectGetPostParameterEachScreen, ScreensEnums.CREATE_GALLERY.getScrenIndex(), SignInActivity.class.getClass());
            passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());
            passParameterbean.setBitmapArrayList_mutliple_image(bitmapArrayList_uploading);
            passParameterbean.setForImageUploadingCustomKeywordName(JSONCommonKeywords.GalleryImage);
            passParameterbean.setNoNeedToDisplayLoadingDialog(false);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;


                if(mResults.size()==0)
                {
                    bitmapArrayList_uploading.clear();
                    if (uploadImageAdapter != null) {
                        uploadImageAdapter.notifyDataSetChanged();
                    }
                    recyclerView_grid_gallery_images.setVisibility(View.GONE);
                    relativeLayout_upload_image_plus_view.setVisibility(View.VISIBLE);
                }
                else
                {
                    if(bitmapArrayList_uploading.size()==0)
                    {
                        for (String result : mResults) {


                            if (mResults.size() >= 2) {
                                edittext_create_gallery_albumtitle.setVisibility(View.VISIBLE);
                            }

                            Bitmap bitmap = decodeAndResizeFile(new File(
                                    result));

                            if (bitmap != null) {

                                recyclerView_grid_gallery_images.setVisibility(View.VISIBLE);
                                relativeLayout_upload_image_plus_view.setVisibility(View.GONE);


                                Imagebean imagebean = new Imagebean();
                                imagebean.setBitmap(bitmap);
                                bitmapArrayList_uploading.add(imagebean);


                            }
                        }
                        bitmapArrayList_uploading.add(bitmapArrayList_uploading.size(), getDefualPlusbean());

                        if (uploadImageAdapter != null) {
                            uploadImageAdapter.notifyDataSetChanged();
                        }
                    }
                    else {


                        bitmapArrayList_uploading.clear();
                        if (uploadImageAdapter != null) {
                            uploadImageAdapter.notifyDataSetChanged();
                        }

                        for (String result : mResults) {


                            if (mResults.size() >= 2) {
                                edittext_create_gallery_albumtitle.setVisibility(View.VISIBLE);
                            }

                            Bitmap bitmap = decodeAndResizeFile(new File(
                                    result));

                            if (bitmap != null) {

                                recyclerView_grid_gallery_images.setVisibility(View.VISIBLE);
                                relativeLayout_upload_image_plus_view.setVisibility(View.GONE);


                                Imagebean imagebean = new Imagebean();
                                imagebean.setBitmap(bitmap);
                                bitmapArrayList_uploading.add(imagebean);


                            }
                        }
                        bitmapArrayList_uploading.add(bitmapArrayList_uploading.size(), getDefualPlusbean());

                        if (uploadImageAdapter != null) {
                            uploadImageAdapter.notifyDataSetChanged();
                        }
                    }
                }





            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



        /*try {
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

                                recyclerView_grid_gallery_images.setVisibility(View.VISIBLE);
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
                        CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateGalleryActivity.this);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreateGalleryActivity.this);

                }
            }
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
*/


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

                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateGalleryActivity.this);

                    }


                    JSONObject json_single_gallery = jsonObject_main.getJSONObject(JSONCommonKeywords.gallery);
                    Gallerybean gallerybean = GallerybeanBuild.getGalleryBeanFromJSON(json_single_gallery);


                    if ((GalleryMainActivity.gallerybeanArrayList_main != null)) {
                        GalleryMainActivity.gallerybeanArrayList_main.add(0, gallerybean);
                        GalleryMainActivity.galleryGridAdapter.notifyItemInserted(0);
                        GalleryMainActivity.mRecyclerView.smoothScrollToPosition(0);
                        GalleryMainActivity.mRecyclerView.setVisibility(View.VISIBLE);
                        GalleryMainActivity.baseTextview_error.setVisibility(View.GONE);

                        finish();

                    }


                } else {
                    if (jsonObject_main.has(JSONCommonKeywords.message)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                    }


                    if (responcebean.getErrorMessage() == null) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.create_gallery_failed), CreateGalleryActivity.this);
                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateGalleryActivity.this);

                    }

                }


            } catch (Exception e) {
                CommonMethods.customToastMessage(e.getMessage(), CreateGalleryActivity.this);

            }

        } else {
            if (responcebean.getErrorMessage() == null) {
                CommonMethods.customToastMessage(getResources().getString(R.string.create_gallery_failed), CreateGalleryActivity.this);
            } else {
                CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateGalleryActivity.this);

            }
        }


    }

    public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageViewHolder> {


        public UploadImageAdapter() {

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
                            recyclerView_grid_gallery_images.setVisibility(View.GONE);
                            relativeLayout_upload_image_plus_view.setVisibility(View.VISIBLE);
                        }
                        uploadImageAdapter = new UploadImageAdapter();
                        recyclerView_grid_gallery_images.setAdapter(uploadImageAdapter);

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
                CommonMethods.customToastMessage(getResources().getString(R.string.image_upload_limit_corss_message), CreateGalleryActivity.this);
            } else {
                Imagebean imagebean = (Imagebean) view.getTag();
                if(imagebean!=null)
                {
                    if (imagebean.isPlusIconNeedToEnable()) {

                        // start multiple photos selector
                        Intent intent = new Intent(CreateGalleryActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
                        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 12);
// min size of image which will be shown; to filter tiny images (mainly icons)
                        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
                        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
                        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }

            }

        }
    }


}

