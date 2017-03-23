package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 31/1/17.
 */

public class Gallerybean  extends Basebean implements Serializable{
    String gallery_image=null;
    String gallery_likes=null;
    String gallery_comments=null;


    public Gallerybean() {
    }

    public String getGallery_image() {
        return gallery_image;
    }

    public void setGallery_image(String gallery_image) {
        this.gallery_image = gallery_image;
    }

    public String getGallery_likes() {
        return gallery_likes;
    }

    public void setGallery_likes(String gallery_likes) {
        this.gallery_likes = gallery_likes;
    }

    public String getGallery_comments() {
        return gallery_comments;
    }

    public void setGallery_comments(String gallery_comments) {
        this.gallery_comments = gallery_comments;
    }
}
