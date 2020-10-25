package cece.com.bannerlib;

import android.content.Context;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import java.util.List;

import cece.com.bannerlib.callback.OnItemViewClickListener;
import cece.com.bannerlib.config.BannerBuilder;
import cece.com.bannerlib.config.CardModeOptions;
import cece.com.bannerlib.config.CirculationModeOptions;
import cece.com.bannerlib.config.DotOptions;
import cece.com.bannerlib.model.PicBean;

/**
 * created by WWL on 2019/8/17 0017:09
 */
public class BannerGetData {

    /**
     * 两种效果 带滑动缩放效果以及圆角以及普通轮播图
     *
     * @param list          数据列表 存放图片以及图片点击或者显示数据所需字段
     * @param cardMode      标示是否需要缩放效果
     * @param NormalColor   标示图标的默认颜色
     * @param SelectorColor 标示图标的选中
     * @param dotWidth      标示图标宽度大小
     * @param dotHeight     标示图标高度大小
     * @param shape         图标标示类型  0.RECTANGLE  1.OVAL   2.LINE   3.RING    其中2和3暂时还没效果
     */
    public static void getBannerData(Context context, OnItemViewClickListener listener, RelativeLayout rl_banner, List<PicBean> list, boolean cardMode,boolean isAutoCirculation, int NormalColor, int SelectorColor, int dotWidth, int dotHeight, int shape) {
        RollViewPage.build(new BannerBuilder(context)
                .setBanList(list)
                .setNotInterceptTouch(true)
                .setRelativeLayout(rl_banner).setOnItemViewClickListener(listener)
                .setCardViewOptions(new CardModeOptions().setCardMode(cardMode))
                .setDotOptions(new DotOptions().setShowDot(true).setMode(Mode.middle)
                        .setDotSize((int) dpToPx(context, dotWidth), (int) dpToPx(context, dotHeight)).setDotNormalColor(NormalColor).setDotSelectorColor(SelectorColor))
                .setCirculationModeOptions(new CirculationModeOptions()
                        .setAutoCirculation(isAutoCirculation)
                        .setRepeat(true).setCirculationTime(3000)), shape);
    }

    /**
     * dip---px转化
     *
     * @param dp
     * @return
     */

    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
