package com.android.albert.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 张雷
 * @data 2018.06.19
 * @brief base adapter
 */
public abstract class RecyclerViewSimpleAdapter extends RecyclerViewBaseAdapter {

    public RecyclerViewSimpleAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder setLayoutViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutId(), null);
        ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), view);
        return holder;
    }

    @Override
    protected int setLayoutItemViewType(Object bean, int position) {
        return 0;
    }

    /**
     * @return 简单模式下的直接返回R.layout.
     */
    @LayoutRes
    protected abstract int layoutId();
}
