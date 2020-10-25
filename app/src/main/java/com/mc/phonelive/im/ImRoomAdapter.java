package com.mc.phonelive.im;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.BuildConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.ChatRoomActivity;
import com.mc.phonelive.bean.RedPackBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.custom.ChatVoiceLayout;
import com.mc.phonelive.custom.MyImageView;
import com.mc.phonelive.dialog.IMRedPackResultDialogFragment;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.httpnet.JsonUtils;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.ClickUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DpUtil;
import com.mc.phonelive.utils.Googleutils;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.TextRender;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.LocationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by cxf on 2018/10/25.
 */

public class ImRoomAdapter extends RecyclerView.Adapter {

    private static final int TYPE_TEXT_LEFT = 1;
    private static final int TYPE_TEXT_RIGHT = 2;
    private static final int TYPE_IMAGE_LEFT = 3;
    private static final int TYPE_IMAGE_RIGHT = 4;
    private static final int TYPE_VOICE_LEFT = 5;
    private static final int TYPE_VOICE_RIGHT = 6;
    private static final int TYPE_LOCATION_LEFT = 7;
    private static final int TYPE_LOCATION_RIGHT = 8;
    private static final int TYPE_VIDEO_LEFT = 9;
    private static final int TYPE_VIDEO_RIGHT = 10;
    private static final int TYPE_REDPACK_LEFT = 11;
    private static final int TYPE_REDPACK_RIGHT = 12;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private UserBean mUserBean;
    private UserBean mToUserBean;
    private String mTxLocationKey;
    private List<ImMessageBean> mList;
    private LayoutInflater mInflater;
    private String mUserAvatar;
    private String mToUserAvatar;
    private long mLastMessageTime;
    private ActionListener mActionListener;
    private View.OnClickListener mOnImageClickListener;
    private int[] mLocation;
    private String toUid;
    private ValueAnimator mAnimator;
    private ChatVoiceLayout mChatVoiceLayout;
    private View.OnClickListener mOnVoiceClickListener;
    private CommonCallback<File> mVoiceFileCallback;
    private Context mContext;
    TXCloudVideoView mTXCloudVideoView;
    TXVodPlayer mPlayer;
    private RedPackBean mRedPackBean;

