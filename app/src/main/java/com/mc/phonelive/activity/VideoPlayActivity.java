package com.mc.phonelive.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.HtmlConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.bean.VideoCommentBean;
import com.mc.phonelive.dialog.ShopShowDialogFragment;
import com.mc.phonelive.dialog.VideoInputDialogFragment;
import com.mc.phonelive.dialog.VideoRedPackSendDialogFragment;
import com.mc.phonelive.event.VideoDeleteEvent;
import com.mc.phonelive.event.VideoShareEvent;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.im.ImChatFacePagerAdapter;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.interfaces.OnFaceClickListener;
import com.mc.phonelive.mob.MobCallback;
import com.mc.phonelive.mob.MobShareUtil;
import com.mc.phonelive.mob.ShareData;
import com.mc.phonelive.utils.DateFormatUtil;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.DownloadUtil;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.ProcessResultUtil;
import com.mc.phonelive.utils.StringUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.VideoLocalUtil;
import com.mc.phonelive.utils.VideoStorge;
import com.mc.phonelive.utils.WordUtil;
import com.mc.phonelive.views.VideoCommentViewHolder;
import com.mc.phonelive.views.VideoScrollViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by cxf on 2018/11/26.
 * 视频播放
 */

public class VideoPlayActivity extends AbsActivity implements View.OnClickListener, OnFaceClickListener {


    public static void forward(String is_shopping, Context context, int position, String videoKey, int page) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("is_shopping", is_shopping);
        intent.putExtra(Constants.VIDEO_POSITION, position);
        intent.putExtra(Constants.VIDEO_KEY, videoKey);
        intent.putExtra(Constants.VIDEO_PAGE, page);
        context.startActivity(intent);
    }

    private String mVideoKey;
    private VideoScrollViewHolder mVideoScrollViewHolder;
    private ClipboardManager mClipboardManager;
    private MobShareUtil mMobShareUtil;
    private DownloadUtil mDownloadUtil;
    private Dialog mDownloadVideoDialog;
    private ProcessResultUtil mProcessResultUtil;
    private View mFaceView;//表情面板
    private int mFaceHeight;//表情面板高度
    private VideoCommentViewHolder mVideoCommentViewHolder;
    private VideoInputDialogFragment mVideoInputDialogFragment;
    private ConfigBean mConfigBean;
    private VideoBean mShareVideoBean;
    private boolean mPaused;
    private  String is_shopping;
    private static VideoBean mVideoBean;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }

    @Override
    protected void main() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        is_shopping = intent.getStringExtra("is_shopping");
        mVideoKey = intent.getStringExtra(Constants.VIDEO_KEY);
        if (TextUtils.isEmpty(mVideoKey)) {
            return;
        }
        int position = intent.getIntExtra(Constants.VIDEO_POSITION, 0);
        int page = intent.getIntExtra(Constants.VIDEO_PAGE, 1);
        mVideoScrollViewHolder = new VideoScrollViewHolder(mContext, (ViewGroup) findViewById(R.id.container), position, mVideoKey, page);
        addLifeCycleListener(mVideoScrollViewHolder.getLifeCycleListener());
        mVideoScrollViewHolder.addToParent();
        mProcessResultUtil = new ProcessResultUtil(this);
        AppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean bean) {
                mConfigBean = bean;
            }
        });
    }

    public static void setVideoBean(VideoBean videoBean){
        mVideoBean =videoBean;
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new EventBusModel("main_video_puase"));
        release();
        super.onBackPressed();
    }

    private void release() {
        HttpUtil.cancel(HttpConsts.SET_VIDEO_SHARE);
        HttpUtil.cancel(HttpConsts.VIDEO_DELETE);
        if (mDownloadVideoDialog != null && mDownloadVideoDialog.isShowing()) {
            mDownloadVideoDialog.dismiss();
        }
        if (mVideoScrollViewHolder != null) {
            mVideoScrollViewHolder.release();
        }
        if (mProcessResultUtil != null) {
            mProcessResultUtil.release();
        }
        if (mMobShareUtil != null) {
            mMobShareUtil.release();
        }
        if (mVideoCommentViewHolder != null) {
            mVideoCommentViewHolder.release();
        }
        VideoStorge.getInstance().removeDataHelper(mVideoKey);
        mDownloadVideoDialog = null;
        mVideoScrollViewHolder = null;
        mProcessResultUtil = null;
        mMobShareUtil = null;
        mVideoCommentViewHolder = null;
        mVideoInputDialogFragment = null;
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
        L.e("VideoPlayActivity------->onDestroy");
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
                    if (mVideoScrollViewHolder != null) {
                        mVideoScrollViewHolder.deleteVideo(videoBean);
                        EventBus.getDefault().post(new VideoDeleteEvent(videoBean.getId()));
                    }
                }
            }
        });
    }

    /**
     * 显示商品列表数据
     * @param mVideoBean
     */
    public void showshopWindow(VideoBean mVideoBean){
        ShopShowDialogFragment fragment = new ShopShowDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LIVE_UID, mVideoBean.getUid());
        fragment.setArguments(bundle);
        fragment.show(((VideoPlayActivity) mContext).getSupportFragmentManager(), "ShopShowDialogFragment");

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
        VideoInputDialogFragment fragment = new VideoInputDialogFragment();
        fragment.setVideoBean(videoBean);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.VIDEO_FACE_OPEN, openFace);
        bundle.putInt(Constants.VIDEO_FACE_HEIGHT, mFaceHeight);
        bundle.putParcelable(Constants.VIDEO_COMMENT_BEAN, bean);
        fragment.setArguments(bundle);
        mVideoInputDialogFragment = fragment;
        fragment.show(getSupportFragmentManager(), "VideoInputDialogFragment");
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
            mVideoCommentViewHolder = new VideoCommentViewHolder(mContext, (ViewGroup) findViewById(R.id.root));
            mVideoCommentViewHolder.addToParent();
        }
        mVideoCommentViewHolder.setVideoBean(videoBean);
        mVideoCommentViewHolder.showBottom();
    }


    /**
     * 打开发红包的弹窗
     */
    public void openRedPackSendWindow() {
        VideoRedPackSendDialogFragment fragment = new VideoRedPackSendDialogFragment();
        fragment.setStream("sss");
        fragment.setUserMsg(mVideoBean);
        fragment.show(getSupportFragmentManager(), "VideoRedPackSendDialogFragment");
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


    @Override
    protected void onPause() {
        EventBus.getDefault().post(new EventBusModel("main_video_puase"));
        mPaused = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().post(new EventBusModel("main_video_play_01"));
        super.onResume();
        mPaused = false;
    }

    public boolean isPaused() {
        return mPaused;
    }
}
