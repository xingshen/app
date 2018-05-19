package com.steptowin.app.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.steptowin.core.tools.WLog;

/**
 * desc:
 * author：zg
 * date:2016/10/18 0018
 * time:上午 11:02
 */
public class AlphaBehavior extends CoordinatorLayout.Behavior<View> {

    View childView;
    NestedScrollView dependencyView;

    public AlphaBehavior() {
    }

    public AlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        childView = child;
        if(dependency instanceof NestedScrollView){
            dependencyView = (NestedScrollView)dependency;
            dependencyView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    WLog.debug("info", "onScrollChange----scrollY=" + scrollY);
                    setChildViewAlpha(scrollY);
                }
            });
        }
        return dependency instanceof NestedScrollView;
    }

    /**
     * 位置、大小变化监听
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        WLog.debug("info", "dependency.getTop()=" + dependency.getTop());
        setChildViewAlpha(0);

        return true;
    }

    private void setChildViewAlpha(int scrollY){
        childView.setAlpha(scrollY/200.0f);
    }


    /**滚动监听----start**/
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        WLog.debug("info", "onStartNestedScroll----directTargetChild.getScrollY()=" + directTargetChild.getScrollY());
        WLog.debug("info", "onStartNestedScroll----target.getScrollY()=" + target.getScrollY());

        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        WLog.debug("info", "onNestedScroll----target.getScrollY()=" + target.getScrollY());
        WLog.debug("info", "onNestedScroll----dxConsumed=" + dxConsumed);
        WLog.debug("info", "onNestedScroll----dyConsumed=" + dyConsumed);
        WLog.debug("info", "onNestedScroll----dxUnconsumed=" + dxUnconsumed);
        WLog.debug("info", "onNestedScroll----dyUnconsumed=" + dyUnconsumed);

        setChildViewAlpha(target.getScrollY());

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        WLog.debug("info", "onNestedScrollAccepted----nestedScrollAxes=" + nestedScrollAxes);

        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        WLog.debug("info", "onStopNestedScroll----target.getScrollY()=" + target.getScrollY());
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    /**滚动监听----end**/

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        WLog.debug("info", "onNestedFling----target.getScrollY()=" + target.getScrollY());
        WLog.debug("info", "onNestedFling----velocityX=" + velocityX);
        WLog.debug("info", "onNestedFling----velocityY=" + velocityY);
        WLog.debug("info", "onNestedFling----consumed=" + consumed);
//        setChildViewAlpha(target.getScrollY());
        return true;
    }
}
