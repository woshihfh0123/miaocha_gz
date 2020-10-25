package com.mc.phonelive.activity.ordertk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.RefundORderBean;
import com.mc.phonelive.bean.foxtone.CityBean01;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.interfaces.ImageResultCallback;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.DpUtil;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.utils.ProcessImageUtil;
import com.mc.phonelive.utils.ToastUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySHApplyTKDetailActivity extends AbsActivity implements View.OnClickListener {

    @BindView(R.id.titleView)
    TextView titleView;

    @BindView(R.id.rel_content)
    LinearLayout rel_content;
    @BindView(R.id.shop_pic)
    ImageView shop_pic;
    @BindView(R.id.store_name)
    TextView store_name;
    @BindView(R.id.ly)
    TextView ly;
    @BindView(R.id.tk_money)
    TextView tk_money;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.choose_pz)
    ImageView choose_pz;
    @BindView(R.id.put_mesg)
            TextView put_mesg;
    @BindView(R.id.choose_reason)
            ImageView choose_reason;
    String storeID, orderId;
    String mobile;
    ProcessImageUtil mImageUtil;

    RefundORderBean.InfoBean mData;
    TKReasonListAdapter reasonAdapter;
    List<RefundORderBean.InfoBean.RefundReasonListBean> list;
    private PopupWindow mLinkMicPopWindow;//优惠券领取弹窗
    private boolean isShowCouponDialog = false;

    private String content,refundId,imageUrl;
    @Override
    protected int getLayoutId() {

        return R.layout.activity_applay_sh_done_detail;
    }

    @Override
    protected void main() {
        titleView.setText("申请售后");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mImageUtil = new ProcessImageUtil(this);
        uploadImage();
        orderId = getIntent().getStringExtra("orderId");
        storeID = getIntent().getStringExtra("goodsId");
        getIntent().getStringExtra("imag");
        getIntent().getStringExtra("goodsname");
        getIntent().getStringExtra("goodsnum");
        getIntent().getStringExtra("goodsprice");
        getIntent().getStringExtra("goodspayprice");

        tk_money.setText("￥" +  getIntent().getStringExtra("goodspayprice"));
        money.setText("￥" +getIntent().getStringExtra("goodsprice"));
        ImgLoader.display( getIntent().getStringExtra("imag"), shop_pic);
        store_name.setText( getIntent().getStringExtra("goodsname"));
        num.setText("x"+getIntent().getStringExtra("goodsnum"));
        put_mesg.setOnClickListener(this);
        choose_reason.setOnClickListener(this);
        loadData();
        ly.setOnClickListener(this);
        choose_pz.setOnClickListener(this);
//        mImageUtil = new ProcessImageUtil(this);
//        uploadImage();
    }

    //本地选择图片
    private void chooseImage(){
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{
                R.string.camera, R.string.alumb}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    mImageUtil.getImageByCamera();
                } else {
                    mImageUtil.getImageByAlumb();
                }
            }
        });
    }
    //初始化图片工具类毁掉,上传图片
    private void uploadImage(){
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
//                    ImgLoader.display(file, mAvatar);
                    HttpUtil.updateImage(file, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0 && info.length > 0) {
                                List<CityBean01.InfoBean> mList = JSON.parseArray(Arrays.toString(info), CityBean01.InfoBean.class);
                                ToastUtil.show("上传图片成功");

                                     imageUrl = mList.get(0).getName();

                                    ImgLoader.displayAvatar(AppConfig.HOST+""+imageUrl,choose_pz);
//                                    picList.add(0,info[0]);

                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
            }
        });
    }
    /**
     * 拨打客服电话
     */
    public static void callPhoneTel(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

    private void loadData() {
//        HttpUtil.getOrderRefundDetail(storeID, orderId, new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                JSONObject obj = JSON.parseObject(info[0]);
//                RefundORderBean.InfoBean mData = JSON.toJavaObject(obj, RefundORderBean.InfoBean.class);
//                tk_money.setText("￥" + mData.getRefund_price());
//                money.setText("￥" + mData.getRefund_price());
//                ImgLoader.display(mData.getGoods_image(), shop_pic);
//                store_name.setText(mData.getGoods_name());
//                num.setText(mData.getRefund_price());
//                num.setText(mData.getGoods_num());
//                ly.setText(mData.getRefund_reason());
//            }
//        });

        HttpUtil.GetRefundReasonList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                JSONObject obj = JSON.parseObject(info[0]);
                RefundORderBean.InfoBean mData = JSON.toJavaObject(obj, RefundORderBean.InfoBean.class);
                list = mData.getRefundReasonList();
                reasonAdapter = new TKReasonListAdapter(mContext, list);
            }
        });
    }


    private void refundOrder(){
        if(TextUtils.isEmpty(refundId)){
            ToastUtil.show("请选择退款原因");
            return;
        }
        if(TextUtils.isEmpty(imageUrl)){
            ToastUtil.show("请上传凭证");
            return;
        }

        HttpUtil.setRefundOrder(storeID,orderId, content, refundId, imageUrl, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 1012) {
                    EventBusUtil.postEvent(new EventBean("orderdetail_refresh"));
//        if(pos==3){
                    finish();
                } else {
                    ToastUtil.show(msg);
                }

            }


        });
    }
    /**
     * 弹窗卡券信息
     */
    private void showLinkMicDialog() {

        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_refund_reson_list, null);
        TextView btn_get = (TextView) v.findViewById(R.id.cancledia);
        GridView grid = (GridView) v.findViewById(R.id.reson_list);
        grid.setAdapter(reasonAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refundId =  ""+list.get(position).getId();
                content = list.get(position).getTitle();
                ly.setText(content);
                mLinkMicPopWindow.dismiss();
                bgAlpha((float) 1);
            }
        });



        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinkMicPopWindow.dismiss();
                bgAlpha((float) 1);

            }
        });


        mLinkMicPopWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,  DpUtil.dp2px(220), true);
        mLinkMicPopWindow.setBackgroundDrawable(new ColorDrawable());
        mLinkMicPopWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        bgAlpha((float) 0.5);
        mLinkMicPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bgAlpha((float) 1);


            }
        });
        mLinkMicPopWindow.showAtLocation(rel_content, Gravity.BOTTOM, 0, 0);

    }

    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_pz:
                chooseImage();
                break;
            case R.id.put_mesg:
                refundOrder();

                break;
            case R.id.choose_reason:
                showLinkMicDialog();
                break;
            case R.id.ly:
                showLinkMicDialog();
                break;
        }
    }
}
