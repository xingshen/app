package com.steptowin.app.view.fragment.db;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.steptowin.app.model.bean.sql.User;
import com.steptowin.app.presenter.DbPresenter;
import com.steptowin.app.view.activity.R;
import com.steptowin.app.view.custom.AppLoadingView;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.common.base.BaseView;
import com.steptowin.common.base.mvp.lce.ErrorView;
import com.steptowin.common.base.mvp.lce.LoadingView;
import com.steptowin.common.base.mvp.lce.MvpLceView;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc: 数据库操作测试
 * author：zg
 * date:2016/4/1 0001
 * time:下午 3:11
 */
public class DbFragment extends BaseFragment<List<User>, BaseView<List<User>>, DbPresenter> implements MvpLceView<List<User>> {
    @Bind(R.id.tv_hint)
    TextView tvHint;

    @Override
    public LoadingView createLoadingView() {
        return AppLoadingView.createView(getAttachedContext());
    }

    @Override
    public ErrorView createErrorView() {
        return null;
    }

    @Override
    public DbPresenter createPresenter() {
        return new DbPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_db;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    User user = new User();
    @OnClick({R.id.btn_insert, R.id.btn_delete, R.id.btn_update, R.id.btn_query})
    public void onClick(View view) {
        user.setUser_id("1");
        switch (view.getId()) {
            case R.id.btn_insert:
                user.setName("user2");

                getPresenter().insert(user);
                break;
            case R.id.btn_delete:

                getPresenter().delete(user);
                break;
            case R.id.btn_update:
                Random random = new Random();
                user.setName("user"+random.nextInt(100));
                user.setAge("18");
                user.setSex("1");
                getPresenter().update(user);
                break;
            case R.id.btn_query:
                getPresenter().query();
                break;
        }
    }

    @Override
    public void setData(List<User> data) {
        if (null != data && data.size() >0)
            tvHint.setText("您好，"+data.get(0).getName()+", 年龄："+data.get(0).getAge()+" 性别："+data.get(0).getSex());
        else
            tvHint.setText("未查到记录");
    }
}
