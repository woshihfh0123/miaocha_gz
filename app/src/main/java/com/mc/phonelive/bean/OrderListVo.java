package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单列表
 */
public class OrderListVo implements Serializable {
    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"id":"2","order_code":"D20040810432211521203415","pay_sn":"200639657802025521","uid":"11521","store_id":"5","store_name":"良品铺子","store_logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e6795360f854.jpg","buyer_id":"11521","buyer_name":"手机用户2262","explain":"","reciver_name":"旺仔","reciver_info":"湖北省襄阳市樊城区卧龙大道86号环球金融城1号楼17-19层","reciver_phone":"15571005523","total_num":"3","total_price":"59.70","total_postage":"5.00","pay_price":"64.70","pay_postage":"5.00","pay_time":"","pay_type":"微信","add_time":"2020-04-08 10:43:22","shipping_name":"","shipping_code":"","evaluation_time":"0","order_state":"0","refund_status":"0","refund_reason_wap_explain":"","refund_reason_time":"0","refund_reason_wap":"","refund_reason":"","refund_price":"0.00","delete_state":"0","order_state_name":"去付款","button_list":[{"id":"2","color":"#4595fd","title":"取消订单"},{"id":"2","color":"#4595fd","title":"去付款"}],"goods_list":[{"id":"2","order_id":"2","uid":"11521","store_id":"5","goods_id":"91","goods_name":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜","goods_price":"19.90","goods_num":"3","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_pay_price":"59.70"}]},{"id":"1","order_code":"D20040810413511521221543","pay_sn":"120639657695827521","uid":"11521","store_id":"5","store_name":"良品铺子","store_logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e6795360f854.jpg","buyer_id":"11521","buyer_name":"手机用户2262","explain":"","reciver_name":"旺仔","reciver_info":"湖北省襄阳市樊城区卧龙大道86号环球金融城1号楼17-19层","reciver_phone":"15571005523","total_num":"3","total_price":"47.40","total_postage":"0.00","pay_price":"47.40","pay_postage":"0.00","pay_time":"","pay_type":"支付宝","add_time":"2020-04-08 10:41:35","shipping_name":"","shipping_code":"","evaluation_time":"0","order_state":"0","refund_status":"0","refund_reason_wap_explain":"","refund_reason_time":"0","refund_reason_wap":"","refund_reason":"","refund_price":"0.00","delete_state":"0","order_state_name":"去付款","button_list":[{"id":"2","color":"#4595fd","title":"取消订单"},{"id":"2","color":"#4595fd","title":"去付款"}],"goods_list":[{"id":"1","order_id":"1","uid":"11521","store_id":"5","goods_id":"88","goods_name":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","goods_price":"15.80","goods_num":"3","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg","goods_pay_price":"47.40"}]}]}
     * msg :
     */

        /**
         * code : 0
         * msg :
         * info : [{"id":"2","order_code":"D20040810432211521203415","pay_sn":"200639657802025521","uid":"11521","store_id":"5","store_name":"良品铺子","store_logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e6795360f854.jpg","buyer_id":"11521","buyer_name":"手机用户2262","explain":"","reciver_name":"旺仔","reciver_info":"湖北省襄阳市樊城区卧龙大道86号环球金融城1号楼17-19层","reciver_phone":"15571005523","total_num":"3","total_price":"59.70","total_postage":"5.00","pay_price":"64.70","pay_postage":"5.00","pay_time":"","pay_type":"微信","add_time":"2020-04-08 10:43:22","shipping_name":"","shipping_code":"","evaluation_time":"0","order_state":"0","refund_status":"0","refund_reason_wap_explain":"","refund_reason_time":"0","refund_reason_wap":"","refund_reason":"","refund_price":"0.00","delete_state":"0","order_state_name":"去付款","button_list":[{"id":"2","color":"#4595fd","title":"取消订单"},{"id":"2","color":"#4595fd","title":"去付款"}],"goods_list":[{"id":"2","order_id":"2","uid":"11521","store_id":"5","goods_id":"91","goods_name":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜","goods_price":"19.90","goods_num":"3","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_pay_price":"59.70"}]},{"id":"1","order_code":"D20040810413511521221543","pay_sn":"120639657695827521","uid":"11521","store_id":"5","store_name":"良品铺子","store_logo":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e6795360f854.jpg","buyer_id":"11521","buyer_name":"手机用户2262","explain":"","reciver_name":"旺仔","reciver_info":"湖北省襄阳市樊城区卧龙大道86号环球金融城1号楼17-19层","reciver_phone":"15571005523","total_num":"3","total_price":"47.40","total_postage":"0.00","pay_price":"47.40","pay_postage":"0.00","pay_time":"","pay_type":"支付宝","add_time":"2020-04-08 10:41:35","shipping_name":"","shipping_code":"","evaluation_time":"0","order_state":"0","refund_status":"0","refund_reason_wap_explain":"","refund_reason_time":"0","refund_reason_wap":"","refund_reason":"","refund_price":"0.00","delete_state":"0","order_state_name":"去付款","button_list":[{"id":"2","color":"#4595fd","title":"取消订单"},{"id":"2","color":"#4595fd","title":"去付款"}],"goods_list":[{"id":"1","order_id":"1","uid":"11521","store_id":"5","goods_id":"88","goods_name":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","goods_price":"15.80","goods_num":"3","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg","goods_pay_price":"47.40"}]}]
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
             * id : 2
             * order_code : D20040810432211521203415
             * pay_sn : 200639657802025521
             * uid : 11521
             * store_id : 5
             * store_name : 良品铺子
             * store_logo : http://zhiboshow.yanshi.hbbeisheng.com/data/upload/apply/5e6795360f854.jpg
             * buyer_id : 11521
             * buyer_name : 手机用户2262
             * explain :
             * reciver_name : 旺仔
             * reciver_info : 湖北省襄阳市樊城区卧龙大道86号环球金融城1号楼17-19层
             * reciver_phone : 15571005523
             * total_num : 3
             * total_price : 59.70
             * total_postage : 5.00
             * pay_price : 64.70
             * pay_postage : 5.00
             * pay_time :
             * pay_type : 微信
             * add_time : 2020-04-08 10:43:22
             * shipping_name :
             * shipping_code :
             * evaluation_time : 0
             * order_state : 0
             * refund_status : 0
             * refund_reason_wap_explain :
             * refund_reason_time : 0
             * refund_reason_wap :
             * refund_reason :
             * refund_price : 0.00
             * delete_state : 0
             * order_state_name : 去付款
             * button_list : [{"id":"2","color":"#4595fd","title":"取消订单"},{"id":"2","color":"#4595fd","title":"去付款"}]
             * goods_list : [{"id":"2","order_id":"2","uid":"11521","store_id":"5","goods_id":"91","goods_name":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜","goods_price":"19.90","goods_num":"3","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg","goods_pay_price":"59.70"}]
             */

            private String id;
            private String order_code;
            private String pay_sn;
            private String uid;
            private String store_id;
            private String store_name;
            private String store_logo;
            private String buyer_id;
            private String buyer_name;
            private String explain;
            private String reciver_name;
            private String reciver_info;
            private String reciver_phone;
            private String total_num;
            private String total_price;
            private String total_postage;
            private String pay_price;
            private String pay_postage;
            private String pay_time;
            private String pay_type;
            private String add_time;
            private String shipping_name;
            private String shipping_code;
            private String evaluation_time;
            private String order_state;
            private String refund_status;
            private String refund_reason_wap_explain;
            private String refund_reason_time;
            private String refund_reason_wap;
            private String refund_reason;
            private String refund_price;
            private String delete_state;
            private String order_state_name;
            private List<ButtonListBean> button_list;
            private List<GoodsListBean> goods_list;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrder_code() {
                return order_code;
            }

            public void setOrder_code(String order_code) {
                this.order_code = order_code;
            }

            public String getPay_sn() {
                return pay_sn;
            }

            public void setPay_sn(String pay_sn) {
                this.pay_sn = pay_sn;
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

            public String getBuyer_id() {
                return buyer_id;
            }

            public void setBuyer_id(String buyer_id) {
                this.buyer_id = buyer_id;
            }

            public String getBuyer_name() {
                return buyer_name;
            }

            public void setBuyer_name(String buyer_name) {
                this.buyer_name = buyer_name;
            }

            public String getExplain() {
                return explain;
            }

            public void setExplain(String explain) {
                this.explain = explain;
            }

            public String getReciver_name() {
                return reciver_name;
            }

            public void setReciver_name(String reciver_name) {
                this.reciver_name = reciver_name;
            }

            public String getReciver_info() {
                return reciver_info;
            }

            public void setReciver_info(String reciver_info) {
                this.reciver_info = reciver_info;
            }

            public String getReciver_phone() {
                return reciver_phone;
            }

            public void setReciver_phone(String reciver_phone) {
                this.reciver_phone = reciver_phone;
            }

            public String getTotal_num() {
                return total_num;
            }

            public void setTotal_num(String total_num) {
                this.total_num = total_num;
            }

            public String getTotal_price() {
                return total_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }

            public String getTotal_postage() {
                return total_postage;
            }

            public void setTotal_postage(String total_postage) {
                this.total_postage = total_postage;
            }

            public String getPay_price() {
                return pay_price;
            }

            public void setPay_price(String pay_price) {
                this.pay_price = pay_price;
            }

            public String getPay_postage() {
                return pay_postage;
            }

            public void setPay_postage(String pay_postage) {
                this.pay_postage = pay_postage;
            }

            public String getPay_time() {
                return pay_time;
            }

            public void setPay_time(String pay_time) {
                this.pay_time = pay_time;
            }

            public String getPay_type() {
                return pay_type;
            }

            public void setPay_type(String pay_type) {
                this.pay_type = pay_type;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getShipping_name() {
                return shipping_name;
            }

            public void setShipping_name(String shipping_name) {
                this.shipping_name = shipping_name;
            }

            public String getShipping_code() {
                return shipping_code;
            }

            public void setShipping_code(String shipping_code) {
                this.shipping_code = shipping_code;
            }

            public String getEvaluation_time() {
                return evaluation_time;
            }

            public void setEvaluation_time(String evaluation_time) {
                this.evaluation_time = evaluation_time;
            }

            public String getOrder_state() {
                return order_state;
            }

            public void setOrder_state(String order_state) {
                this.order_state = order_state;
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

            public String getRefund_price() {
                return refund_price;
            }

            public void setRefund_price(String refund_price) {
                this.refund_price = refund_price;
            }

            public String getDelete_state() {
                return delete_state;
            }

            public void setDelete_state(String delete_state) {
                this.delete_state = delete_state;
            }

            public String getOrder_state_name() {
                return order_state_name;
            }

            public void setOrder_state_name(String order_state_name) {
                this.order_state_name = order_state_name;
            }

            public List<ButtonListBean> getButton_list() {
                return button_list;
            }

            public void setButton_list(List<ButtonListBean> button_list) {
                this.button_list = button_list;
            }

            public List<GoodsListBean> getGoods_list() {
                return goods_list;
            }

            public void setGoods_list(List<GoodsListBean> goods_list) {
                this.goods_list = goods_list;
            }

            public static class ButtonListBean {
                /**
                 * id : 2
                 * color : #4595fd
                 * title : 取消订单
                 */

                private String id;
                private String color;
                private String title;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }

            public static class GoodsListBean {
                /**
                 * id : 2
                 * order_id : 2
                 * uid : 11521
                 * store_id : 5
                 * goods_id : 91
                 * goods_name : 泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜
                 * goods_price : 19.90
                 * goods_num : 3
                 * goods_image : http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg
                 * goods_pay_price : 59.70
                 */

                private String id;
                private String order_id;
                private String uid;
                private String store_id;
                private String goods_id;
                private String goods_name;
                private String goods_price;
                private String goods_num;
                private String goods_image;
                private String goods_pay_price;

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

                public String getGoods_price() {
                    return goods_price;
                }

                public void setGoods_price(String goods_price) {
                    this.goods_price = goods_price;
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

                public String getGoods_pay_price() {
                    return goods_pay_price;
                }

                public void setGoods_pay_price(String goods_pay_price) {
                    this.goods_pay_price = goods_pay_price;
                }
            }
        }
//    public static List<OrderListVo.DataBean.ListBean> getGoodsList(){
//        List<OrderListVo.DataBean.ListBean> mList = new ArrayList<>();
//        for (int i=0;i<3;i++){
//            OrderListVo.DataBean.ListBean shoplist = new OrderListVo.DataBean.ListBean();
//            shoplist.setCombination_id(i+"");
//            shoplist.setDaishou_type("1");
//            shoplist.setLun_jinbi(")");
//            shoplist.setPay_type("1");
//            shoplist.setMark("备注");
//            shoplist.setOrder_id(""+i);
//            shoplist.setOut_trade_no("23749274832");
//            shoplist.setStatus_name("待付款");
//            shoplist.setStatus(i+"");
//            List<FoodOrderVo.InfoBean.StoreBean.GoodsBean> mGlist = new ArrayList<>();
//            for (int j=0;j<2;j++){
//                FoodOrderVo.InfoBean.StoreBean.GoodsBean goods = new FoodOrderVo.InfoBean.StoreBean.GoodsBean();
//                goods.setGoods_image_url("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4035618483,1210925427&fm=26&gp=0.jpg");goods.setGoods_freight("10");
//                goods.setCart_num("2");goods.setPrice("300");goods.setGoods_title("商品名称");
//                goods.setId(i+"");
//                mGlist.add(goods);
//            }
//            shoplist.setCartInfo(mGlist);
//            mList.add(shoplist);
//        }
//
//        return mList;
//    }


}