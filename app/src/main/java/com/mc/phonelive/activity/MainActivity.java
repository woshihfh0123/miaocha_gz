package com.mc.phonelive.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.tecent.common.ShortVideoDialog;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.AppContext;
import com.mc.phonelive.Constants;
import com.mc.phonelive.HtmlConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.MoneyPActivity;
import com.mc.phonelive.activity.shop.MyCardActivity;
import com.mc.phonelive.activity.shop.QianDaoActivity;
import com.mc.phonelive.activity.shop.ShopToolsActivity;
import com.mc.phonelive.activity.shop.ZbServiceActivity;
import com.mc.phonelive.adapter.ViewPagerAdapter;
import com.mc.phonelive.bean.AdsBean;
import com.mc.phonelive.bean.BonusBean;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.bean.VideoCommentBean;
import com.mc.phonelive.bean.WyZbBean;
import com.mc.phonelive.custom.TabButtonGroup;
import com.mc.phonelive.dialog.HomeAvDialog;
import com.mc.phonelive.dialog.MainStartDialogFragment;
import com.mc.phonelive.dialog.ShopShowDialogFragment;
import com.mc.phonelive.dialog.VideoHomeInputDialogFragment;
import com.mc.phonelive.dialog.VideoRedPackSendDialogFragment;
import com.mc.phonelive.event.VideoDeleteEvent;
import com.mc.phonelive.event.VideoShareEvent;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.im.ImChatFacePagerAdapter;
import com.mc.phonelive.im.ImMessageUtil;
import com.mc.phonelive.im.ImUnReadCountEvent;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.interfaces.MainAppBarLayoutListener;
import com.mc.phonelive.interfaces.MainStartChooseCallback;
import com.mc.phonelive.interfaces.OnFaceClickListener;
import com.mc.phonelive.jmrtc.AndroidUtils;
import com.mc.phonelive.jmrtc.JMRTCSendActivity;
import com.mc.phonelive.mob.MobCallback;
import com.mc.phonelive.mob.MobShareUtil;
import com.mc.phonelive.mob.ShareData;
import com.mc.phonelive.presenter.CheckLivePresenter;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DataUtils;
import com.mc.phonelive.utils.DateFormatUtil;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.DownloadUtil;
import com.mc.phonelive.utils.DpUtil;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.LiveStorge;
import com.mc.phonelive.utils.LocationUtil;
import com.mc.phonelive.utils.ProcessResultUtil;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.StringUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.VersionUtil;
import com.mc.phonelive.utils.VideoLocalUtil;
import com.mc.phonelive.utils.VideoStorge;
import com.mc.phonelive.utils.WordUtil;
import com.mc.phonelive.views.AbsMainViewHolder;
import com.mc.phonelive.views.BonusViewHolder;
import com.mc.phonelive.views.MainLiveViewHolder;
import com.mc.phonelive.views.PersonMyViewHolder;
import com.mc.phonelive.views.foxtone.AskDtViewHolder;
import com.mc.phonelive.views.foxtone.FamilyVidewHolder;
import com.mc.phonelive.views.foxtone.MainVideoViewHolder;
import com.mc.phonelive.views.foxtone.VideoCommentHomeViewHolder;
import com.tencent.qcloud.ugckit.module.upload.TCUserMgr;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.jiguang.jmrtc.api.JMRtcClient;
import cn.jiguang.jmrtc.api.JMRtcListener;
import cn.jiguang.jmrtc.api.JMRtcSession;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.model.UserInfo;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 主页面
 */
