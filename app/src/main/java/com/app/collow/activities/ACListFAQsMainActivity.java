package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.ACFAQAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACFAQbean;
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
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.util.ArrayList;

public class ACListFAQsMainActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
    ACFAQAdapter acFaqAdapter = null;
    BaseTextview baseTextview_header_title=null;
    public static ArrayList<ACFAQbean> acfaqbeanArrayList = new ArrayList<>();
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    protected Handler handler;
    CommonSession commonSession = null;
    BaseTextview baseTextview_left_side=null;
    FloatingActionButton floating_new_faq=null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null,imageview_right_foursquare=null;
    ImageView imageView_delete = null,imageView_view=null,imageView_edit=null,imageView_search=null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    String communityID=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(ACListFAQsMainActivity.this, getApplicationContext(), getIntent().getExtras(), ACListFAQsMainActivity.class.getClass());
            commonSession = new CommonSession(ACListFAQsMainActivity.this);

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

            baseTextview_header_title= (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.faq));

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
            imageview_right_foursquare= (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);

      /*  baseTextview_left_side = (BaseTextview) headerview.findViewById(R.id.textview_left_side_title);

        // baseTextview_left_side.setText(getResources().getString(R.string.cancel));
        baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);*/
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
        try {
            view_home = getLayoutInflater().inflate(R.layout.acfaq_main, null);
            floating_new_faq=(FloatingActionButton)view_home.findViewById(R.id.fab_create_new_faq) ;

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            ACFAQbean faqbean = new ACFAQbean();
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            acfaqbeanArrayList.add(faqbean);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            acFaqAdapter = new ACFAQAdapter(ACListFAQsMainActivity.this, mRecyclerView);
            mRecyclerView.setAdapter(acFaqAdapter);
            frameLayout_container.addView(view_home);

           // getFaqtList();


            acFaqAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


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
        try {
            floating_new_faq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ACListFAQsMainActivity.this, ACNewFAQ.class);
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
    /*public void getFaqtList() {
        try {

            requestParametersbean.setStart_limit();

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACNEWFAQ.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ACListFAQsMainActivity.this, getApplicationContext(), URLs. ACNEWFAQ, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACNEWFAQ.getScrenIndex(), ACListFAQsMainActivity.class.getClass());


            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }*/

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

    public void handlerError(Responcebean responcebean)
    {
        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals(""))
        {
            if(requestParametersbean.getStart_limit()==0)
            {
                baseTextview_error.setText(getResources().getString(R.string.no_data_founds));
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            }
            else
            {

            }
        }
        else
        {
            if(requestParametersbean.getStart_limit()==0)
            {
                baseTextview_error.setText(responcebean.getErrorMessage());
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            }
            else
            {

            }
        }
    }
}