package com.steptowin.common.base.mvp.lce;

/**
 * desc: 数据加载时的视图接口;控制视图的显示与隐藏，并且支持显示过程中的刷新操作
 * author：zg
 * date:2016-03-31
 */
public interface LoadingView {

    /**
     * 显示
     */
    public void show();

    /**
     * 刷新
     */
    public void update();

    /**
     * 隐藏
     */
    public void dismiss();
}
