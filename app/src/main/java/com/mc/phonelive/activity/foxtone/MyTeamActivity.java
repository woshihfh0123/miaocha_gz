package com.mc.phonelive.activity.foxtone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.UserHomeActivity;
import com.mc.phonelive.bean.foxtone.MyTeamBean;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.fragment.TeamFragment;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的团队
 */
public class MyTeamActivity extends AbsActivity {
    @BindView(R.id.agent_relayout)
    RelativeLayout agent_relayout;
    @BindView(R.id.team_avator)
    RatioRoundImageView teamAvator;
    @BindView(R.id.team_invite_username)
    TextView teamInviteUsername;
    @BindView(R.id.team_username)
    TextView teamUsername;
    @BindView(R.id.team_invite)
    TextView teamInvite;
    @BindView(R.id.team_all_num)
    TextView teamAllNum;
    @BindView(R.id.team_people_num)
    TextView teamPeopleNum;
    @BindView(R.id.team_yinfenzhi_num)
    TextView teamYinfenzhiNum;
    @BindView(R.id.team_realname_num)
    TextView teamRealnameNum;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private String[] mTitleDataList = {"全部队员", "未实名队员", "实名队员"};
    private ArrayList<Fragment> mFragmentList;
    private String mUserId = "";

    @Override
    protected int getLayoutId() {
        return R.layout.my_team_layout;
    }

    @Override
    protected void main() {

        mViewPager.setOffscreenPageLimit(mTitleDataList.length);
        initMagicIndicator();

        mViewPager.setCurrentItem(0);
        initHttpData();
    }

    private void initHttpData() {
        HttpUtil.TeamListData(1, "0", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<MyTeamBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), MyTeamBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(data) && data.size() > 0) {
                    if (!DataSafeUtils.isEmpty(data.get(0).getData())) {
                        setInitViewData(data.get(0).getData());
                    }
                }
            }
        });
    }

    private void setInitViewData(MyTeamBean.InfoBean.TeamHeadBean data) {

        if (!DataSafeUtils.isEmpty(data.getAgent()) && data.getAgent().size() > 0) {
            agent_relayout.setVisibility(View.VISIBLE);
            if (!DataSafeUtils.isEmpty(data.getAgent().get(0).getAvatar()))
                ImgLoader.display(data.getAgent().get(0).getAvatar(), teamAvator);
            if (!DataSafeUtils.isEmpty(data.getAgent().get(0).getUser_nicename()))
                teamUsername.setText(data.getAgent().get(0).getUser_nicename());

            if (!DataSafeUtils.isEmpty(data.getAgent().get(0).getId())){
                mUserId =data.getAgent().get(0).getId();
            }
        } else {
            agent_relayout.setVisibility(View.GONE);
        }

        if (!DataSafeUtils.isEmpty(data.getTeam_counts())) {
            //团队总人数
            teamPeopleNum.setText(data.getTeam_counts());
        }
        if (!DataSafeUtils.isEmpty(data.getArea_fee())) {
            //小区音分值
            teamYinfenzhiNum.setText(data.getArea_fee());
        }
        if (!DataSafeUtils.isEmpty(data.getAuth_fee())) {
            //认证人数
            teamRealnameNum.setText(data.getAuth_fee());
        }
        if (!DataSafeUtils.isEmpty(data.getTeam_fee())) {
            //我的团队音分值
            teamAllNum.setText(data.getTeam_fee());
        }

    }


    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);//是固定

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitleDataList.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);// 文字变化样式
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mTitleDataList[index]);
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 3));
                indicator.setLineWidth(UIUtil.dip2px(context, 20));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#FF5000"));
                return indicator;
            }

        });

        initViewPagerData();
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }


    public void initViewPagerData() {
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTitleDataList.length; i++) {
            TeamFragment fragment = (TeamFragment) TeamFragment.newInstance();
            fragment.setStatus(i + "");
            mFragmentList.add(fragment);
        }
        mViewPager.setOffscreenPageLimit(mTitleDataList.length);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.team_invite, R.id.team_avator})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.team_invite:
                mContext.startActivity(new Intent(mContext, InviteFriendsActivity.class));
                break;
            case R.id.team_avator:
                if (!DataSafeUtils.isEmpty(mUserId))
                UserHomeActivity.forward(mContext, mUserId+"");
                break;
        }
    }
}
