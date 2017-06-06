package com.app.collow.utils;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;

import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyJSONParser {

    public MyJSONParser() {

    }

    public JSONObject postData(String url, JSONObject jsonobject) {

        Log.e("URL===>", url);
        Log.e("parameter ===>", jsonobject.toString());
        // Create a new HttpClient and Post Header

        JSONObject jObj_responce = null;
        try {

            URL urlm = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlm.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");

            StringEntity stringEntity = new StringEntity(jsonobject.toString());
            stringEntity.setContentType(new BasicHeader("Content-Type", "application/json"));


            conn.addRequestProperty("Content-length", stringEntity.getContentLength() + "");
            conn.addRequestProperty(stringEntity.getContentType().getName(), stringEntity.getContentType().getValue());


            OutputStream os = conn.getOutputStream();
            stringEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();

            // Log.e("responce code :===>", ""+conn.getResponseCode());
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try {


                    String responce = readStream(conn.getInputStream());
                    Log.e("responce ===>", responce);
                    jObj_responce = new JSONObject(responce);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (conn.getResponseCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {


            } else {
            }

        } catch (IOException e) {
        }

        // return JSON String
        return jObj_responce;
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }




}