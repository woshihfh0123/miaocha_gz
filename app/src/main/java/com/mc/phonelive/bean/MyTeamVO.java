package com.mc.phonelive.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * created by WWL on 2019/6/28 0028:09
 * 我的团队
 */
public class MyTeamVO {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String user_count;
        private String money;
        private String page;
        private String count;
        private List<ListBean> list;
        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getUser_count() {
            return user_count;
        }

        public void setUser_count(String user_count) {
            this.user_count = user_count;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String uid;
            private String nickname;
            private String avatar;
            private String phone;
            private String add_time;
            private String price;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }

    public static  MyTeamVO.DataBean getTeamData(){
        MyTeamVO.DataBean dataBean = new DataBean();
        dataBean.setUser_count("100");
        dataBean.setMoney("300");
        dataBean.setPage("1");
        dataBean.setCount("1");
        List<MyTeamVO.DataBean.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<20;i++){
            MyTeamVO.DataBean.ListBean bean = new MyTeamVO.DataBean.ListBean();
            bean.setAdd_time("2020-12-20 00:00:11");
            bean.setAvatar("");
            bean.setNickname("名称名称名称");
            bean.setPhone("129139223123");
            bean.setPrice("300");
            bean.setUid("11521");
            listBeans.add(bean);
        }
        dataBean.setList(listBeans);

        return dataBean;
    }
}
