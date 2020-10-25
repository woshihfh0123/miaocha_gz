package com.mc.phonelive.activity.foxtone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.picture.android.PictureActivity;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确认收款
 */
public class TradingPayconfirmActivity extends AbsActivity {
    @BindView(R.id.images)
    ImageView images;
    @BindView(R.id.load_tv)
    TextView loadTv;

    private String mTradId = "";
    private String mImages="";
    @Override
    protected int getLayoutId() {
        return R.layout.trading_pay_confirm_layout;
    }

    @Override
    protected void main() {
        setTitle("确认收款");

        String id = this.getIntent().getStringExtra("id");
        if (!DataSafeUtils.isEmpty(id)) {
            this.mTradId = id;
        }
        String img = this.getIntent().getStringExtra("img");
        if (!DataSafeUtils.isEmpty(img)) {
            mImages =img;
            ImgLoader.display(img, images);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.load_tv)
    public void onViewClicked() {

    }

    @OnClick({R.id.images, R.id.load_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.images:
                getBigImages();
                break;
            case R.id.load_tv:
                HttpUtil.sureTrade(mTradId, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        ToastUtil.show(msg);
                        if (code == 0) {
                            EventBus.getDefault().post(new EventBusModel("refresh_trading"));
                            TradingPayconfirmActivity.this.finish();
                        }
                    }
                });
                break;
        }
    }

    private void getBigImages() {
        ArrayList<String> mList = new ArrayList<>();
        mList.add(mImages);
        Intent intent = new Intent(mContext, PictureActivity.class);
        intent.putStringArrayListExtra("list", mList);
        intent.putExtra("id", 0 + "");
        mContext.startActivity(intent);
    }

    @Override
    protected void onDestroy01() {
        HttpUtil.cancel(HttpConsts.SURETRADE);
    }
}
