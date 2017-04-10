package com.cpigeon.app.modular.footsearch.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.fragment.BaseFragment;
import com.cpigeon.app.modular.footsearch.activity.FootSearchActivity;
import com.cpigeon.app.modular.footsearch.model.bean.CpigeonServicesInfo;
import com.cpigeon.app.modular.footsearch.presenter.FootSearchPre;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;
import com.cpigeon.app.utils.customview.SearchEditText;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cpigeon.app.MyApp.mCpigeonData;

/**
 * Created by Administrator on 2017/4/5.
 */

public class FootSearchFragment extends BaseFragment implements IFootSearchView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_prompt_title)
    TextView tvPromptTitle;
    @BindView(R.id.tv_prompt_right)
    TextView tvPromptRight;
    @BindView(R.id.tv_openServices)
    TextView tvOpenServices;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;
    private View mView;
    private int userQueryTimes = 0;
    private boolean isDev = false;
    private boolean doubleClickCloseLoadingDialog = false;
    private ArrayList<CpigeonServicesInfo> allServicesInfo;
    private String queryKey;
    private SweetAlertDialog pDialog;
    private Callback.Cancelable mFoorSearchCancelable;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doubleClickCloseLoadingDialog = false;
        }
    };
    private FootSearchPre pre = new FootSearchPre(this);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_footsearch, container, false);
        ButterKnife.bind(this, mView);
        initToolbar();
        initView();
        return mView;
    }

    private void initToolbar() {
        toolbar.setTitle("足环查询");
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    private void initView() {
        pre.loadUserServiceInfo();

    }

    @Override
    public void onResume() {
        if (isDev) {
            super.onResume();
            return;
        }
        pre.loadUserServiceInfo();
        super.onResume();
    }

    @Override
    public void getFootSearchService(CpigeonUserServiceInfo info) {
        boolean noService = TextUtils.isEmpty(info.getName());
        tvPromptTitle.setText(noService ? "未购买套餐,最多显示2条结果" :
                info.getShowNumber() == 0 ? String.format("您当前使用%s,结果条数不限制", info.getPackageName()) :
                        String.format("您当前使用%s套餐，当前套餐最多显示%d条结果", info.getPackageName(), info.getShowNumber()));
        if (!noService) {
            tvPromptRight.setVisibility(View.VISIBLE);
            tvPromptRight.setText(String.format("套餐剩余查询次数:%s%s", info.getNumbers(), info.getUnitname()));
        }
        if (noService) {
            info = null;
        }
        userQueryTimes = noService ? -1 : info.getNumbers();
    }

    @Override
    public void queryFoot(Map<String, Object> map) {
        userQueryTimes = (int) map.get("rest");//设置套餐剩余次数

        CpigeonUserServiceInfo userData = mCpigeonData.getUserFootSearchServiceInfo();
        if (userData != null && (int) map.get("resultCount") > 0) {
            userData.setNumbers(userQueryTimes);
            mCpigeonData.setUserFootSearchServiceInfo(userData);
        }

        Intent intent = new Intent(getActivity(), FootSearchActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(FootSearchActivity.BUNDLE_FOOT_QUERY_RESULT_LIST, (ArrayList<FootSearchActivity>) map.get("data"));
        b.putString(FootSearchActivity.BUNDLE_KEY_SEARCH_KEY, getQueryKey());
        b.putSerializable(FootSearchActivity.BUNDLE_KEY_ALL_SERVICES_INFO, allServicesInfo);
        b.putSerializable(FootSearchActivity.BUNDLE_KEY_SEARCH_MAX_SHOW_COUNT, (int) map.get("rest") == -1 ? 0 : (int) map.get("maxShowCount"));
        b.putInt(FootSearchActivity.BUNDLE_KEY_SEARCH_ALL_COUNT, (int) map.get("rest") == -1 ? 0 : (int) map.get("resultCount"));
        intent.putExtra(FootSearchActivity.INTENT_DATA_KEY, b);
        startActivity(intent);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //更新套餐剩余信息
                CpigeonUserServiceInfo userPackageData = mCpigeonData.getUserFootSearchServiceInfo();
                if (userPackageData == null) {
                    tvPromptRight.setVisibility(View.GONE);
                } else {
                    tvPromptTitle.setText(userQueryTimes <= 0 ? "未购买套餐,最多显示2条结果" :
                            String.format("您当前使用%s，当前套餐最多显示%d条结果", userPackageData.getPackageName(), userPackageData.getShowNumber()));
                    if (userPackageData.getShowNumber() == 0) {
                        tvPromptTitle.setText(String.format("您当前使用%%s,结果条数不限制%s", userPackageData.getPackageName()));
                    }
                    tvPromptRight.setVisibility(View.VISIBLE);
                    tvPromptRight.setText(String.format("剩余查询次数:%s%s", userPackageData.getNumbers(), userPackageData.getUnitname()));
                }
                pDialog.dismiss();
            }
        });
    }

    @Override
    public String getQueryKey() {
        return queryKey;
    }

    @Override
    public String getQueryService() {
        return "足环查询服务";
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    private void footSearchRun(String queryKey) {
        this.queryKey = queryKey;
        if (TextUtils.isEmpty(queryKey)) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("请您输入足环号码")
                    .setConfirmText("知道了")
                    .show();

        } else {
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("搜索中...");
            pDialog.setCancelable(true);
            pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if (mFoorSearchCancelable != null && !mFoorSearchCancelable.isCancelled()) {
                        mFoorSearchCancelable.cancel();
                    }
                }
            });
            pDialog.show();
            pre.queryFoot();
        }



    }

    @OnClick({R.id.tv_openServices, R.id.tv_search, R.id.search_edittext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_openServices:

                break;
            case R.id.tv_search:
                footSearchRun(searchEdittext.getText().toString().trim());
                break;
            case R.id.search_edittext:
                break;
        }
    }


}
