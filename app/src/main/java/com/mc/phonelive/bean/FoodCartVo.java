package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车数据
 */
public class FoodCartVo implements Serializable {
    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"cart_list":[{"store_id":"5","store_name":"良品铺子","store_total":"59.7","goods":[{"id":"1","store_id":"5","goods_id":"91","cart_num":"2","is_check":"0","title":"良品铺子","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_total":"39.8"},{"id":"2","store_id":"5","goods_id":"90","cart_num":"1","is_check":"0","title":"良品铺子","goods_title":"凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920137895869.jpg","goods_total":"19.9"}]}],"total":"59.7"}]}
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
         * info : [{"cart_list":[{"store_id":"5","store_name":"良品铺子","store_total":"59.7","goods":[{"id":"1","store_id":"5","goods_id":"91","cart_num":"2","is_check":"0","title":"良品铺子","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_total":"39.8"},{"id":"2","store_id":"5","goods_id":"90","cart_num":"1","is_check":"0","title":"良品铺子","goods_title":"凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920137895869.jpg","goods_total":"19.9"}]}],"total":"59.7"}]
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
             * cart_list : [{"store_id":"5","store_name":"良品铺子","store_total":"59.7","goods":[{"id":"1","store_id":"5","goods_id":"91","cart_num":"2","is_check":"0","title":"良品铺子","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_total":"39.8"},{"id":"2","store_id":"5","goods_id":"90","cart_num":"1","is_check":"0","title":"良品铺子","goods_title":"凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920137895869.jpg","goods_total":"19.9"}]}]
             * total : 59.7
             */

            private String total;
            private List<CartListBean> cart_list;

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public List<CartListBean> getCart_list() {
                return cart_list;
            }

            public void setCart_list(List<CartListBean> cart_list) {
                this.cart_list = cart_list;
            }

            public static class CartListBean {
                /**
                 * store_id : 5
                 * store_name : 良品铺子
                 * store_total : 59.7
                 * goods : [{"id":"1","store_id":"5","goods_id":"91","cart_num":"2","is_check":"0","title":"良品铺子","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_total":"39.8"},{"id":"2","store_id":"5","goods_id":"90","cart_num":"1","is_check":"0","title":"良品铺子","goods_title":"凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920137895869.jpg","goods_total":"19.9"}]
                 */

                private String store_id;
                private String store_name;
                private String store_logo;
                private String store_total;
                private String ShopTotalPrice = "0";//自定义的总价
                private boolean shopcheck;
                private List<GoodsBean> goods;

                public String getStore_logo() {
                    return store_logo;
                }

                public void setStore_logo(String store_logo) {
                    this.store_logo = store_logo;
                }

                public String getShopTotalPrice() {
                    return ShopTotalPrice;
                }

                public void setShopTotalPrice(String shopTotalPrice) {
                    ShopTotalPrice = shopTotalPrice;
                }

                public boolean isShopcheck() {
                    return shopcheck;
                }

                public void setShopcheck(boolean shopcheck) {
                    this.shopcheck = shopcheck;
                }

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

                public String getStore_total() {
                    return store_total;
                }

                public void setStore_total(String store_total) {
                    this.store_total = store_total;
                }

                public List<GoodsBean> getGoods() {
                    return goods;
                }

                public void setGoods(List<GoodsBean> goods) {
                    this.goods = goods;
                }

                public static class GoodsBean {
                    /**
                     * id : 1
                     * store_id : 5
                     * goods_id : 91
                     * cart_num : 2
                     * is_check : 0
                     * title : 良品铺子
                     * goods_title : 泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食
                     * price : 19.9
                     * goods_image_url : http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg
                     * goods_total : 39.8
                     */

                    private String id;
                    private String store_id;
                    private String goods_id;
                    private String cart_num;
                    private String is_check;
                    private String title;
                    private String goods_title;
                    private String price;
                    private String goods_image_url;
                    private String goods_total;

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

                    public String getGoods_id() {
                        return goods_id;
                    }

                    public void setGoods_id(String goods_id) {
                        this.goods_id = goods_id;
                    }

                    public String getCart_num() {
                        return cart_num;
                    }

                    public void setCart_num(String cart_num) {
                        this.cart_num = cart_num;
                    }

                    public String getIs_check() {
                        return is_check;
                    }

                    public void setIs_check(String is_check) {
                        this.is_check = is_check;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
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

                    public String getGoods_image_url() {
                        return goods_image_url;
                    }

                    public void setGoods_image_url(String goods_image_url) {
                        this.goods_image_url = goods_image_url;
                    }

                    public String getGoods_total() {
                        return goods_total;
                    }

                    public void setGoods_total(String goods_total) {
                        this.goods_total = goods_total;
                    }
                }
            }
        }
    }


