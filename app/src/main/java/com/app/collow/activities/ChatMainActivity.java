package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.ChatAdapter;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Chatbean;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by harmis on 27/2/17.
 */

public class ChatMainActivity extends BaseActivity implements SetupViewInterface {

    public static ChatAdapter chatAdapter = null;
    public static ArrayList<Chatbean> chatbeanArrayList = new ArrayList<>();
    public static RecyclerView mRecyclerView;
    public static ChatMainActivity chatMainActivity = null;
    public static BaseTextview baseTextview_error = null;
    protected Handler handler;
    View view_home = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    FloatingActionButton floatingActionButton_create_chat = null;
    CommunityAccessbean communityAccessbean = null;
    String communityID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            chatMainActivity = this;
            retryParameterbean = new RetryParameterbean(ChatMainActivity.this, getApplicationContext(), getIntent().getExtras(), ChatMainActivity.class.getClass());
            commonSession = new CommonSession(ChatMainActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            //getNewsListing();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.chat));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });


            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    MyUtils.openCommunityMenu(ChatMainActivity.this, communityID, baseTextview_header_title.getText().toString(), communityAccessbean);


                }
            });

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.chat_main, null);

            Chatbean chatbean = new Chatbean();
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            chatbeanArrayList.add(chatbean);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);
            floatingActionButton_create_chat = (FloatingActionButton) view_home.findViewById(R.id.fab_create_new_chat);


            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);


            chatAdapter = new ChatAdapter(mRecyclerView, this);
            mRecyclerView.setAdapter(chatAdapter);
            frameLayout_container.addView(view_home);


            chatAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            current_start += 10;

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
        try {
            floatingActionButton_create_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatMainActivity.this, CreateChatMainActivity.class);

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


    @Override
    public void setupUI(Responcebean responcebean) {

        try {
            if (responcebean.isServiceSuccess()) {

                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                    //parse here data of following


                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.NewsLists)) {

                        ArrayList<Chatbean> chatbeanArrayList_local = new ArrayList<>();
                        JSONArray jsonArray_news_list = jsonObject_main.getJSONArray(JSONCommonKeywords.NewsLists);
                        for (int i = 0; i < jsonArray_news_list.length(); i++) {

                            Chatbean chatbean = new Chatbean();
                            JSONObject jsonObject_single = jsonArray_news_list.getJSONObject(i);

                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Chatid)) {
                                chatbean.setChat_id(jsonObject_single.getString(JSONCommonKeywords.Chatid));

                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Chat)) {
                                chatbean.setChat_title(jsonObject_single.getString(JSONCommonKeywords.Chat));

                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Description)) {
                                chatbean.setChat_description(jsonObject_single.getString(JSONCommonKeywords.Description));

                            }

                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.ChatDate)) {
                                chatbean.setChat_time(jsonObject_single.getString(JSONCommonKeywords.ChatDate));

                            }


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.UserName)) {
                                chatbean.setChat_username(jsonObject_single.getString(JSONCommonKeywords.UserName));

                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.CommunityName)) {
                                chatbean.setCommunityName(jsonObject_single.getString(JSONCommonKeywords.CommunityName));

                            }


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.ProfilePic)) {
                                chatbean.setChat_userprofilepic(jsonObject_single.getString(JSONCommonKeywords.ProfilePic));

                            }

                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.ActivityId)) {
                                chatbean.setActivityID(jsonObject_single.getString(JSONCommonKeywords.ActivityId));

                            }

                            SocialOptionbean socialOptionbean = CommonMethods.getSocialOptionbean(jsonObject_single);

                            if (socialOptionbean != null) {
                                chatbean.setSocialOptionbean(socialOptionbean);
                            }


                            if (jsonObject_single.has(JSONCommonKeywords.isLiked)) {


                                String islikedfeedString = jsonObject_single.getString(JSONCommonKeywords.isLiked);
                                if (islikedfeedString == null || islikedfeedString.equals("")) {
                                    chatbean.setLiked(false);
                                } else if (islikedfeedString.equals("1")) {
                                    chatbean.setLiked(true);

                                } else {
                                    chatbean.setLiked(false);

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


                            chatbean.setStringArrayList_fileURLs(files_of_news);


                            chatbean.setPosition(i);
                            chatbeanArrayList_local.add(chatbean);
                        }

                        if (current_start == 0) {
                            chatbeanArrayList = chatbeanArrayList_local;
                            chatAdapter.notifyDataSetChanged();

                        } else {

                            int start = chatbeanArrayList.size();

                            for (int i = 0; i < chatbeanArrayList_local.size(); i++) {

                                Chatbean chatbean = chatbeanArrayList_local.get(i);
                                chatbean.setPosition(start);
                                chatbeanArrayList.add(start, chatbean);
                                chatAdapter.notifyItemInserted(chatbeanArrayList.size());

                                start++;

                            }
                            chatAdapter.setLoaded();

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
        try {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                if (current_start == 0) {
                    baseTextview_error.setText(getResources().getString(R.string.no_news_found));
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
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }
    }
}
