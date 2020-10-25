package com.bs.xyplibs.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.xyplibs.R;
import com.bs.xyplibs.base.BaseApp;


/**
 * Created by admin on 2017/12/20.
 */

public class MyTitleBarView extends RelativeLayout {

    /**
     * actionbar布局
     */
    private RelativeLayout rl_base_titlebar;

    /**
     * 返回键的布局
     */
    private RelativeLayout rl_left_back;

    /**
     * 标题
     */
    private TextView tv_center_title;

    /**
     * 右边的文字
     */
    private TextView tv_right_text;

    /**
     * 右边的图片
     */
    private ImageView iv_right_image;

    /**
     * XML里获取的标题
     */
    private String titleBar_title;

    /**
     * XML里设置的背景颜色
     */
    private int backgroundColor;

    /**
     * 是否显示返回键
     */
    private boolean isVisiableBack;

    /**
     * 状态栏颜色
     */
    private int statueColor;


    /**
     * 标题颜色
     */
    private int titleColor;

    /**
     * 左边的图片view
     */
    private ImageView iv_left;

    /**
     * 左边的图片view
     */
    private TextView tv_left_text;

    /**
     * 左边的图片资源地址
     */
    private int leftImage;

    /**
     * 右边的布局
     */
    private RelativeLayout rl_right;

    /**
     * 标题的底线
     */
    private TextView view_line;

    public TextView getRightTextView(){
        return tv_right_text;
    }

    public MyTitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_my_titlebar, this);

        rl_base_titlebar = (RelativeLayout) findViewById(R.id.rl_base_titlebar);

        rl_left_back = (RelativeLayout) findViewById(R.id.rl_left_back);

        tv_center_title = (TextView) findViewById(R.id.tv_center_title);

        tv_right_text = (TextView) findViewById(R.id.tv_right_text);

        iv_right_image = (ImageView) findViewById(R.id.iv_right_image);

        iv_left = (ImageView) findViewById(R.id.iv_left);

        tv_left_text = (TextView) findViewById(R.id.tv_left_text);


        rl_right = (RelativeLayout) findViewById(R.id.rl_right);

        view_line = (TextView) findViewById(R.id.view_line);

        setback();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTitleBarView);

        titleBar_title = typedArray.getString(R.styleable.MyTitleBarView_my_title);

        backgroundColor = typedArray.getColor(R.styleable.MyTitleBarView_backgroundColor,
                getResources().getColor(R.color.ThemeTitleBackgroundcolor));

        titleColor = typedArray.getColor(R.styleable.MyTitleBarView_titleColor, 0);

        isVisiableBack = typedArray.getBoolean(R.styleable.MyTitleBarView_isVisiableBack, true);

        statueColor = typedArray.getColor(R.styleable.MyTitleBarView_statueColor, 0);

        leftImage = typedArray.getResourceId(R.styleable.MyTitleBarView_leftImage, 0);

        initAttras();
    }


    /**
     * 初始化XML里获取的标题和背景颜色
     */
    private void initAttras() {

        setTittle(titleBar_title);

        setTitleColor(titleColor);

        setBackgroundColor(backgroundColor);

//        setStatueBarColor(backgroundColor);

        isVisiableBack(isVisiableBack);

        setLeftImage(leftImage);

    }


    /**
     * actionbar返回键的方法
     */
    private void setback() {


        rl_left_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) getContext();
                if (activity.getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    activity.getSupportFragmentManager().popBackStack();
                } else {
                    BaseApp.removeActivity(activity);
                }

            }
        });
    }


    /**
     * 设置返回键的点击事件
     *
     * @param backListener
     */
    public void setBackClickListener(OnClickListener backListener) {
        rl_left_back.setOnClickListener(backListener);
    }

    /**
     * 设置左边的图片
     *
     * @param image
     */
    public void setLeftImage(int image) {
        if (image != 0) {
            tv_left_text.setVisibility(GONE);
            iv_left.setVisibility(VISIBLE);
            iv_left.setImageResource(image);
        }
    }



    /**
     * 设置左边文字属性
     *
     * @param text            文字
     * @param color           颜色
     * @param textSize        大小
     * @param onClickListener 点击事件
     */
    public void setLeftText(String text, int color, float textSize, OnClickListener onClickListener) {
        iv_left.setVisibility(GONE);
        tv_left_text.setVisibility(VISIBLE);

        if (!TextUtils.isEmpty(text)) {
            tv_left_text.setText(text);
        }

        if (color != 0) {
            tv_left_text.setTextColor(color);
        }

        if (textSize != 0) {
            tv_left_text.setTextSize(textSize);
        }

        if (onClickListener != null) {
            rl_left_back.setOnClickListener(onClickListener);
        }


    }


    public void setTitleOnClick(OnClickListener clickListener) {
        tv_center_title.setOnClickListener(clickListener);

    }


    /**
     * 设置actionbar的背景颜色
     *
     * @param color 颜色值
     */
    public void setBackgroundColor(int color) {
        rl_base_titlebar.setBackgroundColor(color);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        if (color != 0) {
            tv_center_title.setTextColor(color);
        }
    }

    /**
     * 设置actionbar的标题
     *
     * @param content 标题名称
     */
    public void setTittle(String content) {
        tv_center_title.setText(content);
    }


    /**
     * 设置右边的文字菜单属性
     *
     * @param text            右边的文字
     * @param color           文字的颜色,默认为0
     * @param textSize        文字的大小,默认为0
     * @param onClickListener 文字的点击事件
     */
    public void setRightText(String text, int color, float textSize, OnClickListener onClickListener) {
        iv_right_image.setVisibility(GONE);
        tv_right_text.setVisibility(VISIBLE);

        if (!TextUtils.isEmpty(text)) {
            tv_right_text.setText(text);
        }

        if (color != 0) {
            tv_right_text.setTextColor(color);
        }

        if (textSize != 0) {
            tv_right_text.setTextSize(textSize);
        }

        if (onClickListener != null) {
            rl_right.setOnClickListener(onClickListener);
        }
    }


    /**
     * 设置右边的图片
     *
     * @param img             图片资源路径
     * @param onClickListener 图片的点击事件
     */
    public void setRightImg(int img, OnClickListener onClickListener) {

        tv_right_text.setVisibility(GONE);
        iv_right_image.setVisibility(VISIBLE);
        iv_right_image.setImageResource(img);
        if (onClickListener != null) {
            rl_right.setOnClickListener(onClickListener);
        }

    }


    /**
     * 设置当前activity的状态栏颜色
     *
     * @param statueBarColor 设置的颜色
     */
    public void setStatueBarColor(int statueBarColor) {

        if (statueBarColor != 0) {
            AppCompatActivity activity = (AppCompatActivity) getContext();
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(statueBarColor);   //这里动态修改颜色
            }
        }

    }


    /**
     * 是否显示返回按钮
     *
     * @param isVisiable
     */
    public void isVisiableBack(boolean isVisiable) {
        rl_left_back.setVisibility(isVisiable == true ? VISIBLE : GONE);
    }


    /**
     * 获取底线对象，设置颜色或宽度
     *
     * @return
     */
    public View getBottomLine() {

        return view_line;
    }


}
