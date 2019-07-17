package com.android.albert.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.albert.bean.DataBean;
import com.android.albert.simple.recyclerview.R;

/**
 * @author zhanglei
 * @date 2018/6/20
 * @brief
 */
public class MultiAdapter extends RecyclerViewBaseAdapter<DataBean> {
    public static final int TYPE1 = 1;
    public static final int TYPE2 = 2;
    public static final int TYPE3 = 3;


    public MultiAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder setLayoutViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.test_recycler_item1;
        switch (viewType) {
            case TYPE1:
                layoutId = R.layout.test_recycler_item1;
                break;
            case TYPE2:
                layoutId = R.layout.test_recycler_item2;
                break;
            case TYPE3:
                layoutId = R.layout.test_recycler_item3;
                break;
        }
        View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        return new ViewHolder(mContext, view);
    }

    @Override
    protected int setLayoutItemViewType(DataBean bean, int position) {
        if (null != bean) {
            if (bean.getId() % 3 == 0) {
                return TYPE1;
            }
            if (bean.getId() % 3 == 1) {
                return TYPE2;
            }
            if (bean.getId() % 3 == 2) {
                return TYPE3;
            }
        }
        return 0;
    }

    @Override
    protected void layoutBindData(RecyclerView.ViewHolder holder, DataBean bean, int position) {

    }
}
