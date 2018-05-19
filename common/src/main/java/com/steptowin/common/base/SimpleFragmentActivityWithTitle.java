package com.steptowin.common.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.steptowin.common.R;

import butterknife.ButterKnife;

/**
 * desc:带有toolbar,固定FragmentContainerId
 * author：zg
 * date:16/6/14
 * time:下午3:26
 */
public class SimpleFragmentActivityWithTitle<M, V extends BaseView<M>, P extends BasePresenter<V>> extends BaseActivity<M,V,P> {

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
        toolbar = (Toolbar)findViewById(R.id.toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
