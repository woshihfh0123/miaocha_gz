package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.List;

public class MyShopBean implements Serializable {


    /**
     * ret : 200
     * data : {"info":[{"storeinfo":{"id":"1","title":"可口可乐","logo":"http://miaocha.yanshi.hbbeisheng.com/uploads/goods/20200623/9ad991570e933cd733a53da409aa88f5.png","info":""},"list":[{"id":"1","store_id":"1","title":"康师傅红烧牛肉面","price":"22.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/data/upload/apply/5e58cca303335.png"},{"id":"34","store_id":"1","title":"商品名~","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363124714331143.jpg"},{"id":"35","store_id":"1","title":"商品名~2","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363125668193837.png"},{"id":"40","store_id":"1","title":"商品名~2","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363127669718948.png"},{"id":"43","store_id":"1","title":"商品名~3","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363134013384168.png"},{"id":"45","store_id":"1","title":"商品名~4","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363136964401876.jpg"},{"id":"77","store_id":"1","title":"商品xxx","price":"455.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200305/06367183236252641.jpg"},{"id":"78","store_id":"1","title":"营养快餐","price":"10.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200305/06367191637794114.png"}]}]}
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
        private List<InfoBean> info;

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * storeinfo : {"id":"1","title":"可口可乐","logo":"http://miaocha.yanshi.hbbeisheng.com/uploads/goods/20200623/9ad991570e933cd733a53da409aa88f5.png","info":""}
             * list : [{"id":"1","store_id":"1","title":"康师傅红烧牛肉面","price":"22.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/data/upload/apply/5e58cca303335.png"},{"id":"34","store_id":"1","title":"商品名~","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363124714331143.jpg"},{"id":"35","store_id":"1","title":"商品名~2","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363125668193837.png"},{"id":"40","store_id":"1","title":"商品名~2","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363127669718948.png"},{"id":"43","store_id":"1","title":"商品名~3","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363134013384168.png"},{"id":"45","store_id":"1","title":"商品名~4","price":"100.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200229/06363136964401876.jpg"},{"id":"77","store_id":"1","title":"商品xxx","price":"455.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200305/06367183236252641.jpg"},{"id":"78","store_id":"1","title":"营养快餐","price":"10.00","sales":"0","goods_img":"http://miaocha.yanshi.hbbeisheng.com/api/upload/store/20200305/06367191637794114.png"}]
             */

            private StoreinfoBean storeinfo;
            private List<ListBean> list;

            public StoreinfoBean getStoreinfo() {
                return storeinfo;
            }

            public void setStoreinfo(StoreinfoBean storeinfo) {
                this.storeinfo = storeinfo;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class StoreinfoBean {
                /**
                 * id : 1
                 * title : 可口可乐
                 * logo : http://miaocha.yanshi.hbbeisheng.com/uploads/goods/20200623/9ad991570e933cd733a53da409aa88f5.png
                 * info :
                 */

                private String id;
                private String title;
                private String logo;
                private String info;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

            public static class ListBean {
                /**
                 * id : 1
                 * store_id : 1
                 * title : 康师傅红烧牛肉面
                 * price : 22.00
                 * sales : 0
                 * goods_img : http://miaocha.yanshi.hbbeisheng.com/data/upload/apply/5e58cca303335.png
                 */

                private String id;
                private String store_id;
                private String title;
                private String price;
                private String sales;
                private String goods_img;

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

                public String getSales() {
                    return sales;
                }

                public void setSales(String sales) {
                    this.sales = sales;
                }

                public String getGoods_img() {
                    return goods_img;
                }

                public void setGoods_img(String goods_img) {
                    this.goods_img = goods_img;
                }
            }
        }
    }
}
