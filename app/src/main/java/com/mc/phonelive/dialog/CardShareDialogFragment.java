package com.mc.phonelive.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.ShapeAdapter;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.FarmilyBean;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.mob.MobBean;
import com.mc.phonelive.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by cxf on 2018/10/19.
 * 视频分享弹窗
 */

public class CardShareDialogFragment extends AbsDialogFragment implements OnItemClickListener<MobBean> {

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
//    private VideoBean mVideoBean;


    @Override
    protected int getLayoutId() {
        return R.layout.card_dialog_live_share;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();
//        if (bundle == null) {
//            return;
//        }
//        mVideoBean = bundle.getParcelable(Constants.VIDEO_BEAN);
//        if (mVideoBean == null) {
//            return;
//        }

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView2 = (RecyclerView) mRootView.findViewById(R.id.recyclerView_2);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        List<MobBean> list = null;
        ConfigBean configBean = AppConfig.getInstance().getConfig();
        if (configBean != null) {
            list = MobBean.getVideoShareTypeList(configBean.getVideoShareTypes());
        }
        if (list != null) {
            ShapeAdapter adapter=new ShapeAdapter();
            List<FarmilyBean> nlist=new ArrayList<>();
            for(int i=0;i<2;i++){
                nlist.add(new FarmilyBean());
            }
            adapter.setNewData(nlist);
//            VideoShareAdapter1 adapter = new VideoShareAdapter1(mContext, list);
//            adapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String url= SpUtil.getInstance().getStringValue("ewmPath");
                if(position==0){
                    Platform plat = ShareSDK.getPlatform(Wechat.NAME);
                    sharePic(plat.getName(),url);
                }else if(position==1){
//                    Platform plat = ShareSDK.getPlatform(Wechat.WechatFavorite);
//                    sharePic(plat.getName(),url);
                }
            }
        });
        }
//        List<MobBean> list2 = new ArrayList<>();
//        MobBean linkBean = new MobBean();
//        linkBean.setType(Constants.LINK);
//        linkBean.setName(R.string.copy_link);
//        linkBean.setIcon1(R.mipmap.icon_share_video_link);
//        list2.add(linkBean);
//        MobBean reportBean = new MobBean();
//        if (mVideoBean.getUid().equals(AppConfig.getInstance().getUid())) {//自己的视频
//            reportBean.setType(Constants.DELETE);
//            reportBean.setName(R.string.delete);
//            reportBean.setIcon1(R.mipmap.icon_share_video_delete);
//        } else {
//            reportBean.setType(Constants.REPORT);
//            reportBean.setName(R.string.report);
//            reportBean.setIcon1(R.mipmap.icon_share_video_report);
//        }
//        list2.add(reportBean);
//        MobBean saveBean = new MobBean();
//        saveBean.setType(Constants.SAVE);
//        saveBean.setName(R.string.save);
//        saveBean.setIcon1(R.mipmap.icon_share_video_save);
//        list2.add(saveBean);
//        VideoShareAdapter adapter2 = new VideoShareAdapter(mContext, list2);
//        adapter2.setOnItemClickListener(this);
//        mRecyclerView2.setAdapter(adapter2);

    }

    @Override
    public void onItemClick(MobBean bean, int position) {
        if (!canClick()) {
            return;
        }
        dismiss();
        switch (bean.getType()) {
            case Constants.LINK://复制链接
//                ((MainActivity) mContext).copyLink(mVideoBean);
                break;
            case Constants.REPORT://举报
//                VideoReportActivity.forward(mContext, mVideoBean.getId());
                break;
            case Constants.SAVE://保存
//                ((MainActivity) mContext).downloadVideo(mVideoBean);
                break;
            case Constants.DELETE://删除
//                ((VideoPlayActivity) mContext).deleteVideo(mVideoBean);
                break;
            default:
                String url= SpUtil.getInstance().getStringValue("ewmPath");
                    Platform plat = ShareSDK.getPlatform(Wechat.NAME);
                    sharePic(plat.getName(),url);
                break;
        }
    }
    private void sharePic(String platform,String url){

        OnekeyShare oks = new OnekeyShare();
//        oks.setPlatform(platform);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("渺茶");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(url);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                    paramsToShare.setImagePath(url);
                } else if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                    paramsToShare.setImagePath(url);
                }

            }
        });
//        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("test");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

// 启动分享GUI
        oks.show(mContext);
    }
    private void showShare(String platform,String url) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImagePath(url);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        //启动分享
        oks.show(mContext);
    }
}
