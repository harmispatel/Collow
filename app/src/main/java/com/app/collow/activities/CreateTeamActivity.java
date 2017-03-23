package com.app.collow.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;

public class CreateTeamActivity extends AppCompatActivity {
    BaseEdittext edittext_createteam_title;
    BaseEdittext edittext_createteam_description;
    BaseEdittext edittext_createteam_price;
    BaseTextview textview_createteam_selectcategory;
    BaseTextview textview_createteam_uploadimages;

    ImageView imageview_createteam_selectcategory;
    ImageView imageview_createteam_uploadimages;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        findViewByIds();
    }
    private  void findViewByIds(){
        edittext_createteam_title=(BaseEdittext)findViewById(R.id.edittext_creatteam_title);
        edittext_createteam_description=(BaseEdittext)findViewById(R.id.edittext_createteam_description);
        edittext_createteam_price=(BaseEdittext)findViewById(R.id.edittext_createteam_price);
        textview_createteam_selectcategory=(BaseTextview) findViewById(R.id.textview_createteam_selectcategory);
        textview_createteam_uploadimages=(BaseTextview) findViewById(R.id.textview_createteam_uploadimages);
        imageview_createteam_selectcategory=(ImageView) findViewById(R.id.imageview_createteam_selectcategory);
        imageview_createteam_uploadimages=(ImageView) findViewById(R.id.imageview_createteam_uploadimages);
    }
}
