package com.steptowin.app.view.activity;

import android.os.Bundle;

import com.steptowin.app.view.fragment.MainFragment;
import com.steptowin.common.base.BaseFragment;

public class MainActivity extends SimpleFragmentActivity {

    @Override
    protected BaseFragment getFirstFragment() {
        return new MainFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FFmpegController.getInstance(getHoldingActivity());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
