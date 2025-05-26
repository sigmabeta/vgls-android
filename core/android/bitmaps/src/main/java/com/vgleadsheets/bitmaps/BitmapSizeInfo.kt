package com.vgleadsheets.bitmaps

data class BitmapSizeInfo(
    val docWidth: Int,
    val pageWidth: Int,
    val pageHeight: Int,
    val pageToMaximumScalingFactor: Float,
    val zoomedScalingFactor: Float,
    val zoomedPageWidth: Int,
    val zoomedPageHeight: Int,
)
