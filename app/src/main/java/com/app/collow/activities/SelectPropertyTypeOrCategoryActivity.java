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
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Harmis on 06/02/17.
 */

public class SelectPropertyTypeOrCategoryActivity extends AppCompatActivity {

    ArrayAdapter<String> myAdapter;
    ListView listView;
    List<String> arStringList_items=new ArrayList<>();
    public HashMap<String,String> stringStringHashMap_data=new HashMap<>();
    int index=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_state_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //==========================
        Bundle bundle=getIntent().getExtras();

        if(bundle!=null)
        {
            index=bundle.getInt(BundleCommonKeywords.KEY_CLASSIFIED_PROPERTY_TYPE_OR_CATEGORY);
            stringStringHashMap_data= (HashMap<String, String>) bundle.getSerializable(BundleCommonKeywords.KEY_CLASSIFIED_PROPERTY_TYPE_OR_CATEGORY_DATA);
            CommonMethods.displayLog("size",stringStringHashMap_data.toString());


        }
        Set<String> keys = stringStringHashMap_data.keySet();





// To get all key: value
        for(String keya: keys){


            arStringList_items.add(stringStringHashMap_data.get(keya));


        }
        Collections.sort(arStringList_items);


        listView = (ListView) findViewById(R.id.listview);
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arStringList_items);
        listView.setAdapter(myAdapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {


                if(index== ScreensEnums.PROPERTY_TYPE_GET.getScrenIndex())
                {

                    if(CreateClassifiedActivity.baseTextview_property_type!=null)
                    {
                        CreateClassifiedActivity.baseTextview_property_type.setText(String.valueOf(arStringList_items.get(arg2)));

                    }
                    Set<String> keys = stringStringHashMap_data.keySet();

                    for(String keya: keys){



                        String s=stringStringHashMap_data.get(keya);

                        if(s.equals(String.valueOf(arStringList_items.get(arg2))))
                        {
                            CreateClassifiedActivity.baseTextview_property_type.setTag(String.valueOf(keya));


                            break;
                        }

                    }

                    finish();

                }
                else if(index==ScreensEnums.PROPERTY_CATEGORY_GET.getScrenIndex())
                {
                    if(CreateClassifiedActivity.baseTextview_property_category!=null)
                    {
                        CreateClassifiedActivity.baseTextview_property_category.setText(String.valueOf(arStringList_items.get(arg2)));

                    }
                    Set<String> keys = stringStringHashMap_data.keySet();

                    for(String keya: keys){



                        String s=stringStringHashMap_data.get(keya);

                        if(s.equals(String.valueOf(arStringList_items.get(arg2))))
                        {
                            CreateClassifiedActivity.baseTextview_property_category.setTag(String.valueOf(keya));


                            break;
                        }

                    }

                    finish();
                }




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
        arStringList_items.clear();
        if (charText.length() == 0) {
            //==========================
            Set<String> keys = stringStringHashMap_data.keySet();
// To get all key: value
            for(String keya: keys){
                arStringList_items.add(stringStringHashMap_data.get(keya));
            }
        }
        else
        {

            Set<String> keys = stringStringHashMap_data.keySet();
// To get all key: value
            for(String keya: keys){
                if (keya.toLowerCase().startsWith(charText)||keya.toLowerCase().contains(charText))
                {
                    arStringList_items.add(stringStringHashMap_data.get(keya));
                }
            }




        }
        Collections.sort(arStringList_items);

        myAdapter.notifyDataSetChanged();
    }



}