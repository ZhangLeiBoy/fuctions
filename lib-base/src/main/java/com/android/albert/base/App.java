package com.android.albert.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.android.albert.crash.CrashHandler;
import com.blankj.utilcode.util.Utils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * @author 张雷
 * @date 2018/3/19.
 * @brief 初始化入口
 */
public class App extends Application {
    protected Activity mainActivity;                        //主框架的activity
    protected static List<Activity> mActivitys;             //app所有activity存储集合
    protected static App mInstance = null;                  //instance

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        registerActivityListener();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        Utils.init(this);

        ZXingLibrary.initDisplayOpinion(this);

        //支持Fragment autoSize
        AutoSizeConfig.getInstance().setCustomFragment(true);

    }

    public void setMainActivity(Activity activity) {
        this.mainActivity = activity;
    }

    public void addActivity(Activity activity) {
        if (null == mActivitys) {
            mActivitys = Collections.synchronizedList(new LinkedList<Activity>());
        }
        mActivitys.add(activity);
    }

    //监听所有的activity创建销毁
    private void registerActivityListener() {
        mActivitys = new ArrayList<>();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (null != mActivitys && mActivitys.contains(activity)) {
                    mActivitys.remove(activity);
                }
            }
        });
    }

    public static void quiet() {
        if (null != mActivitys) {
            for (Activity a : mActivitys) {
                a.finish();
            }
            System.gc();
        }
    }
}
