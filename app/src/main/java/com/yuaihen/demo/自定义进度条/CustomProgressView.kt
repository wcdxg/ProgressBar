package com.yuaihen.demo.自定义进度条

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ProgressBar
import com.yuaihen.demo.R

/**
 * Created by Yuaihen.
 * on 2020/9/24
 */
class CustomProgressView : ProgressBar {

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progressStartColor = Color.parseColor("#E66CB1")
    private var progressEndColor = Color.parseColor("#1872FA")
    private var progressDefaultColor = Color.parseColor("#F5F5F5")

    //    private var progressDefaultColor = Color.parseColor("#ff0000")
    private var progressDefaultHeight = 0f
    private var progressCorner = 0f
    private val path by lazy { Path() }
    private val rect by lazy {
        RectF(0f, 0f, measuredWidth.toFloat() - 20f, progressDefaultHeight)
    }
    private val radiusArray = floatArrayOf(10f, 0f, 10f, 0f, 10f, 0f, 10f, 0f)

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomProgressBar)
            progressStartColor = typedArray.getColor(
                    R.styleable.CustomProgressBar_progressbar_startColor,
                    progressStartColor
            )
            progressEndColor = typedArray.getColor(
                    R.styleable.CustomProgressBar_progressbar_endColor,
                    progressEndColor
            )
            progressDefaultColor = typedArray.getColor(
                    R.styleable.CustomProgressBar_progressbar_default_color,
                    progressDefaultColor
            )
            progressDefaultHeight = typedArray.getDimension(
                    R.styleable.CustomProgressBar_progressbar_default_height,
                    25f
            )
            progressCorner =
                    typedArray.getDimension(R.styleable.CustomProgressBar_progressbar_corner_size, 21f)
            typedArray.recycle()
        }

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = progressDefaultColor

//        setLayerType(LAYER_TYPE_SOFTWARE, paint)
    }

    private var secondProgressRectF = RectF(0f, 0f, 0f, 0f)
    private var linearGradient: LinearGradient? = null
    private var endX = 0f

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        endX = progress / 100f * measuredWidth
        if (endX >= measuredWidth - 20f) {
            endX = measuredWidth - 20f
        }
        secondProgressRectF = RectF(0f, 0f, endX, progressDefaultHeight)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        //绘制第一段进度条
        canvas.translate(0f, measuredHeight / 2f - progressDefaultHeight / 2f)
        paint.xfermode = null
        paint.shader = null
        paint.color = progressDefaultColor
        canvas.drawRoundRect(rect, progressCorner, progressCorner, paint)

        //绘制第二段进度条 渐变色
        linearGradient = LinearGradient(
                0f,
                0f,
                endX,
                progressDefaultHeight,
                progressStartColor, progressEndColor,
                Shader.TileMode.CLAMP
        )
        paint.shader = linearGradient
        canvas.drawRoundRect(secondProgressRectF, progressCorner, progressCorner, paint)
    }

}