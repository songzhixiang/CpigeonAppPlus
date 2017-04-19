package com.cpigeon.app.utils;

import android.os.Environment;

import com.cpigeon.app.BuildConfig;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.modular.matchlive.model.bean.Collection;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;

/**
 * Created by Administrator on 2016-10-20.
 */

public class CpigeonConfig {


    /**
     * App更新信息缓存时间（毫秒）
     */
    public static final int CACHE_UPDATE_INFO_TIME = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 1;
    /**
     * 链接超时时间
     */
    // public static final int CONNECT_TIMEOUT = 1000 * 10;
    public static final int CACHE_COMMON_1MIN_TIME = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 1;
    public static final int CACHE_COMMON_3MIN_TIME = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 3;
    public static final int CACHE_COMMON_30MIN_TIME = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 30;
    /**
     * 公告缓存时间
     */
    public static final int CACHE_BULLETIN_INFO_TIME = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 10;
    /**
     * 插组统计缓存时间
     */
    public static final int CACHE_RACE_GROUPS_COUNT_TIME = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 3;
    /**
     * 赛事列表信息缓存时间(毫秒)
     */
    public static final int CACHE_MATCH_INFO_TIME = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 3;
    /**
     * 报道列表信息缓存时间(毫秒)
     */
    public static final int CACHE_MATCH_REPORT_INFO_TIME = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 3;
    /**
     * 新闻列表缓存时间(毫秒)
     */
    public static final int CACHE_TIME_NEWS_LIST = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 3;
    /**
     * 服务信息列表缓存时间(毫秒)
     */
    public static final int CACHE_SERVICES_INFO_LIST = BuildConfig.DEBUG ? 1000 * 10 : 1000 * 60 * 60;
    /**
     * 直播时间天数,公棚
     */
    public static final int LIVE_DAYS_GP = 7;
    /**
     * 直播时间天数,协会
     */
    public static final int LIVE_DAYS_XH = 3;
    /**
     * 外部文件存储跟路径
     */
    private static final String FILE_PATH_ROOT = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + MyApp.getInstance().getPackageName();
    /**
     * 更新APP安装包存放文件夹
     */
    public static final String UPDATE_SAVE_FOLDER = FILE_PATH_ROOT + "/update/";
    /**
     * 缓存路径
     */
    public static final String CACHE_FOLDER = FILE_PATH_ROOT + "/cache/";

    static {
    }

    /**
     * 获取默认Db
     *
     * @return
     */
    public static DbManager.DaoConfig getDataDb() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        daoConfig.setDbName("data.db");
        daoConfig.setDbVersion(4);
        daoConfig.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                try {
                    db.dropTable(Collection.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });
        daoConfig.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {
                db.getDatabase().enableWriteAheadLogging();
            }
        });

        daoConfig.setAllowTransaction(true);
        daoConfig.setTableCreateListener(new DbManager.TableCreateListener() {
            @Override
            public void onTableCreated(DbManager db, TableEntity<?> table) {

            }
        });
        return daoConfig;
    }

    /**
     * 获取资讯用DB
     *
     * @return
     */
    public static DbManager.DaoConfig getDataDbforNews() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        daoConfig.setDbName("news.db");
        daoConfig.setDbVersion(1);
        daoConfig.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
            }
        });
        daoConfig.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {
                db.getDatabase().enableWriteAheadLogging();
            }
        });

        daoConfig.setAllowTransaction(true);
        daoConfig.setTableCreateListener(new DbManager.TableCreateListener() {
            @Override
            public void onTableCreated(DbManager db, TableEntity<?> table) {

            }
        });
        return daoConfig;
    }

}
