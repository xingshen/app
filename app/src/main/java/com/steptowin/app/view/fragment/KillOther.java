package com.steptowin.app.view.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.core.tools.WLog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:16/6/9
 * time:下午10:43
 */
public class KillOther extends BaseFragment {
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_kill_other;
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

    @OnClick(R.id.btn_kill)
    public void onClick() {
        WLog.debug("zhou", "killing..");
        String nameOfProcess = "com.steptowin.weixue";
        ActivityManager manager = (ActivityManager) getHoldingActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager.getRunningAppProcesses();
        PackageManager packageManager = getHoldingActivity().getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(nameOfProcess, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (null != applicationInfo) {
            WLog.debug("zhou", applicationInfo.processName + " : " + applicationInfo.uid);
//                android.os.Process.killProcess(applicationInfo.uid);
            manager.killBackgroundProcesses(applicationInfo.processName);
        }

    }
}
