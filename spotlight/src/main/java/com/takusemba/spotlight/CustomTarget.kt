package com.takusemba.spotlight

import android.app.Activity
import android.graphics.PointF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.takusemba.spotlight.shapes.Shape

/**
 * Target
 *
 * @author takusemba
 * @since 26/06/2017
 */
class CustomTarget private constructor(
		override var shape: Shape,
		private val mView: View,
		override var listener: OnTargetStateChangedListener?
) : Target {
	override fun createView(
			layoutInflater: LayoutInflater,
			rootView: ViewGroup,
			spotlight: Spotlight
	) {
		if (mView.parent != null)
			(mView.parent as ViewGroup).removeView(mView)
	}

	override fun getView(): View? = mView

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
		private var view: View? = null

		override fun self(): Builder {
			return this
		}

		/**
		 * Set the custom view shown on Spotlight
		 *
		 * @param layoutId layout id shown on Spotlight
		 * @param root ViewGroup required to properly create this view
		 * @return This Builder
		 */
		fun setView(@LayoutRes layoutId: Int, root: ViewGroup): Builder {
			this.view = activity.layoutInflater.inflate(layoutId, root, false)
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
		public override fun build(): CustomTarget {
			return CustomTarget(shape, view!!, listener)
		}
	}
}
