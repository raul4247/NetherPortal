package com.raulfm.netherportal.extensions

import android.widget.Button
import androidx.core.content.ContextCompat
import com.raulfm.netherportal.R

fun Button.setState(state: Boolean) {
    val enabledBackground = ContextCompat.getDrawable(context, R.drawable.button_primary_background)
    val disabledBackground =
        ContextCompat.getDrawable(context, R.drawable.button_disabled_background)

    isEnabled = state
    background = if (state) enabledBackground else disabledBackground
}