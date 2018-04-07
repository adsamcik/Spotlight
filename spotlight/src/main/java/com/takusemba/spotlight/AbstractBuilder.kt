package com.takusemba.spotlight

import android.app.Activity
import android.graphics.PointF
import android.view.View

import com.takusemba.spotlight.shapes.Circle
import com.takusemba.spotlight.shapes.Shape

import java.lang.ref.WeakReference

/**
 * Position Target
 *
 * @author takusemba
 * @since 26/06/2017
 */
abstract class AbstractBuilder<T : AbstractBuilder<T, S>, S : Target>
/**
 * Constructor
 */
protected constructor(context: Activity) {

    private val contextWeakReference: WeakReference<Activity> = WeakReference(context)

    protected var listener: OnTargetStateChangedListener<*>? = null
    protected var shape: Shape = Circle(PointF(0f, 0f), 100f)

    /**
     * Return context weak reference
     *
     * @return the activity
     */
    protected val context: Activity
        get() = contextWeakReference.get()!!

    /**
     * return the builder itself
     */
    protected abstract fun self(): T

    /**
     * return the built [Target]
     */
    protected abstract fun build(spotlight: Spotlight): S

    /**
     * Sets the initial position of target
     *
     * @param y starting position of y where spotlight reveals
     * @param x starting position of x where spotlight reveals
     * @return This Builder
     */
    fun setPoint(x: Float, y: Float): T {
        return setPoint(PointF(x, y))
    }

    /**
     * Sets the initial position of target
     *
     * @param point starting position where spotlight reveals
     * @return This Builder
     */
    fun setPoint(point: PointF): T {
        shape.setPoint(point)
        return self()
    }

    /**
     * Sets the initial position of target
     * Make sure the view already has a fixed position
     *
     * @param view starting position where spotlight reveals
     * @return This Builder
     */
    fun setPoint(view: View): T {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val x = location[0] + view.width / 2
        val y = location[1] + view.height / 2
        return setPoint(x.toFloat(), y.toFloat())
    }

    /**
     * Sets shape of the target
     *
     * @param shape shape of the target
     * @return This Builder
     */
    fun setShape(shape: Shape?): T {
        if (shape == null)
            throw IllegalArgumentException("Shape cannot be null")
        this.shape = shape
        return self()
    }

    /**
     * Sets Target state changed Listener to target
     *
     * @param listener OnTargetStateChangedListener of target
     * @return This Builder
     */
    fun setOnSpotlightStartedListener(listener: OnTargetStateChangedListener<S>): T {
        this.listener = listener
        return self()
    }
}
