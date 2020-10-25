package com.bs.xyplibs.bean;

import com.bs.xyplibs.base.BaseVO;

import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 */

public class TabTwoBean extends BaseVO<TabTwoBean.DataBean>{


    /**
     * data : {"count":"1","page":"1","list":[{"id":"1","uid":"3","title":"chelly","address":"今天有优惠啦，全场6.8折，啤酒免费畅饮","release_time":"3个月前","longitude":"1555397449","latitude":"112.114147","is_read":"32","status":"1","username":"1","headpic":"http://thirdwx.qlogo.cn/mmopen/vi_32/rw5mLNEdt1MslVgm6SK2JrlAshxHMnY2J3H9nicwB2Y4xOKGzVOV5W7KrXH7ojEUZicES4YLS1nIC1cibwaibBo3Hw/132","images_list":["http://huangxy.test.hbbeisheng.com//uploads/business/dynamic/880.jpg","http://huangxy.test.hbbeisheng.com//uploads/business/dynamic/2116gp0.jpg"],"eval_list":[{"eval_id":"1","uid":"4","username":"我爱Miss_Tao","eval_content":"我要来,看起来很好吃的样子","eval_addtime":"3天前","eval_explain":"","reply_name":""}],"praise_list":[{"eval_id":"2","dynamic_id":"1","uid":"4","username":"我爱Miss_Tao"},{"eval_id":"3","dynamic_id":"1","uid":"3","username":"chelly"}]}]}
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
         * count : 1
         * page : 1
         * list : [{"id":"1","uid":"3","title":"chelly","address":"今天有优惠啦，全场6.8折，啤酒免费畅饮","release_time":"3个月前","longitude":"1555397449","latitude":"112.114147","is_read":"32","status":"1","username":"1","headpic":"http://thirdwx.qlogo.cn/mmopen/vi_32/rw5mLNEdt1MslVgm6SK2JrlAshxHMnY2J3H9nicwB2Y4xOKGzVOV5W7KrXH7ojEUZicES4YLS1nIC1cibwaibBo3Hw/132","images_list":["http://huangxy.test.hbbeisheng.com//uploads/business/dynamic/880.jpg","http://huangxy.test.hbbeisheng.com//uploads/business/dynamic/2116gp0.jpg"],"eval_list":[{"eval_id":"1","uid":"4","username":"我爱Miss_Tao","eval_content":"我要来,看起来很好吃的样子","eval_addtime":"3天前","eval_explain":"","reply_name":""}],"praise_list":[{"eval_id":"2","dynamic_id":"1","uid":"4","username":"我爱Miss_Tao"},{"eval_id":"3","dynamic_id":"1","uid":"3","username":"chelly"}]}]
         */

        private String count;
        private String page;
        private List<ListBean> list;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * uid : 3
             * title : chelly
             * address : 今天有优惠啦，全场6.8折，啤酒免费畅饮
             * release_time : 3个月前
             * longitude : 1555397449
             * latitude : 112.114147
             * is_read : 32
             * status : 1
             * username : 1
             * headpic : http://thirdwx.qlogo.cn/mmopen/vi_32/rw5mLNEdt1MslVgm6SK2JrlAshxHMnY2J3H9nicwB2Y4xOKGzVOV5W7KrXH7ojEUZicES4YLS1nIC1cibwaibBo3Hw/132
             * images_list : ["http://huangxy.test.hbbeisheng.com//uploads/business/dynamic/880.jpg","http://huangxy.test.hbbeisheng.com//uploads/business/dynamic/2116gp0.jpg"]
             * eval_list : [{"eval_id":"1","uid":"4","username":"我爱Miss_Tao","eval_content":"我要来,看起来很好吃的样子","eval_addtime":"3天前","eval_explain":"","reply_name":""}]
             * praise_list : [{"eval_id":"2","dynamic_id":"1","uid":"4","username":"我爱Miss_Tao"},{"eval_id":"3","dynamic_id":"1","uid":"3","username":"chelly"}]
             */

            private String id;
            private String shop_id;
            private String uid;
            private String title;
            private String address;
            private String release_time;
            private String longitude;
            private String latitude;
            private String is_read;
            private String status;
            private String username;
            private String headpic;
            private String istop;
            private List<String> images_list;
            private List<EvalListBean> eval_list;
            private List<PraiseListBean> praise_list;

