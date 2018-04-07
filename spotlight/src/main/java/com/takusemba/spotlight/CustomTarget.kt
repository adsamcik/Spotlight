package com.takusemba.spotlight

import android.app.Activity
import android.graphics.PointF
import android.support.annotation.LayoutRes
import android.view.View
import com.takusemba.spotlight.shapes.Shape

/**
 * Target
 *
 * @author takusemba
 * @since 26/06/2017
 */
class CustomTarget private constructor(override val shape: Shape, override val view: View, override val listener: OnTargetStateChangedListener<*>) : Target {
    private var actionListener: (() -> Unit)? = null

    override val point: PointF
        get() = shape.getPoint()


    internal fun setOnTargetActionListener(listener: () -> Unit) {
        this.actionListener = listener
    }

    fun closeTarget() {
        actionListener?.invoke()
    }

    /**
     * Builder class which makes it easier to create [CustomTarget]
     */
    class Builder(context: Activity) : AbstractBuilder<Builder, CustomTarget>(context) {
        private var viewId: Int? = null

        override fun self(): Builder {
            return this
        }

        /**
         * Set the custom view shown on Spotlight
         *
         * @param layoutId layout id shown on Spotlight
         * @return This Builder
         */
        fun setView(@LayoutRes layoutId: Int): Builder {
            this.view =
                    return this
        }

        /**
         * Set the custom view shown on Spotlight
         *
         * @param view view shown on Spotlight
         * @return This Builder
         */
        fun setView(view: View): Builder {
            this.view = view
            return this
        }

        /**
         * Create the [CustomTarget]
         *
         * @return the created CustomTarget
         */
        public override fun build(spotlight: Spotlight): CustomTarget {
            val view = context.layoutInflater.inflate(viewId!!, spotlight.)
            return CustomTarget(shape, view!!, listener)
        }
    }
}
