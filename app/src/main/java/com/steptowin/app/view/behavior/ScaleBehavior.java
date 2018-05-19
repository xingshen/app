package com.steptowin.app.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.steptowin.common.tool.UnitTool;

/**
 * desc:
 * author：zg
 * date:2016/10/18 0018
 * time:下午 4:25
 */
public class ScaleBehavior extends CoordinatorLayout.Behavior<TextView> {
    int normalSize;
    TextView childTv;
    NestedScrollView dependencyView;

    public ScaleBehavior() {
    }

    public ScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        normalSize = UnitTool.sp2px(context, 20);

    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        childTv = child;

        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        childTv = child;
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, TextView child, View directTargetChild, View target, int nestedScrollAxes) {
        setTextSize(target.getScrollY());
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, TextView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        setTextSize(target.getScrollY());
    }

    private void setTextSize(int scrollY) {
        float ratio = scrollY / 200.0f;
        if (ratio < 1)
            childTv.setTextSize((int) (20 * (ratio > 1 ? 1 : ratio)));
    }
}
