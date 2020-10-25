package com.mc.phonelive.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.ChatRoomActivity;
import com.mc.phonelive.activity.EditProfileActivity;
import com.mc.phonelive.activity.FansActivity;
import com.mc.phonelive.activity.FollowActivity;
import com.mc.phonelive.activity.LiveContributeActivity;
import com.mc.phonelive.activity.LiveGuardListActivity;
import com.mc.phonelive.activity.MyShopActivity;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.activity.shop.FansAndGzActivity;
import com.mc.phonelive.adapter.ViewPagerAdapter;
import com.mc.phonelive.bean.ImpressBean;
import com.mc.phonelive.bean.LevelBean;
import com.mc.phonelive.bean.SearchUserBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.UserHomeConBean;
import com.mc.phonelive.bean.UserItemBean;
import com.mc.phonelive.custom.MyViewPager;
import com.mc.phonelive.custom.ViewPagerIndicator;
import com.mc.phonelive.dialog.DianZanDialog;
import com.mc.phonelive.dialog.LiveShareDialogFragment;
import com.mc.phonelive.event.FollowEvent;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.CommonCallback;
import com.mc.phonelive.interfaces.LifeCycleAdapter;
import com.mc.phonelive.interfaces.LifeCycleListener;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.presenter.UserHomeSharePresenter;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.utils.IconUtil;
import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.StringUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 新个人中心
 */

public class PersonMyViewHolder extends AbsMainChildViewHolder implements OnItemClickListener<UserItemBean>, View.OnClickListener, AppBarLayout.OnOffsetChangedListener, LiveShareDialogFragment.ActionListener  {
    private VideoHomeViewHolderBackUp mVideoHomeViewHolder;
    private CenterDtSb mLiveRecordViewHolder;
//    private VideoLike mDtViewHolder;
    private VideoHomeViewLike mDtViewHolder;
    private List<LifeCycleListener> mLifeCycleListeners;
    private LayoutInflater mInflater;
    private AppBarLayout mAppBarLayout;
    private ImageView mAvatarBg;
    private MyImageView mAvatar;
    private TextView mName;
    private ImageView mSex;
    private ImageView btn_Edit;
    private ImageView mLevelAnchor;
    private ImageView mLevel;
    private TextView mID;
    private TextView mBtnFans;
    private TextView btn_dz;
    private TextView mBtnFollow;
    private TextView mBtnFollow2;
    private TextView mSign;
    private LinearLayout mImpressGroup;
    private View mNoImpressTip;
    private TextView mVotesName;
    private LinearLayout mConGroup;
    private LinearLayout mGuardGroup;
    private LinearLayout guard_shop_wrap;
    private TextView mTitleView;
    private ImageView mBtnBack;
    private ImageView mBtnShare;
    private TextView mBtnBlack;
    private MyViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private String mToUid;
    private boolean mSelf;
    private float mRate;
    private int[] mWhiteColorArgb;
    private int[] mBlackColorArgb;
    private View.OnClickListener mAddImpressOnClickListener;
    private UserHomeSharePresenter mUserHomeSharePresenter;
    private SearchUserBean mSearchUserBean;
    private String mVideoString;
    private String mLiveString;
    private String mDtString;
    private boolean mLoadLiveRecord;
    private int mVideoCount;
    private String fansNum="";
    private String gzNum="";
    private String toName="";
    private TextView btn_famly;
    private String lives="";
    private TextView tv_address,tv_sex;

