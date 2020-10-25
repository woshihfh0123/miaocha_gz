package com.mc.phonelive.activity.shop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.FriendsFxAdapter;
import com.mc.phonelive.adapter.FsRightAdapter;
import com.mc.phonelive.bean.FamilyRightBean;
import com.mc.phonelive.bean.FansItemBean;
import com.mc.phonelive.custom.ActionSheetDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *粉丝
 * 新888888
 *
 */

public class FunsActivity extends AbsActivity {

    private com.scwang.smartrefresh.layout.SmartRefreshLayout mSrl;
    private RecyclerView mRv,rv_tj;
    private FsRightAdapter mAdapter;
    private FriendsFxAdapter fsBtmAdapter;
    private String type="";
    private int page=1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_fans;
    }

    @Override
    protected void main() {
        setTitle("粉丝");
        mSrl = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.srl);
        mRv = (RecyclerView) findViewById(R.id.rv);
        rv_tj = (RecyclerView) findViewById(R.id.rv_tj);

        mAdapter=new FsRightAdapter();
        fsBtmAdapter=new FriendsFxAdapter();
        mRv.setLayoutManager(Utils.getLvManager(mContext));
        mRv.setAdapter(mAdapter);
        rv_tj.setLayoutManager(Utils.getLvManager(mContext));
        rv_tj.setAdapter(fsBtmAdapter);
        getList();
        mSrl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                page=1;
                getList();
            }
        });
        fsBtmAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_status){
//                    guanZhu(fsBtmAdapter.getData().get(position).getId());
                    if(fsBtmAdapter.getData().get(position).getIsattention().equals("1")){
                        fsBtmAdapter.getData().get(position).setIsattention("0");
                    }else{
                        fsBtmAdapter.getData().get(position).setIsattention("1");
                    }
                    fsBtmAdapter.notifyItemChanged(position);
                    changeGz(position,fsBtmAdapter.getData().get(position).getId());

                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch(view.getId()){
                    case R.id.iv_yc:
                        ActionSheetDialog asd = new ActionSheetDialog(mContext)
                                .builder()
                                .setCancelable(true)
                                .setCanceledOnTouchOutside(true)
                                .addSheetItem("移除粉丝",ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {

                                        removeFans(position,mAdapter.getData().get(position).getId());
                                    }
                                });

                        asd.show();
                        break;
                    case R.id.tv_status:
                        if(mAdapter.getData().get(position).getIsattention().equals("1")){
                            mAdapter.getData().get(position).setIsattention("0");
                        }else{
                            mAdapter.getData().get(position).setIsattention("1");
                        }
                        mAdapter.notifyItemChanged(position);
                        changeGzFs(position,mAdapter.getData().get(position).getId());

                        break;
                    default:

                        break;
                }
            }
        });
    }

    private void removeFans(int position, String id) {
        HttpUtil.removeGz(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                mAdapter.remove(position);
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String isremove = obj.getString("isremove");
                    if(Utils.isNotEmpty(isremove)&&isremove.equals("1")){
                        ToastUtil.show("移除成功");
                    }
                }
            }
        });
    }
    private void changeGz(int position,String id) {
        HttpUtil.setGz(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String isattent = obj.getString("isattent");
                    fsBtmAdapter.getData().get(position).setIsattention(isattent);
                    fsBtmAdapter.notifyItemChanged(position);
                }
            }
        });

    }
    private void changeGzFs(int position,String id) {
        HttpUtil.setGz(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String isattent = obj.getString("isattent");
                    mAdapter.getData().get(position).setIsattention(isattent);
                    mAdapter.notifyItemChanged(position);
                }
            }
        });

    }
    private void getList() {
        HttpUtil.getFansList("", 1, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    ArrayList<FansItemBean> bannerList = (ArrayList<FansItemBean>) JSON.parseArray(Arrays.toString(info), FansItemBean.class);
                    if(Utils.isNotEmpty(bannerList)){
                        if(page==1){
                            mAdapter.setNewData(bannerList);
                        }else{
                            mAdapter.addData(bannerList);
                        }
                    }
                }
                getBtmList();
            }
        });
    }

    private void getBtmList() {
        HttpUtil.getFriendsList(page,"",new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<FamilyRightBean> nowsBean = (ArrayList<FamilyRightBean>) JSON.parseArray(obj.getString("list"), FamilyRightBean.class);
                    if(Utils.isNotEmpty(nowsBean)){
                        if(page==1){
                            fsBtmAdapter.setNewData(nowsBean);
                        }else{
                            fsBtmAdapter.addData(nowsBean);
                        }
                    }else{
                        if(page==1){
                            fsBtmAdapter.setNewData(null);
                        }
                    }

                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


}
