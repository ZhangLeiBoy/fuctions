package com.android.albert.base;

import android.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author zhanglei
 * @date 2018/6/20
 * @brief
 */
public class BaseFragment extends Fragment implements CustomAdapt {


    protected void showToast(String content) {
        ToastUtils.showShort(content);
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
