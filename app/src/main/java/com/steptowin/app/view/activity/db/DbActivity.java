package com.steptowin.app.view.activity.db;

import android.os.Bundle;

import com.steptowin.app.view.activity.R;
import com.steptowin.app.view.fragment.db.DbFragment;
import com.steptowin.common.base.BaseActivity;
import com.steptowin.common.base.BaseFragment;

/**
 * desc:
 * author：zg
 * date:2016/4/1 0001
 * time:下午 3:11
 */
public class DbActivity extends BaseActivity {

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
        return new DbFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
