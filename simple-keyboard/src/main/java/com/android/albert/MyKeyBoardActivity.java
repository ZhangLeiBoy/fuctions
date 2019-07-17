package com.android.albert;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.android.albert.base.BaseSimpleActivity;
import com.android.albert.simple.keyboard.R;

import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

/**
 * @author 张雷
 * @date 2018/6/15.
 * @brief 软键盘 测试
 */
public class MyKeyBoardActivity extends BaseSimpleActivity {
    private Context mContext;
    private KPSwitchPanelLinearLayout mPanelLayout = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.test_keyboard);
        mPanelLayout = findViewById(R.id.panel_root);
        setListener();
    }

    private void setListener() {
        //监听键盘状态
        KeyboardUtil.attach((Activity) mContext, mPanelLayout, new KeyboardUtil.OnKeyboardShowingListener() {
            @Override
            public void onKeyboardShowing(boolean isShowing) {
                if (isShowing) {
                    mPanelLayout.setVisibility(View.VISIBLE);
                } else {
                    mPanelLayout.setVisibility(View.GONE);
                }
                invalidateOptionsMenu();
            }
        });
    }
}
