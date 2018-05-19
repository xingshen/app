package com.steptowin.app.view.fragment.coor_layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:2016/10/20 0020
 * time:下午 2:49
 */
public class CoorMainFragment extends BaseFragment {
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_coor_main;
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

    @OnClick({R.id.btn_behavior, R.id.btn_appbar_recyclerview,R.id.btn_appbar_fade_recyclerview
    ,R.id.btn_appbar_head_fade_recyclerview,R.id.btn_appbar_float_recyclerview
            ,R.id.btn_appbar_top_enteralways_recyclerview,R.id.btn_appbar_float_tab_recyclerview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_behavior:
                addFragment(new BehaviorFragment());
                break;
            case R.id.btn_appbar_recyclerview:
                addFragment(new CoorCollapseFragment());
                break;
            case R.id.btn_appbar_fade_recyclerview:
                addFragment(new CoorCustomCollapseFragment());
                break;
            case R.id.btn_appbar_head_fade_recyclerview:
                addFragment(new CoorCustomHeadCollapseFragment());
                break;
            case R.id.btn_appbar_float_recyclerview:
                addFragment(new CoorFloatLayoutFragment());
                break;
            case R.id.btn_appbar_top_enteralways_recyclerview:
                addFragment(new CoorTopEnterAlwaysFragment());
                break;
            case R.id.btn_appbar_float_tab_recyclerview:
                addFragment(new CoorFloatTabLayoutFragment());
                break;
        }
    }
}
