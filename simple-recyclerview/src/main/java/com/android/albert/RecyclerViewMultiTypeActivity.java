package com.android.albert;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.android.albert.adapter.MultiAdapter;
import com.android.albert.base.BaseSimpleActivity;
import com.android.albert.bean.DataBean;
import com.android.albert.listener.DataLoadListener;
import com.android.albert.listener.RefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglei
 * @date 2018/6/20
 * @brief 多样式
 */
public class RecyclerViewMultiTypeActivity extends BaseSimpleActivity implements DataLoadListener {
    private Context mContext;
    private RefreshRecyclerView refreshRecyclerView;
    private MultiAdapter adapter;
    private List<DataBean> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        refreshRecyclerView = new RefreshRecyclerView(this);
        setContentView(refreshRecyclerView);
        refreshRecyclerView.setOnDataLoadListener(this);
        adapter =new  MultiAdapter(mContext);
        setHeader();
        //
        refreshRecyclerView.setAdapter(adapter);
        //
        refreshRecyclerView.showLoading();
        refreshRecyclerView.onRefresh(true);
    }

    private void setHeader() {
        TextView textView = new TextView(this);
        textView.setText("111111");
        TextView textView2 = new TextView(this);
        textView.setText("22222");
        refreshRecyclerView.addHeader(textView);
        refreshRecyclerView.addHeader(textView2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRecyclerView.onRefresh(false);
            }
        });
    }

    private List<DataBean> getData() {
        List<DataBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DataBean bean = new DataBean();
            bean.setId(i);
            bean.setTitle("12或者ragment添加代码4sdfewsfvc");
            bean.setAd(i == 3);
            list.add(bean);
        }
        return list;
    }

    @Override
    public void onLoadData(RefreshView refreshView, final boolean isRefresh) {
        if (isRefresh) {
            data.clear();
        }
        data.addAll(getData());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.appendData(data);
                refreshRecyclerView.showData(true);
            }
        }, 3000);
    }
}
