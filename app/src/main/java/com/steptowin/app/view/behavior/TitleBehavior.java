package com.steptowin.app.view.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * desc:
 * author：zg
 * date:2016/10/19 0019
 * time:下午 2:59
 */
public class TitleBehavior extends CoordinatorLayout.Behavior<View> {

    public TitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.onLayoutChild(parent, child, layoutDirection);


        return false;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float alpha = Math.abs(dependency.getTop())/200.0f;
        child.setAlpha(alpha);
        return true;
    }
}
