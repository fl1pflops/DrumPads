package com.example.drumpads.presentation.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.drumpads.R
import com.google.android.material.color.MaterialColors
import kotlin.math.sqrt

class PadButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.Pad
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var fadeInDuration: Long = 0
    private var fadeOutDuration: Long = 0
    private var pressedColor: Int = 0
    private var outerRadius: Float = 0f
    private val bounds: RectF = RectF()

    private val animator by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(this, PRESSURE, 0f)
    }

    private val pressedPaint: Paint by lazy(LazyThreadSafetyMode.NONE) {
        Paint().apply {
            color = pressedColor
            style = Paint.Style.FILL
        }
    }

    private var pressure: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.PadButton, defStyleAttr, defStyleRes) {
            val defaultPressedColor = MaterialColors.getColor(this@PadButton, R.attr.colorPrimary)

            pressedColor = getColor(R.styleable.PadButton_pressedColor, defaultPressedColor)
            fadeInDuration = getInt(R.styleable.PadButton_fadeInDuration, FADE_IN_ANIMATION_DURATION).toLong()
            fadeOutDuration = getInt(R.styleable.PadButton_fadeOutDuration, FADE_OUT_ANIMATION_DURATION).toLong()
        }

        clipToOutline = true
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        pressure = 0f
        super.onWindowFocusChanged(hasWindowFocus)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        outerRadius = (sqrt((width * width + height * height).toDouble()) / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), pressure, pressedPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action ?: return super.onTouchEvent(event)

        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                animatePressure(outerRadius, fadeInDuration)
                true
            }
            MotionEvent.ACTION_UP -> {
                animatePressure(0f, fadeOutDuration)
                true
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    private fun animatePressure(newPressure: Float, duration: Long) {
        animator.takeIf { it.isRunning }?.cancel()

        animator.duration = duration
        animator.setFloatValues(pressure, newPressure)
        animator.start()
    }


    companion object {
        private const val PRESSURE = "pressure"
        private const val FADE_IN_ANIMATION_DURATION = 0
        private const val FADE_OUT_ANIMATION_DURATION = 200
    }

}
