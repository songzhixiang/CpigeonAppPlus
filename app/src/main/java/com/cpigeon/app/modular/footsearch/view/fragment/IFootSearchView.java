package com.cpigeon.app.modular.footsearch.view.fragment;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/8.
 */

public interface IFootSearchView extends IView{
    void getFootSearchService(CpigeonUserServiceInfo info);
    void queryFoot(Map<String, Object> map);
    String getQueryKey();
    String getQueryService()
;}
