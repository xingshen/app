package com.steptowin.common.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:ExpandableList的通用adapter
 * author：zg
 * date:2016/5/6 0006
 * time:上午 11:57
 */
public abstract class BaseExpandableListAdapterWithHolder<G ,T> extends BaseExpandableListAdapter {
    private Context context;
    LayoutInflater inflater;
    private ExpandableListView expandableListView;

    protected List<G> groupList = new ArrayList<G>();
    protected List<List<T>> childList = new ArrayList<>();

    public BaseExpandableListAdapterWithHolder(Context context, ExpandableListView expandableListView){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.expandableListView = expandableListView;
    }

    public void expandAll(){
        for(int i=0;i<groupList.size();i++){
            expandableListView.expandGroup(i);
        }
    }

    public void expandGroup(int groupPosition){
        expandableListView.expandGroup(groupPosition);
    }

    public void collapseAll() {
        for (int i = 0; i < groupList.size(); i++) {
            expandableListView.collapseGroup(i);
        }
    }

    public void collapseGroup(int groupPosition) {
        expandableListView.collapseGroup(groupPosition);
    }

    public void noGroupIndicator(){
        expandableListView.setGroupIndicator(null);
    }

    public void replaceGroupData(List<G> groupList){
        this.groupList.clear();
        this.groupList.addAll(groupList);
    }
    public void replaceChildData(List<List<T>> childList){
        this.childList.clear();
        this.childList.addAll(childList);
    }

    public @LayoutRes
    int getGroupItemRes(int groupPosition){
        return 0;
    }

    public @LayoutRes
    int getChildItemRes(int groupPosition, int childPosition){
        return 0;
    }


    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public G getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public T getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View createView(int res){
        return inflater.inflate(res,null);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        SimpleViewHolderAdapter.SimpleViewHolder vh;
        if (convertView == null) {
            vh = onCreateGroupViewHolder(parent, groupPosition);
            convertView = vh.getConvertView();
            convertView.setTag(vh);
        } else {
            vh = (SimpleViewHolderAdapter.SimpleViewHolder) convertView.getTag();
        }

        onBindGroupViewHolder(vh, groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SimpleViewHolderAdapter.SimpleViewHolder vh;
        if (convertView == null) {
            vh = onCreateChildViewHolder(parent, groupPosition,childPosition);
            convertView = vh.getConvertView();
            convertView.setTag(vh);
        } else {
            vh = (SimpleViewHolderAdapter.SimpleViewHolder) convertView.getTag();
        }

        onBindChildViewHolder(vh, groupPosition,childPosition);
        return convertView;
    }

    protected SimpleViewHolderAdapter.SimpleViewHolder onCreateGroupViewHolder(ViewGroup parent, int groupPosition){
        if(getGroupItemRes(groupPosition) >0){
            return new SimpleViewHolderAdapter.SimpleViewHolder(createView(getGroupItemRes(groupPosition)));
        }
        return null;
    }

    protected abstract void onBindGroupViewHolder(SimpleViewHolderAdapter.SimpleViewHolder viewHolder, int groupPosition);

    protected SimpleViewHolderAdapter.SimpleViewHolder onCreateChildViewHolder(ViewGroup parent, int groupPosition, int childPosition){
        if(getChildItemRes(groupPosition,childPosition) >0){
            return new SimpleViewHolderAdapter.SimpleViewHolder(createView(getChildItemRes(groupPosition,childPosition)));
        }
        return null;
    }

    protected abstract void onBindChildViewHolder(SimpleViewHolderAdapter.SimpleViewHolder viewHolder, int groupPosition, int childPosition);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}