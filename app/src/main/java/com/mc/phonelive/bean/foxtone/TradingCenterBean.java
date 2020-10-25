package com.mc.phonelive.bean.foxtone;

import java.io.Serializable;
import java.util.List;

/**
 * 交易中心
 */
public class TradingCenterBean implements Serializable {

        /**
         * code : 0
         * msg : 成功
         * info : [{"data":{"total":"28945.00","trade_fee":"0","trade_counts":"0"},"list":[]}]
         */

        private List<InfoBean> info;

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * data : {"total":"28945.00","trade_fee":"0","trade_counts":"0"}
             * list : []
             */

            private DataBean data;
            private List<TradingCenterListBean> list;

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public List<TradingCenterListBean> getList() {
                return list;
            }

            public void setList(List<TradingCenterListBean> list) {
                this.list = list;
            }

            public static class DataBean {
                /**
                 * total : 28945.00
                 * trade_fee : 0
                 * trade_counts : 0
                 * rate: "50%",
                 * web_url: "http://huyin.yanshi.hbbeisheng.com/index.php?g=portal&m=page&a=index&id=28"
                 *
                 */

                private String total;
                private String trade_fee;
                private String trade_counts;
                private String rate;//: "50%",
                private String web_url;//: "http://huyin.yanshi.hbbeisheng.com/index.php?g=portal&m=page&a=index&id=28"

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public String getWeb_url() {
                    return web_url;
                }

                public void setWeb_url(String web_url) {
                    this.web_url = web_url;
                }

                public String getTotal() {
                    return total;
                }

                public void setTotal(String total) {
                    this.total = total;
                }

                public String getTrade_fee() {
                    return trade_fee;
                }

                public void setTrade_fee(String trade_fee) {
                    this.trade_fee = trade_fee;
                }

                public String getTrade_counts() {
                    return trade_counts;
                }

                public void setTrade_counts(String trade_counts) {
                    this.trade_counts = trade_counts;
                }
            }
        }
}
