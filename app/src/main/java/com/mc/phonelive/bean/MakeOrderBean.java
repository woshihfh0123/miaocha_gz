package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.List;

public class MakeOrderBean implements Serializable {

    /**
     * ifcart : 1
     * goodsinfo : [{"cart_num":"1","goods_attr":"","goods_id":"9"},{"cart_num":"1","goods_attr":"","goods_id":"8"}]
     * remark :
     * store_num : 2
     * store_freight : 免邮
     * store_id : 1
     * total : 46
     */

    private String ifcart;
    private String remark;
    private String store_num;
    private String store_freight;
    private String store_id;
    private String total;
    private List<GoodsinfoBean> goodsinfo;

    public String getIfcart() {
        return ifcart;
    }

    public void setIfcart(String ifcart) {
        this.ifcart = ifcart;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStore_num() {
        return store_num;
    }

    public void setStore_num(String store_num) {
        this.store_num = store_num;
    }

    public String getStore_freight() {
        return store_freight;
    }

    public void setStore_freight(String store_freight) {
        this.store_freight = store_freight;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<GoodsinfoBean> getGoodsinfo() {
        return goodsinfo;
    }

    public void setGoodsinfo(List<GoodsinfoBean> goodsinfo) {
        this.goodsinfo = goodsinfo;
    }

    public static class GoodsinfoBean {
        /**
         * cart_num : 1
         * goods_attr :
         * goods_id : 9
         */

        private String cart_num;
        private String goods_attr;
        private String goods_id;

        public String getCart_num() {
            return cart_num;
        }

        public void setCart_num(String cart_num) {
            this.cart_num = cart_num;
        }

        public String getGoods_attr() {
            return goods_attr;
        }

        public void setGoods_attr(String goods_attr) {
            this.goods_attr = goods_attr;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }
    }
}
