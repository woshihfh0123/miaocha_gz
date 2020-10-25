package com.mc.phonelive.bean.foxtone;

import java.io.Serializable;

/**
 * 交易中心   求购列表
 */
public class TradingCenterListBean implements Serializable {


    /**
     * id : 3
     * trade_sn : T20052511543811521378134
     * uid : 11521
     * type_id : 2
     * nums : 120
     * price : 50.00
     * total : 6000.00
     * addtime : 2020-05-25 11:54:38
     * buyer_uid : 0
     * buy_nums : 0
     * fee : 0.00
     * buy_total : 0.00
     * mobile : 155****2262
     * buy_time : 0
     * pay_time : 0
     * pay_img : null
     * state : 1
     * user_nicename : 手机用户2262
     * avatar_thumb : http://huyin.yanshi.hbbeisheng.com/api/upload/avatar/20200522/06434832628507949_thumb.png
     * grade_id : 0
     */

    private String id;
    private String trade_sn;
    private String uid;
    private String type_id;
    private String nums;
    private String price;
    private String total;
    private String addtime;
    private String buyer_uid;
    private String buy_nums;
    private String fee;
    private String buy_total;
    private String mobile;
    private String buy_time;
    private String pay_time;
    private Object pay_img;
    private String state;
    private String user_nicename;
    private String avatar_thumb;
    private String grade_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrade_sn() {
        return trade_sn;
    }

    public void setTrade_sn(String trade_sn) {
        this.trade_sn = trade_sn;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getBuyer_uid() {
        return buyer_uid;
    }

    public void setBuyer_uid(String buyer_uid) {
        this.buyer_uid = buyer_uid;
    }

    public String getBuy_nums() {
        return buy_nums;
    }

    public void setBuy_nums(String buy_nums) {
        this.buy_nums = buy_nums;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getBuy_total() {
        return buy_total;
    }

    public void setBuy_total(String buy_total) {
        this.buy_total = buy_total;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public Object getPay_img() {
        return pay_img;
    }

    public void setPay_img(Object pay_img) {
        this.pay_img = pay_img;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar_thumb() {
        return avatar_thumb;
    }

    public void setAvatar_thumb(String avatar_thumb) {
        this.avatar_thumb = avatar_thumb;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }
}
