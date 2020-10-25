package com.mc.phonelive.bean.foxtone;

import java.io.Serializable;

/**
 * 账户绑定
 */
public class TradeAccountBean implements Serializable {
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable {

        private ListInfoBean ali_info;
        private ListInfoBean card_info;

        public ListInfoBean getAli_info() {
            return ali_info;
        }

        public void setAli_info(ListInfoBean ali_info) {
            this.ali_info = ali_info;
        }

        public ListInfoBean getCard_info() {
            return card_info;
        }

        public void setCard_info(ListInfoBean card_info) {
            this.card_info = card_info;
        }

        public static class ListInfoBean implements Serializable{
            private String id;
            private String uid;
            private String type_id;
            private String name;
            private String account;
            private String mobile;
            private String api_ewm;
            private String card;
            private String addtime;

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

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
            }

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

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
        }


    }
}
