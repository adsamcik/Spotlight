package com.takusemba.spotlight

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import java.lang.ref.WeakReference
import java.util.*

typealias BaseListener = () -> Unit

/**
 * Spotlight
 *
 * @author takusemba
 * @since 26/06/2017
 */
class Spotlight private constructor(activity: Activity) {
    private var targets: ArrayList<out Target>? = null
    private var duration = DEFAULT_DURATION
    private var animation: TimeInterpolator = DEFAULT_ANIMATION
    private var overlayColor = DEFAULT_OVERLAY_COLOR
    private var isClosedOnTouchedOutside = true

    var onStartedListener: BaseListener? = null
    var onEndedListener: BaseListener? = null

    init {
        contextWeakReference = WeakReference(activity)
    }

    /**
     * sets [Target]s to Spotlight
     *
     * @param targets targets to show
     * @return the Spotlight
     */
    fun <T : Target> setTargets(vararg targets: T): Spotlight {
        this.targets = ArrayList(Arrays.asList(*targets))
        for (target in targets) {
            if (target is CustomTarget) {
                target.setOnTargetActionListener { finishTarget() }
            }
        }
        return this
    }

    /**
     * sets spotlight background color to Spotlight
     *
     * @param overlayColor background color to be used for the spotlight overlay
     * @return the Spotlight
     */
    fun setOverlayColor(@ColorInt overlayColor: Int): Spotlight {
        this.overlayColor = overlayColor
        return this
    }

    /**
     * sets duration to [Target] Animation
     *
     * @param duration duration of Target Animation
     * @return the Spotlight
     */
    fun setDuration(duration: Long): Spotlight {
        this.duration = duration
        return this
    }

    /**
     * sets duration to [Target] Animation
     *
     * @param animation type of Target Animation
     * @return the Spotlight
     */
    fun setAnimation(animation: TimeInterpolator): Spotlight {
        this.animation = animation
        return this
    }

    /**
     * Sets Spotlight start Listener to Spotlight
     *
     * @param listener OnSpotlightStartedListener of Spotlight
     * @return This Spotlight
     */
    fun setOnSpotlightStartedListener(
            listener: BaseListener?): Spotlight {
        onStartedListener = listener
        return this
    }

    /**
     * Sets Spotlight end Listener to Spotlight
     *
     * @param listener OnSpotlightEndedListener of Spotlight
     * @return This Spotlight
     */
    fun setOnSpotlightEndedListener(listener: BaseListener?): Spotlight {
        onEndedListener = listener
        return this
    }

    /**
     * Sets if Spotlight closes Target if touched outside
     *
     * @param isClosedOnTouchedOutside OnSpotlightEndedListener of Spotlight
     * @return This Spotlight
     */
    fun setClosedOnTouchedOutside(isClosedOnTouchedOutside: Boolean): Spotlight {
        this.isClosedOnTouchedOutside = isClosedOnTouchedOutside
        return this
    }

    /**
     * Shows [SpotlightView]
     */
    fun start() {
        spotlightView()
    }

    /**
     * Creates the spotlight view and starts
     */
    private fun spotlightView() {
        if (context == null) {
            throw RuntimeException("context is null")
        }
        val decorView = (context as Activity).window.decorView
        val spotlightView = SpotlightView(context!!)
        spotlightViewWeakReference = WeakReference(spotlightView)
        spotlightView.setOverlayColor(overlayColor)
        spotlightView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (decorView as ViewGroup).addView(spotlightView)
        spotlightView.setOnSpotlightStateChangedListener(object : SpotlightView.OnSpotlightStateChangedListener {
            override fun onTargetClosed() {
                if (!targets!!.isEmpty()) {
                    val target = targets!!.removeAt(0)
                    if (target.listener != null) target.listener.onEnded(target)
                    if (targets!!.size > 0) {
                        startTarget()
                    } else {
                        finishSpotlight()
                    }
                }
            }

            override fun onTargetClicked() {
                if (isClosedOnTouchedOutside) {
                    finishTarget()
                }
            }
        })
        startSpotlight()
    }

    /**
     * show Target
     */
    private fun startTarget() {
        if (targets != null && targets!!.size > 0 && spotlightView != null) {
            val target = targets!![0]
            val spotlightView = spotlightView

            spotlightView!!.removeAllViews()
            spotlightView.addView(target.view)
            spotlightView.setShape(target.shape)
            spotlightView.turnUp(target.point.x, target.point.y,
                    duration, animation)
            if (target.listener != null)
                target.listener =target
        }
    }

    /**
     * show Spotlight
     */
    private fun startSpotlight() {
        if (spotlightView == null) return
        val objectAnimator = ObjectAnimator.ofFloat(spotlightView, "alpha", 0f, 1f)
        objectAnimator.duration = START_SPOTLIGHT_DURATION
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                if (startedListener != null) startedListener!!.onStarted()
            }

            override fun onAnimationEnd(animation: Animator) {
                startTarget()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        objectAnimator.start()
    }

    /**
     * hide Target
     */
    private fun finishTarget() {
        if (targets != null && targets!!.size > 0 && spotlightView != null) {
            spotlightView!!.turnDown(duration, animation)
        }
    }

    /**
     * hide Spotlight
     */
    private fun finishSpotlight() {
        if (spotlightView == null) return
        val objectAnimator = ObjectAnimator.ofFloat(spotlightView, "alpha", 1f, 0f)
        objectAnimator.duration = FINISH_SPOTLIGHT_DURATION
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                val decorView = (context as Activity).window.decorView
                (decorView as ViewGroup).removeView(spotlightView)
                if (endedListener != null) endedListener!!.onEnded()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        objectAnimator.start()
    }

    companion object {
        /**
         * Duration of Spotlight emerging
         */
        private val START_SPOTLIGHT_DURATION = 500L
        /**
         * Duration of Spotlight disappearing
         */
        private val FINISH_SPOTLIGHT_DURATION = 500L
        /**
         * Default of Spotlight overlay color
         */
        @ColorInt
        private val DEFAULT_OVERLAY_COLOR = Color.parseColor("#E6000000")

        private val DEFAULT_DURATION = 1000L
        private val DEFAULT_ANIMATION = DecelerateInterpolator(2f)

        private var spotlightViewWeakReference: WeakReference<SpotlightView>? = null
        private var contextWeakReference: WeakReference<Activity>

        /**
         * Create Spotlight with activity reference
         *
         * @param activity Activity to create Spotlight
         * @return This Spotlight
         */
        fun with(activity: Activity): Spotlight {
            return Spotlight(activity)
        }

        /**
         * Return context weak reference
         *
         * @return the activity
         */
        private val context: Context?
            get() = contextWeakReference.get()

        /**
         * Returns [SpotlightView] weak reference
         *
         * @return the SpotlightView
         */
        private val spotlightView: SpotlightView?
            get() = spotlightViewWeakReference!!.get()
    }
}
