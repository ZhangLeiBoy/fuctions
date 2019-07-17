package com.android.albert.base.utils.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.albert.base.R;
import com.android.albert.base.utils.dialog.listener.OnDialogCancelListener;
import com.android.albert.base.utils.dialog.listener.OnDialogConfirmListener;
import com.android.albert.base.utils.dialog.listener.OnDialogListItemClickListener;

import java.util.List;

/**
 * @author 张雷
 * @date 2018/6/26
 * @brief 弹框
 */

public class DialogUtils {


    public static void showDialog(final Context mContext,
                                  final String content,
                                  final OnDialogConfirmListener onDialogConfirmListener,
                                  final OnDialogCancelListener onDialogCancelListener) {
        showDialog(mContext, DialogHelper.getString(R.string.app_tip), content, "", "", true, onDialogConfirmListener, onDialogCancelListener);
    }

    /**
     * @param mContext
     * @param content
     * @param onDialogConfirmListener
     * @param onDialogCancelListener
     * @return
     */
    public static void showDialog(final Context mContext,
                                  final String topTipText,
                                  final String content,
                                  final String confirmText,
                                  final String cancelText,
                                  final boolean cancelAble,
                                  final OnDialogConfirmListener onDialogConfirmListener,
                                  final OnDialogCancelListener onDialogCancelListener) {
        if (DialogHelper.isActivityDestory(mContext)) {
            return;
        }
        AlertDialog mCusDialog = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_common_layout, null);
        //view.setBackground(mContext.getResources().getDrawable(R.drawable.dialog_common_root));
        //设置顶部提示内容
        TextView topTip = view.findViewById(R.id.dialog_top_tip);
        if (TextUtils.isEmpty(topTipText)) {
            topTip.setVisibility(View.GONE);
        } else {
            topTip.setVisibility(View.VISIBLE);
            topTip.setText(topTipText);
        }
        //设置显示的内容
        TextView contentTv = view.findViewById(R.id.dialog_content);
        if (!TextUtils.isEmpty(content)) {
            contentTv.setText(content);
        }
        //
        TextView confirmTv = view.findViewById(R.id.dialog_delete_confirm);
        if (!TextUtils.isEmpty(confirmText)) {
            confirmTv.setText(confirmText);
        }
        //
        TextView cancelTv = view.findViewById(R.id.dialog_delete_cancel);
        if (!TextUtils.isEmpty(cancelText)) {
            cancelTv.setText(cancelText);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogStyle);
        builder.setView(view);

        mCusDialog = builder.create();
        mCusDialog.setCancelable(cancelAble);
        mCusDialog.setCanceledOnTouchOutside(cancelAble);

        if (!cancelAble) {
            mCusDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP
                            && keyCode == KeyEvent.KEYCODE_BACK
                            && event.getRepeatCount() == 0) {
                        return true;
                    }
                    return false;
                }
            });
        }
        Window mWindow = mCusDialog.getWindow();
//        int minWidth = (int) (DialogHelper.getScreenWidth() * .8);
//        WindowManager.LayoutParams layoutparams = mWindow.getAttributes();
//        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
//        layoutparams.width = 100;
//        layoutparams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        layoutparams.height = metrics.heightPixels;
//        mWindow.setAttributes(layoutparams);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setWindowAnimations(R.style.AnimBottom);
        mCusDialog.show();

        final AlertDialog finalMCusDialog = mCusDialog;
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalMCusDialog.dismiss();
                if (null != onDialogCancelListener) {
                    onDialogCancelListener.onCancel();
                }
            }
        });

        confirmTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finalMCusDialog.dismiss();
                if (null != onDialogConfirmListener) {
                    onDialogConfirmListener.onConfirm();
                }
            }
        });
    }

    /**
     * 列表dialog 封装,动态数据变化 简化
     *
     * @param mContext
     * @param list
     * @param listener
     */
    public static void showListDialogFromBottom(final Context mContext, final List<DialogItemBean> list, final OnDialogListItemClickListener listener) {
        if (DialogHelper.isActivityDestory(mContext) || list == null) {
            return;
        }
        AlertDialog dialog = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_more_with_cancle_layout, null);
        final TextView cancle = view.findViewById(R.id.cancle);
        ListView listView = view.findViewById(R.id.dialog_more_list);
        final AlertDialog finalDialog = dialog;
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_more_with_cancle_item, null);
                }

                final DialogItemBean bean = list.get(position);
                TextView textView = convertView.findViewById(R.id.dialog_more_list_tv);
                textView.setText(bean.getTitle());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalDialog.dismiss();
                        if (listener != null) {
                            listener.onDialogListItemClick(bean.getType());
                        }
                    }
                });
                return convertView;
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogStyle);
        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        int minWidth = (int) (DialogHelper.getScreenWidth() * .9);
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams layoutparams = mWindow.getAttributes();
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        layoutparams.width = minWidth;
        layoutparams.height = metrics.heightPixels;
        mWindow.setAttributes(layoutparams);
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setWindowAnimations(R.style.AnimBottom);
        dialog.show();
    }


}
