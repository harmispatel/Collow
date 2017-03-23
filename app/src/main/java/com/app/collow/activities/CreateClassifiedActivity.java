package com.app.collow.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.app.collow.R;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;

public class CreateClassifiedActivity extends AppCompatActivity {
    BaseEdittext edittext_createclassification_title=null;
    BaseEdittext edittext_createclassification_description=null;
    BaseEdittext edittext_createclassification_price=null;
    ImageView imageview_createclassification_upload=null;
    BaseTextview textview_createclassification_title=null;
    BaseTextview textview_createclassification_price=null;
    BaseTextview textview_createclassification_comments=null;
    BaseTextview textview_createclassification_selectcategory=null;
    BaseTextview textview_createclassification_uploadimage=null;
    Button button_createclassification_submit=null;
    Spinner spinner_createclassification_choosecategory=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classified);
        findviewByIds();

    }
    private void findviewByIds(){
        edittext_createclassification_title=(BaseEdittext)findViewById(R.id.edittext_createclassification_title);
        edittext_createclassification_price=(BaseEdittext)findViewById(R.id.edittext_createclassification_price);
        edittext_createclassification_description=(BaseEdittext)findViewById(R.id.edittext_createclassification_description);
        imageview_createclassification_upload=(ImageView)findViewById(R.id.imageview_createclassification_upload);
        textview_createclassification_title=(BaseTextview) findViewById(R.id.textview_createclassification_title);
        textview_createclassification_comments=(BaseTextview)findViewById(R.id.textview_createclassification_description);
        textview_createclassification_price=(BaseTextview)findViewById(R.id.textview_createclassification_price);
        textview_createclassification_selectcategory=(BaseTextview)findViewById(R.id.textview_createclassification_selectcategory);
        textview_createclassification_uploadimage=(BaseTextview)findViewById(R.id.textview_createclassification_uploadimages);
        button_createclassification_submit=(Button)findViewById(R.id.button_createclassification_submit);
        spinner_createclassification_choosecategory=(Spinner)findViewById(R.id.spinner_createclassification_selectcategory);

    }
}
