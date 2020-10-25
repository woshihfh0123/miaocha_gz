package com.mc.phonelive.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RefundORderBean implements Serializable {

    /**
     * code : 0
     * msg :
     * info : {"id":"14","order_id":"137","uid":"11908","store_id":"14","refund_status":"2","refund_reason_wap_explain":"1","refund_reason_time":"1590562556","refund_reason_wap":"","refund_reason":"3333444555555","order_goods_id":"120","refund_price":"1.00","refund_image":[],"account_time":"1590572491","goods_id":"112","goods_name":"文件夹多功能a4定制开单本写字夹","goods_num":"1","goods_image":"http://youpei.yanshi.hbbeisheng.com/data/upload/store/5ec64accbd669.png","update_time":"1590572490","refund_no":"H200527145556120698119","pay_sn":"350643904503510908","pay_type":"2","trade_no":"2","refund_reason_id":"1","delete_state":"0","reason_name":"不喜欢","mobile":"15926888140"}
     */

    private int code;
    private String msg;
    private InfoBean info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 14
         * order_id : 137
         * uid : 11908
         * store_id : 14
         * refund_status : 2
         * refund_reason_wap_explain : 1
         * refund_reason_time : 1590562556
         * refund_reason_wap :
         * refund_reason : 3333444555555
         * order_goods_id : 120
         * refund_price : 1.00
         * refund_image : []
         * account_time : 1590572491
         * goods_id : 112
         * goods_name : 文件夹多功能a4定制开单本写字夹
         * goods_num : 1
         * goods_image : http://youpei.yanshi.hbbeisheng.com/data/upload/store/5ec64accbd669.png
         * update_time : 1590572490
         * refund_no : H200527145556120698119
         * pay_sn : 350643904503510908
         * pay_type : 2
         * trade_no : 2
         * refund_reason_id : 1
         * delete_state : 0
         * reason_name : 不喜欢
         * mobile : 15926888140
         */

        private String id;
        private String order_id;
        private String uid;
        private String store_id;
        private String refund_status;
        private String refund_reason_wap_explain;
        private String refund_reason_time;
        private String refund_reason_wap;
        private String refund_reason;
        private String order_goods_id;
        private String refund_price;
        private String account_time;
        private String goods_id;
        private String goods_name;
        private String goods_num;
        private String goods_image;
        private String update_time;
        private String refund_no;
        private String pay_sn;
        private String pay_type;
        private String trade_no;
        private String refund_reason_id;
        private String delete_state;
        private String reason_name;
        private String mobile;
        private List<?> refund_image;
        private List<RefundReasonListBean> refundReasonList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getRefund_status() {
            return refund_status;
        }

        public void setRefund_status(String refund_status) {
            this.refund_status = refund_status;
        }

        public String getRefund_reason_wap_explain() {
            return refund_reason_wap_explain;
        }

        public void setRefund_reason_wap_explain(String refund_reason_wap_explain) {
            this.refund_reason_wap_explain = refund_reason_wap_explain;
        }

        public String getRefund_reason_time() {
            return refund_reason_time;
        }

        public void setRefund_reason_time(String refund_reason_time) {
            this.refund_reason_time = refund_reason_time;
        }

        public String getRefund_reason_wap() {
            return refund_reason_wap;
        }

        public void setRefund_reason_wap(String refund_reason_wap) {
            this.refund_reason_wap = refund_reason_wap;
        }

        public String getRefund_reason() {
            return refund_reason;
        }

        public void setRefund_reason(String refund_reason) {
            this.refund_reason = refund_reason;
        }

        public String getOrder_goods_id() {
            return order_goods_id;
        }

        public void setOrder_goods_id(String order_goods_id) {
            this.order_goods_id = order_goods_id;
        }

        public String getRefund_price() {
            return refund_price;
        }

        public void setRefund_price(String refund_price) {
            this.refund_price = refund_price;
        }

        public String getAccount_time() {
            return account_time;
        }

        public void setAccount_time(String account_time) {
            this.account_time = account_time;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getRefund_no() {
            return refund_no;
        }

        public void setRefund_no(String refund_no) {
            this.refund_no = refund_no;
        }

        public String getPay_sn() {
            return pay_sn;
        }

        public void setPay_sn(String pay_sn) {
            this.pay_sn = pay_sn;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getRefund_reason_id() {
            return refund_reason_id;
        }

        public void setRefund_reason_id(String refund_reason_id) {
            this.refund_reason_id = refund_reason_id;
        }

        public String getDelete_state() {
            return delete_state;
        }

        public void setDelete_state(String delete_state) {
            this.delete_state = delete_state;
        }

        public String getReason_name() {
            return reason_name;
        }

        public void setReason_name(String reason_name) {
            this.reason_name = reason_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public List<?> getRefund_image() {
            return refund_image;
        }

        public void setRefund_image(List<?> refund_image) {
            this.refund_image = refund_image;
        }

        public List<RefundReasonListBean> getRefundReasonList() {
            return refundReasonList;
        }

        public void setRefundReasonList(List<RefundReasonListBean> refundReasonList) {
            this.refundReasonList = refundReasonList;
        }


        public static class RefundReasonListBean {
            /**
             * id : 1
             * uid : 1
             * status : 0
             * add_time : 1590113088
             * title : 不喜欢
             * update_time : null
             * is_show : 1
             */

            @SerializedName("id")
            private String id;
            @SerializedName("uid")
            private String uidX;
            private String status;
            private String add_time;
            private String title;
            @SerializedName("update_time")
            private Object update_timeX;
            private String is_show;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUidX() {
                return uidX;
            }

            public void setUidX(String uidX) {
                this.uidX = uidX;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getUpdate_timeX() {
                return update_timeX;
            }

            public void setUpdate_timeX(Object update_timeX) {
                this.update_timeX = update_timeX;
            }

            public String getIs_show() {
                return is_show;
            }

            public void setIs_show(String is_show) {
                this.is_show = is_show;
            }
        }
    }
}
