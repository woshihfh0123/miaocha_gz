package cece.com.bannerlib.model;

import java.io.Serializable;

/**
 * created by WWL on 2019/6/1 0001:10
 */
public class PicBean implements Serializable{
    /**
     * id : 2
     * pic : http://film.test.hbbeisheng.com/uploads/admin/image/2019-03-20/5c91f23b0dad7.png
     * title : 1
     * url : https://www.baidu.com/
     */

    private String id;
    private String pic;
    private String title;
    private String type;
    private String url;
    private String uid;//自定义的

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
