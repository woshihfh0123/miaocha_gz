package com.bs.xyplibs.bean;


import com.bs.xyplibs.base.BaseVO;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/23.
 */

public class UserBean extends BaseVO {


    /**
     * data : {"uid":"18","jiguang_alias":"Feidu_merchant_18","merchant_name":"邓家牛腩面"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * uid : 18
         * jiguang_alias : Feidu_merchant_18
         * merchant_name : 邓家牛腩面
         */

        private String uid;
        private String jiguang_alias;
        private String merchant_name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getJiguang_alias() {
            return jiguang_alias;
        }

        public void setJiguang_alias(String jiguang_alias) {
            this.jiguang_alias = jiguang_alias;
        }

        public String getMerchant_name() {
            return merchant_name;
        }

        public void setMerchant_name(String merchant_name) {
            this.merchant_name = merchant_name;
        }
    }
}
