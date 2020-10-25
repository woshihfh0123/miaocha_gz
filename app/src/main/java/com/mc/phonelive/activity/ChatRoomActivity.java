package com.mc.phonelive.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.dialog.ChatVoiceInputDialog;
import com.mc.phonelive.dialog.LiveRedPackSendDialogFragment;
import com.mc.phonelive.interfaces.ActivityResultCallback;
import com.mc.phonelive.interfaces.ChatRoomActionListener;
import com.mc.phonelive.interfaces.ImageResultCallback;
import com.mc.phonelive.interfaces.KeyBoardHeightChangeListener;
import com.mc.phonelive.utils.KeyBoardHeightUtil;
import com.mc.phonelive.utils.ProcessImageUtil;
import com.mc.phonelive.utils.ProcessResultUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;
import com.mc.phonelive.views.ChatRoomViewHolder;

import java.io.File;

/**
 * Created by cxf on 2018/10/24.
 * 聊天室
 */

public class ChatRoomActivity extends AbsActivity implements KeyBoardHeightChangeListener {

    private ViewGroup mRoot;
    private ViewGroup mContianer;
    private ChatRoomViewHolder mChatRoomViewHolder;
    private KeyBoardHeightUtil mKeyBoardHeightUtil;
    private ProcessImageUtil mImageUtil;
    private boolean mPaused;

    private ProcessResultUtil mProcessResultUtil;
    UserBean userBean = null;

//    private JMRtcSession session;//通话数据元信息对象
//    boolean requestPermissionSended = false;
//    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};


