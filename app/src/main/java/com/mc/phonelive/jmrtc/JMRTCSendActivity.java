package com.mc.phonelive.jmrtc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.MainActivity;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DataUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.jmrtc.api.JMRtcClient;
import cn.jiguang.jmrtc.api.JMRtcListener;
import cn.jiguang.jmrtc.api.JMRtcSession;
import cn.jiguang.jmrtc.api.JMSignalingMessage;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 发起音视频
 */
public class JMRTCSendActivity extends AbsActivity {
    public static Activity mJMRTCSendActivity;
    LinearLayout user_layout;
    @BindView(R.id.avatar)
    RoundedImageView avatar;
    @BindView(R.id.username)
    TextView username;
    //    private TextView tvLog;
//    ScrollView svLog;
    ImageView btn_accept;
    ImageView close_voice_img;
    ImageView voice_img_swith;//切换摄像头
    LinearLayout surfaceViewContainer, surfaceViewContainerSend;
    private static final String TAG = "tags";

    LongSparseArray<SurfaceView> surfaceViewCacheSend = new LongSparseArray<SurfaceView>();
    LongSparseArray<SurfaceView> surfaceViewCache = new LongSparseArray<SurfaceView>();
    UserInfo myinfo = JMessageClient.getMyInfo();
    SurfaceView mSendSurfaceView;

    private String mStatus = "0";//0发起通话   1 接收到通话请求  2.通话接通
    private String mToUid = "";
    private String mType = "";//1音频通话   2视频通话
    private UserBean userBean;
    //    private String mUserName;
    boolean isSeepke = false;
    boolean CloseBtn = false;// false 可拨打或者距接   true 表示挂断

    AssetManager assetManager;
    private MediaPlayer mPlayer, mNextPlayer;
    private int mPlayResId = R.raw.bond;
    private JMRtcSession session;//通话数据元信息对象
    private boolean requestPermissionSended=false;
    @Override
    protected int getLayoutId() {
        return R.layout.jmrtc_send_activity;
    }

    @Override
    protected void main() {
        EventBus.getDefault().register(this);
        Log.v("tags", "main=====");
        mSendSurfaceView = new SurfaceView(mContext);
        mJMRTCSendActivity = JMRTCSendActivity.this;
        close_voice_img = findViewById(R.id.close_voice_img);
        voice_img_swith = findViewById(R.id.voice_img_swith);
        user_layout = findViewById(R.id.user_layout);
        btn_accept = findViewById(R.id.btn_accept);
        avatar = findViewById(R.id.avatar);
        username = findViewById(R.id.username);
        surfaceViewContainer = (LinearLayout) findViewById(R.id.surface_container);
        surfaceViewContainerSend = (LinearLayout) findViewById(R.id.surface_container_send);
        voice_img_swith.setVisibility(View.GONE);
        initJMRTCData();

    }


    private void playVideo() {
        mPlayer = MediaPlayer.create(this, mPlayResId);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mPlayer.start();
            }
        });
        createNextMediaPlayer();
//        player = new MediaPlayer();
//        assetManager = getResources().getAssets();
//        try {
//            AssetFileDescriptor fileDescriptor = assetManager.openFd("bond.mp3");
//            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getStartOffset());
//            player.prepare();
//            player.start();
//
//            //配置音量，是否循环
////            player.setVolume(1, 1);
////            player.setLooping(true);
////            player.prepareAsync();
//        } catch (IOException e) {
//            playStop();
//        }
    }
    private void createNextMediaPlayer() {
        mNextPlayer = MediaPlayer.create(this, mPlayResId);
        mPlayer.setNextMediaPlayer(mNextPlayer);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mPlayer = mNextPlayer;
                createNextMediaPlayer();
            }
        });
    }

//    private void playVideo() {
//        player = new MediaPlayer();
//        assetManager = getResources().getAssets();
//        try {
//            AssetFileDescriptor fileDescriptor = assetManager.openFd("bond.mp3");
//            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getStartOffset());
//            player.prepare();
//            player.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void initJMRTCData() {
        String index = this.getIntent().getStringExtra("index");
        String userName = this.getIntent().getStringExtra("userName");
        if (!DataSafeUtils.isEmpty(userName)) {
            this.mToUid = userName;
        }
        UserBean user = this.getIntent().getParcelableExtra("user");
        String touid = this.getIntent().getStringExtra("touid");
        String type = this.getIntent().getStringExtra("type");
        if (!DataSafeUtils.isEmpty(user)) {
            this.userBean = user;
            if (!DataSafeUtils.isEmpty(userBean.getAvatar())) {
                ImgLoader.display(userBean.getAvatar(), avatar);
            }
            username.setText(userBean.getUserNiceName() + "");
        }

        if (!DataSafeUtils.isEmpty(touid)) {
            this.mToUid = touid;
        }
        if (!DataSafeUtils.isEmpty(index)) {
            this.mStatus = index;
            playVideo();
            if (jmRtcListener==null) {
                getJMRTCData();
                JMRtcClient.getInstance().initEngine(jmRtcListener);
//                JMRtcClient.getInstance().reinitEngine();
            }
        }

        if (!DataSafeUtils.isEmpty(type)) {
            this.mType = type;

        }
