package com.mc.phonelive.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.mc.phonelive.R;
import com.mc.phonelive.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2017/8/14.
 */

public class UserBean implements Parcelable {

    protected String id;
    protected String uid;
    protected String user_nicename;
    private String is_shopping;
    protected String avatar;
    protected String avatar_thumb;
    protected String sex;
    protected String signature;
    protected String coin;
    protected String votes;
    protected String mobile;
    protected String consumption;
    protected String votestotal;
    protected String province;
    protected String city;
    protected String area;
    protected String birthday;
    protected String is_attention;
    protected String store_id;
    protected String is_live_auth;
    protected int level;
    protected int level_anchor;
    protected int lives;
    protected int follows;
    protected int fans;
    protected String work_num;
    protected String like_num;
    private String stream;
    private String pull;
    private String isZbz="0";
    protected Vip vip;
    protected ArrayList<Label> label;
    protected Liang liang;
    protected Car car;
    protected  String isSendMes;
    private String urls;

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getIsSendMes() {
        return isSendMes;
    }

    public void setIsSendMes(String isSendMes) {
        this.isSendMes = isSendMes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIs_shopping() {
        return is_shopping;
    }

    public void setIs_shopping(String is_shopping) {
        this.is_shopping = is_shopping;
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

    private List<String> histList=new ArrayList<>();
    protected String is_store;// 0 是未开通  等于 1是已开通，大于1都是审核中

    public List<String> getHistList() {
        return histList;
    }

    public void setHistList(List<String> histList) {
        this.histList = histList;
    }

    public String getIs_live_auth() {
        return is_live_auth;
    }

    public void setIs_live_auth(String is_live_auth) {
        this.is_live_auth = is_live_auth;
    }

    public String getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(String is_attention) {
        this.is_attention = is_attention;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWork_num() {
        return work_num;
    }

    public void setWork_num(String work_num) {
        this.work_num = work_num;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    //---------------------狐音需求字段start--------------
    private double fee;//音豆
    private String cent;//音分值
    private String grade_id;//音分值等级
    private String skill_id;//熟练度
    private String skill;//熟练度
    private String freeze_fee;//冻结音豆
    private String musical_count;//乐器数量
    private String area_status;//区域代理申请  0 可以申请 1审核通过 2待审核 3审核失败
    //---------------------狐音需求字段-end-------------



    public ArrayList<Label> getLabel() {
        return label;
    }

    public void setLabel(ArrayList<Label> label) {
        this.label = label;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea_status() {
        return area_status;
    }

    public void setArea_status(String area_status) {
        this.area_status = area_status;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }

    public String getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(String skill_id) {
        this.skill_id = skill_id;
    }

    public String getMusical_count() {
        return musical_count;
    }

    public void setMusical_count(String musical_count) {
        this.musical_count = musical_count;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getCent() {
        return cent;
    }

    public void setCent(String cent) {
        this.cent = cent;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getFreeze_fee() {
        return freeze_fee;
    }

    public void setFreeze_fee(String freeze_fee) {
        this.freeze_fee = freeze_fee;
    }

    public String getIs_store() {
        return is_store;
    }

    public void setIs_store(String is_store) {
        this.is_store = is_store;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JSONField(name = "user_nicename")
    public String getUserNiceName() {
        return user_nicename;
    }

    @JSONField(name = "user_nicename")
    public void setUserNiceName(String userNiceName) {
        this.user_nicename = userNiceName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JSONField(name = "avatar_thumb")
    public String getAvatarThumb() {
        return avatar_thumb;
    }

    @JSONField(name = "avatar_thumb")
    public void setAvatarThumb(String avatarThumb) {
        this.avatar_thumb = avatarThumb;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getVotestotal() {
        return votestotal;
    }

    public void setVotestotal(String votestotal) {
        this.votestotal = votestotal;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    @JSONField(name = "city")
    public String getCity() {
        return city;
    }
    @JSONField(name = "city")
    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getLevel() {
        if (level == 0) {
            level = 1;
        }
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JSONField(name = "level_anchor")
    public int getLevelAnchor() {
        return level_anchor;
    }

    @JSONField(name = "level_anchor")
    public void setLevelAnchor(int levelAnchor) {
        this.level_anchor = levelAnchor;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public Vip getVip() {
        return vip;
    }

    public void setVip(Vip vip) {
        this.vip = vip;
    }

    public Liang getLiang() {
        return liang;
    }

    public void setLiang(Liang liang) {
        this.liang = liang;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * 显示靓号
     */
    public String getLiangNameTip() {
        if (this.liang != null) {
            String liangName = this.liang.getName();
            if (!TextUtils.isEmpty(liangName) && !"0".equals(liangName)) {
                return WordUtil.getString(R.string.live_liang) + ":" + liangName;
            }
        }
        return "ID:" + this.id;
    }

    /**
     * 获取靓号
     */
    public String getGoodName() {
        if (this.liang != null) {
            return this.liang.getName();
        }
        return "0";
    }

    public int getVipType() {
        if (this.vip != null) {
            return this.vip.getType();
        }
        return 0;
    }


    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.id = in.readString();
        this.user_nicename = in.readString();
        this.avatar = in.readString();
        this.avatar_thumb = in.readString();
        this.sex = in.readString();
        this.signature = in.readString();
        this.coin = in.readString();
        this.votes = in.readString();
        this.consumption = in.readString();
        this.votestotal = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.birthday = in.readString();
        this.level = in.readInt();
        this.level_anchor = in.readInt();
        this.lives = in.readInt();
        this.follows = in.readInt();
        this.fans = in.readInt();
        this.is_store =in.readString();
        this.cent =in.readString();
        this.fee =in.readDouble();
        this.skill =in.readString();
        this.freeze_fee =in.readString();
        this.musical_count =in.readString();
        this.skill_id =in.readString();
        this.grade_id =in.readString();
        this.vip = in.readParcelable(Vip.class.getClassLoader());
        this.liang = in.readParcelable(Liang.class.getClassLoader());
        this.car = in.readParcelable(Car.class.getClassLoader());

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.user_nicename);
        dest.writeString(this.avatar);
        dest.writeString(this.avatar_thumb);
        dest.writeString(this.sex);
        dest.writeString(this.signature);
        dest.writeString(this.coin);
        dest.writeString(this.votes);
        dest.writeString(this.consumption);
        dest.writeString(this.votestotal);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.birthday);
        dest.writeInt(this.level);
        dest.writeInt(this.level_anchor);
        dest.writeInt(this.lives);
        dest.writeInt(this.follows);
        dest.writeInt(this.fans);
        dest.writeString(this.is_store);
        dest.writeString(this.cent);
        dest.writeDouble(this.fee);
        dest.writeString(this.skill);
        dest.writeString(this.freeze_fee);
        dest.writeString(this.musical_count);
        dest.writeString(this.skill_id);
        dest.writeString(this.grade_id);
        dest.writeParcelable(this.vip, flags);
        dest.writeParcelable(this.liang, flags);
        dest.writeParcelable(this.car, flags);
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }

        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }
    };

    public static class Label implements Parcelable {
        protected String id;
        protected String name;
        protected String orderno;
        protected String colour;
        protected String nums;


        @Override
        public String toString() {
            return "{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", orderno='" + orderno + '\'' +
                    ", colour='" + colour + '\'' +
                    ", nums='" + nums + '\'' +
                    '}';
        }

        public Label() {

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public String getColour() {
            return colour;
        }

        public void setColour(String colour) {
            this.colour = colour;
        }

        public String getNums() {
            return nums;
        }

        public void setNums(String nums) {
            this.nums = nums;
        }

        public static Creator<Label> getCREATOR() {
            return CREATOR;
        }

        protected Label(Parcel in) {
            id = in.readString();
            name = in.readString();
            orderno = in.readString();
            colour = in.readString();
            nums = in.readString();
        }

        public static final Creator<Label> CREATOR = new Creator<Label>() {
            @Override
            public Label createFromParcel(Parcel in) {
                return new Label(in);
            }

            @Override
            public Label[] newArray(int size) {
                return new Label[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(orderno);
            dest.writeString(colour);
            dest.writeString(nums);
        }
    }
    public static class Vip implements Parcelable {
        protected int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public Vip() {

        }

        public Vip(Parcel in) {
            this.type = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
        }

        public static final Creator<Vip> CREATOR = new Creator<Vip>() {
            @Override
            public Vip[] newArray(int size) {
                return new Vip[size];
            }

            @Override
            public Vip createFromParcel(Parcel in) {
                return new Vip(in);
            }
        };
    }

    public static class Liang implements Parcelable {
        protected String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Liang() {

        }

        public Liang(Parcel in) {
            this.name = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
        }

        public static final Creator<Liang> CREATOR = new Creator<Liang>() {

            @Override
            public Liang createFromParcel(Parcel in) {
                return new Liang(in);
            }

            @Override
            public Liang[] newArray(int size) {
                return new Liang[size];
            }
        };

    }

    public static class Car implements Parcelable {
        protected int id;
        protected String swf;
        protected float swftime;
        protected String words;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSwf() {
            return swf;
        }

        public void setSwf(String swf) {
            this.swf = swf;
        }

        public float getSwftime() {
            return swftime;
        }

        public void setSwftime(float swftime) {
            this.swftime = swftime;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public Car() {

        }

        public Car(Parcel in) {
            this.id = in.readInt();
            this.swf = in.readString();
            this.swftime = in.readFloat();
            this.words = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.swf);
            dest.writeFloat(this.swftime);
            dest.writeString(this.words);
        }


        public static final Creator<Car> CREATOR = new Creator<Car>() {
            @Override
            public Car[] newArray(int size) {
                return new Car[size];
            }

            @Override
            public Car createFromParcel(Parcel in) {
                return new Car(in);
            }
        };

    }

}
