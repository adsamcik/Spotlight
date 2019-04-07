package com.takusemba.spotlight.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.View

abstract class Shape {
    protected var mPoint = PointF(0f, 0f)

    abstract fun draw(canvas: Canvas, animValue: Float, paint: Paint)

    protected fun setPoint(view: View) {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val x = location[0] + view.width / 2
        val y = location[1] + view.height / 2
        setPoint(x.toFloat(), y.toFloat())
    }

    fun setPoint(point: PointF) {
        this.mPoint = point
    }

    fun setPoint(x: Float, y: Float) {
        this.mPoint = PointF(x, y)
    }

    fun getPoint(): PointF {
        return mPoint
    }
}
