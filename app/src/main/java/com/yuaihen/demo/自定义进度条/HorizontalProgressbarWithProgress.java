package com.yuaihen.demo.自定义进度条;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.yuaihen.demo.R;
import com.yuaihen.demo.UiUtils;

/**
 * Created by Yuaihen.
 * on 2018/11/14
 * 自定义炫酷进度条
 * eg1:  ::::::::::::::::: 50% :::::::::::::::::::
 * eg2:  :::::::::::::::::::::::::::::::::::100%
 */
public class HorizontalProgressbarWithProgress extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACH = 0xFFD3D6DA;
    private static final int DEFAULT_HEIGHT_UNREACH = 2; //dp
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH = 2;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp

    protected int mTextSize = UiUtils.sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mTextOffset = UiUtils.dp2px(DEFAULT_TEXT_OFFSET);
    protected int mUnReachColor = DEFAULT_COLOR_UNREACH;
    protected int mUnReachHeight = UiUtils.dp2px(DEFAULT_HEIGHT_UNREACH);
    protected int mReachColor = DEFAULT_COLOR_REACH;
    protected int mReachHeight = UiUtils.dp2px(DEFAULT_HEIGHT_REACH);

    protected Paint mPaint = new Paint();

    private int mRealWidth;


    public HorizontalProgressbarWithProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyleAttrs(attrs);
    }


    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyleAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressbarWithProgress);
        mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_text_color, DEFAULT_TEXT_COLOR);
        mUnReachColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_color, DEFAULT_COLOR_UNREACH);
        mUnReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_height, DEFAULT_HEIGHT_UNREACH);
        mReachColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_reach_color, DEFAULT_COLOR_REACH);
        mReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_reach_height, DEFAULT_HEIGHT_REACH);
        mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_offset, DEFAULT_TEXT_OFFSET);
        ta.recycle();

        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //水平进度条wrap_content没有意义  必须用户给定具体值或者match_parent
        //        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);

        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthVal, height);

        //实际绘制区域的宽度=view的宽度-padding
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        //移动到需要绘制位置的起始中间位置
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        //是否需要绘制右边区域 --------50%---------------
        boolean noNeedUnRech = false;

        //draw reach bar
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        //绘制百分比   当前进度/最大进度
        //绘制起始位置X
        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnRech = true;
        }

        //        float endX = radio * mRealWidth - mTextOffset / 2;
        //绘制结束位置X
        float endX = progressX - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        //draw text
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX, y, mPaint);

        //draw unreach bar
        if (!noNeedUnRech) {
            //右边进度条起始点
            float startX = progressX + textWidth + mTextOffset / 2;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(startX, 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();
    }

    /**
     * 测量高度
     */
    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            //以进度条中最高的为默认高度  - 字体的高度

            //测量字体高度
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop()            //getPaddingTop() 和 getPaddingBottom()必加
                    + getPaddingBottom()
                    + Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));

            if (mode == MeasureSpec.AT_MOST) {
                //最大不能超过父类的size 因此取两者中较小的
                result = Math.min(result, size);
            }

        }

        return result;
    }
}
