package com.mc.phonelive.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.FansActivity;
import com.mc.phonelive.activity.FollowActivity;
import com.mc.phonelive.activity.LiveRecordActivity;
import com.mc.phonelive.activity.MyCoinActivity;
import com.mc.phonelive.activity.MyProfitActivity;
import com.mc.phonelive.activity.MyShopActivity;
import com.mc.phonelive.activity.MyShopConditionActivity;
import com.mc.phonelive.activity.MyVideoActivity;
import com.mc.phonelive.activity.SettingActivity;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.activity.foxtone.AgentApplyActivity;
import com.mc.phonelive.activity.foxtone.AreaAgentTypeActivity;
import com.mc.phonelive.activity.foxtone.ClubActivity;
import com.mc.phonelive.activity.foxtone.CollectionBindingActivity;
import com.mc.phonelive.activity.foxtone.InviteFriendsActivity;
import com.mc.phonelive.activity.foxtone.MusicalCenterActivity;
import com.mc.phonelive.activity.foxtone.MyLevelActivity;
import com.mc.phonelive.activity.foxtone.MyShulianduActivity;
import com.mc.phonelive.activity.foxtone.MyTeamActivity;
import com.mc.phonelive.activity.foxtone.MyYindouActivity;
import com.mc.phonelive.activity.foxtone.MyYinfenzhiActivity;
import com.mc.phonelive.activity.foxtone.RealNameAuthenticationActivity;
import com.mc.phonelive.activity.foxtone.TradingCenterActivity;
import com.mc.phonelive.activity.shop.CartMainActivity;
import com.mc.phonelive.activity.shop.LeaderboardListActivity;
import com.mc.phonelive.activity.shop.LiveRoomManagementActivity;
import com.mc.phonelive.activity.shop.MyAddressListActivity;
import com.mc.phonelive.activity.shop.MyOrderActivity;
import com.mc.phonelive.adapter.MainMeAdapter;
import com.mc.phonelive.adapter.MainMeOneMenuAdapter;
import com.mc.phonelive.adapter.MainMePlayAdapter;
import com.mc.phonelive.bean.LevelBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.UserItemBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.MainAppBarLayoutListener;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.IconUtil;
import com.mc.phonelive.utils.L;
import com.mc.phonelive.utils.StringUtil;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 老个人中心
 */

public class MainMeViewHolder extends AbsMainChildViewHolder implements OnItemClickListener<UserItemBean>, View.OnClickListener {

    private TextView mTtileView;
    private ImageView mAvatar;
    private TextView mName;
    private ImageView mSex;
    private ImageView mLevelAnchor;
    private ImageView mLevel;
    private TextView mID;
    private TextView mLive;
    private TextView mFollow;
    private TextView mFans;
    private boolean mPaused;
    //    private RecyclerView shopRecyclerView;
    private RecyclerView mRecyclerView01, mRecyclerView02, mRecyclerView;

    private TextView me_yq_num;//乐器数量
    private TextView yindou_tv;//音豆
    private TextView yfz_scale;//音分值
    private TextView me_yfz_level;//音分值等级
    private TextView sld_scale;//熟练度
    private TextView me_sld_level;//熟练度等级
    private ProgressBar seek_bar1, seek_bar2;

    private String area_status="";//合伙招募申请状态


    private MainMeAdapter mThreeAdapter;
    private MainMeOneMenuAdapter mOneAdapter, mTwoAdapter;
    private MainMePlayAdapter mPlayAdapter;
    List<UserItemBean> mOneList = new ArrayList<>();
    List<UserItemBean> mTwoList = new ArrayList<>();
    List<UserItemBean> mThreeList = new ArrayList<>();

