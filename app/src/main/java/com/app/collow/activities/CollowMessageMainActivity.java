package com.app.collow.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.CollowMessageAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.ACFollowersbean;
import com.app.collow.beans.CollowMessagebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beansgenerate.CallowMessageBuild;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;
import com.vincentbrison.openlibraries.android.swipelistview.BaseSwipeListViewListener;
import com.vincentbrison.openlibraries.android.swipelistview.SwipeListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Harmis on 20/02/17.
 */

public class CollowMessageMainActivity extends BaseActivity implements SetupViewInterface, CollowMessageAdapter.MyAdapterCallbacks {
    public static ArrayList<CollowMessagebean> collowMessagebeanArrayList_all = new ArrayList<>();
    public static CollowMessageMainActivity collowMessageMainActivity = null;
    protected Handler handler;
    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    CommonSession commonSession = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    private SwipeListView mListView;
    private CollowMessageAdapter collowMessageAdapter;
    private BaseTextview baseTextview_error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            retryParameterbean = new RetryParameterbean(CollowMessageMainActivity.this, getApplicationContext(), getIntent().getExtras(), CollowMessageMainActivity.class.getClass());
            commonSession = new CommonSession(CollowMessageMainActivity.this);
            collowMessageMainActivity = this;
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            //getMessages();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }


    public void setupHeaderView() {
        try {
            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.inbox));
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);


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
            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    MyUtils.leftMenuUpdateDataifOpenedDrawer(CollowMessageMainActivity.this, drawerLayout, circularImageView_profile_pic, baseTextview_left_menu_unread_message, retryParameterbean);
                }

                @Override
                public void onDrawerClosed(View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            };
            drawerLayout.setDrawerListener(drawerListener);
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);
        }

    }


    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.collow_message_main, null);

            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mListView = (SwipeListView) view_home.findViewById(R.id.my_activity_my_listview);
            mListView.setSwipeOpenOnLongPress(true);
            mListView.setSwipeCloseAllItemsWhenMoveList(true);
            //mListView.setOffsetRight(300);
            CollowMessagebean collowMessagebean = new CollowMessagebean();

            collowMessagebean.setContent("Test");


            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);
            collowMessagebeanArrayList_all.add(collowMessagebean);

            collowMessageAdapter = new CollowMessageAdapter(this, this, mListView);
            mListView.setAdapter(collowMessageAdapter);

            mListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
                @Override
                public void onDismiss(int[] reverseSortedPositions) {
                    super.onDismiss(reverseSortedPositions);
                    for (int i = 0; i < reverseSortedPositions.length; i++) {
                        collowMessagebeanArrayList_all.remove(reverseSortedPositions[0]);
                    }

                    collowMessageAdapter.notifyDataSetChanged();
                }
            });


            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        CollowMessagebean collowMessagebean = (CollowMessagebean) view.getTag();

                        Intent intent = new Intent(CollowMessageMainActivity.this, CollowMessageDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN, collowMessagebean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonMethods.displayLog("Error", e.getMessage());
                    }
                }
            });


            frameLayout_container.addView(view_home);

// Create an OnScrollListener
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) { // TODO Auto-generated method stub
                    int threshold = 1;
                    int count = mListView.getCount();

                    if (scrollState == SCROLL_STATE_IDLE) {
                        if (mListView.getLastVisiblePosition() >= count
                                - threshold) {
                            // Execute LoadMoreDataTask AsyncTask

                        }
                    }


                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    // TODO Auto-generated method stub
                }

            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }


    private void setupEvents() {


    }

    public void loadMore()
    {
        CollowMessagebean collowMessagebean = new CollowMessagebean();

        collowMessagebean.setContent("Test");
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);

        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
        collowMessagebeanArrayList_all.add(collowMessagebean);
    }


    // this method is used for login user
    public void getMessages() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.MESSAGES_INBOX.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CollowMessageMainActivity.this, getApplicationContext(), URLs.GET_MESSAGES, jsonObjectGetPostParameterEachScreen, ScreensEnums.MESSAGES_INBOX.getScrenIndex(), CollowMessageMainActivity.class.getClass());


            if (current_start == 0) {
                passParameterbean.setNoNeedToDisplayLoadingDialog(false);
            } else {
                passParameterbean.setNoNeedToDisplayLoadingDialog(true);

            }
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

                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.userList)) {
                        JSONArray jsonArray_message = jsonObject_main.getJSONArray(JSONCommonKeywords.userList);
                        ArrayList<CollowMessagebean> messagebeanArrayList_local = new ArrayList<>();

                        for (int i = 0; i < jsonArray_message.length(); i++) {

                            JSONObject jsonObject_single_message = jsonArray_message.getJSONObject(i);
                            CollowMessagebean collowMessagebean1 = CallowMessageBuild.getMessagebeanFromJSON(jsonObject_single_message);
                            messagebeanArrayList_local.add(collowMessagebean1);
                        }

                        if (current_start == 0) {
                            if (collowMessageAdapter != null) {
                                collowMessageAdapter.notifyDataSetChanged();
                            }
                        } else {


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
            if (requestParametersbean.getStart_limit() == 0) {
                baseTextview_error.setText(getResources().getString(R.string.no_message_found));
                baseTextview_error.setVisibility(View.VISIBLE);

            } else {

            }
        } else {
            if (requestParametersbean.getStart_limit() == 0) {
                baseTextview_error.setText(responcebean.getErrorMessage());
                baseTextview_error.setVisibility(View.VISIBLE);

            } else {

            }
        }
    }


    private void showDialogForApproveOrReject(final ACFollowersbean acFollowersbean, final int position) {

        // custom dialog
        try {
            final Dialog dialog = new Dialog(CollowMessageMainActivity.this, R.style.MyDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow()
                    .setLayout(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


            dialog.setContentView(R.layout.ac_followers_approve_reject_dialog);
            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);
            MyButtonView myButtonView_approve = (MyButtonView) dialog.findViewById(R.id.mybuttonview_approve);
            MyButtonView myButtonView_reject = (MyButtonView) dialog.findViewById(R.id.mybuttonview_reject);

            if (!acFollowersbean.isApproved()) {
                myButtonView_approve.setVisibility(View.VISIBLE);

            } else {
                myButtonView_approve.setVisibility(View.GONE);
            }


            myButtonView_approve.setOnClickListener(new MyOnClickListener(CollowMessageMainActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {

                        requestParametersbean.setApproveOrReject(1);


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            myButtonView_reject.setOnClickListener(new MyOnClickListener(CollowMessageMainActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        requestParametersbean.setApproveOrReject(0);


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });

            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }


    }


    @Override
    public void onClickDelete(int i) {
        deleteMessageConfirm(i);
    }


    protected void deleteMessageConfirm(final int position) {
        try {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                    CollowMessageMainActivity.this);
            myAlertDialog.setTitle(getResources().getString(R.string.delete_message));
            myAlertDialog.setMessage(getResources().getString(R.string.sure_want_delete_message));

            myAlertDialog.setPositiveButton(getResources().getString(android.R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {


                            try {
                                collowMessagebeanArrayList_all.remove(position);
                                mListView.dismiss(position);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    });

            myAlertDialog.setNegativeButton(getResources().getString(android.R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {


                        }
                    });
            myAlertDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
