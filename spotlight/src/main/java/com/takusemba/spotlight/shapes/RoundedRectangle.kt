package com.takusemba.spotlight.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import androidx.annotation.RequiresApi
import android.view.View

@RequiresApi(21)
class RoundedRectangle : Shape {
    private var mHalfWidth: Float = 0.toFloat()
    private var mHalfHeight: Float = 0.toFloat()
    private var mRadius: Float = 0.toFloat()

    constructor(view: View, radius: Float) : this(view, 0f, radius)

    constructor(view: View, offset: Float, radius: Float) {
        setPoint(view)
        this.mHalfHeight = view.height / 2 + offset
        this.mHalfWidth = view.width / 2 + offset
        this.mRadius = radius
    }

    constructor(pointF: PointF, width: Float, height: Float, radius: Float) {
        this.mPoint = pointF
        this.mHalfWidth = width / 2
        this.mHalfHeight = height / 2
        this.mRadius = radius
    }

    override fun draw(canvas: Canvas, animValue: Float, paint: Paint) {
        val mAnimWidth = mHalfWidth * animValue
        val mAnimHeight = mHalfHeight * animValue

        canvas.drawRoundRect(mPoint.x - mAnimWidth,
                mPoint.y - mAnimHeight,
                mPoint.x + mAnimWidth,
                mPoint.y + mAnimHeight, mRadius,
                mRadius,
                paint)
    }
}
