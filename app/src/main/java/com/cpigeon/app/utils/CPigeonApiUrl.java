package com.cpigeon.app.utils;

import com.cpigeon.app.BuildConfig;

/**
 * Created by chenshuai on 2017/3/4.
 */

public class CPigeonApiUrl {

    private static CPigeonApiUrl instance;

    private static String[] Servers;

    private int mServerIndex = -1;

    //    Api 版本
    private static final String API_VERSION = "/CPAPI/V1/";
//    public static final String API_VERSION = "/API/";
    /**
     * 服务器主机
     */
//    public static final String SERVER = "http://192.168.0.12:8888"; //
//    public static final String SERVER = "http://114.141.132.146:818";
    private static final String SERVER = BuildConfig.SERVER;

    private CPigeonApiUrl() {
        initServers();
    }

    public static CPigeonApiUrl getInstance() {
        if (instance == null) {
            synchronized (CpigeonData.class) {
                if (instance == null) {
                    instance = new CPigeonApiUrl();
                }
            }
        }
        return instance;
    }

    private void initServers() {
        if (Servers == null) {
//            if (BuildConfig.DEBUG)
//                Servers = new String[]{"http://192.168.0.52:8889"};//调试版
////                Servers = new String[]{SERVER};//调试版
//            else
                Servers = new String[]{"http://114.141.132.146:818", "http://221.236.20.76:818", "http://118.123.244.89:818"};//服务器
        }

    }

    public String getServer(boolean forceChange) {
        if (forceChange || mServerIndex < 0) {
            initServers();
            int index = -1;
            do {
                index = (int) (Math.random() * Servers.length);
            } while (index == mServerIndex && index >= 0 && Servers.length > 0);
            mServerIndex = index;
        }
        return Servers[mServerIndex];
    }

    public String getServer() {
        return getServer(false);
    }

    /**
     * Crash日志报告url
     */
    public static final String CRASH_REPORT_URL = API_VERSION + "CrashReport";
    /**
     * App更新url
     */
    public static final String UPDATE_CHECK_URL = API_VERSION + "version";
    /**
     * 检查是否是可用版本
     */
    public static final String IS_AVAILABLE_VERSION_URL = API_VERSION + "IsAvailableVersion";
    /**
     * 广告信息Url
     */
    public static final String AD_URL = API_VERSION + "Ad";
    /**
     * 登录URL
     */
    public static final String LOGIN_URL = API_VERSION + "login";
    /**
     * 自动登录URL
     */
    public static final String AUTO_LOGIN_URL = API_VERSION + "AutoLogin";
    /**
     * 发送验证码URL
     */
    public static final String PHONE_YZM_URL = API_VERSION + "yzm";
    /**
     * 注册URL
     */
    public static final String REGIST_URL = API_VERSION + "regist";
    /**
     * 找回密码URL
     */
    public static final String FIND_PASSWORD_URL = API_VERSION + "find";
    /**
     * 获取用户头像URL
     */
    public static final String GET_USER_HEAD_IMG_URL = API_VERSION + "GetUserHeadImg";

    /**
     * 设置用户登录密码URL
     */
    public static final String SET_USER_PWD_URL = API_VERSION + "SetUserPwd";
    /**
     * 设置用户支付密码URL
     */
    public static final String SET_USER_PAYPWD_URL = API_VERSION + "SetUserPayPwd";

    /**
     * 用户余额积分信息URL
     */
    public static final String USER_YUE_JIFEN_INFO_URL = API_VERSION + "GetUserYEJF";


    /**
     * 获取用户绑定手机号URL
     */
    public static final String GET_USER_BANDPHONE_URL = API_VERSION + "GetUserBandPhone";

    /**
     * 获取用户积分记录URL
     */
    public static final String GET_USER_SCORE_RECORD_URL = API_VERSION + "GetScoreRecord";

    /**
     * 获取用户订单记录URL
     */
    public static final String GET_ORDER_LIST_URL = API_VERSION + "GetOrderList";
    /**
     * 比赛信息列表URL
     */
    public static final String RACE_ITEM_INFO_URL = API_VERSION + "RaceList";

    /**
     * 公告连接
     */
    public static final String RACE_BULLETIN_URL = API_VERSION + "RaceBulletin";
    /**
     * 插组统计连接
     */
    public static final String RACE_GROUPS_COUNT_URL = API_VERSION + "RaceGroupsCount";

    /**
     * 比赛归巢数据统计URL
     */
    public static final String RACE_ITEM_HOMING_COUNT_URL = API_VERSION + "RaceHomingCount";

