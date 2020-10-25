package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

import java.io.Serializable;
import java.util.List;

/**
 * 我的等级
 */
public class MyLevelBean extends BaseVO implements Serializable {
        private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable{
            /**
             * profits : {"total":"2","grade_id":"0","user_grade":"普通用户","diff_fee":"500","next_grade":"1","next_fee":"500","rate":"无"}
             * list : [{"icon":"http://huyin.yanshi.hbbeisheng.com/public/appapi/images/profit/1.png","title":"邀请新用户注册并实名认证,即可获得成长值","button_id":"1","button_name":"立即邀请","type":"1"},{"icon":"http://huyin.yanshi.hbbeisheng.com/public/appapi/images/profit/2.png","title":"创建俱乐部,可获直推奖励+团队奖励+俱乐部奖励","button_id":"0","button_name":"等级不足","type":"2"},{"icon":"http://huyin.yanshi.hbbeisheng.com/public/appapi/images/profit/3.png","title":"升级为城市合伙人，可获城市合伙人收益直推奖励+团队奖励+俱乐部奖励","button_id":"0","button_name":"等级不足","type":"3"}]
             */
            private ProfitsBean profits;
            private List<ListBean> list;

            public ProfitsBean getProfits() {
                return profits;
            }

            public void setProfits(ProfitsBean profits) {
                this.profits = profits;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ProfitsBean implements Serializable{
                /**
                 * total : 2
                 * grade_id : 0
                 * user_grade : 普通用户
                 * diff_fee : 500
                 * next_grade : 1
                 * next_fee : 500
                 * rate : 无
                 */

                private String total;
                private String grade_id;
                private String user_grade;
                private String diff_fee;
                private String next_grade;
                private String next_fee;
                private String rate;

                public String getTotal() {
                    return total;
                }

                public void setTotal(String total) {
                    this.total = total;
                }

                public String getGrade_id() {
                    return grade_id;
                }

                public void setGrade_id(String grade_id) {
                    this.grade_id = grade_id;
                }

                public String getUser_grade() {
                    return user_grade;
                }

                public void setUser_grade(String user_grade) {
                    this.user_grade = user_grade;
                }

                public String getDiff_fee() {
                    return diff_fee;
                }

                public void setDiff_fee(String diff_fee) {
                    this.diff_fee = diff_fee;
                }

                public String getNext_grade() {
                    return next_grade;
                }

                public void setNext_grade(String next_grade) {
                    this.next_grade = next_grade;
                }

                public String getNext_fee() {
                    return next_fee;
                }

                public void setNext_fee(String next_fee) {
                    this.next_fee = next_fee;
                }

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }
            }

            public static class ListBean {
                /**
                 * icon : http://huyin.yanshi.hbbeisheng.com/public/appapi/images/profit/1.png
                 * title : 邀请新用户注册并实名认证,即可获得成长值
                 * button_id : 1
                 * button_name : 立即邀请
                 * type : 1
                 */

                private String icon;
                private String title;
                private String button_id;
                private String button_name;
                private String type;

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getButton_id() {
                    return button_id;
                }

                public void setButton_id(String button_id) {
                    this.button_id = button_id;
                }

                public String getButton_name() {
                    return button_name;
                }

                public void setButton_name(String button_name) {
                    this.button_name = button_name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }
}
