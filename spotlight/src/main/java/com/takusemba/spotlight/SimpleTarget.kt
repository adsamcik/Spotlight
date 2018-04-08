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
                                       val title: CharSequence,
                                       val description: CharSequence,
                                       val buttonData: ButtonData?,
                                       override var listener: OnTargetStateChangedListener?) : Target {

    private var view: View? = null

    override fun getView(): View? = view

    override fun createView(layoutInflater: LayoutInflater, rootView: ViewGroup, spotlight: Spotlight) {
        val view = layoutInflater.inflate(R.layout.layout_simple, rootView, false)
        view.findViewById<TextView>(R.id.title).text = title
        view.findViewById<TextView>(R.id.description).text = description

        val button = view.findViewById<Button>(R.id.button)
        if(buttonData == null)
            button.visibility = View.GONE
        else {
            button.visibility = View.VISIBLE
            button.text = buttonData.text
            button.setOnClickListener { buttonData.listener.invoke(it, spotlight) }
        }

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

    data class ButtonData(val text: String, val listener: ButtonClickListener)

    /**
     * Builder class which makes it easier to create [SimpleTarget]
     */
    class Builder(context: Activity) : AbstractBuilder<Builder, SimpleTarget>(context) {
        private var title: CharSequence? = null
        private var description: CharSequence? = null
        private var buttonData: ButtonData? = null

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

        fun setButtonData(buttonData: ButtonData): Builder {
            this.buttonData = buttonData
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