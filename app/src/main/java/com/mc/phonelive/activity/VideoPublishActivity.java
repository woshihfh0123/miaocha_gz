package com.mc.phonelive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.HtmlConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.VideoPubShareAdapter;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.VideoClassBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.mob.MobShareUtil;
import com.mc.phonelive.mob.ShareData;
import com.mc.phonelive.upload.VideoUploadBean;
import com.mc.phonelive.upload.VideoUploadCallback;
import com.mc.phonelive.upload.VideoUploadQnImpl;
import com.mc.phonelive.upload.VideoUploadStrategy;
import com.mc.phonelive.upload.VideoUploadTxImpl;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by cxf on 2018/12/10.
 * 视频发布
 */

public class VideoPublishActivity extends AbsActivity implements ITXLivePlayListener, View.OnClickListener {


    public static void forward(Context context, String videoPath, int saveType) {
        Intent intent = new Intent(context, VideoPublishActivity.class);
        intent.putExtra(Constants.VIDEO_PATH, videoPath);
        intent.putExtra(Constants.VIDEO_SAVE_TYPE, saveType);
        context.startActivity(intent);
    }

    private static final String TAG = "VideoPublishActivity";
    private TextView mNum;
    private TextView mLocation;
    private TextView shop_open;
    private TXCloudVideoView mTXCloudVideoView;
    private TXLivePlayer mPlayer;
    private String mVideoPath;
    private boolean mPlayStarted;//播放是否开始了
    private boolean mPaused;//生命周期暂停
    private RecyclerView mRecyclerView;
    private ConfigBean mConfigBean;
    private VideoPubShareAdapter mAdapter;
    private VideoUploadStrategy mUploadStrategy;
    private EditText mInput;
    private String mVideoTitle;//视频标题
    private TextView mTv_link;//
    private Switch mSwitch_link;//

    private RelativeLayout choise_type;
    private TextView mChoseTypeName;

    private Dialog mLoading;
    private MobShareUtil mMobShareUtil;
    private int mSaveType;
    private String mTypeId="1";
    private String  is_shopping="0";
    private boolean isShopping=false;//是否开启店铺
    String[] mTypeListName;
    String[] mTypeListId ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_publish;
    }

    @Override
    protected void main() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTitle(WordUtil.getString(R.string.video_pub));
        Intent intent = getIntent();
        mVideoPath = intent.getStringExtra(Constants.VIDEO_PATH);
        mSaveType = intent.getIntExtra(Constants.VIDEO_SAVE_TYPE, Constants.VIDEO_SAVE_SAVE_AND_PUB);
        if (TextUtils.isEmpty(mVideoPath)) {
            return;
        }
        mChoseTypeName =findViewById(R.id.choise_type_name);
        findViewById(R.id.choise_type).setOnClickListener(this);
        findViewById(R.id.btn_pub).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        AppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean bean) {
                mConfigBean = bean;
                if (mRecyclerView != null) {
                    mAdapter = new VideoPubShareAdapter(mContext, bean);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
        mNum = (TextView) findViewById(R.id.num);
        mInput = (EditText) findViewById(R.id.input);
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNum != null) {
                    mNum.setText(s.length() + "/50");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLocation = findViewById(R.id.location);
        mLocation.setText(AppConfig.getInstance().getUserBean().getCity());
        mTXCloudVideoView = findViewById(R.id.video_view);
        mPlayer = new TXLivePlayer(mContext);
        mPlayer.setConfig(new TXLivePlayConfig());
        mPlayer.setPlayerView(mTXCloudVideoView);
        mPlayer.enableHardwareDecode(false);
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mPlayer.setPlayListener(this);
        int result = mPlayer.startPlay(mVideoPath, TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO);
        Log.v("tags",result+"----resut");
        if (result == 0) {
            mPlayStarted = true;
        }

        shop_open =findViewById(R.id.shop_open);
        String isStore =  SpUtil.getInstance()
                .getMultiStringValue(new String[]{"is_store"})[0];
        Log.v("tags",isStore+"=====");
        if (isStore.equals("1")){
            shop_open.setVisibility(View.VISIBLE);
        }else{
            shop_open.setVisibility(View.GONE);
        }
        shop_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShopping) {
                    isShopping =true;
                    is_shopping = "1";
                    shop_open.setText("购物车已开启");
                    shop_open.setTextColor(getResources().getColor(R.color.redlive));
                    shop_open.setBackgroundResource(R.mipmap.close_cart_img);
                }else{
                    isShopping =false;
                    is_shopping = "0";
                    shop_open.setText("开启购物车");
                    shop_open.setTextColor(getResources().getColor(R.color.white));
                    shop_open.setBackgroundResource(R.mipmap.open_cart_img);
                }
            }
        });

        mTv_link = findViewById(R.id.a_videoPublish_lianjie_tv);
        mSwitch_link = findViewById(R.id.a_videoPublish_lianjie_switch);
