package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单提交界面
 */
public class FoodOrderVo implements Serializable {
    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"store":[{"store_id":"5","store_name":"良品铺子","store_logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e6795360f854.jpg","store_total":"24.9","goods":[{"id":"91","store_id":"5","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_freight":"免邮","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","cart_num":"1"}]}],"addressinfo":{"id":"1","real_name":"旺仔","phone":"15571005523","district":"太平人寿保险有限公司襄阳中心支公司","detail":"湖北省襄阳市樊城区卧龙大道86号环球金融城1号楼17-19层"}}]}
     * msg :
     */


    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * store : [{"store_id":"5","store_name":"良品铺子","store_logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e6795360f854.jpg","store_total":"24.9","goods":[{"id":"91","store_id":"5","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_freight":"免邮","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","cart_num":"1"}]}]
         * addressinfo : {"id":"1","real_name":"旺仔","phone":"15571005523","district":"太平人寿保险有限公司襄阳中心支公司","detail":"湖北省襄阳市樊城区卧龙大道86号环球金融城1号楼17-19层"}
         */

        private AddressinfoBean addressinfo;
        private List<StoreBean> store;

        public AddressinfoBean getAddressinfo() {
            return addressinfo;
        }

        public void setAddressinfo(AddressinfoBean addressinfo) {
            this.addressinfo = addressinfo;
        }

        public List<StoreBean> getStore() {
            return store;
        }

        public void setStore(List<StoreBean> store) {
            this.store = store;
        }

        public static class AddressinfoBean {
            /**
             * id : 1
             * real_name : 旺仔
             * phone : 15571005523
             * district : 太平人寿保险有限公司襄阳中心支公司
             * detail : 湖北省襄阳市樊城区卧龙大道86号环球金融城1号楼17-19层
             */

            private String id;
            private String real_name;
            private String phone;
            private String district;
            private String detail;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getReal_name() {
                return real_name;
            }

            public void setReal_name(String real_name) {
                this.real_name = real_name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }
        }

        public static class StoreBean {
            /**
             * store_id : 5
             * store_name : 良品铺子
             * store_logo : http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e6795360f854.jpg
             * store_total : 24.9
             * goods : [{"id":"91","store_id":"5","goods_title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.9","goods_freight":"免邮","goods_image_url":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","cart_num":"1"}]
             */

            private String store_id;
            private String store_name;
            private String store_logo;
            private String store_total;
            private String store_freight;
            private String store_num;
            private String ifcart;//是否购物车提交 1是 0 否
            private String remark;//备注（自定义的）
            private List<GoodsBean> goods;

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

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getIfcart() {
                return ifcart;
            }

            public void setIfcart(String ifcart) {
                this.ifcart = ifcart;
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

            public String getStore_logo() {
                return store_logo;
            }

            public void setStore_logo(String store_logo) {
                this.store_logo = store_logo;
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
                 * id : 91
                 * store_id : 5
                 * goods_title : 泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食
                 * price : 19.9
                 * goods_freight : 免邮
                 * goods_image_url : http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg
                 * cart_num : 1
                 */

                private String id;
                private String store_id;
                private String goods_title;
                private String price;
                private String goods_freight;
                private String goods_image_url;
                private String cart_num;

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
        }
    }
}
