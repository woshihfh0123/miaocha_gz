package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

/**
 * 俱乐部成员
 */
public class ClubMemberBean extends BaseVO {


    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {

        /**
         * id : 1
         * uid : 11648
         * name : 文俱
         * phone : 15571005523
         * badge : /api/upload/club/20200520/06432826760085841.png
         * briefing : 简介简介
         * user_nicename : 手机用户2263
         * avatar_thumb : http://huyin.yanshi.hbbeisheng.com/default_thumb.jpg
         * grade_id : 0
         * is_area : 0
         * type : 0
         * area : 无
         */

        private String id;
        private String uid;
        private String mobile;
        private String user_nicename;
        private String avatar_thumb;
        private String grade_id;
        private String is_area;
        private String type;
        private String area;

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getGrade_id() {
            return grade_id;
        }

        public void setGrade_id(String grade_id) {
            this.grade_id = grade_id;
        }

        public String getIs_area() {
            return is_area;
        }

        public void setIs_area(String is_area) {
            this.is_area = is_area;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }
}
