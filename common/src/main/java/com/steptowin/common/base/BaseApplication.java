package com.steptowin.common.base;

import android.app.Application;

import com.steptowin.common.base.delegate.ApplicationDelegate;
import com.steptowin.common.base.delegate.ApplicationDelegateImp;
import com.steptowin.common.tool.context.AppStorage;
import com.steptowin.core.common.Constants;
import com.steptowin.core.common.AppVariables;
import com.steptowin.core.tools.ToastTool;
import com.steptowin.core.tools.WLog;

/**
 * @author zg
 * @Description 类描述：为自定义Application添加了工具方法的支持。
 * @date 创建日期
 * @record 修改记录：
 */
public abstract class BaseApplication extends Application {
    private ApplicationDelegate applicationProxy;

    @Override
    public void onCreate() {
        super.onCreate();
        initPre();
        init();
        initAfter();
    }

    protected void init() {
        AppStorage.setRootFilePath(this);
        applicationProxy = ApplicationDelegateImp.createProxy(this);
        AppVariables.put(Constants.KEY_APP_CONTEXT,this);
        if (!applicationProxy.isDebugMode()) {
            WLog.closeLogger();
            ToastTool.closeToast();
        }
    }

    /**
     * 初始化之前的操作
     */
    protected abstract void initPre();

    /**
     * 初始化之後的操作
     */
    protected abstract void initAfter();

}