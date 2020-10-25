package com.mc.phonelive.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.LiveAudienceActivity;
import com.mc.phonelive.adapter.MoreZbGvAdapter;
import com.mc.phonelive.bean.LiveBean;
import com.mc.phonelive.bean.WyBannerBean;
import com.mc.phonelive.bean.WyZbBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.presenter.CheckLivePresenter;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.GlideUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;


public class MoreZbDialog extends Dialog {

    private com.youth.banner.Banner mBanner;
    private Context context;
    private Button bt_left,bt_right;
    private String id;
private View view_close;
    private int page=1;
    private RecyclerView rv;
    private MoreZbGvAdapter mAdapter;

    public MoreZbDialog(@NonNull Context context, String id) {
        super(context, R.style.custom_options_dialog_lr);
        this.context=context;
        this.id=id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertext_form);
mAdapter=new MoreZbGvAdapter();
        initDialoParameter();
        setCanceledOnTouchOutside(true);
        initView();
        getList();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((LiveAudienceActivity)context).exitLiveRoom();
                LiveBean liveBean=new LiveBean();//LiveBean
                liveBean.setUid(mAdapter.getData().get(position).getUid());
                liveBean.setStream(mAdapter.getData().get(position).getStream());
                liveBean.setThumb(mAdapter.getData().get(position).getThumb());
                liveBean.setPull(mAdapter.getData().get(position).getPull());
                liveBean.setUserNiceName(mAdapter.getData().get(position).getUser_nicename());
                liveBean.setIs_shopping(mAdapter.getData().get(position).getIs_shopping());
                liveBean.setAvatar(mAdapter.getData().get(position).getAvatar());
                watchLive(liveBean, Constants.LIVE_FOLLOW,position);
                dismiss();
            }
        });
    }
    private CheckLivePresenter mCheckLivePresenter;
    public void watchLive(LiveBean liveBean, String key, int position) {
        if (mCheckLivePresenter == null) {
            mCheckLivePresenter = new CheckLivePresenter(context);
        }
        mCheckLivePresenter.watchLive(liveBean, key, position);
    }
    private void initView() {
        mBanner = (com.youth.banner.Banner) findViewById(R.id.banner);
        view_close=findViewById(R.id.view_close);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(Utils.getGvManager(context,2));
        rv.setAdapter(mAdapter);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object o, ImageView imageView){
                WyBannerBean advBean= (WyBannerBean) o;
                if(Utils.isNotEmpty(advBean)&&Utils.isNotEmpty(advBean.getSlide_pic())){
                    GlideUtils.loadBannerImage(context,advBean.getSlide_pic(),imageView);
                }
            }
        }).setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER);

        view_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



    }
    private void getList() {//String p,String type,String catid
        HttpUtil.getMoreZb(page+"",new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<WyBannerBean> bannerList = (ArrayList<WyBannerBean>) JSON.parseArray(obj.getString("banner"), WyBannerBean.class);
                    ArrayList<WyZbBean> nlist = (ArrayList<WyZbBean>) JSON.parseArray(obj.getString("list"), WyZbBean.class);
                    if(Utils.isNotEmpty(bannerList)){
                        mBanner.setImages(bannerList);
                        mBanner.start();
                    }
                    if(Utils.isNotEmpty(nlist)){
                        mAdapter.setNewData(nlist);
                    }else{
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }



    @Override
    public void show() {
        super.show();
    }




    /**
     * 初始化弹窗参数
     */
    private void initDialoParameter() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent); //设置对话框背景透明 ，对于AlertDialog 就不管用了
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        lp.x = 0; // 新位置X坐标
//        lp.y = -20; // 新位置Y坐标
//        lp.width= (int) CommentUtil.dpToPx(280);
//        lp.height =(int) CommentUtil.dpToPx(200);
//        lp.alpha = 9f; // 透明度
//        dialogWindow.setAttributes(lp);

    }


}
