package com.mc.phonelive.views.shopmall;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.AgentLevelBean;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 代理等级
 * created by WWL on 2020/4/15 0015:09
 */
public class MyTeamAgentLevelActivity extends AbsActivity {
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_title)
    FrameLayout viewTitle;
    @BindView(R.id.level_user_img)
    RatioRoundImageView levelUserImg;
    @BindView(R.id.level_img)
    RatioRoundImageView levelImg;
    @BindView(R.id.level_user_name)
    TextView levelUserName;
    @BindView(R.id.level_user_level)
    TextView levelUserLevel;
    @BindView(R.id.level_num1)
    TextView levelNum1;
    @BindView(R.id.level_user_level1)
    TextView levelUserLevel1;
    @BindView(R.id.level_num2)
    TextView levelNum2;
    @BindView(R.id.level_recyclerview)
    RecyclerView levelRecyclerview;
    @BindView(R.id.level_msg)
    RelativeLayout levelMsg;
    AgentLevelBean.InfoBean mLevelData = new AgentLevelBean.InfoBean();
    BaseQuickAdapter<AgentLevelBean.InfoBean.LevelsBean, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.team_agent_level_view;
    }

    @Override
    protected void main() {
        titleView.setText("代理级别");
        mLevelData = AgentLevelBean.getLeveData();
        levelNum1.setText(mLevelData.getLevelnum1());
        levelNum2.setText(mLevelData.getLevelnum2());
        getAdapter(mLevelData.getLevelist());

        initHttpData();
    }

    private void initHttpData() {
        HttpUtil.myTeamAgentLevel(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

            }
        });
    }

    private void getAdapter(List<AgentLevelBean.InfoBean.LevelsBean> levelist) {
        mAdapter = new BaseQuickAdapter<AgentLevelBean.InfoBean.LevelsBean, BaseViewHolder>(R.layout.agent_level_item, levelist) {
            @Override
            protected void convert(BaseViewHolder helper, AgentLevelBean.InfoBean.LevelsBean item) {
                TextView level_tip = helper.getView(R.id.level_tip);
                ProgressBar mSeekBar = helper.getView(R.id.seek_bar);
                TextView mTitle = helper.getView(R.id.level_name);
                TextView mNums = helper.getView(R.id.level_num);
                mSeekBar.setMax(Integer.parseInt(item.getMaxprogress()));
                mTitle.setText(item.getName());
                mNums.setText(item.getNum());
                if (item.getStatus().equals("1")) {
                    level_tip.setBackgroundResource(R.drawable.level_bg02);
                } else {
                    level_tip.setBackgroundResource(R.drawable.level_bg01);
                }
                if (Integer.parseInt(mLevelData.getLevelnum1()) > Integer.parseInt(item.getProgress())) {
                    mSeekBar.setProgress(Integer.parseInt(item.getProgress()));
                } else {
                        mSeekBar.setProgress( Integer.parseInt(item.getProgress()));
                }

            }
        };
        levelRecyclerview.setAdapter(mAdapter);
        levelRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.level_msg)
    public void onViewClicked() {
        ToastUtil.show("权益url");
    }
}
