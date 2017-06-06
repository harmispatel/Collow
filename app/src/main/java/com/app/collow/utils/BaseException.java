package com.app.collow.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.app.collow.R;
import com.app.collow.activities.InternetLostActivity;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.RetryParameterbean;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by harmis on 8/1/17.
 */

public class BaseException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static RetryParameterbean retryParameterbean=null;
    int i = 0;
    // Default constructor
    // initializes custom exception variable to none



    // Custom Exception Constructor
    public BaseException(Exception e, boolean flag_toast_or_alert, RetryParameterbean retryParameterbean) {
        // Call super class constructor
        super(e.getMessage());
        i = 0;

         if(retryParameterbean!=null)
             this.retryParameterbean=retryParameterbean;
         { if (e instanceof UnknownHostException) {
            // internetNotAvailableOrSlow(retryParameterbean);

             Intent intent=new Intent(retryParameterbean.getActivity(), InternetLostActivity.class);
             retryParameterbean.getActivity().startActivity(intent);
         } else if (e instanceof ConnectTimeoutException) {
           //  internetNotAvailableOrSlow(retryParameterbean);
             Intent intent=new Intent(retryParameterbean.getActivity(), InternetLostActivity.class);
             retryParameterbean.getActivity().startActivity(intent);
         } else if (e instanceof SocketTimeoutException) {
             Intent intent=new Intent(retryParameterbean.getActivity(), InternetLostActivity.class);
             retryParameterbean.getActivity().startActivity(intent);

         } else {

             if (retryParameterbean.getContext() != null) {
                 StackTraceElement l = getStackTrace()[0];
                 alertUser(flag_toast_or_alert, retryParameterbean.getContext(), e.getMessage() + "\n" + "Class Name =:" +
                         l.getClassName() + "\n" + " Method Name =:" + l.getMethodName() + "\n" + "Line number =:" + l.getLineNumber());
             }
         }

         }



    }


    //if true we will display alert dialog other wise toast message
    public void alertUser(boolean flag_toast_or_alert, Context context, String message) {
        if (flag_toast_or_alert) {
            /*AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(message);
            dialog.setNeutralButton(context.getResources().getString(android.R.string.ok), null);
            dialog.create().show();*/
        } else {
            CommonMethods.customToastMessage(message, context);
        }

    }

    public void internetNotAvailableOrSlow(final RetryParameterbean retryParameterbean) {
        try {


            if (retryParameterbean.getContext() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(retryParameterbean.getContext());
                if (i == 0) {
                    builder.setMessage(retryParameterbean.getActivity().getResources().getString(R.string.internet_connection_slow));

                } else {
                    builder.setMessage(retryParameterbean.getActivity().getResources().getString(R.string.internet_connection_slow) + "\n" + retryParameterbean.getActivity().getResources().getString(R.string.attempting) + " " + String.valueOf(i) + " " + retryParameterbean.getActivity().getResources().getString(R.string.times));

                }


                builder.setCancelable(false)
                        .setPositiveButton(retryParameterbean.getActivity().getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ConnectionDetector connectionDetector = new ConnectionDetector(retryParameterbean.getActivity());
                                if (connectionDetector.isConnectingToInternet()) {
                                    dialog.cancel();
                                    if (retryParameterbean.getActivity() != null) {
                                        retryParameterbean.getActivity().finish();
                                        Bundle bundle = null;
                                        if (retryParameterbean.getBundle() != null) {
                                            bundle = retryParameterbean.getBundle();
                                        } else {
                                            bundle = null;
                                        }
                                        CommonMethods.startNewActivityWithCloseCurrentActivity(retryParameterbean.getActivity(), retryParameterbean.getCurrentActivityClass(), bundle);

                                    }
                                } else {
                                    i++;
                                    internetNotAvailableOrSlow(retryParameterbean);
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }


        } catch (Exception e) {


        }
    }
}