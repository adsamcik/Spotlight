package com.takusemba.spotlight

import android.content.res.Resources

internal val Int.dpAsPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()