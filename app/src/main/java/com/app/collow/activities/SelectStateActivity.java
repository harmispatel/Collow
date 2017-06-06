package com.app.collow.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.collow.R;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.Statesbean;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SelectStateActivity extends AppCompatActivity {

    public HashMap<String, Statesbean> stringStatesbeanHashMap = new HashMap<>();
    ArrayAdapter<String> myAdapter;
    ListView listView;
    List<String> arStringList_states = new ArrayList<>();
    int indexFrom = 0;
    CommonSession commonSession = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.select_state_main);
            commonSession = new CommonSession(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                indexFrom = bundle.getInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE);
            }
            stringStatesbeanHashMap = SplashActvitiy.stringStatesbeanHashMap_globle;
            if (stringStatesbeanHashMap.size() == 0) {
                getStatesListing();
            } else {
                handleData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void handleData() {
        Set<String> keys = stringStatesbeanHashMap.keySet();

// To get all key: value
        for (String keya : keys) {
            arStringList_states.add(stringStatesbeanHashMap.get(keya).getStateName());
        }
        Collections.sort(arStringList_states);


        listView = (ListView) findViewById(R.id.listview);
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arStringList_states);
        listView.setAdapter(myAdapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (indexFrom == ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex()) {
                    if (FollowingActivity.baseTextview_state_right_menu != null) {
                        FollowingActivity.baseTextview_state_right_menu.setText(String.valueOf(arStringList_states.get(arg2)));

                        Set<String> keys = stringStatesbeanHashMap.keySet();

                        for (String keya : keys) {


                            Statesbean statesbean = stringStatesbeanHashMap.get(keya);

                            if (statesbean.getStateName().equals(String.valueOf(arStringList_states.get(arg2)))) {
                                FollowingActivity.baseTextview_state_right_menu.setTag(statesbean);

                                break;
                            }

                        }

                    }
                } else if (indexFrom == ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex()) {
                    if (CommunitySearchByNameActivity.baseTextview_state_right_menu != null) {
                        CommunitySearchByNameActivity.baseTextview_state_right_menu.setText(String.valueOf(arStringList_states.get(arg2)));

                        Set<String> keys = stringStatesbeanHashMap.keySet();

                        for (String keya : keys) {


                            Statesbean statesbean = stringStatesbeanHashMap.get(keya);

                            if (statesbean.getStateName().equals(String.valueOf(arStringList_states.get(arg2)))) {
                                CommunitySearchByNameActivity.baseTextview_state_right_menu.setTag(statesbean);

                                break;
                            }

                        }

                    }
                } else if (indexFrom == ScreensEnums.SEARCH_BY_COMMMUNITY_LATLONG.getScrenIndex()) {
                    if (CommunitySearchResultsActivity.baseTextview_state_right_menu != null) {
                        CommunitySearchResultsActivity.baseTextview_state_right_menu.setText(String.valueOf(arStringList_states.get(arg2)));

                        Set<String> keys = stringStatesbeanHashMap.keySet();

                        for (String keya : keys) {


                            Statesbean statesbean = stringStatesbeanHashMap.get(keya);

                            if (statesbean.getStateName().equals(String.valueOf(arStringList_states.get(arg2)))) {
                                CommunitySearchResultsActivity.baseTextview_state_right_menu.setTag(statesbean);

                                break;
                            }

                        }

                    }
                } else {
                    if (EditProfileActivity.baseTextview_state != null) {
                        EditProfileActivity.baseTextview_state.setText(String.valueOf(arStringList_states.get(arg2)));

                        Set<String> keys = stringStatesbeanHashMap.keySet();

                        for (String keya : keys) {


                            Statesbean statesbean = stringStatesbeanHashMap.get(keya);

                            if (statesbean.getStateName().equals(String.valueOf(arStringList_states.get(arg2)))) {
                                EditProfileActivity.baseTextview_state.setTag(String.valueOf(statesbean.getStateCode()));


                                break;
                            }

                        }

                    }
                }


                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_state_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);

                return false;
            }
        });
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Filter Class
    public void filter(String charText) {
        arStringList_states.clear();
        if (charText.length() == 0) {
            //==========================
            Set<String> keys = stringStatesbeanHashMap.keySet();
// To get all key: value
            for (String keya : keys) {
                arStringList_states.add(stringStatesbeanHashMap.get(keya).getStateName());
            }
        } else {

            Set<String> keys = stringStatesbeanHashMap.keySet();
// To get all key: value
            for (String keya : keys) {
                if (keya.toLowerCase().startsWith(charText) || keya.toLowerCase().contains(charText)) {
                    arStringList_states.add(stringStatesbeanHashMap.get(keya).getStateName());
                }
            }


        }
        Collections.sort(arStringList_states);

        myAdapter.notifyDataSetChanged();
    }


    // this method is used for login user
    public void getStatesListing() {
        try {


            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (jsonObject_main.has(JSONCommonKeywords.StateList)) {
                                JSONArray jsonArray_statelist = jsonObject_main.getJSONArray(JSONCommonKeywords.StateList);
                                if (jsonArray_statelist == null || jsonArray_statelist.equals("")) {

                                } else {
                                    if (jsonArray_statelist.length() != 0) {
                                        for (int i = 0; i < jsonArray_statelist.length(); i++) {

                                            JSONObject jsonObject_single = jsonArray_statelist.getJSONObject(i);

                                            String statecode = jsonObject_single.getString(JSONCommonKeywords.stateCode);
                                            String statename = jsonObject_single.getString(JSONCommonKeywords.StateName);
                                            Statesbean statesbean = new Statesbean();
                                            statesbean.setStateCode(statecode);
                                            statesbean.setStateName(statename);
                                            SplashActvitiy.stringStatesbeanHashMap_globle.put(statecode, statesbean);


                                        }

                                        stringStatesbeanHashMap = SplashActvitiy.stringStatesbeanHashMap_globle;


                                        handleData();

                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GET_STATES_LISTING, null, ScreensEnums.LOGIN.getScrenIndex(), SignInActivity.class.getClass());


            new RequestToServer(passParameterbean, null).execute();


        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }
    }

}


