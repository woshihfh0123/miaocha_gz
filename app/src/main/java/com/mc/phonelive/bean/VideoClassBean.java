package com.mc.phonelive.bean;

import java.io.Serializable;

/**
 * created by WWL on 2020/3/31 0031:16
 */
public class VideoClassBean implements Serializable {

   private String id;
    private String  title;
    private String img_url;
    private String thumb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
