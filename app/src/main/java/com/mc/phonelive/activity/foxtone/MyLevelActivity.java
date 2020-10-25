package com.mc.phonelive.activity.foxtone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.foxtone.MyLevelBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的等级
 */
public class MyLevelActivity extends AbsActivity {
    @BindView(R.id.rightView)
    TextView rightView;
    @BindView(R.id.level_title)
    TextView levelTitle;
    @BindView(R.id.level_yfz)
    TextView levelYfz;
    @BindView(R.id.level_fenhong)
    TextView levelFenhong;
    @BindView(R.id.level_name)
    TextView levelName;
    @BindView(R.id.me_yfz_level)
    TextView meYfzLevel;
    @BindView(R.id.Level_bar)
    ProgressBar LevelBar;
    @BindView(R.id.level_tip)
    TextView level_tip;
    @BindView(R.id.level_recyclerview)
    RecyclerView levelRecyclerview;
    private BaseQuickAdapter<MyLevelBean.InfoBean.ListBean, BaseViewHolder> mAdapter;
    private List<MyLevelBean.InfoBean.ListBean> mList = new ArrayList<>();
    private MyLevelBean.InfoBean mData = new MyLevelBean.InfoBean();

    @Override
    protected int getLayoutId() {
        return R.layout.my_level_layout;
    }

    @Override
    protected void main() {
        setAdapter(mList);
        initHttpData();
    }

    private void initHttpData() {

        HttpUtil.getUserGrade(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<MyLevelBean.InfoBean> infodata = JSON.parseArray(Arrays.toString(info), MyLevelBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(infodata) && infodata.size() > 0) {
                    mData = infodata.get(0);
                    if (!DataSafeUtils.isEmpty(infodata.get(0).getProfits())) {
                        setLevelData(infodata.get(0).getProfits());
                    }
                    if (!DataSafeUtils.isEmpty(infodata.get(0).getList()) && infodata.get(0).getList().size() > 0) {
                        if (mAdapter != null) {
                            mAdapter.setNewData(infodata.get(0).getList());
                        }

                    }
                }
            }
        });
    }

    private void setLevelData(MyLevelBean.InfoBean.ProfitsBean data) {
        if (!DataSafeUtils.isEmpty(data.getUser_grade())) {
            levelTitle.setText(data.getUser_grade());//名称
            levelName.setText(data.getUser_grade());//名称
        }
        if (!DataSafeUtils.isEmpty(data.getTotal()))
            levelYfz.setText("当前音分值：" + data.getTotal());//音分值

        if (!DataSafeUtils.isEmpty(data.getRate()))
            levelFenhong.setText("手续费分红：" + data.getRate());//手续费

        if (!DataSafeUtils.isEmpty(data.getGrade_id()))
            meYfzLevel.setText("Lv." + data.getGrade_id());//等级

        LevelBar.setMax(Integer.parseInt(data.getNext_fee()));
        LevelBar.setProgress(Integer.parseInt(data.getTotal()));//进度
        level_tip.setText("还需" + (Integer.parseInt(data.getNext_fee()) - Integer.parseInt(data.getTotal())) + "贡献值升级至V" + (Integer.parseInt(data.getGrade_id()) + 1));//备注
    }

    private void setAdapter(List<MyLevelBean.InfoBean.ListBean> mList) {
        mAdapter = new BaseQuickAdapter<MyLevelBean.InfoBean.ListBean, BaseViewHolder>(R.layout.mylevel_list_item, mList) {
            @Override
            protected void convert(BaseViewHolder helper, MyLevelBean.InfoBean.ListBean item) {
                if (!DataSafeUtils.isEmpty(item.getIcon())) {
                    Glide.with(MyLevelActivity.this).load(item.getIcon()).into((ImageView) helper.getView(R.id.item_img));
                }
                if (!DataSafeUtils.isEmpty(item.getTitle())) {
                    helper.setText(R.id.item_title, item.getTitle());
                }
                TextView mSubmit = helper.getView(R.id.item_submit);
                if (!DataSafeUtils.isEmpty(item.getButton_name())) {
                    mSubmit.setText(item.getButton_name());
                }
                if (!DataSafeUtils.isEmpty(item.getButton_id())) {
                    if (item.getButton_id().equals("1")) {
                        mSubmit.setBackgroundResource(R.drawable.level_submit_bg);
                    } else {
                        mSubmit.setBackgroundResource(R.drawable.level_submit_bg1);
                    }
                }
                mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getType().equals("1")) {
                            mContext.startActivity(new Intent(mContext, InviteFriendsActivity.class));
                        } else if (item.getType().equals("2")) {
                            mContext.startActivity(new Intent(mContext, ClubActivity.class));
                        } else if (item.getType().equals("3")) {
                            mContext.startActivity(new Intent(mContext, AgentApplyActivity.class));
                        }

                    }
                });
            }
        };
        levelRecyclerview.setAdapter(mAdapter);
        levelRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rightView)
    public void onViewClicked() {
        mContext.startActivity(new Intent(mContext, LevelPrivilegeActivity.class));
    }
}
