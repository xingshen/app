package com.steptowin.app.view.fragment.coor_layout;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:2016/10/20 0020
 * time:下午 2:32
 */
public class BehaviorFragment extends BaseFragment {


    @Bind(R.id.id_ball)
    TextView idBall;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_behavior;
    }

    /**
     * 自由落体
     *
     * @param view
     */
    public void verticalRun(View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1000
                - idBall.getHeight());
        animator.setTarget(idBall);
        animator.setDuration(1000).start();
//      animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                idBall.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }

    /**
     * 抛物线
     * @param view
     */
    public void paowuxian(View view)
    {

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(1000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>()
        {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue)
            {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                PointF point = (PointF) animation.getAnimatedValue();
                idBall.setX(point.x);
                idBall.setY(point.y);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_vertical_run, R.id.btn_throw_run})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_vertical_run:
                verticalRun(view);
                break;
            case R.id.btn_throw_run:
                paowuxian(view);
                break;
        }
    }
}
