package com.mc.phonelive.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/12/18.
 */

public class MyPagerAdapterStr extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles;

    public MyPagerAdapterStr(FragmentManager fm, ArrayList<Fragment> mFragments,String[] mTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return mTitles[position];
        return "111";
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}