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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.CreatePollsOptionsAdapter;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Imagebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.PollOptionbean;
import com.app.collow.beans.Pollsbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beansgenerate.PollsbeanBuild;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
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
import java.util.List;

/**
 * Created by Harmis on 08/02/17.
 */

public class CreatePollsActivity extends BaseActivity implements SetupViewInterface {
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    BaseTextview baseTextview_left_side = null, baseTextview_save_polls= null;
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageView_plus_icon_only;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    ArrayList<Imagebean> bitmapArrayList_uploading = new ArrayList<>();
    RecyclerView recyclerView_grid_classfied_images = null;
    CreatePollsActivity.UploadImageAdapter uploadImageAdapter = null;
    String communityID = null;
    private Intent pictureActionIntent = null;
    RelativeLayout relativeLayout_upload_image_plus_view = null;

    BaseEdittext baseEdittext_enter_questions=null;
    RecyclerView recyclerView_options=null;
    RelativeLayout relativeLayout_upload_options=null;
   public static ArrayList<PollOptionbean> pollOptionbeanArrayList=new ArrayList<>();
    CreatePollsOptionsAdapter pollsOptionsAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(CreatePollsActivity.this, getApplicationContext(), getIntent().getExtras(), CreatePollsActivity.class.getClass());
            commonSession = new CommonSession(CreatePollsActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
            }
            pollOptionbeanArrayList.clear();
            //create default two options

            PollOptionbean pollOptionbean1=new PollOptionbean();
            pollOptionbean1.setHint_title(getResources().getString(R.string.option) + "1");
            pollOptionbean1.setNeedToCreateOptions(true);


            PollOptionbean pollOptionbean2=new PollOptionbean();
            pollOptionbean2.setHint_title(getResources().getString(R.string.option) + "2");
            pollOptionbean2.setNeedToCreateOptions(true);

