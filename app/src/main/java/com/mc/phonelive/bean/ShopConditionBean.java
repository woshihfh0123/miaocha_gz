package com.mc.phonelive.bean;

import java.util.List;

public class ShopConditionBean {


    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"sys_fans":"10","empirical":"3","bond":"10","fans_pic":"http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/fans.png","empirical_pic":"http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/empirical.png","bond_pic":"http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/bond.png","equity":"1.个人主页拥有我的店铺的入口； 2.在直播间内添加直播购物车； 3.发布短视频时可以关联商品；","sys_fans_id":"0","sys_fans_title":"未完成","empirical_id":"1","empirical_title":"已完成","bond_id":"1","bond_title":"已完成"}]}
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
         * info : [{"sys_fans":"10","empirical":"3","bond":"10","fans_pic":"http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/fans.png","empirical_pic":"http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/empirical.png","bond_pic":"http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/bond.png","equity":"1.个人主页拥有我的店铺的入口； 2.在直播间内添加直播购物车； 3.发布短视频时可以关联商品；","sys_fans_id":"0","sys_fans_title":"未完成","empirical_id":"1","empirical_title":"已完成","bond_id":"1","bond_title":"已完成"}]
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
             * sys_fans : 10
             * empirical : 3
             * bond : 10
             * fans_pic : http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/fans.png
             * empirical_pic : http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/empirical.png
             * bond_pic : http://zhiboshow.yanshi.hbbeisheng.com/public/appapi/images/personal/bond.png
             * equity : 1.个人主页拥有我的店铺的入口； 2.在直播间内添加直播购物车； 3.发布短视频时可以关联商品；
             * sys_fans_id : 0
             * sys_fans_title : 未完成
             * empirical_id : 1
             * empirical_title : 已完成
             * bond_id : 1
             * bond_title : 已完成
             */

            private String sys_fans;
            private String empirical;
            private String bond;
            private String fans_pic;
            private String empirical_pic;
            private String bond_pic;
            private String equity;
            private String sys_fans_id;
            private String sys_fans_title;
            private String empirical_id;
            private String empirical_title;
            private String bond_id;
            private String bond_title;

            public String getSys_fans() {
                return sys_fans;
            }

            public void setSys_fans(String sys_fans) {
                this.sys_fans = sys_fans;
            }

            public String getEmpirical() {
                return empirical;
            }

            public void setEmpirical(String empirical) {
                this.empirical = empirical;
            }

            public String getBond() {
                return bond;
            }

            public void setBond(String bond) {
                this.bond = bond;
            }

            public String getFans_pic() {
                return fans_pic;
            }

            public void setFans_pic(String fans_pic) {
                this.fans_pic = fans_pic;
            }

            public String getEmpirical_pic() {
                return empirical_pic;
            }

            public void setEmpirical_pic(String empirical_pic) {
                this.empirical_pic = empirical_pic;
            }

            public String getBond_pic() {
                return bond_pic;
            }

            public void setBond_pic(String bond_pic) {
                this.bond_pic = bond_pic;
            }

            public String getEquity() {
                return equity;
            }

            public void setEquity(String equity) {
                this.equity = equity;
            }

            public String getSys_fans_id() {
                return sys_fans_id;
            }

            public void setSys_fans_id(String sys_fans_id) {
                this.sys_fans_id = sys_fans_id;
            }

            public String getSys_fans_title() {
                return sys_fans_title;
            }

            public void setSys_fans_title(String sys_fans_title) {
                this.sys_fans_title = sys_fans_title;
            }

            public String getEmpirical_id() {
                return empirical_id;
            }

            public void setEmpirical_id(String empirical_id) {
                this.empirical_id = empirical_id;
            }

            public String getEmpirical_title() {
                return empirical_title;
            }

            public void setEmpirical_title(String empirical_title) {
                this.empirical_title = empirical_title;
            }

            public String getBond_id() {
                return bond_id;
            }

            public void setBond_id(String bond_id) {
                this.bond_id = bond_id;
            }

            public String getBond_title() {
                return bond_title;
            }

            public void setBond_title(String bond_title) {
                this.bond_title = bond_title;
            }
        }
    }
}