    //  mContext.startActivity(new Intent(mContext, SettingActivity.class));
    public PersonMyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_ps_main;
    }

    @Override
    public void init() {
        mInflater = LayoutInflater.from(mContext);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mAvatarBg = (ImageView) findViewById(R.id.bg_avatar);
        mAvatar = (MyImageView) findViewById(R.id.avatar);
        btn_Edit = (ImageView) findViewById(R.id.btn_Edit);
        mName = (TextView) findViewById(R.id.name);
        btn_dz = (TextView) findViewById(R.id.btn_dz);
        mSex = (ImageView) findViewById(R.id.sex);
        mLevelAnchor = (ImageView) findViewById(R.id.level_anchor);
        mLevel = (ImageView) findViewById(R.id.level);
        mID = (TextView) findViewById(R.id.id_val);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        btn_famly = (TextView) findViewById(R.id.btn_famly);
        mBtnFans = (TextView) findViewById(R.id.btn_fans);
        mBtnFollow = (TextView) findViewById(R.id.btn_follow);
        mBtnFollow2 = (TextView) findViewById(R.id.btn_follow_2);
        mBtnBlack = (TextView) findViewById(R.id.btn_black);
        mSign = (TextView) findViewById(R.id.sign);
        mImpressGroup = (LinearLayout) findViewById(R.id.impress_group);
        mNoImpressTip = findViewById(R.id.no_impress_tip);
        mVotesName = (TextView) findViewById(R.id.votes_name);
        mConGroup = (LinearLayout) findViewById(R.id.con_group);
        mGuardGroup = (LinearLayout) findViewById(R.id.guard_group);
        mTitleView = (TextView) findViewById(R.id.titleView);
        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        guard_shop_wrap = (LinearLayout) findViewById(R.id.guard_shop_wrap);
        mBtnShare = (ImageView) findViewById(R.id.btn_share);
        mViewPager = (MyViewPager) findViewById(R.id.viewPager);
        mBtnFollow2.setOnClickListener(this);
        btn_famly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().setBooleanValue("setMe",false);
                EventBusUtil.postEvent(new EventBean("show_jia_ren_from_click"));
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                EventBusUtil.postEvent(new EventBean("clise_video_from_qh"));
                if (position == 1) {
                    if (mLiveRecordViewHolder != null) {
                        mLiveRecordViewHolder.loadData();
                    }
                }else if(position==2){
                    if (mDtViewHolder != null) {
                        mDtViewHolder.loadData();
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVideoHomeViewHolder = new VideoHomeViewHolderBackUp(mContext, mViewPager, mToUid);
        mVideoHomeViewHolder.setRefreshEnable(false);
        mVideoHomeViewHolder.setActionListener(new VideoHomeViewHolderBackUp.ActionListener() {
            @Override
            public void onVideoDelete(int deleteCount) {
                mVideoCount -= deleteCount;
                if (mVideoCount < 0) {
                    mVideoCount = 0;
                }
                if (mIndicator != null) {
                    mIndicator.setTitleText(0, mVideoString + " " + mVideoCount);
                }
            }
        });
        mLiveRecordViewHolder = new CenterDtSb(mContext, mViewPager,mToUid);
        mDtViewHolder = new VideoHomeViewLike(mContext, mViewPager,mToUid);
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_USER_HOME);
                HttpUtil.cancel(HttpConsts.SET_ATTENTION);
                HttpUtil.cancel(HttpConsts.SET_BLACK);
            }
        };
//        mLifeCycleListeners = new ArrayList<>();
//        mLifeCycleListeners.add(mLifeCycleListener);
//        mLifeCycleListeners.add(mVideoHomeViewHolder.getLifeCycleListener());
//        mLifeCycleListeners.add(mLiveRecordViewHolder.getLifeCycleListener());
//        mLifeCycleListeners.add(mDtViewHolder.getLifeCycleListener());
        List<View> viewList = new ArrayList<>();
        viewList.add(mVideoHomeViewHolder.getContentView());
        viewList.add(mLiveRecordViewHolder.getContentView());
        viewList.add(mDtViewHolder.getContentView());
        mViewPager.setAdapter(new ViewPagerAdapter(viewList));
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mIndicator.setVisibleChildCount(3);
        mVideoString = "作品";
        mDtString = "动态";
        mLiveString = "喜欢";
        mIndicator.setTitles(new String[]{mVideoString, mLiveString,mDtString});
        mIndicator.setViewPager(mViewPager);
        mBtnShare.setOnClickListener(this);
        mBtnFans.setOnClickListener(this);
        mBtnFollow.setOnClickListener(this);//   mContext.startActivity(new Intent(mContext, EditProfileActivity.class));
        mBtnBlack.setOnClickListener(this);
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UserHomeActivityMenu.forward(mContext, AppConfig.getInstance().getUid());
//                mContext.startActivity(new Intent(mContext,SlideTestActivity.class));

                EventBusUtil.postEvent(new EventBean("SHOW_MENU_FROM_CLICK"));
            }
        });

        findViewById(R.id.btn_pri_msg).setOnClickListener(this);
        findViewById(R.id.con_group_wrap).setOnClickListener(this);
        findViewById(R.id.guard_group_wrap).setOnClickListener(this);
        findViewById(R.id.guard_shop_wrap).setOnClickListener(this);
        mWhiteColorArgb = getColorArgb(0xffffffff);
        mBlackColorArgb = getColorArgb(0xff323232);
        mAddImpressOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusUtil.postEvent(new EventBean("clise_video_from_qh"));
                addImpress();
            }
        };
        mUserHomeSharePresenter = new UserHomeSharePresenter(mContext);

    }
