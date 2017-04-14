package com.cpigeon.app.modular.footsearch.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/8.
 */

public interface ICpigeonServicesInfo extends IBaseDao{
    void getFootSearchService(String query,OnCompleteListener<CpigeonUserServiceInfo> listener);

    void queryFoot(String key,OnCompleteListener<Map<String, Object>> listener);
}
