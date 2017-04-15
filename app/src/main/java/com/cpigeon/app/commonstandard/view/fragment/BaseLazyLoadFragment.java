package com.cpigeon.app.commonstandard.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class BaseLazyLoadFragment<Pre extends BasePresenter> extends BaseMVPFragment<Pre> {

    protected boolean isVisible;

    protected boolean isFristLoad = true;
    protected boolean isCreateView = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Logger.e("setUserVisibleHint " + TAG + " isFristLoad=" + isFristLoad + "；isVisible=" + isVisible);
        canLoadLazy();
    }

    private void canLoadLazy() {
        isVisible = getUserVisibleHint();
        if (isVisible && isFristLoad && isCreateView) {
            lazyLoad();
            isFristLoad = false;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCreateView = true;
        Logger.e("onActivityCreated " + TAG + " isFristLoad=" + isFristLoad + "；isVisible=" + isVisible);
        canLoadLazy();
    }

    /***
     * 懒加载，仅第一次执行
     */
    protected abstract void lazyLoad();
}
