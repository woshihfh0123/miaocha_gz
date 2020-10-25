package com.mc.phonelive.views.foxtone;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.mc.phonelive.activity.WebViewActivity;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.MainActivity;
import com.mc.phonelive.activity.ShopsActivity;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.MyShopGoodsList;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.VideoBean;
import com.mc.phonelive.dialog.VideoShareDialogFragmentHome;
import com.mc.phonelive.event.VideoLikeEvent;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.presenter.CheckLivePresenter;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.views.AbsViewHolder;
import com.mc.phonelive.views.MyImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by cxf on 2018/11/26.
 * 视频播放外框
 */

public class VideoPlayHomeWrapViewHolder extends AbsViewHolder implements View.OnClickListener {

    private ViewGroup mVideoContainer;
    private ImageView mCover;
    private ImageView mAvatar;
    private TextView mName;
    private TextView mTitle;
    private ImageView mBtnLike;//点赞按钮
    private TextView mLikeNum;//点赞数
    private TextView mCommentNum;//评论数
    private TextView mShareNum;//分享数
    private ImageView mBtnFollow;//关注按钮
    private ImageView mBtnCart;//购物车按钮
    private ImageView mBtnReward;//打赏按钮
    private VideoBean mVideoBean;
    private Drawable mFollowDrawable;//已关注
    private Drawable mUnFollowDrawable;//未关注
    private Animation mFollowAnimation;
    private boolean mCurPageShowed;//当前页面是否可见
    private ValueAnimator mLikeAnimtor;
    private Drawable[] mLikeAnimDrawables;//点赞帧动画
    private int mLikeAnimIndex;
    private String mTag;

    private LinearLayout tj_goods_layout;//商品推荐布局
    private ImageView tj_img;//商品图片
    private TextView tj_goods_name, tj_goods_price;//商品名称和价格
    private TextView tv_address;
    private LinearLayout ll_address;
    private MyImageView miv_phc;
    private ImageView iv_zbz;
    private CheckLivePresenter mCheckLivePresenter;
    private LinearLayout ll_zbz;
    private LinearLayout btn_info; //详情按钮

    public VideoPlayHomeWrapViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_play_wrap_home;
    }

    @Override
    public void init() {
        mTag = this.toString();
        mVideoContainer = (ViewGroup) findViewById(R.id.video_container);
        mCover = (ImageView) findViewById(R.id.cover);
        iv_zbz = (ImageView) findViewById(R.id.iv_zbz);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        miv_phc = (MyImageView) findViewById(R.id.miv_phc);
        mTitle = (TextView) findViewById(R.id.title);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_zbz = (LinearLayout) findViewById(R.id.ll_zbz);
        tv_address = (TextView) findViewById(R.id.tv_address);
        mBtnLike = (ImageView) findViewById(R.id.btn_like);
        mLikeNum = (TextView) findViewById(R.id.like_num);
        mCommentNum = (TextView) findViewById(R.id.comment_num);
        mShareNum = (TextView) findViewById(R.id.share_num);
        mBtnFollow = (ImageView) findViewById(R.id.btn_follow);
        mBtnCart = (ImageView) findViewById(R.id.shop_show);
        mBtnReward = (ImageView) findViewById(R.id.shop_reward);
        mFollowDrawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_video_follow_1);
        mUnFollowDrawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_video_follow_0);
        mAvatar.setOnClickListener(this);
        mBtnFollow.setOnClickListener(this);
        mBtnLike.setOnClickListener(this);
        mBtnCart.setOnClickListener(this);
        mBtnReward.setOnClickListener(this);
        findViewById(R.id.btn_comment).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        btn_info = (LinearLayout) findViewById(R.id.btn_info);
        btn_info.setOnClickListener(this); //点击进入详情

        tj_goods_layout = (LinearLayout) findViewById(R.id.tj_goods_layout);//商品推荐布局
        tj_img = (ImageView) findViewById(R.id.tj_img);//商品图片
        tj_goods_name = (TextView) findViewById(R.id.tj_goods_name);//商品名称
        tj_goods_price = (TextView) findViewById(R.id.tj_goods_price);//商品价格
        tj_goods_layout.setOnClickListener(this);
        miv_phc.setOnClickListener(this);
        ll_zbz.setOnClickListener(this);
    }

    /**
     * 初始化点赞动画
     */
    private void initLikeAnimtor() {
        if (mLikeAnimDrawables != null && mLikeAnimDrawables.length > 0) {
            mLikeAnimtor = ValueAnimator.ofFloat(0, mLikeAnimDrawables.length);
            mLikeAnimtor.setDuration(800);
            mLikeAnimtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    int index = (int) v;
                    if (mLikeAnimIndex != index) {
                        mLikeAnimIndex = index;
                        if (mBtnLike != null && mLikeAnimDrawables != null && index < mLikeAnimDrawables.length) {
                            mBtnLike.setImageDrawable(mLikeAnimDrawables[index]);
                        }
                    }
                }
            });
        }
    }

    /**
     * 初始化关注动画
     */
    public void initFollowAnimation() {
        mFollowAnimation = new ScaleAnimation(1, 0.3f, 1, 0.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mFollowAnimation.setRepeatMode(Animation.REVERSE);
        mFollowAnimation.setRepeatCount(1);
        mFollowAnimation.setDuration(200);
        mFollowAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (mBtnFollow != null && mVideoBean != null) {
                    if (mVideoBean.getAttent() == 1) {
                        mBtnFollow.setImageDrawable(mFollowDrawable);
                    } else {
                        mBtnFollow.setImageDrawable(mUnFollowDrawable);
                    }
                }
            }
        });
    }

    public void setLikeAnimDrawables(Drawable[] drawables) {
        mLikeAnimDrawables = drawables;
    }

    public void setData(VideoBean bean, Object payload) {
        if (bean == null) {
            return;
        }
        mVideoBean = bean;
        if (Utils.isNotEmpty(mVideoBean) && Utils.isNotEmpty(mVideoBean.getUserBean()) && Utils.isNotEmpty(mVideoBean.getUserBean().getIsZbz()) && mVideoBean.getUserBean().getIsZbz().equals("1")) {
            ll_zbz.setVisibility(View.VISIBLE);
        } else {
            ll_zbz.setVisibility(View.GONE);
        }
        //设置按钮是否显示，is_ad为1显示0不显示
        if (Utils.isNotEmpty(mVideoBean) && mVideoBean.getIs_ad().equals("1")) {
            btn_info.setVisibility(View.VISIBLE);
        } else {
            btn_info.setVisibility(View.GONE);
        }

        UserBean u = mVideoBean.getUserBean();
        if (tv_address != null) {
            if (Utils.isNotEmpty(u) && Utils.isNotEmpty(u.getCity())) {
                ll_address.setVisibility(View.VISIBLE);
                tv_address.setText(u.getCity());
            } else {
                ll_address.setVisibility(View.GONE);
            }
        }
        if (payload == null) {
            if (mCover != null) {
                setCoverImage();
            }
            if (mTitle != null) {
                mTitle.setText(bean.getTitle() + "123456");
            }
            if (u != null) {
                if (mAvatar != null) {
                    ImgLoader.displayAvatar(u.getAvatar(), mAvatar);
                }
                if (mName != null) {
                    mName.setText("@" + u.getUserNiceName());
                }

            }
        }
        if (mBtnLike != null) {
            if (bean.getLike() == 1) {
                if (mLikeAnimDrawables != null && mLikeAnimDrawables.length > 0) {
                    mBtnLike.setImageDrawable(mLikeAnimDrawables[mLikeAnimDrawables.length - 1]);
                }
            } else {
                mBtnLike.setImageResource(R.mipmap.icon_video_zan_01);
            }
        }
        if (mLikeNum != null) {
            mLikeNum.setText(bean.getLikeNum());
        }
        if (mCommentNum != null) {
            mCommentNum.setText(bean.getCommentNum());
        }
        if (mShareNum != null) {
            mShareNum.setText(bean.getShareNum());
        }
        if (u != null && mBtnFollow != null) {
            String toUid = u.getId();
            if (!TextUtils.isEmpty(toUid) && !toUid.equals(AppConfig.getInstance().getUid())) {
                if (mBtnFollow.getVisibility() != View.VISIBLE) {
                    mBtnFollow.setVisibility(View.VISIBLE);
                }
                if (bean.getAttent() == 1) {
                    mBtnFollow.setImageDrawable(mFollowDrawable);
                } else {
                    mBtnFollow.setImageDrawable(mUnFollowDrawable);
                }
            } else {
                if (mBtnFollow.getVisibility() == View.VISIBLE) {
                    mBtnFollow.setVisibility(View.INVISIBLE);
                }
            }
        }

        Log.v("tags", mVideoBean.getIs_shopping() + "--issh");
        if (!DataSafeUtils.isEmpty(mVideoBean.getIs_shopping()) && mVideoBean.getIs_shopping().equals("1")) {
            mBtnCart.setVisibility(View.VISIBLE);
//            tj_goods_layout.setVisibility(View.VISIBLE);
            tj_goods_layout.setVisibility(View.GONE);
        } else {
//            mBtnCart.setVisibility(View.GONE);
            mBtnCart.setVisibility(View.VISIBLE);
            tj_goods_layout.setVisibility(View.GONE);
        }
        //获取主播推荐商品
        loadData(mVideoBean.getUid());
        MainActivity.setVideoBean(bean);
    }

    private void setCoverImage() {//横竖屏切换问题
        ImgLoader.displayDrawable(mVideoBean.getThumb(), new ImgLoader.DrawableCallback() {
            @Override
            public void callback(Drawable drawable) {
                if (mCover != null && drawable != null) {
                    float w = drawable.getIntrinsicWidth();
                    float h = drawable.getIntrinsicHeight();
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCover.getLayoutParams();
                    int targetH = 0;
                    if (w / h > 0.5625f) {//横屏  9:16=0.5625
                        targetH = (int) (mCover.getWidth() / w * h);
                    } else {
                        targetH = ViewGroup.LayoutParams.MATCH_PARENT;
                    }
                    if (targetH != params.height) {
                        params.height = targetH;
                        mCover.requestLayout();
                    }
                    mCover.setImageDrawable(drawable);
                }
            }
        });
    }

    public void addVideoView(View view) {
        if (mVideoContainer != null && view != null) {
            ViewParent parent = view.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                if (viewGroup != mVideoContainer) {
                    viewGroup.removeView(view);
                    mVideoContainer.addView(view);
                }
            } else {
                mVideoContainer.addView(view);
            }
        }
    }

    public VideoBean getVideoBean() {
        return mVideoBean;
    }


    /**
     * 获取到视频首帧回调
     */
    public void onFirstFrame() {
        if (mCover != null && mCover.getVisibility() == View.VISIBLE) {
            mCover.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 滑出屏幕
     */
    public void onPageOutWindow() {
        mCurPageShowed = false;
        if (mCover != null && mCover.getVisibility() != View.VISIBLE) {
            mCover.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 滑入屏幕
     */
    public void onPageInWindow() {
        if (mCover != null) {
            if (mCover.getVisibility() != View.VISIBLE) {
                mCover.setVisibility(View.VISIBLE);
            }
            mCover.setImageDrawable(null);
            setCoverImage();
        }
    }

    /**
     * 滑动到这一页 准备开始播放
     */
    public void onPageSelected() {
        mCurPageShowed = true;
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_info:  //点击进入详情页面
                VideoBean.ADList adList = mVideoBean.getAd_list();
                String url = "";
                if (adList != null && adList.getSlide_url() != null && adList.getSlide_url().length() > 0) {
                     url = adList.getSlide_url();
                }else{
                    url = "www.baidu.com";
                }
                WebViewActivity.forward(mContext, url);
                break;
            case R.id.miv_phc://关注

                WebViewActivity.forward(mContext, "http://hotel.yct365.net/h5/");

//                String appId = "wxfd300020414fcd39"; // 填应用AppId
//                IWXAPI api = WXAPIFactory.createWXAPI(mContext, appId);
//                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                req.userName = "gh_dd20a1b48096"; // 填小程序原始id
////                req.path = path;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//                api.sendReq(req);
                break;
            case R.id.btn_follow://关注
                clickFollow();
                break;
            case R.id.btn_comment://评论
                clickComment();
                break;
            case R.id.btn_share://分享
                clickShare();
                break;
            case R.id.btn_like://点赞
                clickLike();
                break;
            case R.id.avatar:
                clickAvatar();
                break;
            case R.id.ll_zbz:
                LiveBean liveBean = new LiveBean();//LiveBean
                liveBean.setUid(mVideoBean.getUserBean().getId());
                liveBean.setStream(mVideoBean.getUserBean().getStream());
                liveBean.setThumb(mVideoBean.getThumb());
                liveBean.setPull(mVideoBean.getUserBean().getPull());
                liveBean.setUserNiceName(mVideoBean.getUserBean().getUserNiceName());
                liveBean.setIs_shopping(mVideoBean.getUserBean().getIs_shopping());
                liveBean.setAvatar(mVideoBean.getUserBean().getAvatar());
                liveBean.setStoreid(mVideoBean.getUserBean().getStore_id());
                watchLive(liveBean, Constants.LIVE_FOLLOW, 0);
                break;
            case R.id.shop_reward://打赏
                ((MainActivity) mContext).openRedPackSendWindow();

                break;
            case R.id.shop_show://显示店铺列表商品
            case R.id.tj_goods_layout://点击商品推荐
//                showShopWindow(mVideoBean);
                //点击跳转H5商城
                if (Utils.isNotEmpty(mVideoBean.getUserBean().getUrls())) {
                    WebViewActivity.forward(mContext, mVideoBean.getUserBean().getUrls());
                } else {
                    mContext.startActivity(new Intent(mContext, ShopsActivity.class));
                }

                break;
        }
    }


    private void showShopWindow(VideoBean mVideoBean) {
        ((MainActivity) mContext).showshopWindow(mVideoBean);
    }

    /**
     * 点击头像
     */
    public void clickAvatar() {

        if (mVideoBean != null) {
//            String isZbz = mVideoBean.getIsZbz();
//            if(isZbz.equals("1")){//头像直播中点击
//                iv_zbz.setVisibility(View.VISIBLE);
//                LiveBean liveBean=new LiveBean();//LiveBean
//                liveBean.setUid(mVideoBean.getUid());
////                liveBean.setStream(mVideoBean.getStream());
//                liveBean.setThumb(mVideoBean.getThumb());
////                liveBean.setPull(mVideoBean.getPull());
//                liveBean.setUserNiceName(mVideoBean.getUserBean().getUserNiceName());
//                liveBean.setIs_shopping(mVideoBean.getIs_shopping());
//                liveBean.setAvatar(mVideoBean.getUserBean().getAvatar());
//                liveBean.setStoreid(mVideoBean.getUserBean().getStore_id());
//                watchLive(liveBean, Constants.LIVE_FOLLOW,0);
//            }else{
            iv_zbz.setVisibility(View.GONE);
            UserHomeActivity.forward(mContext, mVideoBean.getUid());
//            }
        }
    }

    public void watchLive(LiveBean liveBean, String key, int position) {
        if (mCheckLivePresenter == null) {
            mCheckLivePresenter = new CheckLivePresenter(mContext);
        }
        mCheckLivePresenter.watchLive(liveBean, key, position);
    }

    /**
     * 点赞,取消点赞
     */
    private void clickLike() {
        if (mVideoBean == null) {
            return;
        }
        HttpUtil.setVideoLike(mTag, mVideoBean.getId(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String likeNum = obj.getString("likes");
                    int like = obj.getIntValue("islike");
                    if (mVideoBean != null) {
                        mVideoBean.setLikeNum(likeNum);
                        mVideoBean.setLike(like);
                        EventBus.getDefault().post(new VideoLikeEvent(mVideoBean.getId(), like, likeNum));
                    }
                    if (mLikeNum != null) {
                        mLikeNum.setText(likeNum);
                    }
                    if (mBtnLike != null) {
                        if (like == 1) {
                            if (mLikeAnimtor == null) {
                                initLikeAnimtor();
                            }
                            mLikeAnimIndex = -1;
                            if (mLikeAnimtor != null) {
                                mLikeAnimtor.start();
                            }
                        } else {
                            mBtnLike.setImageResource(R.mipmap.icon_video_zan_01);
                        }
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    /**
     * 点击关注按钮
     */
    private void clickFollow() {
        Log.e("FFFFF:", "-------");
        if (mVideoBean == null) {
            return;
        }
        final UserBean u = mVideoBean.getUserBean();
        if (u == null) {
            return;
        }
        HttpUtil.setGz(u.getId(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String isattent = obj.getString("isattent");
                    if (Utils.isNotEmpty(isattent) && isattent.equals("1")) {//拉黑了
                        mBtnFollow.setImageDrawable(mFollowDrawable);
                    } else {
                        mBtnFollow.setImageDrawable(mUnFollowDrawable);
                    }

                }
            }
        });
//        HttpUtil.setAttention(mTag, Constants.FOLLOW_FROM_VIDEO_PLAY, u.getId(), new CommonCallback<Integer>() {
//            @Override
//            public void callback(Integer attent) {
//                mVideoBean.setAttent(attent);
//                if (mCurPageShowed) {
//                    if (mFollowAnimation == null) {
//                        initFollowAnimation();
//                    }
//                    mBtnFollow.startAnimation(mFollowAnimation);
//                } else {
//                    if (attent == 1) {
//                        mBtnFollow.setImageDrawable(mFollowDrawable);
//                    } else {
//                        mBtnFollow.setImageDrawable(mUnFollowDrawable);
//                    }
//                }
//            }
//        });
    }

    /**
     * 点击评论按钮
     */
    private void clickComment() {
        ((MainActivity) mContext).openCommentWindow(mVideoBean);
    }

    /**
     * 点击分享按钮
     */
    private void clickShare() {
        if (mVideoBean == null) {
            return;
        }
        VideoShareDialogFragmentHome fragment = new VideoShareDialogFragmentHome();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.VIDEO_BEAN, mVideoBean);
        fragment.setArguments(bundle);
        fragment.show(((MainActivity) mContext).getSupportFragmentManager(), "VideoShareDialogFragmentHome");
    }

    public void release() {
        HttpUtil.cancel(mTag);
        if (mLikeAnimtor != null) {
            mLikeAnimtor.cancel();
        }
        if (mBtnFollow != null && mFollowAnimation != null) {
            mBtnFollow.clearAnimation();
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

                        if (!DataSafeUtils.isEmpty(list.get(0).getImg_list()[0])) {
                            Glide.with(mContext).load(list.get(0).getImg_list()[0]).into(tj_img);
                        }
                        if (!DataSafeUtils.isEmpty(list.get(0).getGoods_name())) {
                            tj_goods_name.setText(list.get(0).getGoods_name() + "");
                        }
                        if (!DataSafeUtils.isEmpty(list.get(0).getPrice())) {
                            tj_goods_price.setText("￥" + list.get(0).getPrice());
                        }

                    }
                }
            }

            @Override
            public void onFinish() {
            }
        });
    }

}
