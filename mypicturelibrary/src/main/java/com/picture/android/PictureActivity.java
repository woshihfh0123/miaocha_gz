package com.picture.android;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.picture.android.Model.EventBusVO;
import com.picture.android.adapter.PicturePagerAdapter;
import com.picture.android.util.HttpUtils;
import com.picture.android.view.AlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * created by WWL on 2019/5/29 0029:10
 */
@RuntimePermissions
public class PictureActivity extends AppCompatActivity{
    ViewPager pager;
    DisplayImageOptions options;
    ArrayList<String> imageUrls = new ArrayList<String>();
    int pagerPosition;
    ImageLoader imageLoader = null;
    ImageView download;
    ImageView backImg;
    TextView num_tv;
    String mFileUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.picture_activity);

        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }

        pager =findViewById(R.id.pager);
        download =findViewById(R.id.download);
        backImg =findViewById(R.id.back_img);
        num_tv =findViewById(R.id.num_tv);


        initView();
    }


    protected void initView() {

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_icon)
                .showImageOnFail(R.drawable.default_icon)
                .resetViewBeforeLoading(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        initHttpData();
    }


    protected void initHttpData() {
        imageUrls = this.getIntent().getStringArrayListExtra("list");
        if (null != imageUrls && !imageUrls.equals("")) {
            pagerPosition = Integer.parseInt(this.getIntent().getStringExtra("id"));
            mFileUrl = imageUrls.get(pagerPosition);
            num_tv.setText((pagerPosition+1)+"/"+imageUrls.size());
            getPicList(imageUrls);
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureActivityPermissionsDispatcher.ViewlickWithPermissionCheck(PictureActivity.this);
            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureActivity.this.finish();
            }
        });
    }

    private void getPicList(final List<String> house_pic) {

        pager.setAdapter(new PicturePagerAdapter(this, house_pic, imageLoader, options));
        pager.setCurrentItem(pagerPosition);


        //监听当前显示页
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                // TODO Auto-generated method stub
                mFileUrl = house_pic.get(index);
                num_tv.setText((index+1)+"/"+imageUrls.size());
                Log.v("logger", index + "");
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

    }


    private void downLoad() {
        HttpUtils.DOWNLOAD_FILE(PictureActivity.this, mFileUrl);

    }


    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void Viewlick() {
        downLoad();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PictureActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showclick(final PermissionRequest request) {
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventBusVO eventBusModel) {
        switch (eventBusModel.getCode()) {
            case "downloadimg":

                new AlertDialog(this).builder().setMsg("是否要下载此图片？").setColor(getResources().getColor(R.color.black)).setNegativeButton("下载", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PictureActivityPermissionsDispatcher.ViewlickWithPermissionCheck(PictureActivity.this);
                    }
                }).setPositiveButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }
    }

}
