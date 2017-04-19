package com.cpigeon.app.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.cpigeon.app.MyApp;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

//import com.cpigeon.common.AppManager;

/**
 * Created by Administrator on 2016-10-20.
 */

public class UpdateManager {
    private static final String DOWNLOAD_STATE_NO = "no";
    private static final String DOWNLOAD_STATE_READY = "ready";
    private static final String DOWNLOAD_STATE_ERROR = "error";
    private static final String DOWNLOAD_STATE_CANCEL = "cancel";
    private static final String DOWNLOAD_STATE_FINISHED = "finished";

    private OnInstallAppListener onInstallAppListener;
    private OnCheckUpdateInfoListener onCheckUpdateInfoListener;
    private Context mContext;
    private ProgressDialog mProgressDialog;


    public UpdateManager(Context context) {
        mContext = context;
    }

    public void setOnCheckUpdateInfoListener(OnCheckUpdateInfoListener onCheckUpdateInfoListener) {
        this.onCheckUpdateInfoListener = onCheckUpdateInfoListener;
    }

    public UpdateManager setOnInstallAppListener(UpdateManager.OnInstallAppListener onInstallAppListener) {
        this.onInstallAppListener = onInstallAppListener;
        return this;
    }

    /**
     * 检查App更新(从服务器拉取数据)
     */
    public void checkUpdate() {
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onGetUpdateInfoStart();
        //从服务器获取更新信息
        CallAPI.getUpdateInfo(new CallAPI.Callback<List<UpdateInfo>>() {
            @Override
            public void onSuccess(List<UpdateInfo> data) {
                if (onCheckUpdateInfoListener != null) {
                    if (!onCheckUpdateInfoListener.onGetUpdateInfoEnd(data))
                        checkUpdate(data);
                } else {
                    checkUpdate(data);
                }
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCheckUpdateInfoListener != null)
                    onCheckUpdateInfoListener.onGetUpdateInfoEnd(null);
            }
        });
    }

    /**
     * 检查App更新
     */

    private void checkUpdate(List<UpdateInfo> updateInfos) {
        if (updateInfos == null || updateInfos.size() == 0) return;
        for (UpdateInfo updateInfo : updateInfos) {
            if (updateInfo.getPackageName().equals(mContext.getPackageName())) {
                if (updateInfo.getVerCode() > CommonTool.getVersionCode(mContext)) {
                    updateReady(updateInfo);
                    return;
                }
            }
        }
    }

    /**
     * 下载准备
     */

    private void updateReady(final UpdateInfo updateInfo) {
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onHasUpdate(updateInfo);
        final String _url = updateInfo.getUrl();
        //Logger.i("下载");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("当前版本:" + CommonTool.getVersionName(mContext)
                + "\n更新时间：" + updateInfo.getUpdateTime()
                + "\n更新说明：\n" + updateInfo.getUpdateExplain()
        );
        builder.setTitle("发现新版本:" + updateInfo.getVerName());
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = CpigeonConfig.UPDATE_SAVE_FOLDER + mContext.getPackageName() + ".apk";
                //判断当前是否已经下载了
                if (SharedPreferencesTool.Get(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE).equals(UpdateManager.DOWNLOAD_STATE_FINISHED)) {
                    //获取APK信息
                    PackageManager pm = mContext.getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                    //判断是否是最新版本的APK文件
                    if (info != null && info.versionCode == updateInfo.getVerCode() && info.applicationInfo.packageName.equals(updateInfo.getPackageName())) {
                        install(path);
                        return;
                    }
                }
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_READY, SharedPreferencesTool.SP_FILE_APPUPDATE);
                DownLoad(_url, path);

            }
        });
        builder.setNegativeButton(updateInfo.isForce() ? "退出程序" : "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE);
                if (updateInfo.isForce())
                    System.exit(0);
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 下载安装包
     *
     * @param url  下载路径
     * @param path 保存路径
     */
    private void DownLoad(String url, final String path) {
        Logger.i(path);
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onDownloadStart();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("准备下载");
        mProgressDialog.setCancelable(false);// 设置点击空白处也不能关闭该对话框
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置采用圆形进度条
        mProgressDialog.setMax(100);
//      mProgressDialog.setIndeterminate(true);//设置不显示明确的进度
        mProgressDialog.setIndeterminate(false);// 设置显示明确的进度
        mProgressDialog.setProgressNumberFormat("%1dK/%2dK");
        mProgressDialog.show();
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(path);

        x.http().get(params, new Callback.ProgressCallback<File>() {


            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                mProgressDialog.setMessage("开始下载");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                BigDecimal b = new BigDecimal((float) current / (float) total);
                float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                if (mProgressDialog.getMax() == 100)
                    mProgressDialog.setMax((int) (total / 1024));
                mProgressDialog.setMessage("正在下载...");
                mProgressDialog.setProgress((int) (f1 * mProgressDialog.getMax()));
            }

            @Override
            public void onSuccess(File result) {

                mProgressDialog.setMessage("下载完成");
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_FINISHED, SharedPreferencesTool.SP_FILE_APPUPDATE);
                install(path);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                ToastUtil.showToast(MyApp.getInstance(), "下载失败", Toast.LENGTH_LONG);
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_ERROR, SharedPreferencesTool.SP_FILE_APPUPDATE);
                closeAndExit();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_CANCEL, SharedPreferencesTool.SP_FILE_APPUPDATE);
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFinished() {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    private void install(String path) {
        CommonTool.installApp(mContext, path);
        if (onInstallAppListener != null)
            onInstallAppListener.onInstallApp();
    }

    private void closeAndExit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(startMain);
        System.exit(0);
    }

    public interface OnCheckUpdateInfoListener {
        void onGetUpdateInfoStart();

        boolean onGetUpdateInfoEnd(List<UpdateInfo> updateInfos);

        void onHasUpdate(UpdateInfo updateInfo);

        void onDownloadStart();
    }

    public interface OnInstallAppListener {
        void onInstallApp();
    }

    /**
     * Created by chenshuai on 2017/4/18.
     * 应用更新信息类
     */

    public static class UpdateInfo {

        /**
         * appName : 中鸽网
         * packageName : com.cpigeon.app
         * url : http://192.168.0.52:8888/Apk/201703100-release.apk
         * verName : 1.1.1
         * verCode : 201703110
         * updateTime : 2017/3/10
         * updateExplain : 1.新增用户个人信息中心，可进行个人信息的设置
         * 2.新增用户签到
         * 3.新增余额充值入口，余额充值更方便
         * 4.新增余额充值记录，充值记录更直观
         * 5.优化界面,修复BUG
         * force : true
         */

        private String appName;//应用名称
        private String packageName;//应用包名
        private String url;//下载url (绝对路径)
        private String verName;//版本名称
        private int verCode;//版本号
        private String updateTime;//更新时间
        private String updateExplain;//更新说明
        private boolean force;//是否强制下载，不下载则退出程序

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVerName() {
            return verName;
        }

        public void setVerName(String verName) {
            this.verName = verName;
        }

        public int getVerCode() {
            return verCode;
        }

        public void setVerCode(int verCode) {
            this.verCode = verCode;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateExplain() {
            return updateExplain;
        }

        public void setUpdateExplain(String updateExplain) {
            this.updateExplain = updateExplain;
        }

        public boolean isForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }
    }

}