package com.steptowin.app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:16/6/3
 * time:下午6:41
 */
public class TestFragment1 extends BaseFragment {
    @Bind(R.id.tv_hint)
    TextView tvHint;
    @Bind(R.id.btn_add)
    Button btnAdd;
    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test1;
    }

    @Override
    protected int getChildFragmentContainerId() {
        return R.id.fragment_container;
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

    @OnClick(R.id.btn_add)
    public void onClick() {
        replaceChildFragment(new EmptyFragment());
    }
}
