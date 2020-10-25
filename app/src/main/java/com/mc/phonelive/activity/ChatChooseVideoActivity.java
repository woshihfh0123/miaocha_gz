//package com.zhiboshow.phonelive.activity;
//
//import android.content.Intent;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//
//import com.zhiboshow.phonelive.Constants;
//import com.zhiboshow.phonelive.R;
//import com.zhiboshow.phonelive.bean.ChatChooseImageBean;
//import com.zhiboshow.phonelive.bean.VideoChooseBean;
//import com.zhiboshow.phonelive.custom.ItemDecoration;
//import com.zhiboshow.phonelive.im.ImChatChooseImageAdapter;
//import com.zhiboshow.phonelive.im.ImChatChooseVideoAdapter;
//import com.zhiboshow.phonelive.interfaces.CommonCallback;
//import com.zhiboshow.phonelive.utils.ImageUtil;
//import com.zhiboshow.phonelive.utils.ToastUtil;
//import com.zhiboshow.phonelive.utils.VideoLocalUtil;
//import com.zhiboshow.phonelive.utils.WordUtil;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.jpush.im.android.api.content.VideoContent;
//
///**
// * Created by cxf on 2018/7/16.
// * 聊天时候选择视频发送
// */
//
//public class ChatChooseVideoActivity extends AbsActivity implements View.OnClickListener {
//
//    private RecyclerView mRecyclerView;
//    private ImChatChooseVideoAdapter mAdapter;
//    private VideoLocalUtil mImageUtil;
//    private View mNoData;
//    List<VideoContent> mList = new ArrayList<>();
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_chat_choose_img;
//    }
//
//    @Override
//    protected void main() {
//        findViewById(R.id.btn_cancel).setOnClickListener(this);
//        findViewById(R.id.btn_send).setOnClickListener(this);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
//        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 1, 1);
//        decoration.setOnlySetItemOffsetsButNoDraw(true);
//        mRecyclerView.addItemDecoration(decoration);
//        mNoData = findViewById(R.id.no_data);
//        mImageUtil = new VideoLocalUtil();
//        mImageUtil.getLocalVideoList(new CommonCallback<List<VideoChooseBean>>() {
//            @Override
//            public void callback(List<VideoChooseBean> list) {
//                if (list.size() == 0) {
//                    if (mNoData.getVisibility() != View.VISIBLE) {
//                        mNoData.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    mAdapter = new ImChatChooseVideoAdapter(mContext, list);
//                    mRecyclerView.setAdapter(mAdapter);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_cancel:
//                onBackPressed();
//                break;
//            case R.id.btn_send:
//                sendImage();
//                break;
//        }
//    }
//
//    private void sendImage() {
//        if (mAdapter != null) {
//            File file = mAdapter.getSelectedFile();
//            if (file != null && file.exists()) {
//                Log.v("tags",file.getAbsolutePath()+"----abs");
//                Intent intent = new Intent();
//                intent.putExtra(Constants.VIDEO_PATH, file.getAbsolutePath());
//                intent.putExtra("duration",file.length()+"");
//                Log.v("tags",mAdapter.getSelectedFile().length()+"-----length");
//                setResult(RESULT_OK, intent);
//                finish();
//            } else {
//                ToastUtil.show(WordUtil.getString(R.string.im_please_choose_image));
//            }
//        } else {
//            ToastUtil.show(WordUtil.getString(R.string.im_no_image));
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        mImageUtil.release();
//        super.onDestroy();
//    }
//
//
//}
