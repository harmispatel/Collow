package com.app.collow.httprequests;


import android.graphics.Bitmap;
import android.util.Log;

import com.app.collow.R;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.beans.Filebean;
import com.app.collow.beans.Imagebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.utils.CommonMethods;

import org.apache.http.HttpStatus;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by harmis on 28/10/16.
 */
public class ManageHttpRequest  {


    public Responcebean makeRequest(PassParameterbean passParameterbean) {
        Responcebean responcebean = null;

        try {
            if (passParameterbean.getRequestMethod() == HTTPRequestMethodEnums.POST.getHTTPRequestMethodInex()) {

                responcebean=makeRequestForWithoutImage(passParameterbean);

            } else if (passParameterbean.getRequestMethod() == HTTPRequestMethodEnums.GET.getHTTPRequestMethodInex()) {

            } else if (passParameterbean.getRequestMethod() == HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex()) {

                responcebean=makeRequestForWithOneImage(passParameterbean);
            }
            else
            {
                responcebean=makeRequestForWithoutImage(passParameterbean);

            }
        }

        catch (Exception e) {

        }

        return responcebean;
    }






    public  static Responcebean makeRequestForWithoutImage(PassParameterbean passParameterbean) {

        Responcebean responcebean = new Responcebean();
        try {

            URL urlm = new URL(passParameterbean.getRequestURL());
            HttpURLConnection conn = (HttpURLConnection) urlm.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setConnectTimeout(7000);

            CommonMethods.displayLog("URL",passParameterbean.getRequestURL());

            if (passParameterbean.getJsonObjectRequestParams() == null) {
            } else {
                CommonMethods.displayLog("Passed Parameters",passParameterbean.getJsonObjectRequestParams().toString());
                StringEntity stringEntity = new StringEntity(passParameterbean.getJsonObjectRequestParams().toString());
                stringEntity.setContentType(new BasicHeader("Content-Type", "application/json"));
                conn.addRequestProperty("Content-length", stringEntity.getContentLength() + "");
                conn.addRequestProperty(stringEntity.getContentType().getName(), stringEntity.getContentType().getValue());
                OutputStream os = conn.getOutputStream();
                stringEntity.writeTo(conn.getOutputStream());
                os.close();
            }

            conn.connect();
            CommonMethods.displayLog("Code",String.valueOf(conn.getResponseCode()));

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                responcebean.setResponceContent(readStream(conn.getInputStream()));

                if(responcebean.getResponceContent()==null||responcebean.getResponceContent().equals(""))
                {
                    responcebean.setServiceSuccess(false);
                    if(passParameterbean.getActivity()!=null)
                    {

                        responcebean.setErrorMessage(passParameterbean.getActivity().getResources().getString(R.string.could_not_get_data));

                    }
                }
                else
                {
                    responcebean.setServiceSuccess(true);

                }

                CommonMethods.displayLog("Success Responce",responcebean.getResponceContent());

            } else if (conn.getResponseCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                responcebean.setErrorMessage("The server encountered an un" +
                        "" +
                        "expected condition which prevented it from fulfilling the request.");
                responcebean.setServiceSuccess(false);

            }
            else if (conn.getResponseCode() == HttpStatus.SC_GATEWAY_TIMEOUT) {
                responcebean.setErrorMessage("The server encountered an unexpected condition which prevented it from fulfilling the request.");
                responcebean.setServiceSuccess(false);

            }
            else {
                responcebean.setServiceSuccess(false);
                responcebean.setErrorMessage(passParameterbean.getActivity().getResources().getString(R.string.could_not_get_data));
            }

        } catch (SocketTimeoutException e) {
            responcebean.setServiceSuccess(false);
            responcebean.setException(e);
            responcebean.setErrorMessage(e.getMessage());
        }catch (ConnectTimeoutException e) {
            responcebean.setServiceSuccess(false);
            responcebean.setException(e);
            responcebean.setErrorMessage(e.getMessage());
        }catch (Exception e) {
            responcebean.setServiceSuccess(false);
            responcebean.setException(e);
            responcebean.setErrorMessage(e.getMessage());
        }
        return responcebean;
    }


    public  Responcebean makeRequestForWithOneImage(PassParameterbean passParameterbean) {

        Responcebean responcebean = new Responcebean();
        try {
            CommonMethods.displayLog("URL",passParameterbean.getRequestURL());


            URL url = new URL(passParameterbean.getRequestURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");

            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            if(passParameterbean.isForImageUploadingCustomKeyword())
            {
                ArrayList<Filebean> fileArrayList_uploading=passParameterbean.getFileArrayList_uploading();
               if(passParameterbean.getForImageUploadingCustomKeywordName()!=null)
               {
                   if(fileArrayList_uploading.size()==0)
                   {

                   }
                   else
                   {
                       for (int i = 0; i < fileArrayList_uploading.size(); i++) {
                           Filebean filebean = fileArrayList_uploading.get(i);

                           if (filebean != null) {
                               if(filebean.getFilePath()!=null)
                               { FileInputStream fis = new FileInputStream(filebean.getFilePath());
                                   //System.out.println(file.exists() + "!!");
                                   //InputStream in = resource.openStream();
                                   ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                   byte[] buf = new byte[1024];
                                   try {
                                       for (int readNum; (readNum = fis.read(buf)) != -1;) {
                                           bos.write(buf, 0, readNum); //no doubt here is 0
                                       }
                                   } catch (IOException ex) {
                                   }
                                   byte[] bytes = bos.toByteArray();
                                   String filename = getFileName(filebean.getFilePath());

                                   CommonMethods.displayLog("Filename",filename);
                                   ByteArrayBody bab = new ByteArrayBody(bytes, filename);
                                   entity.addPart(passParameterbean.getForImageUploadingCustomKeywordName()+i, bab);

                               }

                           }
                       }
                   }
               }


            }
            else
            {
                ArrayList<Imagebean> bitmapArrayList=passParameterbean.getBitmapArrayList_mutliple_image();


                if(bitmapArrayList.size()==0)
                {
                    Bitmap bitmap = passParameterbean.getBitmap();


                    if (bitmap != null) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] data = bos.toByteArray();
                        ByteArrayBody bab = new ByteArrayBody(data, "collow_user_profile_pic.jpg");
                        // int newi = i + 1;
                        entity.addPart(passParameterbean.getForImageUploadingCustomKeywordName(), bab);

                        CommonMethods.displayLog("Key",passParameterbean.getForImageUploadingCustomKeywordName());


                    }
                }
               else if(bitmapArrayList.size()==1)
                {


                    for (int i = 0; i < bitmapArrayList.size(); i++) {
                        Imagebean imagebean = bitmapArrayList.get(i);

                        Bitmap bitmap=imagebean.getBitmap();
                        if (bitmap != null) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] data = bos.toByteArray();
                            ByteArrayBody bab = new ByteArrayBody(data, "collow_user_profile_pic.jpg");
                            // int newi = i + 1;
                            entity.addPart(passParameterbean.getForImageUploadingCustomKeywordName(), bab);

                            CommonMethods.displayLog("Key",passParameterbean.getForImageUploadingCustomKeywordName());


                        }
                    }



                }
                else
                {
                    for (int i = 0; i < bitmapArrayList.size(); i++) {
                        Imagebean imagebean = bitmapArrayList.get(i);

                        Bitmap bitmap=imagebean.getBitmap();
                        if (bitmap != null) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] data = bos.toByteArray();
                            ByteArrayBody bab = new ByteArrayBody(data, "collow_user_profile_pic"+"_"+i+".jpg");
                            // int newi = i + 1;
                            entity.addPart(passParameterbean.getForImageUploadingCustomKeywordName()+i, bab);

                        }
                    }
                }

            }




            entity.addPart("json_content", new StringBody(passParameterbean.getJsonObjectRequestParams().toString()));
            CommonMethods.displayLog("Passed Parameters",passParameterbean.getJsonObjectRequestParams().toString());

            conn.addRequestProperty("Content-length", entity.getContentLength() + "");
            conn.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            entity.writeTo(conn.getOutputStream());

            os.close();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                responcebean.setServiceSuccess(true);
                responcebean.setResponceContent(readStream(conn.getInputStream()));

                CommonMethods.displayLog("SuccessResponce",responcebean.getResponceContent());

                if(responcebean.getResponceContent()==null||responcebean.getResponceContent().equals(""))
                {
                    responcebean.setServiceSuccess(false);

                }


            } else if (conn.getResponseCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                responcebean.setErrorMessage("The server encountered an un" +
                        "" +
                        "expected condition which prevented it from fulfilling the request.");
                responcebean.setServiceSuccess(false);

            }
            else if (conn.getResponseCode() == HttpStatus.SC_GATEWAY_TIMEOUT) {
                responcebean.setErrorMessage("The server encountered an unexpected condition which prevented it from fulfilling the request.");
                responcebean.setServiceSuccess(false);

            }else {
                responcebean.setServiceSuccess(false);
            }


        } catch (Exception
                e) {
            e.printStackTrace();
            responcebean.setErrorMessage(e.getMessage());
            responcebean.setServiceSuccess(false);


        }
        return responcebean;
    }


    private  static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
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
    public  String getFileName(String filePath) {
        if( filePath==null || filePath.length()==0 )
            return "";
        filePath = filePath.replaceAll("[/\\\\]+", "/");
        int len = filePath.length(),
                upCount = 0;
        while( len>0 ) {
            //remove trailing separator
            if( filePath.charAt(len-1)=='/' ) {
                len--;
                if( len==0 )
                    return "";
            }
            int lastInd = filePath.lastIndexOf('/', len-1);
            String fileName = filePath.substring(lastInd+1, len);
            if( fileName.equals(".") ) {
                len--;
            }
            else if( fileName.equals("..") ) {
                len -= 2;
                upCount++;
            }
            else {
                if( upCount==0 )
                    return fileName;
                upCount--;
                len -= fileName.length();
            }
        }
        return "";
    }
}