    public static void forward(Context context, UserBean userBean, boolean following) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        intent.putExtra(Constants.USER_BEAN, userBean);
        intent.putExtra(Constants.FOLLOW, following);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_room;
    }

    @Override
    protected void main() {

        setBarModel(true);
        mProcessResultUtil = new ProcessResultUtil(this);

        Intent intent = getIntent();
        userBean = intent.getParcelableExtra(Constants.USER_BEAN);
        if (userBean == null) {
            return;
        }
        boolean following = intent.getBooleanExtra(Constants.FOLLOW, false);
        mRoot = (ViewGroup) findViewById(R.id.root);
        mContianer = (ViewGroup) findViewById(R.id.container);
        mChatRoomViewHolder = new ChatRoomViewHolder(mContext, mContianer, userBean, ChatRoomViewHolder.TYPE_ACTIVITY, following);
        mChatRoomViewHolder.setActionListener(new ChatRoomActionListener() {
            @Override
            public void onCloseClick() {
                superBackPressed();
            }

            @Override
            public void onPopupWindowChanged(final int height) {
                onKeyBoardChanged(height);
            }

            @Override
            public void onChooseImageClick() {
                checkReadWritePermissions();
            }

            @Override
            public void onCameraClick() {
                takePhoto();
            }

            @Override
            public void onVoiceInputClick() {
                checkVoiceRecordPermission(new Runnable() {
                    @Override
                    public void run() {
                        openVoiceInputDialog();
                    }
                });
            }

            @Override
            public void onLocationClick() {
                checkLocationPermission();
            }

            @Override
            public void onVoiceClick() {
                checkVoiceRecordPermission(new Runnable() {
                    @Override
                    public void run() {
                        if (mChatRoomViewHolder != null) {
                            mChatRoomViewHolder.clickVoiceRecord();
                        }
                    }
                });
            }

            @Override
            public void onVideoClick() {
                checkVideoReadWritePermissions();
//                mProcessResultUtil.requestPermissions(new String[]{
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.RECORD_AUDIO
//                }, mStartVideoRunnable);
            }

            @Override
            public void onVideoVoiceClick() {
                if (mChatRoomViewHolder != null) {
                    mChatRoomViewHolder.sendVideoVoice();
                }
            }

            @Override
            public void onRedPackClick() {
                forwardRedPack();
//                openRedPackSendWindow();
            }

            @Override
            public ViewGroup getImageParentView() {
                return mRoot;
            }

            @Override
            public void onVideoAutoClick() {
                if (mChatRoomViewHolder != null) {
                    mChatRoomViewHolder.sendVideoAuto();
                }
            }

//            @Override
//            public void onImageClick() {
//
//            }

        });
        mChatRoomViewHolder.addToParent();
        mChatRoomViewHolder.loadData();
        mImageUtil = new ProcessImageUtil(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                if (mChatRoomViewHolder != null) {
                    mChatRoomViewHolder.sendImage(file.getAbsolutePath());
                }
            }

            @Override
            public void onFailure() {
                Log.e("AAA","--------");

            }
        });
        mKeyBoardHeightUtil = new KeyBoardHeightUtil(mContext, findViewById(android.R.id.content), this);
        mRoot.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mKeyBoardHeightUtil != null) {
                    mKeyBoardHeightUtil.start();
                }
            }
        }, 500);

    }

    private Runnable mStartVideoRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(mContext, VideoRecordActivity.class));
        }
    };


    private void onKeyBoardChanged(int keyboardHeight) {

        if (mRoot != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRoot.getLayoutParams();
            params.setMargins(0, 0, 0, keyboardHeight);
            mRoot.setLayoutParams(params);
            if (mChatRoomViewHolder != null) {
                mChatRoomViewHolder.scrollToBottom();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mChatRoomViewHolder != null) {
            mChatRoomViewHolder.back();
        } else {
            superBackPressed();
        }
    }

    private void release() {
        if (mKeyBoardHeightUtil != null) {
            mKeyBoardHeightUtil.release();
        }
        if (mChatRoomViewHolder != null) {
            mChatRoomViewHolder.refreshLastMessage();
            mChatRoomViewHolder.release();
        }
        if (mImageUtil != null) {
            mImageUtil.release();
        }
        mKeyBoardHeightUtil = null;
        mChatRoomViewHolder = null;
        mImageUtil = null;
    }

    private void superBackPressed() {
        release();
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }


    @Override
    public void onKeyBoardHeightChanged(int visibleHeight, int keyboardHeight) {
        if (mPaused) {
            return;
        }
        if (keyboardHeight == 0 && mChatRoomViewHolder != null && mChatRoomViewHolder.isPopWindowShowed()) {
            return;
        }
        onKeyBoardChanged(keyboardHeight);
    }

    @Override
    public boolean isSoftInputShowed() {
        if (mKeyBoardHeightUtil != null) {
            return mKeyBoardHeightUtil.isSoftInputShowed();
        }
        return false;
    }

    /**
     * 聊天时候选择图片，检查读写权限
     */
    private void checkReadWritePermissions() {
        if (mImageUtil == null) {
            return;
        }
        mImageUtil.requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new Runnable() {
                    @Override
                    public void run() {
                        forwardChooseImage();
                    }
                });
    }

    /**
     * 聊天时候选择视频，检查读写权限
     */
    private void checkVideoReadWritePermissions() {
        if (mImageUtil == null) {
            return;
        }
        mImageUtil.requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new Runnable() {
                    @Override
                    public void run() {
                        forwardChooseVideo();
                    }
                });
    }


    /**
     * 前往选择视频页面
     */
    private void forwardChooseVideo() {
        if (mImageUtil == null) {
            return;
        }
        mImageUtil.startActivityForResult(new Intent(mContext, VideoChooseActivity.class), new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    String mVideoPath = intent.getStringExtra(Constants.VIDEO_PATH);
                    long mDuration = intent.getLongExtra(Constants.VIDEO_DURATION, 0);
                    Log.v("tags", "imagePath--" + mVideoPath);
                    if (mChatRoomViewHolder != null) {
                        mChatRoomViewHolder.sendVideo(mVideoPath, mDuration);
                    }
                }
            }
        });
    }

    /**
     * 前往选择图片页面
     */
    private void forwardChooseImage() {
        if (mImageUtil == null) {
            return;
        }
        mImageUtil.startActivityForResult(new Intent(mContext, ChatChooseImageActivity.class), new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    String imagePath = intent.getStringExtra(Constants.SELECT_IMAGE_PATH);
                    if (mChatRoomViewHolder != null) {
                        mChatRoomViewHolder.sendImage(imagePath);
                    }
                }
            }
        });
    }


    /**
     * 拍照
     */
    private void takePhoto() {
        if (mImageUtil != null) {
            mImageUtil.getImageByCamera(false);
        }
    }

    /**
     * 发送位置的时候检查定位权限
     */
    private void checkLocationPermission() {
        if (mImageUtil == null) {
            return;
        }
        mImageUtil.requestPermissions(
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                new Runnable() {
                    @Override
                    public void run() {
                        forwardLocation();
                    }
                });
    }

    /**
     * 前往发送位置页面
     */
    private void forwardLocation() {
        if (mImageUtil == null) {
            return;
        }
        Intent intent=null;
        if (!Constants.TENXUN_GOOGLE) {
             intent = new Intent(mContext, LocationActivity.class);
        }else {
             intent = new Intent(mContext, LocationGoogleActivity.class);
        }
        mImageUtil.startActivityForResult(intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    double lat = intent.getDoubleExtra(Constants.LAT, 0);
                    double lng = intent.getDoubleExtra(Constants.LNG, 0);
                    int scale = intent.getIntExtra(Constants.SCALE, 0);
                    String address = intent.getStringExtra(Constants.ADDRESS);
                    if (lat > 0 && lng > 0 && scale > 0 && !TextUtils.isEmpty(address)) {
                        if (mChatRoomViewHolder != null) {
                            mChatRoomViewHolder.sendLocation(lat, lng, scale, address);
                        }
                    } else {
                        ToastUtil.show(WordUtil.getString(R.string.im_get_location_failed));
                    }
                }
            }
        });
    }
    /**
     * 打开发红包的弹窗
     */
    public void openRedPackSendWindow() {
        LiveRedPackSendDialogFragment fragment = new LiveRedPackSendDialogFragment();
//        fragment.setStream(mStream);
        //fragment.setCoinName(mCoinName);
        fragment.show(getSupportFragmentManager(), "LiveRedPackSendDialogFragment");
    }
    /**
     * 前往发送红包
     */
    private void forwardRedPack() {

        if (mImageUtil == null) {
            return;
        }
        mImageUtil.startActivityForResult(new Intent(mContext, RedPackActivity.class), new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {

                if (intent != null) {
                    String title = intent.getStringExtra("title");
                    String content_text = intent.getStringExtra("content_text");
                    String content = intent.getStringExtra("content");
                    String money = intent.getStringExtra("money");
                    if (!TextUtils.isEmpty(money)) {
                        if (mChatRoomViewHolder != null) {
                            mChatRoomViewHolder.sendRedPack(title, content, money, content_text);
                        }
                    } else {
                        ToastUtil.show(WordUtil.getString(R.string.im_get_redpack_failed));
                    }
                }
            }

            @Override
            public void onFailure() {

                super.onFailure();
            }
        });
    }

    /**
     * 检查录音权限
     */
    private void checkVoiceRecordPermission(Runnable runnable) {
        if (mImageUtil == null) {
            return;
        }
        mImageUtil.requestPermissions(
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO},
                runnable);
    }

    /**
     * 打开语音输入窗口
     */
    private void openVoiceInputDialog() {
        ChatVoiceInputDialog fragment = new ChatVoiceInputDialog();
        fragment.setChatRoomViewHolder(mChatRoomViewHolder);
        fragment.show(getSupportFragmentManager(), "ChatVoiceInputDialog");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
        if (mChatRoomViewHolder != null) {
            mChatRoomViewHolder.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;
        if (mChatRoomViewHolder != null) {
            mChatRoomViewHolder.onResume();
        }

    }




}
