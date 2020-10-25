package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.List;

/**
 * created by WWL on 2020/4/9 0009:11
 * 快递公司
 */
public class CourierBan implements Serializable {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"id":"254","code":"ems","name":"EMS"},{"id":"188","code":"shunfeng","name":"顺丰速运"},{"id":"266","code":"shentong","name":"申通快递"},{"id":"243","code":"yuantong","name":"圆通速递"},{"id":"252","code":"zhongtong","name":"中通快递"}]}
     * msg :
     */

    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * code : 0
         * msg :
         * info : [{"id":"254","code":"ems","name":"EMS"},{"id":"188","code":"shunfeng","name":"顺丰速运"},{"id":"266","code":"shentong","name":"申通快递"},{"id":"243","code":"yuantong","name":"圆通速递"},{"id":"252","code":"zhongtong","name":"中通快递"}]
         */

        private int code;
        private String msg;
        private List<InfoBean> info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 254
             * code : ems
             * name : EMS
             */

            private String id;
            private String code;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
