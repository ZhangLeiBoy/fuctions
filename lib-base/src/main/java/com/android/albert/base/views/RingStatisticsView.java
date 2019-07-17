package com.android.albert.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.android.albert.base.R;

/**
 * @author 张雷
 * @date 2018/7/9
 * @brief 百分比环形统计
 */
public class RingStatisticsView extends View {
    private Context mContext;
    private float[] mPercent = new float[]{
            0.1f,
            0.2f,
            0.3f,
            0.4f};
    private int[] mColors = new int[]{
            Color.parseColor("#F9AA28"),
            Color.parseColor("#009752"),
            Color.parseColor("#2EC1FB"),
            Color.parseColor("#FA6723")};

    private Paint mPaint, mLinePaint;
    private TextPaint mTextPaint;
    private RectF mRectF;

    private static final String DEFAULT_CENTER_TEXT = "统计";
    private static final int DEFAULT_RINGWIDTH = 5;                     //
    private static final int DEFAULT_TEXTSIZE1 = 18;
    private static final int DEFAULT_TEXTSIZE2 = 14;
    private static final int DEFAULT_TEXTSIZE3 = 15;

    private float mRingWidth = 0;                                       //图标宽度
    private int textSize1 = 0,                                          //圈内的个数字号
            textSize2 = 0;                                              //圈内的提示语字号
    private int mTextColor1 = Color.parseColor("#3f3f3f"),   //圈内总个数 颜色值
            mTextColor2 = Color.parseColor("#9b9b9b");       //圈内提示的 颜色值
    private String mstr_total_text = DEFAULT_CENTER_TEXT;               //默认值
    private String mstr_total_number = "1000";                          //默认值
    private int width, height;
    private int radius;

    public RingStatisticsView(Context context) {
        super(context);
        initSize(context);
        init();
    }

    public RingStatisticsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initSize(context);
        init();
    }

    public RingStatisticsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initSize(context);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RingStatisticsView);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.RingStatisticsView_rsv_RingWidth) {
                mRingWidth = array.getInt(attr, dp2px(context, DEFAULT_RINGWIDTH));
            } else if (attr == R.styleable.RingStatisticsView_rsv_CenterText) {
                mstr_total_text = array.getString(attr);
            } else if (attr == R.styleable.RingStatisticsView_rsv_CenterNumber) {
                mstr_total_number = array.getString(attr);
            } else if (attr == R.styleable.RingStatisticsView_rsv_CenterNumberColor) {
                mTextColor1 = array.getColor(attr, Color.parseColor("#3f3f3f"));
            } else if (attr == R.styleable.RingStatisticsView_rsv_CenterTextColor) {
                mTextColor2 = array.getColor(attr, Color.parseColor("#9b9b9b"));
            } else if (attr == R.styleable.RingStatisticsView_rsv_CenterNumberSize) {
                textSize1 = array.getDimensionPixelSize(attr, sp2px(context, DEFAULT_TEXTSIZE1));
            } else if (attr == R.styleable.RingStatisticsView_rsv_CenterTextSize) {
                textSize2 = array.getDimensionPixelSize(attr, sp2px(context, DEFAULT_TEXTSIZE2));
            }
        }
        array.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRingWidth);

        mLinePaint = new Paint(mPaint);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);

        mRectF = new RectF();
    }

    private void initSize(Context context) {
        mContext = context;
        if (mRingWidth == 0) {
            mRingWidth = dp2px(context, dp2px(context, DEFAULT_RINGWIDTH));
        }
        if (textSize1 == 0) {
            textSize1 = sp2px(context, DEFAULT_TEXTSIZE1);
        }
        if (textSize2 == 0) {
            textSize2 = sp2px(context, DEFAULT_TEXTSIZE2);
        }
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dp2px(Context context, float dpValue) {
        final float densityScale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * densityScale + 0.5f);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.max(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        radius = Math.max(width, height) / 15;
        mRectF.set(getPaddingLeft() + mRingWidth / 2 + radius,
                getPaddingTop() + mRingWidth / 2 + radius,
                width - getPaddingRight() - mRingWidth / 2 - radius,
                getHeight() - getPaddingBottom() - mRingWidth / 2 - radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float sweepAngle = 0f;
        float startAngle = 180f;
        float angle = 0f;
        int mCount = mPercent.length;
        mPaint.setStrokeWidth(mRingWidth);
        mLinePaint.setStrokeWidth(mRingWidth);
        for (int i = 0; i < mCount; i++) {
            mPaint.setColor(mColors[i]);
            startAngle += sweepAngle;
            sweepAngle = 360 * mPercent[i];
            canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaint);
            angle = (startAngle - 180) + sweepAngle / 2;
        }
        mLinePaint.setColor(Color.WHITE);
        for (int i = 0; i < mCount; i++) {
            if (i == 0) {
                angle = 179;
            } else {
                angle += 360 * mPercent[i - 1];
            }
            canvas.drawArc(mRectF, angle, 1, false, mLinePaint);
        }
        //总个数   “1000”
        mTextPaint.setTextSize(textSize1);
        float textwidth1 = mTextPaint.measureText(mstr_total_number);
        mTextPaint.setColor(mTextColor1);
        canvas.drawText(mstr_total_number, width / 2 - textwidth1 / 2,
                (height / 2 - (mTextPaint.getFontMetrics().descent - mTextPaint.getFontMetrics().ascent) / 2) + dp2px(mContext, 8), mTextPaint);
        //统计类别提示 “稿件”
        mTextPaint.setTextSize(textSize2);
        float textwidth2 = mTextPaint.measureText(mstr_total_text);
        mTextPaint.setColor(mTextColor2);
        canvas.drawText(mstr_total_text, width / 2 - textwidth2 / 2, (height / 2 + (mTextPaint.getFontMetrics().descent - mTextPaint.getFontMetrics().ascent) / 2) + dp2px(mContext, 10), mTextPaint);
        //
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.save();
    }

    public void setPercentAndColors(float[] percent, int[] colors) {
        if (percent != null && colors != null) {
            if (percent.length == colors.length) {
                mPercent = percent;
                mColors = colors;
            } else {
                throw new IllegalArgumentException("length of percent must equals length of colors");
            }
        }
    }

    public void setRingWidth(float ringWindth) {
        mRingWidth = ringWindth;
    }

    public void setCenterText(String text) {
        mstr_total_text = text;
    }

    public void setCenterNumber(String number) {
        mstr_total_number = number;
    }

    public void setCenterTextColor(int color) {
        mTextColor1 = color;
    }

    public void setCenterNumberColor(int color) {
        mTextColor2 = color;
    }

    public void setCenterTextSize(int size) {
        textSize1 = size;
    }

    public void setCenterNumberSize(int size) {
        textSize2 = size;
    }

    public void refresh() {
        postInvalidate();
    }
}
