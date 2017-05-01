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
 * Created by Harmis on 07/02/17.
 */

public class SelectCommunityTypeActivity extends AppCompatActivity {

    ArrayAdapter<String> myAdapter;
    ListView listView;
    List<String> arStringList_types=new ArrayList<>();
    public HashMap<String,CommunityTypebean> stringCommunityTypebeanHashMap=new HashMap<>();
    int indexFrom=0;
    CommonSession commonSession=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_state_main);
        commonSession=new CommonSession(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //===========================
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            indexFrom=bundle.getInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE);

        }
        stringCommunityTypebeanHashMap=SplashActvitiy.stringCommunityTypebeanHashMap_globle;
        if(stringCommunityTypebeanHashMap.size()==0)
        {
            getCommunityTypesData();
        }
        else
        {
            handleData();
        }



    }


    public void handleData()
    {
        Set<String> keys = stringCommunityTypebeanHashMap.keySet();





// To get all key: value
        for(String keya: keys){
            arStringList_types.add(stringCommunityTypebeanHashMap.get(keya).getTypeName());

        }
        Collections.sort(arStringList_types);


        listView = (ListView) findViewById(R.id.listview);
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arStringList_types);
        listView.setAdapter(myAdapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if(indexFrom== ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex())
                {
                    if(FollowingActivity.baseTextview_community_type_right_menu!=null)
                    {
                        FollowingActivity.baseTextview_community_type_right_menu.setText(String.valueOf(arStringList_types.get(arg2)));

                        Set<String> keys = stringCommunityTypebeanHashMap.keySet();

                        for(String keya: keys){



                            CommunityTypebean communityTypebean=stringCommunityTypebeanHashMap.get(keya);

                            if(communityTypebean.getTypeName().equals(String.valueOf(arStringList_types.get(arg2))))
                            {
                                FollowingActivity.baseTextview_community_type_right_menu.setTag(communityTypebean);
                                break;
                            }

                        }

                    }
                }

                else  if(indexFrom== ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex())
                {
                    if(CommunitySearchByNameActivity.baseTextview_community_type_right_menu!=null)
                    {
                        CommunitySearchByNameActivity.baseTextview_community_type_right_menu.setText(String.valueOf(arStringList_types.get(arg2)));

                        Set<String> keys = stringCommunityTypebeanHashMap.keySet();

                        for(String keya: keys){



                            CommunityTypebean communityTypebean=stringCommunityTypebeanHashMap.get(keya);

                            if(communityTypebean.getTypeName().equals(String.valueOf(arStringList_types.get(arg2))))
                            {
                                CommunitySearchByNameActivity.baseTextview_community_type_right_menu.setTag(communityTypebean);
                                break;
                            }

                        }

                    }
                }

                else  if(indexFrom== ScreensEnums.SEARCH_BY_COMMMUNITY_LATLONG.getScrenIndex())
                {
                    if(CommunitySearchResultsActivity.baseTextview_community_type_right_menu!=null)
                    {
                        CommunitySearchResultsActivity.baseTextview_community_type_right_menu.setText(String.valueOf(arStringList_types.get(arg2)));

                        Set<String> keys = stringCommunityTypebeanHashMap.keySet();

                        for(String keya: keys){



                            CommunityTypebean communityTypebean=stringCommunityTypebeanHashMap.get(keya);

                            if(communityTypebean.getTypeName().equals(String.valueOf(arStringList_types.get(arg2))))
                            {
                                CommunitySearchResultsActivity.baseTextview_community_type_right_menu.setTag(communityTypebean);
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
        arStringList_types.clear();
        if (charText.length() == 0) {
            //==========================
            Set<String> keys = stringCommunityTypebeanHashMap.keySet();
// To get all key: value
            for(String keya: keys){
                arStringList_types.add(stringCommunityTypebeanHashMap.get(keya).getTypeName());
            }
        }
        else
        {

            Set<String> keys = stringCommunityTypebeanHashMap.keySet();
// To get all key: value
            for(String keya: keys){
                CommunityTypebean  communityTypebean=stringCommunityTypebeanHashMap.get(keya);





                if (communityTypebean.getTypeName().toLowerCase().startsWith(charText)||keya.toLowerCase().contains(charText))
                {
                    arStringList_types.add(stringCommunityTypebeanHashMap.get(keya).getTypeName());
                }
            }




        }
        Collections.sort(arStringList_types);

        myAdapter.notifyDataSetChanged();
    }

    public void getCommunityTypesData() {

        try {

            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {

                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.typeList)) {
                                    JSONArray jsonArray_typelisting = jsonObject_main.getJSONArray(JSONCommonKeywords.typeList);
                                    for (int i = 0; i < jsonArray_typelisting.length(); i++) {

                                        JSONObject jsonObject_single_type = jsonArray_typelisting.getJSONObject(i);

                                        CommunityTypebean communityTypebean = new CommunityTypebean();

                                        if (jsonObject_single_type.has(JSONCommonKeywords.TypeId)) {
                                            communityTypebean.setTypeID(jsonObject_single_type.getString(JSONCommonKeywords.TypeId));

                                        }

                                        if (jsonObject_single_type.has(JSONCommonKeywords.TypeName)) {
                                            communityTypebean.setTypeName(jsonObject_single_type.getString(JSONCommonKeywords.TypeName));

                                        }

                                        SplashActvitiy.stringCommunityTypebeanHashMap_globle.put(communityTypebean.getTypeID(), communityTypebean);


                                    }
                                    stringCommunityTypebeanHashMap=SplashActvitiy.stringCommunityTypebeanHashMap_globle;
                                    handleData();
                                }


                            } else {

                            }




                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GETCOMMUNITYTYPES, null, ScreensEnums.PROPERTY_TYPE_GET.getScrenIndex(), SignInActivity.class.getClass());

            passParameterbean.setNoNeedToDisplayLoadingDialog(true);

            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {

        }

    }

}