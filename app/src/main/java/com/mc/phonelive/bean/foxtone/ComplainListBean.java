package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

/**
 * 申诉列表
 */
public class ComplainListBean extends BaseVO {
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean{
        private String id;
        private String uid;
        private String content;
        private String addtime;
        private String state;
        private String state_name;
        private String trade_sn;

        public String getTrade_sn() {
            return trade_sn;
        }

        public void setTrade_sn(String trade_sn) {
            this.trade_sn = trade_sn;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }
    }
}
