package com.takusemba.spotlight.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.view.View

abstract class Shape {
	protected var mPoint = PointF(0f, 0f)

	abstract val bounds: RectF

	abstract fun draw(canvas: Canvas, animValue: Float, paint: Paint)

	/**
	 * Sets point to the center of the [view].
	 * Shapes are centered around this point.
	 */
	protected fun setPoint(view: View) {
		val location = IntArray(2)
		view.getLocationInWindow(location)
		val x = location[0] + view.width / 2
		val y = location[1] + view.height / 2
		setPoint(x.toFloat(), y.toFloat())
	}

	/**
	 * Sets point.
	 * Shapes are centered around this point.
	 */
	fun setPoint(point: PointF) {
		this.mPoint = point
	}

	/**
	 * Sets point.
	 * Shapes are centered around this point.
	 */
	@Suppress("WeakerAccess")
	fun setPoint(x: Float, y: Float) {
		this.mPoint = PointF(x, y)
	}

	/**
	 * Returns point.
	 * Shapes are centered around this point.
	 */
	fun getPoint(): PointF {
		return mPoint
	}
}
