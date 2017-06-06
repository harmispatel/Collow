package com.app.collow.asyntasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.app.collow.R;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.httprequests.ManageHttpRequest;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;

/**
 * Created by harmis on 28/10/16.
 */
public class RequestToServer extends AsyncTask<Void, Void, Responcebean> {
    PassParameterbean passParameterbean = null;
    RetryParameterbean retryParameterbean=null;
    ManageHttpRequest manageHttpRequest = new ManageHttpRequest();
    ConnectionDetector connectionDetector = null;
    ProgressDialog prog;


    public RequestToServer(PassParameterbean passParameterbean, RetryParameterbean retryParameterbean) {
        this.passParameterbean = passParameterbean;
        connectionDetector = new ConnectionDetector(passParameterbean.getContext());
        this.retryParameterbean=retryParameterbean;
        if (passParameterbean.getActivity() != null) {
            if(passParameterbean.isNoNeedToDisplayLoadingDialog())
            {
            }
            else
            {
                prog=new ProgressDialog(passParameterbean.getActivity());
                prog.setMessage(passParameterbean.getActivity().getResources().getString(R.string.loading));
                prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            }


        }


    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (prog != null) {
            prog.show();
        }
    }

    @Override
    protected Responcebean doInBackground(Void... params) {
        Responcebean responcebean = null;
        try {


            if (connectionDetector.isConnectingToInternet()) {


                responcebean = manageHttpRequest.makeRequest(passParameterbean);


            } else {
                throw new UnknownHostException();

            }


        } catch (final Exception e) {
            cancel(true);
            passParameterbean.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                     if(prog!=null&&prog.isShowing())
                     {
                         prog.dismiss();
                     }

                    new BaseException(e, false, retryParameterbean);

                }
            });


        }
        return responcebean;
    }

    @Override
    protected void onPostExecute(Responcebean responcebean) {
        super.onPostExecute(responcebean);

        try {
            if (prog != null && prog.isShowing()) {
                prog.dismiss();
            }
            responcebean.setScreenIndex(passParameterbean.getScreenIndex());
            //this is callback method of interface calling for setupview
            passParameterbean.getSetupViewInterface().setupUI(responcebean);

        } catch (Exception e) {
            if(retryParameterbean!=null)
            {
                new BaseException(e, false, retryParameterbean);

            }


        }


    }





}
