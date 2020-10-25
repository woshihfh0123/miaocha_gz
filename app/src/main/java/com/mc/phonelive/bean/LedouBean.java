package com.mc.phonelive.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 乐豆
 * created by WWL on 2020/4/14 0014:15
 */
public class LedouBean extends BaseVO {

    public static InfoBean getLeDouData() {
        InfoBean bean = new InfoBean();
        bean.setId(0 + "");
        bean.setPrice("860");
        bean.setSignnum("3天");
        List<InfoBean.SignBean> signBeanList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            InfoBean.SignBean sign = new InfoBean.SignBean();
            sign.setId(i + "");
            sign.setNums((i * 2) + "");
            if (i < 3) {
                sign.setIs_sign("1");
            } else {
                sign.setIs_sign("0");
            }
            signBeanList.add(sign);
        }
        bean.setSignlist(signBeanList);

        List<InfoBean.DetailsBean> detailsBeans = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            InfoBean.DetailsBean sign = new InfoBean.DetailsBean();
            sign.setId(i + "");
            sign.setPrice((i * 2) + "");
            if (i < 3) {
                sign.setStatus("1");
            } else {
                sign.setStatus("0");
            }
            sign.setTime("2020-03-12 12:12");
            sign.setTitle("签到" + i);

            detailsBeans.add(sign);
        }
        bean.setDetaillist(detailsBeans);
        return bean;
    }


    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private String id;
        private String price;
        private String fudou_num;
        private String fudou_frezz_num;
        private String signnum;
        private List<SignBean> signlist;
        private List<DetailsBean> detaillist;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSignnum() {
            return signnum;
        }

        public void setSignnum(String signnum) {
            this.signnum = signnum;
        }

        public List<SignBean> getSignlist() {
            return signlist;
        }

        public void setSignlist(List<SignBean> signlist) {
            this.signlist = signlist;
        }

        public List<DetailsBean> getDetaillist() {
            return detaillist;
        }

        public void setDetaillist(List<DetailsBean> detaillist) {
            this.detaillist = detaillist;
        }

        public static class SignBean {
            private String id;
            private String is_sign;
            private String nums;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIs_sign() {
                return is_sign;
            }

            public void setIs_sign(String is_sign) {
                this.is_sign = is_sign;
            }

            public String getNums() {
                return nums;
            }

            public void setNums(String nums) {
                this.nums = nums;
            }
        }

        public static class DetailsBean {
            private String id;
            private String title;
            private String time;
            private String price;
            private String status;

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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