    public ImRoomAdapter(Context context, String toUid, UserBean toUserBean) {
        this.mContext = context;
        this.toUid = toUid;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mUserBean = AppConfig.getInstance().getUserBean();
        mToUserBean = toUserBean;
        mTxLocationKey = AppConfig.getInstance().getTxLocationKey();
        mUserAvatar = mUserBean.getAvatar();
        mToUserAvatar = mToUserBean.getAvatar();
        mLocation = new int[2];
        mOnImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                MyImageView imageView = (MyImageView) v;
                imageView.getLocationOnScreen(mLocation);
                if (mActionListener != null) {
                    mActionListener.onImageClick(imageView, mLocation[0], mLocation[1]);
                }
            }
        };
        mVoiceFileCallback = new CommonCallback<File>() {
            @Override
            public void callback(File file) {
                if (mActionListener != null) {
                    mActionListener.onVoiceStartPlay(file);
                }
            }
        };
        mOnVoiceClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ClickUtil.canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                final int position = (int) tag;
                ImMessageBean bean = mList.get(position);
                if (mChatVoiceLayout != null) {
                    int msgId1 = mChatVoiceLayout.getMsgId();
                    int msgId2 = bean.getMessageId();
                    mChatVoiceLayout.cancelAnim();
                    if (msgId1 == msgId2) {//同一个消息对象
                        if (mRecyclerView != null) {
                            mRecyclerView.setLayoutFrozen(false);
                        }
                        mChatVoiceLayout = null;
                        if (mActionListener != null) {
                            mActionListener.onVoiceStopPlay();
                        }
                    } else {
                        mChatVoiceLayout = (ChatVoiceLayout) v;
                        mAnimator.start();
                        ImMessageUtil.getInstance().getVoiceFile(bean, mVoiceFileCallback);
                        bean.hasRead(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                notifyItemChanged(position, Constants.PAYLOAD);
                            }
                        });

                    }
                } else {
                    if (mRecyclerView != null) {
                        mRecyclerView.setLayoutFrozen(true);
                    }
                    mChatVoiceLayout = (ChatVoiceLayout) v;
                    mAnimator.start();
                    ImMessageUtil.getInstance().getVoiceFile(bean, mVoiceFileCallback);
                    bean.hasRead(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            notifyItemChanged(position, Constants.PAYLOAD);
                        }
                    });

                }
            }
        };
        mAnimator = ValueAnimator.ofFloat(0, 900);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(700);
        mAnimator.setRepeatCount(-1);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                if (mChatVoiceLayout != null) {
                    mChatVoiceLayout.animate((int) (v / 300));
                }
            }
        });
    }

    /**
     * 停止语音动画
     */
    public void stopVoiceAnim() {
        if (mChatVoiceLayout != null) {
            mChatVoiceLayout.cancelAnim();
        }
        mChatVoiceLayout = null;
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutFrozen(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ImMessageBean msg = mList.get(position);
        switch (msg.getType()) {
            case ImMessageBean.TYPE_TEXT:
                if (msg.isFromSelf()) {
                    return TYPE_TEXT_RIGHT;
                } else {
                    return TYPE_TEXT_LEFT;
                }
            case ImMessageBean.TYPE_IMAGE:
                if (msg.isFromSelf()) {
                    return TYPE_IMAGE_RIGHT;
                } else {
                    return TYPE_IMAGE_LEFT;
                }
            case ImMessageBean.TYPE_VOICE:
                if (msg.isFromSelf()) {
                    return TYPE_VOICE_RIGHT;
                } else {
                    return TYPE_VOICE_LEFT;
                }
            case ImMessageBean.TYPE_LOCATION:
                if (msg.isFromSelf()) {
                    return TYPE_LOCATION_RIGHT;
                } else {
                    return TYPE_LOCATION_LEFT;
                }
            case ImMessageBean.TYPE_VIDEO:
                if (msg.isFromSelf()) {
                    return TYPE_VIDEO_RIGHT;
                } else {
                    return TYPE_VIDEO_LEFT;
                }
            case ImMessageBean.TYPE_REDPACK:
                if (msg.isFromSelf()) {
                    return TYPE_REDPACK_RIGHT;
                } else {
                    return TYPE_REDPACK_LEFT;
                }

        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("tags", viewType + "-----viewType");
        switch (viewType) {
            case TYPE_TEXT_LEFT:
                return new TextVh(mInflater.inflate(R.layout.item_chat_text_left, parent, false));
            case TYPE_TEXT_RIGHT:
                return new SelfTextVh(mInflater.inflate(R.layout.item_chat_text_right, parent, false));
            case TYPE_IMAGE_LEFT:
                return new ImageVh(mInflater.inflate(R.layout.item_chat_image_left, parent, false));
            case TYPE_IMAGE_RIGHT:
                return new SelfImageVh(mInflater.inflate(R.layout.item_chat_image_right, parent, false));
            case TYPE_VOICE_LEFT:
                return new VoiceVh(mInflater.inflate(R.layout.item_chat_voice_left, parent, false));
            case TYPE_VOICE_RIGHT:
                return new SelfVoiceVh(mInflater.inflate(R.layout.item_chat_voice_right, parent, false));
            case TYPE_LOCATION_LEFT:
                return new LocationVh(mInflater.inflate(R.layout.item_chat_location_left, parent, false));
            case TYPE_LOCATION_RIGHT:
                return new SelfLocationVh(mInflater.inflate(R.layout.item_chat_location_right, parent, false));
            case TYPE_REDPACK_LEFT:
                return new RedPackVh(mInflater.inflate(R.layout.item_chat_redpack_left, parent, false));
            case TYPE_REDPACK_RIGHT:
                return new SelfRedPackVh(mInflater.inflate(R.layout.item_chat_redpack_right, parent, false));
            case TYPE_VIDEO_LEFT:
                return new VideoVh(mInflater.inflate(R.layout.item_chat_video_left, parent, false));
            case TYPE_VIDEO_RIGHT:
                return new SelfVideoVh(mInflater.inflate(R.layout.item_chat_video_right, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position, List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    public int insertItem(ImMessageBean bean) {
        if (mList != null && bean != null) {
            int size = mList.size();
            mList.add(bean);
            notifyItemInserted(size);
            int lastItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
            if (lastItemPosition != size - 1) {
                mRecyclerView.smoothScrollToPosition(size);
            } else {
                mRecyclerView.scrollToPosition(size);
            }
            return size;
        }
        return -1;
    }

    public void insertSelfItem(final ImMessageBean bean) {
        bean.setLoading(true);
        final int position = insertItem(bean);
        if (position != -1) {
            final Message msg = bean.getRawMessage();
            if (msg != null) {
                msg.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseDesc) {
                        bean.setLoading(false);
                        if (responseCode != 0) {
                            bean.setSendFail(true);
                            //消息发送失败
                            ToastUtil.show(WordUtil.getString(R.string.im_msg_send_failed));
                            L.e("极光IM---消息发送失败--->  responseDesc:" + responseDesc);
                        } else {
                            String money = JsonUtils.getSinglePara(bean.getRawMessage().getContent().toJson(), "money");
                            String title = JsonUtils.getSinglePara(bean.getRawMessage().getContent().toJson(), "title");
                            if (msg.getContentType().equals(ContentType.custom)) {
                                sendRedPackMsg(toUid, money, title);
                            }
                        }
                        notifyItemChanged(position, Constants.PAYLOAD);
                    }
                });
                ImMessageUtil.getInstance().sendMessage(msg);
            }

        }
    }

    public void sendRedPackMsg(String recvid, String coin, String desc) {
        HttpUtil.sendRedPackData(recvid, coin, desc, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
            }

        });
    }


    public ImChatImageBean getChatImageBean(int msgId) {
        List<ImMessageBean> list = new ArrayList<>();
        int imagePosition = 0;
        for (int i = 0, size = mList.size(); i < size; i++) {
            ImMessageBean bean = mList.get(i);
            if (bean.getType() == ImMessageBean.TYPE_IMAGE) {
                list.add(bean);
                if (bean.getRawMessage().getId() == msgId) {
                    imagePosition = list.size() - 1;
                }
            }
        }
        return new ImChatImageBean(list, imagePosition);
    }

    public void setList(List<ImMessageBean> list) {
        if (mList != null && list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void scrollToBottom() {
        if (mList.size() > 0 && mLayoutManager != null) {
            mLayoutManager.scrollToPositionWithOffset(mList.size() - 1, -DpUtil.dp2px(20));
        }
    }

    public ImMessageBean getLastMessage() {
        if (mList == null || mList.size() == 0) {
            return null;
        }
        return mList.get(mList.size() - 1);
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView mAvatar;
        TextView mTime;
        ImMessageBean mImMessageBean;

        public Vh(View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mTime = (TextView) itemView.findViewById(R.id.time);
        }

        void setData(ImMessageBean bean, int position, Object payload) {
            mImMessageBean = bean;
            if (payload == null) {
                Log.e("TTTT:",mUserAvatar+"");
                if (bean.isFromSelf()) {
                    ImgLoader.display(mUserAvatar, mAvatar);
                } else {
                    ImgLoader.display(mToUserAvatar, mAvatar);
                }
                if (position == 0) {
                    mLastMessageTime = bean.getTime();
                    if (mTime.getVisibility() != View.VISIBLE) {
                        mTime.setVisibility(View.VISIBLE);
                    }
                    mTime.setText(ImDateUtil.getTimestampString(mLastMessageTime));
                } else {
                    if (ImDateUtil.isCloseEnough(bean.getTime(), mLastMessageTime)) {
                        if (mTime.getVisibility() == View.VISIBLE) {
                            mTime.setVisibility(View.GONE);
                        }
                    } else {
                        mLastMessageTime = bean.getTime();
                        if (mTime.getVisibility() != View.VISIBLE) {
                            mTime.setVisibility(View.VISIBLE);
                        }
                        mTime.setText(ImDateUtil.getTimestampString(mLastMessageTime));
                    }
                }
            }
        }
    }

    class TextVh extends Vh {

        TextView mText;

        public TextVh(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                String text = ((TextContent) bean.getRawMessage().getContent()).getText();
                mText.setText(TextRender.renderChatMessage(text));
            }
        }
    }

    class SelfTextVh extends TextVh {

        View mFailIcon;
        View mLoading;

        public SelfTextVh(View itemView) {
            super(itemView);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class ImageVh extends Vh {

        MyImageView mImg;

        public ImageVh(View itemView) {
            super(itemView);
            mImg = (MyImageView) itemView.findViewById(R.id.img);
            mImg.setOnClickListener(mOnImageClickListener);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mImg.setMsgId(bean.getRawMessage().getId());
                File imageFile = bean.getImageFile();
                if (imageFile != null) {
                    mImg.setFile(imageFile);
                    ImgLoader.display(imageFile, mImg);
                } else {
                    ImMessageUtil.getInstance().displayImageFile(bean, mImg);
                }
            }
        }
    }

    class SelfImageVh extends ImageVh {

        View mFailIcon;
        View mLoading;

        public SelfImageVh(View itemView) {
            super(itemView);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class VideoVh extends Vh {

        MyImageView mImg;

        public VideoVh(View itemView) {
            super(itemView);
            mImg = (MyImageView) itemView.findViewById(R.id.img);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mImg.setMsgId(bean.getRawMessage().getId());
                File imageFile = bean.getImageFile();
                File VideoFile = bean.getVideoFile();
                if (imageFile != null) {
                    Log.v("tags", "file=" + imageFile);
                    mImg.setFile(imageFile);
                    ImgLoader.display(imageFile, mImg);
                } else {
                    ImMessageUtil.getInstance().videoDownload(bean, mImg, mContext);
                }
//                String finalFName = fName;
                mImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO：视频点击
                        if (!DataSafeUtils.isEmpty(VideoFile))
                            getVideoPlay(VideoFile);
                        else
                            ImMessageUtil.getInstance().videoIMData1(bean, mImg, mContext);
                    }
                });
            }
        }


    }

    public void getVideoPlay(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", imageFile);
            Log.v("tags", imageFile + "**********" + contentUri);
            intent.setDataAndType(contentUri, "video/*");
        } else {
            uri = Uri.fromFile(imageFile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "video/*");
        }

        mContext.startActivity(intent);
    }

    class SelfVideoVh extends VideoVh {

        View mFailIcon;
        View mLoading;

        public SelfVideoVh(View itemView) {
            super(itemView);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class VoiceVh extends Vh {

        TextView mDuration;
        View mRedPoint;
        ChatVoiceLayout mChatVoiceLayout;

        public VoiceVh(View itemView) {
            super(itemView);
            mRedPoint = itemView.findViewById(R.id.red_point);
            mDuration = (TextView) itemView.findViewById(R.id.duration);
            mChatVoiceLayout = itemView.findViewById(R.id.voice);
            mChatVoiceLayout.setOnClickListener(mOnVoiceClickListener);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mDuration.setText(bean.getVoiceDuration() + "s");
                mChatVoiceLayout.setTag(position);
                mChatVoiceLayout.setMsgId(bean.getMessageId());
                mChatVoiceLayout.setDuration(bean.getVoiceDuration());
            }
            if (bean.isRead()) {
                if (mRedPoint.getVisibility() == View.VISIBLE) {
                    mRedPoint.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mRedPoint.getVisibility() != View.VISIBLE) {
                    mRedPoint.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    class SelfVoiceVh extends Vh {

        TextView mDuration;
        ChatVoiceLayout mChatVoiceLayout;
        View mFailIcon;
        View mLoading;

        public SelfVoiceVh(View itemView) {
            super(itemView);
            mDuration = (TextView) itemView.findViewById(R.id.duration);
            mChatVoiceLayout = itemView.findViewById(R.id.voice);
            mChatVoiceLayout.setOnClickListener(mOnVoiceClickListener);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                mDuration.setText(bean.getVoiceDuration() + "s");
                mChatVoiceLayout.setTag(position);
                mChatVoiceLayout.setMsgId(bean.getMessageId());
                mChatVoiceLayout.setDuration(bean.getVoiceDuration());
            }
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    class LocationVh extends Vh implements OnMapReadyCallback {

        TextView mTitle;
        TextView mAddress;
        ImageView mMap;

        public LocationVh(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mAddress = (TextView) itemView.findViewById(R.id.address);
            mMap = itemView.findViewById(R.id.map);

        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                LocationContent locationContent = (LocationContent) (bean.getRawMessage().getContent());
                try {
                    JSONObject obj = JSON.parseObject(locationContent.getAddress());
                    mTitle.setText(obj.getString("name"));
                    mAddress.setText(obj.getString("info"));
                } catch (Exception e) {
                    mTitle.setText("");
                    mAddress.setText("");
                }
                int zoom = locationContent.getScale().intValue();
                if (zoom > 18 || zoom < 4) {
                    zoom = 18;
                }
                double lat = locationContent.getLatitude().doubleValue();
                double lng = locationContent.getLongitude().doubleValue();
                String staticMapUrl = "";
                if (Constants.TENXUN_GOOGLE) {
                    //谷歌地图
                    staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng + "&markers=" + lat + "," + lng + ",orangea&zoom=" + zoom + "&sensor=false&size=200x120&key=" + AppConfig.MGOOGLEKEY;
                } else {
                    //腾讯地图生成静态图接口
                    staticMapUrl = "https://apis.map.qq.com/ws/staticmap/v2/?center=" + lat + "," + lng + "&size=200*120&scale=2&zoom=" + zoom + "&key=" + mTxLocationKey;
                }
                Log.v("tags",staticMapUrl);
                ImgLoader.display(staticMapUrl, mMap);

                mMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!ButtonUtils.isFastDoubleClick()) {
                            String merchantLon = lng+"";
                            String merchantLat = lat+"";
                            Log.i("tags", merchantLon + "----" + merchantLat);
                            Googleutils.openGoogleMap(mContext, mAddress.getText().toString(), merchantLon, merchantLat);
                        }
                    }
                });
            }

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(39.908173, 116.397947)));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(39.908173, 116.397947)));
        }
    }


    class SelfLocationVh extends LocationVh {

        View mFailIcon;
        View mLoading;

        public SelfLocationVh(View itemView) {
            super(itemView);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }

        }
    }


    class RedPackVh extends Vh {

        TextView mText, tv_content;
        LinearLayout bubble;

        public RedPackVh(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            bubble = (LinearLayout) itemView.findViewById(R.id.bubble);

        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (payload == null) {
                CustomContent redpack = (CustomContent) bean.getRawMessage().getContent();
                Log.v("tags", bean.getRawMessage().getContent().toJson() + "");
                JSONObject obj = JSON.parseObject(redpack.toJson());
                String title = obj.getString("title");
                String content = obj.getString("content");
                String money = obj.getString("money");
                mText.setText(title);
//                tv_content.setText(content);

                bubble.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRedPackBean = new RedPackBean();
                        mRedPackBean.setAvatar(mUserAvatar);
                        mRedPackBean.setCoin(Integer.parseInt(money));
                        mRedPackBean.setCount(1);
                        mRedPackBean.setTitle(mUserBean.getUserNiceName());
                        mRedPackBean.setUid(mUserBean.getId());
                        mRedPackBean.setId(Integer.parseInt(mUserBean.getId()));


                        if (bean.isFromSelf()) {
//                            ToastUtil.show("红包已发出");
                            mRedPackBean.setmTip("红包已发出");
                            mRedPackBean.setSendType(1);
                        } else {
                            ToastUtil.show("红包已存入余额中");
                            mRedPackBean.setmTip("红包已领取");
                            mRedPackBean.setSendType(2);
                        }
                        ImgLoader.displayAvatar(mRedPackBean.getAvatar(), mAvatar);
                        forwardRobDetail(mRedPackBean);
                    }
                });
            }
        }
    }

    /**
     * 聊天界面点击红包查看领取详情
     *
     * @param mRedPackBean
     */
    private void forwardRobDetail(RedPackBean mRedPackBean) {
        IMRedPackResultDialogFragment fragment = new IMRedPackResultDialogFragment();
        fragment.setStream("钻石红包");
        fragment.setRedPackBean(mRedPackBean);
        fragment.setCoinName("钻");
        fragment.show(((ChatRoomActivity) mContext).getSupportFragmentManager(), "IMRedPackResultDialogFragment");
    }

    class SelfRedPackVh extends RedPackVh {

        View mFailIcon;
        View mLoading;

        public SelfRedPackVh(View itemView) {
            super(itemView);
            mFailIcon = itemView.findViewById(R.id.icon_fail);
            mLoading = itemView.findViewById(R.id.loading);
        }

        @Override
        public void setData(ImMessageBean bean, int position, Object payload) {
            super.setData(bean, position, payload);
            if (bean.isLoading()) {
                if (mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
            } else {
                if (mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
            }
            if (bean.isSendFail()) {
                if (mFailIcon.getVisibility() != View.VISIBLE) {
                    mFailIcon.setVisibility(View.VISIBLE);
                }
            } else {
                if (mFailIcon.getVisibility() == View.VISIBLE) {
                    mFailIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onImageClick(MyImageView imageView, int x, int y);

        void onVoiceStartPlay(File voiceFile);

        void onVoiceStopPlay();
    }

    public void release() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mActionListener = null;
        mOnImageClickListener = null;
        mOnVoiceClickListener = null;
    }


}
