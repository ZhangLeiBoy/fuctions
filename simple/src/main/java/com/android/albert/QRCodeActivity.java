package com.android.albert;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.android.albert.base.BaseSimpleActivity;
import com.android.albert.simple.R;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * @author 张雷
 * @date 2018/6/27
 * @brief 二维码扫描
 */

public class QRCodeActivity extends BaseSimpleActivity{
    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_layout_main);
        initQrscan();
    }
    private void initQrscan() {
        captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.qrcode_layout);
        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {

            }

            @Override
            public void onAnalyzeFailed() {

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.scan_layout, captureFragment).commit();
    }
}
