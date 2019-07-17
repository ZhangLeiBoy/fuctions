package com.android.albert.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.android.albert.CommonActionBar;
import com.android.albert.base.utils.RomUtils;
import com.android.albert.utils.KeyboardConflictCompat;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author zhanglei
 * @date 2018/6/20
 * @brief actionBar 封装内容。包含主题设置，沉浸式导航栏等
 */
public class BaseActionBarActivity extends BaseActivity implements CommonActionBar.OnMenuClickListener {
    //<editor-fold  desc="变量参数">
    protected final int ACTIONBAR_ID = -11;
    protected RelativeLayout layout;
    protected CommonActionBar actionBar;
    protected int menu_padding;                             //padding值
    protected float menu_text_size;                         // 二级字号大小
    //</editor-fold>

    // <editor-fold  desc="生命周期方法 ">

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //默认值
        menu_padding = SizeUtils.dp2px(12.5f);
        menu_text_size = 17;
        //根布局
        layout = new RelativeLayout(mContext);
        super.setContentView(layout);
        //actionBar
        actionBar = new CommonActionBar(mContext, null);
        actionBar.setMenuClickListener(this);
        actionBar.setId(ACTIONBAR_ID);
        layout.addView(actionBar);
        initActionBar();

        StatusBarUtil.setTranslucentForImageView(this, actionBar);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View v = mLayoutInflater.inflate(layoutResID, null);
        setContentView(v);
    }

    @Override
    public void setContentView(View view) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        if (isBelowActionBar()) {
            lp.addRule(RelativeLayout.BELOW, ACTIONBAR_ID);
        }
        layout.addView(view, 0, lp);
        //
        setNavBgColor();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        setContentView(view);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        KeyboardConflictCompat.assistWindow(getWindow());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
//        initStatusBar();
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
    }

    //</editor-fold>

    //<editor-fold desc="沉浸式导航栏设置">

    protected void setNavBgColor(){
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app_nav_bg),0);
    }

    /**
     * @return 导航栏背景色
     */
    protected int getNavBgColor() {
        return getResources().getColor(R.color.app_nav_bg);
    }

    /**
     * 设置沉浸式导航栏
     */
    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColorNoTranslucent(this, getNavBgColor());
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //针对小米系统处理
            if (RomUtils.checkIsMiuiRom()) {
                setStatusBarDarkMode(false, this);
            }
        }
    }

    /**
     * 设置状态栏背景及字体颜色,深色
     *
     * @param color          状态栏背景颜色，一般为设置为黑色
     * @param whiteTextColor 是否为白色字体
     */
    protected void initStatusBar(int color, boolean whiteTextColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColorNoTranslucent(this, color);
            if (whiteTextColor) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                setStatusBarDarkMode(false, this);
            }
        }
    }

    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //</editor-fold>


    // <editor-fold  desc="开放方法 ">

    /**
     * 是否在actionBar下方
     *
     * @return
     */
    protected boolean isBelowActionBar() {
        return true;
    }


    //</editor-fold>

    // <editor-fold  desc="actionBar操作 ">

    @Override
    public void onMenuClick(int mid, View v) {
        switch (mid) {
            case CommonActionBar.ACTIONBAR_ICON:
                KeyboardUtils.hideSoftInput(v);
                finish();
                break;
        }
    }

    protected void initActionBar() {
        actionBar.setBackView(R.drawable.nav_back_selector);
        actionBar.setBackgroundColor(getResources().getColor(R.color.app_nav_bg));
        actionBar.setTitleColor(getResources().getColor(R.color.app_nav_title));
        actionBar.setDividerColor(getResources().getColor(R.color.app_line_nav_column));
    }


    /**
     * @param resId        图片资源
     * @param menuId       menu Id 可用于在onMenuClick中操作
     * @param isSquareView 是否为方形图片资源
     */
    protected ImageView addRightImageView(@DrawableRes int resId, int menuId, boolean isSquareView) {
        ImageView menu = new ImageView(mContext);
        menu.setImageResource(resId);
        menu.setPadding(menu_padding, menu_padding, menu_padding, menu_padding);
        actionBar.addMenu(menuId, menu, isSquareView);
        return menu;
    }

    /**
     * @param menuText     文字菜单
     * @param menuId       menu Id 可用于在onMenuClick中操作
     * @param isSquareView 是否为方形资源
     */
    protected TextView addRightTextView(String menuText, int menuId, boolean isSquareView) {
        return addRightTextView(menuText, menuId, isSquareView, getResources().getColor(R.color.app_main_color));
    }

    /**
     * @param menuText     文字菜单
     * @param menuId       menu Id 可用于在onMenuClick中操作
     * @param isSquareView 是否为方形资源
     * @param color        文字颜色
     */
    protected TextView addRightTextView(String menuText, int menuId, boolean isSquareView, int color) {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setText(menuText);
        textView.setTextColor(color);
        textView.setPadding(0, 0, menu_padding, 0);
        textView.setTextSize(menu_text_size);
        textView.setSingleLine();
        actionBar.addMenu(menuId, textView, isSquareView);
        return textView;
    }

    /**
     * 左边TextView
     *
     * @param menuText
     * @param menuId
     * @param isSquareView
     * @return
     */
    protected TextView addLeftTextView(String menuText, int menuId, boolean isSquareView) {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setText(menuText);
        textView.setTextColor(getResources().getColor(R.color.app_nav_second_text));
        textView.setPadding(menu_padding, 0, 0, 0);
        textView.setTextSize(menu_text_size);
        actionBar.addLeftView(menuId, textView, isSquareView);
        return textView;
    }

    //</editor-fold>

}
