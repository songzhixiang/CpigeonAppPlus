package com.cpigeon.app.commonstandard;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2017/4/11.
 */

public class AppManager {
    private Stack<WeakReference<Activity>> mActivityStack;
    private volatile static AppManager instance;

    private AppManager() {

    }
    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class){
                if(instance==null){
                    instance = new AppManager();
                }
            }

        }
        return instance;
    }

    /***
     * 栈中Activity的数
     *
     * @return Activity的数
     */
    public int stackSize() {
        return mActivityStack.size();
    }

    /***
     * 获得Activity栈
     *
     * @return Activity栈
     */
    public Stack<WeakReference<Activity>> getStack() {
        return mActivityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(WeakReference<Activity> activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    public void removeActivity(WeakReference<Activity> activity){
        if(mActivityStack!=null){
            mActivityStack.remove(activity);
        }
    }

    /***
     * 获取栈顶Activity（堆栈中最后一个压入的）
     *
     * @return Activity
     */
    public Activity getTopActivity() {
        Activity activity = mActivityStack.lastElement().get();
        return activity;
    }

    /***
     * 通过class 获取栈顶Activity
     *
     * @param cls
     * @return Activity
     */
    public Activity getActivityByClass(Class<?> cls) {
        Activity return_activity = null;
        for (WeakReference<Activity> activity : mActivityStack) {
            if (activity.get().getClass().equals(cls)) {
                return_activity = activity.get();
                break;
            }
        }
        return return_activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void killTopActivity() {
        try {
            WeakReference<Activity> activity = mActivityStack.lastElement();
            killActivity(activity);
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }

    /***
     * 结束指定的Activity
     *
     * @param activity
     */
    public void killActivity(WeakReference<Activity> activity) {
        try {
            Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> stackActivity = iterator.next();
                if(stackActivity.get()==null){
                    iterator.remove();
                    continue;
                }
                if (stackActivity.get().getClass().getName().equals(activity.get().getClass().getName())) {
                    iterator.remove();
                    stackActivity.get().finish();
                    break;
                }
            }
//            if (activity != null) {
//                mActivityStack.remove(activity);
//                activity.finish();
//                activity = null;
//            }
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }

    public void killAllToLoginActivity(Class<?> cls) {
        try {

            ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null && cls != activity.getClass()) {
                    listIterator.remove();
                    activity.finish();
                }
            }
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }

    /***
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void killActivity(Class<?> cls) {
        try {

            ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity == null) {
                    listIterator.remove();
                    continue;
                }
//                if (activity.getClass().getName().equals(cls.getName())) {
                if (activity.getClass() == cls) {
                    listIterator.remove();
                    if (activity != null) {
                        activity.finish();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }

    /**
     * 结束所有Activity
     */
    public void killAllActivity() {
        try {
            ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null) {
                    activity.finish();
                }
                listIterator.remove();
            }
//			for (int i = 0, size = mActivityStack.size(); i < size; i++) {
//				if (null != mActivityStack.get(i)) {
//					mActivityStack.get(i).finish();
//				}
//			}
//            mActivityStack.clear();
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit() {
        try {
            killAllActivity();
            Process.killProcess(Process.myPid());
        } catch (Exception e) {
            com.orhanobut.logger.Logger.e(e.getMessage());
        }
    }
}