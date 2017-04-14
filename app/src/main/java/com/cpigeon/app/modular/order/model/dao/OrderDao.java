package com.cpigeon.app.modular.order.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public interface OrderDao extends IBaseDao {
    void getUserAllOrder(int ps,int pi,String query,OnCompleteListener<List<CpigeonOrderInfo>> listener);

}