//
//
//    public int getRet() {
//        return ret;
//    }
//
//    public void setRet(int ret) {
//        this.ret = ret;
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//
//    public static class DataBean {
//        /**
//         * code : 0
//         * msg :
//         * info : [{"store_id":"5","store_name":"良品铺子","goods":[{"id":"2","store_id":"5","goods_id":"90","cart_num":"2","is_check":"0","title":"良品铺子","goods_title":"凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920137895869.jpg","goods_total":"39.8"},{"id":"1","store_id":"5","goods_id":"91","cart_num":"1","is_check":"0","title":"良品铺子","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_total":"19.9"}]}]
//         * total : 59.7
//         */
//
//        private int code;
//        private String msg;
//        private double total;
//        private List<InfoBean> info;
//
//        public int getCode() {
//            return code;
//        }
//
//        public void setCode(int code) {
//            this.code = code;
//        }
//
//        public String getMsg() {
//            return msg;
//        }
//
//        public void setMsg(String msg) {
//            this.msg = msg;
//        }
//
//        public double getTotal() {
//            return total;
//        }
//
//        public void setTotal(double total) {
//            this.total = total;
//        }
//
//        public List<InfoBean> getInfo() {
//            return info;
//        }
//
//        public void setInfo(List<InfoBean> info) {
//            this.info = info;
//        }
//
//        public static class InfoBean {
//            /**
//             * store_id : 5
//             * store_name : 良品铺子
//             * goods : [{"id":"2","store_id":"5","goods_id":"90","cart_num":"2","is_check":"0","title":"良品铺子","goods_title":"凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920137895869.jpg","goods_total":"39.8"},{"id":"1","store_id":"5","goods_id":"91","cart_num":"1","is_check":"0","title":"良品铺子","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_total":"19.9"}]
//             */
//
//            private String store_id;
//            private String store_name;
//            private String store_total;//店铺总价
//            private String ShopTotalPrice="0";//自定义的总价
//            private boolean shopcheck;
//            private List<GoodsBean> goods;
//
//            public String getShopTotalPrice() {
//                return ShopTotalPrice;
//            }
//
//            public void setShopTotalPrice(String shopTotalPrice) {
//                ShopTotalPrice = shopTotalPrice;
//            }
//
//            public String getStore_total() {
//                return store_total;
//            }
//
//            public void setStore_total(String store_total) {
//                this.store_total = store_total;
//            }
//
//            public boolean isShopcheck() {
//                return shopcheck;
//            }
//
//            public void setShopcheck(boolean shopcheck) {
//                this.shopcheck = shopcheck;
//            }
//
//            public String getStore_id() {
//                return store_id;
//            }
//
//            public void setStore_id(String store_id) {
//                this.store_id = store_id;
//            }
//
//            public String getStore_name() {
//                return store_name;
//            }
//
//            public void setStore_name(String store_name) {
//                this.store_name = store_name;
//            }
//
//            public List<GoodsBean> getGoods() {
//                return goods;
//            }
//
//            public void setGoods(List<GoodsBean> goods) {
//                this.goods = goods;
//            }
//
//            public static class GoodsBean {
//                /**
//                 * id : 2
//                 * store_id : 5
//                 * goods_id : 90
//                 * cart_num : 2
//                 * is_check : 0
//                 * title : 良品铺子
//                 * goods_title : 凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品
//                 * price : 19.9
//                 * goods_image_url : http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920137895869.jpg
//                 * goods_total : 39.8
//                 */
//
//                private String id;
//                private String store_id;
//                private String goods_id;
//                private String cart_num;
//                private String is_check;
//                private String title;
//                private String goods_title;
//                private String price;
//                private String goods_image_url;
//                private String goods_total;
//
//                public String getId() {
//                    return id;
//                }
//
//                public void setId(String id) {
//                    this.id = id;
//                }
//
//                public String getStore_id() {
//                    return store_id;
//                }
//
//                public void setStore_id(String store_id) {
//                    this.store_id = store_id;
//                }
//
//                public String getGoods_id() {
//                    return goods_id;
//                }
//
//                public void setGoods_id(String goods_id) {
//                    this.goods_id = goods_id;
//                }
//
//                public String getCart_num() {
//                    return cart_num;
//                }
//
//                public void setCart_num(String cart_num) {
//                    this.cart_num = cart_num;
//                }
//
//                public String getIs_check() {
//                    return is_check;
//                }
//
//                public void setIs_check(String is_check) {
//                    this.is_check = is_check;
//                }
//
//                public String getTitle() {
//                    return title;
//                }
//
//                public void setTitle(String title) {
//                    this.title = title;
//                }
//
//                public String getGoods_title() {
//                    return goods_title;
//                }
//
//                public void setGoods_title(String goods_title) {
//                    this.goods_title = goods_title;
//                }
//
//                public String getPrice() {
//                    return price;
//                }
//
//                public void setPrice(String price) {
//                    this.price = price;
//                }
//
//                public String getGoods_image_url() {
//                    return goods_image_url;
//                }
//
//                public void setGoods_image_url(String goods_image_url) {
//                    this.goods_image_url = goods_image_url;
//                }
//
//                public String getGoods_total() {
//                    return goods_total;
//                }
//
//                public void setGoods_total(String goods_total) {
//                    this.goods_total = goods_total;
//                }
//            }
//        }
//    }
}
