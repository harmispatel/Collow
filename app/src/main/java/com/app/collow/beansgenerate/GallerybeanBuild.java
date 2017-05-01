package com.app.collow.beansgenerate;

import com.app.collow.beans.Gallerybean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.JSONCommonKeywords;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Harmis on 25/02/17.
 */

public class GallerybeanBuild {
    //get gallery bean
    public static Gallerybean getGalleryBeanFromJSON(JSONObject jsonObject_single_gallery)
    {
        Gallerybean gallerybean = new Gallerybean();


        try {
            if(jsonObject_single_gallery.has(JSONCommonKeywords.id))
            {
                gallerybean.setGalleryID(jsonObject_single_gallery.getString(JSONCommonKeywords.id));
            }
            if(jsonObject_single_gallery.has(JSONCommonKeywords.ActivityId))
            {
                gallerybean.setActivityID(jsonObject_single_gallery.getString(JSONCommonKeywords.ActivityId));
            }

            if(jsonObject_single_gallery.has(JSONCommonKeywords.CommunityID))
            {
                gallerybean.setCommunityID(jsonObject_single_gallery.getString(JSONCommonKeywords.CommunityID));
            }

            if(jsonObject_single_gallery.has(JSONCommonKeywords.userid))
            {
                gallerybean.setUserID(jsonObject_single_gallery.getString(JSONCommonKeywords.userid));
            }


            if(jsonObject_single_gallery.has(JSONCommonKeywords.UserName))
            {
                gallerybean.setUserName(jsonObject_single_gallery.getString(JSONCommonKeywords.UserName));
            }


            if(jsonObject_single_gallery.has(JSONCommonKeywords.ProfilePic))
            {
                gallerybean.setProfilePic(jsonObject_single_gallery.getString(JSONCommonKeywords.ProfilePic));
            }


            if(jsonObject_single_gallery.has(JSONCommonKeywords.Title))
            {
                gallerybean.setTitle(jsonObject_single_gallery.getString(JSONCommonKeywords.Title));
            }


            if(jsonObject_single_gallery.has(JSONCommonKeywords.createdDate))
            {
                gallerybean.setCreatedDate(jsonObject_single_gallery.getString(JSONCommonKeywords.createdDate));
                String dateString=CommonMethods.changeDateFormat(gallerybean.getCreatedDate(),1);
                if(dateString!=null)
                {
                    gallerybean.setCreatedDate(dateString);
                }
            }





            if(jsonObject_single_gallery.has(JSONCommonKeywords.AlbumName))
            {
                gallerybean.setAlbumName(jsonObject_single_gallery.getString(JSONCommonKeywords.AlbumName));
            }


            SocialOptionbean socialOptionbean = CommonMethods.getSocialOptionbean(jsonObject_single_gallery);

            if (socialOptionbean != null) {
                gallerybean.setSocialOptionbean(socialOptionbean);
            }


            if (jsonObject_single_gallery.has(JSONCommonKeywords.isLiked)) {


                String islikedfeedString = jsonObject_single_gallery.getString(JSONCommonKeywords.isLiked);
                if (islikedfeedString == null || islikedfeedString.equals("")) {
                    gallerybean.setLikedByLoggedUser(false);
                } else if (islikedfeedString.equals("1")) {
                    gallerybean.setLikedByLoggedUser(true);

                } else {
                    gallerybean.setLikedByLoggedUser(false);

                }


            }




            ArrayList<String> images_of_classified = new ArrayList<>();

            if (CommonMethods.handleKeyInJSON(jsonObject_single_gallery, JSONCommonKeywords.Image))

            {
                if (CommonMethods.checkJSONArrayHasData(jsonObject_single_gallery, JSONCommonKeywords.Image)) {
                    JSONArray jsonArray_images = jsonObject_single_gallery.getJSONArray(JSONCommonKeywords.Image);
                    for (int j = 0; j < jsonArray_images.length(); j++) {

                        if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                            images_of_classified.add(jsonArray_images.getString(j));
                        }
                    }

                    gallerybean.setStringArrayList_images_urls(images_of_classified);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gallerybean;
    }

}
