package com.cpigeon.app.commonstandard.listener;

import android.view.KeyEvent;

/**
 * Created by Administrator on 2017/1/6.
 * fragment 继承，当activity触发KeyDown事件时回调
 */

public interface IOnKeyDownForFragment {
    boolean onAcvivityKeyDown(int keyCode, KeyEvent event);
}
