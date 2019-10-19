package com.takusemba.spotlight

import android.content.res.Resources

internal val Int.dp get() = (this * Resources.getSystem().displayMetrics.density).toInt()
