package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.GalleryGridSubAdapter;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Gallerybean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.RecyclerItemClickListener;


/**
 * Created by Harmis on 24/02/17.
 */

public class GallerySubActivity extends BaseActivity {

    public static GallerySubActivity GallerySubActivity = null;
    protected Handler handler;
    View view_home = null;
    public static GalleryGridSubAdapter galleryGridSubAdapter = null;
    CommonSession commonSession = null;
    int current_start = 0;
    //header iterms
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    FloatingActionButton floatingActionButton_create_gallery = null;
    String communityID = null;
    Gallerybean gallerybean = null;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(GallerySubActivity.this, getApplicationContext(), getIntent().getExtras(), GallerySubActivity.class.getClass());
            commonSession = new CommonSession(GallerySubActivity.this);
            GallerySubActivity = this;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                gallerybean = (Gallerybean) bundle.getSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN);
            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    public void setupHeaderView() {


        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(gallerybean.getTitle());

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

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        view_home = getLayoutInflater().inflate(R.layout.gallery_main, null);

        floatingActionButton_create_gallery = (FloatingActionButton) view_home.findViewById(R.id.fab_create_news_gallery);
        floatingActionButton_create_gallery.setVisibility(View.GONE);



        mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        mRecyclerView.setLayoutManager(gaggeredGridLayoutManager);


        // use a linear layout manager

        galleryGridSubAdapter = new GalleryGridSubAdapter(GallerySubActivity, gallerybean);
        mRecyclerView.setAdapter(galleryGridSubAdapter);
        frameLayout_container.addView(view_home);


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(GallerySubActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        String url=view.getTag().toString();
                        Intent intent=new Intent(GallerySubActivity.this,GalleryDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,gallerybean);
                        bundle.putString(BundleCommonKeywords.KEY_URL,url);
                        intent.putExtras(bundle);

                        startActivity(intent);


                        // do whatever
                    }


                })
        );
    }


}
