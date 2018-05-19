package com.steptowin.app.view.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.steptowin.core.tools.WLog;

/**
 * desc:依赖于AppbarLayout透明度渐变行为
 * author：zg
 * date:2016/10/18 0018
 * time:上午 11:02
 */
public class AppbarFadeBehavior extends CoordinatorLayout.Behavior {


    public AppbarFadeBehavior() {
    }

    public AppbarFadeBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
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

        WLog.info("info", "dependency.getTop()=" + dependency.getTop());
//        setChildViewAlpha(0);

        if (dependency instanceof AppBarLayout) {
            float alpha = Math.abs(dependency.getTop()) * 1.0f / ((AppBarLayout) dependency).getTotalScrollRange();
            if (alpha >= 0 && alpha <= 1)
                child.setAlpha(alpha);
        }
        return true;
    }

}
