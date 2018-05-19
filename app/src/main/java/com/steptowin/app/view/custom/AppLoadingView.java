package com.steptowin.app.view.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.mvp.lce.LoadingView;


/**
 * desc: 自定义加载视图
 * author：zg
 * date:2016/4/7 0007
 * time:下午 6:18
 */
public class AppLoadingView extends AlertDialog implements LoadingView {
    TextView tip;
    private AppLoadingView(Context context) {
        super(context);
    }

    private AppLoadingView(Context context ,int theme){
        super(context, theme);
    }

    private void init(){
        setContentView(R.layout.app_loading_dialog);
        tip = (TextView)findViewById(R.id.tv_tip);
    }

    public static AppLoadingView createView(Context context ,int theme) {
        return new AppLoadingView(context,theme);
    }

    public static AppLoadingView createView(Context context) {
        return createView(context, R.style.CommonDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void setTip(CharSequence charSequence) {
        tip.setText(charSequence);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void update() {

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
