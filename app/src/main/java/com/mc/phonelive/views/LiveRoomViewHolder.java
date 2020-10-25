package com.mc.phonelive.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mc.phonelive.HtmlConfig;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.bean.AdsBean;
import com.mc.phonelive.dialog.MoreZbDialog;
import com.mc.phonelive.utils.GlideUtils;
import com.opensource.svgaplayer.SVGAImageView;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.LiveActivity;
import com.mc.phonelive.activity.LiveAnchorActivity;
import com.mc.phonelive.activity.LiveAudienceActivity;
import com.mc.phonelive.activity.MyCoinActivity;
import com.mc.phonelive.adapter.LiveChatAdapter;
import com.mc.phonelive.adapter.LiveUserAdapter;
import com.mc.phonelive.bean.LevelBean;
import com.mc.phonelive.bean.LiveBuyGuardMsgBean;
import com.mc.phonelive.bean.LiveChatBean;
import com.mc.phonelive.bean.LiveDanMuBean;
import com.mc.phonelive.bean.LiveEnterRoomBean;
import com.mc.phonelive.bean.LiveReceiveGiftBean;
import com.mc.phonelive.bean.LiveUserGiftBean;
import com.mc.phonelive.bean.MyShopGoodsList;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.custom.TopGradual;
import com.mc.phonelive.dialog.LiveUserDialogFragment;
import com.mc.phonelive.dialog.ZbAvDialog;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.presenter.LiveDanmuPresenter;
import com.mc.phonelive.presenter.LiveEnterRoomAnimPresenter;
import com.mc.phonelive.presenter.LiveGiftAnimPresenter2;
import com.mc.phonelive.presenter.LiveLightAnimPresenter;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.StringUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by cxf on 2018/10/9.
 * 直播间公共逻辑
 */

public class LiveRoomViewHolder extends AbsViewHolder implements View.OnClickListener {

    public static int sOffsetY = 0;
    private ViewGroup mRoot;
    private ImageView mAvatar;
    private ImageView mLevelAnchor;
    private TextView mName;
    private TextView mID;
    private View mBtnFollow;
    private TextView mVotesName;//映票名称
    private TextView mVotes;
    private TextView mGuardNum;//守护人数
    private LinearLayout btn_people;
    private TextView mPeopleNum;//观看人数
    private RecyclerView mUserRecyclerView;
    private RecyclerView mChatRecyclerView;
    private LiveUserAdapter mLiveUserAdapter;
    private LiveChatAdapter mLiveChatAdapter;
    private View mBtnRedPack;
    private String mLiveUid;
    private String mStream;
    private LiveLightAnimPresenter mLightAnimPresenter;
    private LiveEnterRoomAnimPresenter mLiveEnterRoomAnimPresenter;
    private LiveDanmuPresenter mLiveDanmuPresenter;
    private LiveGiftAnimPresenter2 mLiveGiftAnimPresenter;
    private LiveRoomHandler mLiveRoomHandler;
    private HttpCallback mRefreshUserListCallback;
    private HttpCallback mTimeChargeCallback;
    protected int mUserListInterval;//用户列表刷新时间的间隔
    private GifImageView mGifImageView;
    private SVGAImageView mSVGAImageView;
    private TextView mLiveTimeTextView;//主播的直播时长
    private long mAnchorLiveTime;//主播直播时间
    private LinearLayout ll_more_zb;

    private LinearLayout tj_goods_layout;//商品推荐布局
    private MyImageView tj_img;//商品图片
    private TextView tj_goods_name, tj_goods_price;//商品名称和价格
    private String isShopping = "";//是否有商品列表
    private ImageView iv_close_goods;
    private RelativeLayout rl_goods;
    private RelativeLayout rl_adv;
    private ImageView adv_iv,iv_cancel;

    private String urls;


    public LiveRoomViewHolder(Context context, ViewGroup parentView, GifImageView gifImageView, SVGAImageView svgaImageView) {
        super(context, parentView);
        mGifImageView = gifImageView;
        mSVGAImageView = svgaImageView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_room;
    }

