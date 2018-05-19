package com.steptowin.app.view.fragment.http;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.steptowin.app.model.bean.sql.User;
import com.steptowin.app.presenter.HttpPresenter;
import com.steptowin.app.view.activity.R;
import com.steptowin.app.view.activity.http.HttpView;
import com.steptowin.app.view.custom.AppLoadingView;
import com.steptowin.app.view.fragment.EmptyFragment;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.common.base.mvp.lce.ErrorView;
import com.steptowin.common.base.mvp.lce.LoadingView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:2016-03-30
 */
public class HttpFragment extends BaseFragment<User, HttpView, HttpPresenter> implements HttpView {


    @Bind(R.id.tv_hint)
    TextView tvHint;

    @Override
    public HttpPresenter createPresenter() {
        return new HttpPresenter();
    }


    @Override
    public LoadingView createLoadingView() {
        return AppLoadingView.createView(getContext());
    }

    @Override
    public ErrorView createErrorView() {
        return null;
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
        return R.layout.fragment_http;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Title.init(toolbar).setTitle(R.string.title_http).setTitleTextColor(R.color.white).setNavigationIcon(BuildUtils.getBackArrowDrawable(getActivity())).setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        toolbar.setTitle(R.string.title_http);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        toolbar.setNavigationIcon(BuildUtils.getBackArrowDrawable(getActivity()));
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finishDelegate();
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_share_list,R.id.btn_jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //"18158179499", "c4ca4238a0b923820dcc509a6f75849b"
                getPresenter().doLogin("test330", "c4ca4238a0b923820dcc509a6f75849b");
                break;
            case R.id.btn_share_list:

                break;
            case R.id.btn_jump:
                replaceFragment(new EmptyFragment());
//                addFragment(new EmptyFragment());
                break;
        }
    }

    @Override
    public void setData(User data) {
        tvHint.setText(data.getName());
    }

    @Override
    public void loadData() {

    }
}
