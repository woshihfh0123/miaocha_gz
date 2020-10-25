package cece.com.bannerlib.callback;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cece.com.bannerlib.model.PicBean;
import cece.com.bannerlib.view.RoundFrameLayout;

/**
 * Created by liugongce on 2017/11/29.
 */

public class DefaultCardBinderListener implements OnPagerAdapterBinerListener {
    private Context mContext;
    private int radius;

    public DefaultCardBinderListener(Context mContext, int radius) {
        this.mContext = mContext;
        this.radius = radius;
    }

    @Override
    public View bind(ViewGroup container, List<PicBean> bannerList, int position) {
        View view = bind(bannerList, position);
        RoundFrameLayout roundFrameLayout = new RoundFrameLayout(mContext);
        roundFrameLayout.setRadius(radius);
        roundFrameLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(roundFrameLayout);
        return roundFrameLayout;
    }

    private View bind(List<PicBean> bannerList, int position) {
        String object = bannerList.get(position).getPic();
//        if (object instanceof View) {
//            View view = (View) object;
//            ViewParent viewParent = view.getParent();
//            if (viewParent != null) {
//                ViewGroup parent = (ViewGroup) viewParent;
//                parent.removeView(view);
//            }
//            return view;
//
//        } else {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Log.v("logger",object+"---");
            Glide.with(mContext).load(object).into(iv);
            return iv;
//        }
    }

}
