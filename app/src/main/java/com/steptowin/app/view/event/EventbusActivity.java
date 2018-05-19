package com.steptowin.app.view.event;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseActivity;

/**
 * desc:
 * author：zg
 * date:16/6/29
 * time:下午3:21
 */
public class EventbusActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_event;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        addFragment(new MyFragmentV2Subscriber());
    }
}
