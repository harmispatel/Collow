package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.PollsOptionAdapter;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.PollOptionbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
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
 * Created by harmis on 1/2/17.
 */

public class PollDetailsActivity extends BaseActivity implements SetupViewInterface {

    public static ArrayList<PollOptionbean> pollOptionbeanArrayList = new ArrayList<>();
    BaseTextview baseTextview_left_side = null, baseTextview_right_side = null;
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageView_plus_icon_only;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    ImageView imageview_right_foursquare = null;
    ImageView imageview_circle = null;
    RecyclerView recyclerView_grid_classfied_images = null;

    int current_start = 0;
    BaseTextview textview_pools_question = null;
    MyButtonView button_polls_submit = null;
    RecyclerView recyclerView_options = null;
    RelativeLayout relativeLayout_upload_options = null;
    PollsOptionAdapter pollsOptionsAdapter = null;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    private boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(PollDetailsActivity.this, getApplicationContext(), getIntent().getExtras(), PollDetailsActivity.class.getClass());
            commonSession = new CommonSession(PollDetailsActivity.this);
            Bundle bundle = getIntent().getExtras();

            pollOptionbeanArrayList.clear();
            //create default two options

            PollOptionbean pollOptionbean1 = new PollOptionbean();
            pollOptionbean1.setHint_title(getResources().getString(R.string.option) + "1");
            pollOptionbean1.setNeedToCreateOptions(true);


            PollOptionbean pollOptionbean2 = new PollOptionbean();
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
            baseTextview_header_title.setText(getResources().getString(R.string.polls));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

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

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }


    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.polls_detail, null);


           /* imageView_plus_icon_only = (ImageView) view_home.findViewById(R.id.imageview_upload_image_plus_icon_image);
            relativeLayout_upload_image_plus_view = (RelativeLayout) view_home.findViewById(R.id.relativelayout_upload_create_polls_images);


            baseEdittext_enter_questions= (BaseEdittext) view_home.findViewById(R.id.edittext_enter_poll_question);
            relativeLayout_upload_options= (RelativeLayout) view_home.findViewById(R.id.relativelayout_upload_create_polls_options);*/
            textview_pools_question = (BaseTextview) view_home.findViewById(R.id.textview_enter_poll_question);
            button_polls_submit = (MyButtonView) view_home.findViewById(R.id.button_polls_submit);
            recyclerView_options = (RecyclerView) view_home.findViewById(R.id.my_recycler_view_polls_options);
            recyclerView_options.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(20);
            recyclerView_options.addItemDecoration(dividerItemDecoration);
            // use a linear layout manager
            recyclerView_options.setLayoutManager(mLayoutManager);
            pollsOptionsAdapter = new PollsOptionAdapter();
            recyclerView_options.setAdapter(pollsOptionsAdapter);


            frameLayout_container.addView(view_home);
            imageview_circle = (ImageView) view_home.findViewById(R.id.imageview_create_polls_circle_selected);

            //recyclerView_grid_classfied_images = (RecyclerView) view_home.findViewById(R.id.recycler_view_grid_create_polls);
            // recyclerView_grid_classfied_images.setHasFixedSize(true);
            // Disabled nested scrolling since Parent scrollview will scroll the content.
            // recyclerView_grid_classfied_images.setNestedScrollingEnabled(false);
            //StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
            // recyclerView_grid_classfied_images.setLayoutManager(gaggeredGridLayoutManager);


           /* int spanCount = 3; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = false;
            recyclerView_grid_classfied_images.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


            uploadImageAdapter = new ACCreatePollsActivity.UploadImageAdapter(this);
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
*/


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    private void setupEvents() {

    }


    @Override
    public void setupUI(Responcebean responcebean) {
        try {
            if (responcebean.isServiceSuccess()) {

                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                    //parse here data of following

                    ArrayList<PollOptionbean> pollsbean_local = new ArrayList<>();
                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.Activities)) {
                        JSONArray jsonArray_events = jsonObject_main.getJSONArray(JSONCommonKeywords.Activities);
                        for (int i = 0; i < jsonArray_events.length(); i++) {

                            JSONObject jsonObject_single_event = jsonArray_events.getJSONObject(i);
                            PollOptionbean pollsoptionbean = new PollOptionbean();
                            pollsbean_local.add(pollsoptionbean);
                        }

                        if (current_start == 0) {
                            pollOptionbeanArrayList = pollsbean_local;
                            pollsOptionsAdapter.notifyDataSetChanged();

                        } else {

                            int start = pollOptionbeanArrayList.size();

                            for (int i = 0; i < pollsbean_local.size(); i++) {

                                pollOptionbeanArrayList.add(start, pollsbean_local.get(i));
                                pollsOptionsAdapter.notifyItemInserted(pollOptionbeanArrayList.size());
                                start++;

                            }


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
