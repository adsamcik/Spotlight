package com.takusemba.spotlight.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt

class Circle : Shape {
	private var mRadius: Float = 0.toFloat()

	override val bounds: RectF
		get() {
			val point = getPoint()
			return RectF(
					point.x - mRadius,
					point.y - mRadius,
					point.x + mRadius,
					point.y + mRadius
			)
		}

	@JvmOverloads
	constructor(
			view: View,
			radius: Float = (sqrt(view.width.toDouble().pow(2.0) + view.height.toDouble().pow(2.0)) / 2.0).toFloat()
	) {
		setPoint(view)
		this.mRadius = radius
	}

	constructor(point: PointF, radius: Float) {
		this.mPoint = point
		this.mRadius = radius
	}

	override fun draw(canvas: Canvas, animValue: Float, paint: Paint) {
		canvas.drawCircle(mPoint.x, mPoint.y, animValue * mRadius, paint)
	}
}
