package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

import java.io.Serializable;

/**
 * 交易中心-----交易信箱
 */
public class TradingMailBean extends BaseVO implements Serializable {

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable{

        /**
         * addtime : 2020-05-25 15:47:39
         * avatar_thumb : http://huyin.yanshi.hbbeisheng.com/api/upload/avatar/20200522/06434832628507949_thumb.png
         * mobile : 155****2262
         * info : {"name":"wwl","account":"15571002264","mobile":"15571002266","api_ewm":"http://huyin.yanshi.hbbeisheng.com/api/upload/trade/20200523/06435438349446228.png","card":""}
         * times : 0
         * desc : 您挂买16777215音豆,单价为100.00共计100000000.00
         * trade_sn : T20052515473911521249496
         * state : 1
         * button_name : 待匹配
         */

        private String id;
        private String addtime;
        private String avatar_thumb;
        private String mobile;
        private String times;
        private String pay_img;
        private String desc;
        private String trade_sn;
        private String state;//交易状态(0取消 1 交易中 2匹配成功 3已付款 4确认收款已完成 5申诉)
        private String button_name;
        private String type_id;

        private UserMsgBean info;


        public String getPay_img() {
            return pay_img;
        }

        public void setPay_img(String pay_img) {
            this.pay_img = pay_img;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getAvatar_thumb() {
            return avatar_thumb;
        }

        public void setAvatar_thumb(String avatar_thumb) {
            this.avatar_thumb = avatar_thumb;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public UserMsgBean getInfo() {
            return info;
        }

        public void setInfo(UserMsgBean info) {
            this.info = info;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTrade_sn() {
            return trade_sn;
        }

        public void setTrade_sn(String trade_sn) {
            this.trade_sn = trade_sn;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getButton_name() {
            return button_name;
        }

        public void setButton_name(String button_name) {
            this.button_name = button_name;
        }

        public static class UserMsgBean implements Serializable{
            /**
             * name : wwl
             * account : 15571002264
             * mobile : 15571002266
             * api_ewm : http://huyin.yanshi.hbbeisheng.com/api/upload/trade/20200523/06435438349446228.png
             * card :
             */

            private String name;
            private String account;
            private String mobile;
            private String api_ewm;
            private String card;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getApi_ewm() {
                return api_ewm;
            }

            public void setApi_ewm(String api_ewm) {
                this.api_ewm = api_ewm;
            }

            public String getCard() {
                return card;
            }

            public void setCard(String card) {
                this.card = card;
            }
        }
    }
}
