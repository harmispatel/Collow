package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.ClassifiedAdapter;
import com.app.collow.adapters.FormsAndDocsAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Eventbean;
import com.app.collow.beans.FormsAndDocsbean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormsAndDocsMainActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
  public static  FormsAndDocsAdapter formsanddocsAdapter = null;
    public static ArrayList<FormsAndDocsbean> formsanddocsbeanlist = new ArrayList<>();
    private BaseTextview baseTextview_error = null;
    public static RecyclerView mRecyclerView;
    protected Handler handler;
    CommonSession commonSession = null;

    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null,imageview_community_menu=null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    BaseTextview baseTextview_header_title=null;
    public static FormsAndDocsMainActivity formsAndDocsMainActivity=null;
    FloatingActionButton floatingActionButton_create_forms_and_docx=null;
    int current_start = 0;
    CommunityAccessbean communityAccessbean = null;
    String communityID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(FormsAndDocsMainActivity.this, getApplicationContext(), getIntent().getExtras(), FormsAndDocsMainActivity.class.getClass());
            commonSession = new CommonSession(FormsAndDocsMainActivity.this);
           Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
            }
            formsAndDocsMainActivity=this;
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            getdocslisting();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    public void setupHeaderView() {
        try {
            baseTextview_header_title=(BaseTextview)toolbar_header.findViewById(R.id.textview_header_title) ;
            baseTextview_header_title.setText(getResources().getString(R.string.forms));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageview_community_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_community_menu.setVisibility(View.VISIBLE);

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
            imageview_community_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MyUtils.openCommunityMenu(FormsAndDocsMainActivity.this,false,false,"1","1");


                }
            });

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.formanddocx_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);
            FormsAndDocsbean docsbean = new FormsAndDocsbean();
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            formsanddocsbeanlist.add(docsbean);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

           floatingActionButton_create_forms_and_docx= (FloatingActionButton) view_home.findViewById(R.id.fab_create_form_and_docx);
            if(commonSession.isUserAdminNow())
            {
                floatingActionButton_create_forms_and_docx.setVisibility(View.VISIBLE);
            }
            else
            {
                floatingActionButton_create_forms_and_docx.setVisibility(View.VISIBLE);

            }


            formsanddocsAdapter = new FormsAndDocsAdapter(FormsAndDocsMainActivity.this,mRecyclerView);
            mRecyclerView.setAdapter(formsanddocsAdapter);
            frameLayout_container.addView(view_home);


            formsanddocsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current_start = +10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);
                            //  getNewsListing();
                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                }
            });


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }

    private void setupEvents() {
       floatingActionButton_create_forms_and_docx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commonSession.isUserAdminNow())
                {
                    Intent intent = new Intent(FormsAndDocsMainActivity.this, CreateFormAndDocumentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                {

                }

            }
        });
    }


    // this method is used for login user
    public void getdocslisting() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.FORMSANDDOCS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this,FormsAndDocsMainActivity.this, getApplicationContext(), URLs.CREATE_FORM_AND_DOCX, jsonObjectGetPostParameterEachScreen, ScreensEnums.FORMSANDDOCS.getScrenIndex(), FormsAndDocsMainActivity.class.getClass());

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


                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.NewsLists)) {

                        ArrayList<FormsAndDocsbean> formsbeanArrayList_local = new ArrayList<>();
                        JSONArray jsonArray_docs_list = jsonObject_main.getJSONArray(JSONCommonKeywords.NewsLists);
                        for (int i = 0; i < jsonArray_docs_list.length(); i++) {

                            FormsAndDocsbean docsbean = new FormsAndDocsbean();
                            JSONObject jsonObject_single = jsonArray_docs_list.getJSONObject(i);


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.News)) {
                                docsbean.setFormsanddocs_title(jsonObject_single.getString(JSONCommonKeywords.News));

                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Description)) {
                                docsbean.setFormsanddocs_date(jsonObject_single.getString(JSONCommonKeywords.Description));

                            }

                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.NewsDate)) {
                                docsbean.setFormsanddocs_description(jsonObject_single.getString(JSONCommonKeywords.NewsDate));

                            }


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.UserName)) {
                                docsbean.setFormsanddocs_download(jsonObject_single.getString(JSONCommonKeywords.UserName));

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
                            formsanddocsbeanlist = formsbeanArrayList_local;
                            formsanddocsAdapter.notifyDataSetChanged();

                        } else {

                            int start = formsanddocsbeanlist.size();

                            for (int i = 0; i < formsbeanArrayList_local.size(); i++) {

                                FormsAndDocsbean docsbean = formsbeanArrayList_local.get(i);
                                docsbean.setPosition(start);
                                formsanddocsbeanlist.add(start, docsbean);
                                formsanddocsAdapter.notifyItemInserted(formsanddocsbeanlist.size());

                                start++;

                            }
                            formsanddocsAdapter.setLoaded();

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

    public void handlerError(Responcebean responcebean)
    {
        try {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals(""))
            {
                if(current_start==0)
                {
                    baseTextview_error.setText(getResources().getString(R.string.no_form_and_document_found));
                    mRecyclerView.setVisibility(View.GONE);
                    baseTextview_error.setVisibility(View.VISIBLE);

                }
                else
                {

                }
            }
            else
            {
                if(current_start==0)
                {
                    baseTextview_error.setText(responcebean.getErrorMessage());
                    mRecyclerView.setVisibility(View.GONE);
                    baseTextview_error.setVisibility(View.VISIBLE);

                }
                else
                {

                }
            }
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }
    }
}