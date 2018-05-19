package com.steptowin.common.tool;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @Desc: 为view提供工具方法（代码复用）
 * @Author: zg
 * @Time: 2016/2/17 16:47
 */
public class ViewTool {

    /**
     * 将与某个View关联的软键盘隐藏掉。
     *
     * @param view
     */
    public static void hideInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                0);

    }
}
