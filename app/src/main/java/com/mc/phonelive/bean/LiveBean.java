package com.mc.phonelive.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.mc.phonelive.R;
import com.mc.phonelive.utils.WordUtil;

import java.util.List;

/**
 * Created by cxf on 2017/8/9.
 * 直播间
 */

public class LiveBean implements Parcelable {
    private String uid;
    private String avatar;
    private String avatarThumb;
    private String userNiceName;
    private String is_shopping;
    private String title;
    private String city;
    private String stream;
    private String pull;
    private String thumb;
    private String nums;
    private String storeid;
    private int sex;
    private String distance;
    private int levelAnchor;
    private int type;
    private String typeVal;
    private String goodNum;//主播的靓号
    private int gameAction;//正在进行的游戏的标识
    private String game;
    private String goods_count;
    private List<GoodsListBean> goods_list;

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(String goods_count) {
        this.goods_count = goods_count;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public static class GoodsListBean{
        private String id;
        private String store_id;
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

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }
    }



    public String getIs_shopping() {
        return is_shopping;
    }

    public void setIs_shopping(String is_shopping) {
        this.is_shopping = is_shopping;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JSONField(name = "avatar_thumb")
    public String getAvatarThumb() {
        return avatarThumb;
    }

    @JSONField(name = "avatar_thumb")
    public void setAvatarThumb(String avatarThumb) {
        this.avatarThumb = avatarThumb;
    }

    @JSONField(name = "user_nicename")
    public String getUserNiceName() {
        return userNiceName;
    }

    @JSONField(name = "user_nicename")
    public void setUserNiceName(String userNiceName) {
        this.userNiceName = userNiceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }


    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    @JSONField(name = "level_anchor")
    public int getLevelAnchor() {
        return levelAnchor;
    }

    @JSONField(name = "level_anchor")
    public void setLevelAnchor(int levelAnchor) {
        this.levelAnchor = levelAnchor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JSONField(name = "type_val")
    public String getTypeVal() {
        return typeVal;
    }

    @JSONField(name = "type_val")
    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    @JSONField(name = "goodnum")
    public String getGoodNum() {
        return goodNum;
    }

    @JSONField(name = "goodnum")
    public void setGoodNum(String goodNum) {
        this.goodNum = goodNum;
    }

    @JSONField(name = "game_action")
    public int getGameAction() {
        return gameAction;
    }

    @JSONField(name = "game_action")
    public void setGameAction(int gameAction) {
        this.gameAction = gameAction;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * 显示靓号
     */
    public String getLiangNameTip() {
        if (!TextUtils.isEmpty(this.goodNum) && !"0".equals(this.goodNum)) {
            return WordUtil.getString(R.string.live_liang) + ":" + this.goodNum;
        }
        return "ID:" + this.uid;
    }

    public LiveBean() {

    }

    private LiveBean(Parcel in) {
        this.uid = in.readString();
        this.avatar = in.readString();
        this.avatarThumb = in.readString();
        this.userNiceName = in.readString();
        this.sex = in.readInt();
        this.title = in.readString();
        this.city = in.readString();
        this.stream = in.readString();
        this.pull = in.readString();
        this.thumb = in.readString();
        this.nums = in.readString();
        this.distance = in.readString();
        this.levelAnchor = in.readInt();
        this.type = in.readInt();
        this.typeVal = in.readString();
        this.goodNum = in.readString();
        this.gameAction = in.readInt();
        this.game = in.readString();
        this.is_shopping = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.avatar);
        dest.writeString(this.avatarThumb);
        dest.writeString(this.userNiceName);
        dest.writeInt(this.sex);
        dest.writeString(this.title);
        dest.writeString(this.city);
        dest.writeString(this.stream);
        dest.writeString(this.pull);
        dest.writeString(this.thumb);
        dest.writeString(this.nums);
        dest.writeString(this.distance);
        dest.writeInt(this.levelAnchor);
        dest.writeInt(this.type);
        dest.writeString(this.typeVal);
        dest.writeString(this.goodNum);
        dest.writeInt(this.gameAction);
        dest.writeString(this.game);
        dest.writeString(this.is_shopping);
    }

    public static final Parcelable.Creator<LiveBean> CREATOR = new Creator<LiveBean>() {
        @Override
        public LiveBean[] newArray(int size) {
            return new LiveBean[size];
        }

        @Override
        public LiveBean createFromParcel(Parcel in) {
            return new LiveBean(in);
        }
    };

    @Override
    public String toString() {
        return "uid: " + uid + " , userNiceName: " + userNiceName + " ,playUrl: " + pull;
    }
}
