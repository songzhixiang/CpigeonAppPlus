package com.cpigeon.app.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/6.
 */

public class CommonTool {


    //public final static String PATTERN_EMAIL = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    public final static String PATTERN_TENDIGITS = "^[1-9]\\d{9}$";
    public final static String PATTERN_PHONE = "^[1][3-8]+\\d{9}$";
    public final static String PATTERN_ALLNUMBER = "^\\d+$";
    public final static String PATTERN_URL = "^((https|http|ftp|rtsp|mms)?://)"
            + "?(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" //ftp的user@
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"                                 // IP形式的URL- 199.194.52.184
            + "|"                                                         // 允许IP和DOMAIN（域名）
            + "([0-9a-zA-Z_!~*'()-]+\\.)*"                                 // 域名- www.
            + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\."                     // 二级域名
            + "[a-zA-Z]{2,6})"                                         // first level domain- .com or .museum
            + "(:[0-9]{1,4})?"                                                     // 端口- :80
            + "((/?)|"
            + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
    public final static String PATTERN_WEB_URL = "^((https|http)?://)"
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"                                 // IP形式的URL- 199.194.52.184
            + "|"                                                         // 允许IP和DOMAIN（域名）
            + "([0-9a-zA-Z_!~*'()-]+\\.)*"                                 // 域名- www.
            + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\."                     // 二级域名
            + "[a-zA-Z]{2,6})"                                         // first level domain- .com or .museum
            + "(:[0-9]{1,4})?"                                                     // 端口- :80
            + "((/?)|"
            + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
    private static String DeviceID;

    /**
     * 获得当前应用的版本号名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获得当前应用的版本号数字
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            verCode = 0;
        }
        return verCode;
    }

    /**
     * 获取设备IMEI号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        return IMEI;
    }

    /**
     * 安装应用
     *
     * @param context
     * @param filePath 文件路径
     */
    public static void installApp(Context context, String filePath) {
        Logger.d(filePath);
        File _file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", _file);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            fileUri = Uri.fromFile(_file);
        }
        Logger.d(fileUri);
        intent.setDataAndType(fileUri,
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 退出
     *
     * @param activity
     */
    public static void exitApp(Activity activity) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(startMain);
        activity.finish();
        System.exit(0);
    }

    public static void hideIME(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }

    public static String getUserToken(Context context) {
        StringBuilder userToken = new StringBuilder();
        String userTokenOri = "";
        try {
            userTokenOri = EncryptionTool.decryptAES((String) SharedPreferencesTool.Get(context, "token", "", SharedPreferencesTool.SP_FILE_LOGIN));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        userToken.append(userTokenOri);
        userToken.append("|appcpigeon|");
        userToken.append(System.currentTimeMillis() / 1000);
        userToken.append("|android " + getVersionCode(context));
        Logger.i(userToken.toString());
        String res = EncryptionTool.encryptAES(userToken.toString());
        Logger.i(res);
        return res;
    }

    /**
     * 通用正则表达式调用方法
     *
     * @param Content    需要检查的字符串
     * @param PatternStr 正则表达式
     * @return
     */
    public static boolean Compile(String Content, String PatternStr) {
        Pattern p = Pattern.compile(PatternStr);
        Matcher m = p.matcher(Content);
        return m.matches();
    }

    /**
     * 结合多种方式计算设备唯一码
     *
     * @param context
     * @return
     */
    public static String getCombinedDeviceID(Context context) {
        if (!TextUtils.isEmpty(DeviceID)) return DeviceID;
        //IMEI
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String m_szImei = TelephonyMgr.getDeviceId();

        //Pseudo-Unique ID
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits

        //Android ID
        String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        //WIFI MAC Address string
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

        //BT MAC Address string
        String m_szBTMAC = BluetoothAdapter.getDefaultAdapter().getAddress();

        String m_szLongID = m_szImei + m_szDevIDShort
                + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
        //compute md5
        DeviceID = EncryptionTool.MD5(m_szLongID).toLowerCase();
        return DeviceID;
    }
}
