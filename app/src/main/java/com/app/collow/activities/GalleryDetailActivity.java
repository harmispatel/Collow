package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.collow.R;
import com.app.collow.adapters.GalleryGridSubAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Gallerybean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
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
import com.app.collow.utils.URLs;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harmis on 3/2/17.
 */

public class GalleryDetailActivity extends BaseActivity implements SetupViewInterface {

    protected Handler handler;
    View view_home = null;
    ImageView imageview_community_likes = null;
    ImageView imageview_gallerydetail_comments = null;
    ImageView imageview_gallerydetail_close = null;
    CircularImageView imageview_gallerydetail_profilepic = null;
    ImageView imageview_gallerydetail_likes = null;
    BaseTextview textview_gallerydetail_likes = null;
    public static BaseTextview textview_gallerydetail_comments = null;
    BaseTextview textview_gallerydetail_name = null;
    BaseTextview textview_gallerydetail_date = null;
    BaseTextview textview_gallerydetail_title = null;
    BaseTextview baseTextview_header_title = null;
    BaseTextview baseTextview_left_side = null;
    String KEY_COMMUNITY_ID = null;
    int current_start = 0;
    CommunityAccessbean communityAccessbean = null;
    CommonSession commonSession = null;
    String communityID = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    Gallerybean gallerybean = null;
    ImageView imageView_main_details = null;
    String url = null;
    LinearLayout linearLayout_likes = null, linearLayout_comments = null;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(GalleryDetailActivity.this, getApplicationContext(), getIntent().getExtras(), GalleryDetailActivity.class.getClass());
            commonSession = new CommonSession(GalleryDetailActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                gallerybean = (Gallerybean) bundle.getSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN);
                url = bundle.getString(BundleCommonKeywords.KEY_URL);
                MyUtils.markAsViewed(GalleryDetailActivity.this, gallerybean.getGalleryID(), CommonKeywords.TYPE_FEED_GALLERY, ScreensEnums.GALLERYDETAIL.getScrenIndex(), gallerybean.getPosition(), requestParametersbean, null);
            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setupHeaderView() {


        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.gallery));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageView_delete = (ImageView) toolbar_header.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.GONE);
            imageView_view = (ImageView) toolbar_header.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.GONE);
            imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.GONE);
            imageView_search = (ImageView) toolbar_header.findViewById(R.id.imageview_search_items);
            imageView_search.setVisibility(View.GONE);
            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);


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
            toolbar_header.setVisibility(View.GONE);
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {

            view_home = getLayoutInflater().inflate(R.layout.gallery_detail, null);

            imageView_main_details = (ImageView) view_home.findViewById(R.id.imageview_gallerydetail_image);
            imageview_gallerydetail_likes = (ImageView) view_home.findViewById(R.id.imageview_gallerydetail_like);
            imageview_gallerydetail_comments = (ImageView) view_home.findViewById(R.id.imageview_gallerydetail_comments);
            imageview_gallerydetail_close = (ImageView) view_home.findViewById(R.id.imageview_gallerydetail_close);
            imageview_gallerydetail_profilepic = (CircularImageView) view_home.findViewById(R.id.imageview_gallerydetail_profileimage);
            textview_gallerydetail_likes = (BaseTextview) view_home.findViewById(R.id.textview_gallerydetail_likes);
            textview_gallerydetail_comments = (BaseTextview) view_home.findViewById(R.id.textview_gallerydetail_comments);
            textview_gallerydetail_name = (BaseTextview) view_home.findViewById(R.id.textview_gallerydetail_name);
            textview_gallerydetail_date = (BaseTextview) view_home.findViewById(R.id.textview_gallerydetail_date);
            textview_gallerydetail_title = (BaseTextview) view_home.findViewById(R.id.textview_gallerydetail_title);


            linearLayout_likes = (LinearLayout) view_home.findViewById(R.id.layout_gallery_details_likes);
            linearLayout_comments = (LinearLayout) view_home.findViewById(R.id.layout_gallery_details_comments);

            frameLayout_container.addView(view_home);

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void setupEvents() {


        if (gallerybean != null) {
            if (CommonMethods.isTextAvailable(gallerybean.getTitle())) {
                textview_gallerydetail_title.setText(gallerybean.getTitle());
            }

            if (CommonMethods.isImageUrlValid(gallerybean.getProfilePic())) {
                Picasso.with(GalleryDetailActivity.this)
                        .load(gallerybean.getProfilePic())
                        .into(imageview_gallerydetail_profilepic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                imageview_gallerydetail_profilepic.setImageResource(R.drawable.defualt_circle);

                            }
                        });
            } else {
                imageview_gallerydetail_profilepic.setImageResource(R.drawable.defualt_circle);

            }

            SocialOptionbean socialOptionbean = gallerybean.getSocialOptionbean();
            MyUtils.handleSocialOption(GalleryDetailActivity.this, socialOptionbean, textview_gallerydetail_likes, textview_gallerydetail_comments, null);


            linearLayout_likes.setTag(String.valueOf(gallerybean.getPosition()));
            linearLayout_likes.setOnClickListener(new MyOnClickListener(GalleryDetailActivity.this) {
                @Override
                public void onClick(View v) {
                    try {
                        if (isAvailableInternet()) {

                            likeItemhandler();


                        } else {
                            showInternetNotfoundMessage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            linearLayout_comments.setOnClickListener(new MyOnClickListener(GalleryDetailActivity.this) {
                @Override
                public void onClick(View v) {
                    try {
                        if (isAvailableInternet()) {
                            MyUtils.openCommentswActivity(GalleryDetailActivity.this, ScreensEnums.GALLERYDETAIL.getScrenIndex(), gallerybean.getActivityID(), gallerybean.getPosition(), CommonKeywords.TYPE_FEED_GALLERY, gallerybean.getTitle ());

                        } else {
                            showInternetNotfoundMessage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            if (gallerybean.isLikedByLoggedUser()) {
                imageview_gallerydetail_likes.setImageResource(R.drawable.gallery_like);
            } else {
                imageview_gallerydetail_likes.setImageResource(R.drawable.gallery_unlike);


            }


            SpannableStringBuilder builder = new SpannableStringBuilder();

            if (CommonMethods.isTextAvailable(gallerybean.getUserName())) {
                SpannableString title = new SpannableString(gallerybean.getUserName());
                title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, title.length(), 0);
                builder.append(title);

            }

            if (CommonMethods.isTextAvailable(gallerybean.getCreatedDate())) {

                SpannableString str2 = new SpannableString(gallerybean.getCreatedDate());
                str2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.edittext_filter_gray_color)), 0, str2.length(), 0);
                str2.setSpan(new RelativeSizeSpan(0.7f), 0, str2.length(), 0); // set size

                builder.append(" ");

                builder.append(str2);
            }


            textview_gallerydetail_name.setText(builder, BaseTextview.BufferType.SPANNABLE);


        }
        imageview_gallerydetail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (CommonMethods.isImageUrlValid(url)) {
            Picasso.with(GalleryDetailActivity.this)
                    .load(url)
                    .into(imageView_main_details, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageView_main_details.setImageResource(R.drawable.defualt_square);

                        }
                    });
        } else {
            imageView_main_details.setImageResource(R.drawable.defualt_square);

        }


        imageview_gallerydetail_profilepic.setOnClickListener(new MyOnClickListener(GalleryDetailActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    Intent intent = new Intent(GalleryDetailActivity.this, FullProfileMainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_ID, gallerybean.getUserID());
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.USER_FULL_PROFILE_VIEW.getScrenIndex());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    showInternetNotfoundMessage();
                }
            }
        });

    }


    // this method is used for login user
    public void acViewEvents() {
        try {


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.GALLERYDETAIL.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, GalleryDetailActivity.this, getApplicationContext(), URLs.GALLERYDETAIL, jsonObjectGetPostParameterEachScreen, ScreensEnums.GALLERYDETAIL.getScrenIndex(), GalleryDetailActivity.class.getClass());

            passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);

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


                } else {
                    handlerError(responcebean);

                }
            } else {
                handlerError(responcebean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handlerError(responcebean);
            new BaseException(e, false, retryParameterbean);

        }


    }

    public void handlerError(Responcebean responcebean) {
        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
            if (current_start == 0) {
                baseTextview_error.setText(getResources().getString(R.string.no_events_exist));
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            } else {


            }
        } else {
            if (current_start == 0) {
                baseTextview_error.setText(responcebean.getErrorMessage());
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            } else {

            }
        }
    }


    private void likeItemhandler() {


        try {
            RequestParametersbean requestParametersbean = new RequestParametersbean();
            requestParametersbean.setActivityId(gallerybean.getActivityID());
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setLiketype(CommonKeywords.TYPE_FEED_GALLERY);
            if (gallerybean.isLikedByLoggedUser()) {
                requestParametersbean.setLike("0");

            } else {
                requestParametersbean.setLike("1");

            }

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.LIKES.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {
                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                                String totallikes = jsonObject_main.getString(JSONCommonKeywords.TotalLike);
                                if (totallikes == null || totallikes.equals("")) {
                                } else {


                                    if (gallerybean.isLikedByLoggedUser()) {
                                        gallerybean.setLikedByLoggedUser(false);
                                        imageview_gallerydetail_likes.setImageResource(R.drawable.gallery_unlike);
                                    } else {
                                        imageview_gallerydetail_likes.setImageResource(R.drawable.gallery_like);
                                        gallerybean.setLikedByLoggedUser(true);


                                    }

                                    final SocialOptionbean socialOptionbean = gallerybean.getSocialOptionbean();
                                    socialOptionbean.setLike(Integer.parseInt(totallikes));
                                    MyUtils.handleSocialOption(GalleryDetailActivity.this, socialOptionbean, textview_gallerydetail_likes, textview_gallerydetail_comments, null);


                                    //sub gallery updating
                                    if (GalleryGridSubAdapter.gallerybean_globle != null) {
                                        GalleryGridSubAdapter.gallerybean_globle.setSocialOptionbean(socialOptionbean);
                                        if (GallerySubActivity.galleryGridSubAdapter != null) {
                                            GallerySubActivity.galleryGridSubAdapter.notifyDataSetChanged();
                                        }

                                    }


                                    if (GalleryMainActivity.gallerybeanArrayList_main.size() != 0) {
                                        //Gallery main activity updating
                                        GalleryMainActivity.gallerybeanArrayList_main.set(gallerybean.getPosition(), gallerybean);
                                        if (GalleryMainActivity.galleryGridAdapter != null) {
                                            GalleryMainActivity.galleryGridAdapter.notifyItemChanged(gallerybean.getPosition());
                                        }
                                    }


                                }

                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, GalleryDetailActivity.this, GalleryDetailActivity.this, URLs.LIKEACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.LIKES.getScrenIndex(), NewsMainActivity.class.getClass());
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GalleryDetail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}


