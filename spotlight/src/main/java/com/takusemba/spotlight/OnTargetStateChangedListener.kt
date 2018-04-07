package com.takusemba.spotlight

/**
 * On Target State Changed Listener
 *
 * @author takusemba
 * @since 13/07/2017
 */
interface OnTargetStateChangedListener<T : Target> {
    /**
     * Called when Target is started
     */
    fun onStarted(target: T)

    /**
     * Called when Target is started
     */
    fun onEnded(target: T)
}
