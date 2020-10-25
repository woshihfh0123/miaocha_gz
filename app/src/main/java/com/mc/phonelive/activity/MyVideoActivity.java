package com.mc.phonelive.activity;

import com.mc.phonelive.R;
import com.mc.phonelive.utils.WordUtil;

/**
 * Created by cxf on 2018/12/14.
 * 我的视频
 */

public class MyVideoActivity extends AbsActivity {

//    private VideoHomeViewHolder mVideoHomeViewHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_video;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.video_my_video));
//        mVideoHomeViewHolder = new VideoHomeViewHolder(mContext, (ViewGroup) findViewById(R.id.container), AppConfig.getInstance().getUid());
//        mVideoHomeViewHolder.addToParent();
//        addLifeCycleListener(mVideoHomeViewHolder.getLifeCycleListener());
//        mVideoHomeViewHolder.loadData();
    }

//    private void release(){
//        if(mVideoHomeViewHolder!=null){
//            mVideoHomeViewHolder.release();
//        }
//        mVideoHomeViewHolder=null;
//    }
//
//    @Override
//    public void onBackPressed() {
//        release();
//        super.onBackPressed();
//    }
//
//    @Override
//    protected void onDestroy() {
//        release();
//        super.onDestroy();
//    }
}
