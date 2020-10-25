package com.mc.phonelive.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
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
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.UserItemBean;
import com.mc.phonelive.fragment.MusicFragment01;
import com.mc.phonelive.fragment.MusicFragment02;
import com.mc.phonelive.interfaces.MainAppBarLayoutListener;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 我的 个人中心
 */

public class MainMyViewHolder extends AbsMainChildViewHolder implements OnItemClickListener<UserItemBean>, View.OnClickListener {
    private static final int PRC_PHOTO_PREVIEW = 1;
    String[] mTitle = null;
    private List<Fragment> fs=new ArrayList<>();
    private String shop_id="";
    private String lat="";
    private String lng="";
    private String is_fav="";


    private RelativeLayout mParallax;
    private ImageView mIv_bg;
    private ImageView mIv_pic;
    private TextView mTv_username;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mRefreshLayout;
    private android.support.design.widget.CoordinatorLayout mCly;
    private android.support.design.widget.AppBarLayout mAbl;
    private LinearLayout mHead_layout;
    private MyImageView mMiv;
    private TextView mTv_gz;
    private TextView mTv_nick_name;
    private TextView mTv_id;
    private TextView mTv_qm;
    private TextView mTv_hz_fs;
    private android.support.v7.widget.Toolbar mToolbar1;
    private com.flyco.tablayout.SlidingTabLayout mTablayout;
    private android.support.v4.view.ViewPager mTab_viewpager;
    private RelativeLayout mRl;
    private RelativeLayout mMrl_back;
    private AlphaTextColorView mTv_center_title1;
    private RelativeLayout mRl_255;
    private RelativeLayout mMrl_back_255;
    private RelativeLayout mMrl_right_255;



    private int mOffset = 0;
    private int mScrollY = 0;
    private String userId="";


    public MainMyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_my;
    }

    @Override
    public void init() {
        super.init();

        mParallax = (RelativeLayout) findViewById(R.id.parallax);
        mIv_bg = (ImageView) findViewById(R.id.iv_bg);
        mIv_pic = (ImageView) findViewById(R.id.iv_pic);
        mTv_username = (TextView) findViewById(R.id.tv_username);
        mRefreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mCly = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.cly);
        mAbl = (android.support.design.widget.AppBarLayout) findViewById(R.id.abl);
        mHead_layout = (LinearLayout) findViewById(R.id.head_layout);
        mMiv = (MyImageView) findViewById(R.id.miv);
        mTv_gz = (TextView) findViewById(R.id.tv_gz);
        mTv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        mTv_id = (TextView) findViewById(R.id.tv_id);
        mTv_qm = (TextView) findViewById(R.id.tv_qm);
        mTv_hz_fs = (TextView) findViewById(R.id.tv_hz_fs);
        mToolbar1 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        mTablayout = (com.flyco.tablayout.SlidingTabLayout) findViewById(R.id.tablayout);
        mTab_viewpager = (android.support.v4.view.ViewPager)  findViewById(R.id.tab_viewpager);
        mRl = (RelativeLayout) findViewById(R.id.rl);
        mMrl_back = (RelativeLayout) findViewById(R.id.mrl_back);
        mTv_center_title1 = (AlphaTextColorView) findViewById(R.id.tv_center_title1);
        mRl_255 = (RelativeLayout) findViewById(R.id.rl_255);
        mMrl_back_255 = (RelativeLayout) findViewById(R.id.mrl_back_255);
        mMrl_right_255 = (RelativeLayout) findViewById(R.id.mrl_right_255);
//        shop_id=getIntent().getStringExtra("shop_id");
        MusicFragment01 fragment01 =  MusicFragment01.newInstance();
        MusicFragment02 fragment02 = MusicFragment02.newInstance();



//        fs.add(fragment01);
//        fs.add(fragment02);


        mRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                mParallax.setTranslationY(mOffset - mScrollY);
//                toolbar.setAlpha(1 - Math.min(percent, 1));       }
            }
        });
        setAnyBarAlpha(0);
        mAbl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int y) {
                Logger.e("sss:"+y);
                if (y <0) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
//                    int headerHeight = mBanner.getHeight();
                    int scrollDistance = Math.min(-y, 350);
                    int statusAlpha = (int) ((float) scrollDistance / (float) 350 * 255F);
                    setAnyBarAlpha(statusAlpha);
                    Logger.e("statusAlpha:"+statusAlpha);
                }else{
                    setAnyBarAlpha(0);
                }
            }
        });
        mMrl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EventBusUtil.postEvent(new EventBean("change_to_video_fragment_from_back"));
            }
        });
        mTv_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
    private void setAnyBarAlpha(final int alpha) {
        mRl.getBackground().mutate().setAlpha(alpha);
        mTv_center_title1.setAlphaPercent(alpha);
    }
    @Override
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
    }

    @Override
    public void loadData() {

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
//                    if (area_status.equals("0")) {
//                        mContext.startActivity(new Intent(mContext, AgentApplyActivity.class));
//                    }else{
//                        AreaAgentTypeActivity.forward(mContext,area_status);
//                    }
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
