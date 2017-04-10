package com.cpigeon.app.networkstatus;

import com.cpigeon.app.utils.NetUtils;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface NetChangeObserver {
    /**
     * 网络连接回调 type为网络类型
     */
    void onNetConnected(NetUtils.NetType type);

    /**
     * 没有网络
     */
    void onNetDisConnect();
}
