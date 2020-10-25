package com.mc.phonelive.bean;

import java.util.List;

public class ShopGoodsAddBean {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"uid":"11521","store_id":"1","title":"商品名~2","goods_url":"https://www.2345.com/?k715892195","price":"100","ot_price":"1222","description":"详情简介","slider_image":"/api/upload/store/20200229/06363127669718948.png,/api/upload/store/20200229/06363127669718948.png.jpg","addtime":1582968767,"is_show":1,"id":"40"}]}
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
         * info : [{"uid":"11521","store_id":"1","title":"商品名~2","goods_url":"https://www.2345.com/?k715892195","price":"100","ot_price":"1222","description":"详情简介","slider_image":"/api/upload/store/20200229/06363127669718948.png,/api/upload/store/20200229/06363127669718948.png.jpg","addtime":1582968767,"is_show":1,"id":"40"}]
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
             * uid : 11521
             * store_id : 1
             * title : 商品名~2
             * goods_url : https://www.2345.com/?k715892195
             * price : 100
             * ot_price : 1222
             * description : 详情简介
             * slider_image : /api/upload/store/20200229/06363127669718948.png,/api/upload/store/20200229/06363127669718948.png.jpg
             * addtime : 1582968767
             * is_show : 1
             * id : 40
             */

            private String uid;
            private String store_id;
            private String title;
            private String goods_url;
            private String price;
            private String ot_price;
            private String description;
            private String slider_image;
            private int addtime;
            private int is_show;
            private String id;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

            public String getSlider_image() {
                return slider_image;
            }

            public void setSlider_image(String slider_image) {
                this.slider_image = slider_image;
            }

            public int getAddtime() {
                return addtime;
            }

            public void setAddtime(int addtime) {
                this.addtime = addtime;
            }

            public int getIs_show() {
                return is_show;
            }

            public void setIs_show(int is_show) {
                this.is_show = is_show;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
