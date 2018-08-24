package com.kanch786.musicapp.extensions

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup


fun View.getHeightInDp(displayMetrics: DisplayMetrics) : Float{

    measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    return measuredHeight/displayMetrics.density
}

