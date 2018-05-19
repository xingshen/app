package com.steptowin.app.view.custom;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;
import android.view.ViewParent;

import com.steptowin.core.tools.WLog;

/**
 * desc:在折叠过程中，改变透明度
 * author：zg
 * date:2016/10/21 0021
 * time:下午 2:24
 */
public class CollapsingFadeToolbarLayout extends CollapsingToolbarLayout {
    AppbarOffsetChangeListener mOffsetChangeListener;


    public CollapsingFadeToolbarLayout(Context context) {
        super(context);
    }

    public CollapsingFadeToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollapsingFadeToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Add an OnOffsetChangedListener if possible
        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            if (mOffsetChangeListener == null) {
                mOffsetChangeListener = new AppbarOffsetChangeListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(mOffsetChangeListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // Remove our OnOffsetChangedListener if possible and it exists
        final ViewParent parent = getParent();
        if (mOffsetChangeListener != null && parent instanceof AppBarLayout) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(mOffsetChangeListener);
        }
        super.onDetachedFromWindow();
    }

    class AppbarOffsetChangeListener implements AppBarLayout.OnOffsetChangedListener {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            WLog.info("info","zhou----verticalOffset="+verticalOffset);

            /**
             * 获取AppBarLayout偏移最大值
             */
            WLog.info("info","zhou----appBarLayout.getTotalScrollRange()="+appBarLayout.getTotalScrollRange());
            float alpha = Math.abs(verticalOffset) * 1.0f / appBarLayout.getTotalScrollRange();
            if (alpha >= 0 && alpha <= 1)
                setAlpha(1 - alpha);
        }
    }
}
