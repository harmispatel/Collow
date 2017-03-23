package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.ACCommunityFeatureAdapter;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACCommunityFeaturebean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by harmis on 23/2/17.
 */

public class FeatureMainActivity extends BaseActivity implements SetupViewInterface {

    public static ArrayList<ACCommunityFeaturebean> accommunityfeaturebeanArrayList = new ArrayList<>();
    protected Handler handler;
    View view_home = null;
    ACCommunityFeatureAdapter acCommunityAdapter = null;
    BaseTextview baseTextview_header_title = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_left_side = null;

    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null;
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
            retryParameterbean = new RetryParameterbean(FeatureMainActivity.this, getApplicationContext(), null, FeatureMainActivity.class.getClass());
            commonSession = new CommonSession(FeatureMainActivity.this);

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
            baseTextview_header_title.setText(getResources().getString(R.string.community_features));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageView_delete = (ImageView) toolbar_header.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.GONE);
            imageView_view = (ImageView) toolbar_header.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.GONE);
            imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.GONE);
            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.GONE);
            //baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

            baseTextview_left_side.setText(getResources().getString(R.string.cancel));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);

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
            view_home = getLayoutInflater().inflate(R.layout.accommunityfeatures_main, null);

            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            ACCommunityFeaturebean communitybean_walkscore = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_greatschool = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_classified = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_chat = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_forms = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_addsales = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_socialmediafeed = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_maintanancerequest = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_packagenotificaion = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_parkingpass = new ACCommunityFeaturebean();
            ACCommunityFeaturebean communitybean_rentpayment = new ACCommunityFeaturebean();


            communitybean_walkscore.setTitle(getResources().getString(R.string.walkscore));
            communitybean_greatschool.setTitle(getResources().getString(R.string.greatschool));
            communitybean_classified.setTitle(getResources().getString(R.string.classified));
            communitybean_chat.setTitle(getResources().getString(R.string.chat));
            communitybean_forms.setTitle(getResources().getString(R.string.forms));
            communitybean_addsales.setTitle(getResources().getString(R.string.addsales));
            communitybean_socialmediafeed.setTitle(getResources().getString(R.string.socialmediafeed));
            communitybean_maintanancerequest.setTitle(getResources().getString(R.string.maintanancerequest));
            communitybean_packagenotificaion.setTitle(getResources().getString(R.string.packagenotificaion));
            communitybean_parkingpass.setTitle(getResources().getString(R.string.parkingpass));
            communitybean_rentpayment.setTitle(getResources().getString(R.string.rentpayment));

            accommunityfeaturebeanArrayList.add(communitybean_walkscore);
            accommunityfeaturebeanArrayList.add(communitybean_greatschool);
            accommunityfeaturebeanArrayList.add(communitybean_classified);
            accommunityfeaturebeanArrayList.add(communitybean_chat);
            accommunityfeaturebeanArrayList.add(communitybean_forms);
            accommunityfeaturebeanArrayList.add(communitybean_addsales);
            accommunityfeaturebeanArrayList.add(communitybean_socialmediafeed);
            accommunityfeaturebeanArrayList.add(communitybean_maintanancerequest);
            accommunityfeaturebeanArrayList.add(communitybean_packagenotificaion);
            accommunityfeaturebeanArrayList.add(communitybean_parkingpass);
            accommunityfeaturebeanArrayList.add(communitybean_rentpayment);


            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            acCommunityAdapter = new ACCommunityFeatureAdapter(FeatureMainActivity.this, mRecyclerView);
            mRecyclerView.setAdapter(acCommunityAdapter);
            frameLayout_container.addView(view_home);

            // getCommunityFeatures();


            acCommunityAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
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
       /* try {
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
*/

    }


    // this method is used for login user
   /* public void getCommunityFeatures() {
        try {


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACCOMMUNITYFEATURES.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, FeatureMainActivity.this, getApplicationContext(), URLs.ACCOMMUNITYFEATURES, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACCOMMUNITYFEATURES.getScrenIndex(), FeatureMainActivity.class.getClass());


            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }
*/
    @Override
    public void setupUI(Responcebean responcebean) {

        try {
            if (responcebean.isServiceSuccess()) {

                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                    //parse here data of following

                    ArrayList<ACCommunityFeaturebean> acCommunityBeen_local = new ArrayList<>();
                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.Activities)) {
                        JSONArray jsonArray_community = jsonObject_main.getJSONArray(JSONCommonKeywords.Activities);
                        for (int i = 0; i < jsonArray_community.length(); i++) {

                            JSONObject jsonObject_single_community = jsonArray_community.getJSONObject(i);
                            ACCommunityFeaturebean acCommunityBean = new ACCommunityFeaturebean();
                            acCommunityBeen_local.add(acCommunityBean);
                        }

                        if (current_start == 0) {
                            accommunityfeaturebeanArrayList = acCommunityBeen_local;
                            acCommunityAdapter.notifyDataSetChanged();

                        } else {

                            int start = accommunityfeaturebeanArrayList.size();

                            for (int i = 0; i < acCommunityBeen_local.size(); i++) {

                                accommunityfeaturebeanArrayList.add(start, acCommunityBeen_local.get(i));
                                acCommunityAdapter.notifyItemInserted(accommunityfeaturebeanArrayList.size());
                                start++;

                            }
                            acCommunityAdapter.setLoaded();

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
                baseTextview_error.setText(getResources().getString(R.string.no_data_founds));
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
