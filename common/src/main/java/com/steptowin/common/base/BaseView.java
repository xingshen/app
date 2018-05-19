package com.steptowin.common.base;

import android.content.Context;

import com.steptowin.common.base.mvp.lce.MvpLceView;

/**
 * desc:基于mvp
 * author：zg
 * date:16/6/1
 * time:下午8:25
 */
public interface BaseView<M> extends MvpLceView<M> {
    Context getAttachedContext();
}
