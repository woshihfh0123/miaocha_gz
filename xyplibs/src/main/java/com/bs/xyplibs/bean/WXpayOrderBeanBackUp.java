package com.bs.xyplibs.bean;

/**
 * Created by Administrator on 2018/4/4.
 * 不能出现package关键字
 */

public class WXpayOrderBeanBackUp {


    /**
     * code : 200
     * desc : 订单创建成功
     * time : 1565429598
     * data : {"wxpay_params":{"appid":"wx7362e75636eaf966","partnerid":"1501545291","prepayid":"wx10173318921073dbb57ad7281280215000","timestamp":"1565429598","noncestr":"BagpOtZExyBqrXCU","package":"Sign=WXPay","sign":"088AB697D21B0993B02EC39236645A7D"}}
     */

    private String code;
    private String desc;
    private String time;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * wxpay_params : {"appid":"wx7362e75636eaf966","partnerid":"1501545291","prepayid":"wx10173318921073dbb57ad7281280215000","timestamp":"1565429598","noncestr":"BagpOtZExyBqrXCU","package":"Sign=WXPay","sign":"088AB697D21B0993B02EC39236645A7D"}
         */

        private WxpayParamsBean wxpay_params;

        public WxpayParamsBean getWxpay_params() {
            return wxpay_params;
        }

        public void setWxpay_params(WxpayParamsBean wxpay_params) {
            this.wxpay_params = wxpay_params;
        }

        public static class WxpayParamsBean {
            /**
             * appid : wx7362e75636eaf966
             * partnerid : 1501545291
             * prepayid : wx10173318921073dbb57ad7281280215000
             * timestamp : 1565429598
             * noncestr : BagpOtZExyBqrXCU
             * package : Sign=WXPay
             * sign : 088AB697D21B0993B02EC39236645A7D
             */

            private String appid;
            private String partnerid;
            private String prepayid;
            private String timestamp;
            private String noncestr;
            private String pkg;
            private String sign;

            public String getPkg() {
                return pkg;
            }

            public void setPkg(String pkg) {
                this.pkg = pkg;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
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

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }


            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }
        }
    }
}