public class MainActivity extends AbsActivity implements OnFaceClickListener, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private DrawerLayout drawerLayout; //右侧侧滑菜单
    private NavigationView navigationView;  //侧滑菜单容器
    private ViewGroup mRootView;
    private TabButtonGroup mTabButtonGroup;
    private ViewPager mViewPager;
    private AbsMainViewHolder[] mViewHolders;
    private View mBottom;
    private int mDp70;
    private ProcessResultUtil mProcessResultUtil;
    private CheckLivePresenter mCheckLivePresenter;
    private boolean mLoad;
    private long mLastClickBackTime;//上次点击back键的时间
    //--------------------首页改版start----------------------------
    private ClipboardManager mClipboardManager;
    private MobShareUtil mMobShareUtil;
    private DownloadUtil mDownloadUtil;
    private Dialog mDownloadVideoDialog;
    //private ProcessResultUtil mProcessResultUtil;
    private View mFaceView;//表情面板
    private int mFaceHeight;//表情面板高度
    private VideoCommentHomeViewHolder mVideoCommentViewHolder;
    private VideoHomeInputDialogFragment mVideoInputDialogFragment;
    private ConfigBean mConfigBean;
    private VideoBean mShareVideoBean;
    private static VideoBean mVideoBean;
    public static boolean isMainVideoTab = true;//切换其他界面，然后返回来，首页几个主界面全部都刷新了，为了判断是否点击了首页，视频继续播放问题 如果在首页 true就播放，如果不在首页 false 不播放
    //--------------------首页改版end----------------------------

    // SpUtil.getInstance().setBooleanValue("setMe",false);
    //语音视频聊天
    private JMRtcSession session;//通话数据元信息对象
    public static boolean requestPermissionSended = false;
    public static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private int checkPostion=0;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    //    MusicMediaPlayerUtil mMusicMediaPlayerUtil;
    @Override
    protected void main() {
        AppConfig.getInstance().isVideoView=false;
//        if (mMusicMediaPlayerUtil != null) {
//            mMusicMediaPlayerUtil.stopPlay();
//        }
        getJMRTCData();
        JMRtcClient.getInstance().initEngine(jmRtcListener);
        boolean showInvite = getIntent().getBooleanExtra(Constants.SHOW_INVITE, false);
        mRootView = (ViewGroup) findViewById(R.id.rootView);
        mTabButtonGroup = (TabButtonGroup) findViewById(R.id.tab_group);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.activity_na);

        navigationView = findViewById(R.id.nav);
        mViewPager.setOffscreenPageLimit(6);
        mViewHolders = new AbsMainViewHolder[4];
        mViewHolders[0] = new MainVideoViewHolder(mContext, mViewPager);
//        mViewHolders[1] = new MainHomeViewHolder(mContext, mViewPager);
        mViewHolders[1] = new FamilyVidewHolder(mContext, mViewPager);
//        mViewHolders[2] = new MainLiveViewHolder(mContext, mViewPager);
        mViewHolders[2] = new AskDtViewHolder(mContext, mViewPager);
//        mViewHolders[3] = new MainMeViewHolder(mContext, mViewPager);
        mViewHolders[3] = new PersonMyViewHolder(mContext, mViewPager);
//        mViewHolders[3] = new MainShopMainViewHolder(mContext, mViewPager);
        List<View> list = new ArrayList<>();
        //初始录制弹窗
        mShortVideoDialog = new ShortVideoDialog();
        MainAppBarLayoutListener appBarLayoutListener = new MainAppBarLayoutListener() {
            @Override
            public void onOffsetChanged(float rate) {
                float curY = mBottom.getTranslationY();
                float targetY = rate * mDp70;
                if (curY != targetY) {
//                    mBottom.setTranslationY(targetY);
                }
            }
        };
        for (AbsMainViewHolder vh : mViewHolders) {
            vh.setAppBarLayoutListener(appBarLayoutListener);
            addAllLifeCycleListener(vh.getLifeCycleListenerList());
            list.add(vh.getContentView());
        }
        mViewPager.setAdapter(new ViewPagerAdapter(list));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                checkPostion=position;
                if(position==3){
                    SpUtil.getInstance().setBooleanValue("setMe",true);
                }else{
                    SpUtil.getInstance().setBooleanValue("setMe",false);
                }
                EventBusUtil.postEvent(new EventBean("clise_video_from_qh"));
                Log.v("tags","======================dddddddddddddddddddddddd======pageselect============"+position);
                for (int i = 0, length = mViewHolders.length; i < length; i++) {
                    if (i == position) { //切换到对应页面时
                        mViewHolders[i].setShowed(true);
                        if (i == 0) {
                            isMainVideoTab = true;
                            EventBus.getDefault().post(new EventBusModel("main_video_play"));//按钮切换 控制播放器播放
                        } else {
                            isMainVideoTab = false;
                        }
                    } else {
                        if (i == 0) {
                            EventBus.getDefault().post(new EventBusModel("main_video_puase"));//按钮切换 控制首页播放器暂停
                        }
                        mViewHolders[i].setShowed(false);
                    }
                }
                mViewHolders[position].loadData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabButtonGroup.setViewPager(mViewPager);
        mDp70 = DpUtil.dp2px(70);
        mBottom = findViewById(R.id.bottom);
        mProcessResultUtil = new ProcessResultUtil(this);
        EventBus.getDefault().register(this);
//        checkVersion();
        if (showInvite) {//邀请码  因为在注册的时候已经设置了 这里先隐藏掉 如果以后需要 再解封
//            showInvitationCode();
        }