    public MainMeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_me;
    }

    @Override
    public void init() {
        super.init();
        mTtileView = (TextView) findViewById(R.id.titleView);

        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mSex = (ImageView) findViewById(R.id.sex);
        mLevelAnchor = (ImageView) findViewById(R.id.level_anchor);
        mLevel = (ImageView) findViewById(R.id.level);
        mID = (TextView) findViewById(R.id.id_val);
        mLive = (TextView) findViewById(R.id.live);
        mFollow = (TextView) findViewById(R.id.follow);
        mFans = (TextView) findViewById(R.id.fans);
        me_yq_num = (TextView) findViewById(R.id.me_yq_num);
        mLive.setOnClickListener(this);
        mFollow.setOnClickListener(this);
        mFans.setOnClickListener(this);
        //------------------------------------------------------------------------------
        yindou_tv = (TextView) findViewById(R.id.yindou_tv);//音豆
        yfz_scale = (TextView) findViewById(R.id.yfz_scale);//音分值
        me_yfz_level = (TextView) findViewById(R.id.me_yfz_level);//音分值等级
        sld_scale = (TextView) findViewById(R.id.sld_scale);//熟练度
        me_sld_level = (TextView) findViewById(R.id.me_sld_level);//熟练度等级
        seek_bar1 = (ProgressBar) findViewById(R.id.seek_bar1);
        seek_bar2 = (ProgressBar) findViewById(R.id.seek_bar2);

        //------------------------------------------------------------------------------


        findViewById(R.id.avatar).setOnClickListener(this);
        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.me_yq_center).setOnClickListener(this);
        findViewById(R.id.btn_level).setOnClickListener(this);
        findViewById(R.id.btn_yindou).setOnClickListener(this);
        findViewById(R.id.btn_yinfenzhi).setOnClickListener(this);
        findViewById(R.id.btn_shuliandu).setOnClickListener(this);


        mRecyclerView01 = (RecyclerView) findViewById(R.id.reyclerview01);
        mRecyclerView01.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerView02 = (RecyclerView) findViewById(R.id.reyclerview02);
        mRecyclerView02.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onResume() {
                if (mPaused && mShowed) {
                    loadData();
                }
                mPaused = false;
            }

            @Override
            public void onPause() {
                mPaused = true;
            }

            @Override
            public void onDestroy() {
                L.e("main----MainMeViewHolder-------LifeCycle---->onDestroy");
                HttpUtil.cancel(HttpConsts.GET_BASE_INFO);
            }
        };
        mAppBarLayoutListener = new MainAppBarLayoutListener() {
            @Override
            public void onOffsetChanged(float rate) {
                mTtileView.setAlpha(rate);
            }
        };
        mNeedDispatch = true;
    }

    @Override
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
    }

    @Override
    public void loadData() {
        if (isFirstLoadData()) {
            AppConfig appConfig = AppConfig.getInstance();
            UserBean u = appConfig.getUserBean();
            List<List<UserItemBean>> list = appConfig.getUserItemList();
            if (u != null && list != null) {
                showData(u, list);
            }
        }
        HttpUtil.getBaseInfo(mCallback);
    }

    private CommonCallback<UserBean> mCallback = new CommonCallback<UserBean>() {
        @Override
        public void callback(UserBean bean) {
            List<List<UserItemBean>> list = AppConfig.getInstance().getUserItemList();
            if (bean != null) {
                showData(bean, list);
            }
        }
    };

    private void showData(UserBean u, List<List<UserItemBean>> list) {
        if (!DataSafeUtils.isEmpty(u.getArea_status())){
            area_status = u.getArea_status();
        }
        ImgLoader.displayAvatar(u.getAvatar(), mAvatar);
        mTtileView.setText(u.getUserNiceName());
        mName.setText(u.getUserNiceName());
        mSex.setImageResource(IconUtil.getSexIcon(Integer.parseInt(u.getSex())));
        AppConfig appConfig = AppConfig.getInstance();
        LevelBean anchorLevelBean = appConfig.getAnchorLevel(u.getLevelAnchor());
        if (anchorLevelBean != null) {
            ImgLoader.display(anchorLevelBean.getThumb(), mLevelAnchor);
        }
        LevelBean levelBean = appConfig.getLevel(u.getLevel());
        if (levelBean != null) {
            ImgLoader.display(levelBean.getThumb(), mLevel);
        }
        mID.setText(u.getLiangNameTip());
        if (!DataSafeUtils.isEmpty(u.getLives()))
            mLive.setText(StringUtil.toWan(u.getLives()) + " 直播");
        if (!DataSafeUtils.isEmpty(u.getFollows()))
            mFollow.setText(StringUtil.toWan(u.getFollows()) + " 关注");
        if (!DataSafeUtils.isEmpty(u.getFans()))
            mFans.setText(StringUtil.toWan(u.getFans()) + " 粉丝");
        if (!DataSafeUtils.isEmpty(u.getMusical_count()))
            me_yq_num.setText(u.getMusical_count() + "个乐器");

        if (!DataSafeUtils.isEmpty(u.getFee())) {
            if (String.valueOf(u.getFee()).contains(".")) {
                int mEndIndex =String.valueOf(u.getFee()).lastIndexOf(".");
                if (String.valueOf(u.getFee()).substring(0, mEndIndex).length() < 4) {
                    yindou_tv.setText(String.valueOf(u.getFee()));
                } else {
                    yindou_tv.setText(String.valueOf(u.getFee()).substring(0, 4) + ".+");
                }
            } else {
                if (String.valueOf(u.getFee()).length() < 5) {
                    yindou_tv.setText(String.valueOf(u.getFee()));
                } else {
                    yindou_tv.setText(String.valueOf(u.getFee()).substring(0, 4) + ".+");
                }
            }
        }


        if (!DataSafeUtils.isEmpty(u.getGrade_id())) {
            me_yfz_level.setText("LV."+u.getGrade_id());//音分值等级
        }
        if (!DataSafeUtils.isEmpty(u.getCent())) {
            yfz_scale.setText(u.getCent() + "");//音分值
//
            if (Float.parseFloat(u.getCent()) < 100) {
                seek_bar1.setMax(100);
            } else {
                seek_bar1.setMax(500);
            }
            seek_bar1.setProgress((int) Float.parseFloat(u.getCent()));
        }
        if (!DataSafeUtils.isEmpty(u.getSkill_id())){
            me_sld_level.setText("LV."+u.getSkill_id());//音分值等级
        }
        if (!DataSafeUtils.isEmpty(u.getSkill())) {
            if (Float.parseFloat(u.getSkill()) < 100) {
                sld_scale.setText(u.getSkill() + "");//熟练度
                //        me_sld_level;//熟练度等级
                if (Float.parseFloat(u.getSkill()) < 100) {
                    seek_bar2.setMax(100);
                } else {
                    seek_bar2.setMax(500);
                }
                seek_bar2.setProgress((int) Float.parseFloat(u.getSkill()));
            }
        }


        if (list != null && list.size() > 0) {
            mOneList = new ArrayList<>();
            mTwoList = new ArrayList<>();
            mThreeList = new ArrayList<>();
            mOneList.addAll(list.get(0));
            if (list.size() > 1) {
                mTwoList.addAll(list.get(1));
            }
            if (list.size() > 2) {
                mThreeList.addAll(list.get(2));
            }

            if (mOneAdapter == null) {
                mOneAdapter = new MainMeOneMenuAdapter(mContext, mOneList, "1");
                mOneAdapter.setOnItemClickListener(this);
                mRecyclerView01.setAdapter(mOneAdapter);
            } else {
                mOneAdapter.setList(mOneList);
            }
            if (mTwoAdapter == null) {
                mTwoAdapter = new MainMeOneMenuAdapter(mContext, mTwoList, "2");
                mTwoAdapter.setOnItemClickListener(this);
                mRecyclerView02.setAdapter(mTwoAdapter);
            } else {
                mTwoAdapter.setList(mTwoList);
            }
            if (mThreeAdapter == null) {
                mThreeAdapter = new MainMeAdapter(mContext, mThreeList);
                mThreeAdapter.setOnItemClickListener(this);
                mRecyclerView.setAdapter(mThreeAdapter);
            } else {
                mThreeAdapter.setList(mThreeList);
            }
        }
    }

    @Override
    public void onItemClick(UserItemBean bean, int position) {
        String url = bean.getHref();
        if (TextUtils.isEmpty(url)) {
            switch (bean.getId()) {
                case 1:
                    forwardProfit();
                    break;
                case 2:
                    forwardCoin();
                    break;
                case 13:
                    forwardSetting();
                    break;
                case 18://我的直播
                    forwardLiveRecord();
                    break;
                case 19://我的视频
                    forwardMyVideo();
                    break;
                case 20:
                    forwardMyShop();
                    break;
                case 21://交易中心
                    mContext.startActivity(new Intent(mContext, TradingCenterActivity.class));
                    break;
                case 22://我的团队
                    mContext.startActivity(new Intent(mContext, MyTeamActivity.class));
                    break;
                case 23://邀请好友
                    mContext.startActivity(new Intent(mContext, InviteFriendsActivity.class));
                    break;
                case 24://实名认证
                    mContext.startActivity(new Intent(mContext, RealNameAuthenticationActivity.class));
                    break;
                case 25://俱乐部
                    mContext.startActivity(new Intent(mContext, ClubActivity.class));
                    break;
                case 26://合伙招募
                    if (area_status.equals("0")) {
                        mContext.startActivity(new Intent(mContext, AgentApplyActivity.class));
                    }else{
                        AreaAgentTypeActivity.forward(mContext,area_status);
                    }
                    break;
                case 27://道具商城
                    if (!DataSafeUtils.isEmpty(bean.getHref()))
                        WebViewActivity.forward(mContext, bean.getHref());
                    break;
                case 28://打电话
                    callPhone("0000000");
                    break;
                case 29://收款绑定
                    mContext.startActivity(new Intent(mContext, CollectionBindingActivity.class));
                    break;
            }
        } else {
            WebViewActivity.forward(mContext, url);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
                forwardmy();
                break;
            case R.id.live:
                forwardLiveRecord();
                break;
            case R.id.follow:
                forwardFollow();
                break;
            case R.id.fans:
                forwardFans();
                break;
            case R.id.avatar:
                forwardmy();
                break;
            case R.id.me_yq_center://乐器中心
                mContext.startActivity(new Intent(mContext, MusicalCenterActivity.class));
                break;
            case R.id.btn_level://我的等级
                mContext.startActivity(new Intent(mContext, MyLevelActivity.class));
                break;
            case R.id.btn_yindou://我的音豆
                mContext.startActivity(new Intent(mContext, MyYindouActivity.class));
                break;
            case R.id.btn_yinfenzhi://我的音分值
                mContext.startActivity(new Intent(mContext, MyYinfenzhiActivity.class));
                break;
            case R.id.btn_shuliandu://我的熟练度
                mContext.startActivity(new Intent(mContext, MyShulianduActivity.class));
                break;
        }
    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }

    /**
     * 道具商城
     */
    private void forwardPropmall() {
        for (UserItemBean bean : mOneList) {
            if (bean.getId() == 4) {
                if (!DataSafeUtils.isEmpty(bean.getHref()))
                    WebViewActivity.forward(mContext, bean.getHref());
                break;
            }
        }
    }


    /**
     * 房间管理
     */
    private void forwardHouse() {
        mContext.startActivity(new Intent(mContext, LiveRoomManagementActivity.class));
    }

    /**
     * 排行榜
     */
    private void forwardListView() {
        mContext.startActivity(new Intent(mContext, LeaderboardListActivity.class));
    }

    /**
     * 我的订单
     */
    private void forwardMyOrder() {
        mContext.startActivity(new Intent(mContext, MyOrderActivity.class));
    }

    /**
     * 购物车
     */
    private void forwardMyCart() {
        mContext.startActivity(new Intent(mContext, CartMainActivity.class));
    }

    /**
     * 我地址
     */
    private void forwardMyAdddress() {
        Intent intent = new Intent(mContext, MyAddressListActivity.class);
        intent.putExtra("id", "0");
        mContext.startActivity(intent);
    }

    /**
     * 编辑个人资料
     */
    private void forwardEditProfile() {
        UserHomeActivity.forward(mContext, AppConfig.getInstance().getUid());
    }

    /**
     * 我的主页
     */
    private void forwardmy() {
        UserHomeActivity.forward(mContext, AppConfig.getInstance().getUid());
    }

    /**
     * 我的关注
     */
    private void forwardFollow() {
        Intent intent = new Intent(mContext, FollowActivity.class);
        intent.putExtra(Constants.TO_UID, AppConfig.getInstance().getUid());
        mContext.startActivity(intent);
    }

    /**
     * 我的粉丝
     */
    private void forwardFans() {
        Intent intent = new Intent(mContext, FansActivity.class);
        intent.putExtra(Constants.TO_UID, AppConfig.getInstance().getUid());
        mContext.startActivity(intent);
    }

    /**
     * 直播记录
     */
    private void forwardLiveRecord() {
        LiveRecordActivity.forward(mContext, AppConfig.getInstance().getUserBean());
    }

    /**
     * 我的收益
     */
    private void forwardProfit() {
        mContext.startActivity(new Intent(mContext, MyProfitActivity.class));
    }

    /**
     * 我的钻石
     */
    private void forwardCoin() {
        mContext.startActivity(new Intent(mContext, MyCoinActivity.class));
    }

    /**
     * 设置
     */
    private void forwardSetting() {
        mContext.startActivity(new Intent(mContext, SettingActivity.class));
    }

    /**
     * 我的视频
     */
    private void forwardMyVideo() {
        mContext.startActivity(new Intent(mContext, MyVideoActivity.class));
    }

    /**
     * 我的小店
     */
    private void forwardMyShop() {
        AppConfig appConfig = AppConfig.getInstance();
        UserBean u = appConfig.getUserBean();
        String mIsStroe = u.getIs_store();// 0 是未开通  等于 1是已开通，大于1都是审核中
        if (mIsStroe != null && !mIsStroe.equals("")) {
            if (mIsStroe.equals("0")) {
                mContext.startActivity(new Intent(mContext, MyShopConditionActivity.class));
            } else if (mIsStroe.equals("1")) {
                Intent intent = new Intent(mContext, MyShopActivity.class);
                intent.putExtra("status", "0");
                intent.putExtra("store_id", AppConfig.getInstance().getUid() + "");
                mContext.startActivity(intent);
            } else {
                ToastUtil.show("正在审核中");
            }
        }


    }
}
