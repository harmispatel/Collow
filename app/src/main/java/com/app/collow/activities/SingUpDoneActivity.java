package com.app.collow.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;

/**
 * Created by harmis on 6/1/17.
 */

public class SingUpDoneActivity extends AppCompatActivity {
    //Sign up
    //Signup done
    ImageView imageView_done=null;
    MyButtonView myButtonView_open_email=null;

    BaseTextview baseTextview_left_side=null;
    BaseTextview baseTextview_right_side=null;
    BaseTextview baseTextview_center_side=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_done);

        findViewByIDs();
        setupEvents();



    }

    private void findViewByIDs() {
        //Sign up findviewby id
        //Signup done findviewby id
        baseTextview_left_side= (BaseTextview) findViewById(R.id.textview_left_side_title);

        baseTextview_center_side= (BaseTextview) findViewById(R.id.textview_header_title);
        baseTextview_right_side = (BaseTextview) findViewById(R.id.textview_right_side_title);


        baseTextview_left_side.setText(getResources().getString(R.string.back));
        baseTextview_center_side.setText(getResources().getString(R.string.done));
        baseTextview_right_side.setText(getResources().getString(R.string.sign_in));


        imageView_done=(ImageView)findViewById(R.id.imageview_done);
        myButtonView_open_email= (MyButtonView) findViewById(R.id.mybutton_view_done_openmailapp);

        baseTextview_left_side.setText(getResources().getString(R.string.back));
        baseTextview_center_side.setText(getResources().getString(R.string.done));
        baseTextview_right_side.setText(getResources().getString(R.string.sign_in));
    }
    private void setupEvents() {




        baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myButtonView_open_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);




            }
        });
        baseTextview_right_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent=new Intent(SingUpDoneActivity.this,SignInActivity.class);
                    ComponentName cn = intent.getComponent();
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                    startActivity(mainIntent);
                } catch (Exception e) {


                }
            }
        });
    }
}