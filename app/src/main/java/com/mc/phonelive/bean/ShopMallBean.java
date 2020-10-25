package com.mc.phonelive.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * created by WWL on 2020/4/17 0017:13
 */
public class ShopMallBean extends BaseVO {


    /**
     * data : {"code":0,"msg":"","info":[{"slide":[{"slide_pic":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/20200413/5e94013fbca7a.jpg","slide_url":"","info_id":"0","goods_store_uid":"0"},{"slide_pic":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/20200413/5e94014e14adc.jpg","slide_url":"","info_id":"0","goods_store_uid":"0"}],"list":[{"id":"91","uid":"11511","title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.90","ot_price":"28.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg"},{"id":"89","uid":"11511","title":"凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品","price":"19.90","ot_price":"28.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920120905176.jpg"},{"id":"88","uid":"11511","title":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","price":"15.80","ot_price":"18.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg"},{"id":"84","uid":"11496","title":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","price":"15.80","ot_price":"18.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg"},{"id":"78","uid":"11521","title":"营养快餐","price":"10.00","ot_price":"100.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200305/06367191637794114.png"}]}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * code : 0
         * msg :
         * info : [{"slide":[{"slide_pic":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/20200413/5e94013fbca7a.jpg","slide_url":"","info_id":"0","goods_store_uid":"0"},{"slide_pic":"http://zhiboshow.yanshi.hbbeisheng.com/data/upload/20200413/5e94014e14adc.jpg","slide_url":"","info_id":"0","goods_store_uid":"0"}],"list":[{"id":"91","uid":"11511","title":"泰国进口贝尔原味奶片50g罐装100片儿童宝宝补钙奶糖果休闲零食 泰国原装进口 萌萌奶牛造型 宝宝喜爱零食","price":"19.90","ot_price":"28.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371926367127534.jpg"},{"id":"89","uid":"11511","title":"凯黛琳新疆和田精品免洗红枣大枣礼盒2000G整箱特产零食 节日礼品","price":"19.90","ot_price":"28.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371920120905176.jpg"},{"id":"88","uid":"11511","title":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","price":"15.80","ot_price":"18.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg"},{"id":"84","uid":"11496","title":"马来西亚进口EGO金小熊夹心小饼干零食约10g*20袋网红下午茶","price":"15.80","ot_price":"18.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200310/06371912957984294.jpg"},{"id":"78","uid":"11521","title":"营养快餐","price":"10.00","ot_price":"100.00","goods_image":"http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200305/06367191637794114.png"}]}]
         */

        private int code;
        @SerializedName("msg")
        private String msgX;
        private List<InfoBean> info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsgX() {
            return msgX;
        }

        public void setMsgX(String msgX) {
            this.msgX = msgX;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean implements Serializable{
            private List<ShopMallPicBean> slide;
            private List<ShopMallGoodsList> list;
            private List<ShopMallMenuList> category;
            private List<Categorylist> categorylist;

            public List<Categorylist> getCategorylist() {
                return categorylist;
            }

            public void setCategorylist(List<Categorylist> categorylist) {
                this.categorylist = categorylist;
            }

            public List<ShopMallPicBean> getSlide() {
                return slide;
            }

            public void setSlide(List<ShopMallPicBean> slide) {
                this.slide = slide;
            }

            public List<ShopMallGoodsList> getList() {
                return list;
            }

            public void setList(List<ShopMallGoodsList> list) {
                this.list = list;
            }

            public List<ShopMallMenuList> getCategory() {
                return category;
            }

            public void setCategory(List<ShopMallMenuList> category) {
                this.category = category;
            }

            public static class  ShopMallMenuList implements Serializable {
               private String id;//: "1",
                private String cate_name;//: "好空气",
                private String thumb;//: "http://zhiboshow.yanshi.hbbeisheng.com/data/upload/20200418/5e9a5afb6fb4b.png",
                private String web_url;//: ""

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getCate_name() {
                    return cate_name;
                }

                public void setCate_name(String cate_name) {
                    this.cate_name = cate_name;
                }

                public String getThumb() {
                    return thumb;
                }

                public void setThumb(String thumb) {
                    this.thumb = thumb;
                }

                public String getWeb_url() {
                    return web_url;
                }

                public void setWeb_url(String web_url) {
                    this.web_url = web_url;
                }
            }
        }
    }
}
