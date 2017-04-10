package com.cpigeon.app.modular.footsearch.presenter;

import android.os.Handler;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.footsearch.view.fragment.IFootSearchView;
import com.cpigeon.app.modular.footsearch.model.dao.ICpigeonServicesInfo;
import com.cpigeon.app.modular.footsearch.model.daoimpl.CpigeonServicesInfoImpl;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/8.
 */

public class FootSearchPre {
    private IFootSearchView iFootSearchView;
    private ICpigeonServicesInfo iCpigeonServicesInfo;
    private Handler mHandler = new Handler();

    public FootSearchPre(IFootSearchView iFootSearchView) {
        this.iFootSearchView = iFootSearchView;
        this.iCpigeonServicesInfo = new CpigeonServicesInfoImpl();
    }
    public void loadUserServiceInfo()
    {
        iFootSearchView.showLoading();
        iCpigeonServicesInfo.getFootSearchService(iFootSearchView.getQueryService(), new ICpigeonServicesInfo.OnLoadCompleteListener() {
            @Override
            public void loadSuccess(final CpigeonUserServiceInfo serviceInfo) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFootSearchView.hideLoading();
                        iFootSearchView.getFootSearchService(serviceInfo);

                    }
                });
            }

            @Override
            public void laodFailed(String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFootSearchView.hideLoading();
                        iFootSearchView.showTips("加载用户套餐失败", IView.TipType.ToastShort);
                    }
                });

            }
        });
    }

    public void queryFoot(){
        iFootSearchView.showLoading();
        iCpigeonServicesInfo.queryFoot(iFootSearchView.getQueryKey(), new ICpigeonServicesInfo.OnQueryCompleteListener() {
            @Override
            public void querySuccess(final Map<String, Object> map) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFootSearchView.queryFoot(map);
                        iFootSearchView.hideLoading();
                    }
                });
            }

            @Override
            public void laodFailed(String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFootSearchView.hideLoading();
                        iFootSearchView.showTips("加载用户套餐失败", IView.TipType.ToastShort);
                    }
                });
            }
        });
    }
}
