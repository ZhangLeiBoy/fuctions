package com.android.albert.listener;

/**
 * @author 张雷
 * @data 2018.06.19
 * @brief 数据加载及刷新的监听
 */
public interface DataLoadListener {

    void onLoadData(RefreshView refreshView, boolean isRefresh);
}
