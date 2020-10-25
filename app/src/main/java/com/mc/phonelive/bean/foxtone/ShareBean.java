package com.mc.phonelive.bean.foxtone;

import java.io.Serializable;

public class ShareBean implements Serializable {
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean{
        private String id;
        private String invite_url;
        private String invite_code;
        private String invite_ewm;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInvite_url() {
            return invite_url;
        }

        public void setInvite_url(String invite_url) {
            this.invite_url = invite_url;
        }

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public String getInvite_ewm() {
            return invite_ewm;
        }

        public void setInvite_ewm(String invite_ewm) {
            this.invite_ewm = invite_ewm;
        }
    }
}