            pollOptionbeanArrayList.add(pollOptionbean1);
            pollOptionbeanArrayList.add(pollOptionbean2);


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
            baseTextview_header_title.setText(getResources().getString(R.string.new_poll));
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);
            baseTextview_save_polls = (BaseTextview) toolbar_header.findViewById(R.id.textview_right_side_title);
            baseTextview_left_side.setVisibility(View.VISIBLE);
            baseTextview_save_polls.setVisibility(View.VISIBLE);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_save_polls.setText(getResources().getString(R.string.save_small));


            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            baseTextview_left_side.setText(getResources().getString(R.string.cancel));
            drawerLayout.setEnabled(true);

            baseTextview_save_polls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                        if(TextUtils.isEmpty(baseEdittext_enter_questions.getText().toString()))
                        {
                            CommonMethods.customToastMessage(getResources().getString(R.string.poll_question_not_to_be_empty),CreatePollsActivity.this);
                        }
                        else
                        {
                            int count_options=recyclerView_options.getChildCount();
                            boolean isAnyEmptyOption=false;

                            JSONArray jsonArray_options=new JSONArray();
                            for (int i = 0; i < count_options; i++) {
                                View view=recyclerView_options.getChildAt(i);



                                EditText baseEdittext_options = (BaseEdittext) view.findViewById(R.id.edittext_create_polls_question);

                                PollOptionbean pollsbean=pollOptionbeanArrayList.get(i);
                                if(TextUtils.isEmpty(baseEdittext_options.getText().toString()))

                                {
                                    isAnyEmptyOption=true;
                                    pollsbean.setEmptyOption(true);
                                    pollOptionbeanArrayList.remove(i);
                                    pollOptionbeanArrayList.add(i,pollsbean);
                                }
                                else
                                {
                                    jsonArray_options.put(baseEdittext_options.getText().toString().trim());
                                }
                            }


                            if(isAnyEmptyOption)
                            {

                                pollsOptionsAdapter.notifyDataSetChanged();
                                CommonMethods.displayLog("Options text","empty");
                                CommonMethods.customToastMessage(getResources().getString(R.string.poll_options_not_to_be_empty),CreatePollsActivity.this);


                            }
                            else
                            {
                                CommonMethods.displayLog("Options text","Not empty");
                                pollsOptionsAdapter.notifyDataSetChanged();

                            }






                            requestParametersbean.setCommunityID(communityID);
                            requestParametersbean.setUserId(commonSession.getLoggedUserID());
                            requestParametersbean.setPollTitle(baseEdittext_enter_questions.getText().toString());
                            requestParametersbean.setPollsOptions(jsonArray_options);



                            ///make request here
                            createPolls();

                        }





                    } catch (Exception e) {
                        new BaseException(e, false, retryParameterbean);
                    }


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
            view_home = getLayoutInflater().inflate(R.layout.polls_create_new, null);


            imageView_plus_icon_only = (ImageView) view_home.findViewById(R.id.imageview_upload_image_plus_icon_image);
            relativeLayout_upload_image_plus_view = (RelativeLayout) view_home.findViewById(R.id.relativelayout_upload_create_polls_images);


            baseEdittext_enter_questions= (BaseEdittext) view_home.findViewById(R.id.edittext_enter_poll_question);
            relativeLayout_upload_options= (RelativeLayout) view_home.findViewById(R.id.relativelayout_upload_create_polls_options);


            recyclerView_options= (RecyclerView) view_home.findViewById(R.id.my_recycler_view_polls_options);
            recyclerView_options.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(20);
            recyclerView_options.addItemDecoration(dividerItemDecoration);
            // use a linear layout manager
            recyclerView_options.setLayoutManager(mLayoutManager);
            pollsOptionsAdapter=new CreatePollsOptionsAdapter();
            recyclerView_options.setAdapter(pollsOptionsAdapter);


            frameLayout_container.addView(view_home);




            recyclerView_grid_classfied_images = (RecyclerView) view_home.findViewById(R.id.recycler_view_grid_create_polls);
            recyclerView_grid_classfied_images.setHasFixedSize(true);
            // Disabled nested scrolling since Parent scrollview will scroll the content.
            recyclerView_grid_classfied_images.setNestedScrollingEnabled(false);
            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
            recyclerView_grid_classfied_images.setLayoutManager(gaggeredGridLayoutManager);


            int spanCount = 3; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = false;
            recyclerView_grid_classfied_images.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


            uploadImageAdapter = new CreatePollsActivity.UploadImageAdapter(this);
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



        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    private void setupEvents() {
        try {


            relativeLayout_upload_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(pollOptionbeanArrayList.size()==10)
                        {
                            CommonMethods.customToastMessage(getResources().getString(R.string.you_can_not_add_more_options),CreatePollsActivity.this);

                            relativeLayout_upload_options.setVisibility(View.GONE);
                        }
                        else
                        {
                            int index=pollOptionbeanArrayList.size();
                            PollOptionbean pollOptionbean=new PollOptionbean();
                            int newindex=index+1;
                            pollOptionbean.setHint_title(getResources().getString(R.string.option) +" " +newindex);
                            pollOptionbean.setNeedToCreateOptions(true);
                            pollOptionbeanArrayList.add(index,pollOptionbean);
                            pollsOptionsAdapter.notifyItemInserted(index);
                        }
                    } catch (Resources.NotFoundException e) {
                        new BaseException(e, false, retryParameterbean);
                    }


                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
    }


    public void createPolls() {

        try {
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.CREATE_POLLS.getScrenIndex(), requestParametersbean);

            PassParameterbean passParameterbean = new PassParameterbean(this, CreatePollsActivity.this, getApplicationContext(), URLs.CREATE_POLL, jsonObjectGetPostParameterEachScreen, ScreensEnums.CREATE_POLLS.getScrenIndex(), CreatePollsActivity.class.getClass());
            passParameterbean.setRequestMethod(HTTPRequestMethodEnums.POST.getHTTPRequestMethodInex());
          //  passParameterbean.setBitmapArrayList_mutliple_image(bitmapArrayList_uploading);
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
                        CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreatePollsActivity.this);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreatePollsActivity.this);

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
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), CreatePollsActivity.this);

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
                    else
                    {
                        responcebean.setErrorMessage(getResources().getString(R.string.polls_created_successfully));

                    }

                    JSONObject jsonObject_single_polls=jsonObject_main.getJSONObject(JSONCommonKeywords.Polls);

                    Pollsbean pollsbean= PollsbeanBuild.getPollsbean(jsonObject_single_polls,CreatePollsActivity.this);

                    if ((PollsMainActivity.pollsbeanArrayList != null)) {
                        PollsMainActivity.pollsbeanArrayList.add(0, pollsbean);
                        PollsMainActivity.pollsAdapter.notifyItemInserted(0);
                        PollsMainActivity.mRecyclerView.smoothScrollToPosition(0);
                        PollsMainActivity.baseTextview_error.setVisibility(View.GONE);
                        PollsMainActivity.mRecyclerView.setVisibility(View.VISIBLE);
                        finish();

                    }



                } else {
                    if (jsonObject_main.has(JSONCommonKeywords.message)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                    }


                    if (responcebean.getErrorMessage() == null) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.polls_created_failed), CreatePollsActivity.this);
                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreatePollsActivity.this);

                    }

                }


            } catch (Exception e) {
                CommonMethods.customToastMessage(e.getMessage(), CreatePollsActivity.this);

            }

        } else {
            if (responcebean.getErrorMessage() == null) {
                CommonMethods.customToastMessage(getResources().getString(R.string.polls_created_failed), CreatePollsActivity.this);
            } else {
                CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreatePollsActivity.this);

            }
        }


    }

    public class UploadImageAdapter extends RecyclerView.Adapter<CreatePollsActivity.UploadImageViewHolder> {

        private Context context;

        public UploadImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public CreatePollsActivity.UploadImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.only_imageview_with_close_icon, null);
            CreatePollsActivity.UploadImageViewHolder rcv = new CreatePollsActivity.UploadImageViewHolder(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(CreatePollsActivity.UploadImageViewHolder holder, int position) {


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
                        uploadImageAdapter = new CreatePollsActivity.UploadImageAdapter(CreatePollsActivity.this);
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
                CommonMethods.customToastMessage(getResources().getString(R.string.image_upload_limit_corss_message), CreatePollsActivity.this);
            } else {
                Imagebean imagebean = (Imagebean) view.getTag();
                if (imagebean.isPlusIconNeedToEnable()) {
                    startDialog();
                }
            }

        }
    }


}
