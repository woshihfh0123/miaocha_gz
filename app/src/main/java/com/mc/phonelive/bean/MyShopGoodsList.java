package com.mc.phonelive.bean;

import java.util.List;

/**直播下面的店铺列表
 */
public class MyShopGoodsList {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"goods_list":[{"id":"84","goods_url":"https://chaoshi.detail.tmall.com/item.htm?spm=a3204.7084713.1998630275.2.73XU8B&pos=2&acm=201507200.1003.1.1236270&id=45803290554&scm=1003.1.201507200.13_45803290554_1236270","price":"15.8","ot_price":"18","description":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","is_show":"1","goods_name":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","img_list":["http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg"],"is_show_name":"上架"}],"store_info":{"id":"3","uid":"11491","title":"BruceLee","logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e677e4e87087.jpg","info":"什么都卖"}}]}
     * msg :
     */

    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * code : 0
         * msg :
         * info : [{"goods_list":[{"id":"84","goods_url":"https://chaoshi.detail.tmall.com/item.htm?spm=a3204.7084713.1998630275.2.73XU8B&pos=2&acm=201507200.1003.1.1236270&id=45803290554&scm=1003.1.201507200.13_45803290554_1236270","price":"15.8","ot_price":"18","description":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","is_show":"1","goods_name":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","img_list":["http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg"],"is_show_name":"上架"}],"store_info":{"id":"3","uid":"11491","title":"BruceLee","logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e677e4e87087.jpg","info":"什么都卖"}}]
         */

        private int code;
        private String msg;
        private List<InfoBean> info;

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

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * goods_list : [{"id":"84","goods_url":"https://chaoshi.detail.tmall.com/item.htm?spm=a3204.7084713.1998630275.2.73XU8B&pos=2&acm=201507200.1003.1.1236270&id=45803290554&scm=1003.1.201507200.13_45803290554_1236270","price":"15.8","ot_price":"18","description":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","is_show":"1","goods_name":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","img_list":["http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg"],"is_show_name":"上架"}]
             * store_info : {"id":"3","uid":"11491","title":"BruceLee","logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e677e4e87087.jpg","info":"什么都卖"}
             */

            private StoreInfoBean store_info;
            private List<GoodsListBean> goods_list;

            public StoreInfoBean getStore_info() {
                return store_info;
            }

            public void setStore_info(StoreInfoBean store_info) {
                this.store_info = store_info;
            }

            public List<GoodsListBean> getGoods_list() {
                return goods_list;
            }

            public void setGoods_list(List<GoodsListBean> goods_list) {
                this.goods_list = goods_list;
            }

            public static class StoreInfoBean {
                /**
                 * id : 3
                 * uid : 11491
                 * title : BruceLee
                 * logo : http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e677e4e87087.jpg
                 * info : 什么都卖
                 */

                private String id;
                private String uid;
                private String title;
                private String logo;
                private String info;

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

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }
            }

            public static class GoodsListBean {
                /**
                 * id : 84
                 * goods_url : https://chaoshi.detail.tmall.com/item.htm?spm=a3204.7084713.1998630275.2.73XU8B&pos=2&acm=201507200.1003.1.1236270&id=45803290554&scm=1003.1.201507200.13_45803290554_1236270
                 * price : 15.8
                 * ot_price : 18
                 * description : 马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶
                 * is_show : 1
                 * goods_name : 马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶
                 * img_list : ["http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg"]
                 * is_show_name : 上架
                 */

                private String id;
                private String goods_url;
                private String price;
                private String ot_price;
                private String description;
                private String is_show;
                private String goods_name;
                private String is_show_name;
                private String stock;
                private String[] img_list;

                public String getStock() {
                    return stock;
                }

                public void setStock(String stock) {
                    this.stock = stock;
                }

                public String[] getImg_list() {
                    return img_list;
                }

                public void setImg_list(String[] img_list) {
                    this.img_list = img_list;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getGoods_url() {
                    return goods_url;
                }

                public void setGoods_url(String goods_url) {
                    this.goods_url = goods_url;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getOt_price() {
                    return ot_price;
                }

                public void setOt_price(String ot_price) {
                    this.ot_price = ot_price;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getIs_show() {
                    return is_show;
                }

                public void setIs_show(String is_show) {
                    this.is_show = is_show;
                }

                public String getGoods_name() {
                    return goods_name;
                }

                public void setGoods_name(String goods_name) {
                    this.goods_name = goods_name;
                }

                public String getIs_show_name() {
                    return is_show_name;
                }

                public void setIs_show_name(String is_show_name) {
                    this.is_show_name = is_show_name;
                }


            }
        }
    }
}
