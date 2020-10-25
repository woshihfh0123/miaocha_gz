package com.mc.phonelive.bean;

public class FansItemBean {
    /**
     * id : 12066
     * user_nicename : 手机用户5716
     * avatar : http://miaocha.yanshi.hbbeisheng.com/api/upload/avatar/20200617/06457350568068514.png
     * avatar_thumb : http://miaocha.yanshi.hbbeisheng.com/api/upload/avatar/20200617/06457350568068514_thumb.png
     * sex : 1
     * signature : 这家伙很懒，什么都没留下
     * coin : 92705
     * consumption : 107318
     * votestotal : 0
     * province :
     * city : 襄阳市
     * birthday : 2020-06-17
     * user_status : 1
     * issuper : 0
     * level : 15
     * level_anchor : 1
     * vip : {"type":"0"}
     * liang : {"name":"0"}
     * isattention : 1
     * is_back_attention : 1
     * all_attention : 1
     */

    private String id;
    private String user_nicename;
    private String avatar;
    private String avatar_thumb;
    private String sex;
    private String signature;
    private String coin;
    private String consumption;
    private String votestotal;
    private String province;
    private String city;
    private String birthday;
    private String user_status;
    private String issuper;
    private String level;
    private String level_anchor;
    private VipBean vip;
    private LiangBean liang;
    private String isattention;
    private String is_back_attention;
    private String all_attention;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_thumb() {
        return avatar_thumb;
    }

    public void setAvatar_thumb(String avatar_thumb) {
        this.avatar_thumb = avatar_thumb;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getIssuper() {
        return issuper;
    }

    public void setIssuper(String issuper) {
        this.issuper = issuper;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel_anchor() {
        return level_anchor;
    }

    public void setLevel_anchor(String level_anchor) {
        this.level_anchor = level_anchor;
    }

    public VipBean getVip() {
        return vip;
    }

    public void setVip(VipBean vip) {
        this.vip = vip;
    }

    public LiangBean getLiang() {
        return liang;
    }

    public void setLiang(LiangBean liang) {
        this.liang = liang;
    }

    public String getIsattention() {
        return isattention;
    }

    public void setIsattention(String isattention) {
        this.isattention = isattention;
    }

    public String getIs_back_attention() {
        return is_back_attention;
    }

    public void setIs_back_attention(String is_back_attention) {
        this.is_back_attention = is_back_attention;
    }

    public String getAll_attention() {
        return all_attention;
    }

    public void setAll_attention(String all_attention) {
        this.all_attention = all_attention;
    }

    public static class VipBean {
        /**
         * type : 0
         */

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class LiangBean {
        /**
         * name : 0
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
}
}
