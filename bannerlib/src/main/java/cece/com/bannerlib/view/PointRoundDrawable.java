package cece.com.bannerlib.view;

import android.graphics.drawable.GradientDrawable;

/**
 */

public class PointRoundDrawable extends GradientDrawable {
    public PointRoundDrawable(int color,int shape) {
        setShape(shape);
        setColor(color);
    }


}
