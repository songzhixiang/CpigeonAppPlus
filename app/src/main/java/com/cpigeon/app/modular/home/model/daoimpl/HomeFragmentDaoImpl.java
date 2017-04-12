package com.cpigeon.app.modular.home.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.modular.home.model.bean.HomeAd;
import com.cpigeon.app.modular.home.model.dao.IHomeFragmentDao;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class HomeFragmentDaoImpl implements IHomeFragmentDao {
    @Override
    public void loadHomeAd(OnLoadCompleteListener onLoadCompleteListener) {

        String adJsonStr = (String) SharedPreferencesTool.Get(MyApp.getInstance(), "ad", "");
        try {
            JSONArray array = new JSONArray(adJsonStr);
            if (array.length()>0)
            {
                List<HomeAd> adList = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date _begin, _end;
                for (int i = 0; i < array.length(); i++) {
                    try {
                        _begin = sdf.parse(array.getJSONObject(i).getString("start"));
                        _end = sdf.parse(array.getJSONObject(i).getString("end"));
                        if (array.getJSONObject(i).getInt("type") == 2 &&
                                array.getJSONObject(i).getBoolean("enable") == true &&
                                _begin.getTime() < new Date().getTime() && _end.getTime() > new Date().getTime()) {
                            final String imageUrl = array.getJSONObject(i).getString("adImageUrl");
                            final String adurl = array.getJSONObject(i).getString("adUrl");
                            Logger.i("第" + (i + 1) + "个下方广告;URL=" + imageUrl);
                            adList.add(new HomeAd(imageUrl, adurl));
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                if (onLoadCompleteListener!=null)
                {
                    onLoadCompleteListener.onLoadSuccess(adList);
                }
            }
        } catch (JSONException e) {
            onLoadCompleteListener.onLoadFailed(e.getMessage());
        }
    }
}
