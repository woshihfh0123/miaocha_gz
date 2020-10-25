package com.mc.phonelive.bean;

import java.util.List;

/**
 * 店铺详情
 */
public class MyShopDetailBean {

    /**
     * about_list : [{"description":"哈哈哈哈","goods_name":"刚好","goods_url":"哈哈","id":"7","img_list":[],"ot_price":"255","price":"15","slider_image":"","title":"刚好"},{"description":"哈哈哈哈","goods_name":"刚好","goods_url":"哈哈","id":"6","img_list":[],"ot_price":"255","price":"15","slider_image":"","title":"刚好"},{"description":"哈哈哈哈","goods_name":"刚好","goods_url":"哈哈","id":"5","img_list":[],"ot_price":"255","price":"15","slider_image":"","title":"刚好"},{"description":"哈哈哈哈","goods_name":"刚好","goods_url":"哈哈","id":"4","img_list":[],"ot_price":"255","price":"15","slider_image":"","title":"刚好"},{"description":"哈哈哈哈","goods_name":"刚好","goods_url":"哈哈","id":"3","img_list":[],"ot_price":"255","price":"15","slider_image":"","title":"刚好"},{"description":"越多越好","goods_name":"来点吃的","goods_url":"http://www.baidu.com","id":"2","img_list":[],"ot_price":"199","price":"100","slider_image":"","title":"来点吃的"}]
     * description : 康师傅红烧牛肉面康师傅红烧牛肉面康师傅红烧牛肉面康师傅红烧牛肉面康师傅红烧牛肉面康师傅红烧牛肉面康师傅红烧牛肉面康师傅红烧牛肉面康师傅红烧牛肉面康师傅红烧牛肉面
     * goods_url : hhtp:///www.taobao.com
     * id : 1
     * img_list : ["http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e58cca303335.png","http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e58cc8e047d5.jpg"]
     * ot_price : 34
     * price : 22
     * store_info : {"id":"1","info":"","logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e58cc862d9ec.png","title":"可口可乐","uid":"11521"}
     * title : 康师傅红烧牛肉面
     */

    private String description;//下面的商品描述

    private String goods_url;
    private String id;
    private String ot_price;
    private String store_id;
    private String price;
    private String pull;
    private String is_show;//1表示上架   2表示下架
    private String stock;
    private String is_show_name;
    private String goods_freight;//: "0.00",
    private String  sales;//: "0",
    private StoreInfoBean store_info;
    private String iscollect;
    private InfoBean info;
    private String title;
    private String uid;
    private String content;
//    private String info;//商品简介
    private String comment_num;//评论条数
    private List<AboutListBean> about_list;
    private List<CommentVO.DataBean.ListBean> comment_list;
    private String[] img_list;


    public String getIscollect() {
        return iscollect;
    }

    public void setIscollect(String iscollect) {
        this.iscollect = iscollect;
    }

    public String getPull() {
        return pull;
    }

    public void setPull(String pull) {
        this.pull = pull;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }
    //    public String getInfo() {
//        return info;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }

    public String getGoods_freight() {
        return goods_freight;
    }

    public void setGoods_freight(String goods_freight) {
        this.goods_freight = goods_freight;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

    public String getIs_show_name() {
        return is_show_name;
    }

    public void setIs_show_name(String is_show_name) {
        this.is_show_name = is_show_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public void setGoods_url(String goods_url) {
        this.goods_url = goods_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOt_price() {
        return ot_price;
    }

    public void setOt_price(String ot_price) {
        this.ot_price = ot_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public StoreInfoBean getStore_info() {
        return store_info;
    }

    public void setStore_info(StoreInfoBean store_info) {
        this.store_info = store_info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AboutListBean> getAbout_list() {
        return about_list;
    }

    public void setAbout_list(List<AboutListBean> about_list) {
        this.about_list = about_list;
    }

    public String[] getImg_list() {
        return img_list;
    }

    public void setImg_list(String[] img_list) {
        this.img_list = img_list;
    }

    public static class InfoBean {
        private String title;
        private String thumb;
        private String inventory;
        private String price;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getInventory() {
            return inventory;
        }

        public void setInventory(String inventory) {
            this.inventory = inventory;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
    public static class StoreInfoBean {
        /**
         * id : 1
         * info :
         * logo : http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e58cc862d9ec.png
         * title : 可口可乐
         * uid : 11521
         */

        private String id;
        private String info;
        private String logo;
        private String title;
        private String uid;
        private String mobile;
        private String counts;
        public String getCounts() {
            return counts;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setCounts(String counts) {
            this.counts = counts;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    public List<CommentVO.DataBean.ListBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<CommentVO.DataBean.ListBean> comment_list) {
        this.comment_list = comment_list;
    }

    public static class AboutListBean {
        /**
         * description : 哈哈哈哈
         * goods_name : 刚好
         * goods_url : 哈哈
         * id : 7
         * img_list : []
         * ot_price : 255
         * price : 15
         * slider_image :
         * title : 刚好
         */

        private String description;
        private String goods_name;
        private String goods_url;
        private String id;
        private String ot_price;
        private String price;
        private String slider_image;
        private String title;
        private String[] img_list;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_url() {
            return goods_url;
        }

        public void setGoods_url(String goods_url) {
            this.goods_url = goods_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOt_price() {
            return ot_price;
        }

        public void setOt_price(String ot_price) {
            this.ot_price = ot_price;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSlider_image() {
            return slider_image;
        }

        public void setSlider_image(String slider_image) {
            this.slider_image = slider_image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String[] getImg_list() {
            return img_list;
        }

        public void setImg_list(String[] img_list) {
            this.img_list = img_list;
        }
    }

}