    /**
     * 比赛报道数据URL
     */
    public static final String RACE_REPORT_INFO_URL = API_VERSION + "RaceReport";
    /**
     * 比赛集鸽数据URL
     */
    public static final String RACE_PIGEONS_INFO_URL = API_VERSION + "GetJGSJ";
    /**
     * 获取资讯列表URL
     */
    public static final String NEWS_TYPE_URL = API_VERSION + "NewsTypes";
    /**
     * 获取资讯列表URL
     */
    public static final String NEWS_LIST_URL = API_VERSION + "NewsList";
    /**
     * 资讯详情URL
     */
    public static final String NEWS_DETIAL_URL = API_VERSION + "NewsDetial";
    /**
     * 获取评论URL
     */
    public static final String GET_COMMENTS_URL = API_VERSION + "GetComments";
    /**
     * 添加评论URL
     */
    public static final String ADD_COMMENTS_URL = API_VERSION + "AddComment";
    /**
     * 足环查询URL
     */
    public static final String FOOT_QUERY_URL = API_VERSION + "FootSearch";
    /**
     * 用户在用服务套餐信息URL
     */
    public static final String USER_SERVICE_URL = API_VERSION + "GetUserService";
    /**
     * 服务（套餐）信息URL
     */
    public static final String SERVICES_INFO_URL = API_VERSION + "GetServicesInfo";
    /**
     * 创建订单（服务）URL
     */
    public static final String CREATE_SERVICE_ORDER_URL = API_VERSION + "CreateServiceOrder";
    /**
     * 订单支付（积分兑换）URL
     */
    public static final String ORDER_PAY_BY_SCORE_URL = API_VERSION + "OrderPayByScore";
    /**
     * 订单支付（余额支付）URL
     */
    public static final String ORDER_PAY_BY_BALANCE_URL = API_VERSION + "OrderPayByBalance";

    /**
     * 获取微信预支付订单URL
     */
    public static final String GET_WX_PREPAY_ORDER_URL = API_VERSION + "GetWXPrePayOrder";


    /**
     * 创建充值订单 --szx
     */
    public static final String GREATERECHARGEORDER = API_VERSION + "CreateRechargeOrder";

    /**
     * 获取充值订单列表 --szx
     */

    public static final String GETRECHARGELIST = API_VERSION + "GetRechargeList";


    /**
     * 创建微信预支付订单 --szx
     */
    public static final String GETWXPREPAYORDERFORRECHARGE = API_VERSION + "GetWXPrePayOrderForRecharge";

    /**
     * 获取签到信息 --szx
     */
    public static final String GETUSERSIGNSTATUS = API_VERSION +"GetUserSignStatus";

    /**
     * 获取用户个人信息  --szx
     */
    public static final String GETBASICUSERINFO = API_VERSION +"GetBasicUserInfo";

    /**
     * 修改用户信息 --szx
     */
    public static final String MOTIFYBASICUSERINFO = API_VERSION +"MotifyBasicUserInfo";

    //获取所有人的动态 -szx
    public static final String GETCIRCLEMESSAGELIST = API_VERSION +"GetCircleMessageList";

    //删除自己发的动态 -szx
    public static final String DELCIRCLEMESSAGE = API_VERSION +"DelCircleMessage";


    //点赞/取消点赞 -szx
    public static final String PRAISECIRCLEMESSAGE = API_VERSION +"PraiseCircleMessage";


    //  添加评论
    public static final String ADDCIRCLEMESSAGECOMMENT = API_VERSION +"AddCircleMessageComment";


    //获取鸽友圈用户信息 -szx
    public static final String GETUSERCIRCLEINFO = API_VERSION +"GetUserCircleInfo";

    //获取鸽友圈关注列表 -szx
    public static final String GETATTENTIONCIRCLEUSERLIST = API_VERSION +"GetAttentionCircleUserList";


    //关注/取消关注 -szx
    public static final String ATTENTIONCIRCLEUSER = API_VERSION +"AttentionCircleUser";


    //获取黑名单列表 -szx
    public static final String GETUSERCIRCLEBLACKLIST = API_VERSION +"GetUserCircleBlackList";

    //取消拉黑 -szx
    public static final String DEFRIENDCIRCLEUSER = API_VERSION +"DefriendCircleUser";


    //屏蔽用户/取消屏蔽用户
    public static final String SHIELDCIRCLEUSER = API_VERSION + "ShieldCircleUser";

    //获取被屏蔽的用户列表 -szx
    public static final String GETUSERSHIELDCIRCLEUSERLIST = API_VERSION + "GetUserShieldCircleUserList";
    //屏蔽消息/取消屏蔽消息 -szx
    public static final String SHIELDCIRCLEMESSAGE = API_VERSION + "ShieldCircleMessage";
    //清除屏蔽消息 -szx
    public static final String CLEARSHIELDCIRCLEMESSAGE = API_VERSION + " ClearShieldCircleMessage";
    //获取被屏蔽的信息列表 -szx
    public static final String GETUSERSHIELDCIRCLEMESSAGELIST = API_VERSION + "GetUserShieldCircleMessageList";
    //删除已发布的评论
    public static final String DELCIRCLECOMMENT = API_VERSION + "DelCircleComment";










    /**
     * 添加点击量 --szx
     */
    public static final String ADDRACECLICKCOUNT = API_VERSION + "AddRaceClickCount";
    /**
     * 上传头像 --szx
     */
    public static final String UPDATEUSERFACEIMAGE = API_VERSION + "UpdateUserFaceImage";


    public static final String PUSHCIRCLEMESSAGE = API_VERSION + "PushCircleMessage";

    /**
     * 反馈提交URL
     */
    public static final String FEEDBACK_URL = API_VERSION + "Feedback";


    /**
     * App介绍页面
     */
    public static final String APP_INTRO_URL = "/APP/Intro?type=android";
    /**
     * App用户协议
     */
    public static final String APP_USER_PROTOCOL_URL = "/APP/Protocol";
    /**
     * App帮助
     */
    public static final String APP_HELP_URL = "/APP/Help?type=help";
    /**
     * App签到链接
     */
    public static final String APP_SIGN_URL = "/APP/UserSign";
}
