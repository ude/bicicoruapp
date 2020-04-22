package com.udepardo.bicicoru.bicicorucommon

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView


/**
 * Created by fernando.ude on 22/02/2018.
 */

class OtterGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var totalbikes = 20
    private var freebikes = 16
    private var isOnline = true

    private lateinit var tv: TextView

    private val myCanvas: RectF = RectF()
    private var canvasHeight = 0
    private var canvasWidth = 0
    private var size = 0
    private var circleRadius = 0f
    private var calculatedStrokeWidth = 0f
    var textSize = 0f

    private lateinit var fullPaint: Paint
    private lateinit var emptyPaint: Paint
    private lateinit var backgroundPaint: Paint


    init {
        initView(context, attrs)
    }


    private fun initView(context: Context, attrs: AttributeSet?) {
        setWillNotDraw(false)
        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt()
        setPadding(padding, padding, padding, padding)

        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(
                attrs, R.styleable.OtterGraph, 0, 0
            )

            try {
                calculatedStrokeWidth = a.getDimensionPixelSize(R.styleable.OtterGraph_strokeWidth, 13).toFloat()
                textSize = a.getDimensionPixelSize(R.styleable.OtterGraph_textSize, 0).toFloat()
            } finally {
                a.recycle()
            }
        }

        fullPaint = Paint().apply {
            color = Color.RED
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = calculatedStrokeWidth
            alpha = 158
            strokeJoin = Paint.Join.BEVEL
            strokeCap = Paint.Cap.BUTT
        }

        emptyPaint = Paint().apply {
            color = Color.DKGRAY
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = calculatedStrokeWidth
            alpha = 158
            strokeJoin = Paint.Join.BEVEL
            strokeCap = Paint.Cap.BUTT
        }

        backgroundPaint = Paint().apply {
            color = Color.DKGRAY
            isAntiAlias = true
            style = Paint.Style.FILL
            alpha = 56
        }


        tv = TextView(context)
        tv.text = "10"
        tv.setTypeface(null, Typeface.BOLD_ITALIC)
        tv.gravity = Gravity.CENTER
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)


        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.CENTER
        tv.layoutParams = params
        addView(tv)


    }

    fun setSlots(free: Int, total: Int, isOnline: Boolean) {
        freebikes = free
        totalbikes = total
        this.isOnline = isOnline
        if (isOnline) {
            tv.text = free.toString()
            fullPaint.alpha = 158
            emptyPaint.alpha = 158
            backgroundPaint.alpha = 56
        } else {
            fullPaint.alpha = 80
            emptyPaint.alpha = 80
            backgroundPaint.alpha = 32
            tv.text = "-"
        }

        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        canvasHeight = measuredHeight
        canvasWidth = measuredWidth

        size = Math.max(canvasWidth, canvasHeight)

        val outline = calculatedStrokeWidth * 2
        circleRadius = (size - (outline * 2 + calculatedStrokeWidth)) / 2


        setMeasuredDimension(size, size)
        myCanvas.set(outline, outline, size - outline, size - outline);
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(size / 2f, size / 2f, circleRadius, backgroundPaint)

        fullPaint.color = getFillColor()
        fullPaint.alpha = if (isOnline) 158 else 80

        if (isOnline) {
            canvas?.drawArc(myCanvas, -90f, getSweepAngle(), false, fullPaint)
            canvas?.drawArc(myCanvas, getSweepAngle() - 90f, 360 - getSweepAngle(), false, emptyPaint)
        } else {
            canvas?.drawArc(myCanvas, 0f, 360f, false, emptyPaint)
        }

    }


    private fun getSweepAngle(): Float {
        return if (freebikes > 0) {
            360 * freebikes.toFloat() / totalbikes.toFloat()
        } else {
            360f
        }
    }

    private fun getFillColor()  = interpolateColor(0xFF0000, 0x00C000, freebikes.toFloat() / totalbikes.toFloat())
    
    private fun interpolateColor(a: Int, b: Int, proportion: Float): Int {
        val hsva = FloatArray(3)
        val hsvb = FloatArray(3)
        Color.colorToHSV(a, hsva)
        Color.colorToHSV(b, hsvb)
        for (i in 0..2) {
            hsvb[i] = interpolate(hsva[i], hsvb[i], proportion)
        }
        return Color.HSVToColor(hsvb)
    }

    private fun interpolate(a: Float, b: Float, proportion: Float): Float {
        return a + (b - a) * proportion
    }

}
