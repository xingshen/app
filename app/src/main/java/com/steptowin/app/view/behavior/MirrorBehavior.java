package com.steptowin.app.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * desc:做出类似照镜子的行为
 * author：zg
 * date:2016/10/20 0020
 * time:下午 3:23
 */
public class MirrorBehavior extends CoordinatorLayout.Behavior {
    public MirrorBehavior() {
    }

    public MirrorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 依据此函数的返回值判定是否要产生依赖关系
     *
     * child  子视图
     * dependency  将要被依赖的视图
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {

        return dependency instanceof TextView;
    }

    /**
     * 被依赖视图的大小、位置变化会回调这里
     *
     * @param parent
     * @param child   依赖视图
     * @param dependency  被依赖视图
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float xOffset = dependency.getTranslationX();
        float yoffset = dependency.getTranslationY();

        child.setTranslationX(-xOffset);
        child.setTranslationY(yoffset);
//        WLog.info("info","xOffset="+xOffset);
//        WLog.info("info","yoffset="+yoffset);
        return false;
    }

    /**
     * 被依赖视图的滚动变化回调这里
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }
}
