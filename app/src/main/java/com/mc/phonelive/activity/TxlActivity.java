package com.mc.phonelive.activity;

import android.Manifest;
import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.adapter.TxlFxAdapter;
import com.mc.phonelive.bean.ContactModel;
import com.mc.phonelive.bean.TxlBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 * 手机通讯录
 */

public class TxlActivity extends AbsActivity implements EasyPermissions.PermissionCallbacks{

//    private VideoHomeViewHolder mVideoHomeViewHolder;
    private RecyclerView mRv;
    private TxlFxAdapter mAdapter;
    private SmartRefreshLayout refreshLayout;
    private TextView tv_friends;
    private int page=1;
    private TextView tv_noinfo;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_txl;
    }

    @Override
    protected void main() {
        setTitle("手机通讯录");
//        mVideoHomeViewHolder = new VideoHomeViewHolder(mContext, (ViewGroup) findViewById(R.id.container), AppConfig.getInstance().getUid());
//        mVideoHomeViewHolder.addToParent();
//        addLifeCycleListener(mVideoHomeViewHolder.getLifeCycleListener());
//        mVideoHomeViewHolder.loadData();
        mRv = (RecyclerView)findViewById(R.id.rv);
        tv_friends = (TextView) findViewById(R.id.tv_friends);
        tv_noinfo = (TextView) findViewById(R.id.tv_noinfo);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mAdapter=new TxlFxAdapter();
        mRv.setLayoutManager(Utils.getLvManager(mContext));
        mRv.setAdapter(mAdapter);
        PermissionRequest();
        getList();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                upTxl();
                page=1;
                getList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getList();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_status){
                    guanZhu(position,mAdapter.getData().get(position).getUid());
                }
            }
        });
    }

    private void guanZhu(int position,String uid) {
        HttpUtil.setGz(uid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                    if (!DataSafeUtils.isEmpty(info)) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        String isattent = obj.getString("isattent");
                        mAdapter.getData().get(position).setIsattent(isattent);
                        mAdapter.notifyItemChanged(position);
                    }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).setTitle("权限访问受限")
                    .setRationale("打开系统设置界面，开启通讯录权限")
                    .build().show();
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Logger.e("ppppppppppppp");
    }
    @AfterPermissionGranted(123)
    public  void PermissionRequest(){
        String[] perms = new String[]{Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            // 已经申请过权限，做想做的事
                upTxl();

        } else {
            // 没有申请过权限，现在去申请
//            if(type.equals("1")){
//                upTxl(false);
//            }
            ActivityCompat.requestPermissions((Activity) mContext, perms,
                    123);
//            EasyPermissions.requestPermissions(this, "系统检测未开启通讯录读取权限，点击确定允许访问", 123, perms);
        }
    }
    private void upTxl() {
        String  getTxl=handleContact();
        Log.e("TTT:",getTxl);
        HttpUtil.upTxl(getTxl,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.e("SSSS:",code+"--"+msg);
//                if (!DataSafeUtils.isEmpty(info)) {
//                    JSONObject obj = JSON.parseObject(info[0]);
//                    ArrayList<TxlBean> nowsBean = (ArrayList<TxlBean>) JSON.parseArray(obj.getString("list"), TxlBean.class);
//                    if(Utils.isNotEmpty(nowsBean)){
//                        mAdapter.setNewData(nowsBean);
//                        tv_friends.setText("共"+nowsBean.size()+"好友");
//                    }
//                }
            }

        });
    }
    private List<ContactModel> contactModels=new ArrayList<>();
    private String end="EEEEEE";
    private String start="SSSSSS";
    private String handleContact(){

        List<HashMap<String, String>> allContactInfo = getAllContactInfo();
        contactModels.clear();
        for(HashMap<String, String> map:allContactInfo ){
            ContactModel contactModel=new ContactModel();
            String name = map.get("name");
            if (TextUtils.isEmpty(name)){
                name="";
            }
            contactModel.setNickname(name);
            List<String>phones=new ArrayList<>();
            String tel = map.get("phone");
            if (!TextUtils.isEmpty(tel)){
                phones.add(tel);
            }
            contactModel.setMobile(phones);
            contactModels.add(contactModel);
        }
        String mobile="";
        for(ContactModel cml:contactModels){
            if(Utils.isNotEmpty(cml)&&Utils.isNotEmpty(cml.getMobile())){
                mobile=mobile+cml.getMobile()+start+cml.getNickname()+end;
            }
        }
        Log.e("AAA",""+JSON.toJSON(contactModels));
        if(Utils.isNotEmpty(mobile)&&mobile.contains(",")){
            mobile= mobile.substring(0,mobile.length()-1);
        }
//        return mobile;
        return JSON.toJSON(contactModels.subList(0,1)).toString();



    }

    public List<HashMap<String, String>> getAllContactInfo() {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        Cursor cur = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cur.moveToNext()) {
            String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).replaceAll(" ","");
            String phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ","");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name",name);
            map.put("phone",phone);
            list.add(map);
        }
        cur.close();

        return list;
    }
    private void getList() {
        HttpUtil.getTxl(page,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ArrayList<TxlBean> nowsBean = (ArrayList<TxlBean>) JSON.parseArray(obj.getString("list"), TxlBean.class);
                    if(Utils.isNotEmpty(nowsBean)){
                        mAdapter.setNewData(nowsBean);
                        tv_friends.setText("共"+nowsBean.size()+"好友");
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                if(Utils.isNotEmpty(mAdapter)&&Utils.isNotEmpty(mAdapter.getData())){
                    tv_friends.setVisibility(View.VISIBLE);
                    tv_noinfo.setVisibility(View.GONE);
                }else{
                    tv_friends.setVisibility(View.GONE);
                    tv_noinfo.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void release(){
//        if(mVideoHomeViewHolder!=null){
//            mVideoHomeViewHolder.release();
//        }
//        mVideoHomeViewHolder=null;
    }

    @Override
    public void onBackPressed() {
        release();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }
}
