package com.steptowin.common.base;

import com.steptowin.common.R;

/**
 * desc:没有toolbar,固定FragmentContainerId
 * author：zg
 * date:16/6/14
 * time:下午3:26
 */
public class SimpleFragmentActivity<M, V extends BaseView<M>, P extends BasePresenter<V>> extends BaseActivity<M,V,P> {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_no_toolbar;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

}
