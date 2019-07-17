package com.android.albert.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.albert.base.permission.listener.OnPermissionDenied;
import com.android.albert.base.permission.listener.OnPermissionGranted;
import com.android.albert.base.utils.dialog.DialogUtils;
import com.android.albert.base.utils.dialog.listener.OnDialogCancelListener;
import com.android.albert.base.utils.dialog.listener.OnDialogConfirmListener;
import com.blankj.utilcode.util.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.List;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author zhanglei
 * @date 2018/6/20
 * @brief 最底层activity 包含界面必要的参数和权限申请
 */
public class BaseActivity extends AppCompatActivity implements CustomAdapt {

    protected Context mContext;
    protected Activity mActivity;
    protected LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    protected void showToast(String content) {
        ToastUtils.showShort(content);
    }

    //<editor-fold desc="权限操作 AndPermission">

    private OnPermissionGranted onPermissionGranted;                //权限申请回调
    private OnPermissionDenied onPermissionDenied;                  //权限申请回调
    private String[] perms;                                         //需要授权的权限集合

    protected void checkPermission(final String[] perms,
                                   OnPermissionGranted onPermissionGranted) {
        checkPermission(perms, onPermissionGranted, null);
    }

    protected void checkPermission(final String[] perms,
                                   OnPermissionGranted onPermissionGranted,
                                   OnPermissionDenied onPermissionDenied) {
        checkPermission(perms, onPermissionGranted, onPermissionDenied, false);
    }

    private void checkPermission(final String[] perms,
                                 final OnPermissionGranted onPermissionGranted,
                                 final OnPermissionDenied onPermissionDenied,
                                 final boolean fromSetting) {
        this.perms = perms;
        this.onPermissionGranted = onPermissionGranted;
        this.onPermissionDenied = onPermissionDenied;
        if (AndPermission.hasPermissions(mContext, perms)) {
            if (onPermissionGranted != null) {
                onPermissionGranted.onGranted();
            }
            return;
        }
        AndPermission.with(this)
                .runtime()
                .permission(perms)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (fromSetting) {
                            if (onPermissionDenied != null) {
                                onPermissionDenied.onDenied();
                            }
                            return;
                        }
                        if (AndPermission.hasAlwaysDeniedPermission(mActivity, permissions)) {
                            showSettingDialog(permissions);
                        } else {
                            showRetryDialog(permissions);
                        }
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //所有授权成功
                        if (onPermissionGranted != null) {
                            onPermissionGranted.onGranted();
                        }
                    }
                }).start();
    }

    //展示前往设置中心的dialog
    private void showSettingDialog(final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(mContext, permissions);
        String message = "请前往设置中心开启以下权限 [ "
                + TextUtils.join("，", permissionNames)
                + " ] ，否则将会影响功能的使用";

        DialogUtils.showDialog(mContext, getString(R.string.app_tip),
                message,
                getString(R.string.permission_denied_to_setting),
                getString(R.string.permission_denied_cancel),
                false,
                new OnDialogConfirmListener() {
                    @Override
                    public void onConfirm() {
                        goSetting();
                    }
                }, new OnDialogCancelListener() {
                    @Override
                    public void onCancel() {
                        if (onPermissionDenied != null) {
                            onPermissionDenied.onDenied();
                        }
                    }
                });
    }

    //再次提示需要授权
    private void showRetryDialog(final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(mContext, permissions);
        String message = "请开启以下权限 [ "
                + TextUtils.join("，", permissionNames)
                + " ] ，否则将会影响功能的使用";
        DialogUtils.showDialog(mContext,
                getString(R.string.app_tip),
                message,
                getString(R.string.permission_denied_retry),
                getString(R.string.permission_denied_cancel),
                false,
                new OnDialogConfirmListener() {
                    @Override
                    public void onConfirm() {
                        checkPermission(perms, onPermissionGranted, onPermissionDenied, false);
                    }
                }, new OnDialogCancelListener() {
                    @Override
                    public void onCancel() {
                        if (onPermissionDenied != null) {
                            onPermissionDenied.onDenied();
                        }
                    }
                });
    }

    //取设置界面授权
    private void goSetting() {
        AndPermission.with(mActivity)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        checkPermission(perms, onPermissionGranted, onPermissionDenied, true);
                    }
                })
                .start();
    }


    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选择一个作为基准进行适配)
     *
     * @return {@code true} 为按照宽度进行适配, {@code false} 为按照高度进行适配
     */
    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    /**
     * 这里使用 iPhone 的设计图, iPhone 的设计图尺寸为 750px * 1334px, 高换算成 dp 为 667 (1334px / 2 = 667dp)
     * <p>
     * 返回设计图上的设计尺寸, 单位 dp
     * {@link #getSizeInDp} 须配合 {@link #isBaseOnWidth()} 使用, 规则如下:
     * 如果 {@link #isBaseOnWidth()} 返回 {@code true}, {@link #getSizeInDp} 则应该返回设计图的总宽度
     * 如果 {@link #isBaseOnWidth()} 返回 {@code false}, {@link #getSizeInDp} 则应该返回设计图的总高度
     * 如果您不需要自定义设计图上的设计尺寸, 想继续使用在 AndroidManifest 中填写的设计图尺寸, {@link #getSizeInDp} 则返回 {@code 0}
     *
     * @return 设计图上的设计尺寸, 单位 dp
     */
    @Override
    public float getSizeInDp() {
        return 667;
    }

    //</editor-fold>
}
