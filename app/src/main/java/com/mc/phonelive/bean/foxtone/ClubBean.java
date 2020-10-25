package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 俱乐部
 */
public class ClubBean extends BaseVO {


    public static InfoBean getClubData() {
        InfoBean info = new InfoBean();
        InfoBean.MyClubBean data = new InfoBean.MyClubBean();
        data.setId("0");data.setBadge("");data.setName("英雄联盟大本营");
        data.setCounts("111");data.setPhone("12235487541");
        info.setMy_club(data);

        List<InfoBean.ListBean> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            InfoBean.ListBean tt = new InfoBean.ListBean();
            tt.setId(""+i);tt.setBadge("");tt.setName("英雄联盟大本营"+i);
            tt.setCounts("111"+i);tt.setPhone("12235487541"+i);
            list.add(tt);
        }
        info.setClub_list(list);
        return info;
    }


    private InfoBean info;


    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
       private MyClubBean my_club;
       private List<ListBean> club_list;

        public MyClubBean getMy_club() {
            return my_club;
        }

        public void setMy_club(MyClubBean my_club) {
            this.my_club = my_club;
        }

        public List<ListBean> getClub_list() {
            return club_list;
        }

        public void setClub_list(List<ListBean> club_list) {
            this.club_list = club_list;
        }

        public static class  MyClubBean{
           private String id;//俱乐部ID
           private String uid;//俱乐部创始人ID
           private String name;//俱乐部名称
           private String phone;//俱乐部电话
           private String badge;//俱乐部图标
           private String briefing;//俱乐部简介
            private String is_founder;//俱乐部创始人
            private String counts;//俱乐部人数
           private String web_url;//俱乐部规则

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

            public String getWeb_url() {
                return web_url;
            }

            public void setWeb_url(String web_url) {
                this.web_url = web_url;
            }
        }

        public static class  ListBean {
            private String id;//俱乐部ID
            private String uid;//俱乐部创始人ID
            private String name;//俱乐部名称
            private String phone;//俱乐部电话
            private String badge;//俱乐部封面
            private String briefing;//俱乐部简介
            private String counts;//俱乐部人数

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

            public String getCounts() {
                return counts;
            }

            public void setCounts(String counts) {
                this.counts = counts;
            }
        }
    }
}
