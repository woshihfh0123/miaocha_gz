package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.List;

/**
 * created by WWL on 2019/5/8 0008:17
 */
public class WrittingCommentVO implements Serializable {


    /**
     * code : 200
     * msg : 请求成功
     * time : 1557306091
     * data : {"id":"1","order_no":"2019050618430001","order_goods":[{"order_goods_id":"1","name":"农大姐妹 香椿 冷链现货香椿芽香春芽露天头茬红香椿山东临朐春芽菜","image":"http://cailanzi.test.hbbeisheng.com/upload/goods/20190506/9ea1b709fa3b0fee9aab47d0fe6c6c723.jpg"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * order_no : 2019050618430001
         * order_goods : [{"order_goods_id":"1","name":"农大姐妹 香椿 冷链现货香椿芽香春芽露天头茬红香椿山东临朐春芽菜","image":"http://cailanzi.test.hbbeisheng.com/upload/goods/20190506/9ea1b709fa3b0fee9aab47d0fe6c6c723.jpg"}]
         */

//        private String id;
//        private String order_no;
        private List<OrderGoodsBean> list;

        public List<OrderGoodsBean> getList() {
            return list;
        }

        public void setList(List<OrderGoodsBean> list) {
            this.list = list;
        }
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getOrder_no() {
//            return order_no;
//        }
//
//        public void setOrder_no(String order_no) {
//            this.order_no = order_no;
//        }
//
//        public List<OrderGoodsBean> getOrder_goods() {
//            return order_goods;
//        }
//
//        public void setOrder_goods(List<OrderGoodsBean> order_goods) {
//            this.order_goods = order_goods;
//        }

        public static class OrderGoodsBean implements Serializable{
            /**
             * order_goods_id : 1
             * name : 农大姐妹 香椿 冷链现货香椿芽香春芽露天头茬红香椿山东临朐春芽菜
             * image : http://cailanzi.test.hbbeisheng.com/upload/goods/20190506/9ea1b709fa3b0fee9aab47d0fe6c6c723.jpg
             */
            private String id;
            private String unique;
            private String title;
            private String image;
            private String suk;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUnique() {
                return unique;
            }

            public void setUnique(String unique) {
                this.unique = unique;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getSuk() {
                return suk;
            }

            public void setSuk(String suk) {
                this.suk = suk;
            }
        }
    }
}
