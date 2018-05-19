package com.steptowin.app.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.steptowin.common.base.BaseActivity;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.common.base.BasePresenter;
import com.steptowin.common.base.BaseView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * desc:带有toolbar,固定FragmentContainerId
 * author：zg
 * date:16/6/14
 * time:下午3:26
 */
public class SimpleFragmentActivityWithTitle<M, V extends BaseView<M>, P extends BasePresenter<V>> extends BaseActivity<M,V,P> {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_with_toolbar;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
