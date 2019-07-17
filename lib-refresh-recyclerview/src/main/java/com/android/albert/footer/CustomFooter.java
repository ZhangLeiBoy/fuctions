package com.android.albert.footer;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

/**
 * @author 张雷
 * @data 2018.06.19
 * @brief 自定义 上拉加载布局
 */
public class CustomFooter extends BallPulseFooter {
    public CustomFooter(Context context) {
        super(context);
    }

    public CustomFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