//        JMRtcClient.getInstance().initEngine(MainActivity.jmRtcListener);
        Log.v("tags", "初始化看看有用没");
        JMRtcClient.getInstance().enableAudio(true);//是不是开启语音
        JMRtcClient.getInstance().enableSpeakerphone(true);//是否开扩音
        JMRtcClient.getInstance().setVideoProfile(JMRtcClient.VideoProfile.Profile_720P);

        if (mStatus.equals("1")) {
            btn_accept.setVisibility(View.VISIBLE);
        } else {
            btn_accept.setVisibility(View.GONE);
        }

        if (mType.equals("1")) {
//音频
            if (!CloseBtn) {
                CloseBtn = true;
                if (jmRtcListener == null) {
                    Log.v("tags", "--------------------1");
//                    JMRtcClient.getInstance().releaseEngine();
                    getJMRTCData();
                    JMRtcClient.getInstance().initEngine(jmRtcListener);
//                    JMRtcClient.getInstance().reinitEngine();
                }
                startCall(mToUid, JMSignalingMessage.MediaType.AUDIO);
                playVideo();
            } else {
                CloseBtn = false;
            }
        } else {
//视频
            if (!CloseBtn) {
                CloseBtn = true;
                if (jmRtcListener == null) {
                    Log.v("tags", "--------------------2");
                    JMRtcClient.getInstance().releaseEngine();
                    getJMRTCData();
                    JMRtcClient.getInstance().initEngine(jmRtcListener);
//                    JMRtcClient.getInstance().reinitEngine();
                }
                startCall(mToUid, JMSignalingMessage.MediaType.VIDEO);
                playVideo();
            } else {
                CloseBtn = false;
            }
        }

        if (!DataSafeUtils.isEmpty(mToUid) && mStatus.equals("1")) {
            HttpUtil.search(mToUid, 1, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    Log.v("tags", "info=" + info[0]);
                    JSONObject obj = JSON.parseObject(info[0]);
                    String userName = obj.getString("user_nicename");
                    String avatarImg = obj.getString("avatar");
                    if (!DataSafeUtils.isEmpty(avatarImg)) {
                        ImgLoader.display(avatarImg, avatar);
                    }
                    username.setText(userName + "");
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_accept, R.id.close_img, R.id.close_voice_img, R.id.voice_img_swith})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept://接听
                CloseBtn = true;
                acceptCall();
                btn_accept.setVisibility(View.GONE);
                break;
            case R.id.close_img://挂断或拒接
                acceptOrClose();
                break;
            case R.id.close_voice_img:
                if (!isSeepke) {
                    isSeepke = true;
                    JMRtcClient.getInstance().enableSpeakerphone(true);
                    close_voice_img.setImageResource(R.mipmap.icon_video_voice_max);
                } else {
                    isSeepke = false;
                    JMRtcClient.getInstance().enableSpeakerphone(false);
                    close_voice_img.setImageResource(R.mipmap.icon_video_voice_small);
                }
                break;
            case R.id.voice_img_swith:
                JMRtcClient.getInstance().switchCamera();
                break;
        }
    }

    /**
     * 发起或者拒接  或者挂断
     */
    private void acceptOrClose() {
        Log.v("tags", mToUid + "----");
        if (mStatus.equals("0")) {//发起
            hangUp();
        } else if (mStatus.equals("1")) {//收到消息
            if (!CloseBtn) {
                CloseBtn = true;
                refuseCall();//拒接
            } else {
                CloseBtn = false;
                hangUp();//挂断
            }
        }
    }

    public void startCall(String username, final JMSignalingMessage.MediaType mediaType) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, UserInfo info) {
                Log.d(TAG, "get user info complete. code = " + responseCode + " msg = " + responseMessage + "----info=" + info);
                if (null != info) {
                    JMRtcClient.getInstance().call(Collections.singletonList(info), mediaType, new BasicCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage) {
                            Log.v("tags",responseCode+"-----code");
                            if(responseCode ==7100002){
                                ToastUtil.show("对方离线");
                            }else if(responseCode ==898030){
                                ToastUtil.show("系统繁忙，稍后重试");
                            }
                            Log.d(TAG, "call send complete发起通话成功,等待对方响应 . code = " + responseCode + " msg = " + responseMessage);
                        }
                    });
                } else {
                    Log.d(TAG, "发起失败= " + responseCode + " msg = " + responseMessage);
                }
            }
        });
    }


    private void acceptCall() {
        playStop();
        JMRtcClient.getInstance().accept(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                Log.d(TAG, "accept call!. code = " + responseCode + " msg = " + responseMessage);
                if (0 == responseCode) {
                    Log.d(TAG, "accept call!接听电话成功. code = " + responseCode + " msg = " + responseMessage);

                } else {
                    Log.d(TAG, "accept call!接听失败. code = " + responseCode + " msg = " + responseMessage);
                }
            }
        });


    }

    private void refuseCall() {
        playStop();
        JMRtcClient.getInstance().refuse(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (0 == responseCode) {
                    Log.d(TAG, "refuse call!拒听电话成功. code = " + responseCode + " msg = " + responseMessage);
                } else {
                    Log.d(TAG, "refuse call!拒听失败. code = " + responseCode + " msg = " + responseMessage);
                }
            }
        });

        this.finish();
    }

    private void hangUp() {
        playStop();
        JMRtcClient.getInstance().hangup(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (0 == responseCode) {
                    ToastUtil.show("挂断成功");
                    Log.d(TAG, "hangup call!挂断成功. code = " + responseCode + " msg = " + responseMessage);
                } else {
                    Log.d(TAG, "hangup call!挂断失败. code = " + responseCode + " msg = " + responseMessage);
                }

                JMRTCSendActivity.this.finish();
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusModel eventBusModel) {
        switch (eventBusModel.getCode()) {
            case "connected"://通话连接已建立
                if (!ButtonUtils.isFastDoubleClick01()) {
                    Log.v("tags", "建立啦@");
                    JMRtcSession session = (JMRtcSession) eventBusModel.getObject();
                    SurfaceView localSurfaceView = (SurfaceView) eventBusModel.getSecondObject();
                    Log.v("tags", "session=" + session);
                    if (JMSignalingMessage.MediaType.VIDEO == session.getMediaType()) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                        localSurfaceView.setLayoutParams(layoutParams);
                        mSendSurfaceView = localSurfaceView;

                        voice_img_swith.setVisibility(View.VISIBLE);
                        close_voice_img.setVisibility(View.GONE);
                        JMRtcClient.getInstance().enableSpeakerphone(true);//是否开扩音
                    } else {
                        close_voice_img.setVisibility(View.VISIBLE);
                        voice_img_swith.setVisibility(View.GONE);
                    }
                }
                break;
            case "MemberJoin"://有其他人加入通话
                if (!ButtonUtils.isFastDoubleClick01()) {
                    Log.v("tags", "有其他人加入通话@");
                    UserInfo userInfo = (UserInfo) eventBusModel.getObject();
                    SurfaceView mSurfaceView = (SurfaceView) eventBusModel.getSecondObject();
                    JMRtcSession sessions = (JMRtcSession) eventBusModel.getThirdObject();
                    Log.v("tags", userInfo + "-----session=" + sessions.getMediaType());
                    if (JMSignalingMessage.MediaType.VIDEO == sessions.getMediaType()) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                        mSurfaceView.setLayoutParams(layoutParams);
                        surfaceViewCache.append(userInfo.getUserID(), mSurfaceView);
                        surfaceViewContainer.addView(mSurfaceView);

                        surfaceViewCacheSend.append(myinfo.getUserID(), mSendSurfaceView);
                        surfaceViewContainerSend.addView(mSendSurfaceView);

                        voice_img_swith.setVisibility(View.VISIBLE);
                        close_voice_img.setVisibility(View.GONE);
                        JMRtcClient.getInstance().enableSpeakerphone(true);//是否开扩音
                    } else {
                        close_voice_img.setVisibility(View.VISIBLE);
                        voice_img_swith.setVisibility(View.GONE);
                    }
                }
                break;
            case "MemberOffline":
                if (!ButtonUtils.isFastDoubleClick01()) {
                    Log.v("tags", "有人退出通话@");
                    UserInfo muserInfo = (UserInfo) eventBusModel.getObject();
                    JMRtcClient.DisconnectReason reason = (JMRtcClient.DisconnectReason) eventBusModel.getSecondObject();
                    Log.v("tags", muserInfo + "-----reason=" + reason);
                    if (reason== JMRtcClient.DisconnectReason.busy) {
                        ToastUtil.show("忙碌中");
                    }
                    if (surfaceViewContainer != null) {
                        surfaceViewContainer.post(new Runnable() {
                            @Override
                            public void run() {
                                SurfaceView cachedSurfaceView = surfaceViewCache.get(muserInfo.getUserID());
                                if (null != cachedSurfaceView) {
                                    surfaceViewCache.remove(muserInfo.getUserID());
                                    surfaceViewContainer.removeView(cachedSurfaceView);
                                }
                                SurfaceView cachedSurfaceView1 = surfaceViewCacheSend.get(myinfo.getUserID());
                                if (null != cachedSurfaceView1) {
                                    surfaceViewCacheSend.remove(myinfo.getUserID());
                                    surfaceViewContainerSend.removeView(cachedSurfaceView1);
                                }
                                playStop();

                                JMRTCSendActivity.this.finish();
                            }
                        });
                    }
                }
                break;
            case "Disconnected":
                if (!ButtonUtils.isFastDoubleClick01()) {
                    Log.v("tags", "本地通话连接断开@");
//                ToastUtil.show("已挂断");
                    if (surfaceViewContainer != null) {
                        surfaceViewContainer.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("tags", "333333333333333");
                                surfaceViewCache.clear();
                                surfaceViewContainer.removeAllViews();
                            }
                        });
                    }
                    if (surfaceViewContainerSend != null) {
                        surfaceViewContainerSend.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("tags", "4444444444444444");
                                surfaceViewCacheSend.clear();
                                surfaceViewContainerSend.removeAllViews();
                            }
                        });
                    }
                    playStop();
                    JMRTCSendActivity.this.finish();
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        hangUp();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            hangUp();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SerializableJMRTCVideAndVoice();//初始化音视频权限

    }


    /**
     * 初始化视频音频通话
     */

    private void SerializableJMRTCVideAndVoice() {
        if (requestPermissionSended) {
            if (AndroidUtils.checkPermission(this, MainActivity.REQUIRED_PERMISSIONS)) {
                JMRtcClient.getInstance().reinitEngine();
            } else {
                Toast.makeText(this, "缺少必要权限，音视频引擎初始化失败，请在设置中打开对应权限", Toast.LENGTH_LONG).show();
            }
        }
        requestPermissionSended = false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        hangUp();//挂断
//        JMRtcClient.getInstance().reinitEngine();
        playStop();
    }

    private void playStop() {
        if (mPlayer != null) {
            try {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer = null;
                    mNextPlayer=null;
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                mPlayer =null;
                mNextPlayer =null;
            }
            mPlayer =null;
        }
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

    private void getJMRTCData(){
        jmRtcListener = new JMRtcListener() {
            @Override
            public void onEngineInitComplete(int i, String s) {
                super.onEngineInitComplete(i, s);
                //引擎初始化
                Log.v("tags", "音视频引擎初始化完成444");
            }

            @Override
            public void onCallOutgoing(JMRtcSession jmRtcSession) {
                super.onCallOutgoing(jmRtcSession);
                //通话已播出
                session = jmRtcSession;
                Log.v("tags", "发起通话邀请444==session=" + jmRtcSession);
            }

            @Override
            public void onCallInviteReceived(JMRtcSession jmRtcSession) {
                super.onCallInviteReceived(jmRtcSession);
                //收到通话邀请
                session = jmRtcSession;
                session.getInviterUserInfo(new RequestCallback<UserInfo>() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        Log.v("tags", "收到通话邀请444--user=" + userInfo.getUserName());
//                        if (!ButtonUtils.isFastDoubleClick())
                        onCallInviteReceiveds(mContext, jmRtcSession, userInfo.getUserName());
                    }
                });
            }

            @Override
            public void onCallConnected(JMRtcSession jmRtcSession, SurfaceView localSurfaceView) {
                super.onCallConnected(jmRtcSession, localSurfaceView);
                session = jmRtcSession;
              playStop();
                Log.v("tags", "通话连接已建立444--session=" + jmRtcSession);
                //通话连接已建立
                Log.v("tags", "session=" + session);
                if (JMSignalingMessage.MediaType.VIDEO == session.getMediaType()) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    localSurfaceView.setLayoutParams(layoutParams);
                    mSendSurfaceView = localSurfaceView;

                    voice_img_swith.setVisibility(View.VISIBLE);
                    close_voice_img.setVisibility(View.GONE);
                    JMRtcClient.getInstance().enableSpeakerphone(true);//是否开扩音
                } else {
                    close_voice_img.setVisibility(View.VISIBLE);
                    voice_img_swith.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCallMemberJoin(UserInfo userInfo, SurfaceView surfaceView) {
                super.onCallMemberJoin(userInfo, surfaceView);
                Log.v("tags", "有其他人加入通话444--userInfo=" + userInfo);
                if (JMSignalingMessage.MediaType.VIDEO == session.getMediaType()) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    surfaceView.setLayoutParams(layoutParams);
                    surfaceViewCache.append(userInfo.getUserID(), surfaceView);
                    surfaceViewContainer.addView(surfaceView);

                    surfaceViewCacheSend.append(myinfo.getUserID(), mSendSurfaceView);
                    surfaceViewContainerSend.addView(mSendSurfaceView);

                    voice_img_swith.setVisibility(View.VISIBLE);
                    close_voice_img.setVisibility(View.GONE);
                    JMRtcClient.getInstance().enableSpeakerphone(true);//是否开扩音
                } else {
                    close_voice_img.setVisibility(View.VISIBLE);
                    voice_img_swith.setVisibility(View.GONE);
                }
                playStop();
            }

            @Override
            public void onCallMemberOffline(UserInfo userInfo, JMRtcClient.DisconnectReason reason) {
                super.onCallMemberOffline(userInfo, reason);
                playStop();
                Log.v("tags", "有人退出通话444--userInfo=" + userInfo);
//                if (!ButtonUtils.isFastDoubleClick())
                if (reason== JMRtcClient.DisconnectReason.busy) {
                    ToastUtil.show("忙碌中");
                }
                if (surfaceViewContainer != null) {
                    surfaceViewContainer.post(new Runnable() {
                        @Override
                        public void run() {
                            SurfaceView cachedSurfaceView = surfaceViewCache.get(userInfo.getUserID());
                            if (null != cachedSurfaceView) {
                                surfaceViewCache.remove(userInfo.getUserID());
                                surfaceViewContainer.removeView(cachedSurfaceView);
                            }
                            SurfaceView cachedSurfaceView1 = surfaceViewCacheSend.get(myinfo.getUserID());
                            if (null != cachedSurfaceView1) {
                                surfaceViewCacheSend.remove(myinfo.getUserID());
                                surfaceViewContainerSend.removeView(cachedSurfaceView1);
                            }
                            playStop();
                            JMRtcClient.getInstance().hangup(new BasicCallback() {
                                @Override
                                public void gotResult(int responseCode, String responseMessage) {
                                    if (0 == responseCode && reason!= JMRtcClient.DisconnectReason.busy) {
                                        if (!userInfo.getUserName().equals(AppConfig.getInstance().getUid())) {
                                            ToastUtil.show("对方已挂断");
                                        }else{
                                            ToastUtil.show("挂断成功");
                                        }
                                        Log.d(TAG, "hangup call!挂断成功. code = " + responseCode + " msg = " + responseMessage);
                                    } else {
                                        Log.d(TAG, "hangup call!挂断失败. code = " + responseCode + " msg = " + responseMessage);
                                    }

                                    JMRTCSendActivity.this.finish();
                                }
                            });
                            JMRTCSendActivity.this.finish();
                        }
                    });
                }
                session=null;
            }

            @Override
            public void onCallDisconnected(JMRtcClient.DisconnectReason disconnectReason) {
                super.onCallDisconnected(disconnectReason);
                //本地通话连接断开
                Log.v("tags", "本地通话连接断开444--disconnectReason=" + disconnectReason);
//                if (!ButtonUtils.isFastDoubleClick())
                if (surfaceViewContainer != null) {
                    surfaceViewContainer.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("tags", "333333333333333");
                            surfaceViewCache.clear();
                            surfaceViewContainer.removeAllViews();
                        }
                    });
                }
                if (surfaceViewContainerSend != null) {
                    surfaceViewContainerSend.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("tags", "4444444444444444");
                            surfaceViewCacheSend.clear();
                            surfaceViewContainerSend.removeAllViews();
                        }
                    });
                }
                hangUp();
                playStop();
                JMRTCSendActivity.this.finish();
                session=null;
                ;
            }

            @Override
            public void onPermissionNotGranted(String[] requiredPermissions) {
                super.onPermissionNotGranted(requiredPermissions);
                try {
                    AndroidUtils.requestPermission(JMRTCSendActivity.this, requiredPermissions);
                    requestPermissionSended= true;
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
                Log.v("tags", "通话发生错误444--S=" + s + "----i=" + i);
                playStop();
            }
        };
    }
}
