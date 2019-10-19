package com.takusemba.spotlight

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.takusemba.spotlight.shapes.Shape
import java.util.concurrent.locks.ReentrantLock

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

	private var animator: ValueAnimator = ValueAnimator()
	//animator.isRunning is not reliable enough
	private var isAnimatorRunning = false

	private var listener: OnSpotlightStateChangedListener? = null

	private var overlayColor: Int = 0
	private var shape: Shape? = null
	private var shapeBounds: RectF = RectF()

	private var animatorLock: ReentrantLock = ReentrantLock()

	private var activeView: View? = null
	private var activeViewDrawRect: RectF = RectF()
	private var viewRectRadius: Float = 16.dp.toFloat()
	private var viewRectPorterDuff = PorterDuffXfermode(PorterDuff.Mode.SRC)


	constructor(context: Context) : super(context, null)
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
			context,
			attrs,
			defStyleAttr
	)

	init {
		bringToFront()
		setWillNotDraw(false)
		setLayerType(View.LAYER_TYPE_HARDWARE, null)
		spotPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
		setOnClickListener {
			if (!animator.isRunning && animator.animatedValue is Float && animator.animatedValue as Float > 0) {
				if (listener != null)
					listener!!.onTargetClicked()
			}
		}
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
		paint.color = overlayColor
	}

	/**
	 * sets the shape
	 *
	 * @param shape shape that will be used to draw
	 */
	fun setShape(shape: Shape) {
		this.shape = shape
		this.shapeBounds.set(shape.bounds)
	}

	fun setView(view: View) {
		activeView = view
		view.post {
			val rect = Rect()
			view.getGlobalVisibleRect(rect)
			activeViewDrawRect = RectF(rect)
		}
		//view.getDrawingRect(activeViewDrawRect)
		removeAllViews()
		addView(view)
	}

	/**
	 * draws black background and trims a circle
	 *
	 * @param canvas the canvas on which the background will be drawn
	 */
	@SuppressLint("CanvasSize")
	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
		shape?.draw(canvas, animator.animatedValue as Float, spotPaint)

		if (shapeBounds.intersect(activeViewDrawRect)) {
			paint.xfermode = viewRectPorterDuff
			canvas.drawRoundRect(activeViewDrawRect, viewRectRadius, viewRectRadius, paint)
			paint.xfermode = null
		}
	}

	/**
	 * starts an animation to show a circle
	 *
	 * @param x         initial position x where the shape is showing up
	 * @param y         initial position y where the shape is showing up
	 * @param duration  duration of the animation
	 * @param animation type of the animation¶
	 */
	fun turnUp(x: Float, y: Float, duration: Long, animation: TimeInterpolator) {
		animatorLock.lock()

		//wait for animation to finish because end on animator does not seem reliable
		if (isAnimatorRunning) {
			animatorLock.unlock()
			return
		}

		isAnimatorRunning = true

		this.point.set(x, y)
		val animator = ValueAnimator.ofFloat(0f, 1f)
		animator.addUpdateListener { this@SpotlightView.invalidate() }
		animator.interpolator = animation
		animator.duration = duration
		animator.addListener(object : Animator.AnimatorListener {
			override fun onAnimationStart(animation: Animator) = Unit

			override fun onAnimationEnd(animation: Animator) {
				isAnimatorRunning = false
			}

			override fun onAnimationCancel(animation: Animator) = Unit

			override fun onAnimationRepeat(animation: Animator) = Unit
		})
		animator.start()
		this.animator = animator

		animatorLock.unlock()
	}

	/**
	 * starts an animation to close the shape
	 *
	 * @param duration  duration of the animation
	 * @param animation type of the animation
	 */
	fun turnDown(duration: Long, animation: TimeInterpolator) {
		animatorLock.lock()

		//wait for animation to finish because end on animator does not seem reliable
		if (isAnimatorRunning) {
			animatorLock.unlock()
			return
		}

		isAnimatorRunning = true

		val animator = ValueAnimator.ofFloat(1f, 0f)
		animator.addUpdateListener { this@SpotlightView.invalidate() }
		animator.addListener(object : Animator.AnimatorListener {
			override fun onAnimationStart(animation: Animator) = Unit

			override fun onAnimationEnd(animation: Animator) {
				isAnimatorRunning = false
				listener?.onTargetClosed()
			}

			override fun onAnimationCancel(animation: Animator) = Unit

			override fun onAnimationRepeat(animation: Animator) = Unit
		})
		animator.interpolator = animation
		animator.duration = duration
		animator.start()
		this.animator = animator

		animatorLock.unlock()
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
