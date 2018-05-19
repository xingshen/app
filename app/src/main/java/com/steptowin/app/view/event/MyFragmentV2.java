package com.steptowin.app.view.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.steptowin.app.view.activity.R;
import com.steptowin.app.view.event.events.Event1;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.core.event.Event;
import com.steptowin.core.event.EventWrapper;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:事件发布者
 * author：zg
 * date:16/6/29
 * time:下午2:20
 */
public class MyFragmentV2 extends BaseFragment {

    @Bind(R.id.btn_post)
    Button btnPost;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_publisher;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().post(new Event1());
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

    @OnClick(R.id.btn_post)
    public void onClick() {
        String[] strParams = {"来自发布者2的事件","来自发布者2的事件2--"};
        Event event = Event.create(R.id.event_test1).putParam(String[].class, strParams);
        EventWrapper.post(event);
        getFragmentManagerDelegate().removeFragment();
    }
}
