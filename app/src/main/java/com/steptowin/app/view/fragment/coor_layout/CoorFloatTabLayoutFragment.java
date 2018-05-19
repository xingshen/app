package com.steptowin.app.view.fragment.coor_layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * desc:移动and悬停效果
 * author：zg
 * date:2016/10/17 0017
 * time:下午 7:34
 */
public class CoorFloatTabLayoutFragment extends BaseFragment {
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_coor_tab_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new RecyclerViewFragment(), "Category 1");
        adapter.addFragment(new RecyclerViewFragment(), "Category 2");
        adapter.addFragment(new RecyclerViewFragment(), "Category 3");
        adapter.addFragment(new RecyclerViewFragment(), "Category 4");
        adapter.addFragment(new RecyclerViewFragment(), "Category 5");
        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public static class RecyclerViewFragment extends BaseRecyclerViewFragment {

        @Override
        public int getLayoutResId() {
            return R.layout.recyclerview;
        }
    }
}

