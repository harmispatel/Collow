package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.GalleryGridAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Gallerybean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beansgenerate.GallerybeanBuild;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.RecyclerItemClickListener;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryMainActivity  extends BaseActivity implements SetupViewInterface {

    View view_home = null;
   public static GalleryGridAdapter galleryGridAdapter = null;
    public static ArrayList<Gallerybean> gallerybeanArrayList_main = new ArrayList<>();
    public static BaseTextview baseTextview_error = null;
    public static RecyclerView mRecyclerView;
    protected Handler handler;
    CommonSession commonSession = null;
    int current_start=0;
    //header iterms
   BaseTextview baseTextview_header_title=null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    public static GalleryMainActivity galleryMainActivity=null;
    FloatingActionButton floatingActionButton_create_gallery=null;
    String communityID = null,communityText=null;
    CommunityAccessbean communityAccessbean=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(GalleryMainActivity.this, getApplicationContext(), getIntent().getExtras(), GalleryMainActivity.class.getClass());
            commonSession = new CommonSession(GalleryMainActivity.this);
            galleryMainActivity=this;
            gallerybeanArrayList_main.clear();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean= (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
                communityText=bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);

            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
       getGalleryImageListing();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);



        }

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

            imageview_right_foursquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openCommunityMenu(GalleryMainActivity.this,communityID,communityText,communityAccessbean);
                }
            });
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
        view_home = getLayoutInflater().inflate(R.layout.gallery_main, null);

        floatingActionButton_create_gallery= (FloatingActionButton) view_home.findViewById(R.id.fab_create_news_gallery);
        if (commonSession.isUserAdminNow()) {
            floatingActionButton_create_gallery.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton_create_gallery.setVisibility(View.GONE);

        }
        if(commonSession.isUserFollow())
        {
            floatingActionButton_create_gallery.setVisibility(View.VISIBLE);
        }
        else
        {
            floatingActionButton_create_gallery.setVisibility(View.GONE);

        }



        baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);

        mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mRecyclerView.setLayoutManager(gaggeredGridLayoutManager);






        // use a linear layout manager

        galleryGridAdapter = new GalleryGridAdapter(galleryMainActivity, mRecyclerView,ScreensEnums.GALLERY.getScrenIndex());
        mRecyclerView.setAdapter(galleryGridAdapter);
        frameLayout_container.addView(view_home);



        galleryGridAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        current_start += 10;


                         requestParametersbean.setStart_limit(current_start);

                        getGalleryImageListing();


                            }
                        }, 2000);

                    }
        });

        floatingActionButton_create_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(GalleryMainActivity.this,CreateGalleryActivity.class);

                Bundle bundle = new Bundle();


                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(GalleryMainActivity.this,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        try {
                            Gallerybean gallerybean= (Gallerybean) view.getTag();

                            ArrayList<String> stringArrayList = gallerybean.getStringArrayList_images_urls();
                            if (stringArrayList != null) {
                                if (stringArrayList.size() != 0) {
                                    if (stringArrayList.size()==1) {

                                        Intent intent=new Intent(GalleryMainActivity.this,GalleryDetailActivity.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,gallerybean);
                                        bundle.putString(BundleCommonKeywords.KEY_URL,stringArrayList.get(0));
                                        intent.putExtras(bundle);

                                        startActivity(intent);

                                    } else if (stringArrayList.size()==2) {
                                        Intent intent=new Intent(GalleryMainActivity.this,GallerySubActivity.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,gallerybean);
                                        intent.putExtras(bundle);
                                        startActivity(intent);


                                    } else if (stringArrayList.size()==3){
                                        Intent intent=new Intent(GalleryMainActivity.this,GallerySubActivity.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,gallerybean);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else if (stringArrayList.size()==4) {
                                        Intent intent=new Intent(GalleryMainActivity.this,GallerySubActivity.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,gallerybean);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent=new Intent(GalleryMainActivity.this,GallerySubActivity.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,gallerybean);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }

                                }
                                else
                                {
                                    Intent intent=new Intent(GalleryMainActivity.this,GalleryDetailActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                Intent intent=new Intent(GalleryMainActivity.this,GalleryDetailActivity.class);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        // do whatever
                    }


                })
        );
    }

    private void setupEvents() {

    }


    // this method is used for login user
    public void getGalleryImageListing() {
        try {
            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.GALLERY.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, GalleryMainActivity.this, getApplicationContext(), URLs.GET_GALLERY_LISTING, jsonObjectGetPostParameterEachScreen, ScreensEnums.GALLERY.getScrenIndex(), GalleryMainActivity.class.getClass());


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

                    ArrayList<Gallerybean> gallerybean_local = new ArrayList<>();
                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.Galleries)) {
                        JSONArray jsonArray_gallery = jsonObject_main.getJSONArray(JSONCommonKeywords.Galleries);
                        for (int i = 0; i < jsonArray_gallery.length(); i++) {

                            JSONObject jsonObject_single_event = jsonArray_gallery.getJSONObject(i);

                            Gallerybean gallerybean = GallerybeanBuild.getGalleryBeanFromJSON(jsonObject_single_event);
                            gallerybean.setPosition(i);
                            gallerybean_local.add(gallerybean);
                        }

                        if (current_start == 0) {
                            gallerybeanArrayList_main = gallerybean_local;
                            galleryGridAdapter.notifyDataSetChanged();

                        } else {

                            int start = gallerybeanArrayList_main.size();

                            for (int i = 0; i < gallerybean_local.size(); i++) {

                                Gallerybean gallerybean=gallerybean_local.get(i);
                                gallerybean.setPosition(start);
                                gallerybeanArrayList_main.add(start, gallerybean);
                                galleryGridAdapter.notifyItemInserted(gallerybeanArrayList_main.size());
                                start++;

                            }
                            galleryGridAdapter.setLoaded();

                        }


                    } else {
                        handlerError(responcebean);

                    }
                } else {
                    handlerError(responcebean);

                }
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
                baseTextview_error.setText(getResources().getString(R.string.no_galleries_found));
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
}
