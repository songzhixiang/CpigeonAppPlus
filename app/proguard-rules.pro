# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Software\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript standard
# class:
#-keepclassmembers class fqcn.of.javascript.standard.for.webview {
#   public *;
#}
#-----------------混淆配置设定------------------------------------------------------------------------
-optimizationpasses 5                                                       #指定代码压缩级别
-dontusemixedcaseclassnames                                                 #混淆时不会产生形形色色的类名
-dontskipnonpubliclibraryclasses                                            #指定不忽略非公共类库
-dontpreverify                                                              #不预校验，如果需要预校验，是-dontoptimize
-ignorewarnings                                                             #屏蔽警告
-verbose                                                                    #混淆时记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #优化
#保护注解
-keepattributes *Annotation*
-keep class * extends *.Base$**{*;}

#-----------------不需要混淆第三方类库------------------------------------------------------------------
-dontwarn android.support.v4.**                                             #去掉警告
-keep class android.support.v4.** { *; }                                    #过滤android.support.v4
-keep interface android.support.v4.app.** { *; }


-keep class org.apache.**{*;}                                               #过滤commons-httpclient-3.1.jar

-dontwarn org.xutils.**                                                     #去掉警告
-keep class org.xutils.** { *; }
-keep class android.**{*;}
-keep class * extends java.lang.annotation.Annotation{*;}                   #这是xUtils文档中提到的过滤掉注解

#不混淆SweetAlert
-dontwarn cn.pedant.SweetAlert.**                                             #去掉警告
-keep class cn.pedant.SweetAlert.** { *; }                                    #过滤cn.pedant.SweetAlert

#不混淆com.alibaba.fastjson
-dontwarn com.alibaba.fastjson.**                                             #去掉警告
-keep class com.alibaba.fastjson.** { *; }                                    #过滤com.alibaba.fastjson
-keep interface com.alibaba.fastjson.** { *; }                                #过滤com.alibaba.fastjson
#fastJson
-keepattributes Singature     #避免混淆泛型
-keepattributes *Annotation  #不混淆注释

# 不混淆butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#不混淆 com.squareup.picasso
-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}
#不混淆 com.squareup.picasso

#不混淆极光推送
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#保留model
-keep class com.cpigeon.app.modular.*.model.bean.**{*;}
-keep class com.cpigeon.app.commonstandard.**{*;}
-keep class com.cpigeon.app.utils.databean.**{*;}

###保留使用xUtils的方法和类，并且不要混淆名字
-keep @org.xutils.db.annotation.Table class *
-keepclassmembers class * {
    @org.xutils.db.annotation.* <fields>;
}
-keepclassmembers class * {
    @org.xutils.view.annotation.* <fields>;
    @org.xutils.view.annotation.* <methods>;
}

#避免混淆Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


#如果引用了v4或者v7包
-dontwarn android.support.**

#集成 JPush Android SDK 的混淆
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

#-----------------不需要混淆系统组件等-------------------------------------------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.cpigeon.model.**{*;}                                   #过滤掉自己编写的实体类
-keep class com.cpigeon.view.**{*;}                                   #过滤掉自己编写的实体类

##混淆保护自己项目的部分代码以及引用的第三方jar包library（想混淆去掉"#"）
#-libraryjars libs/umeng-analytics-v5.2.4.jar
#-libraryjars libs/alipaysecsdk.jar
#-libraryjars libs/alipayutdid.jar
#-libraryjars libs/weibosdkcore.jar

#Warning:com.amap.api.maps2d.overlay.BusLineOverlay: can't find referenced class com.amap.api.services.busline.BusLineItem
-dontwarn com.amap.**
-keep class com.amap.** { *; }

#Warning:com.alipay.android.phone.mrpc.core.AndroidHttpClient: can't find referenced method 'org.apache.http.conn.ssl.SSLSocketFactory getHttpSocketFactory(int,android.net.SSLSessionCache)' in library class android.net.SSLCertificateSocketFactory
-dontwarn com.alipay.**
-keep class com.alipay.** { *; }
-keepattributes EnclosingMethod






