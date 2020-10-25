package com.mc.phonelive.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by cxf on 2017/10/25.
 * 视频
 */

public class VideoBean implements Parcelable {
    private String id;
    private String uid;
    private String ordercode;
    private String title;
    private String stream;
    private String pull;
    private String isZbz="0";
    private String thumb;
    private String thumbs;
    private String href;
    private String likeNum;
    private String viewNum;
    private String commentNum;
    private String stepNum;
    private String shareNum;
    private String addtime;
    private String lat;
    private String lng;
    private String city;
    private UserBean userBean;
    private String datetime;
    private String distance;
    private int step;//是否踩过
    private int like;//是否赞过
    private int attent;//是否关注过作者
    private int status;
    private int musicId;
    private String is_shopping;
    private String is_hot;

    private String is_ad; //是否有广告0没有1是
    private String pay_type;
    private String tradeno;
    private String pay_time;
    private String urls;
    private String is_goods;//是否是商品
    private ADList ad_list;
    private MusicBean musicInfo;
    private GoodsInfoBean goods_info;

    public String getIs_ad() {
        return is_ad;
    }

    public void setIs_ad(String is_ad) {
        this.is_ad = is_ad;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public ADList getAd_list() {
        return ad_list;
    }

    public void setAd_list(ADList ad_list) {
        this.ad_list = ad_list;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getPull() {
        return pull;
    }

    public void setPull(String pull) {
        this.pull = pull;
    }

    public String getIsZbz() {
        return isZbz;
    }

    public void setIsZbz(String isZbz) {
        this.isZbz = isZbz;
    }

    public String getIs_goods() {
        return is_goods;
    }

    public void setIs_goods(String is_goods) {
        this.is_goods = is_goods;
    }

    public GoodsInfoBean getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(GoodsInfoBean goods_info) {
        this.goods_info = goods_info;
    }

    public  static class ADList{
        private String slide_name;
        private String slide_pic;
        private String slide_url; //webview页面调用地址
        private String type;

        public String getSlide_name() {
            return slide_name;
        }

        public void setSlide_name(String slide_name) {
            this.slide_name = slide_name;
        }

        public String getSlide_pic() {
            return slide_pic;
        }

        public void setSlide_pic(String slide_pic) {
            this.slide_pic = slide_pic;
        }

        public String getSlide_url() {
            return slide_url;
        }

        public void setSlide_url(String slide_url) {
            this.slide_url = slide_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class  GoodsInfoBean{
        private String id;
        private String title;
        private String goods_img;

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

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }
    }

    public VideoBean() {

    }

    public String getIs_shopping() {
        return is_shopping;
    }

    public void setIs_shopping(String is_shopping) {
        this.is_shopping = is_shopping;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @JSONField(name = "thumb_s")
    public String getThumbs() {
        return thumbs;
    }

    @JSONField(name = "thumb_s")
    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @JSONField(name = "likes")
    public String getLikeNum() {
        return likeNum;
    }

    @JSONField(name = "likes")
    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    @JSONField(name = "views")
    public String getViewNum() {
        return viewNum;
    }

    @JSONField(name = "views")
    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }

    @JSONField(name = "comments")
    public String getCommentNum() {
        return commentNum;
    }

    @JSONField(name = "comments")
    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    @JSONField(name = "steps")
    public String getStepNum() {
        return stepNum;
    }

    @JSONField(name = "steps")
    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    @JSONField(name = "shares")
    public String getShareNum() {
        return shareNum;
    }

    @JSONField(name = "shares")
    public void setShareNum(String shareNum) {
        this.shareNum = shareNum;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
    @JSONField(name = "city")
    public String getCity() {
        return city;
    }
    @JSONField(name = "city")
    public void setCity(String city) {
        this.city = city;
    }


    @JSONField(name = "userinfo")
    public UserBean getUserBean() {
        return userBean;
    }

    @JSONField(name = "userinfo")
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @JSONField(name = "isstep")
    public int getStep() {
        return step;
    }

    @JSONField(name = "isstep")
    public void setStep(int step) {
        this.step = step;
    }

    @JSONField(name = "islike")
    public int getLike() {
        return like;
    }

    @JSONField(name = "islike")
    public void setLike(int like) {
        this.like = like;
    }

    @JSONField(name = "isattent")
    public int getAttent() {
        return attent;
    }

    @JSONField(name = "isattent")
    public void setAttent(int attent) {
        this.attent = attent;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JSONField(name = "music_id")
    public int getMusicId() {
        return musicId;
    }

    @JSONField(name = "music_id")
    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    @JSONField(name = "musicinfo")
    public MusicBean getMusicInfo() {
        return musicInfo;
    }

    @JSONField(name = "musicinfo")
    public void setMusicInfo(MusicBean musicInfo) {
        this.musicInfo = musicInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.title);
        dest.writeString(this.thumb);
        dest.writeString(this.thumbs);
        dest.writeString(this.href);
        dest.writeString(this.likeNum);
        dest.writeString(this.viewNum);
        dest.writeString(this.commentNum);
        dest.writeString(this.stepNum);
        dest.writeString(this.shareNum);
        dest.writeString(this.addtime);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.city);
        dest.writeString(this.datetime);
        dest.writeInt(this.like);
        dest.writeInt(this.attent);
        dest.writeString(this.distance);
        dest.writeInt(this.step);
        dest.writeParcelable(this.userBean, flags);
        dest.writeInt(this.status);
        dest.writeInt(this.musicId);
        dest.writeString(this.is_shopping);
        dest.writeParcelable(this.musicInfo, flags);
    }


    public VideoBean(Parcel in) {
        this.id = in.readString();
        this.uid = in.readString();
        this.title = in.readString();
        this.thumb = in.readString();
        this.thumbs = in.readString();
        this.href = in.readString();
        this.likeNum = in.readString();
        this.viewNum = in.readString();
        this.commentNum = in.readString();
        this.stepNum = in.readString();
        this.shareNum = in.readString();
        this.addtime = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.city = in.readString();
        this.datetime = in.readString();
        this.like = in.readInt();
        this.attent = in.readInt();
        this.distance = in.readString();
        this.is_shopping = in.readString();
        this.step = in.readInt();
        this.userBean = in.readParcelable(UserBean.class.getClassLoader());
        this.status = in.readInt();
        this.musicId = in.readInt();
        this.musicInfo = in.readParcelable(MusicBean.class.getClassLoader());
    }


    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }

        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }
    };

    @Override
    public String toString() {
        try {
            return "VideoBean{" +
                    "title='" + title + '\'' +
                    ",href='" + href + '\'' +
                    ",id='" + id + '\'' +
                    ",uid='" + uid + '\'' +
                    ",userNiceName='" + userBean.getUserNiceName() + '\'' +
                    ",thumb='" + thumb + '\'' +
                    '}';
        }catch (Exception e){
            return "";
        }

    }


    public String getTag() {
        return "VideoBean" + this.getId() + this.hashCode();
    }

}
