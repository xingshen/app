package com.steptowin.app.view.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:17/2/8
 * time:下午5:05
 */
public class ServiceFragment extends BaseFragment {
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_service;
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

    ServiceConnection serviceConnection1;
    ServiceConnection serviceConnection2;
    @OnClick({R.id.btn_start, R.id.btn_stop, R.id.btn_bind, R.id.btn_unbind,R.id.btn_bind2})
    public void onClick(View view) {
        Intent servie = new Intent(getHoldingActivity(),LearnService.class);
        switch (view.getId()) {
            case R.id.btn_start:
                getHoldingActivity().startService(servie);
                break;
            case R.id.btn_stop:
                getHoldingActivity().stopService(servie);
                break;
            case R.id.btn_bind:
                servie.putExtra("binder","1");
                serviceConnection1 =  new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        Log.e("zhou","iBinder="+iBinder);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                };
                getHoldingActivity().bindService(servie,serviceConnection1, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_bind2:
                servie.putExtra("binder","2");
                serviceConnection2 = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        Log.e("zhou","iBinder="+iBinder);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                };
                getHoldingActivity().bindService(servie, serviceConnection2, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind:
                getHoldingActivity().unbindService(serviceConnection1);
                break;
        }
    }
}
