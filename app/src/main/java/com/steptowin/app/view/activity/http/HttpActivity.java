package com.steptowin.app.view.activity.http;

import android.os.Bundle;

import com.steptowin.app.view.activity.R;
import com.steptowin.app.view.fragment.http.HttpFragment;
import com.steptowin.common.base.BaseActivity;
import com.steptowin.common.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * desc:
 * author：zg
 * date:2016-03-30
 */
public class HttpActivity extends BaseActivity {

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
        return new HttpFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

    }
}
