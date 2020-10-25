package com.mc.phonelive.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;
import com.mc.phonelive.R;

public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        RequestOptions options = new RequestOptions().fallback(R.drawable.default_pic)
                .skipMemoryCache(true).placeholder(R.drawable.default_pic).error(R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(imageView.getContext())
                .load(url).apply(options).into(imageView);
    }
}