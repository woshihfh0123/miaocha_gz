package com.mc.phonelive.bean.foxtone;

import java.io.Serializable;

public class FindSchoolBean implements Serializable {
    /**
     * id : 25
     * title : 美国商学院开课啦
     * pic : http://huyin.local.com/data/upload/ueditor/20200506/5eb2b42d5d8f1.jpg
     * web_url : http://huyin.yanshi.hbbeisheng.com/index.php?g=portal&m=page&a=index&id=25
     */

    private String id;
    private String title;
    private String pic;
    private String web_url;

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }
}
