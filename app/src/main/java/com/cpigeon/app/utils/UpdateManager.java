package com.cpigeon.app.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;

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

    private onInstallAppListener onInstallAppListener;
    private Context mContext;
    //    NotificationManager mNotifyManager;
//    NotificationCompat.Builder mBuilder;
    private ProgressDialog mProgressDialog;


    public UpdateManager(Context context) {
        mContext = context;
//        File file = new File(CpigeonConfig.UPDATE_SAVE_PATH);
//        if (file.exists())
//            file.delete();
    }

    public UpdateManager setOnInstallAppListener(UpdateManager.onInstallAppListener onInstallAppListener) {
        this.onInstallAppListener = onInstallAppListener;
        return this;
    }

    /**
     * 检查App更新
     */
    public void checkUpdate() {
        //从服务器获取更新信息
        RequestParams params = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.UPDATE_CHECK_URL);
        CallAPI.pretreatmentParams(params);
        params.setCacheMaxAge(CpigeonConfig.CACHE_UPDATE_INFO_TIME);//设置缓存时间
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.i(result);
                try {
                    JSONArray array = new JSONArray(result);
                    if (array.length() > 0) {
                        JSONObject obj;
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
                            if (obj.getString("packageName").equals(mContext.getPackageName())) {
                                if (obj.getInt("verCode") > CommonTool.getVersionCode(mContext)) {
                                    updateReady(obj.getInt("verCode"),
                                            obj.getString("packageName"),
                                            obj.getString("verName"),
                                            obj.getString("url"),
                                            obj.getString("updateExplain"),
                                            obj.getString("updateTime"),
                                            obj.has("force") ? obj.getBoolean("force") : false);
                                }
                                return;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 下载准备
     *
     * @param varCode     版本号
     * @param packageName
     * @param verName     版本名称
     * @param url         下载url (绝对路径)
     * @param explain     更新说明
     * @param updateTime  更新时间
     */
    public void updateReady(final int varCode, final String packageName, String verName, String url, String explain, String updateTime) {
        updateReady(varCode, packageName, verName, url, explain, updateTime, false);
    }


    /**
     * @param varCode     版本号
     * @param packageName
     * @param verName     版本名称
     * @param url         下载url (绝对路径)
     * @param explain     更新说明
     * @param updateTime  更新时间
     * @param force       是否强制下载，不下载则退出程序
     */
    public void updateReady(final int varCode, final String packageName, String verName, String url, String explain, String updateTime, final boolean force) {
        final String _url = url;
        //Logger.i("下载");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("当前版本:" + CommonTool.getVersionName(mContext)
                + "\n更新时间：" + updateTime
                + "\n更新说明：\n" + explain
        );
        builder.setTitle("发现新版本:" + verName);
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
                    if (info != null && info.versionCode == varCode && info.applicationInfo.packageName.equals(packageName)) {
                        install(path);
                        return;
                    }
                }
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_READY, SharedPreferencesTool.SP_FILE_APPUPDATE);
                DownLoad(_url, path);

            }
        });
        builder.setNegativeButton(force ? "退出程序" : "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE);
                if (force)
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
//        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mBuilder = new NotificationCompat.Builder(context);
//        mBuilder.setContentTitle("版本更新")
//                .setContentText("正在下载......")
//                .setContentInfo("0%")
//                .setSmallIcon(R.mipmap.logo);

        mProgressDialog = new ProgressDialog(mContext);
//        dialog.setTitle(title);
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
                //Toast.makeText(x.app(), "开始下载", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                BigDecimal b = new BigDecimal((float) current / (float) total);
                float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
//                mBuilder.setProgress(100, (int) (f1*100), false);
//                mBuilder.setContentInfo((int) (f1*100) + "%");
//                mNotifyManager.notify(1, mBuilder.build());
                if (mProgressDialog.getMax() == 100)
                    mProgressDialog.setMax((int) (total / 1024));
                mProgressDialog.setMessage("正在下载...");
                mProgressDialog.setProgress((int) (f1 * mProgressDialog.getMax()));
            }

            @Override
            public void onSuccess(File result) {
//                mBuilder.setContentText("正在下载...")
//                        // Removes the progress bar
//                        .setProgress(0, 0, false);
//                mNotifyManager.notify(1, mBuilder.build());
//                mNotifyManager.cancel(1);

                //Toast.makeText(x.app(), "下载成功", Toast.LENGTH_LONG).show();
                mProgressDialog.setMessage("下载完成");
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                //删除登录信息
                // SharedPreferencesTool.Save(mContext, "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);

                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_FINISHED, SharedPreferencesTool.SP_FILE_APPUPDATE);
                install(path);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(x.app(), "下载失败", Toast.LENGTH_LONG).show();
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
        if(onInstallAppListener!=null)
            onInstallAppListener.onInstallApp();
    }

    public void closeAndExit(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(startMain);
        System.exit(0);
    }
    public  interface  onInstallAppListener{
        void onInstallApp();
    }
}