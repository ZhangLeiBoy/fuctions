package com.android.albert.base.permission;

import android.Manifest;

/**
 * @author zhanglei
 * @date 2018/6/21
 * @brief
 */
public class AppPermissions {
    //基础读写权限
    public static final String[] basePermission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    //相机权限
    public static final String[] cameraPermission = {
            Manifest.permission.CAMERA};
    //音频操作权限
    public static final String[] recordPermission = {
            Manifest.permission.RECORD_AUDIO};
    //相机及音频权限
    public static final String[] cameraAudioPermission = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};
    //联系人权限
    public static final String[] contactPermission = {
            Manifest.permission.READ_CONTACTS
    };
}