    @Override
    public void init() {
        mRoot = (ViewGroup) findViewById(R.id.root);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        iv_close_goods = (ImageView) findViewById(R.id.iv_close_goods);
        mLevelAnchor = (ImageView) findViewById(R.id.level_anchor);
        mName = (TextView) findViewById(R.id.name);
        mID = (TextView) findViewById(R.id.id_val);
        mBtnFollow = findViewById(R.id.btn_follow);
        rl_goods = (RelativeLayout) findViewById(R.id.rl_goods);
        rl_adv = (RelativeLayout) findViewById(R.id.rl_adv);
        adv_iv = (ImageView) findViewById(R.id.adv_iv);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_adv.setVisibility(View.GONE);
            }
        });
        adv_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.forward(mContext,urls);
            }
        });
        mVotesName = (TextView) findViewById(R.id.votes_name);
        mVotes = (TextView) findViewById(R.id.votes);
        mGuardNum = (TextView) findViewById(R.id.guard_num);
        mPeopleNum = (TextView) findViewById(R.id.people_num);
        btn_people = (LinearLayout) findViewById(R.id.btn_people);
        ll_more_zb = (LinearLayout) findViewById(R.id.ll_more_zb);
        //用户头像列表
        mUserRecyclerView = (RecyclerView) findViewById(R.id.user_recyclerView);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mLiveUserAdapter = new LiveUserAdapter(mContext);
        mLiveUserAdapter.setOnItemClickListener(new OnItemClickListener<UserBean>() {
            @Override
            public void onItemClick(UserBean bean, int position) {
                showUserDialog(bean.getId());
            }
        });
        mUserRecyclerView.setAdapter(mLiveUserAdapter);
        //聊天栏
        mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recyclerView);
        mChatRecyclerView.setHasFixedSize(true);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mChatRecyclerView.addItemDecoration(new TopGradual());
        mLiveChatAdapter = new LiveChatAdapter(mContext);
        mLiveChatAdapter.setOnItemClickListener(new OnItemClickListener<LiveChatBean>() {
            @Override
            public void onItemClick(LiveChatBean bean, int position) {
                showUserDialog(bean.getId());
            }
        });
        mChatRecyclerView.setAdapter(mLiveChatAdapter);
        mVotesName.setText(AppConfig.getInstance().getVotesName());
        mBtnFollow.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        ll_more_zb.setOnClickListener(this);
        findViewById(R.id.btn_votes).setOnClickListener(this);
        findViewById(R.id.btn_guard).setOnClickListener(this);
        btn_people.setOnClickListener(this);
        mBtnRedPack = findViewById(R.id.btn_red_pack);
        mBtnRedPack.setOnClickListener(this);
        if (mContext instanceof LiveAudienceActivity) {
            mRoot.setOnClickListener(this);
        } else {
            mLiveTimeTextView = (TextView) findViewById(R.id.live_time);
            mLiveTimeTextView.setVisibility(View.VISIBLE);
        }
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_USER_LIST);
                HttpUtil.cancel(HttpConsts.TIME_CHARGE);
                HttpUtil.cancel(HttpConsts.SET_ATTENTION);
                L.e("LiveRoomViewHolder-------->onDestroy");
            }
        };
        mLightAnimPresenter = new LiveLightAnimPresenter(mContext, mParentView);
        mLiveEnterRoomAnimPresenter = new LiveEnterRoomAnimPresenter(mContext, mContentView);
        mLiveRoomHandler = new LiveRoomHandler(this);
        mRefreshUserListCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    if (mLiveUserAdapter != null) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        List<LiveUserGiftBean> list = JSON.parseArray(obj.getString("userlist"), LiveUserGiftBean.class);
                        mLiveUserAdapter.refreshList(list);
                        setPeopleNum(mLiveUserAdapter.getItemCount());
                        EventBus.getDefault().post(new EventBusModel("userlists", mLiveUserAdapter.getAllList()));
                    }
                }
            }
        };
        mTimeChargeCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mContext instanceof LiveAudienceActivity) {
                    final LiveAudienceActivity liveAudienceActivity = (LiveAudienceActivity) mContext;
                    if (code == 0) {
                        liveAudienceActivity.roomChargeUpdateVotes();
                    } else {
                        if (mLiveRoomHandler != null) {
                            mLiveRoomHandler.removeMessages(LiveRoomHandler.WHAT_TIME_CHARGE);
                        }
                        liveAudienceActivity.pausePlay();
                        if (code == 1008) {//余额不足
                            liveAudienceActivity.setCoinNotEnough(true);
                            DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.live_coin_not_enough), false,
                                    new DialogUitl.SimpleCallback2() {
                                        @Override
                                        public void onConfirmClick(Dialog dialog, String content) {
                                            MyCoinActivity.forward(mContext);
                                        }

                                        @Override
                                        public void onCancelClick() {
                                            liveAudienceActivity.exitLiveRoom();
                                        }
                                    });
                        }
                    }
                }
            }
        };

        tj_goods_layout = (LinearLayout) findViewById(R.id.tj_goods_layout);//商品推荐布局
        tj_img = (MyImageView) findViewById(R.id.tj_img);//商品图片
        tj_goods_name = (TextView) findViewById(R.id.tj_goods_name);//商品名称
        tj_goods_price = (TextView) findViewById(R.id.tj_goods_price);//商品价格
        tj_goods_layout.setOnClickListener(this);

