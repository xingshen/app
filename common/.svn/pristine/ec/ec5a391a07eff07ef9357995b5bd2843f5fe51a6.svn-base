package com.steptowin.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:ViewPager加载Fragment的简单实现
 * author：zg
 * date:16/7/2
 * time:上午12:08
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<? extends Fragment> fragments = new ArrayList<>();

    public static SimpleFragmentPagerAdapter create(FragmentManager fm){
        return new SimpleFragmentPagerAdapter(fm);
    }

    private SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<? extends Fragment> fragments){
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
