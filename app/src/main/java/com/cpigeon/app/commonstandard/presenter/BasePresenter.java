package com.cpigeon.app.commonstandard.presenter;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.WeakHandler;

import org.xutils.common.Callback;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class BasePresenter<TView extends IView, TDao extends IBaseDao> {

    protected TView mView;

    protected TDao mDao;

    private WeakHandler mHandler;

    private WeakHashMap<String, Callback.Cancelable> mCancelableWeakHashMap;

    public BasePresenter(TView mView) {
        onAttach();
        this.mView = mView;
        onAttached();
        mCancelableWeakHashMap = new WeakHashMap<>();
        this.mHandler = new WeakHandler();
        mDao = initDao();
    }

    protected abstract TDao initDao();

    /**
     * 是否已绑定视图
     *
     * @return
     */
    public boolean isAttached() {
        return mView != null;
    }

    /**
     * 是否已释放视图
     *
     * @return
     */
    public boolean isDetached() {
        return mView == null;
    }

    /**
     * 解除绑定，释放视图的引用
     */
    public void detach() {
        onDetach();
        this.mView = null;
        this.mHandler = null;
        if (mDao != null)
        {
            mDao = null;
        }
        if (mCancelableWeakHashMap != null)
            for (String key : mCancelableWeakHashMap.keySet()) {
                Callback.Cancelable cancelable = mCancelableWeakHashMap.get(key);
                if (cancelable != null && !cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        onDetached();
    }

    /**
     * 添加需要在解绑视图时取消请求的Cancelable
     * <p>key若在Map中存在，将会取消Map中的Cancelable，并将新的Cancelable放入Map中</p>
     *
     * @param key
     */
    public void addCancelableIntoMap(String key, Callback.Cancelable cancelable) {
        if (mCancelableWeakHashMap.containsKey(key)) {
            Callback.Cancelable temp = mCancelableWeakHashMap.get(key);
            if (temp != null && !temp.isCancelled()) {
                temp.cancel();
            }
            mCancelableWeakHashMap.remove(key);
        }
        mCancelableWeakHashMap.put(key, cancelable);
    }

    /**
     * 绑定视图之前
     */
    public void onAttach() {
    }

    /**
     * 绑定视图之后
     */
    public void onAttached() {
    }

    /**
     * 解绑视图之前
     */
    public void onDetach() {
    }

    /**
     * 解绑视图之后
     */
    public void onDetached() {
    }

    /**
     * 执行Handler.post
     *
     * @param r
     */
    public void post(@NonNull CheckAttachRunnable r) {
        if (mHandler == null || r == null || isDetached()) return;
        mHandler.post(r);
    }

    /**
     * 执行Handler.postDelayed
     *
     * @param r
     * @param delayMillis
     */
    public void postDelayed(@NonNull CheckAttachRunnable r, long delayMillis) {
        if (mHandler == null || r == null || isDetached()) return;
        mHandler.postDelayed(r, delayMillis);
    }

    /**
     * 自动检查Presenter与视图的绑定情况的Runnable
     */
    public abstract class CheckAttachRunnable implements Runnable {
        @Override
        public void run() {
            if (isAttached()) {
                runAttached();
            } else {
                runDetached();
            }
        }

        /**
         * 绑定情况下
         */
        protected abstract void runAttached();

        /**
         * 解绑或未绑定情况下
         */
        protected void runDetached() {
        }
    }
}