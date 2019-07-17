package com.android.albert.listener;

/**
 * @author 张雷
 * @data 2018.06.19
 * @brief 监听
 */
public interface RefreshView {

    /**
     * 设置数据加载刷新的监听
     */
    void setOnDataLoadListener(DataLoadListener dataLoadListener);

    /**
     * 是否可以进行上拉加载更多数据
     */
    void setEnableLoadMore(boolean enableLoadMore);

    /**
     * 是否可以进行下拉刷新
     */
    void setEnableRefresh(boolean enableRefresh);

    /**
     * 刷新数据， 是否为静默刷新
     */
    void onRefresh(boolean isSilent);

    /**
     * 展示正在加载内容的动画效果
     */
    void showLoading();

    /**
     * 是否在空数据时展示头部内容
     */
    void showData(boolean emptyWithHeader);

    /**
     * 加载失败
     */
    void showFailure();
}
