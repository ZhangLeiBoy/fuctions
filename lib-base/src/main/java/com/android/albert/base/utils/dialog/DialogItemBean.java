package com.android.albert.base.utils.dialog;

/**
 * @author 张雷
 * @date 2018/6/26
 * @brief
 */

public class DialogItemBean {
    private int type;//通过此字段可区分回调的点击事件
    private String title;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
