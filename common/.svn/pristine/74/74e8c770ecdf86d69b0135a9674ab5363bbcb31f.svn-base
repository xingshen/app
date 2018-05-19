package com.steptowin.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Desc: 简化adapter的使用，只需要实现{@link #onCreateViewHolder(ViewGroup, int)}和{@link #onBindViewHolder(ViewHolder, int)}
 * 可以构造函数传递数据，也可以{@link #replaceAll(Collection)}传递
 * @Author: zg
 * @Time: 2016/2/18 11:33
 */
public abstract class ViewHolderAdapter<VH extends ViewHolderAdapter.ViewHolder, T> extends BaseAdapter {
    private Context mContext;
    private List<T> mList = new ArrayList<T>();
    protected LayoutInflater mInflater;

    public ViewHolderAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public ViewHolderAdapter replaceAll(Collection<? extends T> list) {
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
        return this;
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public T getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        VH holder;
        if (view == null) {
            holder = onCreateViewHolder(viewGroup, i);
            holder.view.setTag(holder);
        } else {
            holder = (VH) view.getTag();
        }

        onBindViewHolder(holder, i);
        return holder.view;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int position);

    public abstract void onBindViewHolder(VH holder, int position);

    public View inflate(int resLayout, ViewGroup parent) {
        return mInflater.inflate(resLayout, parent, false);
    }

    /**
     * 返回列表数据
     *
     * @return
     */
    public List<T> getDatas() {
        return this.mList;
    }

    public Context getContext() {
        return this.mContext;
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    public static class ViewHolder {
        View view;

        public ViewHolder(View view) {
            this.view = view;
        }

        public View getConvertView(){
            return view;
        }
    }
}