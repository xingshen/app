package com.steptowin.minivideo.shoot.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import com.steptowin.minivideo.R;


/**
 * desc:可以设置宽高比
 * author：zg
 * date:16/12/15
 * time:下午8:55
 */
public class AspectRatioSurfaceView extends SurfaceView {


    /**
     * 调整宽设置resizeBy值为height,
     * 调整高设置resizeBy值为width
     */
    private final boolean resizeByWidth;
    private final float ratio;


    public AspectRatioSurfaceView(Context context) {
        this(context,null);
    }

    public AspectRatioSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AspectRatioSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioSurfaceView);
        int resizeByWidth = ta.getInt(R.styleable.AspectRatioSurfaceView_resizeBy, 0);
        this.resizeByWidth = resizeByWidth == 1;
        float ratio = ta.getFloat(R.styleable.AspectRatioSurfaceView_ratio, 1.f);
        this.ratio = ratio <= 0 ? 1.f : ratio;

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (resizeByWidth) {

            int width = MeasureSpec.getSize(widthMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) Math.ceil(width * ratio), MeasureSpec.EXACTLY);
        } else {
            int height = MeasureSpec.getSize(heightMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) Math.ceil(height * ratio), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("zhou","h:"+getMeasuredHeight());
        Log.e("zhou","w:"+getMeasuredWidth());
    }
}
