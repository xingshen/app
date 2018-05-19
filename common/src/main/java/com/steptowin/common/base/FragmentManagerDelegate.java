package com.steptowin.common.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.steptowin.common.base.delegate.ApplicationDelegate;
import com.steptowin.common.base.delegate.ApplicationDelegateImp;

import java.util.List;

/**
 * fragment管理类
 */
public class FragmentManagerDelegate {
    /**
     * fragment切换时，是否带有动画.查看{@link ApplicationDelegateImp#enterAnim()}
     */
    private boolean switchAnimEnable = true;
    public FragmentManager fragmentManager;
    private FragmentActivity fragmentActivity;
    ApplicationDelegate mApplication;

    public FragmentManagerDelegate(FragmentActivity fragment) {
        fragmentActivity = fragment;
        fragmentManager = fragment.getSupportFragmentManager();
        mApplication = ApplicationDelegateImp.createProxy(fragment.getApplication());
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setAnimEnable(boolean flag) {
        switchAnimEnable = flag;
    }

    /**
     * @param @param fragment
     * @param @param isanim
     * @return void
     * @Description: 添加新的fragment
     */
    public void addFragment(int viewId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (switchAnimEnable) {
            fragmentTransaction.setCustomAnimations(mApplication.enterAnim(), mApplication.exitAnim(), mApplication.popEnterAnim(), mApplication.popExitAnim());
        }

        if (addToBackStack)
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());

        fragmentTransaction.add(viewId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void addFragment(int viewId, Fragment fragment) {
        addFragment(viewId, fragment, false);
    }

    public void addFragment(Fragment fragment){
        if (null != fragmentActivity && fragmentActivity instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) fragmentActivity;
            addFragment(activity.getFragmentContainerId(), fragment);
        }
    }

    /**
     * @desc 流程式fragment切换, 建议使用
     * 同级fragment切换,使用{@link #switchFragment(int, Fragment)}
     * <p/>
     * edit by zg
     */
    public void replaceFragment(int viewId, Fragment fragment) {
        replaceFragment(viewId, fragment, switchAnimEnable);
    }

    public void replaceFragment(int viewId, Fragment fragment, boolean anim) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (anim) {
            fragmentTransaction.setCustomAnimations(mApplication.enterAnim(), mApplication.exitAnim(), mApplication.popEnterAnim(), mApplication.popExitAnim());
        }
        fragmentTransaction.replace(viewId, fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * @desc 移除加入堆栈中的最后一个fragment, 触发返回键, 系统会自动移除
     * <p/>
     * edit by zg
     */
    public boolean removeFragment() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    /**
     * @desc 同级式fragment切换, 建议使用.由于通常是开关或者tab切换手动显示与隐藏,所以,默认是不加入返回栈.
     * <p/>
     * edit by zg
     */
    public void switchFragment(int viewId, Fragment fragment) {
        switchFragment(viewId, fragment, false);
    }

    public void switchFragment(int viewId, Fragment fragment, boolean addToBack) {
        switchFragment(viewId, fragment, addToBack, false);
    }

    public void switchFragment(int viewId, Fragment fragment, boolean addToBack, boolean anim) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (anim)
            transaction.setCustomAnimations(mApplication.enterAnim(), mApplication.exitAnim(), mApplication.popEnterAnim(), mApplication.popExitAnim());
        if (null == fragments || fragments.size() == 0 || !fragments.contains(fragment)) {
            transaction.add(viewId, fragment);
            if (addToBack)
                transaction.addToBackStack(fragment.getClass().getSimpleName());

        } else {
            for (Fragment f : fragments) {
                if (f == null)
                    continue;
                if (f == fragment) {
                    transaction.show(f);
                } else {
                    transaction.hide(f);
                }
            }
        }
        transaction.commitAllowingStateLoss();
    }


}
