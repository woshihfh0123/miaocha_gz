package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cece.com.bannerlib.model.PicBean;

/**
 * created by WWL on 2020/4/2 0002:11
 */
public class ShopMallMainBean implements Serializable {
        /**
         * ret : 200
         * data : {"code":0,"msg":"","info":[{"uid":"11521","store_id":"1","title":"商品名~2","goods_url":"https://www.2345.com/?k715892195","price":"100","ot_price":"1222","description":"详情简介","slider_image":"/api/upload/store/20200229/06363127669718948.png,/api/upload/store/20200229/06363127669718948.png.jpg","addtime":1582968767,"is_show":1,"id":"40"}]}
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
             * info : [{"uid":"11521","store_id":"1","title":"商品名~2","goods_url":"https://www.2345.com/?k715892195","price":"100","ot_price":"1222","description":"详情简介","slider_image":"/api/upload/store/20200229/06363127669718948.png,/api/upload/store/20200229/06363127669718948.png.jpg","addtime":1582968767,"is_show":1,"id":"40"}]
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
               private List<PicBean> adv_list;

                public List<PicBean> getAdv_list() {
                    return adv_list;
                }

                public void setAdv_list(List<PicBean> adv_list) {
                    this.adv_list = adv_list;
                }

            }
        }


        public static DataBean.InfoBean getShopData(){
            DataBean.InfoBean infoBean = new DataBean.InfoBean();
            List<PicBean> picBeans = new ArrayList<>();
            for (int i=0;i<5;i++){
                PicBean pic =new PicBean();
                pic.setId(i+"");
                pic.setTitle("标题名");
                pic.setPic("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg");
                pic.setType("1");
                pic.setUrl("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg");
                picBeans.add(pic);
            }
            infoBean.setAdv_list(picBeans);
            return infoBean;
        }
}
