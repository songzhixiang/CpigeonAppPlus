package com.cpigeon.app.commonstandard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cpigeon.app.R;
import com.cpigeon.app.broadcastreceiver.NetStateReceiver;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.networkstatus.NetChangeObserver;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.StatusBarSetting;
import com.cpigeon.app.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by Administrator on 2017/4/5.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements IView, SwipeBackActivityBase {
    public Context mContext;
    private Unbinder mUnbinder;
    private WeakReference<Activity> weakReference;
    private SwipeBackActivityHelper mHelper;
    private SwipeBackLayout mSwipeBackLayout;
    private SweetAlertDialog dialogPrompt;
    /**
     * 网络观察者
     */
    protected NetChangeObserver mNetChangeObserver = null;

    protected T mPresenter;
    /**
     * 加载中--对话框
     */
    protected SweetAlertDialog mLoadingSweetAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Window window = getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        doBeforeSetcontentView();
        setContentView(getLayoutId());

        if (canSwipeBack()) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
            mSwipeBackLayout = getSwipeBackLayout();
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
        mUnbinder = ButterKnife.bind(this);
        mContext = this;
        mPresenter = this.initPresenter();
        initView();
        // 网络改变的一个回掉类
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                onNetworkDisConnected();
            }
        };
        //开启广播去监听 网络 改变事件
        NetStateReceiver.registerObserver(mNetChangeObserver);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
//        Logger.e("当前" + this.getClass().getSimpleName() + "上面的栈内存Activity还有" + AppManager.getAppManager().stackSize());

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (canSwipeBack())
            mHelper.onPostCreate();
    }

    //获取布局文件
    @LayoutRes
    public abstract int getLayoutId();

    public abstract T initPresenter();

    public abstract void initView();

    /**
     * 是否可以滑动返回
     *
     * @return
     */
    public boolean canSwipeBack() {
        return false;
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (canSwipeBack() && v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarSetting.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarSetting.setColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarSetting.setTransparent(this);
    }

    /**
     * 设置沉浸式
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {
        weakReference = new WeakReference<Activity>(this);
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(weakReference);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null && !mPresenter.isDetached())
        {
            mPresenter.detach();
        }

        CommonTool.hideIME(this);
        mUnbinder.unbind();
        AppManager.getAppManager().removeActivity(weakReference);
    }

    @Override
    public void finish() {
        if (mPresenter != null) mPresenter.detach();
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    /**
     * 网络连接状态
     *
     * @param type 网络状态
     */
    protected abstract void onNetworkConnected(NetUtils.NetType type);

    /**
     * 网络断开的时候调用
     */
    protected abstract void onNetworkDisConnected();


    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {

        switch (tipType) {
            case Dialog:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText(getString(R.string.prompt))
                        .setContentText(tip)
                        .setConfirmText(getString(R.string.confirm)).show();
                return true;
            case DialogSuccess:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText("成功")
                        .setContentText(tip)
                        .setConfirmText(getString(R.string.confirm)).show();
                return true;
            case DialogError:
                dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                dialogPrompt.setCancelable(false);
                dialogPrompt.setTitleText("失败")
                        .setContentText(tip).
                        setConfirmText(getString(R.string.confirm)).show();
                return true;
            case View:
            case ViewSuccess:
            case ViewError:
                return false;
            case LoadingShow:
                if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                    mLoadingSweetAlertDialog.dismiss();
                mLoadingSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                mLoadingSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mLoadingSweetAlertDialog.setCancelable(true);
                mLoadingSweetAlertDialog.setTitleText(tip);
                mLoadingSweetAlertDialog.show();
                return true;
            case LoadingHide:
                if (mLoadingSweetAlertDialog != null && mLoadingSweetAlertDialog.isShowing())
                    mLoadingSweetAlertDialog.dismiss();
                return true;
            case ToastLong:
                ToastUtil.showToast(this, tip, Toast.LENGTH_LONG);
                return true;
            case ToastShort:
                ToastUtil.showToast(this, tip, Toast.LENGTH_SHORT);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        if (tag == 0)
            return showTips(tip, tipType);
        return false;
    }

    /**
     * 检查是否登录
     *
     * @return
     */
    @Override
    public boolean checkLogin() {
        try {
            boolean res = (boolean) SharedPreferencesTool.Get(this, "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);
            res &= SharedPreferencesTool.Get(this, "userid", 0, SharedPreferencesTool.SP_FILE_LOGIN).equals(
                    Integer.valueOf(EncryptionTool.decryptAES(SharedPreferencesTool.Get(this, "token", "", SharedPreferencesTool.SP_FILE_LOGIN).toString()).split("\\|")[0]));
            return res;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取用户登录信息
     *
     * @return
     */
    public Map<String, Object> getLoginUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", getString(R.string.user_name));
        map.put("touxiang", "");
        map.put("touxiangurl", "");
        map.put("nicheng", "");
        map.put("userid", 0);
        map.put("phone", "");
        return SharedPreferencesTool.Get(this, map, SharedPreferencesTool.SP_FILE_LOGIN);
    }

    /**
     * 清除用户登录信息
     */
    protected void clearLoginInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "");
        map.put("token", "");
        map.put("touxiang", "");
        map.put("touxiangurl", "");
        map.put("nicheng", "");
        map.put("logined", false);
        map.put("sltoken", "");
        SharedPreferencesTool.Save(mContext, map, SharedPreferencesTool.SP_FILE_LOGIN);
        CpigeonData.getInstance().initialization();
    }
}
