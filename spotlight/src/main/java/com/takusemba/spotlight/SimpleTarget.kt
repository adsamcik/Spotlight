package com.takusemba.spotlight

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.takusemba.spotlight.shapes.Shape

/**
 * Position Target
 *
 * @author takusemba
 * @since 26/06/2017
 */
class SimpleTarget
/**
 * Constructor
 */
private constructor(override val shape: Shape,
                    val title: CharSequence,
                    val description: CharSequence,
                    override var listener: OnTargetStateChangedListener?) : Target {

    private var view: View? = null

    override fun getView(): View? = view

    override fun createView(layoutInflater: LayoutInflater, rootView: ViewGroup, spotlight: Spotlight) {
        val view = layoutInflater.inflate(R.layout.layout_spotlight, rootView, false)
        (view.findViewById<View>(R.id.title) as TextView).text = title
        (view.findViewById<View>(R.id.description) as TextView).text = description
        calculatePosition(shape.getPoint(), view.pivotY, view)
        this.view = view
    }

    override val point: PointF
        get() = shape.getPoint()

    /**
     * calculate the position of title and description based off of where the spotlight reveals
     */
    private fun calculatePosition(point: PointF, radius: Float, spotlightView: View) {
        val areas = FloatArray(2)
        val screenSize = Point()
        (spotlightView.context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(screenSize)

        areas[ABOVE_SPOTLIGHT] = point.y / screenSize.y
        areas[BELOW_SPOTLIGHT] = (screenSize.y - point.y) / screenSize.y

        val largest: Int
        largest = if (areas[ABOVE_SPOTLIGHT] > areas[BELOW_SPOTLIGHT]) {
            ABOVE_SPOTLIGHT
        } else {
            BELOW_SPOTLIGHT
        }

        val layout = spotlightView.findViewById<LinearLayout>(R.id.container)
        layout.setPadding(100, 0, 100, 0)
        when (largest) {
            ABOVE_SPOTLIGHT -> spotlightView.viewTreeObserver
                    .addOnGlobalLayoutListener { layout.y = point.y - radius - 100f - layout.height.toFloat() }
            BELOW_SPOTLIGHT -> layout.y = (point.y + radius + 100f).toInt().toFloat()
        }
    }

    companion object {

        private const val ABOVE_SPOTLIGHT = 0
        private const val BELOW_SPOTLIGHT = 1
    }

    /**
     * Builder class which makes it easier to create [SimpleTarget]
     */
    class Builder(context: Activity) : AbstractBuilder<Builder, SimpleTarget>(context) {
        private var title: CharSequence? = null
        private var description: CharSequence? = null

        override fun self(): Builder {
            return this
        }

        /**
         * Set the title text shown on Spotlight
         *
         * @param title title shown on Spotlight
         * @return This Builder
         */
        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        /**
         * Set the description text shown on Spotlight
         *
         * @param description title shown on Spotlight
         * @return This Builder
         */
        fun setDescription(description: CharSequence): Builder {
            this.description = description
            return this
        }

        /**
         * Create the [SimpleTarget]
         *
         * @return the created SimpleTarget
         */
        public override fun build(): SimpleTarget {
            return SimpleTarget(shape, title!!, description!!, listener)
        }
    }
}