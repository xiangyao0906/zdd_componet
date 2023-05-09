package com.juju.core.widgets.dialog

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import com.xinly.core.R

/**
 *
 *
 *
 * Created by xiangyao on 2020/5/14.
 */
class LoadingView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : View(context, attrs, defStyleAttr) {
    /**
     * 菊花颜色
     */
    private var mColor = Color.argb(255, 255, 255, 255)

    /**
     * 开始角度
     */
    private var mStartAngle = 0

    /**
     * 每条线的宽度
     */
    private var mStrokeWidth = 0f

    /**
     * 是否自动开始旋转
     */
    private var isAutoStart = false

    /**
     * 一共多少条先
     */
    private val mLineCount = 12
    private val minAlpha = 0

    /**
     * 每一条线叠加的
     */
    private val mAngleGradient = 360 / mLineCount

    /**
     * 每条线的颜色
     */
    private val mColors = IntArray(mLineCount)

    /**
     * 画笔
     */
    private var mPaint: Paint? = null

    /**
     * 动画Handler
     */
    private val mAnimHandler: Handler? = Handler(Looper.getMainLooper())

    /**
     * 动画任务
     */
    private val mAnimRunnable: Runnable = object : Runnable {
        override fun run() {
            mStartAngle += mAngleGradient
            invalidate()
            mAnimHandler!!.postDelayed(this, 50)
        }
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(
        context,
        attrs,
        0
    )

    private fun setup(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingView,
            defStyleAttr,
            defStyleRes
        )
        mColor = typedArray.getColor(R.styleable.LoadingView_aiv_color, mColor)
        mStartAngle = typedArray.getInt(R.styleable.LoadingView_aiv_startAngle, mStartAngle)
        mStrokeWidth =
            typedArray.getDimension(R.styleable.LoadingView_aiv_strokeWidth, mStrokeWidth)
        isAutoStart = typedArray.getBoolean(R.styleable.LoadingView_aiv_auto_start, true)
        typedArray.recycle()
        initialize()
    }

    private fun initialize() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val alpha = Color.alpha(mColor)
        val red = Color.red(mColor)
        val green = Color.green(mColor)
        val blue = Color.blue(mColor)
        val alphaGradient = Math.abs(alpha - minAlpha) / mLineCount
        for (i in mColors.indices) {
            mColors[i] =
                Color.argb(alpha - alphaGradient * i, red, green, blue)
        }
        mPaint!!.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(
            width - paddingLeft - paddingRight,
            height - paddingTop - paddingBottom
        ) * 0.5f
        if (mStrokeWidth == 0f) {
            mStrokeWidth = pointX(mAngleGradient / 2, radius / 2) / 2
        }
        mPaint!!.strokeWidth = mStrokeWidth
        for (i in mColors.indices) {
            mPaint!!.color = mColors[i]
            canvas.drawLine(
                centerX + pointX(-mAngleGradient * i + mStartAngle, radius / 2),
                centerY + pointY(
                    -mAngleGradient * i + mStartAngle,
                    radius / 2
                ),  //这里计算Y值时, 之所以减去线宽/2, 是防止没有设置的Padding时,图像会超出View范围
                centerX + pointX(
                    -mAngleGradient * i + mStartAngle,
                    radius - mStrokeWidth / 2
                ),  //这里计算Y值时, 之所以减去线宽/2, 是防止没有设置的Padding时,图像会超出View范围
                centerY + pointY(-mAngleGradient * i + mStartAngle, radius - mStrokeWidth / 2),
                mPaint!!
            )
        }
    }

    private fun pointX(angle: Int, radius: Float): Float {
        return (radius * Math.cos(angle * Math.PI / 180)).toFloat()
    }

    private fun pointY(angle: Int, radius: Float): Float {
        return (radius * Math.sin(angle * Math.PI / 180)).toFloat()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isAutoStart) {
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mAnimHandler != null) {
            stop()
        }
    }

    /**
     * 开始动画
     */
    fun start() {
        mAnimHandler!!.post(mAnimRunnable)
    }

    /**
     * 停止动画
     */
    fun stop() {
        mAnimHandler!!.removeCallbacks(mAnimRunnable)
    }

    /**
     * 设置菊花颜色
     */
    fun setColor(color: Int) {
        mColor = color
    }

    /**
     * 设置线宽
     */
    fun setStrokeWidth(strokeWidth: Float) {
        mStrokeWidth = strokeWidth
    }

    /**
     * 设置开始的角度
     */
    fun setStartAngle(startAngle: Int) {
        mStartAngle = startAngle
    }

    init {
        setup(context, attrs, defStyleAttr, 0)
    }
}