//        requestBonus();
        loginIM();
        checkAds(); //是否弹出广告弹窗
        AppConfig.getInstance().setLaunched(true);

        openNotiationPermission();

        mainstartUpdate();  //获取首页设置 AppConfig.getInstance().getConfig
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SpUtil.getInstance().setBooleanValue("setMe",false);
                switch (item.getItemId()){
                    case R.id.it_ewm://二维码
                        mContext.startActivity(new Intent(mContext, MyCardActivity.class));
                        break;
                    case R.id.it_qd://签到
                        mContext.startActivity(new Intent(mContext, QianDaoActivity.class));
                        break;
                    case R.id.it_msg://我的消息
                        mContext.startActivity(new Intent(mContext, ChatActivity.class));
                        break;
                    case R.id.it_qb://钱包
                        mContext.startActivity(new Intent(mContext, MoneyPActivity.class));
                        break;
                    case R.id.it_gwzs://购物助手
                        mContext.startActivity(new Intent(mContext, ShopToolsActivity.class));
                        break;
                    case R.id.it_zbfw://直播服务
                        mContext.startActivity(new Intent(mContext, ZbServiceActivity.class));
                        break;
                    case R.id.it_set://设置
                        mContext.startActivity(new Intent(mContext, SettingActivity.class));
                        break;
                    default:
                }
                drawerLayout.closeDrawer(navigationView);

                return true;
            }
        });
