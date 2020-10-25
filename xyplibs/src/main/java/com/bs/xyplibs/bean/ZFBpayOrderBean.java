package com.bs.xyplibs.bean;


import com.bs.xyplibs.base.BaseVO;

/**
 * Created by Administrator on 2018/7/12.
 */

public class ZFBpayOrderBean extends BaseVO {

    /**
     * data : {"alipay_string":"alipay_sdk=alipay-sdk-php-20161101&app_id=2018070360566249&biz_content=%7B%22productCode%22%3A%22QUICK_WAP_PAY%22%2C%22subject%22%3A%22%E5%90%8C%E5%9F%8E%E6%B1%BD%E9%85%8D-%E5%B9%BF%E5%91%8A%E6%8A%95%E6%94%BE%E6%94%AF%E4%BB%98%22%2C%22out_trade_no%22%3A%22g153135932135679%22%2C%22total_amount%22%3A1%2C%22timeout_express%22%3A%221m%22%7D&charset=UTF-8&format=json&method=alipay.trade.wap.pay¬ify_url=http%3A%2F%2Ftcqp.test.hbbeisheng.com%2Fapi%2FAliNotify%2Findex.html&sign_type=RSA2×tamp=2018-07-12+09%3A35%3A21&version=1.0&sign=ZB11%2BHtFS2LccSrs5qLxxDq3hrqIHMZr3NEWyutL0e2FuGFUHX35a95JDwznD%2BumNVEkEPiJc4HWcnsaqLYnjqeVi1a2KKpzfr1n3mRy9g8ZDZoAzy%2BxUttTu0loJsJXt0duwzXcrnlHhdS45RCHDy6zP4JnEwxjKOikRM2luYcAWt%2Fo3E64jwQmhYtEuwMnCXz6whMlyGEiK5fpXgB5KE4VB%2BTF6h9XpEx8JPI4k0sDXpOX7vq2eeVQORN3fI4Fy7nzxV2oeGzOS0Z0Qdl4gj6SmNzwlURx6BxZgERsY0vnzWplY1KbiO2Tj8roXhjUVewduopbyKpTaMwb8XykwQ%3D%3D"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * alipay_string : alipay_sdk=alipay-sdk-php-20161101&app_id=2018070360566249&
         * biz_content=%7B%22productCode%22%3A%22QUICK_WAP_PAY%22%2C%22subject%22%3A%22%E5%90%8C%E5%9F%8E%E6%B1%BD%E9%85%8D-%E5%B9%BF%E5%91%8A%E6%8A%95%E6%94%BE%E6%94%AF%E4%BB%98%22%2C%22out_trade_no%22%3A%22g153135932135679%22%2C%22total_amount%22%3A1%2C%22timeout_express%22%3A%221m%22%7D&charset=UTF-8&format=json&method=alipay.trade.wap.pay¬ify_url=http%3A%2F%2Ftcqp.test.hbbeisheng.com%2Fapi%2FAliNotify%2Findex.html&sign_type=RSA2×tamp=2018-07-12+09%3A35%3A21&version=1.0&sign=ZB11%2BHtFS2LccSrs5qLxxDq3hrqIHMZr3NEWyutL0e2FuGFUHX35a95JDwznD%2BumNVEkEPiJc4HWcnsaqLYnjqeVi1a2KKpzfr1n3mRy9g8ZDZoAzy%2BxUttTu0loJsJXt0duwzXcrnlHhdS45RCHDy6zP4JnEwxjKOikRM2luYcAWt%2Fo3E64jwQmhYtEuwMnCXz6whMlyGEiK5fpXgB5KE4VB%2BTF6h9XpEx8JPI4k0sDXpOX7vq2eeVQORN3fI4Fy7nzxV2oeGzOS0Z0Qdl4gj6SmNzwlURx6BxZgERsY0vnzWplY1KbiO2Tj8roXhjUVewduopbyKpTaMwb8XykwQ%3D%3D
         */

        private String payOrder;
        private String out_trade_no;
        private String apliy_pay_url;

        public String getApliy_pay_url() {
            return apliy_pay_url;
        }

        public void setApliy_pay_url(String apliy_pay_url) {
            this.apliy_pay_url = apliy_pay_url;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getPayOrder() {
            return payOrder;
        }

        public void setPayOrder(String payOrder) {
            this.payOrder = payOrder;
        }
    }
}
