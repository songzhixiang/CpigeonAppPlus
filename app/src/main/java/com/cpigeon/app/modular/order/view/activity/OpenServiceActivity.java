package com.cpigeon.app.modular.order.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.bean.CpigeonServicesInfo;
import com.cpigeon.app.modular.order.presenter.OpenServicePresenter;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOpenServiceView;
import com.cpigeon.app.modular.order.view.adapter.ServiceListAdapter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class OpenServiceActivity extends BaseActivity<OpenServicePresenter> implements IOpenServiceView {
    public static final String INTENT_DATA_KEY_SERVICENAME = "service_name";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private String mServiceName;
    private ServiceListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_services_open;
    }

    @Override
    public OpenServicePresenter initPresenter() {
        return new OpenServicePresenter(this);
    }

    @Override
    public void initView() {
        mServiceName = getIntent().getStringExtra(INTENT_DATA_KEY_SERVICENAME);
        if (TextUtils.isEmpty(mServiceName))
            throw new NullPointerException("service name is empty");
        toolbar.setTitle(String.format("开通%s", TextUtils.isEmpty(mServiceName) ? "服务" : mServiceName));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new ServiceListAdapter();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final CpigeonServicesInfo cpigeonServicesInfo = (CpigeonServicesInfo) adapter.getData().get(position);
                if (CpigeonData.getInstance().getUserServicesIds().contains(cpigeonServicesInfo.getId()))
                    return;
                if (DateTool.strToDateTime(cpigeonServicesInfo.getOpentime()).getTime() > System.currentTimeMillis()) {
                    showTips(cpigeonServicesInfo.getPackageName() + "尚未上架，请上架后再来购买", TipType.Dialog);
                    return;
                }
                if (DateTool.strToDateTime(cpigeonServicesInfo.getExpiretime()).getTime() < System.currentTimeMillis()) {
                    showTips(cpigeonServicesInfo.getPackageName() + "已下架，感谢您的支持", TipType.Dialog);
                    return;
                }
                SweetAlertDialog dialog = new SweetAlertDialog(mContext);
                dialog.setConfirmText("确认");
                dialog.setTitleText("提示");
                dialog.setContentText(String.format("确认开通%s?", cpigeonServicesInfo.getPackageName()));
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.openService(cpigeonServicesInfo.getId());
                        sweetAlertDialog.dismiss();
                    }
                });
                dialog.setCancelText("取消");
                dialog.show();
            }
        });
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.loadAllServicesInfo();
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        if (tag == TAG_LoadServiceInfo) {
            return true;
        }
        return super.showTips(tip, tipType, tag);
    }

    @Override
    public String getOpenServiceName() {
        return mServiceName;
    }

    @Override
    public void showServicesInfo(List<CpigeonServicesInfo> infoList) {
        mAdapter.addData(infoList);
    }

    @Override
    public void createServiceOrderSuccess(CpigeonOrderInfo orderInfo) {
        Intent intent = new Intent(mContext, OrderPayActivity.class);
        intent.putExtra(OrderPayActivity.INTENT_DATA_KEY_ORDERINFO, orderInfo);
        startActivity(intent);
        ((BaseActivity) mContext).finish();
    }
}
