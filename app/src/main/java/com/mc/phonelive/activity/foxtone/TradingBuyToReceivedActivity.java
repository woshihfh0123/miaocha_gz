package com.mc.phonelive.activity.foxtone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.picture.android.PictureActivity;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.BaseVO;
import com.mc.phonelive.bean.foxtone.TradingMailBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.httpnet.Callback;
import com.mc.phonelive.httpnet.HttpUtils;
import com.mc.phonelive.httpnet.JsonUtils;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.interfaces.ImageResultCallback;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ProcessImageUtil;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 上传交易信息
 */
public class TradingBuyToReceivedActivity extends AbsActivity {
    @BindView(R.id.zfb_usename)
    TextView zfbUsename;
    @BindView(R.id.zfb_account)
    TextView zfbAccount;
    @BindView(R.id.buy_lookimg)
    TextView buyLookimg;
    @BindView(R.id.zfb_img)
    ImageView zfbImg;
    @BindView(R.id.layout01)
    LinearLayout layout01;
    @BindView(R.id.yl_usename)
    TextView ylUsename;
    @BindView(R.id.yl_account)
    TextView ylAccount;
    @BindView(R.id.yl_card)
    TextView ylCard;
    @BindView(R.id.layout02)
    LinearLayout layout02;
    @BindView(R.id.upload_img)
    ImageView uploadImg;
    @BindView(R.id.load_layout)
    LinearLayout load_layout;
    @BindView(R.id.load_tv)
    TextView loadTv;

    private String mId="";
    private String mImages="";
    private String mType = "";//支付类型 1.支付宝   2.银联卡
    private String mStatus = "";//是上传资料，还是只是查看资料
    TradingMailBean.InfoBean.UserMsgBean mData = null;
    private ProcessImageUtil mImageUtil;


    @Override
    protected int getLayoutId() {
        return R.layout.trading_buy_received_layout;
    }

    @Override
    protected void main() {
        setTitle("买入代收款");
        TradingMailBean.InfoBean.UserMsgBean data = (TradingMailBean.InfoBean.UserMsgBean) this.getIntent().getSerializableExtra("bean");
        if (!DataSafeUtils.isEmpty(data)) {
            this.mData = data;
        }
        String id = this.getIntent().getStringExtra("id");
        if (!DataSafeUtils.isEmpty(id)) {
            this.mId = id;
        }
        String status = this.getIntent().getStringExtra("status");
        if (!DataSafeUtils.isEmpty(status)) {
            this.mStatus = status;
        }
        String type = this.getIntent().getStringExtra("type");
        if (!DataSafeUtils.isEmpty(type)) {
            mType = type;
        }

        initViewData();
    }

    private void initViewData() {
        if (mStatus.equals("1") || mStatus.equals("2")) {
            load_layout.setVisibility(View.VISIBLE);
        } else {
            load_layout.setVisibility(View.GONE);
        }


        if (mType.equals("1")) {
            layout01.setVisibility(View.VISIBLE);
            layout02.setVisibility(View.GONE);
            if (!DataSafeUtils.isEmpty(mData)) {
                if (!DataSafeUtils.isEmpty(mData.getName()))
                    zfbUsename.setText(mData.getName());
                if (!DataSafeUtils.isEmpty(mData.getAccount()))
                    zfbAccount.setText(mData.getAccount());
                if (!DataSafeUtils.isEmpty(mData.getApi_ewm()))
                    ImgLoader.display(mData.getApi_ewm(), zfbImg);
            }
        } else {
            layout01.setVisibility(View.GONE);
            layout02.setVisibility(View.VISIBLE);

            if (!DataSafeUtils.isEmpty(mData)) {
                if (!DataSafeUtils.isEmpty(mData.getName()))
                    ylUsename.setText(mData.getName());
                if (!DataSafeUtils.isEmpty(mData.getAccount()))
                    ylAccount.setText(mData.getAccount());
                if (!DataSafeUtils.isEmpty(mData.getCard()))
                    ylCard.setText(mData.getCard());
            }
        }



        mImageUtil = new ProcessImageUtil(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {
            }
            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    ImgLoader.display(file, uploadImg);
                    mImages = file.toString();
                }
            }

            @Override
            public void onFailure() {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.buy_lookimg, R.id.zfb_img, R.id.upload_img, R.id.load_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buy_lookimg:
            case R.id.zfb_img:
                getBigImages();
                break;
            case R.id.upload_img:
                editImages();
                break;
            case R.id.load_tv:
                if (DataSafeUtils.isEmpty(mImages)){
                    ToastUtil.show("请上传支付凭证");
                    return;
                }

                loadSubmit(mId,mImages);
                break;
        }
    }

    private void loadSubmit(String mId, String mImages) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", AppConfig.getInstance().getUid());
        map.put("token", AppConfig.getInstance().getToken());
        map.put("trade_id", mId);
        Map<String, List<String>> filepath = new HashMap<>();

        Map<String, String> singleFilePath = new HashMap<>();
        singleFilePath.put("file", mImages);

        String mUrls = AppConfig.HOST + "/api/public/?service=";
        HttpUtils.POST_WHITH_UPLOAD(mUrls + "Trade.getTradePay", map, singleFilePath, filepath, true, BaseVO.class, new Callback<BaseVO>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSucceed(String json, String code, BaseVO model) {
                if (!DataSafeUtils.isEmpty(model)) {
                    //{"ret":200,"data":{"code":0,"msg":"\u63d0\u4ea4\u6210\u529f","info":[{"msg":"\u63d0\u4ea4\u6210\u529f"}]},"msg":""}
                   String data = JsonUtils.getSinglePara(json,"data");
                   int ret =JsonUtils.getIntPara(data,"code");
                   String  msgs =JsonUtils.getSinglePara(data,"msg");
                    if (ret== 0) {
                        ToastUtil.show( msgs);
                        EventBus.getDefault().post(new EventBusModel("refresh_trading"));
                        TradingBuyToReceivedActivity.this.finish();
                    }else{
                        if (!DataSafeUtils.isEmpty(msgs)) {
                            ToastUtil.show(msgs + "");
                        }
                    }
                }
            }

            @Override
            public void onOtherStatus(String json, String code) {
                String msg = JsonUtils.getSinglePara(json, "msg");
                Log.v("tags", msg);
            }

            @Override
            public void onFailed(Throwable e) {
                Log.v("tags", "---错误---"+e.getMessage());
            }

            @Override
            public void onFinish() {
                Log.v("tags", "---结束---");
            }
        });
    }

    private void editImages() {
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

    private void getBigImages() {
        ArrayList<String> mList = new ArrayList<>();
        mList.add(mData.getApi_ewm());

        Intent intent = new Intent(mContext, PictureActivity.class);
        intent.putStringArrayListExtra("list", mList);
        intent.putExtra("id", 0 + "");
        mContext.startActivity(intent);
    }
}
