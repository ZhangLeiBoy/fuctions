package com.android.albert;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.android.albert.base.BaseSimpleActivity;
import com.android.albert.base.permission.AppPermissions;
import com.android.albert.base.permission.listener.OnPermissionDenied;
import com.android.albert.base.permission.listener.OnPermissionGranted;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;

import io.flutter.facade.Flutter;

import static com.android.albert.base.App.quiet;

/**
 * @author 张雷
 * @date 2018/3/19.
 * @brief 测试入口
 */
public class HomeActivity extends BaseSimpleActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        actionBar.setTitle("首页");
        actionBar.removeAllLeftView();
        initView();
    }

    private void initView() {
        findViewById(R.id.to_rcy).setOnClickListener(this);

        findViewById(R.id.to_keyboard).setOnClickListener(this);

        findViewById(R.id.to_qrcode).setOnClickListener(this);

        findViewById(R.id.to_ringstatistics).setOnClickListener(this);

        findViewById(R.id.to_statistics_progress).setOnClickListener(this);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showShort("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                ToastUtils.cancel();
                quiet();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.to_keyboard:
                startActivity(new Intent(this, MyKeyBoardActivity.class));
                break;
            case R.id.to_rcy:
                startActivity(new Intent(this, RecyclerViewHomeActivity.class));
                break;
            case R.id.to_qrcode:
                checkPermission(AppPermissions.cameraAudioPermission, new OnPermissionGranted() {
                    @Override
                    public void onGranted() {
                        showToast("授权成功");
                    }
                }, new OnPermissionDenied() {
                    @Override
                    public void onDenied() {
                        showToast("拒绝授权");
                    }
                });
                break;
            case R.id.to_ringstatistics:
                startActivity(new Intent(this, StatisticsRingActivity.class));
                break;
            case R.id.to_statistics_progress:

//                View flutterView = Flutter.createView(
//                        HomeActivity.this,
//                        getLifecycle(),
//                        "route1"
//                );
//                FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
//                addContentView(flutterView, layout);

                startActivity(new Intent(this, FlutterPageActivity.class));
                break;
        }
    }
}
