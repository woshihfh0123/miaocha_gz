package com.mc.phonelive.glide;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mc.phonelive.AppContext;
import com.mc.phonelive.R;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by cxf on 2017/8/9.
 */

public class ImgLoader {
    private static RequestManager sManager;
    private static BlurTransformation sBlurTransformation;

    static {
        sManager = Glide.with(AppContext.sInstance);
        sBlurTransformation = new BlurTransformation(AppContext.sInstance, 10);
    }

    public static void display(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(url).apply(options).into(imageView);
    }

    public static void displayWithError(String url, ImageView imageView, int errorRes) {
        RequestOptions options = new RequestOptions().error(R.drawable.default_img)
                .skipMemoryCache(true).error(errorRes)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(url).apply(options).into(imageView);
    }

    public static void displayAvatar(String url, ImageView imageView) {
        displayWithError(url, imageView, R.mipmap.icon_avatar_placeholder);
    }

    public static void display(File file, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(file).apply(options).into(imageView);
    }

    public static void display(int res, ImageView imageView) {
        sManager.load(res).into(imageView);
    }

    /**
     * 显示视频封面缩略图
     */
    public static void displayVideoThumb(String videoPath, ImageView imageView) {
        sManager.load(Uri.fromFile(new File(videoPath))).into(imageView);
    }

    public static void displayDrawable(String url, final DrawableCallback callback) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(url).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> glideAnimation) {
                if (callback != null) {
                    callback.callback(resource);
                }
            }
        });
    }

    public static void displayDrawable(File file, final DrawableCallback callback) {
//        sManager.load(file).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<GlideDrawable>() {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                if (callback != null) {
//                    callback.callback(resource);
//                }
//            }
//        });
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(file).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (callback != null) {
                    callback.callback(resource);
                }
            }
        });
    }


    public static void display(String url, ImageView imageView, int placeholderRes) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true).placeholder(placeholderRes)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(url).apply(options).into(imageView);
    }

    /**
     * 显示模糊的毛玻璃图片
     */
    public static void displayBlur(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true).bitmapTransform(sBlurTransformation)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(url).apply(options)
                .into(imageView);
    }


    public interface DrawableCallback {
        void callback(Drawable drawable);
    }


}
