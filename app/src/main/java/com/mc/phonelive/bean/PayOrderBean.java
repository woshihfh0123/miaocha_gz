package com.mc.phonelive.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayOrderBean {
    private String store_id;
    private String store_name;
    private String store_logo;
    private String store_freight;
    private String store_num;
    private String store_total;
    private String ifcart;
    private List<GoodsBean> goods;
    private List<YhqSelBean> list;


    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_logo() {
        return store_logo;
    }

    public void setStore_logo(String store_logo) {
        this.store_logo = store_logo;
    }

    public String getStore_freight() {
        return store_freight;
    }

    public void setStore_freight(String store_freight) {
        this.store_freight = store_freight;
    }

    public String getStore_num() {
        return store_num;
    }

    public void setStore_num(String store_num) {
        this.store_num = store_num;
    }

    public String getStore_total() {
        return store_total;
    }

    public void setStore_total(String store_total) {
        this.store_total = store_total;
    }

    public String getIfcart() {
        return ifcart;
    }

    public void setIfcart(String ifcart) {
        this.ifcart = ifcart;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public List<YhqSelBean> getList() {
        return list;
    }

    public void setList(List<YhqSelBean> list) {
        this.list = list;
    }

    public static class GoodsBean {
        /**
         * id : 109
         * store_id : 11
         * goods_title : 大西瓜
         * price : 9.8
         * goods_freight : 15
         * goods_image_url : http://miaocha.yanshi.hbbeisheng.com/uploads/goods/20200629/40a604028fa8b31dea295d4c8e1600bb.jpg
         * cart_num : 1
         */

        private String id;
        private String store_id;
        private String goods_title;
        private String price;
        private String goods_freight;
        private String goods_image_url;
        private String cart_num;
        private String goods_attr;

        public String getGoods_attr() {
            return goods_attr;
        }

        public void setGoods_attr(String goods_attr) {
            this.goods_attr = goods_attr;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getGoods_title() {
            return goods_title;
        }

        public void setGoods_title(String goods_title) {
            this.goods_title = goods_title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGoods_freight() {
            return goods_freight;
        }

        public void setGoods_freight(String goods_freight) {
            this.goods_freight = goods_freight;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }

        public String getCart_num() {
            return cart_num;
        }

        public void setCart_num(String cart_num) {
            this.cart_num = cart_num;
        }
    }

    public static class ListBean {
        /**
         * id : 16
         * uid : 4
         * store_id : 1
         * goods_id : 0
         * type : 0
         * title : 双十一优惠
         * price : 5.00
         * market_price : 20.00
         * desc : 满20减5块
         * storage : 100
         * nums : 3
         * create_time : 1601100585
         * start_date : 2020-09-23
         * end_date : 2020-09-30
         * is_show : 1
         * status : 0
         * name : 满20.00减5.00
         * status_txt : 未使用
         * enddate : 有效期至2020/09/30
         */

        private String id;
        private String uid;
        @SerializedName("store_id")
        private String store_idX;
        private String goods_id;
        private String type;
        private String title;
        private String price;
        private String market_price;
        private String desc;
        private String storage;
        private String nums;
        private String create_time;
        private String start_date;
        private String end_date;
        private String is_show;
        private String status;
        private String name;
        private String status_txt;
        private String enddate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getStore_idX() {
            return store_idX;
        }

        public void setStore_idX(String store_idX) {
            this.store_idX = store_idX;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        public String getNums() {
            return nums;
        }

        public void setNums(String nums) {
            this.nums = nums;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus_txt() {
            return status_txt;
        }

        public void setStatus_txt(String status_txt) {
            this.status_txt = status_txt;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }
    }
}
