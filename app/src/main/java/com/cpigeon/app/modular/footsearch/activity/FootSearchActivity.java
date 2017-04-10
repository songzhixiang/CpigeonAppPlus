package com.cpigeon.app.modular.footsearch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.activity.BaseActivity;
import com.cpigeon.app.commonstandard.dao.IOnKeyDownForFragment;
import com.cpigeon.app.modular.footsearch.fragment.FootSearchResultCardFragment;
import com.cpigeon.app.modular.footsearch.model.bean.CpigeonServicesInfo;
import com.cpigeon.app.modular.footsearch.model.bean.FootQueryResult;
import com.cpigeon.app.utils.NetUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 */

public class FootSearchActivity extends BaseActivity{

    public static final String INTENT_DATA_KEY = "intent_data_key";
    public static final String BUNDLE_FOOT_QUERY_RESULT_LIST = "bundel_search_result_data";
    public static final String BUNDLE_KEY_SEARCH_KEY = "key";
    private static final String BUNDLE_KEY_SEARCH_RESULT_COUNT = "count";
    public static final String BUNDLE_KEY_SEARCH_ALL_COUNT = "count_all";
    public static final String BUNDLE_KEY_SEARCH_MAX_SHOW_COUNT = "max_show_count";
    public static final String BUNDLE_KEY_ALL_SERVICES_INFO = "services_info";
    private FootSearchResultCardFragment mFootSearchFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footsearch);
        initView();
    }

    private void initView() {
        //setActionbarTitleText("查询结果");
        mFootSearchFragment = new FootSearchResultCardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mFootSearchFragment).commit();
        Bundle b = getIntent().getBundleExtra(INTENT_DATA_KEY);
        mFootSearchFragment.setData((List<FootQueryResult>) b.getSerializable(BUNDLE_FOOT_QUERY_RESULT_LIST));
        mFootSearchFragment.setSearchKey(b.getString(BUNDLE_KEY_SEARCH_KEY, ""));
        if (b.getInt(BUNDLE_KEY_SEARCH_RESULT_COUNT) > 0) {
            mFootSearchFragment.setSearchResultCount(b.getInt(BUNDLE_KEY_SEARCH_RESULT_COUNT));
        }
        // mFootSearchResultFragment.setUserPackageData((Map<String, Object>) b.getSerializable(BUNDLE_KEY_USER_SERVICE_INFO));
        if (b.getInt(BUNDLE_KEY_SEARCH_MAX_SHOW_COUNT) > 0) {
            mFootSearchFragment.setMaxShowCount(b.getInt(BUNDLE_KEY_SEARCH_MAX_SHOW_COUNT));
        }
        mFootSearchFragment.setAllServicesInfo((List<CpigeonServicesInfo>) b.getSerializable(BUNDLE_KEY_ALL_SERVICES_INFO));
        mFootSearchFragment.setResultAllCount(b.getInt(BUNDLE_KEY_SEARCH_ALL_COUNT));
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFootSearchFragment != null && mFootSearchFragment instanceof IOnKeyDownForFragment) {
            return ((IOnKeyDownForFragment) mFootSearchFragment).onAcvivityKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
