package com.takusemba.spotlight

/**
 * On Target State Changed Listener
 *
 * @author takusemba
 * @since 13/07/2017
 */
interface OnTargetStateChangedListener {
	/**
	 * Called when Target is started
	 */
	fun onStarted(target: Target)

	/**
	 * Called when Target is started
	 */
	fun onEnded(target: Target)
}
