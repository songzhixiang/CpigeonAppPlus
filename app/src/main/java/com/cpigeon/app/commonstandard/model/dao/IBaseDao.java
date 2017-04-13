package com.cpigeon.app.commonstandard.model.dao;

import android.media.SoundPool;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface IBaseDao {
    interface OnCompleteListener<T> {
        void onSuccess(T data);

        void onFail(String msg);
    }
}
