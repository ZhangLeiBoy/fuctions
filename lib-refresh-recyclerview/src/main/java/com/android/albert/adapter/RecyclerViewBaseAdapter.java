package com.android.albert.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张雷
 * @data 2018.06.19
 * @brief base adapter
 */
public abstract class RecyclerViewBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context mContext;
    protected List<T> datas = new ArrayList<>();

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    public RecyclerViewBaseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void clearData() {
        datas.clear();
        notifyDataSetChanged();
    }

    public void appendData(List<T> datas) {
        if (null != datas && datas.size() > 0) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 删除一行
     *
     * @param position
     */
    public void removeItem(int position) {
        if (datas.size() > 0) {
            if (datas.size() < 2 && datas.size() != 0) {
                datas.remove(0);
                notifyDataSetChanged();
            } else {//更新列表
                datas.remove(position);
                notifyDataSetChanged();
                notifyItemRemoved(position + getHeadersCount());
                notifyItemRangeChanged(position + getHeadersCount(), getItemCount());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            return holder;
        }
        return setLayoutViewHolder(parent, viewType);
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        int realPosition = position - getHeadersCount();
        T bean = null;
        if(datas.size()>realPosition){
            bean = datas.get(realPosition);
        }
        return setLayoutItemViewType(bean, realPosition);
    }

    private int getRealItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        int realPosition = position - getHeadersCount();
        if (datas.size() > realPosition) {
            layoutBindData(holder, datas.get(realPosition), realPosition);
        }
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return spanSize(gridLayoutManager, spanSizeLookup, position);
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    private int spanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
        int viewType = getItemViewType(position);
        if (mHeaderViews.get(viewType) != null) {
            return layoutManager.getSpanCount();
        } else if (mFootViews.get(viewType) != null) {
            return layoutManager.getSpanCount();
        }
        if (oldLookup != null)
            return oldLookup.getSpanSize(position);
        return 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    protected abstract RecyclerView.ViewHolder setLayoutViewHolder(ViewGroup parent, int viewType);

    protected abstract int setLayoutItemViewType(T bean, int position);

    protected abstract void layoutBindData(RecyclerView.ViewHolder holder, T bean, int position);
}
