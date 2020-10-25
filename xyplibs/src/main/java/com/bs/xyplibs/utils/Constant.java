package com.bs.xyplibs.utils;

/**
 * Created by Administrator on 2018/1/4.
 */

import java.util.UUID;

/**
 * 网络请求接口
 */
public class Constant {
    //    public static final String BK_URL_SAME = "http://api.test.feiducn.com/index.php/api/";
//    public static final String BK_URL_SAME = "http://feiduapi.test.hbbeisheng.com/index.php/api/";
    public static final String BK_URL_SAME = "https://api.pinyanzhi.net/";//http://api.pinyanzhi.net
    public static final String BK_URL_SAMEN = "http://api.pinyanzhi.net/";//http://api.pinyanzhi.net
    public static final String BASEWEB = "https://www.118114hyym.com/";
//        public static final String BK_URL_SAME = "http://huangxy.test.hbbeisheng.com/api/";
//    public static final String BASEWEB = "http://huangxy.test.hbbeisheng.com/";
    public static final String MD5_TITLE = "XFHXYqSiokyCmtfSZbAjmelTOpy39eG7";
    /**
     * https://www.kancloud.cn/yyl0818/yzyjq/961859
     */
    public static final boolean isDebug=false;//是否是测试版，正式版改false
    public static final  UUID services=UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    public static final  UUID cNUid=UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
//    UUID services=UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
//    UUID cNUid=UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    /**
     *员工首页本地化接口
     */
    public static final String HOME_NEW_LOCAL = BK_URL_SAME + "mine/workerAll.html";
    /**
     *获取病害库
     */
    public static final String GET_BING_HAI = BK_URL_SAME + "Mine/disease.html";
    /**
     * 测量员提交测量数据接口
     */
    public static final String UP_WORK = BK_URL_SAME + "mine/measure.html";
    /**
     * 发送短信接口
     */
    public static final String GET_CODE = BK_URL_SAME + "api/user/smsCode.rpc";
    /**
     * 手机动态码登录接口
     */
    public static final String CODE_LOGIN = BK_URL_SAME + "publics/phone_login.html";
    /**
     * 动态发布次数接口
     */
    public static final String SEND_CS = BK_URL_SAME + "dynamic/select_dynamic.html";
    /**
     * 立即领取活动接口
     */
    public static final String GET_HD = BK_URL_SAME + "other/add_activity.html";
    /**
     * 个人信息接口
     */
    public static final String PERSON_INFO = BK_URL_SAME + "api/user/mine.rpc";
    /**
     * 个人资料
     */
    public static final String PERSON_SET = BK_URL_SAME + "api/user/personalInfo.rpc";
    /**
     * 我的团队
     */
    public static final String MY_TEAM = BK_URL_SAME + "api/user/myTeam.rpc";
    /**
     * 读取消息
     */
    public static final String SET_MSG_ISREAD = BK_URL_SAME + "api/message/read.rpc";
    /**
     * 签到
     */
    public static final String SET_QD = BK_URL_SAME + "api/user/qiandao.rpc";
    /**
     * 签到主页
     */
    public static final String SET_HOME_QD = BK_URL_SAME + "api/user/myQiandao.rpc";
    /**
     * 购买vip成功后礼品领取
     */
    public static final String GET_LP = BK_URL_SAME + "api/order/vipGift.rpc";
    /**
     * 退款
     */
    public static final String TUI_KUAN = BK_URL_SAME + "api/order/refoundOrder.rpc";
    /**
     * 物流
     */
    public static final String WU_LIU = BK_URL_SAME + "api/express/queryExpress.rpc";
    /**
     * 确认收货
     */
    public static final String GET_GOODS = BK_URL_SAME + "api/order/received.rpc";
    /**
     * 关注
     */
    public static final String SET_GZ = BK_URL_SAME + "api/focus/focus.rpc";
    /**
     * 支付
     */
    public static final String PAY_WX = BK_URL_SAME + "api/wxPay/createWxOrder.rpc";
    /**
     * 支付
     */
    public static final String PAY_VIP = BK_URL_SAME + "api/vip/buyVip.rpc";
    /**
     * 读取消息
     */
    public static final String CHANGE_ADDRESS = BK_URL_SAME + "api/order/modifyOrderAddress.rpc";
    /**
     * 删除
     */
    public static final String DELETE_ADDRESS = BK_URL_SAME + "api/address/delete.rpc";
    /**
     * 地址管理
     */
    public static final String ADDRESS_LIST = BK_URL_SAME + "api/address/list.rpc";
    /**
     * 购物车
     */
    public static final String GWC_MONEY = BK_URL_SAME + "api/car/modifyNum.rpc";
    /**
     * 下单
     */
    public static final String CREADE_ORDER = BK_URL_SAME + "api/order/createOrder.rpc";
    /**
     * 积分支付
     */
    public static final String ORD_ID = BK_URL_SAME + "api/score/pay.rpc";
    /**
     * VIP
     */
    public static final String GET_VIP = BK_URL_SAME + "api/vip/template.rpc";
    /**
     * GWC_下单
     */
    public static final String GWC_XD = BK_URL_SAME + "api/car/createOrderFromCar.rpc";
    /**
     * orderId订单详情
     */
    public static final String ORDER_INFO = BK_URL_SAME + "api/order/detail.rpc";
    /**
     * 获取评论
     */
    public static final String GET_PING_LUN = BK_URL_SAME + "api/comment/list.rpc";
    /**
     * 评论
     */
    public static final String SEND_PL = BK_URL_SAME + "api/comment/comment.rpc";
    /**
     * 获取二维码
     */
    public static final String GET_EWM = BK_URL_SAME + "api/user/getQrContent.rpc";
    /**
     * 删除购物车
     */
    public static final String DELETE_CAR = BK_URL_SAME + "api/car/delete.rpc";
    /**
     * 删除订单
     */
    public static final String DELETE_ORDER = BK_URL_SAME + "api/order/delete.rpc";
    /**
     * 编辑信息
     */
    public static final String SET_PSINFO = BK_URL_SAME + "api/user/setNickname.rpc";
    /**
     * 删除RJ
     */
    public static final String DELETE_RJ= BK_URL_SAME + "api/product/delete.rpc";
    /**
     * 商品详情
     */
    public static final String GET_GOODS_INFO = BK_URL_SAME + "api/good/detail.rpc";
    /**
     * 添加购物车
     */
    public static final String ADD_GWC = BK_URL_SAME + "api/car/add.rpc";
    /**
     * 商城BANNER
     */
    public static final String GET_BANNER = BK_URL_SAME + "api/banner/getBanner.rpc";
    /**
     * 用户信息
     */
    public static final String USER_INFO = BK_URL_SAME + "api/user/userInfo.rpc";
    /**
     * 获取实名认证信息
     */
    public static final String GET_SHI_MING = BK_URL_SAME + "api/user/getAuthInfo.rpc";
    /**
     * 更改购物车数量
     */
    public static final String CHANGE_GWC = BK_URL_SAME + "api/car/modifyNum.rpc";
    /**
     * 更改购物车数量
     */
    public static final String CHANGE_ORDER_GWC = BK_URL_SAME + "api/order/modifyOrderItemNum.rpc";
    /**
     * 点击提现按钮获取提现首页数据
     */
    public static final String GET_TX_INFO = BK_URL_SAME + "api/account/getAccount.rpc";
    /**
     * 我的团队-用户查看
     */
    public static final String GET_USER_FROM_LEVEL = BK_URL_SAME + "api/user/queryTeamUserByLevel.rpc";
    /**
     * CITY_LIST
     */
    public static final String GET_CITY_LIST = BK_URL_SAME + "api/area/areaHome.rpc";
    /**
     * 结算
     */
    public static final String GET_JS = BK_URL_SAME + "api/settle/settleIncome.rpc";
    /**
     * 我的团队-用户查看-内部
     */
    public static final String GET_USER_FROM_USERID= BK_URL_SAME + "api/user/queryTeamUserByUserId.rpc";
    /**
     * 首页大接口
     */
    public static final String GET_HOME_VIDEO = BK_URL_SAME + "api/product/homeV2.rpc";
    /**
     * 用户注册接口
     */
    public static final String RIGISTER = BK_URL_SAME + "api/user/register.rpc";
    /**
     * 实名认证
     */
    public static final String REN_ZHENG = BK_URL_SAME + "api/user/idAuthentication.rpc";
    /**
     * zp
     */
    public static final String ZAN_ZP = BK_URL_SAME + "api/love/love.rpc";
    /**
     * PL
     */
    public static final String ZAN_PL = BK_URL_SAME + "api/comment/love.rpc";
    /**
     * 确认提现
     */
    public static final String TI_XIAN_OK = BK_URL_SAME + "api/account/confirmCash.rpc";
    /**
     * 消息列表
     */
    public static final String MSG_LIST_TYPE = BK_URL_SAME + "api/message/list.rpc";
    /**
     * 订单列表
     */
    public static final String MSG_LIST_ORDER = BK_URL_SAME + "api/order/listV2.rpc";
    /**
     * 购物车
     */
    public static final String GWC_LIST = BK_URL_SAME + "api/car/list.rpc";
    /**
     * 全部评价
     */
    public static final String PING_JIA_LIST = BK_URL_SAME + "api/mall/shoppingComments.rpc";
    /**
     * 商城首页
     */
    public static final String SHOP_HOME = BK_URL_SAME + "api/mall/home.rpc";
    /**
     * 城市切换
     */
    public static final String SHOP_HOME_CHANGE = BK_URL_SAME + "api/area/changeCity.rpc";
    /**
     * 美发
     */
    public static final String SHOP_MF= BK_URL_SAME + "api/mall/queryHairByType.rpc";
    /**
     * 商城首页大类二级分类
     */
    public static final String SHOP_HOME_DL = BK_URL_SAME + "api/mall/querySubTypes.rpc";
    /**
     * 首页搜索
     */
    public static final String HOME_SEARCH = BK_URL_SAME + "api/product/searchProduct.rpc";
    /**
     * 店铺详情
     */
    public static final String SHOP_HOME_INFO = BK_URL_SAME + "api/mall/shopDetail.rpc";
    /**
     * 订单销售-结算页面
     */
    public static final String ORDER_JIESUAN = BK_URL_SAME + "api/settle/orderIncomes.rpc";
    /**
     * 订单销售
     */
    public static final String ORDER_LIST_TYPE = BK_URL_SAME + "api/settle/mySettles.rpc";
    /**
     * 颜券主页列表
     */
    public static final String YAN_QUAN = BK_URL_SAME + "api/coupon/myCoupon.rpc";
    /**
     * 收益统计
     */
    public static final String SHOU_YI = BK_URL_SAME + "api/user/incomeStatistics.rpc";
    /**
     * 人气榜单
     */
    public static final String BANG_DAN = BK_URL_SAME + "api/user/hot.rpc";
    /**
     * 提现记录
     */
    public static final String TI_XIAN = BK_URL_SAME + "api/account/cashedList.rpc";
    /**
     * 二级
     */
    public static final String TYPE_TWO_SHOP = BK_URL_SAME + "api/mall/queryGoodBySubType.rpc";
    /**
     * 二级搜索
     */
    public static final String TYPE_TWO_SEARCH = BK_URL_SAME + "api/mall/searchGoods.rpc";
    /**
     * 选择商品
     */
    public static final String SELECTOR_SP = BK_URL_SAME + "api/good/queryAllGoods.rpc";
    /**
     * 我的-首页作品列表
     */
    public static final String MY_ZP_LIST = BK_URL_SAME + "api/product/queryByUserId.rpc";
    /**
     * 绑定手机号
     */
    public static final String BINDWX = BK_URL_SAME + "publics/bind_phone.html";
    /**
     * 立即报名活动接口
     */
    public static final String BAO_MING = BK_URL_SAME + "other/sign_activity.html";
    /**
     * 密码登录接口
     */
    public static final String PSD_LOGIN = BK_URL_SAME + "api/user/login.rpc";
    /**
     * 版本检测
     */
    public static final String CHECK_VERSION = BK_URL_SAME + "api/version/checkVersion.rpc";
    /**
     * 密码登录接口
     */
    public static final String WX_LOGIN = BK_URL_SAME + "publics/weixin_login.html";
    /**
     * 修改密码接口
     */
    public static final String CHANGE_PWD = BK_URL_SAME + "api/user/resetPwd.rpc";
    /**
     * 发现列表接口
     */
    public static final String TAB_TWO = BK_URL_SAME + "dynamic/get_list.html";
    /**
     * 商家分类详情
     */
    public static final String SHOP_TYPE = BK_URL_SAME + "shops/get_shop_list.html";
    /**
     * 获取商家列表接口
     */
    public static final String TAB_ONE = BK_URL_SAME + "shops/get_list.html";
    /**
     * 获取商家列表接口
     */
    public static final String SEARCH = BK_URL_SAME + "shops/search.html";
    /**
     * 添加动态接口
     */
    public static final String ADD_DT = BK_URL_SAME + "dynamic/add_dynamic.html";
    /**
     * 评论接口
     */
    public static final String PING_LUN = BK_URL_SAME + "dynamic/add_eval.html";
    /**
     * 我要入驻接口
     */
    public static final String RUZHU = BK_URL_SAME + "mine/apply_shop.html";
    /**
     * 房屋出租首页
     */
    public static final String CHU_ZU = BK_URL_SAME + "house/get_list.html";
    /**
     * 创业园地首页接口
     */
    public static final String CHUANG_YE = BK_URL_SAME + "other/get_work_list.html";
    /**
     * 创业园地详情接口
     */
    public static final String CHUANG_YE_SHARE = BK_URL_SAME + "other/get_work_detail.html";
    /**
     * IT服务详情接口
     */
    public static final String IT_SHARE = BK_URL_SAME + "other/get_server_detail.html";
    /**
     * 招聘详情接口
     */
    public static final String ZP_SHARE = BK_URL_SAME + "other/get_recruit_detail.html";
    /**
     * 投票活动详情接口
     */
    public static final String TP_SHARE = BK_URL_SAME + "other/get_activity_rank.html";
    /**
     * 投票活动内页
     */
    public static final String INNER_HD = BK_URL_SAME + "other/get_rank_detail.html";
    /**
     * 活动首页接口
     */
    public static final String HUO_DONG = BK_URL_SAME + "other/get_activity_list.html";
    /**
     * IT服务首页接口
     */
    public static final String IT_SERVICE = BK_URL_SAME + "other/get_server_list.html";
    /**
     * 招聘首页
     */
    public static final String ZHAO_PIN = BK_URL_SAME + "other/get_recruit_list.html";
    /**
     * 获取商家详情接口
     */
    public static final String SHOP_INFO = BK_URL_SAME + "shops/get_detail.html";
    /**
     * 添加地址
     */
    public static final String ADD_ADDRESS = BK_URL_SAME + "api/address/add.rpc";
    /**
     * 线下活动详情
     */
    public static final String HD_INFO = BK_URL_SAME + "other/get_activity_detail.html";
    /**
     * 房屋出租详情接口
     */
    public static final String HOURSE_INFO = BK_URL_SAME + "house/get_house_detail.html";
    /**
     * 商家类型
     */
    public static final String RUZHU_SHOP_TYPE = BK_URL_SAME + "shops/get_shoptype_list.html";
    /**
     * 获取优惠券详情接口
     */
    public static final String YHQ_INFO = BK_URL_SAME + "shops/get_voucher_detail.html";
    /**
     * 我的优惠券核销接口
     */
    public static final String HX_INFO = BK_URL_SAME + "order/show_qrcode.html";
    /**
     * 我的活动核销接口
     */
    public static final String HD_HXINFO = BK_URL_SAME + "other/receive_detail.html";
    /**
     * 优惠券核销接口
     */
    public static final String HX_JK = BK_URL_SAME + "api/qrcode/scan.rpc";
    /**
     * 投票接口
     */
    public static final String H5_TP = BK_URL_SAME + "other/set_rank_vote.html";
    /**
     * 购买卡券接口
     */
    public static final String GM_KQ = BK_URL_SAME + "order/goOrder.html";
    /**
     * 购买卡券接口
     */
    public static final String GM_LQ = BK_URL_SAME + "order/buy_voucher.html";
    /**
     * 提交收藏接口
     */
    public static final String SJ_SC = BK_URL_SAME + "mine/add_favorites.html";
    /**
     * 开启拼团接口
     */
    public static final String KAI_QPT = BK_URL_SAME + "order/group_order.html";
    /**
     * 我的卡券接口
     */
    public static final String KA_QUAN = BK_URL_SAME + "order/get_list.html";
    /**
     * 我的活动列表接口
     */
    public static final String MY_HD = BK_URL_SAME + "other/receive_list.html";
    /**
     * 核销记录接口
     */
    public static final String HX_LIST = BK_URL_SAME + "shops/get_consume_list.html";
    /**
     * 获取订单或商品评价列表（订单编号和商品编号必传其一）
     */
    public static final String LIST_PJ = BK_URL_SAME + "shops/get_comment_list.html";
    /**
     * 修改个人/商家信息
     */
    public static final String SET_INFO = BK_URL_SAME + "mine/update_user_info.html";
    /**
     * 修改头像
     */
    public static final String SET_PIC = BK_URL_SAME + "api/user/changeCover.rpc";
    /**
     * 发表评论
     */
    public static final String SEND_PL_GOODS = BK_URL_SAME + "api/good/comment.rpc";
    /**
     * 发布动态
     */
    public static final String SET_DONG_TAI = BK_URL_SAME + "api/product/publish.rpc";
    /**
     * 回复评论接口
     */
    public static final String HUI_FU = BK_URL_SAME + "dynamic/add_reply_eval.html";
    /**
     * 我的消费接口
     */
    public static final String XIAO_FEI = BK_URL_SAME + "mine/get_my_orderlist.html";
    /**
     * 我的消息接口
     */
    public static final String MY_MSG = BK_URL_SAME + "mine/get_message_list.html";
    /**
     * 我的好友列表
     */
    public static final String MY_Friend = BK_URL_SAME + "mine/get_my_friend.html";
    /**
     * 添加我的好友列表
     */
    public static final String ADD_Friend = BK_URL_SAME + "mine/add_my_friend.html";
    /**
     * 我的收藏
     */
    public static final String MY_SC = BK_URL_SAME + "mine/get_favorites_list.html";
    /**
     * 我的足迹
     */
    public static final String MY_ZJ = BK_URL_SAME + "mine/get_browse_list.html";
    /**
     * 获取我的消息接口
     */
    public static final String GET_MSG = BK_URL_SAME + "mine/get_my_message.html";
    /**
     * 数据统计接口
     */
    public static final String SJ_TJ = BK_URL_SAME + "shops/shop_statistics.html";
    /**
     * 数据统计接口
     */
    public static final String CITY_LIST = BK_URL_SAME + "publics/get_city_list.html";
    /**
     * 提交订单评价
     */
    public static final String PIN_LUN = BK_URL_SAME + "shops/comment.html";
    /**
     * 点赞接口
     */
    public static final String DIAN_ZAN = BK_URL_SAME + "dynamic/add_praise.html";
    /**
     * 收藏
     */
    public static final String SHOU_C = BK_URL_SAME + "api/collect/collect.rpc";
    /**
     * 用户登录接口
     */
    public static final String LOGIN = BK_URL_SAME + "publics/login.html";
    /**
     * 用于注册时选择区域的三级联动
     */
    public static final String GET_QUYU_LIST = BK_URL_SAME + "publics/districttreelist.html";
    /**
     * 全局参数
     * 商户端所有接口均做加密处理，加密参数按键名降序排序，全局参数接口说明中不再列举，全局参数如下：
     "lang": 中文：'zh-cn',英文：'en-us',不传或为空的时候，接口默认返回中文,
     "sign": 加密字符串，
     "timestamp": 时间戳，
     "uid":商家唯一标识，登录接口传入0或空
     */
    /**
     * 商家登陆
     */
    public static final String LOGIN1 = BK_URL_SAME+"Mlogin/index";
    /**
     * 商家分类列表
     */
    public static final String SHOP_CLASS_LIST = BK_URL_SAME+"Merchant/shopClassLists";
    /**
     * 添加/编辑分类
     */
    public static final String ADD_EDIT_CLASS = BK_URL_SAME+"Merchant/addOrUpdateShopClass";
    /**
     * 商品列表
     */
    public static final String GOODS_LIST = BK_URL_SAME+"Merchant/goodsLists";
    /**
     * 商品上下架
     */
    public static final String CHANGE_GOODS_STATUS = BK_URL_SAME+"Merchant/changeGoodsState";
    /**
     * 获取系统内置商品属性
     */
    public static final String GOODS_ATTR_LIST = BK_URL_SAME+"Merchant/getGoodsAttrList";
    /**
     * 添加或编辑商品
     */
    public static final String ADD_EDIT_GOODS = BK_URL_SAME+"Merchant/addOrUpdateGoods";
    /**
     * 已创建的商家活动
     */
    public static final String GET_ACTIVITYS_LIST = BK_URL_SAME+"Merchant/activityTypes";
    /**
     * 商家活动
     */
    public static final String ACTIVITYS = BK_URL_SAME+"Merchant/activitys";
    /**
     * 新建活动
     */
    public static final String ADD_ACTIVITYS = BK_URL_SAME+"Merchant/addActivity";
    /**
     * 撤销活动
     */
    public static final String CANCLE_ACTIVITY = BK_URL_SAME+"Merchant/cancelActivity";
    /**
     * 订单列表
     */
    public static final String ORDER_LISTS = BK_URL_SAME+"Merchant/orderLists";
    /**
     * 订单详情
     */
    public static final String ORDER_DETAIL = BK_URL_SAME+"merchant/orderDetail";
    /**
     * 公共参数
     */
    public static final String PUB_PARAMS = BK_URL_SAME+"publics/globalparam.html";
    /**
     * 取消订单
     */
    public static final String CANCLE_ORDER = BK_URL_SAME+"Merchant/cancelOrder";
    /**
     * 商品详情
     */
    public static final String GOODS_DETAIL = BK_URL_SAME+"merchant/getGoodsDetail";
    /**
     * 运营首页
     */
    public static final String YUN_YING = BK_URL_SAME+"Merchant/operate";
    /**
     * 评价列表
     */
    public static final String PING_JIA = BK_URL_SAME+"merchant/evaluateLists";
    /**
     * 评价回复和回复编辑
     */
    public static final String HUI_FU_PJ = BK_URL_SAME+"merchant/reply";
    /**
     * 运营首页图表
     */
    public static final String YUN_YINGK = BK_URL_SAME+"merchant/operateCharts";
    /**
     * 开启或关闭营业状态
     */
    public static final String SET_YINGYE_STATUS = BK_URL_SAME+"Merchant/onOffBusiness";
    /**
     * 我的
     */
    public static final String GET_MY_INFO = BK_URL_SAME+"merchant/merchantCenter";
    /**
     * 店铺公告
     */
    public static final String SET_NOTICE = BK_URL_SAME+"merchant/updateNotice";
    /**
     * 财务首页
     */
    public static final String CAIWU_INFO = BK_URL_SAME+"merchant/financeCenter";
    /**
     * 接单
     */
    public static final String GET_ORDER = BK_URL_SAME+"Merchant/receiveOrder";
    /**
     * 跟踪订单
     */
    public static final String GET_MAP_POINT = BK_URL_SAME+"Merchant/tractOrder";


}