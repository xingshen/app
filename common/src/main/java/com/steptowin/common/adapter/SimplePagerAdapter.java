package com.steptowin.common.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * desc:
 * author：zg
 * date:2016/10/20 0020
 * time:下午 5:41
 */
public abstract class SimplePagerAdapter<E> extends PagerAdapter {
    private final List<E> mData;

    public SimplePagerAdapter() {
        mData = new ArrayList<E>();
    }

    public SimplePagerAdapter replaceAll(Collection<? extends E> collection) {
        mData.clear();
        if (collection != null) {
            mData.addAll(collection);
        }
        notifyDataSetChanged();

        return this;
    }

    public List<E> getData() {
        return mData;
    }

    protected final E getItem(int index){
        return index >= 0 && index < mData.size() ? mData.get(index) : null;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        View view = getView(container, position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    protected abstract
    @NonNull
    View getView(ViewGroup container, int position);
}
