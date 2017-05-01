package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.DetailsPollsOptionAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.PollOptionbean;
import com.app.collow.beans.Pollsbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beansgenerate.PollsbeanBuild;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by harmis on 1/2/17.
 */

public class PollDetailsActivity extends BaseActivity implements SetupViewInterface {

    BaseTextview baseTextview_left_side = null;

    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageView_plus_icon_only;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    ImageView imageview_right_foursquare = null;
    ImageView imageview_circle = null;

    int current_start = 0;
    BaseTextview textview_pools_question = null;
    BaseTextview baseTextview_number_of_votes = null;
    MyButtonView button_polls_submit = null;
    RecyclerView recyclerView_options = null;
    DetailsPollsOptionAdapter pollsOptionsAdapter = null;

    Pollsbean pollsbean = null;
    ArrayList<PollOptionbean> pollOptionbeanArrayList = new ArrayList<>();
    String communityID = null,communityText=null;
    CommunityAccessbean communityAccessbean=null;

    boolean isVoteSentSucuesssfully = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(PollDetailsActivity.this, getApplicationContext(), getIntent().getExtras(), PollDetailsActivity.class.getClass());
            commonSession = new CommonSession(PollDetailsActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                pollsbean = (Pollsbean) bundle.getSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN);
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean= (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
                communityText=bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);