//        checkAds();
        iv_close_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_goods.setVisibility(View.GONE);
            }
        });
    }
    private void checkAds() {
        HttpUtil.getAds("2", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
//                    ArrayList<AdsBean> nowsBean = (ArrayList<AdsBean>) JSON.parseArray(obj.getString("goods_list"), AdsBean.class);
                    List<AdsBean> list = JSON.parseArray(Arrays.toString(info), AdsBean.class);
                    if(Utils.isNotEmpty(list)){
                        urls = Utils.isNotEmpty(list.get(0).getUrls())?list.get(0).getUrls():"";
                        rl_adv.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(mContext,list.get(0).getThumb(),adv_iv);
//                        new ZbAvDialog(mContext,list.get(0).getUrl(),list.get(0).getThumb()).show();
                    }else{
                        rl_adv.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    /**
     * 显示主播头像
     */
    public void setAvatar(String url) {
        if (mAvatar != null) {
            ImgLoader.displayAvatar(url, mAvatar);
        }
    }

    /**
     * 显示主播等级
     */
    public void setAnchorLevel(int anchorLevel) {
        if (mLevelAnchor != null) {
            LevelBean levelBean = AppConfig.getInstance().getAnchorLevel(anchorLevel);
            if (levelBean != null) {
                ImgLoader.display(levelBean.getThumbIcon(), mLevelAnchor);
            }
        }
    }

    /**
     * 显示用户名
     */
    public void setName(String name) {
        if (mName != null) {
            mName.setText(name);
            if(name.equals(AppConfig.getInstance().getUserBean().getUserNiceName())){
                ll_more_zb.setVisibility(View.GONE);
                rl_goods.setVisibility(View.GONE);
            }else{
                ll_more_zb.setVisibility(View.VISIBLE);
                rl_goods.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 显示房间号
     */
    public void setRoomNum(String roomNum,String liveuid) {
        if (mID != null) {
            mID.setText(roomNum);
            if(mID.equals(AppConfig.getInstance().getUid())){
                ll_more_zb.setVisibility(View.GONE);

            }else{
                checkAds();
                ll_more_zb.setVisibility(View.VISIBLE);
            }
            Log.v("tags",liveuid+"---");
            loadData(liveuid+"");
        }
    }

    /**
     * 判断是否显示推荐商品 默认是第一个商品
     * @param is_shopping
     */
    public void setIsShowShopping(String is_shopping) {
        Log.v("tags",is_shopping+"----------------------us--");
        this.isShopping = is_shopping;
        if (!DataSafeUtils.isEmpty(is_shopping) && is_shopping.equals("1")) {
            findViewById(R.id.tj_goods_layout).setVisibility(View.VISIBLE);
            YoYo.with( Techniques.ZoomInLeft).duration(3000) .playOn(findViewById(R.id.tj_goods_layout));
        } else {
            findViewById(R.id.tj_goods_layout).setVisibility(View.GONE);

        }
//        YoYo.with( Techniques.ZoomOut).duration(3000) .playOn(findViewById(R.id.tj_goods_layout));
    }

    /**
     * 显示是否关注
     */
    public void setAttention(int attention) {
        if (mBtnFollow != null) {
            if (attention == 0) {
                if (mBtnFollow.getVisibility() != View.VISIBLE) {
                    mBtnFollow.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBtnFollow.getVisibility() == View.VISIBLE) {
                    mBtnFollow.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 显示刷新直播间用户列表
     */
    public void setUserList(List<LiveUserGiftBean> list) {
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.refreshList(list);
            setPeopleNum(mLiveUserAdapter.getItemCount());
            EventBus.getDefault().post(new EventBusModel("userlists", mLiveUserAdapter.getAllList()));
        }
    }

    /**
     * 显示主播映票数
     */
    public void setVotes(String votes) {
        if (mVotes != null) {
            mVotes.setText(votes);
        }
    }

    /**
     * 显示主播守护人数
     */
    public void setGuardNum(int guardNum) {
        if (mGuardNum != null) {
            if (guardNum > 0) {
                mGuardNum.setText(guardNum + WordUtil.getString(R.string.ren));
            } else {
                mGuardNum.setText(R.string.main_list_no_data);
            }
        }
    }

    /**
     * 显示主播守护人数
     */
    @SuppressLint("SetTextI18n")
    public void setPeopleNum(int peoleNum) {
        if (mPeopleNum != null) {
            if (peoleNum > 0) {
                btn_people.setVisibility(View.VISIBLE);
                mPeopleNum.setText(peoleNum + WordUtil.getString(R.string.ren));
            } else {
                btn_people.setVisibility(View.GONE);
                mPeopleNum.setText("等待中");
            }
        }
    }

    public void setLiveInfo(String liveUid, String stream, int userListInterval) {
        mLiveUid = liveUid;
        mStream = stream;
        mUserListInterval = userListInterval;
    }

    /**
     * 守护信息发生变化
     */
    public void onGuardInfoChanged(LiveBuyGuardMsgBean bean) {
        setGuardNum(bean.getGuardNum());
        setVotes(bean.getVotes());
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.onGuardChanged(bean.getUid(), bean.getGuardType());
        }
    }

    /**
     * 设置红包按钮是否可见
     */
    public void setRedPackBtnVisible(boolean visible) {
        if (mBtnRedPack != null) {
            if (visible) {
                if (mBtnRedPack.getVisibility() != View.VISIBLE) {
                    mBtnRedPack.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBtnRedPack.getVisibility() == View.VISIBLE) {
                    mBtnRedPack.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.avatar:
                showAnchorUserDialog();
                break;
            case R.id.btn_follow:
                follow();
                break;
            case R.id.btn_votes://茶票贡献榜
//                openContributeWindow();
//                WebViewActivity.forward(mContext, AppConfig.HOST + "/Appapi/Contribute/index.html");
                WebViewActivity.forward(mContext, HtmlConfig.LIVE_LIST + mLiveUid);
                break;
            case R.id.btn_guard:
                ((LiveActivity) mContext).openGuardListWindow();
                break;
            case R.id.btn_people://打开观众列表
                ((LiveActivity) mContext).openPeopleListWindow();
                break;
            case R.id.root:
                light();
                break;
            case R.id.btn_red_pack:
                ((LiveActivity) mContext).openRedPackListWindow();
                break;
            case R.id.tj_goods_layout://点击商品推荐
                showShopWindow();
                break;
            case R.id.ll_more_zb://点击选择更多直播
                new MoreZbDialog(mContext,"").show();
                break;
        }
    }

    /**
     * 显示店铺列表商品
     */
    private void showShopWindow() {
        ((LiveActivity) mContext).showshopWindow();
    }
    /**
     * 显示商品列表数据
     */
//    public void showshopWindow(){
//        ShopShowDialogFragment fragment = new ShopShowDialogFragment();
//        fragment.setLiveGuardInfo(mLiveGuardInfo);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.LIVE_UID, mLiveUid);
//        bundle.putString(Constants.LIVE_STREAM, mStream);
//        fragment.setArguments(bundle);
//        fragment.show(((LiveAudienceActivity) mContext).getSupportFragmentManager(), "ShopShowDialogFragment");
//
//    }
    /**
     * 关注主播
     */
    private void follow() {
        if (TextUtils.isEmpty(mLiveUid)) {
            return;
        }
        HttpUtil.setAttention(Constants.FOLLOW_FROM_LIVE, mLiveUid, new CommonCallback<Integer>() {
            @Override
            public void callback(Integer isAttention) {
                if (isAttention == 1) {
                    ((LiveActivity) mContext).sendSystemMessage(
                            AppConfig.getInstance().getUserBean().getUserNiceName() + WordUtil.getString(R.string.live_follow_anchor));
                }
            }
        });
    }

    /**
     * 用户进入房间，用户列表添加该用户
     */
    public void insertUser(LiveUserGiftBean bean) {
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.insertItem(bean);
            setPeopleNum(mLiveUserAdapter.getItemCount());
            EventBus.getDefault().post(new EventBusModel("userlists", mLiveUserAdapter.getAllList()));
        }
    }

    /**
     * 用户进入房间，添加僵尸粉
     */
    public void insertUser(List<LiveUserGiftBean> list) {
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.insertList(list);
        }
    }

    /**
     * 用户离开房间，用户列表删除该用户
     */
    public void removeUser(String uid) {
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.removeItem(uid);

            setPeopleNum(mLiveUserAdapter.getItemCount());
            EventBus.getDefault().post(new EventBusModel("userlists", mLiveUserAdapter.getAllList()));
        }
    }

    /**
     * 刷新用户列表
     */
    private void refreshUserList() {
        if (!TextUtils.isEmpty(mLiveUid) && mRefreshUserListCallback != null && mLiveUserAdapter != null) {
            HttpUtil.cancel(HttpConsts.GET_USER_LIST);
            HttpUtil.getUserList(mLiveUid, mStream, mRefreshUserListCallback);
            startRefreshUserList();
        }
    }

    /**
     * 开始刷新用户列表
     */
    public void startRefreshUserList() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageDelayed(LiveRoomHandler.WHAT_REFRESH_USER_LIST, mUserListInterval > 0 ? mUserListInterval : 60000);

        }
    }

    /**
     * 请求计时收费的扣费接口
     */
    private void requestTimeCharge() {
        if (!TextUtils.isEmpty(mLiveUid) && mTimeChargeCallback != null) {
            HttpUtil.cancel(HttpConsts.TIME_CHARGE);
            HttpUtil.timeCharge(mLiveUid, mStream, mTimeChargeCallback);
            startRequestTimeCharge();
        }
    }

    /**
     * 开始请求计时收费的扣费接口
     */
    public void startRequestTimeCharge() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageDelayed(LiveRoomHandler.WHAT_TIME_CHARGE, 60000);
        }
    }


    /**
     * 添加聊天消息到聊天栏
     */
    public void insertChat(LiveChatBean bean) {
        if (mLiveChatAdapter != null) {
            mLiveChatAdapter.insertItem(bean);
        }
    }

    /**
     * 播放飘心动画
     */
    public void playLightAnim() {
        if (mLightAnimPresenter != null) {
            mLightAnimPresenter.play();
        }
    }

    /**
     * 点亮
     */
    private void light() {
        ((LiveAudienceActivity) mContext).light();
    }


    /**
     * 键盘高度变化
     */
    public void onKeyBoardChanged(int visibleHeight, int keyBoardHeight) {
        if (mRoot != null) {
            if (keyBoardHeight == 0) {
                mRoot.setTranslationY(0);
                return;
            }
            if (sOffsetY == 0) {
                mRoot.setTranslationY(-keyBoardHeight);
                return;
            }
            if (sOffsetY > 0 && sOffsetY < keyBoardHeight) {
                mRoot.setTranslationY(sOffsetY - keyBoardHeight);
            }
        }
    }

    /**
     * 聊天栏滚到最底部
     */
    public void chatScrollToBottom() {
        if (mLiveChatAdapter != null) {
            mLiveChatAdapter.scrollToBottom();
        }
    }

    /**
     * 用户进入房间 金光一闪,坐骑动画
     */
    public void onEnterRoom(LiveEnterRoomBean bean) {
        if (bean == null) {
            return;
        }
        if (mLiveEnterRoomAnimPresenter != null) {
            mLiveEnterRoomAnimPresenter.enterRoom(bean);
        }
    }

    /**
     * 显示弹幕
     */
    public void showDanmu(LiveDanMuBean bean) {
        if (mVotes != null) {
            mVotes.setText(bean.getVotes());
        }
        if (mLiveDanmuPresenter == null) {
            mLiveDanmuPresenter = new LiveDanmuPresenter(mContext, mParentView);
        }
        mLiveDanmuPresenter.showDanmu(bean);
    }

    /**
     * 显示主播的个人资料弹窗
     */
    private void showAnchorUserDialog() {
        if (TextUtils.isEmpty(mLiveUid)) {
            return;
        }
        showUserDialog(mLiveUid);
    }

    /**
     * 显示个人资料弹窗
     */
    private void showUserDialog(String toUid) {
        if (!TextUtils.isEmpty(mLiveUid) && !TextUtils.isEmpty(toUid)) {
            LiveUserDialogFragment fragment = new LiveUserDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.LIVE_UID, mLiveUid);
            bundle.putString(Constants.TO_UID, toUid);
            fragment.setArguments(bundle);
            fragment.show(((LiveActivity) mContext).getSupportFragmentManager(), "LiveUserDialogFragment");
        }
    }

    /**
     * 直播间贡献榜窗口
     */
    private void openContributeWindow() {
        ((LiveActivity) mContext).openContributeWindow();
    }


    /**
     * 显示礼物动画
     */
    public void showGiftMessage(LiveReceiveGiftBean bean) {
        mVotes.setText(bean.getVotes());
        if (mLiveGiftAnimPresenter == null) {
            mLiveGiftAnimPresenter = new LiveGiftAnimPresenter2(mContext, mContentView, mGifImageView, mSVGAImageView);
        }
        mLiveGiftAnimPresenter.showGiftAnim(bean);
    }

    /**
     * 增加主播映票数
     *
     * @param deltaVal 增加的映票数量
     */
    public void updateVotes(String deltaVal) {
        if (mVotes == null) {
            return;
        }
        String votesVal = mVotes.getText().toString().trim();
        if (TextUtils.isEmpty(votesVal)) {
            return;
        }
        try {
            double votes = Double.parseDouble(votesVal);
            double addVotes = Double.parseDouble(deltaVal);
            votes += addVotes;
            mVotes.setText(StringUtil.format(votes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ViewGroup getInnerContainer() {
        return (ViewGroup) findViewById(R.id.inner_container);
    }


    /**
     * 主播显示直播时间
     */
    private void showAnchorLiveTime() {
        if (mLiveTimeTextView != null) {
            mAnchorLiveTime += 1000;
            mLiveTimeTextView.setText(StringUtil.getDurationText(mAnchorLiveTime));
            startAnchorLiveTime();
        }
    }

    public void startAnchorLiveTime() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageDelayed(LiveRoomHandler.WHAT_ANCHOR_LIVE_TIME, 1000);
        }
    }

    /**
     * 主播切后台，50秒后关闭直播
     */
    public void anchorPause() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageDelayed(LiveRoomHandler.WHAT_ANCHOR_PAUSE, 50000);
        }
    }

    /**
     * 主播切后台后又回到前台
     */
    public void anchorResume() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.removeMessages(LiveRoomHandler.WHAT_ANCHOR_PAUSE);
        }
    }

    /**
     * 主播结束直播
     */
    private void anchorEndLive() {
        if (mContext instanceof LiveAnchorActivity) {
            ((LiveAnchorActivity) mContext).endLive();
        }
    }

    public void release() {
        HttpUtil.cancel(HttpConsts.GET_USER_LIST);
        HttpUtil.cancel(HttpConsts.TIME_CHARGE);
        HttpUtil.cancel(HttpConsts.SET_ATTENTION);
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.release();
        }
        mLiveRoomHandler = null;
        if (mLightAnimPresenter != null) {
            mLightAnimPresenter.release();
        }
        if (mLiveEnterRoomAnimPresenter != null) {
            mLiveEnterRoomAnimPresenter.release();
        }
        if (mLiveGiftAnimPresenter != null) {
            mLiveGiftAnimPresenter.release();
        }
        mRefreshUserListCallback = null;
        mTimeChargeCallback = null;
    }

    public void clearData() {
        HttpUtil.cancel(HttpConsts.GET_USER_LIST);
        HttpUtil.cancel(HttpConsts.TIME_CHARGE);
        HttpUtil.cancel(HttpConsts.SET_ATTENTION);
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.removeCallbacksAndMessages(null);
        }
        if (mAvatar != null) {
            mAvatar.setImageDrawable(null);
        }
        if (mLevelAnchor != null) {
            mLevelAnchor.setImageDrawable(null);
        }
        if (mName != null) {
            mName.setText("");
        }
        if (mID != null) {
            mID.setText("");
        }
        if (mVotes != null) {
            mVotes.setText("");
        }
        if (mGuardNum != null) {
            mGuardNum.setText("");
        }
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.clear();
        }
        if (mLiveChatAdapter != null) {
            mLiveChatAdapter.clear();
        }
        if (mLiveEnterRoomAnimPresenter != null) {
            mLiveEnterRoomAnimPresenter.cancelAnim();
            mLiveEnterRoomAnimPresenter.resetAnimView();
        }
        if (mLiveDanmuPresenter != null) {
            mLiveDanmuPresenter.release();
            mLiveDanmuPresenter.reset();
        }
        if (mLiveGiftAnimPresenter != null) {
            mLiveGiftAnimPresenter.cancelAllAnim();
        }
    }


    private static class LiveRoomHandler extends Handler {

        private LiveRoomViewHolder mLiveRoomViewHolder;
        private static final int WHAT_REFRESH_USER_LIST = 1;
        private static final int WHAT_TIME_CHARGE = 2;//计时收费房间定时请求接口扣费
        private static final int WHAT_ANCHOR_LIVE_TIME = 3;//直播间主播计时
        private static final int WHAT_ANCHOR_PAUSE = 4;//主播切后台

        public LiveRoomHandler(LiveRoomViewHolder liveRoomViewHolder) {
            mLiveRoomViewHolder = new WeakReference<>(liveRoomViewHolder).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mLiveRoomViewHolder != null) {
                switch (msg.what) {
                    case WHAT_REFRESH_USER_LIST:
                        mLiveRoomViewHolder.refreshUserList();
                        break;
                    case WHAT_TIME_CHARGE:
                        mLiveRoomViewHolder.requestTimeCharge();
                        break;
                    case WHAT_ANCHOR_LIVE_TIME:
                        mLiveRoomViewHolder.showAnchorLiveTime();
                        break;
                    case WHAT_ANCHOR_PAUSE:
                        mLiveRoomViewHolder.anchorEndLive();
                        break;
                }
            }
        }

        public void release() {
            removeCallbacksAndMessages(null);
            mLiveRoomViewHolder = null;
        }
    }


    /**
     * 获取主播推荐商品
     */
    private void loadData(String mLiveUid) {
        HttpUtil.shopGoodsList(1, mLiveUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

                if (code == 0 && info.length > 0) {
                    MyShopGoodsList.DataBean.InfoBean infoBean = JSON.parseObject(info[0], MyShopGoodsList.DataBean.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(infoBean.getGoods_list()) && infoBean.getGoods_list().size() > 0) {
                        List<MyShopGoodsList.DataBean.InfoBean.GoodsListBean> list = infoBean.getGoods_list();
                        if(Utils.isNotEmpty(list)){
                            rl_goods.setVisibility(View.VISIBLE);

                        if (!DataSafeUtils.isEmpty(list.get(0).getImg_list()[0])) {
                            Glide.with(mContext).load(list.get(0).getImg_list()[0]).into(tj_img);
                        }
                        if (!DataSafeUtils.isEmpty(list.get(0).getGoods_name())) {
                            tj_goods_name.setText(list.get(0).getGoods_name() + "");
                        }
                        if (!DataSafeUtils.isEmpty(list.get(0).getPrice())) {
                            tj_goods_price.setText("￥" + list.get(0).getPrice());
                        }
                        }else{
                            rl_goods.setVisibility(View.GONE);
                        }

                    }
                }else{
                    rl_goods.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFinish() {
            }
        });
    }

}
