package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

/**
 * 我的俱乐部
 */
public class ClubDetailBean extends BaseVO {
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean{
        private String id;//俱乐部ID
        private String uid;//俱乐部创始人ID
        private String name;//俱乐部名称
        private String phone;//俱乐部电话
        private String badge;//俱乐部图标
        private String briefing;//俱乐部简介
        private String is_founder;//俱乐部创始人
        private String user_nicename;//俱乐部创始人姓名
        private String counts;//俱乐部人数

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBadge() {
            return badge;
        }

        public void setBadge(String badge) {
            this.badge = badge;
        }

        public String getBriefing() {
            return briefing;
        }

        public void setBriefing(String briefing) {
            this.briefing = briefing;
        }

        public String getIs_founder() {
            return is_founder;
        }

        public void setIs_founder(String is_founder) {
            this.is_founder = is_founder;
        }

        public String getCounts() {
            return counts;
        }

        public void setCounts(String counts) {
            this.counts = counts;
        }
    }
}
