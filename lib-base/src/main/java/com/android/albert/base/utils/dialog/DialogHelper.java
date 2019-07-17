package com.android.albert.base.utils.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.android.albert.base.App;

/**
 * @author 张雷
 * @time: 2017/11/6
 * @brief: Dialog 中使用到的一些辅助工具
 */
class DialogHelper {

    static final int SDK_17 = 17;

    /**
     * 是否已经销毁
     *
     * @param mContext
     * @return
     */
    public static boolean isActivityDestory(Context mContext) {
        if (mContext == null) {
            return true;
        }
        boolean isFinishing = false;
        if (mContext instanceof Activity) {
            if (Build.VERSION.SDK_INT < SDK_17) {
                isFinishing = ((Activity) mContext).isFinishing();
            } else {
                isFinishing = ((Activity) mContext).isDestroyed();
            }
        }
        return isFinishing;
    }

    /**
     * 动态显示软键盘
     *
     * @param view 视图
     */
    public static void showSoftInput(final View view) {
        if (view == null) {
            return;
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 动态显示软键盘
     *
     * @param activity activity
     */
    public static void showSoftInput(final Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 通过高度 判断当前的软键盘是否显示了
     *
     * @param mActivity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isSoftShowing(Activity mActivity) {
        //获取当前屏幕内容的高度
        int screenHeight = mActivity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        int navH = getNavigationBarHeight(mActivity);
        Rect rect = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - navH - rect.bottom != 0;
    }

    /**
     * 底部导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (hasSoftKeys(manager)) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        }
        return 0;
    }

    /**
     * 判断底部navigator是否已经显示
     *
     * @param
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasSoftKeys(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.heightPixels;
    }

    /**
     * 设置 View 显示状态
     *
     * @param view
     * @param flag
     */
    public static void setVisibility(View view, int flag) {
        if (view != null && view.getVisibility() != flag) {
            view.setVisibility(flag);
        }
    }

    /**
     * 获取指定 id 的String资源
     *
     * @param resourceId
     * @return
     */
    public static String getString(int resourceId) {
        return getString(App.getInstance(), resourceId);
    }

    /**
     * 获取指定 id 的String资源
     *
     * @param resourceId
     * @return
     */
    public static String getString(Context mContext, int resourceId) {
        if (null == mContext || mContext.getResources() == null) {
            return "";
        }
        return mContext.getResources().getString(resourceId);
    }
}
