package com.yuaihen.demo.自定义进度条;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.yuaihen.demo.R;
import com.yuaihen.demo.UiUtils;

/**
 * Created by Yuaihen.
 * on 2018/11/14
 * 圆形进度条
 */
public class RoundProgressBarWithProgress extends HorizontalProgressbarWithProgress {

    //半径
    private int mRadius = UiUtils.dp2px(30);
    //画笔的宽度
    private int mMaxPaintWidth;
    private RectF mRectF;


    public RoundProgressBarWithProgress(Context context) {
        this(context, null);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mReachHeight = (int) (mUnReachHeight * 2.5f);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithProgress);
        mRadius = ta.getDimensionPixelOffset(R.styleable.RoundProgressBarWithProgress_radius, mRadius);
        ta.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setStrokeCap(Paint.Cap.ROUND);


    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mMaxPaintWidth = Math.max(mReachHeight, mUnReachHeight);
        //默认四个padding一致
        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();

        //View的宽高(包含圆形和画笔的宽度)
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, widthMeasureSpec);
        //源码方法判断Mode 对应的Size
        int readWidth = Math.min(width, height);

        //圆的半径
        mRadius = (readWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;

        mRectF = new RectF(0, 0, mRadius * 2, mRadius * 2);
        setMeasuredDimension(readWidth, readWidth);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        //保存画布的状态和还原画布之前的状态  针对画布做出的操作
        //<a link="https://www.cnblogs.com/lcchuguo/p/5117220.html">save和restore</a>
        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);

        mPaint.setStyle(Paint.Style.STROKE);
        //dras unreach bar
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        //draw reach bar
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);

        //弧度  画扇形
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(mRectF, 0, sweepAngle, false, mPaint);

        //draw text
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);

        canvas.restore();

    }
}
