package com.mc.phonelive.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.AppContext;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.LevelBean;
import com.mc.phonelive.bean.LiveChatBean;
import com.mc.phonelive.custom.VerticalImageSpan;
import com.mc.phonelive.glide.ImgLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cxf on 2018/10/11.
 */

public class TextRender {

    private static StyleSpan sBoldSpan;
    private static StyleSpan sNormalSpan;
    private static ForegroundColorSpan sWhiteColorSpan;
    private static ForegroundColorSpan sGlobalColorSpan;
    private static ForegroundColorSpan sColorSpan1;
    private static AbsoluteSizeSpan sFontSizeSpan;
    private static AbsoluteSizeSpan sFontSizeSpan2;
    private static AbsoluteSizeSpan sFontSizeSpan3;
    private static AbsoluteSizeSpan sFontSizeSpan4;
    private static final int FACE_WIDTH;
    private static final int VIDEO_FACE_WIDTH;
    private static final String REGEX = "\\[([\u4e00-\u9fa5\\w])+\\]";
    private static final Pattern PATTERN;

    static {
        sBoldSpan = new StyleSpan(Typeface.BOLD);
        sNormalSpan = new StyleSpan(Typeface.NORMAL);
        sWhiteColorSpan = new ForegroundColorSpan(0xffffffff);
        sGlobalColorSpan = new ForegroundColorSpan(0xffffdd00);
        sColorSpan1 = new ForegroundColorSpan(0xffc8c8c8);
        sFontSizeSpan = new AbsoluteSizeSpan(17, true);
        sFontSizeSpan2 = new AbsoluteSizeSpan(13, true);
        sFontSizeSpan3 = new AbsoluteSizeSpan(14, true);
        sFontSizeSpan4 = new AbsoluteSizeSpan(12, true);
        FACE_WIDTH = DpUtil.dp2px(20);
        VIDEO_FACE_WIDTH = DpUtil.dp2px(16);
        PATTERN = Pattern.compile(REGEX);
    }


