package com.mc.phonelive.http;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.ErrorActivity;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.TxLocationBean;
import com.mc.phonelive.bean.TxLocationPoiBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.event.FollowEvent;
import com.mc.phonelive.im.ImPushUtil;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.MD5Util;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by cxf on 2018/9/17.
 * 获取接口数据
 */

public class HttpUtil {

    private static final String SALT = "76576076c1f5f657b634e966c8836a06";
    private static final String DEVICE = "android";
    private static final String VIDEO_SALT = "#2hgfk85cm23mk58vncsark";

    /**
     * 初始化
     */
    public static void init() {
        HttpClient.getInstance().init();
    }

    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }

    /**
     * 使用腾讯定位sdk获取 位置信息
     *
     * @param lng 经度
     * @param lat 纬度
     * @param poi 是否要查询POI
     */
    public static void getAddressInfoByTxLocaitonSdk(final double lng, final double lat, final int poi, int pageIndex, String tag, final CommonCallback<TxLocationBean> commonCallback) {
        OkGo.<String>get("https://apis.map.qq.com/ws/geocoder/v1/")
                .params("location", lat + "," + lng)
                .params("get_poi", poi)
                .params("poi_options", "address_format=short;radius=1000;page_size=20;page_index=" + pageIndex + ";policy=5")
                .params("key", AppConfig.getInstance().getTxLocationKey())
                .tag(tag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject obj = JSON.parseObject(response.body());
                        if (obj.getIntValue("status") == 0) {
                            JSONObject result = obj.getJSONObject("result");
                            if (result != null) {
                                TxLocationBean bean = new TxLocationBean();
                                bean.setLng(lng);
                                bean.setLat(lat);
                                bean.setAddress(result.getString("address"));
                                JSONObject addressComponent = result.getJSONObject("address_component");
                                if (addressComponent != null) {
                                    bean.setNation(addressComponent.getString("nation"));
                                    bean.setProvince(addressComponent.getString("province"));
                                    bean.setCity(addressComponent.getString("city"));
                                    bean.setDistrict(addressComponent.getString("district"));
                                    bean.setStreet(addressComponent.getString("street"));
                                }
                                if (poi == 1) {
                                    List<TxLocationPoiBean> poiList = JSON.parseArray(result.getString("pois"), TxLocationPoiBean.class);
                                    bean.setPoiList(poiList);
                                }
                                if (commonCallback != null) {
                                    commonCallback.callback(bean);
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 使用腾讯地图API进行搜索
     *
     * @param lng 经度
     * @param lat 纬度
     */
    public static void searchAddressInfoByTxLocaitonSdk(final double lng, final double lat, String keyword, int pageIndex, final CommonCallback<List<TxLocationPoiBean>> commonCallback) {
        OkGo.<String>get("https://apis.map.qq.com/ws/place/v1/search?")
                .params("keyword", keyword)
                .params("boundary", "nearby(" + lat + "," + lng + ",1000)&orderby=_distance&page_size=20&page_index=" + pageIndex)
                .params("key", AppConfig.getInstance().getTxLocationKey())
                .tag(HttpConsts.GET_MAP_SEARCH)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject obj = JSON.parseObject(response.body());
                        if (obj.getIntValue("status") == 0) {
                            List<TxLocationPoiBean> poiList = JSON.parseArray(obj.getString("data"), TxLocationPoiBean.class);
                            if (commonCallback != null) {
                                commonCallback.callback(poiList);
                            }
                        }
                    }
                });
    }

    /**
     * 验证token是否过期
     */
    public static void ifToken(String uid, String token, HttpCallback callback) {
        HttpClient.getInstance().get("User.iftoken", HttpConsts.IF_TOKEN)
                .params("uid", uid)
                .params("token", token)
                .execute(callback);
    }

    /**
     * 获取config
     */
    public static void getConfig(final CommonCallback<ConfigBean> commonCallback) {
        HttpClient.getInstance().get("Home.getConfig", HttpConsts.GET_CONFIG)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        Log.v("tags", info[0] + "----");
                        if (code == 0 && info.length > 0) {
                            try {
                                JSONObject obj = JSON.parseObject(info[0]);

                                ConfigBean bean = JSON.toJavaObject(obj, ConfigBean.class);
                                AppConfig.getInstance().setConfig(bean);
                                AppConfig.getInstance().setLevel(obj.getString("level"));
                                AppConfig.getInstance().setAnchorLevel(obj.getString("levelanchor"));
                                SpUtil.getInstance().setStringValue(SpUtil.CONFIG, info[0]);
                                if (commonCallback != null) {
                                    commonCallback.callback(bean);
                                }
                            } catch (Exception e) {
                                String error = "info[0]:" + info[0] + "\n\n\n" + "Exception:" + e.getClass() + "---message--->" + e.getMessage();
                                ErrorActivity.forward("GetConfig接口返回数据异常", error);
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        if (commonCallback != null) {
                            commonCallback.callback(null);
                        }
                    }
                });
    }

    /**
     * 获取用户信息
     */
    public static void getBaseInfo(final CommonCallback<UserBean> commonCallback) {
        HttpClient.getJavaInstance().get("User.getBaseInfo", HttpConsts.GET_BASE_INFO)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            JSONObject obj = JSON.parseObject(info[0]);
                            UserBean bean = JSON.toJavaObject(obj, UserBean.class);
                            AppConfig.getInstance().setUserBean(bean);
                            AppConfig.getInstance().setisStore(bean.getIs_store(), true);
                            AppConfig.getInstance().setUserItemList(obj.getString("list"));

                            SpUtil.getInstance().setStringValue(SpUtil.USER_INFO, info[0]);
                            if (commonCallback != null) {
                                commonCallback.callback(bean);
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        if (commonCallback != null) {
                            commonCallback.callback(null);
                        }
                    }
                });
    }


    /**
     * 手机号 密码登录
     */
    public static void login(String phoneNum, String pwd, HttpCallback callback) {
        HttpClient.getInstance().get("Login.userLogin", HttpConsts.LOGIN)
                .params("user_login", phoneNum)
                .params("user_pass", pwd)
                .params("pushid", ImPushUtil.getInstance().getPushID())
                .execute(callback);

    }

    /**
     * 第三方登录
     */
    public static void loginByThird(String openid, String nicename, String avatar, String type, HttpCallback callback) {
        String sign = MD5Util.getMD5("openid=" + openid + "&" + SALT);
        HttpClient.getInstance().get("Login.userLoginByThird", HttpConsts.LOGIN_BY_THIRD)
                .params("openid", openid)
                .params("nicename", nicename)
                .params("avatar", avatar)
                .params("type", type)
                .params("source", DEVICE)
                .params("sign", sign)
                .params("pushid", ImPushUtil.getInstance().getPushID())
                .execute(callback);
    }

    /**
     * QQ登录的时候 获取unionID 与PC端互通的时候用
     */
    public static void getQQLoginUnionID(String accessToken, final CommonCallback<String> commonCallback) {
        OkGo.<String>get("https://graph.qq.com/oauth2.0/me?access_token=" + accessToken + "&unionid=1")
                .tag(HttpConsts.GET_QQ_LOGIN_UNION_ID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (commonCallback != null) {
                            String data = response.body();
                            data = data.substring(data.indexOf("{"), data.lastIndexOf("}") + 1);
                            L.e("getQQLoginUnionID------>" + data);
                            JSONObject obj = JSON.parseObject(data);
                            commonCallback.callback(obj.getString("unionid"));
                        }
                    }
                });
    }

    /**
     * 获取验证码接口 注册用
     */
    public static void getRegisterCode(String mobile, HttpCallback callback) {
        String sign = MD5Util.getMD5("mobile=" + mobile + "&" + SALT);
        HttpClient.getInstance().get("Login.getCode", HttpConsts.GET_REGISTER_CODE)
                .params("mobile", mobile)
                .params("sign", sign)
                .execute(callback);
    }

    /**
     * 手机注册接口
     */
    public static void register(String user_login, String pass, String pass2, String code, String invatecode, HttpCallback callback) {
        HttpClient.getInstance().get("Login.userReg", HttpConsts.REGISTER)
                .params("user_login", user_login)
                .params("user_pass", pass)
                .params("user_pass2", pass2)
                .params("code", code)
                .params("invite_code", invatecode)
                .params("source", DEVICE)
                .execute(callback);
    }

    /**
     * 找回密码
     */
    public static void findPwd(String user_login, String pass, String pass2, String code, HttpCallback callback) {
        HttpClient.getInstance().get("Login.userFindPass", HttpConsts.FIND_PWD)
                .params("user_login", user_login)
                .params("user_pass", pass)
                .params("user_pass2", pass2)
                .params("code", code)
                .execute(callback);
    }


    /**
     * 重置密码
     */
    public static void modifyPwd(String oldpass, String pass, String pass2, HttpCallback callback) {
        HttpClient.getInstance().get("User.updatePass", HttpConsts.MODIFY_PWD)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("oldpass", oldpass)
                .params("pass", pass)
                .params("pass2", pass2)
                .execute(callback);
    }


    /**
     * 获取验证码接口 找回密码用
     */
    public static void getFindPwdCode(String mobile, HttpCallback callback) {
        String sign = MD5Util.getMD5("mobile=" + mobile + "&" + SALT);
        HttpClient.getInstance().get("Login.getForgetCode", HttpConsts.GET_FIND_PWD_CODE)
                .params("mobile", mobile)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 首页直播
     */
    public static void getHot(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getHot", HttpConsts.GET_HOT)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 首页视频界面附近
     */
    public static void getVideoNear(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Video.getNearby", HttpConsts.GETVideoNEAR)
                .params("lng", AppConfig.getInstance().getLng())
                .params("lat", AppConfig.getInstance().getLat())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 直播界面附近
     */
    public static void getNear(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getNearby", HttpConsts.GET_NEAR)
                .params("lng", AppConfig.getInstance().getLng())
                .params("lat", AppConfig.getInstance().getLat())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 首页视频界面关注界面
     */
    public static void getvideoFollow(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Video.getVideoFollow", HttpConsts.GETVideoFOLLOW)
                .params("uid", AppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }
    /**
     * 首页视频界面关注界面
     */
    public static void getvideoFollowGz(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Video.getVideoFollow", HttpConsts.GETVideoFOLLOW)
                .params("uid", AppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 直播界面关注界面
     */
    public static void getFollow(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getFollow", HttpConsts.GET_FOLLOW)
                .params("uid", AppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 分类直播
     */
    public static void getClassLive(int classId, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getClassLive", HttpConsts.GET_CLASS_LIVE)
                .params("liveclassid", classId)
                .params("p", p)
                .execute(callback);
    }


    //排行榜  收益榜
    public static void profitList(String type, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.profitList", HttpConsts.PROFIT_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("type", type)
                .params("p", p)
                .execute(callback);
    }

    //排行榜  贡献榜
    public static void consumeList(String type, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.consumeList", HttpConsts.CONSUME_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("type", type)
                .params("p", p)
                .execute(callback);

    }

    /**
     * 关注别人 或 取消对别人的关注的接口
     */
    public static void setAttention(int from, String touid, CommonCallback<Integer> callback) {
        setAttention(HttpConsts.SET_ATTENTION, from, touid, callback);
    }

    /**
     * 关注别人 或 取消对别人的关注的接口
     */
    public static void setAttention(String tag, final int from, final String touid, final CommonCallback<Integer> callback) {
        if (touid.equals(AppConfig.getInstance().getUid())) {
            ToastUtil.show(WordUtil.getString(R.string.cannot_follow_self));
            return;
        }
        HttpClient.getInstance().get("User.setAttent", tag)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", touid)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            int isAttention = JSON.parseObject(info[0]).getIntValue("isattent");//1是 关注  0是未关注
                            EventBus.getDefault().post(new FollowEvent(from, touid, isAttention));
                            if (callback != null) {
                                callback.callback(isAttention);
                            }
                        }
                    }
                });
    }

    /**
     * 上传头像，用post
     */
    public static void updateAvatar(File file, HttpCallback callback) {
        HttpClient.getInstance().post("User.updateAvatar", HttpConsts.UPDATE_AVATAR)
                .isMultipart(true)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("file", file)
                .execute(callback);
    }

    /**
     * 更新用户资料
     *
     * @param fields 用户资料 ,以json形式出现
     */
    public static void updateFields(String fields, HttpCallback callback) {
        HttpClient.getInstance().get("User.updateFields", HttpConsts.UPDATE_FIELDS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("fields", fields)
                .execute(callback);
    }


    /**
     * 获取对方的关注列表
     *
     * @param touid 对方的uid
     */
    public static void getFollowList(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getFollowsList", HttpConsts.GET_FOLLOW_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 获取对方的粉丝列表
     *
     * @param touid 对方的uid
     */
    public static void getFansList(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getFansList", HttpConsts.GET_FANS_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);

    }
    public static void getFansListLL(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetFollowsList", HttpConsts.GET_FANS_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);

    }

    /**
     * 获取用户的直播记录
     *
     * @param touid 对方的uid
     */
    public static void getLiveRecord(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getLiverecord", HttpConsts.GET_LIVE_RECORD)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 获取个性设置列表
     */
    public static void getSettingList(HttpCallback callback) {
        HttpClient.getInstance().get("User.getPerSetting", HttpConsts.GET_SETTING_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 请求签到奖励
     */
    public static void requestBonus(HttpCallback callback) {
        HttpClient.getInstance().get("User.Bonus", HttpConsts.REQUEST_BONUS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 获取签到奖励
     */
    public static void getBonus(HttpCallback callback) {
        HttpClient.getInstance().get("User.getBonus", HttpConsts.GET_BONUS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 主播开播
     *
     * @param title    直播标题
     * @param type     直播类型 普通 密码 收费等
     * @param typeVal  密码 价格等
     * @param file     封面图片文件
     * @param callback
     */
    public static void createRoom(String goodsids,String title, int liveClassId, int type, int typeVal, File file, String is_shopping, HttpCallback callback) {
        AppConfig appConfig = AppConfig.getInstance();
        UserBean u = appConfig.getUserBean();
        if (u == null) {
            return;
        }
        PostRequest<JsonBean> request = HttpClient.getInstance().post("Live.createRoom", HttpConsts.CREATE_ROOM)
                .params("uid", appConfig.getUid())
                .params("token", appConfig.getToken())
                .params("user_nicename", u.getUserNiceName())
                .params("avatar", u.getAvatar())
                .params("avatar_thumb", u.getAvatarThumb())
                .params("city", appConfig.getCity())
                .params("province", appConfig.getProvince())
                .params("lat", appConfig.getLat())
                .params("lng", appConfig.getLng())
                .params("title", title)
                .params("liveclassid", liveClassId)
                .params("goodsids", goodsids)
                .params("type", type)
                .params("type_val", typeVal)
                .params("is_shopping", is_shopping);
        if (file != null) {
            request.params("file", file);
        }
        Log.e("PPPP:",request.toString());
        request.execute(callback);
    }

    /**
     * 修改直播状态
     */
    public static void changeLive(String stream) {
        HttpClient.getInstance().get("Live.changeLive", HttpConsts.CHANGE_LIVE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("status", "1")
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        L.e("开播---changeLive---->" + info[0]);
                    }
                });
    }

    /**
     * 主播结束直播
     */
    public static void stopLive(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.stopRoom", HttpConsts.STOP_LIVE)
                .params("stream", stream)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 直播结束后，获取直播收益，观看人数，时长等信息
     */
    public static void getLiveEndInfo(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.stopInfo", HttpConsts.GET_LIVE_END_INFO)
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 检查直播间状态，是否收费 是否有密码等
     *
     * @param liveUid 主播的uid
     * @param stream  主播的stream
     */
    public static void checkLive(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.checkLive", HttpConsts.CHECK_LIVE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 当直播间是门票收费，计时收费或切换成计时收费的时候，观众请求这个接口
     *
     * @param liveUid 主播的uid
     * @param stream  主播的stream
     */
    public static void roomCharge(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.roomCharge", HttpConsts.ROOM_CHARGE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("liveuid", liveUid)
                .execute(callback);

    }

    /**
     * 当直播间是计时收费的时候，观众每隔一分钟请求这个接口进行扣费
     *
     * @param liveUid 主播的uid
     * @param stream  主播的stream
     */
    public static void timeCharge(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.timeCharge", HttpConsts.TIME_CHARGE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("liveuid", liveUid)
                .execute(callback);
    }

    /**
     * 观众进入直播间
     */
    public static void enterRoom(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.enterRoom", HttpConsts.ENTER_ROOM)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("city", AppConfig.getInstance().getCity())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 发送弹幕
     */
    public static void sendDanmu(String content, String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.sendBarrage", HttpConsts.SEND_DANMU)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .params("giftid", "1")
                .params("giftcount", "1")
                .params("content", content)
                .execute(callback);
    }

    /**
     * 获取礼物列表，同时会返回剩余的钱
     */
    public static void getGiftList(HttpCallback callback) {
        HttpClient.getInstance().get("Live.getGiftList", HttpConsts.GET_GIFT_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 获取用户余额
     */
    public static void getCoin(HttpCallback callback) {
        HttpClient.getInstance().get("Live.getCoin", HttpConsts.GET_COIN)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 观众给主播送礼物
     */
    public static void sendGift(String liveUid, String stream, int giftId, String giftCount, HttpCallback callback) {
        HttpClient.getInstance().get("Live.sendGift", HttpConsts.SEND_GIFT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .params("giftid", giftId)
                .params("giftcount", giftCount)
                .execute(callback);
    }


    /**
     * 获取主播印象列表
     */
    public static void getAllImpress(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetUserLabel", HttpConsts.GET_ALL_IMPRESS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 获取自己收到的主播印象列表
     */
    public static void getMyImpress(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetMyLabel", HttpConsts.GET_MY_IMPRESS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 给主播设置印象
     */
    public static void setImpress(String touid, String ImpressIDs, HttpCallback callback) {
        HttpClient.getInstance().get("User.setUserLabel", HttpConsts.SET_IMPRESS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", touid)
                .params("labels", ImpressIDs)
                .execute(callback);
    }

    /**
     * 直播间点击聊天列表和头像出现的弹窗
     */
    public static void getLiveUser(String touid, String liveUid, HttpCallback callback) {
        HttpClient.getInstance().get("Live.getPop", HttpConsts.GET_LIVE_USER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .params("liveuid", liveUid)
                .execute(callback);
    }

    /**
     * 获取当前直播间的管理员列表
     */
    public static void getAdminList(String liveUid, HttpCallback callback) {
        HttpClient.getInstance().get("Live.getAdminList", HttpConsts.GET_ADMIN_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("liveuid", liveUid)
                .execute(callback);
    }

    /**
     * 主播设置或取消直播间的管理员
     */
    public static void setAdmin(String liveUid, String touid, HttpCallback callback) {
        HttpClient.getInstance().get("Live.setAdmin", HttpConsts.SET_ADMIN)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 主播或管理员踢人
     */
    public static void kicking(String liveUid, String touid, HttpCallback callback) {
        HttpClient.getInstance().get("Live.kicking", HttpConsts.KICKING)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("touid", touid)
                .execute(callback);
    }


    /**
     * 主播或管理员禁言
     */
    public static void setShutUp(String liveUid, String touid, HttpCallback callback) {
        HttpClient.getInstance().get("Live.setShutUp", HttpConsts.SET_SHUT_UP)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("touid", touid)
                .execute(callback);
    }


    /**
     * 超管关闭直播间或禁用账户
     */
    public static void superCloseRoom(String liveUid, boolean forbidAccount, HttpCallback callback) {
        HttpClient.getInstance().get("Live.superStopRoom", HttpConsts.SUPER_CLOSE_ROOM)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("type", forbidAccount ? 1 : 0)
                .execute(callback);
    }


    /**
     * 举报用户
     */
    public static void setReport(String touid, String content, HttpCallback callback) {
        HttpClient.getInstance().get("Live.setReport", HttpConsts.SET_REPORT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", touid)
                .params("content", content)
                .execute(callback);
    }

    /**
     * 用户个人主页信息
     */
    public static void getUserHome(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("User.getCenterInfo", HttpConsts.GET_USER_HOME)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", touid)
                .execute(callback);
    }
    /**
     * 用户个人主页信息
     */
    public static void isBlack(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("User.IsBlacked", HttpConsts.GET_USER_HOME)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", touid)
                .execute(callback);
    }
    public static void getUserHomeBackUp(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("User.getUserHome", HttpConsts.GET_USER_HOME)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 拉黑对方， 解除拉黑
     */
    public static void setBlack(String toUid, HttpCallback callback) {
        HttpClient.getInstance().get("User.setBlack", HttpConsts.SET_BLACK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", toUid)
                .execute(callback);
    }

    /**
     * 主播添加背景音乐时，搜索歌曲
     *
     * @param key      关键字
     * @param callback
     */
    public static void searchMusic(String key, HttpCallback callback) {
        HttpClient.getInstance().get("Livemusic.searchMusic", HttpConsts.SEARCH_MUSIC)
                .params("key", key)
                .execute(callback);
    }

    /**
     * 获取歌曲的地址 和歌词的地址
     */
    public static void getMusicUrl(String musicId, HttpCallback callback) {
        HttpClient.getInstance().get("Livemusic.getDownurl", HttpConsts.GET_MUSIC_URL)
                .params("audio_id", musicId)
                .execute(callback);
    }

    /**
     * 获取 我的收益 可提现金额数
     */
    public static void getProfit(HttpCallback callback) {
        HttpClient.getInstance().get("User.getProfit", HttpConsts.GET_PROFIT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }
    public static void getProfitDp(HttpCallback callback) {
        HttpClient.getInstance().get("Setting.getStoreProfit", HttpConsts.GET_PROFIT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 获取 提现账户列表
     */
    public static void getCashAccountList(HttpCallback callback) {
        HttpClient.getInstance().get("User.GetUserAccountList", HttpConsts.GET_USER_ACCOUNT_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 添加 提现账户
     */
    public static void addCashAccount(String account, String name, String bank, int type, HttpCallback callback) {
        HttpClient.getInstance().get("User.SetUserAccount", HttpConsts.ADD_CASH_ACCOUNT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("account", account)
                .params("name", name)
                .params("account_bank", bank)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 删除 提现账户
     */
    public static void deleteCashAccount(String accountId, HttpCallback callback) {
        HttpClient.getInstance().get("User.delUserAccount", HttpConsts.DEL_CASH_ACCOUNT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("id", accountId)
                .execute(callback);
    }

    /**
     * 提现
     */
    public static void doCash(String catid,String votes, String accountId, HttpCallback callback) {
        HttpClient.getInstance().get("User.setCash", HttpConsts.DO_CASH)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("cashvote", votes)//提现的票数
                .params("accountid", accountId)//账号ID
                .params("catid", catid)//账号ID
                .execute(callback);
    }

    /**
     * 充值页面，我的钻石
     */
    public static void getBalance(HttpCallback callback) {
        HttpClient.getInstance().get("User.getBalance", HttpConsts.GET_BALANCE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 用支付宝充值 的时候在服务端生成订单号
     *
     * @param money    RMB价格
     * @param changeid 要购买的钻石的id
     * @param coin     要购买的钻石的数量
     * @param callback
     */
    public static void getAliOrder(String money, String changeid, String coin, HttpCallback callback) {
        HttpClient.getInstance().get("Charge.getAliOrder", HttpConsts.GET_ALI_ORDER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("money", money)
                .params("changeid", changeid)
                .params("coin", coin)
                .execute(callback);
    }

    /**
     * 用微信支付充值 的时候在服务端生成订单号
     *
     * @param money    RMB价格
     * @param changeid 要购买的钻石的id
     * @param coin     要购买的钻石的数量
     * @param callback
     */
    public static void getWxOrder(String money, String changeid, String coin, HttpCallback callback) {
        HttpClient.getInstance().get("Charge.getWxOrder", HttpConsts.GET_WX_ORDER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("money", money)
                .params("changeid", changeid)
                .params("coin", coin)
                .execute(callback);
    }

    /**
     * 我的订单 售后/审核 详情
     *
     * @param goodsId
     * @param orderId
     * @param callback
     */
    public static void getOrderRefundDetail(String goodsId, String orderId, HttpCallback callback) {
        HttpClient.getInstance().get("Order.GetRefund", HttpConsts.ORDERGETLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_id", goodsId)
                .params("order_id", orderId)
                .execute(callback);
    }

    /**
     * 我的退款原因
     * @param callback
     */
    public static void  GetRefundReasonList( HttpCallback callback) {
        HttpClient.getInstance().get("Order.GetRefundReasonList", HttpConsts.ORDERGETLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", 1)
                .execute(callback);
    }

    /**
     * 提交申请
     *
     * @param goodsId
     * @param orderId
     * @param callback
     */
    public static void setRefundOrder(String goodsId,String orderId, String content,String refund_reason_id,String iamgeUrl, HttpCallback callback) {
        HttpClient.getInstance().get("Order.SetRefund", HttpConsts.ORDERGETLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_id", goodsId)
                .params("order_id", orderId)
                .params("content",content)
                .params("refund_reason_id",refund_reason_id)
                .params("images",iamgeUrl)
                .execute(callback);
    }


    /**
     * 单个图片，用post
     */
    public static void updateImage(File file, HttpCallback callback) {
        HttpClient.getInstance().post("Order.getUpload", HttpConsts.UPDATE_AVATAR)
                .isMultipart(true)
                .params("file", file)
                .execute(callback);
    }
    /**
     * 私信聊天页面用于获取用户信息
     */
    public static void getImUserInfo(String uids, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetUidsInfo", HttpConsts.GET_IM_USER_INFO)
                .params("uid", AppConfig.getInstance().getUid())
                .params("uids", uids)
                .execute(callback);
    }

    /**
     * 判断自己有没有被对方拉黑，聊天的时候用到
     */
    public static void checkBlack(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("User.checkBlack", HttpConsts.CHECK_BLACK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 搜索
     */
    public static void search(String key, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.search", HttpConsts.SEARCH)
                .params("uid", AppConfig.getInstance().getUid())
                .params("key", key)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 观众跟主播连麦时，获取自己的流地址
     */
    public static void getLinkMicStream(HttpCallback callback) {
        HttpClient.getInstance().get("Linkmic.RequestLVBAddrForLinkMic", HttpConsts.GET_LINK_MIC_STREAM)
                .params("uid", AppConfig.getInstance().getUid())
                .execute(callback);
    }

    /**
     * 主播连麦成功后，要把这些信息提交给服务器
     *
     * @param touid    连麦用户ID
     * @param pull_url 连麦用户播流地址
     */
    public static void linkMicShowVideo(String touid, String pull_url) {
        HttpClient.getInstance().get("Live.showVideo", HttpConsts.LINK_MIC_SHOW_VIDEO)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .params("pull_url", pull_url)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {

                    }
                });
    }


    /**
     * 获取当前直播间的用户列表
     */
    public static void getUserList(String liveuid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.getUserLists", HttpConsts.GET_USER_LIST)
                .params("liveuid", liveuid)
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 获取直播回放url
     *66666666666
     * @param recordId 视频的id
     */
    public static void getAliCdnRecord(String recordId, HttpCallback callback) {
        HttpClient.getInstance().get("User.getAliCdnRecord", HttpConsts.GET_ALI_CDN_RECORD)
                .params("id", recordId)
                .execute(callback);
    }


    /**
     * 用于用户首次登录推荐
     */
    public static void getRecommend(HttpCallback callback) {
        HttpClient.getInstance().get("Home.getRecommend", HttpConsts.GET_RECOMMEND)
                .params("uid", AppConfig.getInstance().getUid())
                .execute(callback);
    }


    /**
     * 用于用户首次登录推荐,关注主播
     */
    public static void recommendFollow(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("Home.attentRecommend", HttpConsts.RECOMMEND_FOLLOW)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 用于用户首次登录设置分销关系
     */
    public static void setDistribut(String code, HttpCallback callback) {
        HttpClient.getInstance().get("User.setDistribut", HttpConsts.SET_DISTRIBUT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("code", code)
                .execute(callback);
    }


    /**
     * 守护商品类型列表
     */
    public static void getGuardBuyList(HttpCallback callback) {
        HttpClient.getInstance().get("Guard.getList", HttpConsts.GET_GUARD_BUY_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 购买守护接口
     */
    public static void buyGuard(String liveUid, String stream, int guardId, HttpCallback callback) {
        HttpClient.getInstance().get("Guard.BuyGuard", HttpConsts.BUY_GUARD)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .params("guardid", guardId)
                .execute(callback);
    }


    /**
     * 查看主播的守护列表
     */
    public static void getGuardList(String liveUid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Guard.GetGuardList", HttpConsts.GET_GUARD_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取主播连麦pk列表
     */
    public static void getLivePkList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Livepk.GetLiveList", HttpConsts.GET_LIVE_PK_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 连麦pk搜索主播
     */
    public static void livePkSearchAnchor(String key, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Livepk.Search", HttpConsts.LIVE_PK_SEARCH_ANCHOR)
                .params("uid", AppConfig.getInstance().getUid())
                .params("key", key)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 连麦pk检查对方主播在线状态
     */
    public static void livePkCheckLive(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Livepk.checkLive", HttpConsts.LIVE_PK_CHECK_LIVE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .execute(callback);
    }


    /**
     * 直播间发红包
     */
    public static void sendRedPack(String stream, String coin, String count, String title, int type, int sendType, HttpCallback callback) {
        HttpClient.getInstance().get("Red.SendRed", HttpConsts.SEND_RED_PACK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("coin", coin)
                .params("nums", count)
                .params("des", title)
                .params("type", type)
                .params("type_grant", sendType)
                .execute(callback);
    }

    /**
     * 获取直播间红包列表
     */
    public static void getRedPackList(String stream, HttpCallback callback) {
        String sign = MD5Util.getMD5("stream=" + stream + "&" + SALT);
        HttpClient.getInstance().get("Red.GetRedList", HttpConsts.GET_RED_PACK_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("sign", sign)
                .execute(callback);
    }

    /**
     * 直播间抢红包
     */
    public static void robRedPack(String stream, int redPackId, HttpCallback callback) {
        String uid = AppConfig.getInstance().getUid();
        String sign = MD5Util.getMD5("redid=" + redPackId + "&stream=" + stream + "&uid=" + uid + "&" + SALT);
        HttpClient.getInstance().get("Red.RobRed", HttpConsts.ROB_RED_PACK)
                .params("uid", uid)
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("redid", redPackId)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 直播间红包领取详情
     */
    public static void getRedPackResult(String stream, int redPackId, HttpCallback callback) {
        String uid = AppConfig.getInstance().getUid();
        String sign = MD5Util.getMD5("redid=" + redPackId + "&stream=" + stream + "&" + SALT);
        HttpClient.getInstance().get("Red.GetRedRobList", HttpConsts.GET_RED_PACK_RESULT)
                .params("uid", uid)
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("redid", redPackId)
                .params("sign", sign)
                .execute(callback);
    }

    /**
     * 获取系统消息列表
     */
    public static void getSystemMessageList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Message.GetList", HttpConsts.GET_SYSTEM_MESSAGE_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 主播设置是否允许观众发起连麦
     */
    public static void setLinkMicEnable(boolean linkMicEnable, HttpCallback callback) {
        HttpClient.getInstance().get("Linkmic.setMic", HttpConsts.SET_LINK_MIC_ENABLE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("ismic", linkMicEnable ? 1 : 0)
                .execute(callback);
    }


    /**
     * 观众检查主播是否允许连麦
     */
    public static void checkLinkMicEnable(String liveUid, HttpCallback callback) {
        HttpClient.getInstance().get("Linkmic.isMic", HttpConsts.CHECK_LINK_MIC_ENABLE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("liveuid", liveUid)
                .execute(callback);
    }

    /**
     * 获取直播间举报内容列表
     */
    public static void getLiveReportList(HttpCallback callback) {
        HttpClient.getInstance().get("Live.getReportClass", HttpConsts.GET_LIVE_REPORT_LIST)
                .execute(callback);
    }

    /**********************
     * 视频
     *****************/

    /**
     * 获取首页视频列表
     */
    public static void getHomeVideoListTc(int p, String type_id, HttpCallback callback) {
        HttpClient.getInstance().get("Video.getNearby", HttpConsts.GET_HOME_VIDEO_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .params("lng", AppConfig.getInstance().getLng())
                .params("lat", AppConfig.getInstance().getLat())
//                .params("type_id", type_id)
                .execute(callback);
    }
    public static void getHomeVideoListTj(int p, String type_id, HttpCallback callback) {
        HttpClient.getInstance().get("Video.getVideoList", HttpConsts.GET_HOME_VIDEO_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
//                .params("type_id", type_id)
                .execute(callback);
    }
    public static void getHomeVideoList(int p, String type_id, HttpCallback callback) {
        HttpClient.getInstance().get("Video.getVideoList", HttpConsts.GET_HOME_VIDEO_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .params("type_id", type_id)
                .execute(callback);
    }

    /**
     * 视频点赞
     */
    public static void setVideoLike(String tag, String videoid, HttpCallback callback) {
        HttpClient.getInstance().get("Video.AddLike", tag)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("videoid", videoid)
                .execute(callback);
    }

    /**
     * 获取视频评论
     */
    public static void getVideoCommentList(String videoid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Video.GetComments", HttpConsts.GET_VIDEO_COMMENT_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("videoid", videoid)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 评论点赞
     */
    public static void setCommentLike(String commentid, HttpCallback callback) {
        HttpClient.getInstance().get("Video.addCommentLike", HttpConsts.SET_COMMENT_LIKE)
                .params("commentid", commentid)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 发表评论
     */
    public static void setComment(String toUid, String videoId, String content, String commentId, String parentId, HttpCallback callback) {
        HttpClient.getInstance().get("Video.setComment", HttpConsts.SET_COMMENT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", toUid)
                .params("videoid", videoId)
                .params("commentid", commentId)
                .params("parentid", parentId)
                .params("content", content)
                .params("at_info", "")
                .execute(callback);
    }


    /**
     * 获取评论回复
     */
    public static void getCommentReply(String commentid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Video.getReplys", HttpConsts.GET_COMMENT_REPLY)
                .params("commentid", commentid)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 获取视频音乐分类列表
     */
    public static void getMusicClassList(HttpCallback callback) {
        HttpClient.getInstance().get("Music.classify_list", HttpConsts.GET_MUSIC_CLASS_LIST)
                .execute(callback);
    }

    /**
     * 获取热门视频音乐列表
     */
    public static void getHotMusicList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Music.hotLists", HttpConsts.GET_HOT_MUSIC_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 音乐收藏
     */
    public static void setMusicCollect(int muiscId, HttpCallback callback) {
        HttpClient.getInstance().get("Music.collectMusic", HttpConsts.SET_MUSIC_COLLECT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("musicid", muiscId)
                .execute(callback);
    }

    /**
     * 音乐收藏列表
     */
    public static void getMusicCollectList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Music.getCollectMusicLists", HttpConsts.GET_MUSIC_COLLECT_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 获取具体分类下的音乐列表
     */
    public static void getMusicList(String classId, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Music.music_list", HttpConsts.GET_MUSIC_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("classify", classId)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 搜索音乐
     */
    public static void videoSearchMusic(String key, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Music.searchMusic", HttpConsts.VIDEO_SEARCH_MUSIC)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("key", key)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 上传视频，获取七牛云token的接口
     */
    public static void getQiNiuToken(HttpCallback callback) {
        HttpClient.getInstance().get("Video.getQiniuToken", HttpConsts.GET_QI_NIU_TOKEN)
                .execute(callback);
    }


    /**
     * 短视频上传信息
     *
     * @param title   短视频标题
     * @param thumb   短视频封面图url
     * @param href    短视频视频url
     * @param musicId 背景音乐Id
     *                type_id:分类
     */
    public static void saveUploadVideoInfo(String title, String thumb, String href, int musicId, String is_shopping, String type_id, HttpCallback callback) {
        HttpClient.getInstance().get("Video.setVideo", HttpConsts.SAVE_UPLOAD_VIDEO_INFO)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("lat", AppConfig.getInstance().getLat())
                .params("lng", AppConfig.getInstance().getLng())
                .params("city", AppConfig.getInstance().getCity())
                .params("title", title)
                .params("thumb", thumb)
                .params("href", href)
                .params("music_id", musicId)
                .params(" is_shopping", is_shopping)
                .params(" type_id", type_id)
                .execute(callback);
    }

    /**
     * 获取腾讯云储存上传签名
     */
    public static void getTxUploadCredential(StringCallback callback) {
        OkGo.<String>get(AppConfig.HOST + "/api/public/?service=Video.getCreateNonreusableSignature")
//        OkGo.<String>get("http://upload.qq163.iego.cn:8088/cam")
                .execute(callback);
    }

    /**
     * 获取某人发布的视频
     */
    public static void getHomeVideo1(String toUid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Video.getHomeVideo", HttpConsts.GET_HOME_VIDEO)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", toUid)
                .params("p", p)
                .execute(callback);
    }
    /**
     * 获取某人发布的视频
     */
    public static void getHomeVideo(String type,String toUid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetMyWorks", "User.GetMyWorks")
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", toUid)
                .params("p", p)
//                .params("type", type)
                .execute(callback);
    }
    /**
     * 获取某人发布的视频
     */
    public static void getHomeVideoLike(String type,String toUid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getLikeVideoList", "User.getLikeVideoList")
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", toUid)
                .params("p", p)
//                .params("type", type)
                .execute(callback);
    }


    /**
     * 获取举报内容列表
     */
    public static void getVideoReportList(HttpCallback callback) {
        HttpClient.getInstance().get("Video.getReportContentlist", HttpConsts.GET_VIDEO_REPORT_LIST)
                .execute(callback);
    }


    /**
     * 举报视频接口
     */
    public static void videoReport(String videoId, String reportId, String content, HttpCallback callback) {
        HttpClient.getInstance().get("Video.report", HttpConsts.VIDEO_REPORT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("videoid", videoId)
                .params("type", reportId)
                .params("content", content)
                .execute(callback);
    }

    /**
     * 删除自己的视频
     */
    public static void videoDelete(String videoid, HttpCallback callback) {
        HttpClient.getInstance().get("Video.del", HttpConsts.VIDEO_DELETE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("videoid", videoid)
                .execute(callback);
    }

    /**
     * 分享视频
     */
    public static void setVideoShare(String videoid, HttpCallback callback) {
        String uid = AppConfig.getInstance().getUid();
        String s = MD5Util.getMD5(uid + "-" + videoid + "-" + VIDEO_SALT);
        HttpClient.getInstance().get("Video.addShare", HttpConsts.SET_VIDEO_SHARE)
                .params("uid", uid)
                .params("token", AppConfig.getInstance().getToken())
                .params("videoid", videoid)
                .params("random_str", s)
                .execute(callback);
    }


    /**
     * 开始观看视频的时候请求这个接口
     */
    public static void videoWatchStart(String videoUid, String videoId) {
        String uid = AppConfig.getInstance().getUid();
        if (TextUtils.isEmpty(uid) || uid.equals(videoUid)) {
            return;
        }
        HttpUtil.cancel(HttpConsts.VIDEO_WATCH_START);
        String s = MD5Util.getMD5(uid + "-" + videoId + "-" + VIDEO_SALT);
        HttpClient.getInstance().get("Video.addView", HttpConsts.VIDEO_WATCH_START)
                .params("uid", uid)
                .params("token", AppConfig.getInstance().getToken())
                .params("videoid", videoId)
                .params("random_str", s)
                .execute(NO_CALLBACK);
    }

    /**
     * 完整观看完视频后请求这个接口
     */
    public static void videoWatchEnd(String videoUid, String videoId) {
        String uid = AppConfig.getInstance().getUid();
        if (TextUtils.isEmpty(uid) || uid.equals(videoUid)) {
            return;
        }
        HttpUtil.cancel(HttpConsts.VIDEO_WATCH_END);
        String s = MD5Util.getMD5(uid + "-" + videoId + "-" + VIDEO_SALT);
        HttpClient.getInstance().get("Video.setConversion", HttpConsts.VIDEO_WATCH_END)
                .params("uid", uid)
                .params("token", AppConfig.getInstance().getToken())
                .params("videoid", videoId)
                .params("random_str", s)
                .execute(NO_CALLBACK);
    }


    //不做任何操作的HttpCallback
    private static final HttpCallback NO_CALLBACK = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {

        }
    };


    /**********************
     * 游戏
     *****************/

    /**
     * 创建炸金花游戏
     */
    public static void gameJinhuaCreate(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Jinhua", HttpConsts.GAME_JINHUA_CREATE)
                .params("liveuid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 炸金花游戏下注
     */
    public static void gameJinhuaBet(String gameid, int coin, int grade, HttpCallback callback) {
        HttpClient.getInstance().get("Game.JinhuaBet", HttpConsts.GAME_JINHUA_BET)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("gameid", gameid)
                .params("coin", coin)
                .params("grade", grade)
                .execute(callback);
    }

    /**
     * 游戏结果出来后，观众获取自己赢到的金额
     */
    public static void gameSettle(String gameid, HttpCallback callback) {
        HttpClient.getInstance().get("Game.settleGame", HttpConsts.GAME_SETTLE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("gameid", gameid)
                .execute(callback);
    }

    /**
     * 创建海盗船长游戏
     */
    public static void gameHaidaoCreate(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Taurus", HttpConsts.GAME_HAIDAO_CREATE)
                .params("liveuid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 海盗船长游戏下注
     */
    public static void gameHaidaoBet(String gameid, int coin, int grade, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Taurus_Bet", HttpConsts.GAME_HAIDAO_BET)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("gameid", gameid)
                .params("coin", coin)
                .params("grade", grade)
                .execute(callback);
    }

    /**
     * 创建幸运转盘游戏
     */
    public static void gameLuckPanCreate(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Dial", HttpConsts.GAME_LUCK_PAN_CREATE)
                .params("liveuid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 幸运转盘游戏下注
     */
    public static void gameLuckPanBet(String gameid, int coin, int grade, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Dial_Bet", HttpConsts.GAME_LUCK_PAN_BET)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("gameid", gameid)
                .params("coin", coin)
                .params("grade", grade)
                .execute(callback);
    }

    /**
     * 创建开心牛仔游戏
     */
    public static void gameNiuzaiCreate(String stream, String bankerid, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Cowboy", HttpConsts.GAME_NIUZAI_CREATE)
                .params("liveuid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("bankerid", bankerid)
                .execute(callback);
    }

    /**
     * 开心牛仔游戏下注
     */
    public static void gameNiuzaiBet(String gameid, int coin, int grade, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Cowboy_Bet", HttpConsts.GAME_NIUZAI_BET)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("gameid", gameid)
                .params("coin", coin)
                .params("grade", grade)
                .execute(callback);
    }

    /**
     * 开心牛仔游戏胜负记录
     */
    public static void gameNiuRecord(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Game.getGameRecord", HttpConsts.GAME_NIU_RECORD)
                .params("action", "4")
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 开心牛仔庄家流水
     */
    public static void gameNiuBankerWater(String bankerId, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Game.getBankerProfit", HttpConsts.GAME_NIU_BANKER_WATER)
                .params("bankerid", bankerId)
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 开心牛仔获取庄家列表,列表第一个为当前庄家
     */
    public static void gameNiuGetBanker(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Game.getBanker", HttpConsts.GAME_NIU_GET_BANKER)
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 开心牛仔申请上庄
     */
    public static void gameNiuSetBanker(String stream, String deposit, HttpCallback callback) {
        HttpClient.getInstance().get("Game.setBanker", HttpConsts.GAME_NIU_SET_BANKER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .params("deposit", deposit)
                .execute(callback);
    }

    /**
     * 开心牛仔申请下庄
     */
    public static void gameNiuQuitBanker(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Game.quietBanker", HttpConsts.GAME_NIU_QUIT_BANKER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 创建二八贝游戏
     */
    public static void gameEbbCreate(String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Cowry", HttpConsts.GAME_EBB_CREATE)
                .params("liveuid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 二八贝下注
     */
    public static void gameEbbBet(String gameid, int coin, int grade, HttpCallback callback) {
        HttpClient.getInstance().get("Game.Cowry_Bet", HttpConsts.GAME_EBB_BET)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("gameid", gameid)
                .params("coin", coin)
                .params("grade", grade)
                .execute(callback);
    }

    /**
     * 小店申请条件
     */
    public static void shopCondition(HttpCallback callback) {
        HttpClient.getInstance().get("User.GetUserApply", HttpConsts.GET_USER_APPLY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 添加商品
     */
    public static void shopAddgoods(String link, String name, String oldPrice, String price, String content, List<File> mFilelist, ArrayList<String> mPicList, HttpCallback callback) {
        Log.v("tags", mFilelist.size() + "-----------");


        PostRequest<JsonBean> request = HttpClient.getInstance().post("User.AddGood", HttpConsts.ADD_GOOD)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_url", link)
                .params("title", name)
                .params("ot_price", oldPrice)
                .params("price", price)
                .params("description", content)
                .addFileParams("file", mFilelist);


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Map<String, String> param = new HashMap<>();
        param.put("uid", AppConfig.getInstance().getUid());
        param.put("token", AppConfig.getInstance().getToken());
        param.put("goods_url", link);
        param.put("title", name);
        param.put("ot_price", oldPrice);
        param.put("price", price);
        param.put("description", content);
        for (String mapkey : param.keySet()) {
            builder.addFormDataPart(mapkey, param.get(mapkey));
        }
        for (int i = 0; i < mFilelist.size(); i++) {
            Log.i("logger", mFilelist.get(i).getPath());
            request.params("file_" + "[" + i + "]", mFilelist.get(i), mFilelist.get(i).getName(), MediaType.parse("image/*"));
            builder.addFormDataPart("file_" + "[" + i + "]", mFilelist.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), mFilelist.get(i)));
        }

        RequestBody requestBody = builder.build();
        Request reqs = request.getRequest();
        Request.Builder builder1 = reqs.newBuilder();
        builder1.url(AppConfig.HOST + "/api/public/?service=User.AddGood")
                .post(requestBody);
        builder1.build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(reqs);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.v("tags", response.message());
            }
        });

//        request.execute(callback);
    }

    /**
     * 小店店铺列表
     */
    public static void shopList(int page, String store_id, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetUserStore", HttpConsts.GET_USER_STORE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params(" store_id", store_id)
                .params("p", page)
                .execute(callback);
    }
    /**
     * 小店店铺列表
     */
    public static void shopListNew(int page, String store_id, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getGoods", HttpConsts.GET_USER_STORE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params(" storeid", store_id)
                .params("p", page)
                .execute(callback);
    }

    /**
     * 小店店铺详情
     */
    public static void shopDetail(String goodsId, String store_id, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetGoodsDetail", HttpConsts.GET_GOODS_DETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params(" store_id", store_id)
                .params("goods_id", goodsId)
                .execute(callback);
    }
    /**
     * 优惠券列表
     */
    public static void getYhqList(String store_id, HttpCallback callback) {
        HttpClient.getInstance().get("User.getCouponsList", HttpConsts.GET_GOODS_DETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params(" storeid", store_id)
                .execute(callback);
    }
    /**
     * 收藏
     */
    public static void saveShop(String store_id, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.setCollect", HttpConsts.GET_GOODS_DETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params(" goodsid", store_id)
                .execute(callback);
    }
    /**
     * 领取优惠券
     */
    public static void getYhqLq(String couponsid, HttpCallback callback) {
        HttpClient.getInstance().get("User.setCoupons", HttpConsts.GET_GOODS_DETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params(" couponsid", couponsid)
                .execute(callback);
    }

    /**
     * 商品下架
     */
    public static void shopGoodsLower(String goodsId, String is_show, HttpCallback callback) {
        HttpClient.getInstance().get("User.SetGoodsLower", HttpConsts.SET_GOODS_LOWER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("is_show", is_show)
                .params("goods_id", goodsId)
                .execute(callback);
    }

    /**
     * 商品删除
     */
    public static void shopGoodsDel(String goodsId, HttpCallback callback) {
        HttpClient.getInstance().get("User.DelGoods", HttpConsts.DEL_GOODS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_id", goodsId)
                .execute(callback);
    }

    /**
     * 直播下的商品列表
     */
    public static void shopGoodsList(int page, String mLiveUid, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetGoodsList", HttpConsts.GET_GOODS_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("store_id", mLiveUid)
                .params("p", page)
                .execute(callback);
    }
    /**
     * 直播下的商品列表
     */
    public static void shopZbGoodsList(int page, String mLiveUid, HttpCallback callback) {
        HttpClient.getInstance().get("Live.getLiveGoods", HttpConsts.GET_GOODS_LIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("touid", mLiveUid)
                .params("p", page)
                .execute(callback);
    }

    /**
     * 私信发红包
     *
     * @param recvid
     * @param coin
     * @param desc
     * @param callback
     */
    public static void sendRedPackData(String recvid, String coin, String desc, HttpCallback callback) {
        HttpClient.getInstance().get("red.sendRedPackage", HttpConsts.SEND_RED_PACKAGE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("recvid", recvid)
                .params("coin", coin)
                .params("desc", desc)
                .execute(callback);
    }

//----------------------------------------商城接口------------------------------------------------------

    /**
     * 直播下的商品列表
     */
    public static void getAddressList(HttpCallback callback) {
        HttpClient.getInstance().get("Address.GetList", HttpConsts.ADDRESS_GETLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 添加地址和修改地址
     *
     * @param realname
     * @param phone
     * @param district
     * @param detail
     * @param isdefault
     * @param callback
     */
    public static void AddAddress(String mAddressId, String realname, String phone, String district, String detail, String isdefault, boolean isUpData, HttpCallback callback) {
        String urls = "";
        if (!isUpData) {
            urls = "Address.AddAddress";
        } else {
            urls = "Address.UpAddress";
        }
        HttpClient.getInstance().post(urls, HttpConsts.ADDRESS_AddAddress)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("realname", realname)
                .params("phone", phone)
                .params("district", district)
                .params("detail", detail)
                .params("address_id", mAddressId)
                .params("is_default", isdefault)
                .execute(callback);
    }

    /**
     * 删除地址
     *
     * @param mAddressId
     * @param callback
     */
    public static void DelAddress(String mAddressId, HttpCallback callback) {
        HttpClient.getInstance().post("Address.DelAddress", HttpConsts.ADDRESS_DELADDRESS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("address_id", mAddressId)
                .execute(callback);
    }

    /**
     * 商城首页
     *
     * @param page
     * @param type     参数类型，1表示爆款推荐，2表示折扣专区
     * @param order    排序类型，1类型，2销量,3价格
     * @param callback
     */
    public static void GetShopMall(String page, String type, String cate_id, String order, String keyword, HttpCallback callback) {
        HttpClient.getInstance().get("Home.GetShop", HttpConsts.HOME_GETSHOP)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("type", type)
                .params("cate_id", cate_id)
                .params("keyword", keyword)
                .params("order", order)
                .execute(callback);
    }

    /**
     * 添加商品到购物车
     *
     * @param goodsId
     * @param goods_nums
     * @param callback
     */
    public static void addGoodsToCart(String goodsId, String goods_nums, HttpCallback callback) {
        HttpClient.getInstance().get("Cart.AddCart", HttpConsts.ADDCART)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_id", goodsId)
                .params("goods_nums", goods_nums)
                .execute(callback);
    }

    public static void addGoodsWithSpanToCart(String goodsId, String goods_attr, String goods_nums, HttpCallback callback) {
        HttpClient.getInstance().get("Cart.AddCart", HttpConsts.ADDCART)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_id", goodsId)
                .params("goods_attr",goods_attr)
                .params("goods_nums", goods_nums)
                .execute(callback);
    }
    /**
     * 购物车数量增加减少
     *
     * @param goodsId
     * @param goods_nums
     * @param callback
     */
    public static void setCartGoodsNum(String goodsId, String goods_nums, HttpCallback callback) {
        HttpClient.getInstance().get("Cart.SetCart", HttpConsts.CARTSETCART)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("cart_id", goodsId)
                .params("cart_num", goods_nums)
                .execute(callback);
    }

    /**
     * 删除购物车
     *
     * @param goodsId
     * @param callback
     */
    public static void DelCart(String goodsId, HttpCallback callback) {
        HttpClient.getInstance().get("Cart.DelCart", HttpConsts.CARTDELCART)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("cart_id", goodsId)
                .execute(callback);
    }

    public static void CartCheckAll(String goodsId, HttpCallback callback) {
        HttpClient.getInstance().get("Cart.CheckAll", HttpConsts.CARTCHECKALL)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("cart_id", goodsId)
                .execute(callback);
    }

    /**
     * 购物车列表
     *
     * @param callback
     */
    public static void getCartList(HttpCallback callback) {
        HttpClient.getInstance().get("Cart.GetList", HttpConsts.CARTGETLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 确认订单
     *
     * @param goods_id
     * @param cart_num
     * @param cart_ids
     * @param callback
     */
    public static void SureOrder(String goods_id, String cart_num, String cart_ids, HttpCallback callback) {
        HttpClient.getInstance().get("order.sureOrder", HttpConsts.ORDERSUREORDER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_id", goods_id)
                .params("goods_attr", goods_id)
                .params("cart_num", cart_num)
                .params("cart_ids", cart_ids)
                .execute(callback);
    }

    public static void SureCartOrder(String goods_id, String cart_num, String cart_ids, HttpCallback callback) {
        HttpClient.getInstance().get("order.sureOrder", HttpConsts.ORDERSUREORDER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_attr", goods_id)
                .params("cart_num", cart_num)
                .params("cart_ids", cart_ids)
                .execute(callback);
    }
    /**
     * 创建订单
     *
     * @param addressId
     * @param mark
     * @param callback
     * @param  isMS 1为秒杀
     */
    public static void OrderCreate(String addressId, String goods_list, String mark,String isMS, HttpCallback callback) {
        HttpClient.getInstance().post("order.goOrder", HttpConsts.ORDERCREATE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("address_id", addressId)
                .params("goods_list", goods_list)
                .params("is_ms",isMS)
                .params("mark", mark)
                .execute(callback);
    }

    public static void OrderGetPay(String out_trade_no, String type, HttpCallback callback) {
        HttpClient.getInstance().get("Order.getPay", HttpConsts.ORDERGETPAY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("out_trade_no", out_trade_no)
                .params("pay_type", type)
                .execute(callback);
    }
    public static void GetAlPay(String out_trade_no, HttpCallback callback) {
        HttpClient.getInstance().get("Auth.getAliOrder", HttpConsts.ORDERGETPAY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("pay_sn", out_trade_no)
                .execute(callback);
    }
    public static void GetWxPay(String out_trade_no,  HttpCallback callback) {
        HttpClient.getInstance().get("Auth.getWxOrder", HttpConsts.ORDERGETPAY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("pay_sn", out_trade_no)
                .execute(callback);
    }


    /**
     * 我的订单列表
     *
     * @param page
     * @param type
     * @param callback
     */
    public static void OrderGetList(String page, String type, String keyword, HttpCallback callback) {
        HttpClient.getInstance().get("Order.GetList", HttpConsts.ORDERGETLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("keyword", keyword)
                .params("order_state", type)
                .execute(callback);
    }

    /**
     * 订单详情
     *
     * @param order_id
     * @param callback
     */
    public static void OrderGetOrderDetail(String order_id, HttpCallback callback) {
        HttpClient.getInstance().get("Order.GetOrderDetail", HttpConsts.ORDERGETORDERDETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("order_id", order_id)
                .execute(callback);
    }

    /**
     * 我的店铺  我的订单详情
     *
     * @param order_id
     * @param callback
     */
    public static void OrderGetStoreOrderDetail(String order_id, HttpCallback callback) {
        HttpClient.getInstance().get("Order.getStoreOrderDetail", HttpConsts.ORDERGETSTOREORDERDETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("order_id", order_id)
                .execute(callback);
    }

    /**
     * 我的小店 订单列表
     *
     * @param page
     * @param type
     * @param callback
     */
    public static void OrderStoreList(String page, String type, String keyword, HttpCallback callback) {
        HttpClient.getInstance().get("Order.GetStoreList", HttpConsts.ORDERSTORELIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("keyword", keyword)
                .params("order_state", type)
                .execute(callback);
    }

    /**
     * 物流公司
     *
     * @param page
     * @param callback
     */
    public static void OrderLogisticsList(String page, HttpCallback callback) {
        HttpClient.getInstance().get("Order.LogisticsList", HttpConsts.ORDERLOGISTICSLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .execute(callback);
    }

    /**
     * 快递发货
     *
     * @param orderid
     * @param shippingid
     * @param shippingcode
     * @param callback
     */
    public static void OrderSendOrder(String orderid, String shippingid, String shippingcode, HttpCallback callback) {
        HttpClient.getInstance().get("Order.sendOrder", HttpConsts.ORDERSENDORDER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("order_id", orderid)
                .params("shipping_id", shippingid)
                .params("shipping_code", shippingcode)
                .execute(callback);
    }

    /**
     * 订单删除、取消
     *
     * @param orderid
     * @param type     订单操作状态(1取消;2删除)
     * @param callback
     */
    public static void OrderDelOrder(String orderid, String type, HttpCallback callback) {
        HttpClient.getInstance().get("Order.DelOrder", HttpConsts.ORDERDELRODER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("order_id", orderid)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 确认收货
     *
     * @param orderid
     * @param callback
     */
    public static void OrderTakeOrder(String orderid, HttpCallback callback) {
        HttpClient.getInstance().get("Order.takeOrder", HttpConsts.ORDERTAKEORDER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("order_id", orderid)
                .execute(callback);
    }


    /**
     * 查看物流
     *
     * @param orderid
     * @param callback
     */
    public static void OrderLogisicsDetail(String orderid, HttpCallback callback) {
        HttpClient.getInstance().get("order.logisticsDetail", HttpConsts.ORDERLOGISTICSDETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("order_id", orderid)
                .execute(callback);
    }

    /**
     * 商品评论列表
     *
     * @param page
     * @param goodsid
     * @param callback
     */
    public static void OrderGetEvalList(String page, String goodsid, HttpCallback callback) {
        HttpClient.getInstance().get("Order.getOrderEvalList", HttpConsts.ORDEREVALIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_id", goodsid)
                .params("p", page)
                .execute(callback);
    }

    /**
     * 视频打赏
     *
     * @param recvid   接收人ID
     * @param video_id 短视频编号
     * @param coin     钻石
     * @param callback
     */
    public static void RedSendReward(String recvid, String video_id, String coin, HttpCallback callback) {
        HttpClient.getInstance().get("red.sendReward", HttpConsts.REDSENDREWARD)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("recvid", recvid)
                .params("video_id", video_id)
                .params("coin", coin)
                .execute(callback);
    }


    /**
     * 我的团队列表
     *
     * @param type
     * @param callback
     */
    public static void myTeamList(String page, String type, HttpCallback callback) {
        HttpClient.getInstance().get("", "")
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 代理权限
     *
     * @param callback
     */
    public static void myTeamAgentLevel(HttpCallback callback) {
        HttpClient.getInstance().get("", "")
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 乐豆列表
     *
     * @param page
     * @param callback
     */
    public static void LedouList(String page, HttpCallback callback) {
        HttpClient.getInstance().get("", "")
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .execute(callback);
    }

    /**
     * 福豆列表
     *
     * @param page
     * @param callback
     */
    public static void FudouList(String page, HttpCallback callback) {
        HttpClient.getInstance().get("", "")
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .execute(callback);
    }


    /**
     * 更多分类
     *
     * @param callback
     */
    public static void MoreCategory(HttpCallback callback) {
        HttpClient.getInstance().get("Home.getCategory", HttpConsts.SHOPMALLHOMEGETCATEGROY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }


    //---------------狐音新功能接口

    /**
     * 发现首页
     *
     * @param callback
     */
    public static void getFindIndex(HttpCallback callback) {
//        HttpClient.getInstance().get("Profit.index", HttpConsts.PROFITINDEX)
//                .params("uid", AppConfig.getInstance().getUid())
//                .params("token", AppConfig.getInstance().getToken())
//                .execute(callback);
    }

    /**
     * 新闻列表
     *
     * @param page
     * @param callback
     */
    public static void getFindNewList(int page, HttpCallback callback) {
        HttpClient.getInstance().get("Profit.getNewList", HttpConsts.PROFITGETNEWLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .execute(callback);
    }

    /**
     * 乐器中心
     *
     * @param callback
     */
    public static void getMusicCenterList(HttpCallback callback) {
//        HttpClient.getInstance().get("Profit.getList", HttpConsts.PROFITGETLIST)
//                .params("uid", AppConfig.getInstance().getUid())
//                .params("token", AppConfig.getInstance().getToken())
//                .execute(callback);
    }

    /**
     * 购买乐器
     *
     * @param musical_id
     * @param callback
     */
    public static void getMusicBuy(String musical_id, HttpCallback callback) {
        HttpClient.getInstance().get("Profit.buy", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("musical_id", musical_id)
                .execute(callback);
    }
    /**
     * 获取通讯录
     *
     * @param callback
     */
    public static void getTxl(int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.GetMyFriend", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }
    /**
     * 获取通讯录
     *
     * @param callback
     */
    public static void getHmdList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getBlackList", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid",AppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 家人列表
     *
     * @param callback
     */
    public static void getFamilyList(int p,String key, HttpCallback callback) {
        HttpClient.getInstance().get("User.getFamilyList", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
//                .params("key", key)
                .execute(callback);
    }
    /**
     *  我的二维码
     *
     * @param callback
     */
    public static void getCard( HttpCallback callback) {
        HttpClient.getInstance().get("User.getQRcode", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }
    public static void setGz( String touid,HttpCallback callback) {
        HttpClient.getInstance().get("User.SetAttent", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .execute(callback);
    }
    public static void removeGz( String touid,HttpCallback callback) {
        HttpClient.getInstance().get("User.removeFans", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("touid", touid)
                .execute(callback);
    }
    public static void getAds( String type,HttpCallback callback) {
        HttpClient.getInstance().get("Home.getAds", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("type", type)
                .execute(callback);
    }
    /**
     * 发现好友
     *
     * @param callback
     */
    public static void getFriendsList(int p,String key, HttpCallback callback) {
        HttpClient.getInstance().get("User.getFindFriend", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .params("key", key)
                .execute(callback);
    }
    /**
     * 喜欢列表
     *
     * @param callback
     */
    public static void getXhList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getMyLike", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }
    /**
     * 评论列表
     *
     * @param callback
     */
    public static void getPingLunList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getMyComments", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }
    /**
     * 评论列表
     *
     * @param callback
     */
    public static void getGoodsList(String store_id,int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getGoodsList", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
//                .params("store_id", store_id)
                .params("p", p)
                .execute(callback);
    }
    /**
     * @我的
     *
     * @param callback
     */
    public static void getMyL(int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getAtMe", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }
    /**
     * 我的优惠券
     *
     * @param callback
     */
    public static void getYhq(int p,String type, HttpCallback callback) {
        HttpClient.getInstance().get("User.getMyCoupons", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", p)
                .params("type", type)
                .execute(callback);
    }
    /**
     *  收藏列表（购物助手）
     *
     * @param callback
     */
    public static void getSave(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.getCollectGoods", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }
    /**
     *  直播服务
     *
     * @param callback
     */
    public static void getService(HttpCallback callback) {
        HttpClient.getInstance().get("User.getLiveService", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }
    /**
     * 积分商城
     *
     * @param callback
     */
    public static void getScoreHome(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.getGoodslist", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }
    /**
     * 商城首页
     *
     * @param callback
     */
    public static void getShopHome( HttpCallback callback) {
        HttpClient.getInstance().get("Mall.buyTime", HttpConsts.PROFITBUY)
//                .params("uid", AppConfig.getInstance().getUid())
//                .params("p", p)
                .execute(callback);
    }
    /**
     *  我的钱包
     *
     * @param callback
     */
    public static void getMyQb( HttpCallback callback) {
        HttpClient.getInstance().get("User.getBalance", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }
    /**
     * 店铺列表
     *
     * @param callback
     */
    public static void getShopsList(String key,String p,HttpCallback callback) {
        HttpClient.getInstance().get("Home.getStore", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("key", key)
                .params("p", p)
                .execute(callback);
    }
    /**
     * 问医
     *
     * @param callback
     */
    public static void getWy(String p,String type,String catid,HttpCallback callback) {
        HttpClient.getInstance().get("Home.askDoctor", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("lng", AppConfig.getInstance().getLng())
                .params("lat", AppConfig.getInstance().getLat())
//                .params("key", key)
                .params("p", p)
                .params("type", type)
                .params("catid", catid)
                .execute(callback);
    }
    /**
     * 问医
     *
     * @param callback
     */
    public static void getWyGz(String p,HttpCallback callback) {
        HttpClient.getInstance().get("Home.getAskDoctorFollow", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }
    /**
     * 问医
     *
     * @param callback
     */
    public static void getMoreZb(String p,HttpCallback callback) {
        HttpClient.getInstance().get("Home.getMoreLive", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }
    /**
     * 问医
     *
     * @param callback
     */
    public static void getWyzB(String p,String type,String catid,HttpCallback callback) {
        HttpClient.getInstance().get("Home.askDoctor", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
//                .params("lng", AppConfig.getInstance().getLng())
//                .params("lat", AppConfig.getInstance().getLat())
//                .params("key", key)
                .params("p", p)
                .params("type", type)
                .params("catid", catid)
                .execute(callback);
    }
    /**
     * 问医
     *
     * @param callback
     */
    public static void getTcWy(String type, String p,HttpCallback callback) {
        HttpClient.getInstance().get("Home.GetNearby", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
//                .params("key", key)
                .params("type", type)
                .params("p", p)
                .params("lng", AppConfig.getInstance().getLng())
                .params("lat", AppConfig.getInstance().getLat())
                .execute(callback);
    }
    /**
     * 店铺列表
     *
     * @param callback
     */
    public static void getZbList(String key,String p,String catid,String typeid,HttpCallback callback) {
        HttpClient.getInstance().get("Home.getLive", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("key", key)
                .params("p", p)
                .params("catid", catid)
                .params("typeid", typeid)
                .execute(callback);
    }
    /**
     * 签到
     *
     * @param callback
     */
    public static void getQd( HttpCallback callback) {
        HttpClient.getInstance().get("User.Bonus", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }
    /**
     * 签到
     *
     * @param callback
     */
    public static void setQd( HttpCallback callback) {
        HttpClient.getInstance().get("User.getBonus", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }
    /**
     * 积分商城 下单
     *
     * @param callback
     */
    public static void setOrder(String goodsid,String goodsattr,String goodsnum, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.goOrder", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goodsid", goodsid)
                .params("goodsattr", goodsattr)
                .params("goodsnum",goodsnum)
                .execute(callback);
    }
    /**
     * 积分商城 下单
     *
     * @param callback
     */
    public static void setPayOrder(String goodsid,String goodsattr,String goodsnum, String isMs,HttpCallback callback) {
        HttpClient.getInstance().get("Order.SureOrder", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goods_id", goodsid)
                .params("goods_attr", goodsattr)
                .params("goods_num",goodsnum)
                .params("is_ms",isMs)
                .execute(callback);
    }

    /*购物车下单*/
    public static void setCartPayOrder(String cartids, HttpCallback callback) {
        HttpClient.getInstance().get("Order.SureOrder", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("cart_ids", cartids)
                .execute(callback);
    }
    /**
     * 积分商城 下单
     *
     * @param callback
     */
    public static void payOrder(String goodsid,String goodsattr,String goodsnum,String addressid,String totalprice, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.setOrder", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("goodsid", goodsid)
                .params("goodsattr", goodsattr)
                .params("goodsnum",goodsnum)
                .params("addressid",addressid)
                .params("totalprice",totalprice)
                .execute(callback);
    }
    /**
     *  确认订单 下单
     *
     * @param callback
     */
    public static void payGoodsBuy(String pay_type,String goodsList,String isCart,String coupons_id,String goodsid,String goodsattr,String goodsnum,String addressid,String totalprice,String isMs, HttpCallback callback) {
        HttpClient.getInstance().get("Order.goOrder", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
//                .params("goods_id", goodsid)
//                .params("goods_attr", goodsattr)
//                .params("goods_num",goodsnum)
                .params("address_id",addressid)
//                .params("ifcart",isCart)
//                .params("totalprice",totalprice)
//                .params("pay_type",pay_type)

                .params("coupons_id",coupons_id)
                .params("goods_list",goodsList)
                .params("is_ms",isMs)
                .execute(callback);
    }
    /**
     * 积分商城
     *
     * @param callback
     */
    public static void getScoreInfo(String goodsid, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.getGoodsdetail", HttpConsts.PROFITBUY)
                .params("goodsid", goodsid)
                .execute(callback);
    }
    /**
     * 上传通讯录
     *
     * @param callback
     */
    public static void upTxl(String lists, HttpCallback callback) {
        HttpClient.getInstance().get("User.SetFriend", HttpConsts.PROFITBUY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("lists", lists)
                .execute(callback);
    }

    /**
     * 用于用户完成任务(看视频 倒计时完了调用此接口)
     *
     * @param callback
     */
    public static void getAddTask(HttpCallback callback) {
//        HttpClient.getInstance().get("Profit.add_task", HttpConsts.PROFITADDTASK)
//                .params("uid", AppConfig.getInstance().getUid())
//                .params("token", AppConfig.getInstance().getToken())
////                .params("task_id", "1")
////                .params("type", "1")
//                .execute(callback);
    }

    /**
     * 用于用户完成任务后领取任务
     *
     * @param task_id
     * @param callback
     */
    public static void getReceiveTask(String task_id, HttpCallback callback) {
        HttpClient.getInstance().get("Profit.receive_task", HttpConsts.PROFITRECEIVETASK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("task_id", task_id)
                .execute(callback);
    }
    /**
     * 积分明细
     *
     * @param day
     * @param callback
     */
    public static void getListScore(String day, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.getIntegrallist", HttpConsts.PROFITRECEIVETASK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("day", day)
                .execute(callback);
    }
    /**
     * 充值明细
     *
     * @param day
     * @param callback
     */
    public static void getChongZhi(int p,String day, HttpCallback callback) {
        HttpClient.getInstance().get("User.getCharge", HttpConsts.PROFITRECEIVETASK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("day", day)
                .params("p", p)
                .execute(callback);
    }
    /**
     * 提现记录
     *
     * @param day
     * @param callback
     */
    public static void getListTiXian(String catid,String day,int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getCash", HttpConsts.PROFITRECEIVETASK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("catid", catid)
                .params("day", day)
                .params("p", p)
                .execute(callback);
    }
    /**
     * 订单列表
     *
     * @param type
     * @param callback
     */
    public static void getListOrder(String type,String page, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.getOrderlist", HttpConsts.PROFITRECEIVETASK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("type", type)
                .params("p", page)
                .execute(callback);
    }

    public static void getSureOrderSH(String orderId,String page, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.takeOrder", HttpConsts.PROFITRECEIVETASK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("order_id", orderId)
                .params("p", page)
                .execute(callback);
    }

    public static void getSureOrderWL(String orderId,String page, HttpCallback callback) {
        HttpClient.getInstance().get("Mall.logisticsDetail", HttpConsts.PROFITRECEIVETASK)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("order_id", orderId)
                .execute(callback);
    }
    /**
     * 我的音豆
     *
     * @param page
     * @param type
     * @param callback
     */
    public static void getYinDouFeeList(int page, String type, HttpCallback callback) {
        HttpClient.getInstance().get("Profit.getFeeList", HttpConsts.PROFITGETFEELIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 音豆转赠
     *
     * @param account
     * @param fee
     * @param callback
     */
    public static void yinDouTransfer(String account, String fee, HttpCallback callback) {
        HttpClient.getInstance().get("Profit.transfer", HttpConsts.PROFITTRANSFER)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("account", account)
                .params("fee", fee)
                .execute(callback);
    }

    /**
     * 音豆转音票
     *
     * @param fee
     * @param callback
     */
    public static void yinDouTransferVote(String fee, HttpCallback callback) {
        HttpClient.getInstance().get("Profit.transfer_vote", HttpConsts.PROFITTRANSFERVOTE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("fee", fee)
                .execute(callback);
    }

    /**
     * 获取我的音分值
     *
     * @param page
     * @param callback
     */
    public static void getProfitCentList(int page, HttpCallback callback) {
        HttpClient.getInstance().get("Profit.getCentList", HttpConsts.PROFITGETCENTLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .execute(callback);
    }

    /**
     * 我的熟练度
     *
     * @param page
     * @param callback
     */
    public static void getSkillList(int page, HttpCallback callback) {
        HttpClient.getInstance().get("Profit.getSkillList", HttpConsts.PROFITGEGETSKILLLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .execute(callback);
    }

    /**
     * 等级说明
     *
     * @param callback
     */
    public static void getUserGrade(HttpCallback callback) {
        HttpClient.getInstance().get("Profit.getUserGrade", HttpConsts.PROFITGETUSERGRADE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 等级说明
     *
     * @param callback
     */
    public static void getProfitGradeList(HttpCallback callback) {
        HttpClient.getInstance().get("Profit.getGradeList", HttpConsts.PROFITGETGRADLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 获取可以申请的城市
     *
     * @param callback
     */
    public static void getUserCity(HttpCallback callback) {
        HttpClient.getInstance().get("User.getCity", HttpConsts.USERGETCITY)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 申请区域代理
     *
     * @param type
     * @param mProvinceid
     * @param cityid
     * @param areaid
     * @param callback
     */
    public static void getAgentApply(String type, String mProvinceid, String cityid, String areaid, HttpCallback callback) {
        HttpClient.getInstance().get("User.applyAgent", HttpConsts.USERAPPLYAGENT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("type", type)
                .params("province_id", mProvinceid)
                .params("city_id", cityid)
                .params("area_id", areaid)
                .execute(callback);
    }

    /**
     * 城市选择
     * @param mProvinceid
     * @param cityid
     * @param areaid
     * @param callback
     */
    public static void setUserArea( String mProvinceid, String cityid, String areaid, HttpCallback callback) {
        HttpClient.getInstance().get("User.setUserArea", HttpConsts.USERSETUSERAREA)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("province_id", mProvinceid)
                .params("city_id", cityid)
                .params("area_id", areaid)
                .execute(callback);
    }

    /**
     * 获取俱乐部
     *
     * @param page
     * @param callback
     */
    public static void getClubList(String page, String keywords, HttpCallback callback) {
        HttpClient.getInstance().get("Club.getClubList", HttpConsts.GETCLUBLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("keywords", keywords)
                .execute(callback);
    }

    /**
     * 获取我的俱乐部详情
     */
    public static void getClubDetail(HttpCallback callback) {
        HttpClient.getInstance().get("Club.getClubDetail", HttpConsts.CLUBGETCLUBDETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 获取我的俱乐部成员
     *
     * @param club_id
     * @param keywords
     * @param callback
     */
    public static void getClubUsers(int page, String club_id, String keywords, HttpCallback callback) {
        HttpClient.getInstance().get("Club.getClubUsers", HttpConsts.CLUBGETCLUBUSERS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("club_id", club_id)
                .params("keywords", keywords)
                .execute(callback);
    }

    /**
     * 加入俱乐部
     *
     * @param club_id
     * @param callback
     */
    public static void joinClub(String club_id, HttpCallback callback) {
        HttpClient.getInstance().get("Club.joinClub", HttpConsts.CLUJOINCLUB)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("club_id", club_id)
                .execute(callback);
    }

    /**
     * 解散俱乐部
     *
     * @param callback
     */
    public static void dissClub(String club_id, HttpCallback callback) {
        HttpClient.getInstance().get("Club.dissClub", HttpConsts.CLUBDISSCLUB)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("club_id", club_id)
                .execute(callback);
    }

    /**
     * 退出俱乐部
     *
     * @param callback
     */
    public static void outClub(HttpCallback callback) {
        HttpClient.getInstance().get("Club.outClub", HttpConsts.CLUBOUTCLUB)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 踢出俱乐部
     *
     * @param tuid
     * @param callback
     */
    public static void delUserClub(String tuid, HttpCallback callback) {
        HttpClient.getInstance().get("Club.delUserClub", HttpConsts.CLUBDELUSERCLUB)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("tuid", tuid)
                .execute(callback);
    }

    /**
     * 我的团队
     *
     * @param type
     * @param callback
     */
    public static void TeamListData(int page, String type, HttpCallback callback) {
        HttpClient.getInstance().get("Team.getTeamList", HttpConsts.TEAMLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 我的团队详情列表
     *
     * @param page
     * @param tuid
     * @param callback
     */
    public static void getTeamDetail(int page, String tuid, HttpCallback callback) {
        HttpClient.getInstance().get("Team.getTeamDetail", HttpConsts.TEAMGETTEAMDETAIL)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p", page)
                .params("tuid", tuid)
                .execute(callback);
    }

    /**
     * 邀请好友
     *
     * @param callback
     */
    public static void getTeamInvite(HttpCallback callback) {
        HttpClient.getInstance().get("Team.invite", HttpConsts.TEAMINVITE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 收款绑定
     *
     * @param callback
     */
    public static void getTradeAccount(HttpCallback callback) {
        HttpClient.getInstance().get("Trade.getTradeAccount", HttpConsts.GETTRADEACCOUNT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 设置交易账户
     * @param type  类型(1支付宝,2银行卡)
     * @param name
     * @param account
     * @param mobile
     * @param file
     * @param card
     * @param callback
     */
    public static void setTradeAccount(String type, String name, String account, String mobile, String file, String card, HttpCallback callback) {
        HttpClient.getInstance().post("Trade.setTradeAccount", HttpConsts.SETTRADEACCOUNT)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("type",type)
                .params("name",name)
                .params("account",account)
                .params("mobile",mobile)
                .params("file",file)
                .params("card",card)
                .execute(callback);
    }

    /**
     * 设置交易密码
     * @param password
     * @param again_pass
     * @param callback
     */
    public static void setTradePass(String password, String again_pass, HttpCallback callback) {
        HttpClient.getInstance().get("Trade.setTradePass", HttpConsts.SETTRADEPASS)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("pass",password)
                .params("again_pass",again_pass)
                .execute(callback);
    }

    /**
     * 挂单买入
     * @param nums
     * @param price
     * @param type
     * @param trade_pass
     * @param callback
     */
    public static void addTrade(String nums, String price,String type,String trade_pass, HttpCallback callback) {
        HttpClient.getInstance().get("Trade.addTrade", HttpConsts.ADDTRADE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("nums",nums)
                .params("price",price)
                .params("type",type)
                .params("trade_pass",trade_pass)
                .execute(callback);
    }


    /**
     * 交易中心
     * @param p
     * @param callback
     */
    public static void getTradeList(int p,String keywords, HttpCallback callback) {
        HttpClient.getInstance().get("Trade.getTradeList", HttpConsts.GETTRADELIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p",p)
                .params("keywords",keywords)
                .execute(callback);
    }

    /**
     * 卖出音豆
     * @param nums
     * @param phone
     * @param trade_pass
     * @param callback
     */
    public static void buyTrade(String trade_id,String nums, String phone,String trade_pass, HttpCallback callback) {
        HttpClient.getInstance().get("Trade.buyTrade", HttpConsts.BUYTRADE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("trade_id",trade_id)
                .params("nums",nums)
                .params("mobile",phone)
                .params("trade_pass",trade_pass)
                .execute(callback);
    }

    /**
     * 取消挂单
     * @param callback
     */
    public static void cancelTrade( HttpCallback callback) {
        HttpClient.getInstance().get("Trade.cancelTrade", HttpConsts.CANCELTRADE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 交易信箱
     * @param page
     * @param type
     * @param callback
     */
    public static void TradegetList( int page,String type,HttpCallback callback) {
        HttpClient.getInstance().get("Trade.getList", HttpConsts.GETLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p",page)
                .params("type",type)
                .execute(callback);
    }

    /**
     * 申诉
     * @param trade_id
     * @param content
     * @param callback
     */
    public static void complainTrade( String trade_id,String content,HttpCallback callback) {
        HttpClient.getInstance().get("Trade.complainTrade", HttpConsts.COMPLAINTRADE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("trade_id",trade_id)
                .params("content",content)
                .execute(callback);
    }

    /**
     * 申诉列表
     * @param page
     */
    public static void getComplainList( int page,HttpCallback callback) {
        HttpClient.getInstance().get("Trade.getComplainList", HttpConsts.GETCOMPLAINLIST)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("p",page)
                .execute(callback);
    }

    /**
     * 确认收款
     * @param trade_id
     * @param callback
     */
    public static void sureTrade( String trade_id,HttpCallback callback) {
        HttpClient.getInstance().get("Trade.sureTrade", HttpConsts.SURETRADE)
                .params("uid", AppConfig.getInstance().getUid())
                .params("token", AppConfig.getInstance().getToken())
                .params("trade_id",trade_id)
                .execute(callback);
    }
}




