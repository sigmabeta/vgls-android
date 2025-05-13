package com.vgleadsheets.bitmaps

data class BitmapSizeInfo(
    val width: Int,
    val height: Int,
    val pageToMaximumScalingFactor: Float,
    val zoomedScalingFactor: Float,
    val zoomedWidth: Int,
    val zoomedHeight: Int,
)
