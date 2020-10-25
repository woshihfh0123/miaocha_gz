/*
package com.bs.xyplibs.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


import com.bs.xyplibs.R;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ShapeUtils {
    public static Context context;

    public ShapeUtils(Context context) {
        this.context = context;
    }

    //QQ第三方登陆获取信息
    public void loginQQ(GetData getData) {
        this.getData = getData;
        UMShareAPI.get(context).doOauthVerify((Activity) context, SHARE_MEDIA.QQ, authListener);

    }

    //新浪第三方登陆
    public void loginXinLang() {
        UMShareAPI.get(context).doOauthVerify((Activity) context, SHARE_MEDIA.SINA, authListener);
    }

    */
/**
     * 用于传值的实体类
     *//*

    class Bean {
        private String name;
        private String icon;
        private String sex;
        private String address;
        private String time;

        @Override
        public String toString() {
            return "Bean{" +
                    "name='" + name + '\'' +
                    ", icon='" + icon + '\'' +
                    ", sex='" + sex + '\'' +
                    ", address='" + address + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Bean() {
        }

        public Bean(String name, String icon, String sex, String address, String time) {
            this.name = name;
            this.icon = icon;
            this.sex = sex;
            this.address = address;
            this.time = time;
        }
    }

    */
/**
     * 监听接口
     *//*

    UMAuthListener authListener = new UMAuthListener() {
        */
/**
         * @desc 授权开始的回调
         * @param platform 平台名称
         *//*

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        */
/**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         *//*

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(context, "登陆成功", Toast.LENGTH_LONG).show();
            UMShareAPI.get(context).getPlatformInfo((Activity) context, SHARE_MEDIA.QQ, authListenerData);
        }

        */
/**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         *//*

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(context, "登陆失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        */
/**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         *//*

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(context, "取消登陆", Toast.LENGTH_LONG).show();
        }
    };

    */
/**
     * 传值监听接口
     *//*

    UMAuthListener authListenerData = new UMAuthListener() {
        */
/**
         * @desc 授权开始的回调
         * @param platform 平台名称
         *//*

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        */
/**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         *//*

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String name = data.get("name");
            String icon = data.get("iconurl");
            String sex = data.get("gender");
            String address = data.get("city") + data.get("province");
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
            Bean bean = new Bean(name, icon, sex, address, time);
            getData.setData(bean);
        }

        */
/**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         *//*

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        */
/**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         *//*

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };


    public interface GetData {
        void setData(Bean bean);
    }

    private GetData getData;


    //分享图片 url
    public void setBitmap(String url) {
        new ShareAction((Activity) context)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, "分享取消", Toast.LENGTH_LONG).show();
                    }
                })
                .withMedia(new UMImage(context, url))
                .open();
    }

    //分享图片 id
    public void setBitmap(int id) {
        new ShareAction((Activity) context)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, "分享取消", Toast.LENGTH_LONG).show();
                    }
                })
                .withMedia(new UMImage(context, id))
                .open();
    }

    //分享文本
    public void setText(String title) {
        new ShareAction((Activity) context)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, "分享取消", Toast.LENGTH_LONG).show();
                    }
                })
                .withText(title)
                .open();
    }

    //分享超链接
    public static void setWeb(Context mcontext,String url,String title,String content) {
        UMWeb web = new UMWeb(url);
        web.setThumb(new UMImage(mcontext, R.mipmap.share_ico));
        web.setTitle(title);
        web.setDescription(content);
        new ShareAction((Activity) mcontext)
                .setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE, SHARE_MEDIA.QQ)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
//                        Toast.makeText(mcontext, "分享成功", Toast.LENGTH_LONG).show();
                        EventBus.getDefault().post(new EventBusModel("share_ok_from_utils","ok"));
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
//                        Toast.makeText(mcontext, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
                        EventBus.getDefault().post(new EventBusModel("share_ok_from_utils","no"));
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
//                        Toast.makeText(mcontext, "分享取消", Toast.LENGTH_LONG).show();
                    }
                })
                .withMedia(web)
                .open();
    }

    //分享超链接
    public static void setWeb(Context mcontext,String url, String title, String message, String image) {
        UMWeb web = new UMWeb(url);
        if (image == null) {
            web.setThumb(new UMImage(mcontext, R.mipmap.share_ico));
        } else {
            web.setThumb(new UMImage(mcontext, image));
        }
        if (title == null) {
            web.setTitle("新闻");
        } else {
            web.setTitle(title);
        }
        if (message == null) {
            web.setDescription("这是一条新闻");
        } else {
            web.setDescription(message);
        }
        new ShareAction((Activity) mcontext)
                .setDisplayList( SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE, SHARE_MEDIA.QQ)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(mcontext, "分享成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(mcontext, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(mcontext, "分享取消", Toast.LENGTH_LONG).show();
                    }
                })
                .withMedia(web)
                .open();
    }

}
*/