                MyUtils.markAsViewed(PollDetailsActivity.this, pollsbean.getId(), CommonKeywords.TYPE_FEED_POLL, ScreensEnums.POLLS_DETAILS_FOR_USER.getScrenIndex(),pollsbean.getPosition(),requestParametersbean,null);

            }


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

            imageview_right_foursquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openCommunityMenu(PollDetailsActivity.this,communityID,communityText,communityAccessbean);

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
        try {
            view_home = getLayoutInflater().inflate(R.layout.polls_detail, null);

            baseTextview_number_of_votes = (BaseTextview) view_home.findViewById(R.id.textview_total_number_votes);
            textview_pools_question = (BaseTextview) view_home.findViewById(R.id.textview_enter_poll_question);
            button_polls_submit = (MyButtonView) view_home.findViewById(R.id.button_polls_submit);
            recyclerView_options = (RecyclerView) view_home.findViewById(R.id.my_recycler_view_polls_options);
            recyclerView_options.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(20);
            recyclerView_options.addItemDecoration(dividerItemDecoration);
            // use a linear layout manager
            recyclerView_options.setLayoutManager(mLayoutManager);


            frameLayout_container.addView(view_home);
            imageview_circle = (ImageView) view_home.findViewById(R.id.imageview_create_polls_circle_selected);


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    private void setupEvents() {

        if (pollsbean != null) {
            pollOptionbeanArrayList = pollsbean.getPollOptionbeanArrayList();

            // button should be disable if logged already submitted answer for this poll
            if (pollsbean.isAdminForThisPoll()) {
                pollsOptionsAdapter = new DetailsPollsOptionAdapter(pollOptionbeanArrayList, ScreensEnums.ADMIN_OF_POLL.getScrenIndex());
                recyclerView_options.setAdapter(pollsOptionsAdapter);
                button_polls_submit.setVisibility(View.GONE);
            } else if (pollsbean.isVotedByLoggedUser()) {

                button_polls_submit.setVisibility(View.GONE);
                pollsOptionsAdapter = new DetailsPollsOptionAdapter(pollOptionbeanArrayList, ScreensEnums.SUBMITTED_POLL.getScrenIndex());
                recyclerView_options.setAdapter(pollsOptionsAdapter);

            } else {
                pollsOptionsAdapter = new DetailsPollsOptionAdapter(pollOptionbeanArrayList, ScreensEnums.POLLS_DETAILS_FOR_USER.getScrenIndex());
                recyclerView_options.setAdapter(pollsOptionsAdapter);
                button_polls_submit.setVisibility(View.VISIBLE);

            }

            if (CommonMethods.isTextAvailable(pollsbean.getTitle())) {
                textview_pools_question.setText(pollsbean.getTitle());
            }


            if (CommonMethods.isTextAvailable(pollsbean.getPollVotes())) {
                baseTextview_number_of_votes.setText(String.valueOf(pollsbean.getPollVotes()) + " " + getResources().getString(R.string.votes));

            }


        }

        button_polls_submit.setOnClickListener(new MyOnClickListener(PollDetailsActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {

                    //if user had already given votes at that time this flag should be on
                    if (isVoteSentSucuesssfully) {

                    } else {
                        boolean atleastOneOptionsSelected = false;
                        for (int i = 0; i < DetailsPollsOptionAdapter.pollOptionbeanArrayList.size(); i++) {

                            PollOptionbean pollOptionbean = DetailsPollsOptionAdapter.pollOptionbeanArrayList.get(i);
                            if (pollOptionbean.isSelectedOptions()) {
                                atleastOneOptionsSelected = true;
                                requestParametersbean.setPollOptionID(String.valueOf(pollOptionbean.getOptionID()));
                            } else {
                            }
                        }

                        if (!atleastOneOptionsSelected) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.select_one_options), PollDetailsActivity.this);
                        } else {
                            sendVoteRequest();


                        }

                    }


                } else {
                    showInternetNotfoundMessage();
                }

            }
        });
    }


    public void sendVoteRequest() {
        requestParametersbean.setUserId(commonSession.getLoggedUserID());
        requestParametersbean.setCommunityID(communityID);
        requestParametersbean.setPollID(pollsbean.getId());
        JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.GIVE_VOTE.getScrenIndex(), requestParametersbean);
        PassParameterbean passParameterbean = new PassParameterbean(this, PollDetailsActivity.this, getApplicationContext(), URLs.SAVEPOLLVOTE, jsonObjectGetPostParameterEachScreen, ScreensEnums.GIVE_VOTE.getScrenIndex(), PollDetailsActivity.class.getClass());
        passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);
        new RequestToServer(passParameterbean, retryParameterbean).execute();
    }

    public void submittedAnswerofQuestion() {
        pollsOptionsAdapter = new DetailsPollsOptionAdapter(pollOptionbeanArrayList, ScreensEnums.SUBMITTED_POLL.getScrenIndex());
        recyclerView_options.setAdapter(pollsOptionsAdapter);
    }

    @Override
    public void setupUI(Responcebean responcebean) {
        if (responcebean.isServiceSuccess()) {
            try {
                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {


                    if (jsonObject_main.has(JSONCommonKeywords.message)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                    } else {
                        responcebean.setErrorMessage(getResources().getString(R.string.your_votes_saved_succesfully));

                    }

                    JSONObject jsonObject_single_polls = jsonObject_main.getJSONObject(JSONCommonKeywords.Pollvote);

                    Pollsbean pollsbean = PollsbeanBuild.getPollsbean(jsonObject_single_polls, PollDetailsActivity.this);

                    if ((PollsMainActivity.pollsbeanArrayList != null)) {
                        PollsMainActivity.pollsbeanArrayList.set(pollsbean.getPosition(), pollsbean);
                        PollsMainActivity.pollsAdapter.notifyItemInserted(pollsbean.getPosition());
                    }
                    isVoteSentSucuesssfully = true;
                    button_polls_submit.setText(getResources().getString(R.string.thank_you));
                    pollOptionbeanArrayList = pollsbean.getPollOptionbeanArrayList();
                    submittedAnswerofQuestion();

                } else {
                    if (jsonObject_main.has(JSONCommonKeywords.message)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                    }


                    if (responcebean.getErrorMessage() == null) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.your_votes_saved_failed), PollDetailsActivity.this);
                    } else {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), PollDetailsActivity.this);

                    }

                }


            } catch (Exception e) {
                CommonMethods.customToastMessage(e.getMessage(), PollDetailsActivity.this);

            }

        } else {
            if (responcebean.getErrorMessage() == null) {
                CommonMethods.customToastMessage(getResources().getString(R.string.polls_created_failed), PollDetailsActivity.this);
            } else {
                CommonMethods.customToastMessage(responcebean.getErrorMessage(), PollDetailsActivity.this);

            }
        }


    }

}
