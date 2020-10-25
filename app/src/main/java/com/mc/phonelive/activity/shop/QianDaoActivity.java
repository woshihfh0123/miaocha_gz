package com.mc.phonelive.activity.shop;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.QdAdapter;
import com.mc.phonelive.adapter.QdScoreAdapter;
import com.mc.phonelive.bean.QianDaoBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *签到
 * 新888888
 *
 */

public class QianDaoActivity extends AbsActivity {
    private TextView mTitleView;
    private ImageView mBtn_back;
    private LinearLayout mLl_wk;
    private android.support.v7.widget.RecyclerView mRv,rv_h;
    private TextView mTv_submit;
    private QdAdapter mAdapter;
    private QdScoreAdapter scoreAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qd;
    }
    @Override
    protected void main() {
        setTitle("签到");
        mTitleView = (TextView) findViewById(R.id.titleView);
        mBtn_back = (ImageView) findViewById(R.id.btn_back);
        mLl_wk = (LinearLayout) findViewById(R.id.ll_wk);
        mRv = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv);
        rv_h = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_h);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
        mAdapter=new QdAdapter();
        scoreAdapter=new QdScoreAdapter();
        rv_h.setLayoutManager(Utils.getGvManager(mContext,7));
        rv_h.setAdapter(scoreAdapter);
        mRv.setLayoutManager(Utils.getGvManager(mContext,7));
        mRv.setAdapter(mAdapter);
//        List<String> nlist=new ArrayList<>();
//        for(int i=0;i<7;i++){
//            nlist.add("");
//        }
//        mAdapter.setNewData(nlist);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                mAdapter.setSeclection(position);
//                mAdapter.notifyDataSetChanged();
            }
        });
        mTv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              sign();
            }
        });
        getInfo();
    }

    private void sign() {
        HttpUtil.setQd(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                getInfo();
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    private void getInfo() {
        HttpUtil.getQd(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String bonus_day=obj.getString("bonus_day");
                    String is_bonus=obj.getString("is_bonus");
                    if(Utils.isNotEmpty(is_bonus)&&is_bonus.equals("1")){//已签到
                        mTv_submit.setText("已签到");
                        mTv_submit.setClickable(false);
                        mTv_submit.setBackgroundResource(R.drawable.shape_gray);

                    }else{
                        mTv_submit.setText("签到");
                        mTv_submit.setClickable(true);
                        mTv_submit.setBackgroundResource(R.drawable.shape_red);

                    }
                    ArrayList<QianDaoBean.BonusListBean> bannerList = (ArrayList<QianDaoBean.BonusListBean>) JSON.parseArray(obj.getString("bonus_list"), QianDaoBean.BonusListBean.class);
                    List<String> nlist=new ArrayList<>();
                    for(int i=0;i<7;i++){
                        nlist.add(bonus_day);
                    }
                    mAdapter.setNewData(nlist);
                    if(Utils.isNotEmpty(bannerList)){
                        scoreAdapter.setNewData(bannerList);
                    }
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    private void release(){
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
