package com.android.albert.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.albert.HomeActivity;
import com.android.albert.R;
import com.android.albert.base.BaseActivity;
import com.android.albert.base.permission.AppPermissions;
import com.android.albert.base.permission.listener.OnPermissionDenied;
import com.android.albert.base.permission.listener.OnPermissionGranted;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.android.albert.base.App.quiet;

/**
 * @author 张雷
 * @date 2018/6/21
 * @brief
 */

public class WelcomeActivity extends BaseActivity {
    private ImageView imageBg;
    private TextView next;
    private ScaleAnimation scaleAnimation;

    //倒计时相关
    private Disposable mDisposable;
    private int count = 6;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        permissionCheck();
    }

    private void permissionCheck() {
        checkPermission(AppPermissions.basePermission, new OnPermissionGranted() {
            @Override
            public void onGranted() {
                initNext();
            }
        }, new OnPermissionDenied() {
            @Override
            public void onDenied() {
                quiet();
            }
        });
    }

    private void initNext() {
        imageBg = findViewById(R.id.iv_start);
        next = findViewById(R.id.next);
        //进行缩放动画
        scaleAnimation = new ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(count * 1000);
        //动画播放完成后保持形状
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //可以在这里先进行某些操作
                countDown();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageBg.startAnimation(scaleAnimation);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMain();
            }
        });
    }

    private void countDown() {
        next.setText("跳过 " + (count - 1));
        next.setVisibility(View.VISIBLE);
        mDisposable = Flowable.intervalRange(0, count, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        long currentCount = ((count - 1) - aLong);
                        if (currentCount > 0) {
                            next.setText("跳过 " + currentCount);
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        goToMain();
                    }
                }).subscribe();
    }

    private void goToMain() {
        startActivity(new Intent(mActivity, HomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != scaleAnimation) {
            scaleAnimation.cancel();
        }
        if (null != imageBg) {
            imageBg.clearAnimation();
        }
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
