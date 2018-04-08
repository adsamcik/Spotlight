package com.takusemba.spotlight

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.takusemba.spotlight.shapes.Shape

typealias ButtonClickListener = (view: View, spotlight: Spotlight) -> Unit

/**
 * Position Target
 *
 * @author takusemba
 * @since 26/06/2017
 */
class SimpleTarget private constructor(override val shape: Shape,
                                       private val title: CharSequence,
                                       private val description: CharSequence,
                                       private val buttonData: List<ButtonData>,
                                       override var listener: OnTargetStateChangedListener?) : Target {

    private var view: View? = null

    override fun getView(): View? = view

    override fun createView(layoutInflater: LayoutInflater, rootView: ViewGroup, spotlight: Spotlight) {
        val viewGroup = layoutInflater.inflate(R.layout.layout_simple, rootView, false) as ViewGroup
        viewGroup.findViewById<TextView>(R.id.title).text = title
        viewGroup.findViewById<TextView>(R.id.description).text = description

        buttonData.forEach { buttonData ->
            //When attachToRoot is true it returns viewGroup anyway
            //AttachToRoot is true to avoid temp layoutParams
            layoutInflater.inflate(R.layout.layout_button, viewGroup, true)
            val button = viewGroup.getChildAt(viewGroup.childCount - 1) as Button
            button.text = buttonData.text
            button.setOnClickListener { buttonData.listener.invoke(it, spotlight) }
        }

        calculatePosition(shape.getPoint(), viewGroup.pivotY, viewGroup)
        this.view = viewGroup
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
        val dp64 = 64.dpAsPx
        layout.setPadding(dp64, 0, dp64, 0)
        when (largest) {
            ABOVE_SPOTLIGHT -> spotlightView.viewTreeObserver
                    .addOnGlobalLayoutListener { layout.y = point.y - radius - dp64 - layout.height.toFloat() }
            BELOW_SPOTLIGHT -> layout.y = (point.y + radius + dp64).toInt().toFloat()
        }
    }

    companion object {

        private const val ABOVE_SPOTLIGHT = 0
        private const val BELOW_SPOTLIGHT = 1
    }

    data class ButtonData(val text: String, val listener: ButtonClickListener)

    /**
     * Builder class which makes it easier to create [SimpleTarget]
     */
    class Builder(context: Activity) : AbstractBuilder<Builder, SimpleTarget>(context) {
        private var title: CharSequence? = null
        private var description: CharSequence? = null
        private var buttonData: ArrayList<ButtonData> = ArrayList()

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

        fun addButtonData(buttonData: ButtonData): Builder {
            this.buttonData.add(buttonData)
            return this
        }

        /**
         * Create the [SimpleTarget]
         *
         * @return the created SimpleTarget
         */
        public override fun build(): SimpleTarget {
            return SimpleTarget(shape, title!!, description!!, buttonData, listener)
        }
    }
}