            public String getIstop() {
                return istop;
            }

            public void setIstop(String istop) {
                this.istop = istop;
            }

            public String getShop_id() {
                return shop_id;
            }

            public void setShop_id(String shop_id) {
                this.shop_id = shop_id;
            }

            public ListBean() {
            }

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

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getRelease_time() {
                return release_time;
            }

            public void setRelease_time(String release_time) {
                this.release_time = release_time;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getIs_read() {
                return is_read;
            }

            public void setIs_read(String is_read) {
                this.is_read = is_read;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getHeadpic() {
                return headpic;
            }

            public void setHeadpic(String headpic) {
                this.headpic = headpic;
            }

            public List<String> getImages_list() {
                return images_list;
            }

            public void setImages_list(List<String> images_list) {
                this.images_list = images_list;
            }

            public List<EvalListBean> getEval_list() {
                return eval_list;
            }

            public void setEval_list(List<EvalListBean> eval_list) {
                this.eval_list = eval_list;
            }

            public List<PraiseListBean> getPraise_list() {
                return praise_list;
            }

            public void setPraise_list(List<PraiseListBean> praise_list) {
                this.praise_list = praise_list;
            }

            public static class EvalListBean {
                /**
                 * eval_id : 1
                 * uid : 4
                 * username : 我爱Miss_Tao
                 * eval_content : 我要来,看起来很好吃的样子
                 * eval_addtime : 3天前
                 * eval_explain :
                 * reply_name :
                 */

                private String eval_id;
                private String uid;
                private String username;
                private String eval_content;
                private String eval_addtime;
                private String eval_explain;
                private String reply_name;
                private String reply_id;

                public String getReply_id() {
                    return reply_id;
                }

                public void setReply_id(String reply_id) {
                    this.reply_id = reply_id;
                }

                public EvalListBean() {
                }

                public EvalListBean(String eval_id, String uid, String username, String eval_content, String eval_addtime, String eval_explain, String reply_name) {
                    this.eval_id = eval_id;
                    this.uid = uid;
                    this.username = username;
                    this.eval_content = eval_content;
                    this.eval_addtime = eval_addtime;
                    this.eval_explain = eval_explain;
                    this.reply_name = reply_name;
                }
                public EvalListBean(String reply_id,String eval_id, String uid, String username, String eval_content, String eval_addtime, String eval_explain, String reply_name) {
                    this.reply_id=reply_id;
                    this.eval_id = eval_id;
                    this.uid = uid;
                    this.username = username;
                    this.eval_content = eval_content;
                    this.eval_addtime = eval_addtime;
                    this.eval_explain = eval_explain;
                    this.reply_name = reply_name;
                }

                public String getEval_id() {
                    return eval_id;
                }

                public void setEval_id(String eval_id) {
                    this.eval_id = eval_id;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getEval_content() {
                    return eval_content;
                }

                public void setEval_content(String eval_content) {
                    this.eval_content = eval_content;
                }

                public String getEval_addtime() {
                    return eval_addtime;
                }

                public void setEval_addtime(String eval_addtime) {
                    this.eval_addtime = eval_addtime;
                }

                public String getEval_explain() {
                    return eval_explain;
                }

                public void setEval_explain(String eval_explain) {
                    this.eval_explain = eval_explain;
                }

                public String getReply_name() {
                    return reply_name;
                }

                public void setReply_name(String reply_name) {
                    this.reply_name = reply_name;
                }
            }

            public static class PraiseListBean {
                /**
                 * eval_id : 2
                 * dynamic_id : 1
                 * uid : 4
                 * username : 我爱Miss_Tao
                 */

                private String eval_id;
                private String dynamic_id;
                private String uid;
                private String username;

                public PraiseListBean(String eval_id, String dynamic_id, String uid, String username) {
                    this.eval_id = eval_id;
                    this.dynamic_id = dynamic_id;
                    this.uid = uid;
                    this.username = username;
                }

                public PraiseListBean() {
                }

                public String getEval_id() {
                    return eval_id;
                }

                public void setEval_id(String eval_id) {
                    this.eval_id = eval_id;
                }

                public String getDynamic_id() {
                    return dynamic_id;
                }

                public void setDynamic_id(String dynamic_id) {
                    this.dynamic_id = dynamic_id;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }
            }
        }
    }
}
