package com.steptowin.app.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.steptowin.common.tool.UnitTool;
import com.steptowin.core.tools.WLog;

/**
 * desc:
 * author：zg
 * date:2016/10/18 0018
 * time:下午 6:31
 */
public class FloatBehavior extends CoordinatorLayout.Behavior<View> {
    View childView;
    NestedScrollView dependencyView;
    int marginTop ;

    public FloatBehavior() {
    }

    public FloatBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        marginTop = UnitTool.dp2px(context, 100);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        childView = child;
        if (dependency instanceof NestedScrollView) {

            dependencyView = (NestedScrollView) dependency;
            dependencyView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    WLog.debug("info", "onScrollChange----scrollY=" + scrollY);
                    trans(scrollY);
                }
            });
        }
        return dependency instanceof NestedScrollView;
    }

    /**
     * 位置、大小变化监听
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        trans(target.getScrollY());
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    private void trans(int scrollY) {
        if (scrollY < marginTop)
            childView.setTranslationY(-scrollY);
    }
}
