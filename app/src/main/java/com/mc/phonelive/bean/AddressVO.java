package com.mc.phonelive.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwl
 * 我的地址列表
 */

public class AddressVO extends BaseVO  implements Serializable {

    /**
     * data : {"code":0,"msg":"","info":[{"id":"1","uid":"11521","real_name":"旺仔","phone":"15571005523","district":"普陀龙湾","detail":"湖北省襄阳市樊城区卧龙大道88号","addtime":"2020-04-07 11:17","is_default":"1"}]}
     */

    private DataBean data;


    public static List<DataBean.InfoBean> getAddressList(){
        List<DataBean.InfoBean> mlist = new ArrayList<>();
        for (int i=0;i<10;i++){
            DataBean.InfoBean bean = new DataBean.InfoBean();
            bean.setDetail("湖北省襄阳市樊城区XXX街道XXX小区");
            bean.setDistrict("XXXXXXXXXXXXXXXXXXXXXX");
            bean.setIs_default("0");
            bean.setId(i+"");bean.setPhone("166987378273");
            bean.setReal_name("XXX先生");
            mlist.add(bean);
        }


        return mlist;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean implements Serializable{
        /**
         * code : 0
         * msg :
         * info : [{"id":"1","uid":"11521","real_name":"旺仔","phone":"15571005523","district":"普陀龙湾","detail":"湖北省襄阳市樊城区卧龙大道88号","addtime":"2020-04-07 11:17","is_default":"1"}]
         */

        private int code;
        @SerializedName("msg")
        private String msgX;
        private List<InfoBean> info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsgX() {
            return msgX;
        }

        public void setMsgX(String msgX) {
            this.msgX = msgX;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean implements Serializable{
            /**
             * id : 1
             * uid : 11521
             * real_name : 旺仔
             * phone : 15571005523
             * district : 普陀龙湾
             * detail : 湖北省襄阳市樊城区卧龙大道88号
             * addtime : 2020-04-07 11:17
             * is_default : 1
             */

            private String id;
            private String uid;
            private String real_name;
            private String phone;
            private String district;
            private String detail;
            private String addtime;
            private String is_default;

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

            public String getReal_name() {
                return real_name;
            }

            public void setReal_name(String real_name) {
                this.real_name = real_name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }
        }
    }
}
