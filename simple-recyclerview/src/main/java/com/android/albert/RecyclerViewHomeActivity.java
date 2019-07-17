package com.android.albert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.albert.base.BaseSimpleActivity;
import com.android.albert.simple.recyclerview.R;

/**
 * @author 张雷
 * @date 2018/3/19.
 * @brief 测试入口
 */
public class RecyclerViewHomeActivity extends BaseSimpleActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rcy_home);
        actionBar.setTitle("RecyclerView");
        initView();
    }

    private void initView() {
        findViewById(R.id.to_rcy_multi).setOnClickListener(this);

        findViewById(R.id.to_rcy_single).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.to_rcy_multi) {
            startActivity(new Intent(this, RecyclerViewMultiTypeActivity.class));
        } else if (id == R.id.to_rcy_single) {
            startActivity(new Intent(this, RecyclerViewSingleListActivity.class));
        }
    }
}
