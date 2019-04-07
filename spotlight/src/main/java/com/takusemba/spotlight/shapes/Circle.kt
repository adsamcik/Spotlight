package com.takusemba.spotlight.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.View

class Circle : Shape {
    private var mRadius: Float = 0.toFloat()

    @JvmOverloads constructor(view: View, radius: Float = (Math.sqrt(Math.pow(view.width.toDouble(), 2.0) + Math.pow(view.height.toDouble(), 2.0)) / 2.0).toFloat()) {
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
