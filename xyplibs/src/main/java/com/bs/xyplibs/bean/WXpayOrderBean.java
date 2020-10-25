package com.bs.xyplibs.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/4/4.
 * 不能出现package关键字
 */

public class WXpayOrderBean {


    /**
     * success : true
     * code : 0000
     * message : SUCCESS
     * data : {"appid":"wx7d4861d828ae9e12","noncestr":"141420364e3743a3839f1f67cb3d3070","package":"Sign=WXPay","partnerid":"1538362501","prepayid":"wx131349333545863ce4175aac1837728900","timestamp":1573624173,"sign":"576ED90E256AACB3EEA602261EB8129B"}
     */

    private boolean success;
    private String code;
    private String message;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * appid : wx7d4861d828ae9e12
         * noncestr : 141420364e3743a3839f1f67cb3d3070
         * package : Sign=WXPay
         * partnerid : 1538362501
         * prepayid : wx131349333545863ce4175aac1837728900
         * timestamp : 1573624173
         * sign : 576ED90E256AACB3EEA602261EB8129B
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private int timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
