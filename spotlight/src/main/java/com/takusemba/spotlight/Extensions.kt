package com.takusemba.spotlight

import android.content.res.Resources

val Int.dpAsPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()