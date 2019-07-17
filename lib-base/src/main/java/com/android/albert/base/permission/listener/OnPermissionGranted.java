package com.android.albert.base.permission.listener;

/**
 * @author 张雷
 * @date 2018/6/27
 * @brief 权限获取，所申请的权限全部允许后调用此接口
 */

public interface OnPermissionGranted {
    //授权,只有全部权限获得时才会调用
    void onGranted();
}