//        mSwitch_link.setChecked(isCheck);

        mSwitch_link.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showlinkDialog();
            }
        });

        getVideoChooiseType();
    }

    /**
     * 要求获取一个分类
     */
    private void getVideoChooiseType() {
        ConfigBean configBean = AppConfig.getInstance().getConfig();
        if (configBean != null) {
            List<VideoClassBean> list = configBean.getVodeoclass();
            if (list == null) {
                return;
            }
            if (list.size()>0) {
//                mTypeId = list.get(0).getId();
                mTypeListName = new String[list.size()];
                mTypeListId = new String[list.size()];
                for (int i=0;i<list.size();i++){
                    mTypeListName[i]=list.get(i).getTitle();
                    mTypeListId[i] =list.get(i).getId();
                }
            }
        }
    }

    @Override
    public void onPlayEvent(int e, Bundle bundle) {
        switch (e) {
            case TXLiveConstants.PLAY_EVT_PLAY_END://播放结束
                onReplay();
                break;
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION:
                onVideoSizeChanged(bundle.getInt("EVT_PARAM1", 0), bundle.getInt("EVT_PARAM2", 0));
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    /**
     * 获取到视频宽高回调
     */
    public void onVideoSizeChanged(float videoWidth, float videoHeight) {
        if (mTXCloudVideoView != null && videoWidth > 0 && videoHeight > 0) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mTXCloudVideoView.getLayoutParams();
            if (videoWidth / videoHeight > 0.5625f) {//横屏 9:16=0.5625
                params.height = (int) (mTXCloudVideoView.getWidth() / videoWidth * videoHeight);
                params.gravity = Gravity.CENTER;
                mTXCloudVideoView.requestLayout();
            }
        }
    }

    /**
     * 循环播放
     */
    private void onReplay() {
        if (mPlayStarted && mPlayer != null) {
            mPlayer.seek(0);
            mPlayer.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
        if (mPlayStarted && mPlayer != null) {
            mPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPaused && mPlayStarted && mPlayer != null) {
            mPlayer.resume();
        }
        mPaused = false;
    }

    public void release() {
        HttpUtil.cancel(HttpConsts.GET_CONFIG);
        HttpUtil.cancel(HttpConsts.SAVE_UPLOAD_VIDEO_INFO);
        mPlayStarted = false;
        if (mPlayer != null) {
            mPlayer.stopPlay(false);
            mPlayer.setPlayListener(null);
        }
        if (mUploadStrategy != null) {
            mUploadStrategy.cancel();
        }
        if (mMobShareUtil != null) {
            mMobShareUtil.release();
        }
        mPlayer = null;
        mUploadStrategy = null;
        mMobShareUtil = null;
    }

    @Override
    public void onBackPressed() {
        DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.video_give_up_pub), new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                if (mSaveType == Constants.VIDEO_SAVE_PUB) {
                    if (!TextUtils.isEmpty(mVideoPath)) {
                        File file = new File(mVideoPath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
                EventBus.getDefault().post(new EventBusModel("close_record"));
                release();
                VideoPublishActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpConsts.SAVE_UPLOAD_VIDEO_INFO);
        EventBus.getDefault().post(new EventBusModel("close_record"));
        release();
        super.onDestroy();
        L.e(TAG, "-------->onDestroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pub:
//                if (DataSafeUtils.isEmpty(mTypeId)){
//                    ToastUtil.show("请选择分类");
//                    return;
//                }
                publishVideo();
                break;
            case R.id.choise_type:
                getTypeId();
                break;
        }
    }

    private void getTypeId(){
        new AlertView("视频类型", null, null, null,
                mTypeListName,
                this, AlertView.Style.ActionSheet, new OnItemClickListener(){
            public void onItemClick(Object o,int position){
                mTypeId = mTypeListId[position];
                mChoseTypeName.setText(mTypeListName[position]+"");
            }
        }).setCancelable(true).show();
    }
    /**
     * 发布视频
     */
    private void publishVideo() {
        if (mConfigBean == null) {
            return;
        }
        String title = mInput.getText().toString().trim();
        //产品要求把视频描述判断去掉
        if (TextUtils.isEmpty(title)) {
            ToastUtil.show(R.string.video_title_empty);
            return;
        }
        mVideoTitle = title;
        if (TextUtils.isEmpty(mVideoPath)) {
            return;
        }
        mLoading = DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.video_pub_ing));
        mLoading.show();
        Bitmap bitmap = null;
        //生成视频封面图
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mVideoPath);
            bitmap = mmr.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }
        if (bitmap == null) {
            if (mLoading != null) {
                mLoading.dismiss();
            }
            ToastUtil.show(R.string.video_cover_img_failed);
            return;
        }
        String coverImagePath1 = mVideoPath.replace(".mp4", ".jpg");
        try {
            String spath=URLDecoder.decode(coverImagePath1, "UTF-8");

        File imageFile = new File(spath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            imageFile = null;
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (imageFile == null) {
            if (mLoading != null) {
                mLoading.dismiss();
            }
            ToastUtil.show(R.string.video_cover_img_failed);
            return;
        }
        if (mConfigBean.getVideoCloudType() == 1) {
            mUploadStrategy = new VideoUploadQnImpl(mConfigBean);
        } else {
            mUploadStrategy = new VideoUploadTxImpl(mConfigBean);
        }
        Log.e("PPPPPPPPP:",mVideoPath);
        mUploadStrategy.upload(new VideoUploadBean(new File(mVideoPath), imageFile), new VideoUploadCallback() {
            @Override
            public void onSuccess(VideoUploadBean bean) {
                if (mSaveType == Constants.VIDEO_SAVE_PUB) {
                    bean.deleteFile();
                }
                Log.v("tags",bean.getResultImageUrl()+"======"+bean.getResultVideoUrl());
                saveUploadVideoInfo(bean);
            }

            @Override
            public void onFailure() {
                ToastUtil.show(R.string.video_pub_failed);
                if (mLoading != null) {
                    mLoading.dismiss();
                }
            }
        });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把视频上传后的信息保存在服务器
     */
    private void saveUploadVideoInfo(VideoUploadBean bean) {
        HttpUtil.saveUploadVideoInfo(mVideoTitle, bean.getResultImageUrl(), bean.getResultVideoUrl(), 0,is_shopping,mTypeId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    if (mConfigBean != null && mConfigBean.getVideoAuditSwitch() == 1) {
                        ToastUtil.show(R.string.video_pub_success_2);
                    } else {
                        ToastUtil.show(R.string.video_pub_success);
                    }
                    if (mAdapter != null) {
                        String shareType = mAdapter.getShareType();
                        if (shareType != null) {
                            JSONObject obj = JSON.parseObject(info[0]);
                            shareVideoPage(shareType, obj.getString("id"), obj.getString("thumb_s"));
                        }
                    }
                    EventBus.getDefault().post(new EventBusModel("close_record"));
                    finish();
                }
            }

            @Override
            public void onFinish() {
                if (mLoading != null) {
                    mLoading.dismiss();
                }
            }
        });
    }


    /**
     * 分享页面链接
     */
    public void shareVideoPage(String shareType, String videoId, String videoImageUrl) {
        ShareData data = new ShareData();
        data.setTitle(mConfigBean.getVideoShareTitle());
        data.setDes(mConfigBean.getVideoShareDes());
        data.setImgUrl(videoImageUrl);
        String webUrl = HtmlConfig.SHARE_VIDEO + videoId;
        data.setWebUrl(webUrl);
        if (mMobShareUtil == null) {
            mMobShareUtil = new MobShareUtil();
        }
        mMobShareUtil.execute(shareType, data, null);
    }

    private AlertDialog mAlertDialog = null;
    private void showlinkDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_link, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.NoBackGroundDialog);
        builder.setView(dialogView);
        builder.setCancelable(true);
        mAlertDialog = builder.show();


        TextView btnSeveAlbum = dialogView.findViewById(R.id.dialog_btn);
        btnSeveAlbum.setOnClickListener(v -> {
            mAlertDialog.dismiss();

        });
    }

}
