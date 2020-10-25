package com.mc.phonelive;

/**
 * Created by cxf on 2018/10/15.
 */

public class HtmlConfig {

    //登录即代表同意服务和隐私条款
    public static final String LOGIN_PRIVCAY = AppConfig.HOST + "/Appapi/page/show.html?id=4";
    //直播间贡献榜
    public static final String LIVE_LIST = AppConfig.HOST + "/Appapi/contribute/index.html?uid=";
    //个人主页分享链接
    public static final String SHARE_HOME_PAGE = AppConfig.HOST + "/Appapi/home/index.html?touid=";
    //提现记录
    public static final String CASH_RECORD = AppConfig.HOST + "/Appapi/cash/index.html";
    //支付宝回调地址
    public static final String ALI_PAY_NOTIFY_URL = AppConfig.HOST + "/Appapi/Pay/notify_ali";
    //视频分享地址
    public static final String SHARE_VIDEO = AppConfig.HOST +"/appapi/video/index.html?videoid=";

}
