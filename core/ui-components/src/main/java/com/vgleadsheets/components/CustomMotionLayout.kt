package com.vgleadsheets.components

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout

class CustomMotionLayout@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): MotionLayout(context, attrs, defStyleAttr) {
    override fun isAttachedToWindow() = true
}
