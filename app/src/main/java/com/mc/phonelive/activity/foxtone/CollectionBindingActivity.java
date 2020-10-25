package com.mc.phonelive.activity.foxtone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.foxtone.TradeAccountBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收款绑定
 */
public class CollectionBindingActivity extends AbsActivity {
    @BindView(R.id.card_tv01)
    TextView cardTv01;
    @BindView(R.id.card_layout01)
    RelativeLayout cardLayout01;
    @BindView(R.id.card_tv02)
    TextView cardTv02;
    @BindView(R.id.card_layout02)
    RelativeLayout cardLayout02;

    boolean isFirst = true;
    List<TradeAccountBean.InfoBean> mList = new ArrayList<>();
    TradeAccountBean.InfoBean.ListInfoBean mZfbData = new TradeAccountBean.InfoBean.ListInfoBean();
    TradeAccountBean.InfoBean.ListInfoBean mYlData = new TradeAccountBean.InfoBean.ListInfoBean();
    @Override
    protected int getLayoutId() {
        return R.layout.collect_binding_layout;
    }

    @Override
    protected void main() {
        setTitle("收款绑定");
        EventBus.getDefault().register(this);

        initHttpData();
    }

    @Override
    protected void onResuname01() {
        if (!isFirst)
            initHttpData();
    }

    private void initHttpData() {
        isFirst = false;
        HttpUtil.getTradeAccount(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<TradeAccountBean.InfoBean> data = JSON.parseArray(Arrays.toString(info),TradeAccountBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size()>0){
                    mList = data;
                    if (!DataSafeUtils.isEmpty(data.get(0).getAli_info())){
                        cardTv01.setText("已绑定");
                        mZfbData =data.get(0).getAli_info();
                    }
                    if (!DataSafeUtils.isEmpty(data.get(0).getCard_info())){
                        cardTv02.setText("已绑定");
                        mYlData =data.get(0).getCard_info();
                    }
//                    for (int i=0;i<data.size();i++){
//                        TradeAccountBean.InfoBean bean = data.get(i);
//                        if (bean.getType_id().equals("1")){
//                            //支付宝
//                            cardTv01.setText("已绑定");
//                            mZfbData =bean;
//                        }else if (bean.getType_id().equals("2")){
//                            //银联
//                            cardTv02.setText("已绑定");
//                            mYlData =bean;
//                        }
//                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.card_layout01, R.id.card_layout02})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        Intent intent = null;
        switch (view.getId()) {
            case R.id.card_layout01:
                intent = new Intent(mContext,ZhifubaoBindingActivity.class);
                if (!DataSafeUtils.isEmpty(mZfbData)){
                    bundle.putSerializable("data",mZfbData);
                    intent.putExtras(bundle);
                }
                mContext.startActivity(intent);
                break;

            case R.id.card_layout02:
                intent = new Intent(mContext,YinlianBindingActivity.class);
                if (!DataSafeUtils.isEmpty(mYlData)){
                    bundle.putSerializable("data",mYlData);
                    intent.putExtras(bundle);
                }
                mContext.startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void viewEvent(EventBusModel code){
        if (code.equals("refresh_data")){
            initHttpData();
        }
    }

    @Override
    protected void onDestroy01() {
        EventBus.getDefault().unregister(this);
    }
}
