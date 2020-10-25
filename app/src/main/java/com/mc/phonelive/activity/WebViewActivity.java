package com.mc.phonelive.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.pay.PayResult;
import com.mc.phonelive.custom.ActionSheetDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.httpnet.PayCallback;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.PayUtis;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by cxf on 2018/9/25.
 */

public class WebViewActivity extends AbsActivity {

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private final int CHOOSE = 100;//Android 5.0以下的
    private final int CHOOSE_ANDROID_5 = 200;//Android 5.0以上的
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mValueCallback2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void main() {
        SpUtil.getInstance().setBooleanValue("setMe",false);
        String url = getIntent().getStringExtra(Constants.URL);
        L.e("H5--->" + url);
//        url=url+"?uid="+ AppConfig.getInstance().getUid();
        Log.e("URL:",url+"");
        LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);
        mWebView = new WebView(mContext);
        mWebView.setBackgroundColor(mContext.getResources().getColor(R.color.global));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        rootView.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.e("H5-------->" + url);
                if (url.startsWith(Constants.COPY_PREFIX)) {
                    String content = url.substring(Constants.COPY_PREFIX.length());
                    if (!TextUtils.isEmpty(content)) {
                        copy(content);
                    }
                } else {
                    if (url.startsWith("alipay://") ||url.startsWith("miaochaauth://")|| url.startsWith("wechat://") || url.startsWith("weixin://") || url.startsWith("alipays://") || url.startsWith("safepay://")) {
                        if(url.startsWith("miaochaauth://")||url.startsWith("miaochaauthstore://")){
                        String sn=url.substring(url.indexOf("=")+1);
                        ActionSheetDialog asd = new ActionSheetDialog(mContext)
                                .builder()
                                .setCancelable(true)
                                .setTitle("选择支付方式")
                                .setCanceledOnTouchOutside(true)
                                .addSheetItem("支付宝",ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        HttpUtil.GetAlPay(sn, new HttpCallback() {
                                            @Override
                                            public void onSuccess(int code, String msg, String[] info) {
                                                JSONObject obj = JSON.parseObject(info[0]);
                                                String payInfo = obj.getString("orderstring");
                                                PayUtis.zhiFuBaoPay(WebViewActivity.this, payInfo, new PayCallback() {
                                                    @Override
                                                    public void payResult(int type) {
                                                        payFinish(type);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                })
                                .addSheetItem("微信",ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        HttpUtil.GetWxPay(sn, new HttpCallback() {
                                            @Override
                                            public void onSuccess(int code, String msg, String[] info) {
                                                JSONObject obj = JSON.parseObject(info[0]);
//                        String json = obj.getString("payOrder");
//                                                JSONObject pod=obj.getJSONObject("payOrder");
                                                Gson gson = new Gson();
                                                final PayResult vo = gson.fromJson(obj.toJSONString(), PayResult.class);
                                                Log.e("PPPP:",vo.getAppid());
                                                PayUtis.weiXinPay(WebViewActivity.this, vo);
                                            }
                                        });
                                    }
                                });
                        asd.show();
                        }

//                        goAlipays(WebViewActivity.this,url);
                    } else {
                        view.loadUrl(url);
                    }
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setTitle(view.getTitle());
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView webView, boolean b, boolean b1, Message resultMsg) {
                WebView newWebView = new WebView(WebViewActivity.this);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                newWebView.setWebChromeClient(new WebChromeClient());
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        try {
                            if (url.startsWith("alipay://") ||url.startsWith("miaochaauth://")|| url.startsWith("wechat://") || url.startsWith("weixin://") || url.startsWith("alipays://") || url.startsWith("safepay://")) {
                                goAlipays(WebViewActivity.this,url);
                            } else {
                                view.loadUrl(url);
                            }
                        } catch (Exception e) {

                        }
                        //防止触发现有界面的WebChromeClient的相关回调
                        return true;
                    }
                });
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    dismissDialog();
                } else {
                    mProgressBar.setProgress(newProgress);
                    showDialog();
                }
            }

            //以下是在各个Android版本中 WebView调用文件选择器的方法
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                openImageChooserActivity(valueCallback);
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                openImageChooserActivity(valueCallback);
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback,
                                        String acceptType, String capture) {
                openImageChooserActivity(valueCallback);
            }

            // For Android >= 5.0
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                mValueCallback2 = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                startActivityForResult(intent, CHOOSE_ANDROID_5);
                return true;
            }

        });
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
//        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(false);
        webSettings.setSupportMultipleWindows(true);

        //        打开页面时， 自适应屏幕：
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放 将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDomStorageEnabled(true); //不设置此 无法加载h5
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);  //支持js调用window.open方法

        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        mWebView.loadUrl(url);
        try {
            if ((url.startsWith("http:") || url.startsWith("https:")) && !url.endsWith(".apk")) {
                if (!DataSafeUtils.isEmpty(mWebView))
                    mWebView.loadUrl(url);
            } else if (url.startsWith("alipay://")||url.startsWith("miaochaauth://") || url.startsWith("wechat://") || url.startsWith("weixin://") || url.startsWith("alipays://") || url.startsWith("safepay://")) {
                goAlipays(WebViewActivity.this,url);
            } else {
                if (!DataSafeUtils.isEmpty(getIntent().getStringExtra("title"))) {
                    setTitle(getIntent().getStringExtra("title"));
                }
                if (!DataSafeUtils.isEmpty(url))
                    mWebView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
            }
        } catch (Exception e) {
        }
    }
    private void payFinish(int type) {
        Log.e("PPP:",type+"");
        finish();

    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusModel event) {
        String code = event.getCode();
        switch (code) {
            case "payment_success":
                Log.e("tttttttttt","支付成功");
                //支付成功
                payFinish(1);
                break;
            case "payment_fail":
                Log.e("ffffffffffff","支付失败");
                //失败
                payFinish(2);
                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SpUtil.getInstance().setBooleanValue("setMe",true);
    }

    public static void goAlipays(Activity activity, String url) {
        //判断是否安装支付宝
        if (checkAliPayInstalled( activity )){
            goUrl( activity, url );
        }else {
            //安装下载支付宝
            updateAlipayDialog(activity);
        }
    }

    private static void goUrl(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
        } catch (Exception e) {

        }
    }

    //判断是否安装支付宝app
    private static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse( "alipays://platformapi/startApp" );
        Intent intent = new Intent( Intent.ACTION_VIEW, uri );
        ComponentName componentName = intent.resolveActivity( context.getPackageManager() );
        return componentName != null;
    }

    //安装alipay支付宝  弹框和文案根据实际情况定制
    private static synchronized void updateAlipayDialog(Context context) {
        Uri alipayUrl = Uri.parse("https://d.alipay.com");
        context.startActivity(new Intent(Intent.ACTION_VIEW, alipayUrl));
    }



    private void openImageChooserActivity(ValueCallback<Uri> valueCallback) {
        mValueCallback = valueCallback;
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, WordUtil.getString(R.string.choose_flie)), CHOOSE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case CHOOSE://5.0以下选择图片后的回调
                processResult(resultCode, intent);
                break;
            case CHOOSE_ANDROID_5://5.0以上选择图片后的回调
                processResultAndroid5(resultCode, intent);
                break;
        }
    }

    private void processResult(int resultCode, Intent intent) {
        if (mValueCallback == null) {
            return;
        }
        if (resultCode == RESULT_OK && intent != null) {
            Uri result = intent.getData();
            mValueCallback.onReceiveValue(result);
        } else {
            mValueCallback.onReceiveValue(null);
        }
        mValueCallback = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void processResultAndroid5(int resultCode, Intent intent) {
        if (mValueCallback2 == null) {
            return;
        }
        if (resultCode == RESULT_OK && intent != null) {
            mValueCallback2.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
        } else {
            mValueCallback2.onReceiveValue(null);
        }
        mValueCallback2 = null;
    }

    protected boolean canGoBack() {
        return mWebView != null && mWebView.canGoBack();
    }

    @Override
    public void onBackPressed() {
        if (isNeedExitActivity()) {
            finish();
        } else {
            if (canGoBack()) {
                dismissDialog();
                mWebView.goBack();
            } else {
                finish();
            }
        }
    }


    private boolean isNeedExitActivity() {
        if (mWebView != null) {
            String url = mWebView.getUrl();
            if (!TextUtils.isEmpty(url)) {
                return url.contains("g=Appapi&m=Auth&a=success")//身份认证成功页面
                        ||url.contains("g=Appapi&m=Family&a=home") ;//家族申请提交成功页面

            }
        }
        return false;
    }

    public static void forward(Context context, String url) {
        if(url.contains("?")){
            url += "&uid=" + AppConfig.getInstance().getUid() + "&token=" + AppConfig.getInstance().getToken();
        }else{
            url += "?uid=" + AppConfig.getInstance().getUid() + "&token=" + AppConfig.getInstance().getToken();
        }
        Log.e("WWWWWWWW:",url+"");

        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
        }
        super.onDestroy();
    }

    /**
     * 复制到剪贴板
     */
    private void copy(String content) {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", content);
        cm.setPrimaryClip(clipData);
        ToastUtil.show(getString(R.string.copy_success));
    }

}
