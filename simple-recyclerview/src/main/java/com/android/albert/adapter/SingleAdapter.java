package com.android.albert.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.android.albert.simple.recyclerview.R;

/**
 * @author zhanglei
 * @date 2018/6/20
 * @brief
 */
public class SingleAdapter extends RecyclerViewSimpleAdapter {

    public SingleAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected void layoutBindData(RecyclerView.ViewHolder holder, Object bean, int position) {

    }

    @Override
    protected int layoutId() {
        return R.layout.test_wangyi_item;
    }
}
