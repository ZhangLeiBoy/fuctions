package com.android.albert;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.android.albert.actionbar.R;

/**
 * @author zhanglei
 * @date 2018/6/20
 * @brief actionBar
 */
public class CommonActionBar extends RelativeLayout {
    // <editor-fold desc="导航栏控件属性">
    private LinearLayout layout_left;// 左控件容器
    private LinearLayout layout_right;// 右控件容器
    private RelativeLayout layout_center;// 中控件容器
    private View divider;
    private ProgressBar progressBar;//进度条
    private View titleView;// 全局标题View
    private View backView;// 返回按钮
    // </editor-fold>

    // <editor-fold desc="初始化">
    private Context mContext;
    public static final int ACTIONBAR_ICON = -2; // ICON
    public static final int ACTIONBAR_TITLE = -3; // TITLE
    private static final int ACTIONBAR_PROGRESS = -1;// 进度TAG

    private int barHeight = 45;// 默认ActionBar的高度
    private int background = Color.WHITE;
    private int titleColor = Color.BLACK;
    private int dividerColor = Color.DKGRAY;
    private float titleTextSize;
    private float menuTextSize;

    public CommonActionBar(Context context, AttributeSet attrs) {
        super(context, attrs, R.style.CommonActionBar);

        mContext = context;
        menuTextSize = 16f;

        setGravity(Gravity.CENTER_VERTICAL);

        TypedArray ta = mContext.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionbarStyle});
        int style = ta.getResourceId(0, R.style.CommonActionBar);
        ta.recycle();
        TypedArray taaction = mContext.getTheme().obtainStyledAttributes(style,
                R.styleable.CommonActionBar);
        barHeight = taaction.getDimensionPixelSize(R.styleable.CommonActionBar_height, barHeight);
        if (attrs != null) {
            barHeight = attrs.getAttributeIntValue("android", "layout_height", barHeight);
        }
        if (barHeight <= 0) {
            barHeight = toDip(45);
        }
        background = taaction.getColor(R.styleable.CommonActionBar_backgroundColor,
                Color.WHITE);
        titleColor = taaction.getColor(R.styleable.CommonActionBar_titleColor,
                Color.BLACK);
        dividerColor = taaction.getColor(
                R.styleable.CommonActionBar_dividerColor, Color.CYAN);
        titleTextSize = taaction.getFloat(R.styleable.CommonActionBar_titleTextSize, 17);
        boolean dividerShow = taaction.getBoolean(
                R.styleable.CommonActionBar_dividerVisible, false);
        int leftPadding = taaction.getDimensionPixelSize(
                R.styleable.CommonActionBar_leftPadding, 0);
        int rightPadding = taaction.getDimensionPixelSize(
                R.styleable.CommonActionBar_rightPadding, 0);
        taaction.recycle();
        /*
         * 初始化View: -添加左侧的控件 (用于程序的图标,返回按钮等默认,或自定义) -添加中部的控件 (图片,文本 Title)
		 * -添加右侧的控件 (菜单等)
		 */
        // 左边布局
        layout_left = new LinearLayout(mContext);
        LayoutParams lpLeft = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        lpLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layout_left.setLayoutParams(lpLeft);
        layout_left.setPadding(toDip(leftPadding), 0, toDip(rightPadding), 0);
        layout_left.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        // 中间布局（优先级最高）
        layout_center = new RelativeLayout(mContext);
        LayoutParams lpCenter = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lpCenter.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout_center.setGravity(Gravity.CENTER_VERTICAL);
        layout_center.setLayoutParams(lpCenter);
        // 右边布局
        layout_right = new LinearLayout(mContext);
        LayoutParams lpRight = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        lpRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layout_right.setLayoutParams(lpRight);
        layout_right.setPadding(toDip(leftPadding), 0, toDip(rightPadding), 0);
        layout_right.setOrientation(LinearLayout.HORIZONTAL);
        layout_right.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        //底部分割线
        divider = new View(mContext);
        LayoutParams lpDivider = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(0.5f));
        lpDivider.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        divider.setLayoutParams(lpDivider);
        divider.setBackgroundColor(dividerColor);
        //progressBar
        progressBar = new ProgressBar(mContext);
        LayoutParams progressBarParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(0.5f));
        progressBarParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        progressBar.setLayoutParams(progressBarParams);
        // 重置
        removeAllViews();
        addView(layout_center);
        addView(layout_left);
        addView(layout_right);
        addView(divider);
        addView(progressBar);

        setDividerVisible(dividerShow);

        setBackgroundColor(background);
        setActionBarHeight(barHeight);
    }

    private int toDip(int size) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return (int) (dm.density * size);
    }

    private int dip2px(float dp) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return (int) (dm.density * dp + 0.5f);
    }
    // </editor-fold>

    // <editor-fold desc="导航栏操作监听事件">
    private OnMenuClickListener menuClickListener;

    /**
     * 设置ActionBar的点击按钮点击监听
     */
    public void setMenuClickListener(OnMenuClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }

    /**
     * 当ActionBar点击时进行回调
     */
    public interface OnMenuClickListener {
        void onMenuClick(int mid, View v);
    }

    // </editor-fold>

    // <editor-fold desc="分割线及导航栏公共方法">

    /**
     * 隐藏菜单
     *
     * @param mid menuId, not repeat, keep > 0
     */
    public void hideMenu(int mid) {
        View v = layout_right.findViewWithTag(mid);
        if (v == null) {
            View leftV = layout_left.findViewWithTag(mid);
            if (leftV != null) {
                leftV.setVisibility(View.GONE);
            }
            return;
        }
        v.setVisibility(GONE);
    }

    public void showMenu(int mid) {
        View v = layout_right.findViewWithTag(mid);
        if (v == null) {
            View leftV = layout_left.findViewWithTag(mid);
            if (leftV != null) {
                leftV.setVisibility(View.VISIBLE);
            }
            return;
        }
        v.setVisibility(VISIBLE);
    }

    /**
     * 设定ActionBar的高度,自定义
     */
    public void setActionBarHeight(int height) {
        barHeight = height;
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
    }

    /**
     * 设置分割线颜色
     */
    public void setDividerColor(int c) {
        divider.setVisibility(View.VISIBLE);
        divider.setBackgroundColor(c);
    }

    /**
     * 设置Divider的显示属性
     */
    public void setDividerVisible(boolean isShow) {
        divider.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    // </editor-fold>

    // <editor-fold desc="左侧菜单栏操作">

    /**
     * 隐藏左侧按钮
     */
    public void hideLeftMenu() {
        layout_left.setVisibility(View.GONE);
    }

    /**
     * 显示左侧按钮
     */
    public void showLeftMenu() {
        layout_left.setVisibility(View.VISIBLE);
    }

    //清空左侧所有控件
    public void removeAllLeftView() {
        layout_left.removeAllViews();
    }

    /**
     * 删除左侧指定id按钮
     *
     * @param mid
     */
    public void removeLeftView(int mid) {
        View v = layout_left.findViewWithTag(mid);
        if (v == null) {
            return;
        }
        layout_left.removeView(v);
    }

    /**
     * 设置左侧图片（用来设置返回按钮）
     */
    public void setBackView(int drawableId) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(drawableId);
        imageView.setLayoutParams(new LayoutParams(toDip(40), toDip(40)));
        int padding = toDip(10);
        imageView.setPadding(padding, padding, padding, padding);
        setBackView(imageView);
    }

    //添加左侧自定义控件
    public void setBackView(View v) {
        removeAllLeftView();
        backView = v;
        addLeftView(ACTIONBAR_ICON, v);
    }

    public View addLeftView(int mid, @DrawableRes int drawableId) {
        View v = layout_left.findViewWithTag(mid);
        if (v != null) {
            return v;
        }
        ImageView menuView = new ImageView(mContext);
        menuView.setImageResource(drawableId);
        return addLeftView(mid, menuView, true);
    }

    /**
     * 添加左侧菜单Menu，自定义的view，输出是宽高为45dip的View
     *
     * @param mid
     * @param leftView
     * @return
     */
    public View addLeftView(final int mid, View leftView) {
        return addLeftView(mid, leftView, true);
    }

    /**
     * 添加左侧菜单Menu，如果不是正方形，会判断有没有LayoutParams，如果没有，默认设置为宽自适应
     *
     * @param mid
     * @param leftView
     * @param isSquareView 是否是正方形的view
     * @return
     */
    public View addLeftView(final int mid, View leftView, boolean isSquareView) {
        return addLeftView(mid, leftView, isSquareView, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != menuClickListener) {
                    menuClickListener.onMenuClick(mid, v);
                }
            }
        });
    }

    /**
     * 添加左侧菜单Menu，如果不是正方形，会判断有没有LayoutParams，如果没有，默认设置为宽自适应
     *
     * @param mid
     * @param leftView
     * @param isSquareView
     * @param mOnClickListener
     * @return
     */
    public View addLeftView(final int mid, View leftView, boolean isSquareView,
                            OnClickListener mOnClickListener) {
        View v = layout_left.findViewWithTag(mid);
        if (v != null) {
            return v;
        }
        if (mOnClickListener != null) {
            leftView.setOnClickListener(mOnClickListener);
        }

        if (leftView != null && leftView instanceof TextView) {
            ((TextView) leftView).setTextSize(menuTextSize);
        }

        leftView.setTag(mid);
        ViewGroup.LayoutParams leftviewpar = leftView.getLayoutParams();
        if (leftviewpar == null) {
            if (isSquareView) {
                leftView.setLayoutParams(new LayoutParams(toDip(45), toDip(45)));
            } else {
                leftviewpar = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, toDip(45));
                leftView.setLayoutParams(leftviewpar);
            }
        }
        layout_left.addView(leftView);
        return leftView;
    }

    // </editor-fold>

    // <editor-fold desc="中间标题区域">

    public void hideTitleView() {
        titleView.setVisibility(View.GONE);
    }


    /**
     * 清空标题layout
     */
    public void removeTitleViews() {
        layout_center.removeAllViews();
    }

    /**
     * 设置标题文本颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        titleColor = color;
        if (titleView != null && titleView instanceof TextView) {
            ((TextView) titleView).setTextColor(color);
        }
    }

    public void setTitleTextSize(float size) {
        titleTextSize = size;
        if (titleView != null && titleView instanceof TextView) {
            ((TextView) titleView).setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    public void setTitleTextBold(boolean isBold) {
        if (titleView != null && titleView instanceof TextView) {
            TextView tv = (TextView) titleView;
            tv.getPaint().setFakeBoldText(isBold);
        }
    }

    /**
     * 设置导航栏文本
     *
     * @param title
     */
    public void setTitle(String title) {
        if (titleView == null
                || (titleView != null && !(titleView instanceof TextView))) {
            TextView titleTv = new TextView(mContext);
            titleTv.setEllipsize(TruncateAt.END);
            titleTv.setMaxEms(8);
            LayoutParams lp = new LayoutParams(
                    LayoutParams
                            .WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
            lp.addRule(CENTER_IN_PARENT);
            titleTv.setLayoutParams(lp);
            titleTv.setSingleLine();
            titleTv.setGravity(Gravity.CENTER_VERTICAL);
            titleTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != menuClickListener) {
                        menuClickListener.onMenuClick(ACTIONBAR_TITLE, v);
                    }
                }
            });
            titleTv.setTextSize(titleTextSize);
            titleTv.setTextColor(titleColor);
            titleTv.setText(title);
            setTitleView(titleTv);
        } else if (titleView instanceof TextView) {
            ((TextView) titleView).setText(title);
        }
    }

    /**
     * 标题默认居中显示比setTitle㡳一个优先级
     *
     * @param view
     */
    public void setTitleView(View view) {
        if (view.getLayoutParams() != null
                && view.getLayoutParams() instanceof LayoutParams) {
            LayoutParams lp = (LayoutParams) view
                    .getLayoutParams();
            if (lp.getRules() == null) {
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                view.setLayoutParams(lp);
            }
        } else {
            LayoutParams lp = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setLayoutParams(lp);
        }
        titleView = view;
        layout_center.addView(view);
    }

    // </editor-fold>

    // <editor-fold desc="右侧菜单栏操作">

    /**
     * 删除菜单
     *
     * @param mid menuId, not repeat, keep > 0
     */
    public void removeRightMenu(int mid) {
        View v = layout_right.findViewWithTag(mid);
        if (v == null) {
            return;
        }
        layout_right.removeView(v);
    }

    /**
     * 删除右侧所有按钮
     */
    public void removeAllRightView() {
        layout_right.removeAllViews();
    }

    /**
     * 添加右侧菜单Menu，自定义的view，输出是宽高为45dip的View
     *
     * @param mid
     * @param menuView
     * @return
     */
    public View addMenu(final int mid, View menuView) {
        return addMenu(mid, menuView, true);
    }

    /**
     * 添加右侧菜单Menu，如果不是正方形，会判断有没有LayoutParams，如果没有，默认设置为宽自适应
     *
     * @param mid
     * @param menuView
     * @param isSquareView 是否是正方形的view
     * @return
     */
    public View addMenu(final int mid, View menuView, boolean isSquareView) {
        return addMenu(mid, menuView, isSquareView, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != menuClickListener) {
                    menuClickListener.onMenuClick(mid, v);
                }
            }
        });
    }

    /**
     * 添加右侧菜单Menu，如果不是正方形，会判断有没有LayoutParams，如果没有，默认设置为宽自适应
     *
     * @param mid
     * @param menuView
     * @param isSquareView
     * @param mOnClickListener
     * @return
     */
    public View addMenu(final int mid, View menuView, boolean isSquareView,
                        OnClickListener mOnClickListener) {
        addMenu(mid, menuView, isSquareView, false, mOnClickListener);
        return menuView;
    }

    /**
     * 添加右侧菜单Menu，如果不是正方形，会判断有没有LayoutParams，如果没有，默认设置为宽自适应
     *
     * @param mid
     * @param menuView
     * @param isSquareView
     * @return
     */
    public View addMenu(final int mid, View menuView, boolean isSquareView, boolean left_to_right,
                        OnClickListener mOnClickListener) {
        View v = layout_right.findViewWithTag(mid);
        if (v != null) {
            return v;
        }
        if (mOnClickListener != null) {
            menuView.setOnClickListener(mOnClickListener);
        }
        menuView.setTag(mid);
        if (menuView instanceof TextView) {
            ((TextView) menuView).setTextSize(menuTextSize);
        }
        ViewGroup.LayoutParams menupar = (ViewGroup.LayoutParams) menuView
                .getLayoutParams();
        if (menupar == null) {
            if (isSquareView) {
                menuView.setLayoutParams(new ViewGroup.LayoutParams(toDip(45), toDip(45)));
            } else {
                menuView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, toDip(45)));
            }
        }
        if (left_to_right) {
            layout_right.addView(menuView, 0);
        } else {
            layout_right.addView(menuView);
        }

        return menuView;
    }

    // </editor-fold>

}
