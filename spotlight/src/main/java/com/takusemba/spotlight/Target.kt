package com.takusemba.spotlight

import android.graphics.PointF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.takusemba.spotlight.shapes.Shape

/**
 * Target
 *
 * @author takusemba
 * @since 26/06/2017
 */
interface Target {

    /**
     * gets the point of this Target
     *
     * @return the point of this Target
     */
    val point: PointF

    /**
     * gets the view of this Target
     *
     * @return the view of this Target or null if createView was not yet called
     */
    fun getView(): View?

    /**
     * gets shape of this Target
     *
     * @return shape of this Target
     */
    val shape: Shape

    /**
     * gets the listener of this Target
     *
     * @return the listener of this Target
     */
    var listener: OnTargetStateChangedListener?

    /**
     * Creates view on demand
     */
    fun createView(layoutInflater: LayoutInflater, rootView: ViewGroup, spotlight: Spotlight)
}