//    public List<LifeCycleListener> getLifeCycleListenerList() {
//        return mLifeCycleListeners;
//    }
    public LifeCycleListener getLifeCycleListener() {
        return mLifeCycleListener;
    }
    @Override
    public void loadData() {

    mToUid=AppConfig.getInstance().getUid();
        Log.e("AAA:",mToUid);
        HttpUtil.getUserHome(mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    SearchUserBean userBean = JSON.toJavaObject(obj.getJSONObject("userinfo"), SearchUserBean.class);
                    mSearchUserBean = userBean;
                    if (mViewPager != null) {
                        mViewPager.setCanScroll(true);
                    }
                    if (mVideoHomeViewHolder != null) {
                        mVideoHomeViewHolder.loadData("3");
                    }
                    String avatar = userBean.getAvatar();
                    if(Utils.isNotEmpty(avatar)){
                        GlideUtils.loadImage(mContext,avatar,mAvatar);
                    }
                     toName = userBean.getUserNiceName();
                    mName.setText(toName);
                    mTitleView.setText(toName);
                    mSex.setImageResource(IconUtil.getSexIcon(Integer.parseInt(userBean.getSex())));
                    tv_sex.setText(Integer.parseInt(userBean.getSex())==1?"男":"女");
                    AppConfig appConfig = AppConfig.getInstance();
                    AppConfig.getInstance().getUserBean().setMobile(userBean.getMobile());
                    AppConfig.getInstance().getUserBean().setSex(userBean.getSex());
                    LevelBean levelAnchor = appConfig.getAnchorLevel(userBean.getLevelAnchor());
                    if (levelAnchor != null)
                        ImgLoader.display(levelAnchor.getThumb(), mLevelAnchor);
                    LevelBean level = appConfig.getLevel(userBean.getLevel());
                    ImgLoader.display(level.getThumb(), mLevel);
                    mID.setText(userBean.getLiangNameTip());
                     fansNum = StringUtil.toWan(userBean.getFans());
                     gzNum=StringUtil.toWan(userBean.getFollows());
                    lives = StringUtil.toWan(userBean.getLives());
                    tv_address.setText(userBean.getCity());
                    btn_dz.setText(lives+"点赞");
                    mBtnFans.setText(fansNum + " " + WordUtil.getString(R.string.fans));
                    mBtnFollow.setText( gzNum+ " " + WordUtil.getString(R.string.follow));
                    mSign.setText(userBean.getSignature());
//                    mBtnFollow2.setText(obj.getIntValue("isattention") == 1 ? R.string.following : R.string.follow);
                    mBtnBlack.setText(obj.getIntValue("isblack") == 1 ? R.string.black_ing : R.string.black);
                    mVideoCount = obj.getIntValue("videonums");
                    String work_num = userBean.getWork_num();
                    String like_num = userBean.getLike_num();
                    mIndicator.setTitleText(0, mVideoString + " " + work_num);
                    mIndicator.setTitleText(1, mDtString + " " + work_num);
                    mIndicator.setTitleText(2, mLiveString + " " + like_num);
//                    showImpress(obj.getString("label"));
                    if(Utils.isNotEmpty(userBean.getLabel())){
                        refreshImpress();
//                        showImpress(JSON.toJSONString(userBean.getLabel()));
                    }
                    mVotesName.setText("茶票"+ WordUtil.getString(R.string.live_user_home_con));
                    mUserHomeSharePresenter.setToUid(mToUid).setToName(toName).setAvatarThumb(userBean.getAvatarThumb()).setFansNum(fansNum);
//                    showContribute(obj.getString("contribute"));
//                    showGuardList(obj.getString("guardlist"));
                    String is_store=userBean.getIs_store();
                    if (is_store.equals("1")) {
                        guard_shop_wrap.setVisibility(View.VISIBLE);
                    } else {
                        guard_shop_wrap.setVisibility(View.INVISIBLE);
                    }
                    if (userBean.getId().equals(AppConfig.getInstance().getUid())) {
                        btn_Edit.setVisibility(View.VISIBLE);
                    } else {
                        btn_Edit.setVisibility(View.GONE);
                    }


                } else {
                    ToastUtil.show(msg);
                }
            }
        });
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadOneyDis();
                }
            }, 3000);
        }catch (Exception e){

        }
        btn_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info="“"+mName.getText().toString()+"”共获得"+lives+"个赞";
                new DianZanDialog(mContext,info,"").show();
            }
        });
    }

    boolean isLoadPreson = false;
    private void loadOneyDis() {
        if(SpUtil.getInstance().getBooleanValue("setMe")){
            HttpUtil.getUserHome(mToUid, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0 && info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        SearchUserBean userBean = JSON.toJavaObject(obj.getJSONObject("userinfo"), SearchUserBean.class);
                        mSearchUserBean = userBean;
                        if (mViewPager != null) {
                            mViewPager.setCanScroll(true);
                        }
                        if (mVideoHomeViewHolder != null) {
                            if(!isLoadPreson){
                                mVideoHomeViewHolder.loadData("4");
                            }

                            isLoadPreson = true;
                        }
                        String avatar = userBean.getAvatar();
                        if(Utils.isNotEmpty(avatar)&&Utils.isNotEmpty(mContext)){
                            try{
                                GlideUtils.loadImage(mContext,avatar,mAvatar);
                            }catch (Exception e){

                            }

                        }
                        toName = userBean.getUserNiceName();
                        mName.setText(toName);
                        mTitleView.setText(toName);
                        mSex.setImageResource(IconUtil.getSexIcon(Integer.parseInt(userBean.getSex())));
                        tv_sex.setText(Integer.parseInt(userBean.getSex())==1?"男":"女");
                        AppConfig.getInstance().getUserBean().setSex(userBean.getSex());
                        AppConfig appConfig = AppConfig.getInstance();
                        LevelBean levelAnchor = appConfig.getAnchorLevel(userBean.getLevelAnchor());
                        if (levelAnchor != null)
                            ImgLoader.display(levelAnchor.getThumb(), mLevelAnchor);
                        LevelBean level = appConfig.getLevel(userBean.getLevel());
                        ImgLoader.display(level.getThumb(), mLevel);
                        mID.setText(userBean.getLiangNameTip());
                        fansNum = StringUtil.toWan(userBean.getFans());
                        gzNum=StringUtil.toWan(userBean.getFollows());
                        lives = StringUtil.toWan(userBean.getLives());
                        tv_address.setText(userBean.getCity());
                        btn_dz.setText(lives+"点赞");
                        mBtnFans.setText(fansNum + " " + WordUtil.getString(R.string.fans));
                        mBtnFollow.setText( gzNum+ " " + WordUtil.getString(R.string.follow));
                        mSign.setText(userBean.getSignature());
//                    mBtnFollow2.setText(obj.getIntValue("isattention") == 1 ? R.string.following : R.string.follow);
                        mBtnBlack.setText(obj.getIntValue("isblack") == 1 ? R.string.black_ing : R.string.black);
                        mVideoCount = obj.getIntValue("videonums");
                        String work_num = userBean.getWork_num();
                        String like_num = userBean.getLike_num();
                        mIndicator.setTitleText(0, mVideoString + " " + work_num);
                        mIndicator.setTitleText(1, mDtString + " " + work_num);
                        mIndicator.setTitleText(2, mLiveString + " " + like_num);
//                    showImpress(obj.getString("label"));
                        if(Utils.isNotEmpty(userBean.getLabel())){
                            refreshImpress();
//                        showImpress(JSON.toJSONString(userBean.getLabel()));
                        }
                        mVotesName.setText("茶票"+ WordUtil.getString(R.string.live_user_home_con));
                        mUserHomeSharePresenter.setToUid(mToUid).setToName(toName).setAvatarThumb(userBean.getAvatarThumb()).setFansNum(fansNum);
                        String is_store=userBean.getIs_store();
                        if (is_store.equals("1")) {
                            guard_shop_wrap.setVisibility(View.VISIBLE);
                        } else {
                            guard_shop_wrap.setVisibility(View.INVISIBLE);
                        }
                        if (userBean.getId().equals(AppConfig.getInstance().getUid())) {
                            btn_Edit.setVisibility(View.VISIBLE);
                        } else {
                            btn_Edit.setVisibility(View.GONE);
                        }
                    }
                        if (mLiveRecordViewHolder != null) {
                            mLiveRecordViewHolder.loadData();
                        }
                        if (mDtViewHolder != null) {
                            mDtViewHolder.loadData();
                        }
                }
            });
        }
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadOneyDis();
                }
            }, 3000);
        }catch (Exception e){

        }

    }


    @Override
    public void onItemClick(UserItemBean bean, int position) {
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
     * 显示印象
     */
    private void showImpress(String impressJson) {
        List<ImpressBean> list = JSON.parseArray(impressJson, ImpressBean.class);
        if (list.size() > 3) {
            list = list.subList(0, 3);
        }
        if (!mSelf) {
//            ImpressBean lastBean = new ImpressBean();
//            lastBean.setName("+ " + WordUtil.getString(R.string.impress_add));
//            lastBean.setColor("#ffdd00");
//            list.add(lastBean);
        } else {
            if (list.size() == 0) {
                mNoImpressTip.setVisibility(View.VISIBLE);
                return;
            }
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            TextView myTextView = (TextView) mInflater.inflate(R.layout.view_impress_item_5, mImpressGroup, false);

            ImpressBean bean = list.get(i);
            bean.setColor("#f5f5f5");
            bean.setCheck(0);
            if (mSelf) {
                bean.setCheck(1);
            } else {
                if (i == size - 1) {
                    myTextView.setOnClickListener(mAddImpressOnClickListener);
                } else {
                    bean.setCheck(1);
                }
            }
//            myTextView.setBean(bean);
            myTextView.setText(bean.getName());
            mImpressGroup.addView(myTextView);
        }
    }


    /**
     * 显示贡献榜
     */
    private void showContribute(String conJson) {
        List<UserHomeConBean> list = JSON.parseArray(conJson, UserHomeConBean.class);
        if (list.size() == 0) {
            return;
        }
        if (list.size() > 3) {
            list = list.subList(0, 3);
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            ImageView imageView = (ImageView) mInflater.inflate(R.layout.view_user_home_con, mConGroup, false);
            ImgLoader.display(list.get(i).getAvatar(), imageView);
            mConGroup.addView(imageView);
        }
    }

    /**
     * 显示守护榜
     */
    private void showGuardList(String guardJson) {
        List<UserBean> list = JSON.parseArray(guardJson, UserBean.class);
        if (list.size() == 0) {
            return;
        }
        if (list.size() > 3) {
            list = list.subList(0, 3);
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            ImageView imageView = (ImageView) mInflater.inflate(R.layout.view_user_home_con, mGuardGroup, false);
            ImgLoader.display(list.get(i).getAvatar(), imageView);
            mGuardGroup.addView(imageView);
        }
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float totalScrollRange = appBarLayout.getTotalScrollRange();
        float rate = -1 * verticalOffset / totalScrollRange * 2;
        if (rate >= 1) {
            rate = 1;
        }
        if (mRate != rate) {
            mRate = rate;
            mTitleView.setAlpha(rate);
            int a = (int) (mWhiteColorArgb[0] * (1 - rate) + mBlackColorArgb[0] * rate);
            int r = (int) (mWhiteColorArgb[1] * (1 - rate) + mBlackColorArgb[1] * rate);
            int g = (int) (mWhiteColorArgb[2] * (1 - rate) + mBlackColorArgb[2] * rate);
            int b = (int) (mWhiteColorArgb[3] * (1 - rate) + mBlackColorArgb[3] * rate);
            int color = Color.argb(a, r, g, b);
//            mBtnBack.setColorFilter(color);
//            btn_Edit.setColorFilter(color);
//            mBtnShare.setColorFilter(color);
        }
    }

    /**
     * 获取颜色的argb
     */
    private int[] getColorArgb(int color) {
        return new int[]{Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color)};
    }


    @Override
    public void onClick(View v) {
        EventBusUtil.postEvent(new EventBean("clise_video_from_qh"));
        switch (v.getId()) {
            case R.id.btn_back:
                back();
                break;
            case R.id.btn_Edit:
//                mContext.startActivity(new Intent(mContext, EditProfileActivity.class));
                break;
            case R.id.btn_share:
                share();
                break;
            case R.id.btn_fans:
//                forwardFans();
                Intent intent1=new Intent(new Intent(mContext, FansAndGzActivity.class));
                intent1.putExtra("pos","1");
                intent1.putExtra("gznum",gzNum);
                intent1.putExtra("fsnum",fansNum);
                intent1.putExtra("name",toName);
                mContext.startActivity(intent1);
//                mContext.startActivity(new Intent(mContext, FansAndGzActivity.class));
                break;
            case R.id.btn_follow:
//                forwardFollow();
                Intent intent2=new Intent(new Intent(mContext, FansAndGzActivity.class));
                intent2.putExtra("pos","0");
                intent2.putExtra("gznum",gzNum);
                intent2.putExtra("fsnum",fansNum);
                intent2.putExtra("name",toName);
                mContext.startActivity(intent2);
                break;
            case R.id.btn_follow_2:
                mContext.startActivity(new Intent(mContext, EditProfileActivity.class));
//                mContext.startActivity(new Intent(mContext, OrderMyActivity.class));//我的订单
//                follow();
                break;
            case R.id.con_group_wrap:
                WebViewActivity.forward(mContext, AppConfig.HOST + "/Appapi/Contribute/index.html");
//                forwardContribute();
                break;
            case R.id.guard_group_wrap:
                forwardGuardList();
                break;
            case R.id.guard_shop_wrap://他的小店
                Intent intent = new Intent(mContext, MyShopActivity.class);
                if (mSelf) {
                    intent.putExtra("status", "0");
                } else {
                    intent.putExtra("status", "1");
                }
                intent.putExtra("store_id", mSearchUserBean.getStore_id() + "");
                mContext.startActivity(intent);
                break;
            case R.id.btn_pri_msg:
                forwardMsg();
                break;
            case R.id.btn_black:

                setBlack();
                break;
        }
    }

    private void back() {
        EventBusUtil.postEvent(new EventBean("clise_video_from_qh"));
        if (mContext instanceof UserHomeActivity) {
            ((UserHomeActivity) mContext).onBackPressed();
        }
    }

    /**
     * 关注
     */
    private void follow() {
        HttpUtil.setAttention(Constants.FOLLOW_FROM_HOME, mToUid, new CommonCallback<Integer>() {
            @Override
            public void callback(Integer isAttention) {
                onAttention(isAttention);
            }
        });
    }

    /**
     * 私信
     */
    private void forwardMsg() {
        if (mSearchUserBean != null) {
            ChatRoomActivity.forward(mContext, mSearchUserBean, mSearchUserBean.getAttention() == 1);
        }
    }

    private void onAttention(int isAttention) {
        if (mBtnFollow2 != null) {
            mBtnFollow2.setText(isAttention == 1 ? R.string.following : R.string.follow);
//            if (isAttention==1){
//                guard_shop_wrap.setVisibility(View.VISIBLE);
//            }else{
//                guard_shop_wrap.setVisibility(View.GONE);
//            }
        }
        if (mBtnBlack != null) {
            if (isAttention == 1) {
                mBtnBlack.setText(R.string.black);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (e.getToUid().equals(mToUid)) {
            int isAttention = e.getIsAttention();
            if (mSearchUserBean != null) {
                mSearchUserBean.setAttention(isAttention);
            }
            onAttention(isAttention);
        }
    }

    /**
     * 添加印象
     */
    private void addImpress() {
        if (mContext instanceof UserHomeActivity) {
            ((UserHomeActivity) mContext).addImpress(mToUid);
        }
    }

    /**
     * 刷新印象
     */
    public void refreshImpress() {
        HttpUtil.getUserHome(mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    SearchUserBean userBean = JSON.toJavaObject(obj.getJSONObject("userinfo"), SearchUserBean.class);
                    if (mImpressGroup != null) {
                        mImpressGroup.removeAllViews();
                    }
                    if(Utils.isNotEmpty(userBean.getLabel())){
                        showImpress(JSON.toJSONString(userBean.getLabel()));
                    }
                }
            }
        });
    }

    /**
     * 查看贡献榜
     */
    private void forwardContribute() {
        Intent intent = new Intent(mContext, LiveContributeActivity.class);
        intent.putExtra(Constants.TO_UID, mToUid);
        mContext.startActivity(intent);
    }

    /**
     * 查看守护榜
     */
    private void forwardGuardList() {
        LiveGuardListActivity.forward(mContext, mToUid);
    }

    /**
     * 前往TA的关注
     */
    private void forwardFollow() {
        Intent intent = new Intent(mContext, FollowActivity.class);
        intent.putExtra(Constants.TO_UID, mToUid);
        mContext.startActivity(intent);
    }
    @Subscribe
    public void getEvent(EventBean event){
        Log.e("KKKKK:","AAA");
        switch(event.getCode()){
            case "refresh_info_person":
                loadData();

                break;
            default:

                break;
        }
    };
    /**
     * 前往TA的粉丝
     */
    private void forwardFans() {
        Intent intent = new Intent(mContext, FansActivity.class);
        intent.putExtra(Constants.TO_UID, mToUid);
        mContext.startActivity(intent);
    }

    /**
     * 拉黑，解除拉黑
     */
    private void setBlack() {
        HttpUtil.setBlack(mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    boolean isblack = JSON.parseObject(info[0]).getIntValue("isblack") == 1;
                    mBtnBlack.setText(isblack ? R.string.black_ing : R.string.black);
                    if (isblack) {
//                        mBtnFollow2.setText(R.string.follow);
                        EventBus.getDefault().post(new FollowEvent(Constants.FOLLOW_FROM_HOME, mToUid, 0));
                    }
                }
            }
        });
    }

    /**
     * 分享
     */
    private void share() {
        LiveShareDialogFragment fragment = new LiveShareDialogFragment();
        fragment.setActionListener(this);
        fragment.show(((AbsActivity) mContext).getSupportFragmentManager(), "LiveShareDialogFragment");
    }


    @Override
    public void onItemClick(String type) {
        EventBusUtil.postEvent(new EventBean("clise_video_from_qh"));
        if (Constants.LINK.equals(type)) {
            copyLink();
        } else {
            shareHomePage(type);
        }
    }

    /**
     * 复制页面链接
     */
    private void copyLink() {
        if (mUserHomeSharePresenter != null) {
            mUserHomeSharePresenter.copyLink();
        }
    }


    /**
     * 分享页面链接
     */
    private void shareHomePage(String type) {
        if (mUserHomeSharePresenter != null) {
            mUserHomeSharePresenter.shareHomePage(type);
        }
    }

//    @Override
//    public void release() {
//        super.release();
//        EventBus.getDefault().unregister(this);
//        if (mUserHomeSharePresenter != null) {
//            mUserHomeSharePresenter.release();
//        }
//        mUserHomeSharePresenter = null;
//        if (mVideoHomeViewHolder != null) {
//            mVideoHomeViewHolder.release();
//        }
//        mVideoHomeViewHolder = null;
//    }

}
