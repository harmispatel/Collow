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
import com.app.collow.beans.CommunityTypebean;
import com.app.collow.beans.EventTypebean;
import com.app.collow.beans.Eventbean;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Harmis on 23/02/17.
 */

public class SelectEventTypeActivity extends AppCompatActivity {

    ArrayAdapter<String> myAdapter;
    ListView listView;
    List<String> arStringList_event_types=new ArrayList<>();
    int indexFrom=0;
    CommonSession commonSession=null;

    public  HashMap<String,EventTypebean> stringEventTypebeanHashMap=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_state_main);
        try {
            commonSession=new CommonSession(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            //==========================
            Bundle bundle=getIntent().getExtras();
            if(bundle!=null)
            {

                indexFrom=bundle.getInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE);

            }
            stringEventTypebeanHashMap=SplashActvitiy.stringEventTypebeanHashMap_globle;
            if(stringEventTypebeanHashMap.size()==0)
            {
                getEventTypeFromServer();
            }
            else
            {
                handleData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void handleData()
    {
        Set<String> keys = stringEventTypebeanHashMap.keySet();


// To get all key: value
        for(String keya: keys){
            arStringList_event_types.add(stringEventTypebeanHashMap.get(keya).getTypeName());

        }
        Collections.sort(arStringList_event_types);


        listView = (ListView) findViewById(R.id.listview);
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arStringList_event_types);
        listView.setAdapter(myAdapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if(indexFrom==ScreensEnums.ACNEWEVENT.getScrenIndex())
                {
                    CreateNewEventActivity.baseTextview_event_type.setText(String.valueOf(arStringList_event_types.get(arg2)));

                    Set<String> keys = stringEventTypebeanHashMap.keySet();

                    for(String keya: keys){



                        EventTypebean eventTypebean=stringEventTypebeanHashMap.get(keya);

                        if(eventTypebean.getTypeName().equals(String.valueOf(arStringList_event_types.get(arg2))))
                        {
                            CreateNewEventActivity.baseTextview_event_type.setTag(eventTypebean);
                            break;
                        }

                    }
                }
                else if(indexFrom==ScreensEnums.EVENT.getScrenIndex())
                {
                    UserEventMainActivity.baseTextview_event_type.setText(String.valueOf(arStringList_event_types.get(arg2)));

                    Set<String> keys = stringEventTypebeanHashMap.keySet();

                    for(String keya: keys){



                        EventTypebean eventTypebean=stringEventTypebeanHashMap.get(keya);

                        if(eventTypebean.getTypeName().equals(String.valueOf(arStringList_event_types.get(arg2))))
                        {
                            UserEventMainActivity.baseTextview_event_type.setTag(eventTypebean);
                            break;
                        }

                    }
                }
                else if(indexFrom==ScreensEnums.ACGETEVENTS.getScrenIndex())
                {
                    UserEventMainActivity.baseTextview_event_type.setText(String.valueOf(arStringList_event_types.get(arg2)));

                    Set<String> keys = stringEventTypebeanHashMap.keySet();

                    for(String keya: keys){



                        EventTypebean eventTypebean=stringEventTypebeanHashMap.get(keya);

                        if(eventTypebean.getTypeName().equals(String.valueOf(arStringList_event_types.get(arg2))))
                        {
                            UserEventMainActivity.baseTextview_event_type.setTag(eventTypebean);
                            break;
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
        searchView.setQueryHint(getResources().getString(R.string.search));
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
        arStringList_event_types.clear();
        if (charText.length() == 0) {
            //==========================
            Set<String> keys = stringEventTypebeanHashMap.keySet();
// To get all key: value
            for(String keya: keys){
                arStringList_event_types.add(stringEventTypebeanHashMap.get(keya).getTypeName());
            }
        }
        else
        {

            Set<String> keys = stringEventTypebeanHashMap.keySet();
// To get all key: value
            for(String keya: keys){
                EventTypebean  eventTypebean=stringEventTypebeanHashMap.get(keya);





                if (eventTypebean.getTypeName().toLowerCase().startsWith(charText)||keya.toLowerCase().contains(charText))
                {
                    arStringList_event_types.add(stringEventTypebeanHashMap.get(keya).getTypeName());
                }
            }




        }
        Collections.sort(arStringList_event_types);

        myAdapter.notifyDataSetChanged();
    }






    // this method is used for getinterest
    public void getEventTypeFromServer() {
        try {


            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {

                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, "EventTypes")) {
                                    JSONArray jsonArray_interests = jsonObject_main.getJSONArray("EventTypes");
                                    for (int i = 0; i < jsonArray_interests.length(); i++) {

                                        JSONObject jsonObject_single = jsonArray_interests.getJSONObject(i);

                                        EventTypebean eventTypebean = new EventTypebean();

                                        if (jsonObject_single.has(JSONCommonKeywords.id)) {
                                            eventTypebean.setTypeID(jsonObject_single.getString(JSONCommonKeywords.id));

                                        }

                                        if (jsonObject_single.has(JSONCommonKeywords.EventTypeName)) {
                                            eventTypebean.setTypeName(jsonObject_single.getString(JSONCommonKeywords.EventTypeName));

                                        }

                                        SplashActvitiy.stringEventTypebeanHashMap_globle.put(eventTypebean.getTypeID(), eventTypebean);



                                    }

                                    stringEventTypebeanHashMap=SplashActvitiy.stringEventTypebeanHashMap_globle;
                                    handleData();
                                }


                            } else {

                            }




                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonMethods.customToastMessage(e.getMessage(), SelectEventTypeActivity.this);

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GETEVENTTYPES, null, ScreensEnums.EVENT_TYPES.getScrenIndex(), SignInActivity.class.getClass());


            new RequestToServer(passParameterbean, null).execute();


        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }
    }


}