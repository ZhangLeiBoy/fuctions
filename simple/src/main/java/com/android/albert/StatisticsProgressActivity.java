package com.android.albert;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.android.albert.base.BaseSimpleActivity;
import com.android.albert.simple.R;

/**
 * @author 张雷
 * @date 2018/7/9
 * @brief 水平进度统计
 * @link https://github.com/bingoogolapple/BGAProgressBar-Android
 */
public class StatisticsProgressActivity extends BaseSimpleActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setTitle("水平进度统计");
        setContentView(R.layout.statistics_progress_layout);
    }
}
