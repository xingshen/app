package com.steptowin.app.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.steptowin.app.view.fragment.WebFragment;
import com.steptowin.app.view.fragment.db.DbFragment;
import com.steptowin.app.view.fragment.http.HttpFragment;
import com.steptowin.common.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:16/6/3
 * time:下午4:39
 */
public class TestFragmentActivity extends BaseActivity {

    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;
    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    @Bind(R.id.btn_fragment1)
    Button btnFragment1;
    @Bind(R.id.btn_fragment2)
    Button btnFragment2;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test_fragment;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        fragment1 = new HttpFragment();
        fragment2 = new DbFragment();
        fragment3 = new WebFragment();
    }

    @OnClick({R.id.btn_fragment1, R.id.btn_fragment2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment1:
                replaceFragment(fragment3);
//                getFragmentManagerDelegate().switchFragment(R.id.fragment_container,fragment3);
                break;
            case R.id.btn_fragment2:
                addFragment(fragment2);
//                getFragmentManagerDelegate().switchFragment(R.id.fragment_container,fragment2);
                break;
        }
    }
}
