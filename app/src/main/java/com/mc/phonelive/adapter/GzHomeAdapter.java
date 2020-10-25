package com.mc.phonelive.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.views.DouYinHeaderView;
import com.mc.phonelive.views.MyImageView;

/**
 * Created by Administrator on 2018/8/7.
 */

public class GzHomeAdapter extends BaseQuickAdapter<LiveBean, BaseViewHolder> {
    //动画的持续时间
    private int mShortAnimationDuration = 1000;

    //当前正在进行的动画
    private AnimatorSet mCurrentAnimation;
    public GzHomeAdapter() {
        super(R.layout.item_gz_home);
    }
    @Override
    protected void convert(BaseViewHolder helper, LiveBean item) {
        DouYinHeaderView img=helper.getView(R.id.gz_img);
        helper.setText(R.id.tv_name,item.getUserNiceName());
        MyImageView miv=helper.getView(R.id.miv);
        GlideUtils.loadImage(mContext,item.getAvatar(),miv);

//        RelativeLayout ctn=helper.getView(R.id.container);
//        MyImageView miv_larger=helper.getView(R.id.miv_larger);
//        MyImageView miv_sm=helper.getView(R.id.miv_sm);
//        zoomImageFromThumb(ctn,miv_larger,miv_sm,R.drawable.gwc_01);
//        TextView tvname=helper.getView(R.id.tv_name);
//        tvname.setText(item.getTime());
    }
    private void zoomImageFromThumb(RelativeLayout container,MyImageView miv_larger, final MyImageView thumbView, int imageResId) {
        //如果当前有动画就取消，并执行当前的动画
        if (mCurrentAnimation != null) {
            mCurrentAnimation.cancel();
        }

        //加载大图到预设好的ImageView中
//        final ImageView expandedImageView = findViewById(
//                R.id.iv_big);
        miv_larger.setImageResource(imageResId);

        //计算放大图像的起始边界和结束边界
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        //起始边界是缩略图的全局可见矩形，最后的边界是容器container的全局可见矩形
        //将容器视图container的偏移量设置为区域的起源，因为定位动画的起源属性是（X，Y）
        thumbView.getGlobalVisibleRect(startBounds);

        container.getGlobalVisibleRect(finalBounds, globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        //使用center_crop将起始边界调整为与最终边界相同的纵横比边界
        //这可以防止在动画期间的不良拉伸。还计算开始缩放比例
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            //横向放大
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            //纵向放大
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        //隐藏缩略图并显示放大视图
        //当动画开始时，它会将放大的视图定位在原来缩略图的位置
        thumbView.setAlpha(0f);
        miv_larger.setVisibility(View.VISIBLE);


        //设置SCALE_X和SCALE_Y转换的轴心点为到放大后视图的左上角（默认值是视图的中心）
        miv_larger.setPivotX(0f);
        miv_larger.setPivotY(0f);


        //四种动画同时播放
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(miv_larger, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(miv_larger, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(miv_larger, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(miv_larger,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimation = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimation = null;
            }
        });
        set.start();

        mCurrentAnimation = set;

        //从这往下就是给大图设置点击监听，完成缩小回去的过程
        final float startScaleFinal = startScale;
        miv_larger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimation != null) {
                    mCurrentAnimation.cancel();
                }

                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(miv_larger, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(miv_larger,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(miv_larger,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(miv_larger,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        miv_larger.setVisibility(View.GONE);
                        mCurrentAnimation = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        miv_larger.setVisibility(View.GONE);
                        mCurrentAnimation = null;
                    }
                });
                set.start();
                mCurrentAnimation = set;
            }
        });
    }
}
