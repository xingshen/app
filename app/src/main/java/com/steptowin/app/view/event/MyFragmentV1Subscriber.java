package com.steptowin.app.view.event;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.steptowin.app.view.activity.R;
import com.steptowin.app.view.event.events.Event2;
import com.steptowin.common.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:16/6/29
 * time:下午2:16
 */
public class MyFragmentV1Subscriber extends BaseFragment {

    @Bind(R.id.tv_hint)
    TextView tvHint;
    @Bind(R.id.btn_to_post)
    Button btnToPost;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_subscriber;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Event2 event1) {
        tvHint.setText(event1.getContent());
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

    @OnClick(R.id.btn_to_post)
    public void onClick() {
        addFragment(new MyFragmentV1());
    }
}