//       getLayoutInflater().setFactory(new android.view.LayoutInflater.Factory(){
//
//            @Override
//            public View onCreateView(String name, Context context, AttributeSet attrs) {
//                if (name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView")) {
//                    try{
//                        LayoutInflater f = LayoutInflater.from(context);
//                        final View view = f.createView(name, null, attrs);
//                        if(view instanceof TextView) {
//                            TextView menuTv = ((TextView)view);
//                            //这里你就可以修改TextView的字体颜色，大小，背景等等.
//                            menuTv.setTextColor(getResources().getColor(R.color.white));
//                        }
//                        return view;
//                    }catch (InflateException e) {
//                        e.printStackTrace();
//                    }catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//        });
    }

    private void checkAds() {
        HttpUtil.getAds("1", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
//                    ArrayList<AdsBean> nowsBean = (ArrayList<AdsBean>) JSON.parseArray(obj.getString("goods_list"), AdsBean.class);
                    List<AdsBean> list = JSON.parseArray(Arrays.toString(info), AdsBean.class);
                    if(Utils.isNotEmpty(list)){
                        new HomeAvDialog(mContext,list.get(0).getUrl(),list.get(0).getThumb()).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppConfig.getInstance().isVideoView=false;
        // 在 onStop 时释放掉播放器
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

//====================首页改版 开始=================================

    /**
     * 首页改版
     */
    private void mainstartUpdate() {
        AppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean bean) {
                mConfigBean = bean;
            }
        });
    }


    public static void setVideoBean(VideoBean videoBean) {
        mVideoBean = videoBean;
    }


    /**
     * 复制视频链接
     */
    public void copyLink(VideoBean videoBean) {
        if (videoBean == null) {
            return;
        }
        if (mClipboardManager == null) {
            mClipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData clipData = ClipData.newPlainText("text", videoBean.getHref());
        mClipboardManager.setPrimaryClip(clipData);
        ToastUtil.show(WordUtil.getString(R.string.copy_success));
    }

    /**
     * 分享页面链接
     */
    public void shareVideoPage(String type, VideoBean videoBean) {
        if (videoBean == null || mConfigBean == null) {
            return;
        }
        mShareVideoBean = videoBean;
        ShareData data = new ShareData();
        data.setTitle(mConfigBean.getVideoShareTitle());
        data.setDes(mConfigBean.getVideoShareDes());
        data.setImgUrl(videoBean.getThumbs());
        String webUrl = HtmlConfig.SHARE_VIDEO + videoBean.getId();
        data.setWebUrl(webUrl);
        if (mMobShareUtil == null) {
            mMobShareUtil = new MobShareUtil();
        }
        mMobShareUtil.execute(type, data, mMobCallback);
    }

    private MobCallback mMobCallback = new MobCallback() {

        @Override
        public void onSuccess(Object data) {
            if (mShareVideoBean == null) {
                return;
            }
            HttpUtil.setVideoShare(mShareVideoBean.getId(), new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0 && info.length > 0 && mShareVideoBean != null) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        EventBus.getDefault().post(new VideoShareEvent(mShareVideoBean.getId(), obj.getString("shares")));
                    } else {
                        ToastUtil.show(msg);
                    }
                }
            });
        }

        @Override
        public void onError() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onFinish() {

        }
    };

    /**
     * 下载视频
     */
    public void downloadVideo(final VideoBean videoBean) {
        if (mProcessResultUtil == null || videoBean == null || TextUtils.isEmpty(videoBean.getHref())) {
            return;
        }
        mProcessResultUtil.requestPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        }, new Runnable() {
            @Override
            public void run() {
                mDownloadVideoDialog = DialogUitl.loadingDialog(mContext);
                mDownloadVideoDialog.show();
                if (mDownloadUtil == null) {
                    mDownloadUtil = new DownloadUtil();
                }
                String fileName = "YB_VIDEO_" + videoBean.getTitle() + "_" + DateFormatUtil.getCurTimeString() + ".mp4";
                mDownloadUtil.download(videoBean.getTag(), AppConfig.VIDEO_PATH, fileName, videoBean.getHref(), new DownloadUtil.Callback() {
                    @Override
                    public void onSuccess(File file) {
                        ToastUtil.show(R.string.video_download_success);
                        if (mDownloadVideoDialog != null && mDownloadVideoDialog.isShowing()) {
                            mDownloadVideoDialog.dismiss();
                        }
                        mDownloadVideoDialog = null;
                        String path = file.getAbsolutePath();
                        try {
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            mmr.setDataSource(path);
                            String d = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            if (StringUtil.isInt(d)) {
                                long duration = Long.parseLong(d);
                                VideoLocalUtil.saveVideoInfo(mContext, path, duration);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(R.string.video_download_failed);
                        if (mDownloadVideoDialog != null && mDownloadVideoDialog.isShowing()) {
                            mDownloadVideoDialog.dismiss();
                        }
                        mDownloadVideoDialog = null;
                    }
                });
            }
        });
    }

    /**
     * 删除视频
     */
    public void deleteVideo(final VideoBean videoBean) {
        HttpUtil.videoDelete(videoBean.getId(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
//                    if (mVideoScrollViewHolder != null) {
//                        mVideoScrollViewHolder.deleteVideo(videoBean);
                    EventBus.getDefault().post(new VideoDeleteEvent(videoBean.getId()));
//                    }
                }
            }
        });
    }

    /**
     * 显示商品列表数据
     *
     * @param mVideoBean
     */
    public void showshopWindow(VideoBean mVideoBean) {
        ShopShowDialogFragment fragment = new ShopShowDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LIVE_UID, mVideoBean.getUid());
        fragment.setArguments(bundle);
        fragment.show(((MainActivity) mContext).getSupportFragmentManager(), "ShopShowDialogFragment");

    }

    /**
     * 初始化表情控件
     */
    private View initFaceView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.view_chat_face, null);
        v.measure(0, 0);
        mFaceHeight = v.getMeasuredHeight();
        v.findViewById(R.id.btn_send).setOnClickListener(this);
        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(10);
        ImChatFacePagerAdapter adapter = new ImChatFacePagerAdapter(mContext, this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0, pageCount = adapter.getCount(); i < pageCount; i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.view_chat_indicator, radioGroup, false);
            radioButton.setId(i + 10000);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send://发表评论
                Log.e("pppppp","aaaa");
                if (mVideoInputDialogFragment != null) {
                    mVideoInputDialogFragment.sendComment();
                }
                break;
        }
    }

    @Override
    public void onFaceClick(String str, int faceImageRes) {
        if (mVideoInputDialogFragment != null) {
            mVideoInputDialogFragment.onFaceClick(str, faceImageRes);
        }
    }

    @Override
    public void onFaceDeleteClick() {
        if (mVideoInputDialogFragment != null) {
            mVideoInputDialogFragment.onFaceDeleteClick();
        }
    }

    /**
     * 打开评论输入框
     */
    public void openCommentInputWindow(boolean openFace, VideoBean videoBean, VideoCommentBean bean) {
        if (mFaceView == null) {
            mFaceView = initFaceView();
        }
        VideoHomeInputDialogFragment fragment = new VideoHomeInputDialogFragment();
        fragment.setVideoBean(videoBean);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.VIDEO_FACE_OPEN, openFace);
        bundle.putInt(Constants.VIDEO_FACE_HEIGHT, mFaceHeight);
        bundle.putParcelable(Constants.VIDEO_COMMENT_BEAN, bean);
        fragment.setArguments(bundle);
        mVideoInputDialogFragment = fragment;
        fragment.show(((MainActivity) mContext).getSupportFragmentManager(), "VideoInputDialogFragment");
    }


    public View getFaceView() {
        if (mFaceView == null) {
            mFaceView = initFaceView();
        }
        return mFaceView;
    }

    /**
     * 显示评论
     */
    public void openCommentWindow(VideoBean videoBean) {
        if (mVideoCommentViewHolder == null) {
            mVideoCommentViewHolder = new VideoCommentHomeViewHolder(mContext, (ViewGroup) findViewById(R.id.rootView));
            mVideoCommentViewHolder.addToParent();
        }
        mVideoCommentViewHolder.setVideoBean(videoBean);
        mVideoCommentViewHolder.showBottom();
    }


    /**
     * 打开发红包的弹窗
     */
    public void openRedPackSendWindow() {
        if (MainActivity.mVideoBean == null) {
            ToastUtil.show("视频为空");
            return;
        }
        VideoRedPackSendDialogFragment fragment = new VideoRedPackSendDialogFragment();
        fragment.setStream("sss");
        fragment.setUserMsg(MainActivity.mVideoBean);
        fragment.show(((MainActivity) mContext).getSupportFragmentManager(), "VideoRedPackSendDialogFragment");
    }

    /**
     * 隐藏评论
     */
    public void hideCommentWindow() {
        if (mVideoCommentViewHolder != null) {
            mVideoCommentViewHolder.hideBottom();
        }
        mVideoInputDialogFragment = null;
    }

