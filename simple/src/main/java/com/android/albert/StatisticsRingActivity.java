package com.android.albert;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.android.albert.base.BaseSimpleActivity;
import com.android.albert.base.views.RingStatisticsView;
import com.android.albert.simple.R;

/**
 * @author 张雷
 * @date 2018/7/9
 * @brief 百分比环形测试类
 */
public class StatisticsRingActivity extends BaseSimpleActivity {
    RingStatisticsView ring_main;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setTitle("环形百分比统计");
        setContentView(R.layout.statistics_ring_layout);
        ring_main = findViewById(R.id.ring_main);
        ring_main.setPercentAndColors(
                new float[]{
                        0.2f,
                        0.2f,
                        0.3f,
                        0.3f
                },
                new int[]{
                        Color.parseColor("#F9AA28"),
                        Color.parseColor("#009752"),
                        Color.parseColor("#2EC1FB"),
                        Color.parseColor("#FA6723")
                });
        ring_main.refresh();
    }
}
