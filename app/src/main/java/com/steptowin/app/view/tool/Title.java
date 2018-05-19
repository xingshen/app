package com.steptowin.app.view.tool;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.steptowin.app.view.activity.R;

/**
 * desc:封装对{@link android.support.v7.widget.Toolbar}的操作
 * author：zg
 * date:2016/4/13 0013
 * time:下午 4:31
 */
public class Title {
    Toolbar toolbar;

    private Title(Toolbar toolbar){
        this.toolbar = toolbar;
    }

    public static Title init(Activity activity){
        return new Title((Toolbar) activity.findViewById(R.id.toolbar));
    }

    public static Title init(Fragment fragment){

        return new Title((Toolbar) fragment.getView().findViewById(R.id.toolbar));
    }

    public static Title init(Toolbar toolbar){
        return new Title(toolbar);
    }

    public Title setTitle(CharSequence title){
        toolbar.setTitle(title);
        return this;
    }

    public Title setTitle(@StringRes int resId){
        toolbar.setTitle(resId);
        return this;
    }

    public Title setTitleTextColor(@ColorRes int resId){
        toolbar.setTitleTextColor(toolbar.getContext().getResources().getColor(resId));
        return this;
    }

    public Title setTitleTextColorInt(@ColorInt int color){
        toolbar.setTitleTextColor(color);
        return this;
    }

    public Title setNavigationIcon(@DrawableRes int resId){
        toolbar.setNavigationIcon(resId);
        return this;
    }

    public Title setNavigationIcon(Drawable drawable){
        toolbar.setNavigationIcon(drawable);
        return this;
    }

    public Title setNavigationOnClickListener(View.OnClickListener listener){
        toolbar.setNavigationOnClickListener(listener);
        return this;
    }

    /**
     * 添加菜单项,已经存在的菜单项不会被移除和覆盖
     * @param resId
     * @return
     */
    public Title addMenu(@MenuRes int resId){
        toolbar.inflateMenu(resId);
        return this;
    }

    public Title setMenuItemClickListener(Toolbar.OnMenuItemClickListener listener){
        toolbar.setOnMenuItemClickListener(listener);
        return this;
    }

    public Title setMenuItemVisiable(int id,boolean visiable){
        toolbar.getMenu().findItem(id).setVisible(visiable);
        return this;
    }

}
