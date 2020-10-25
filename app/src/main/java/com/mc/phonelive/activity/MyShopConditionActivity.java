package com.mc.phonelive.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.ShopConditionBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 小店申请条件
 */
public class MyShopConditionActivity extends AbsActivity implements View.OnClickListener {
    private TextView mConditionFensNum,mConditionLvNum,mConditionPrice;
    private ImageView mConditionFensImg,mConditionLvImg,mConditionJNImg;
    private TextView mConditionStatus1,mConditionStatus2,mConditionStatus3,condition_content;
    private TextView condition_apply;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myshop_condition;
    }

    @Override
    protected void main() {
        setTitle("我的小店");
        mConditionFensNum =findViewById(R.id.condition_num1);
        mConditionLvNum =findViewById(R.id.condition_num2);
        mConditionPrice =findViewById(R.id.condition_num3);
        mConditionFensImg =findViewById(R.id.condition_img1);
        mConditionLvImg =findViewById(R.id.condition_img2);
        mConditionJNImg =findViewById(R.id.condition_img3);
        mConditionStatus1 =findViewById(R.id.condition_status1);
        mConditionStatus2 =findViewById(R.id.condition_status2);
        mConditionStatus3 =findViewById(R.id.condition_status3);
        condition_content =findViewById(R.id.condition_content);
        condition_apply =findViewById(R.id.condition_apply);



        HttpUtil.shopCondition(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags",Arrays.toString(info)+"-----info");
                if (code == 0) {
                    List<ShopConditionBean.DataBean.InfoBean> list = JSON.parseArray(Arrays.toString(info), ShopConditionBean.DataBean.InfoBean.class);
                    ShopConditionBean.DataBean.InfoBean bean =list.get(0);

                    initDataView(bean);
                }
            }
        });





    }

    private void initDataView(ShopConditionBean.DataBean.InfoBean bean) {
        Glide.with(mContext).load(bean.getFans_pic()).into(mConditionFensImg);
        Glide.with(mContext).load(bean.getEmpirical_pic()).into(mConditionLvImg);
        Glide.with(mContext).load(bean.getBond_pic()).into(mConditionJNImg);
        mConditionFensNum.setText(">="+bean.getSys_fans()+"名");
        mConditionLvNum.setText(">=Lv."+bean.getEmpirical());
        mConditionPrice.setText(""+bean.getBond());
        mConditionStatus1.setText(bean.getSys_fans_title());
        mConditionStatus2.setText(bean.getEmpirical_title());
        mConditionStatus3.setText(bean.getBond_title());
        condition_content.setText(bean.getEquity());
        if(bean.getSys_fans_id().equals("0")){
            mConditionStatus1.setTextColor(getResources().getColor(R.color.red));
        }
        if(bean.getEmpirical_id().equals("0")){
            mConditionStatus2.setTextColor(getResources().getColor(R.color.red));
        }
        if(bean.getBond_id().equals("0")){
            mConditionStatus1.setTextColor(getResources().getColor(R.color.red));
            mConditionStatus3.setOnClickListener(this);
        }

        if (bean.getSys_fans_id().equals("1") && bean.getEmpirical_id().equals("1")&& bean.getBond_id().equals("1")){
            condition_apply.setOnClickListener(this);
            condition_apply.setBackgroundResource(R.drawable.bg_myshop_condition);
        }else{
            condition_apply.setBackgroundResource(R.drawable.bg_myshop_condition1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.condition_num3:
                Toast.makeText(this,"缴纳保证金",Toast.LENGTH_SHORT).show();
                break;
            case R.id.condition_apply:
                forwardApply();
                break;

        }
    }


    private void forwardApply(){
        this.finish();
        String mUrl=AppConfig.HOST+"/index.php?g=Appapi&m=Store&a=apply&uid="+AppConfig.getInstance().getUid()+"&token="+AppConfig.getInstance().getToken();
        Intent intent = new Intent(mContext, MyShopApplyActivity.class);
        intent.putExtra("url",mUrl);
        mContext.startActivity(intent);
    }
}
