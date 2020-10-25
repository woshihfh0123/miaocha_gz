package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.List;

/**
 * created by WWL on 2019/4/26 0026:13
 * 我的评论列表
 */
public class CommentVO implements Serializable{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private int code;
        private String msg;
        private  List<ListBean> info;

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

        public List<ListBean> getInfo() {
            return info;
        }

        public void setInfo(List<ListBean> info) {
            this.info = info;
        }

        public static class ListBean implements Serializable{
            /**
             * goods_id : 92
             * goods_name : 测试看看
             * goods_image : /api/upload/store/20200311/06372392641764087.jpg
             * content : 徒弟
             * addtime : 1586503236
             * user_nicename : 手机用户2263
             * avatar_thumb : /data/upload/20200401/5e84114e7c694.png
             */

            private String goods_id;
            private String goods_name;
            private String goods_image;
            private String content;
            private String addtime;
            private String user_nicename;
            private String avatar_thumb;
            private String[] img_list;

            public String[] getImg_list() {
                return img_list;
            }

            public void setImg_list(String[] img_list) {
                this.img_list = img_list;
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

            public String getGoods_image() {
                return goods_image;
            }

            public void setGoods_image(String goods_image) {
                this.goods_image = goods_image;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getUser_nicename() {
                return user_nicename;
            }

            public void setUser_nicename(String user_nicename) {
                this.user_nicename = user_nicename;
            }

            public String getAvatar_thumb() {
                return avatar_thumb;
            }

            public void setAvatar_thumb(String avatar_thumb) {
                this.avatar_thumb = avatar_thumb;
            }

            public static class ImageBean implements Serializable{
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

    }
}
