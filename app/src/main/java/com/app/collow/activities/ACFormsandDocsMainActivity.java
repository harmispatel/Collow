package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.ACFormsandDocsAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACFormsandDocsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ACFormsandDocsMainActivity extends BaseActivity implements SetupViewInterface {

    public static ArrayList<ACFormsandDocsbean> acdocumentbeanArrayList = new ArrayList<>();
    protected Handler handler;
    View view_home = null;
    ACFormsandDocsAdapter acDocumentAdapter = null;
    BaseTextview baseTextview_header_title = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_left_side = null;
    FloatingActionButton floatingActionButton_create_forms = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    String communityID = null;
    int current_start = 0;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(ACFormsandDocsMainActivity.this, getApplicationContext(), null, ACFormsandDocsMainActivity.class.getClass());
            commonSession = new CommonSession(ACFormsandDocsMainActivity.this);

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
            baseTextview_header_title.setText(getResources().getString(R.string.address));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);

            imageView_delete = (ImageView) toolbar_header.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.GONE);
            imageView_view = (ImageView) toolbar_header.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.GONE);
            imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.GONE);
            imageView_search = (ImageView) toolbar_header.findViewById(R.id.imageview_search_items);
            imageView_search.setVisibility(View.VISIBLE);
            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);
            //baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);


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
           /* setSupportActionBar(toolbar_header);
            toolbar_header.addView(toolbar_header);*/
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.acformsanddocs_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            floatingActionButton_create_forms = (FloatingActionButton) view_home.findViewById(R.id.fab_create_new_acforms);
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            ACFormsandDocsbean docsbean = new ACFormsandDocsbean();
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            acdocumentbeanArrayList.add(docsbean);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            acDocumentAdapter = new ACFormsandDocsAdapter(ACFormsandDocsMainActivity.this, mRecyclerView);
            mRecyclerView.setAdapter(acDocumentAdapter);
            frameLayout_container.addView(view_home);

            // getEventList(0);


            acDocumentAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);


                        }
                    }, 2000);

                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    private void setupEvents() {
        try {
            floatingActionButton_create_forms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ACFormsandDocsMainActivity.this, ACNewFormsandDocs.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }


    // this method is used for login user
    public void getDocumentsList() {
        try {


          //  JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACDOCUMENTS.getScrenIndex(), requestParametersbean);
         //   PassParameterbean passParameterbean = new PassParameterbean(this, ACFormsandDocsMainActivity.this, getApplicationContext(), URLs.ACDOCUMENTS, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACDOCUMENTS.getScrenIndex(), ACFormsandDocsMainActivity.class.getClass());


          //  new RequestToServer(passParameterbean, retryParameterbean).execute();


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


                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.NewsLists)) {

                        ArrayList<ACFormsandDocsbean> formsbeanArrayList_local = new ArrayList<>();
                        JSONArray jsonArray_docs_list = jsonObject_main.getJSONArray(JSONCommonKeywords.NewsLists);
                        for (int i = 0; i < jsonArray_docs_list.length(); i++) {

                            ACFormsandDocsbean docsbean = new ACFormsandDocsbean();
                            JSONObject jsonObject_single = jsonArray_docs_list.getJSONObject(i);


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.News)) {
                                docsbean.setAclist_documenttiitle(jsonObject_single.getString(JSONCommonKeywords.News));

                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Description)) {

                            }

                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.NewsDate)) {
                                docsbean.setAclist_documentdate(jsonObject_single.getString(JSONCommonKeywords.NewsDate));

                            }






                            if (jsonObject_single.has(JSONCommonKeywords.isLiked)) {


                                String islikedfeedString = jsonObject_single.getString(JSONCommonKeywords.isLiked);
                                if (islikedfeedString == null || islikedfeedString.equals("")) {
                                    docsbean.setLiked(false);
                                } else if (islikedfeedString.equals("1")) {
                                    docsbean.setLiked(true);

                                } else {
                                    docsbean.setLiked(false);

                                }


                            }

                            ArrayList<String> files_of_news = new ArrayList<>();


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Image))

                            {
                                if (CommonMethods.checkJSONArrayHasData(jsonObject_single, JSONCommonKeywords.Image)) {
                                    JSONArray jsonArray_images = jsonObject_single.getJSONArray(JSONCommonKeywords.Image);
                                    for (int j = 0; j < jsonArray_images.length(); j++) {

                                        if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                                            files_of_news.add(jsonArray_images.getString(j));
                                        }
                                    }


                                }


                            } else {

                            }


                            docsbean.setStringArrayList_fileURLs(files_of_news);


                            docsbean.setPosition(i);
                            formsbeanArrayList_local.add(docsbean);
                        }

                        if (current_start == 0) {
                            acdocumentbeanArrayList = formsbeanArrayList_local;
                            acDocumentAdapter.notifyDataSetChanged();

                        } else {

                            int start = acdocumentbeanArrayList.size();

                            for (int i = 0; i < formsbeanArrayList_local.size(); i++) {

                                ACFormsandDocsbean docsbean = formsbeanArrayList_local.get(i);
                                docsbean.setPosition(start);
                                acdocumentbeanArrayList.add(start, docsbean);
                                acDocumentAdapter.notifyItemInserted(acdocumentbeanArrayList.size());

                                start++;

                            }
                            acDocumentAdapter.setLoaded();

                        }
                    } else {
                        handlerError(responcebean);

                    }


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
}
