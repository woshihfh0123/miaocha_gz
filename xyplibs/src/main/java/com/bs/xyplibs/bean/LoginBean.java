package com.bs.xyplibs.bean;

import com.bs.xyplibs.base.BaseVO;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 */

public class LoginBean extends BaseVO<LoginBean.DataBean>{


    /**
     * data : {"shop_id":"8","login_name":"15072207013","password":"670b14728ad9902aecba32e22fa4f6bd","real_name":"","nick_name":"大我看看哥大","mobile":"15072207013","weixin_openid":"","headpic":"http://huangxy.test.hbbeisheng.com//uploads/member/20190426/bccdc2dca6caa092ca1c71833c03f5ae.jpg","sex":"男","age":"0","birthday":"2019-04-30","register_time":"1555663639","login_time":"1556261171","address":"湖北省襄阳市樊城区王寨街道江山南路33号","signature":"柠檬攻888破民宿","ewm_img":"","money":"0.00","remarks":"","hobby":"","status":"1","longitude":"","latitude":"","shop_status":"未认证","uid":"48","token":"NcDxgpO0O0Oh","shop_name":"肯德基","hobby_list":[{"is_hobby":0,"id":"1","title":"旅游"},{"is_hobby":0,"id":"2","title":"美食"},{"is_hobby":0,"id":"3","title":"看书"},{"is_hobby":0,"id":"4","title":"运动"},{"is_hobby":0,"id":"5","title":"唱歌"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * shop_id : 8
         * login_name : 15072207013
         * password : 670b14728ad9902aecba32e22fa4f6bd
         * real_name :
         * nick_name : 大我看看哥大
         * mobile : 15072207013
         * weixin_openid :
         * headpic : http://huangxy.test.hbbeisheng.com//uploads/member/20190426/bccdc2dca6caa092ca1c71833c03f5ae.jpg
         * sex : 男
         * age : 0
         * birthday : 2019-04-30
         * register_time : 1555663639
         * login_time : 1556261171
         * address : 湖北省襄阳市樊城区王寨街道江山南路33号
         * signature : 柠檬攻888破民宿
         * ewm_img :
         * money : 0.00
         * remarks :
         * hobby :
         * status : 1
         * longitude :
         * latitude :
         * shop_status : 未认证
         * uid : 48
         * token : NcDxgpO0O0Oh
         * shop_name : 肯德基
         * hobby_list : [{"is_hobby":0,"id":"1","title":"旅游"},{"is_hobby":0,"id":"2","title":"美食"},{"is_hobby":0,"id":"3","title":"看书"},{"is_hobby":0,"id":"4","title":"运动"},{"is_hobby":0,"id":"5","title":"唱歌"}]
         */

        private String shop_id;
        private String is_examine;
        private String login_name;
        private String password;
        private String real_name;
        private String nick_name;
        private String mobile;
        private String weixin_openid;
        private String headpic;
        private String sex;
        private String age;
        private String birthday;
        private String unread_msg ;
        private String register_time;
        private String login_time;
        private String address;
        private String signature;
        private String ewm_img;
        private String money;
        private String remarks;
        private String hobby;
        private String status;
        private String longitude;
        private String latitude;
        private String shop_status;
        private String is_show;
        private String uid;
        private String token;
        private String shop_name;
        private String is_binding;
        private String shop_mobile;

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getUnread_msg() {
            return unread_msg;
        }

        public void setUnread_msg(String unread_msg) {
            this.unread_msg = unread_msg;
        }

        public String getShop_mobile() {
            return shop_mobile;
        }

        public void setShop_mobile(String shop_mobile) {
            this.shop_mobile = shop_mobile;
        }

        public String getIs_examine() {
            return is_examine;
        }

        public void setIs_examine(String is_examine) {
            this.is_examine = is_examine;
        }

        public String getIs_binding() {
            return is_binding;
        }

        public void setIs_binding(String is_binding) {
            this.is_binding = is_binding;
        }

        private List<HobbyListBean> hobby_list;

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getLogin_name() {
            return login_name;
        }

        public void setLogin_name(String login_name) {
            this.login_name = login_name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWeixin_openid() {
            return weixin_openid;
        }

        public void setWeixin_openid(String weixin_openid) {
            this.weixin_openid = weixin_openid;
        }

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getEwm_img() {
            return ewm_img;
        }

        public void setEwm_img(String ewm_img) {
            this.ewm_img = ewm_img;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getShop_status() {
            return shop_status;
        }

        public void setShop_status(String shop_status) {
            this.shop_status = shop_status;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public List<HobbyListBean> getHobby_list() {
            return hobby_list;
        }

        public void setHobby_list(List<HobbyListBean> hobby_list) {
            this.hobby_list = hobby_list;
        }

        public static class HobbyListBean implements Serializable{
            /**
             * is_hobby : 0
             * id : 1
             * title : 旅游
             */

            private String is_hobby;
            private String id;
            private String title;

            public String getIs_hobby() {
                return is_hobby;
            }

            public void setIs_hobby(String is_hobby) {
                this.is_hobby = is_hobby;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
