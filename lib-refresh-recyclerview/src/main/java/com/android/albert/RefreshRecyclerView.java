package com.android.albert;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.albert.adapter.RecyclerViewBaseAdapter;
import com.android.albert.footer.CustomFooter;
import com.android.albert.header.CustomHeader;
import com.android.albert.listener.DataLoadListener;
import com.android.albert.listener.RefreshView;
import com.android.albert.recyclerlib.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张雷
 * @data 2018.06.19
 * @brief 带刷新效果的recyclerview
 */
public class RefreshRecyclerView extends RelativeLayout implements RefreshView {
    private Context mContext;
    //<editor-fold  desc="布局参数">

    private RelativeLayout refreshRoot;//根布局，最底层view
    private SmartRefreshLayout refreshLayout;//刷新控制的布局
    private RecyclerView recyclerView;//recyclview
    private RelativeLayout loadingRoot;//正在加载的动画 view层
    private RelativeLayout emptyRoot;//数据为空的界面展示层
    private RelativeLayout failureRoot;//网络异常等情况下的失败界面展示

    private RecyclerView.Adapter adapter;
    //</editor-fold>

    //<editor-fold  desc="监听事件">

    private DataLoadListener dataLoadListener;//上拉 下拉的监听回调事件
    //</editor-fold>

    //<editor-fold desc="属性变量 ">
    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFootViews = new ArrayList<>();
    protected boolean showEmptyWithHeader = true;//是否在空数据时共存空数据界面与header头
    //</editor-fold>

    //<editor-fold desc="构造方法 ">

    public RefreshRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.refresh_recycler, null);
        refreshRoot = view.findViewById(R.id.refresh_root);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        emptyRoot = view.findViewById(R.id.refresh_empty_root);
        loadingRoot = view.findViewById(R.id.refresh_loading_root);
        failureRoot = view.findViewById(R.id.refresh_failure_root);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        setLayoutManager(manager);

        setRefreshThemeColorIds(R.color.refrsh_theme_default_color1, R.color.refrsh_theme_default_color2);
        setRefreshHeader(new CustomHeader(mContext));
        setRefreshFooter(new CustomFooter(mContext));

        addView(view);
        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (null != dataLoadListener) {
                    dataLoadListener.onLoadData(RefreshRecyclerView.this, true);
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (null != dataLoadListener) {
                    dataLoadListener.onLoadData(RefreshRecyclerView.this, false);
                }
            }
        });
    }
    //</editor-fold>

    // <editor-fold desc="开放方法，外部调用 ">

    public void setRefreshThemeColor(@ColorInt int... primaryColors) {
        refreshLayout.setPrimaryColors(primaryColors);
    }

    public void setRefreshThemeColorIds(@ColorRes int... primaryColorId) {
        refreshLayout.setPrimaryColorsId(primaryColorId);
    }

    /**
     * 设置刷新头样式
     *
     * @param header
     */
    public void setRefreshHeader(com.scwang.smartrefresh.layout.api.RefreshHeader header) {
        refreshLayout.setRefreshHeader(header);
    }

    /**
     * 设置上拉加载样式
     *
     * @param footer
     */
    public void setRefreshFooter(com.scwang.smartrefresh.layout.api.RefreshFooter footer) {
        refreshLayout.setRefreshFooter(footer);
    }

    /**
     * 注册数据加载监听
     *
     * @param dataLoadListener
     */
    @Override
    public void setOnDataLoadListener(DataLoadListener dataLoadListener) {
        this.dataLoadListener = dataLoadListener;
    }

    /**
     * 是否在空数据的情况下展示Header 头,默认为true
     *
     * @param showEmptyWithHeader
     */
    public void setShowEmptyWithHeader(boolean showEmptyWithHeader) {
        this.showEmptyWithHeader = showEmptyWithHeader;
    }

    /**
     * 设置是否可进行上拉加载数据
     *
     * @param enableLoadMore
     */
    @Override
    public void setEnableLoadMore(boolean enableLoadMore) {
        refreshLayout.setEnableLoadMore(enableLoadMore);
    }

    /**
     * 设置是否可进行刷新数据
     *
     * @param enableRefresh
     */
    @Override
    public void setEnableRefresh(boolean enableRefresh) {
        refreshLayout.setEnableRefresh(enableRefresh);
    }

    /**
     * 刷新
     *
     * @param isSilent 是否静默刷新
     */
    @Override
    public void onRefresh(boolean isSilent) {
        if (!isSilent) {
            refreshLayout.autoRefresh();
        }
        if (null != dataLoadListener) {
            dataLoadListener.onLoadData(this, true);
        }
    }

    /**
     * 显示正在加载数据的状态
     */
    @Override
    public void showLoading() {
        loadingRoot.setVisibility(View.VISIBLE);
        emptyRoot.setVisibility(View.GONE);
        failureRoot.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    /**
     * 展示失败界面
     */
    @Override
    public void showFailure() {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();

        loadingRoot.setVisibility(View.GONE);
        emptyRoot.setVisibility(View.GONE);
        failureRoot.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }

    /**
     * 展示数据
     *
     * @param success 是否成功获取到数据，影响到上一次刷新的时间
     */
    @Override
    public void showData(boolean success) {
        refreshLayout.finishRefresh(success);
        refreshLayout.finishLoadMore(success);

        loadingRoot.setVisibility(View.GONE);
        failureRoot.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        if (adapter.getItemCount() > 0) {
            emptyRoot.setVisibility(View.GONE);
        } else {
            setEnableLoadMore(false);
            if (showEmptyWithHeader && mHeaderViews.size() > 0) {
                emptyRoot.setVisibility(View.VISIBLE);
            } else {
                emptyRoot.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    //</editor-fold>

    /**
     * 设置一个头布局
     *
     * @param view
     */
    public void addHeader(View view) {
        if (null != view) {
            mHeaderViews.add(view);
        }
    }

    /**
     * 设置一个头布局
     *
     * @param view
     */
    public void addFooter(View view) {
        if (null != view) {
            mFootViews.add(view);
        }
    }

    /**
     * 设置一个适配器
     *
     * @param userAdapter
     */
    public RecyclerView.Adapter setAdapter(RecyclerViewBaseAdapter userAdapter) {
        adapter = userAdapter;
        if (mHeaderViews.size() > 0 || mFootViews.size() > 0) {
            for (View view : mHeaderViews) {
                userAdapter.addHeaderView(view);
            }
            for (View view : mFootViews) {
                userAdapter.addFootView(view);
            }
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return this.adapter;
    }

    /**
     * 设置RecycleView的布局类型，线性还是网格类型
     *
     * @param layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public <T extends RecyclerView.ItemDecoration> void setDivider(T t) {
        recyclerView.addItemDecoration(t);
    }

    public <T extends RecyclerView.ItemAnimator> void setItemAnimator(T t) {
        recyclerView.setItemAnimator(t);
    }

    public RecyclerView getRecyclerview() {
        return recyclerView;
    }

}
