package com.android.albert;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.albert.base.BaseSimpleActivity;
import com.android.albert.simple_exam.R;

/**
 * @author 张雷
 * @date 2018/7/7
 * @brief 模拟考试
 */

public class ExamActivity extends BaseSimpleActivity implements View.OnClickListener {
    private TextView start_exam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_main);
        initView();
    }

    private void initView() {
        start_exam = findViewById(R.id.start_exam);
        start_exam.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_exam) {
            //
            return;
        }
    }
}
