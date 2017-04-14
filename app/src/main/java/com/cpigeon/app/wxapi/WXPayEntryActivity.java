package com.cpigeon.app.wxapi;

//import com.cpigeon.common.AppManager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017/1/11.
 * 微信支付回调Activity
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    //微信开放平台appID
    public static final String APP_ID = "wxc9d120321bd1180a";
    private IWXAPI api;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 得到支付结果回调
     */
    @Override
    public void onResp(BaseResp baseResp) {
//        0 支付成功
//        -1 发生错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//        -2 用户取消 发生场景：用户不支付了，点击取消，返回APP。
        Logger.d("errCode = " + baseResp.errCode + ";errStr = " + baseResp.errStr);// 支付结果码
        CpigeonData.getInstance().onWxPay(this, baseResp.errCode);
        this.finish();
    }
}