//====================首页改版 结束=================================

    /**
     * 打开通知权限
     */
    private void openNotiationPermission() {
        NotificationManagerCompat notification = NotificationManagerCompat.from(this);
        boolean isEnabled = notification.areNotificationsEnabled();
        Log.v("tags", isEnabled + "----");
        if (!isEnabled) {
            //未打开通知
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请在“通知”中打开通知权限")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("android.provider.extra.APP_PACKAGE", MainActivity.this.getPackageName());
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", MainActivity.this.getPackageName());
                                intent.putExtra("app_uid", MainActivity.this.getApplicationInfo().uid);
                                startActivity(intent);
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                            } else if (Build.VERSION.SDK_INT >= 15) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                            }
                            startActivity(intent);

                        }
                    })
                    .create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        }
    }
    private void getBaseUserInfo() {
        HttpUtil.getBaseInfo(new CommonCallback<UserBean>() {
            @Override
            public void callback(UserBean bean) {
                String usb=bean.getIs_live_auth();
                Log.e("ccccc:",usb+"");
                if(usb.equals("1")){
                    //是主播
                }else {

                }

            }
        });
    }
    public void mainClick(View v) {
        if (!canClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_start://发布拍摄666666

//                showStartDialog();//老式弹窗选择
//                Intent intent=new Intent(mContext,MainPsAndZb.class);
//                startActivity(intent);
  /*              SpUtil.getInstance().setBooleanValue("setMe",false);
                PermissionRequest();*/

//                startActivity(new Intent(mContext,VideoRecordActivity.class));

                showSelect();



                break;
            case R.id.btn_search:
                SearchActivity.forward(mContext);
                break;
            case R.id.btn_zb:
//                ToastUtil.show("随机直播");
                getList();
//                SearchActivity.forward(mContext);

                break;
            case R.id.btn_msg:

                break;
            case R.id.btn_phone:
                ToastUtil.show("电话号码呢?");
                break;
        }
    }
    private void getList() {//String p,String type,String catid
        HttpUtil.getMoreZb("1",new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<WyZbBean> nlist = (ArrayList<WyZbBean>) JSON.parseArray(obj.getString("list"), WyZbBean.class);
                    if(Utils.isNotEmpty(nlist)){
                        LiveBean liveBean=new LiveBean();//LiveBean
                        liveBean.setUid(nlist.get(0).getUid());
                        liveBean.setStream(nlist.get(0).getStream());
                        liveBean.setThumb(nlist.get(0).getThumb());
                        liveBean.setPull(nlist.get(0).getPull());
                        liveBean.setUserNiceName(nlist.get(0).getUser_nicename());
                        liveBean.setIs_shopping(nlist.get(0).getIs_shopping());
                        liveBean.setAvatar(nlist.get(0).getAvatar());
                        watchLive(liveBean, Constants.LIVE_FOLLOW,0);
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }
    private void showStartDialog() {
        MainStartDialogFragment dialogFragment = new MainStartDialogFragment();
        dialogFragment.setMainStartChooseCallback(mMainStartChooseCallback);
        dialogFragment.show(getSupportFragmentManager(), "MainStartDialogFragment");

    }

    private MainStartChooseCallback mMainStartChooseCallback = new MainStartChooseCallback() {
        @Override
        public void onLiveClick() {
            mProcessResultUtil.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            }, mStartLiveRunnable);
        }

        @Override
        public void onVideoClick() {
            mProcessResultUtil.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            }, mStartVideoRunnable);
        }
    };

    private Runnable mStartLiveRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(mContext, LiveAnchorActivity.class));
        }
    };


    private Runnable mStartVideoRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(mContext, VideoRecordActivity.class));
        }
    };


    /**
     * 检查版本更新
     */
    private void checkVersion() {
        AppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean configBean) {
                if (configBean != null) {
                    if (configBean.getMaintainSwitch() == 1) {//开启维护
                        DialogUitl.showSimpleTipDialog(mContext, WordUtil.getString(R.string.main_maintain_notice), configBean.getMaintainTips());
                    }
                    Log.v("ttt",configBean.getVersion()+"----version");
                    if (!VersionUtil.isLatest(configBean.getVersion())) {
                        VersionUtil.showDialog(mContext, configBean.getUpdateDes(), configBean.getDownloadApkUrl());
                    }
                }
            }
        });
    }

    /**
     * 填写邀请码
     */
    private void showInvitationCode() {
        DialogUitl.showSimpleInputDialog(mContext, WordUtil.getString(R.string.main_input_invatation_code), new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(final Dialog dialog, final String content) {
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.show(R.string.main_input_invatation_code);
                    return;
                }
                HttpUtil.setDistribut(content, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            ToastUtil.show(JSON.parseObject(info[0]).getString("msg"));
                            dialog.dismiss();
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                });
            }
        });
    }

    /**
     * 签到奖励
     */
    private void requestBonus() {
        HttpUtil.requestBonus(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    if (obj.getIntValue("bonus_switch") == 0) {
                        return;
                    }
                    int day = obj.getIntValue("bonus_day");
                    if (day <= 0) {
                        return;
                    }
                    List<BonusBean> list = JSON.parseArray(obj.getString("bonus_list"), BonusBean.class);
                    BonusViewHolder bonusViewHolder = new BonusViewHolder(mContext, mRootView);
                    bonusViewHolder.setData(list, day, obj.getString("count_day"));
                    bonusViewHolder.show();
                }
            }
        });
    }

    /**
     * 登录IM
     */
    private void loginIM() {
        String uid = AppConfig.getInstance().getUid();
        ImMessageUtil.getInstance().loginJMessage(uid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkPostion==3){
            SpUtil.getInstance().setBooleanValue("setMe",true);
        }else{
            SpUtil.getInstance().setBooleanValue("setMe",false);
        }
        EventBusUtil.postEvent(new EventBean("refresh_info_person"));
        Log.v("tags", "------------dd----------mload=" + mLoad);
//        if (!mLoad) {
//            mLoad = true;
//            AskDorctorHolder mvh = (AskDorctorHolder) mViewHolders[2];
//            if (ImPushUtil.getInstance().isClickNotification()) {//MainActivity是点击通知打开的
//                ImPushUtil.getInstance().setClickNotification(false);
//                int notificationType = ImPushUtil.getInstance().getNotificationType();
//                switch (notificationType) {
//                    case Constants.JPUSH_TYPE_LIVE:
//                        mvh.setCurrentPage(1);
//                        break;
//                    case Constants.JPUSH_TYPE_MESSAGE:
//                        mvh.setCurrentPage(1);
//                        ChatActivity.forward(mContext);
//                        break;
//                }
//            } else {
//                mvh.setCurrentPage(1);
//            }
//            getLocation();
//        }

        SerializableJMRTCVideAndVoice();//初始化音视频权限
    }

    /**
     * 获取所在位置
     */
    private void getLocation() {
        mProcessResultUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new Runnable() {
            @Override
            public void run() {
                LocationUtil.getInstance().startLocation(MainActivity.this);
            }
        });
    }
    @Subscribe
    public void getEvent(EventBean event){
        Log.e("EEEEEEEEEEE:",event.getCode());

        switch(event.getCode()){
            case "tozb":
                SpUtil.getInstance().setBooleanValue("setMe",false);
                PermissionRequest();
                break;
            case "SHOW_MENU_FROM_CLICK":
                EventBusUtil.postEvent(new EventBean("clise_video_from_qh"));
                if (drawerLayout.isDrawerOpen(navigationView)){
                    drawerLayout.closeDrawer(navigationView);
                }else{
                    drawerLayout.openDrawer(navigationView);
                }

                break;
            case"clise_video_from_qh":
                NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                break;
            case"show_jia_ren_from_click":
                mTabButtonGroup.checkedPositionTab(1);
                mViewPager.setCurrentItem(1);

                break;
            case "send_info_from_click_view":
                Log.e("WWWWW","6666");

                break;
            default:

                break;
        }
    };

    @Override
    protected void onDestroy() {
        if (mTabButtonGroup != null) {
            mTabButtonGroup.cancelAnim();
        }
        EventBus.getDefault().unregister(this);
        HttpUtil.cancel(HttpConsts.GET_CONFIG);
        HttpUtil.cancel(HttpConsts.REQUEST_BONUS);
        HttpUtil.cancel(HttpConsts.GET_BONUS);
        HttpUtil.cancel(HttpConsts.SET_DISTRIBUT);
        if (mCheckLivePresenter != null) {
            mCheckLivePresenter.cancel();
        }
        LocationUtil.getInstance().stopLocation();
        if (mProcessResultUtil != null) {
            mProcessResultUtil.release();
        }
        AppConfig.getInstance().setGiftList(null);
        AppConfig.getInstance().setLaunched(false);
        LiveStorge.getInstance().clear();
        VideoStorge.getInstance().clear();
        super.onDestroy();
    }

    public static void forward(Context context) {
        forward(context, false);
    }

    public static void forward(Context context, boolean showInvite) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constants.SHOW_INVITE, showInvite);
        context.startActivity(intent);
    }

    /**
     * 观看直播
     */
    public void watchLive(LiveBean liveBean, String key, int position) {
        if (mCheckLivePresenter == null) {
            mCheckLivePresenter = new CheckLivePresenter(mContext);
        }
        mCheckLivePresenter.watchLive(liveBean, key, position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImUnReadCountEvent(ImUnReadCountEvent e) {
        String unReadCount = e.getUnReadCount();
        if (!TextUtils.isEmpty(unReadCount)) {
            try{
                ((MainLiveViewHolder) mViewHolders[0]).setUnReadCount(unReadCount);
                ((MainLiveViewHolder) mViewHolders[1]).setUnReadCount(unReadCount);
                ((MainLiveViewHolder) mViewHolders[2]).setUnReadCount(unReadCount);
            }catch (Exception e1){

            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void TabCheckedEvent(EventBusModel model){
        switch (model.getCode()){
            case "startIndex":
                Log.v("tags","startindex");
                mViewPager.setCurrentItem(0);
                mTabButtonGroup.checkedPositionTab(0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        long curTime = System.currentTimeMillis();
        if (curTime - mLastClickBackTime > 2000) {
            mLastClickBackTime = curTime;
            ToastUtil.show(R.string.main_click_next_exit);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 初始化视频音频通话
     */

    private void SerializableJMRTCVideAndVoice() {
        if (requestPermissionSended) {
            if (AndroidUtils.checkPermission(this, REQUIRED_PERMISSIONS)) {
                JMRtcClient.getInstance().reinitEngine();
            } else {
//                Toast.makeText(this, "缺少必要权限，音视频引擎初始化失败，请在设置中打开对应权限", Toast.LENGTH_LONG).show();
            }
        }
        requestPermissionSended = false;
    }

    private void getJMRTCData() {
        jmRtcListener = new JMRtcListener() {
            @Override
            public void onEngineInitComplete(int i, String s) {
                super.onEngineInitComplete(i, s);
                //引擎初始化
                Log.v("tags", "音视频引擎初始化完成");
            }

            @Override
            public void onCallOutgoing(JMRtcSession jmRtcSession) {
                super.onCallOutgoing(jmRtcSession);
                //通话已播出
                session = jmRtcSession;
                Log.v("tags", "发起通话邀请==session=" + jmRtcSession);
            }

            @Override
            public void onCallInviteReceived(JMRtcSession jmRtcSession) {
                super.onCallInviteReceived(jmRtcSession);
                //收到通话邀请
                session = jmRtcSession;
                session.getInviterUserInfo(new RequestCallback<UserInfo>() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        Log.v("tags", "收到通话邀请--user=" + userInfo.getUserName());
//                        if (!ButtonUtils.isFastDoubleClick())
                        onCallInviteReceiveds(AppContext.sInstance, session, userInfo.getUserName());
                    }
                });
            }

            @Override
            public void onCallConnected(JMRtcSession jmRtcSession, SurfaceView localSurfaceView) {
                super.onCallConnected(jmRtcSession, localSurfaceView);
                session = jmRtcSession;
                Log.v("tags", "通话连接已建立--session=" + jmRtcSession);
                //通话连接已建立
//                if (!ButtonUtils.isFastDoubleClick())
                EventBus.getDefault().post(new EventBusModel("connected", jmRtcSession, localSurfaceView));
            }

            @Override
            public void onCallMemberJoin(UserInfo userInfo, SurfaceView surfaceView) {
                super.onCallMemberJoin(userInfo, surfaceView);
                Log.v("tags", "有其他人加入通话--userInfo=" + userInfo);
//                if (!ButtonUtils.isFastDoubleClick())
                EventBus.getDefault().post(new EventBusModel("MemberJoin", userInfo, surfaceView, session));
            }

            @Override
            public void onCallMemberOffline(UserInfo userInfo, JMRtcClient.DisconnectReason reason) {
                super.onCallMemberOffline(userInfo, reason);
                Log.v("tags", "有人退出通话--userInfo=" + userInfo);
//                if (!ButtonUtils.isFastDoubleClick())
                EventBus.getDefault().post(new EventBusModel("MemberOffline", userInfo, reason));
                session = null;
            }

            @Override
            public void onCallDisconnected(JMRtcClient.DisconnectReason disconnectReason) {
                super.onCallDisconnected(disconnectReason);
                //本地通话连接断开
                Log.v("tags", "本地通话连接断开--disconnectReason=" + disconnectReason);
//                if (!ButtonUtils.isFastDoubleClick())
                EventBus.getDefault().post(new EventBusModel("Disconnected", disconnectReason));
                session = null;
                ;
            }

            @Override
            public void onPermissionNotGranted(String[] requiredPermissions) {
                super.onPermissionNotGranted(requiredPermissions);
                try {
                    AndroidUtils.requestPermission(MainActivity.this, requiredPermissions);
                    requestPermissionSended = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                session = null;
            }

            @Override
            public void onCallError(int i, String s) {
                super.onCallError(i, s);
                //通话发生错误
                session = null;
                Log.v("tags", "通话发生错误--S=" + s + "----i=" + i);
            }
        };
    }

    /**
     * 获取到请求通话
     */
    public static void onCallInviteReceiveds(Context context, JMRtcSession session, String userName) {
        Log.v("tags", !DataUtils.isForeground(JMRTCSendActivity.mJMRTCSendActivity) + "---");
        if (!DataUtils.isForeground(JMRTCSendActivity.mJMRTCSendActivity) || JMRTCSendActivity.mJMRTCSendActivity == null) {
            Intent intent = new Intent(context, JMRTCSendActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putString("session", session.toString());
            bundle.putString("userName", userName);
            bundle.putString("index", "1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }

    @Override
    protected void onDestroy01() {
        JMRtcClient.getInstance().releaseEngine();
        EventBus.getDefault().post(new EventBusModel("main_video_stop"));//控制播放器播放
    }
    @AfterPermissionGranted(1323)
    public  void PermissionRequest(){
        String[] perms = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 已经申请过权限，做想做的事
//            if (mMusicMediaPlayerUtil != null) {
//                mMusicMediaPlayerUtil.stopPlay();
//            }
            startActivity(new Intent(mContext,ZbAndPsActivity.class));
        } else {
            // 没有申请过权限，现在去申请
//            EasyPermissions.requestPermissions(this, "系统访问受限，点击开启相关权限",1323, perms);
            ActivityCompat.requestPermissions(this, perms,
                    1323);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private long mLastClickPubTS = 0;
    private ShortVideoDialog mShortVideoDialog; //底部添加按钮菜单

    private void showSelect() {
//        if (!TCUserMgr.getInstance().hasUser()) {
//            Intent intent = new Intent(TCMainActivity.this, TCLoginActivity.class);
//            startActivity(intent);
//        } else {
        // 防止多次点击
        if (System.currentTimeMillis() - mLastClickPubTS > 1000) {
            mLastClickPubTS = System.currentTimeMillis();
            if (mShortVideoDialog.isAdded()) {
                mShortVideoDialog.dismiss();
            } else {
                mShortVideoDialog.show(getFragmentManager(), "");
            }
        }
//        }
    }

}