    /**
     * 生成前缀
     */
    private static SpannableStringBuilder createPrefix(Drawable levelDrawable, LiveChatBean bean) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("  ");
        if (levelDrawable!=null){
            levelDrawable.setBounds(0, 0, DpUtil.dp2px(28), DpUtil.dp2px(14));
            builder.setSpan(new VerticalImageSpan(levelDrawable), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        int index = 2;
        if (bean.getVipType() != 0) {//vip图标
            Drawable vipDrawable = ContextCompat.getDrawable(AppContext.sInstance, R.mipmap.icon_live_chat_vip);
            if (vipDrawable != null) {
                builder.append("  ");
                vipDrawable.setBounds(0, 0, DpUtil.dp2px(28), DpUtil.dp2px(14));
                builder.setSpan(new VerticalImageSpan(vipDrawable), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                index += 2;
            }
        }
        if (bean.isManager()) {//直播间管理员图标
            Drawable drawable = ContextCompat.getDrawable(AppContext.sInstance, R.mipmap.icon_live_chat_m);
            if (drawable != null) {
                builder.append("  ");
                drawable.setBounds(0, 0, DpUtil.dp2px(14), DpUtil.dp2px(14));
                builder.setSpan(new VerticalImageSpan(drawable), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                index += 2;
            }
        }
        if (bean.getGuardType() != Constants.GUARD_TYPE_NONE) {//守护图标
            Drawable drawable = ContextCompat.getDrawable(AppContext.sInstance,
                    bean.getGuardType() == Constants.GUARD_TYPE_YEAR ? R.mipmap.icon_live_chat_guard_2 : R.mipmap.icon_live_chat_guard_1);
            if (drawable != null) {
                builder.append("  ");
                drawable.setBounds(0, 0, DpUtil.dp2px(14), DpUtil.dp2px(14));
                builder.setSpan(new VerticalImageSpan(drawable), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                index += 2;
            }
        }
        if (!TextUtils.isEmpty(bean.getLiangName())) {//靓号图标
            Drawable drawable = ContextCompat.getDrawable(AppContext.sInstance, R.mipmap.icon_live_chat_liang);
            if (drawable != null) {
                builder.append("  ");
                drawable.setBounds(0, 0, DpUtil.dp2px(14), DpUtil.dp2px(14));
                builder.setSpan(new VerticalImageSpan(drawable), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }

    /**
     * 产品规定，进场消息不允许添加管理员图标,
     * 产品规定，进场消息不允许添加靓号图标
     * 所以 我只能复制一份上面的代码。。。。
     */
    private static SpannableStringBuilder createPrefix2(Drawable levelDrawable, LiveChatBean bean) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("  ");
        levelDrawable.setBounds(0, 0, DpUtil.dp2px(28), DpUtil.dp2px(14));
        builder.setSpan(new VerticalImageSpan(levelDrawable), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int index = 2;
        if (bean.getVipType() != 0) {//vip图标
            Drawable vipDrawable = ContextCompat.getDrawable(AppContext.sInstance, R.mipmap.icon_live_chat_vip);
            if (vipDrawable != null) {
                builder.append("  ");
                vipDrawable.setBounds(0, 0, DpUtil.dp2px(28), DpUtil.dp2px(14));
                builder.setSpan(new VerticalImageSpan(vipDrawable), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                index += 2;
            }
        }
        if (bean.getGuardType() != Constants.GUARD_TYPE_NONE) {//守护图标
            Drawable drawable = ContextCompat.getDrawable(AppContext.sInstance,
                    bean.getGuardType() == Constants.GUARD_TYPE_YEAR ? R.mipmap.icon_live_chat_guard_2 : R.mipmap.icon_live_chat_guard_1);
            if (drawable != null) {
                builder.append("  ");
                drawable.setBounds(0, 0, DpUtil.dp2px(14), DpUtil.dp2px(14));
                builder.setSpan(new VerticalImageSpan(drawable), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }

    public static void render(final TextView textView, final LiveChatBean bean) {
        final LevelBean levelBean = AppConfig.getInstance().getLevel(bean.getLevel());
        Log.i("ywl", "levelBean:" +levelBean);

        if (levelBean == null) {
            return;
        }
        Log.i("ywl", "levelBean.getThumb():" +TextUtils.isEmpty(levelBean.getThumb()));
        if (TextUtils.isEmpty(levelBean.getThumb())){
            initText(textView,bean,null,levelBean.getColor());
        return;
        }
        ImgLoader.displayDrawable(levelBean.getThumb(), new ImgLoader.DrawableCallback() {
            @Override
            public void callback(Drawable drawable) {
                if (textView != null) {
                    initText(textView,bean,drawable,levelBean.getColor());
                }
            }
        });
    }
    public static void initText(final TextView textView, final LiveChatBean bean,Drawable drawable,String colorStr){
        SpannableStringBuilder builder = createPrefix(drawable, bean);
        int color = 0;
        if (bean.isAnchor()) {
            color = 0xffffdd00;
        } else {
            color = Color.parseColor(colorStr);
        }
        switch (bean.getType()) {
            case LiveChatBean.GIFT:
                Log.i("ywl", "LiveChatBean.GIFT:" + LiveChatBean.GIFT);
                builder = renderGift(color, builder, bean);
                break;
            default:
                Log.i("ywl", "renderChat:" + LiveChatBean.GIFT);
                builder = renderChat(color, builder, bean);
                break;
        }
        textView.setText(builder);
    }
    /**
     * 渲染普通聊天消息
     */
    private static SpannableStringBuilder renderChat(int color, SpannableStringBuilder builder, LiveChatBean bean) {
        int length = builder.length();
        String name = bean.getUserNiceName();
        if (bean.getType() != LiveChatBean.ENTER_ROOM) {//产品规定，进场消息不允许加冒号
            name += "：";
        }
        SpannableString spannableName = new SpannableString(name);
        builder.append(spannableName);
        builder.setSpan(new ForegroundColorSpan(color), length, length + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(new SpannableString(bean.getContent()));
        if (bean.getType() == LiveChatBean.LIGHT) {
            Drawable heartDrawable = ContextCompat.getDrawable(AppContext.sInstance, IconUtil.getLiveLightIcon(bean.getHeart()));
            if (heartDrawable != null) {
                builder.append(new SpannableString(" "));
                heartDrawable.setBounds(0, 0, DpUtil.dp2px(16), DpUtil.dp2px(16));
                length = builder.length();
                builder.setSpan(new VerticalImageSpan(heartDrawable), length - 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }

    /**
     * 渲染送礼物消息
     */
    private static SpannableStringBuilder renderGift(int color, SpannableStringBuilder builder, LiveChatBean bean) {
        int length = builder.length();
        String name = bean.getUserNiceName() + "：";
        builder.append(name);
        builder.setSpan(new ForegroundColorSpan(color), length, length + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        length = builder.length();
        String content = bean.getContent();
        builder.append(new SpannableString(content));
        builder.setSpan(sGlobalColorSpan, length, length + content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }


    /**
     * 渲染用户进入房间消息
     */
    public static void renderEnterRoom(final TextView textView, final LiveChatBean bean) {
        final LevelBean levelBean = AppConfig.getInstance().getLevel(bean.getLevel());
        if (levelBean == null) {
            return;
        }
        ImgLoader.displayDrawable(levelBean.getThumb(), new ImgLoader.DrawableCallback() {
            @Override
            public void callback(Drawable drawable) {
                if (textView != null) {
                    SpannableStringBuilder builder = createPrefix2(drawable, bean);
                    int start = builder.length();
                    String name = bean.getUserNiceName() + " ";
                    builder.append(name);
                    int end = start + name.length();
                    builder.setSpan(sWhiteColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(sFontSizeSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(sBoldSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(bean.getContent());
                    textView.setText(builder);
                }
            }
        });
    }

    public static SpannableStringBuilder renderGiftInfo2(String giftName) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String s1 = WordUtil.getString(R.string.live_send_gift_1);
        String content = s1 + " " + giftName;
        int index1 = s1.length();
        builder.append(content);
        builder.setSpan(sGlobalColorSpan, index1, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder renderGiftInfo(int giftCount, String giftName) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String s1 = WordUtil.getString(R.string.live_send_gift_1);
        String s2 = WordUtil.getString(R.string.live_send_gift_2) + giftName;
        String content = s1 + giftCount + s2;
        int index1 = s1.length();
        int index2 = index1 + String.valueOf(giftCount).length();
        builder.append(content);
        builder.setSpan(sFontSizeSpan3, index1, index2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(sGlobalColorSpan, index1, index2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }


    public static SpannableStringBuilder renderGiftCount(int count) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String s = String.valueOf(count);
        builder.append(s);
        for (int i = 0, length = s.length(); i < length; i++) {
            String c = String.valueOf(s.charAt(i));
            if (StringUtil.isInt(c)) {
                int icon = IconUtil.getGiftCountIcon(Integer.parseInt(c));
                Drawable drawable = ContextCompat.getDrawable(AppContext.sInstance, icon);
                if (drawable != null) {
                    drawable.setBounds(0, 0, DpUtil.dp2px(24), DpUtil.dp2px(32));
                    builder.setSpan(new ImageSpan(drawable), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return builder;
    }

    /**
     * 渲染直播间用户弹窗数据
     */
    public static CharSequence renderLiveUserDialogData(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        //有时在想，海外项目的时候这个"万"怎么翻译？？？而且英语中也没有"万"这个单位啊。。
        String wan = " " + WordUtil.getString(R.string.num_wan);
        String s = StringUtil.toWan2(num) + wan;
        builder.append(s);
        int index2 = s.length();
        int index1 = index2 - wan.length();
        builder.setSpan(sNormalSpan, index1, index2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(sFontSizeSpan2, index1, index2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 聊天表情
     */
    public static CharSequence getFaceImageSpan(String content, int imgRes) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        Drawable faceDrawable = ContextCompat.getDrawable(AppContext.sInstance, imgRes);
        faceDrawable.setBounds(0, 0, FACE_WIDTH, FACE_WIDTH);
        ImageSpan imageSpan = new ImageSpan(faceDrawable, ImageSpan.ALIGN_BOTTOM);
        builder.setSpan(imageSpan, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 聊天内容，解析里面的表情
     */
    public static CharSequence renderChatMessage(String content) {
        Matcher matcher = PATTERN.matcher(content);
        boolean hasFace = false;
        SpannableStringBuilder builder = null;
        while (matcher.find()) {
            // 获取匹配到的具体字符
            String key = matcher.group();
            Integer imgRes = FaceUtil.getFaceImageRes(key);
            if (imgRes != null && imgRes != 0) {
                hasFace = true;
                if (builder == null) {
                    builder = new SpannableStringBuilder(content);
                }
                Drawable faceDrawable = ContextCompat.getDrawable(AppContext.sInstance, imgRes);
                if (faceDrawable != null) {
                    faceDrawable.setBounds(0, 0, FACE_WIDTH, FACE_WIDTH);
                    ImageSpan imageSpan = new ImageSpan(faceDrawable, ImageSpan.ALIGN_BOTTOM);
                    // 匹配字符串的开始位置
                    int startIndex = matcher.start();
                    builder.setSpan(imageSpan, startIndex, startIndex + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        if (hasFace) {
            return builder;
        } else {
            return content;
        }
    }


    /**
     * 评论内容，解析里面的表情
     */
    public static CharSequence renderVideoComment(String content, String addTime) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        Matcher matcher = PATTERN.matcher(content);
        while (matcher.find()) {
            // 获取匹配到的具体字符
            String key = matcher.group();
            Integer imgRes = FaceUtil.getFaceImageRes(key);
            if (imgRes != null && imgRes != 0) {
                Drawable faceDrawable = ContextCompat.getDrawable(AppContext.sInstance, imgRes);
                if (faceDrawable != null) {
                    faceDrawable.setBounds(0, 0, VIDEO_FACE_WIDTH, VIDEO_FACE_WIDTH);
                    ImageSpan imageSpan = new ImageSpan(faceDrawable, ImageSpan.ALIGN_BOTTOM);
                    // 匹配字符串的开始位置
                    int startIndex = matcher.start();
                    builder.setSpan(imageSpan, startIndex, startIndex + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        int startIndex = builder.length();
        builder.append(addTime);
        int endIndex = startIndex + addTime.length();
        builder.setSpan(sColorSpan1, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(sFontSizeSpan4, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

}
