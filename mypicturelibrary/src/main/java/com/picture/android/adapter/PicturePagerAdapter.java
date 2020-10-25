package com.picture.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.picture.android.R;
import com.picture.android.view.PictureView;

import java.util.List;

/**
 * created by WWL on 2018/12/4 0004:13
 * 图片适配器
 */
public class PicturePagerAdapter extends PagerAdapter {


    private List<String> images;
    private LayoutInflater inflater;
    private Context mContext;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public PicturePagerAdapter(Context context, List<String> images, ImageLoader imageLoader, DisplayImageOptions options) {
        this.images = images;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);//
        this.imageLoader = imageLoader;
        this.options = options;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final View imageLayout = inflater.inflate(
                R.layout.picture_pager_image, view, false);
        final PictureView imageView = imageLayout
                .findViewById(R.id.post_image1);
        //都没有就从第三方的中获取
        try {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            imageView.setLayoutParams(params);
            Log.v("logger",images.toArray()[position].toString()+"====mFileUrl");
            imageLoader.displayImage(images.toArray()[position].toString(),
                    imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                    message = "网络异常";
                                    break;
                                case DECODING_ERROR:
                                    message = "图片加载失败";
                                    break;
                                case NETWORK_DENIED:
                                    message = "网络异常";
                                    // message = "Downloads are denied";
                                    break;
                                case OUT_OF_MEMORY:
                                    break;
                                case UNKNOWN:
                                    message = "未知错误";
                                    break;
                            }
                            if (null != message) {
                                Toast.makeText(mContext,
                                        message, Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, final Bitmap loadedImage) {

                        }
                    });
        } catch (Exception e) {
        }

        ((ViewPager) view).addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View container) {
    }


}
