package com.mc.phonelive.activity.shop;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.Categorylist;
import com.mc.phonelive.fragment.MainShopGoodsListFragment;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品全部分类
 * created by WWL on 2020/4/17 0017:14
 */
public class ShopMallGoodsListActivity extends AbsActivity {
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<String> mTitleDataList=new ArrayList<>();
    private List<String> mTypeIdList =new ArrayList<>();
    private ArrayList<Fragment> mFragmentList;
    private int mTypeId = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.shopmall_goodslist_view;
    }

    protected boolean isStatusBarWhite() {
        return true;
    }

    @Override
    protected void main() {
        titleView.setText("商品分类");

        mViewPager = findViewById(R.id.view_pager);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String typeid = bundle.getString("id");
            if (!DataSafeUtils.isEmpty(typeid)) {
                Log.v("tags",typeid+"---------typeid");
                this.mTypeId = Integer.parseInt(typeid);
            }
            List<Categorylist> typelist = (List<Categorylist>) bundle.getSerializable("typelist");
            if (!DataSafeUtils.isEmpty(typelist) && typelist.size() > 0) {
                for (int i = 0; i < typelist.size(); i++) {
                    mTitleDataList.add(typelist.get(i).getCate_name());
                    mTypeIdList.add(typelist.get(i).getId());
                }
            }
        }

        initMagicIndicator();
        for (int i = 0; i < mTypeIdList.size(); i++) {
            if (mTypeIdList.get(i).equals(mTypeId+"")) {
                mViewPager.setCurrentItem(i);
            }
        }

    }


    private void initMagicIndicator() {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(false);//是固定

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);// 文字变化样式
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mTitleDataList.get(index));
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setNormalColor(Color.parseColor("#f5f5f5"));
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
                indicator.setColors(Color.parseColor("#ffffff"));
                return indicator;
            }

        });

        initViewPagerData();
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }


    public void initViewPagerData() {
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTitleDataList.size(); i++) {
            MainShopGoodsListFragment fragment = MainShopGoodsListFragment.newInstance();
            fragment.setState(mTypeIdList.get(i) + "");
            mFragmentList.add(fragment);
        }
        mViewPager.setOffscreenPageLimit(mTitleDataList.size());
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
}
