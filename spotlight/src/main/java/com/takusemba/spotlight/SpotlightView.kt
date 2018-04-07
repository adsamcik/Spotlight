package com.takusemba.spotlight

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

import com.takusemba.spotlight.shapes.Shape

/**
 * Spotlight View
 *
 * @author takusemba
 * @since 26/06/2017
 */
internal class SpotlightView : FrameLayout {

    private val paint = Paint()
    private val spotPaint = Paint()
    private val point = PointF()
    private var animator: ValueAnimator? = null
    private var listener: OnSpotlightStateChangedListener? = null
    private var overlayColor: Int = 0
    private var shape: Shape? = null


    constructor(context: Context) : super(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    /**
     * sets listener to [SpotlightView]
     */
    fun setOnSpotlightStateChangedListener(l: OnSpotlightStateChangedListener) {
        this.listener = l
    }

    /**
     * sets the spotlight color
     *
     * @param overlayColor the color that will be used for the spotlight overlay
     */
    fun setOverlayColor(@ColorInt overlayColor: Int) {
        this.overlayColor = overlayColor
    }

    /**
     * sets the shape
     *
     * @param shape shape that will be used to draw
     */
    fun setShape(shape: Shape) {
        this.shape = shape
    }

    /**
     * prepares to show this Spotlight
     */
    private fun init() {
        bringToFront()
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        spotPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        setOnClickListener {
            if (animator != null && !animator!!.isRunning && animator!!.animatedValue as Float > 0) {
                if (listener != null) listener!!.onTargetClicked()
            }
        }
    }

    /**
     * draws black background and trims a circle
     *
     * @param canvas the canvas on which the background will be drawn
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = overlayColor
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
        if (animator != null) {
            shape!!.draw(canvas, animator!!.animatedValue as Float, spotPaint)
        }
    }

    /**
     * starts an animation to show a circle
     *
     * @param x         initial position x where the shape is showing up
     * @param y         initial position y where the shape is showing up
     * @param duration  duration of the animation
     * @param animation type of the animation
     */
    fun turnUp(x: Float, y: Float, duration: Long, animation: TimeInterpolator) {
        this.point.set(x, y)
        animator = ValueAnimator.ofFloat(0f, 1f)
        animator!!.addUpdateListener { this@SpotlightView.invalidate() }
        animator!!.interpolator = animation
        animator!!.duration = duration
        animator!!.start()
    }

    /**
     * starts an animation to close the shape
     *
     * @param duration  duration of the animation
     * @param animation type of the animation
     */
    fun turnDown(duration: Long, animation: TimeInterpolator) {
        animator = ValueAnimator.ofFloat(1f, 0f)
        animator!!.addUpdateListener { this@SpotlightView.invalidate() }
        animator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                if (listener != null) listener!!.onTargetClosed()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        animator!!.interpolator = animation
        animator!!.duration = duration
        animator!!.start()
    }

    /**
     * Listener to control Target state
     */
    internal interface OnSpotlightStateChangedListener {
        /**
         * Called when Target closed completely
         */
        fun onTargetClosed()

        /**
         * Called when Target is Clicked
         */
        fun onTargetClicked()
    }
}
