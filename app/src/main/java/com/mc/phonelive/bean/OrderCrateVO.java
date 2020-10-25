package com.mc.phonelive.bean;

import java.io.Serializable;

/**
 * Created by wwl on
 */

public class OrderCrateVO  implements Serializable {

    /**
     * data : {"rate":"0.13333330","order_id":"164","out_trade_no":"151582007838911000","order_type":"1","pay_money":"34825.00","pay_rmb":"4643.33"}
     */
    private String time;

    private DataBean data;


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

    public static class DataBean implements Serializable {
        /**
         * rate : 0.13333330
         * order_id : 164
         * out_trade_no : 151582007838911000
         * order_type : 1
         * pay_money : 34825.00
         * pay_rmb : 4643.33
         */

        private String rate;
        private String order_id;
        private String out_trade_no;
        private String order_type;
        private String pay_money;
        private String pay_rmb;
        private String mType;//0:充值；1：商城

        public String getmType() {
            return mType;
        }

        public void setmType(String mType) {
            this.mType = mType;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getPay_money() {
            return pay_money;
        }

        public void setPay_money(String pay_money) {
            this.pay_money = pay_money;
        }

        public String getPay_rmb() {
            return pay_rmb;
        }

        public void setPay_rmb(String pay_rmb) {
            this.pay_rmb = pay_rmb;
        }
    }
}
