package com.vgleadsheets.bitmaps

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import com.vgleadsheets.logging.Hatchet
import kotlin.math.roundToInt

object BitmapUtils {
    fun computeBitmapSize(
        hatchet: Hatchet,
        maxWidth: Int,
        maxHeight: Int,
        docWidth: Int,
        docHeight: Int,
        zoom: Float,
    ): BitmapSizeInfo {
        val pageToMaximumScalingFactor = computePageToMaxScalingFactor(maxWidth, maxHeight, docWidth, docHeight)
        val zoomedScalingFactor = pageToMaximumScalingFactor * zoom

        val zoomedWidth = (docWidth * zoomedScalingFactor).roundToInt()
        val zoomedHeight = (docHeight * zoomedScalingFactor).roundToInt()

        val scalingType = computeScalingType(maxWidth, maxHeight, zoomedWidth, zoomedHeight)

        val bitmapWidth = computeBitmapWidth(scalingType, zoomedWidth, maxWidth)
        val bitmapHeight = computeBitmapHeight(scalingType, zoomedHeight, maxHeight)

        hatchet.v("PDF to max scaling factor: $pageToMaximumScalingFactor")
        hatchet.v("Zoomed     scaling factor: $zoomedScalingFactor")

        hatchet.v("Maximum      dimensions: $maxWidth x $maxHeight")
        hatchet.v("Specced page dimensions: $docWidth x $docHeight")

        hatchet.v("Zoomed  page dimensions: $zoomedWidth x $zoomedHeight")
        hatchet.v("Bitmap  page dimensions: $bitmapWidth x $bitmapHeight")
        hatchet.v("Scaling type: $scalingType")

        return BitmapSizeInfo(
            width = bitmapWidth,
            height = bitmapHeight,
            pageToMaximumScalingFactor = pageToMaximumScalingFactor,
            zoomedScalingFactor = zoomedScalingFactor,
            zoomedWidth = zoomedWidth,
            zoomedHeight = zoomedHeight
        )
    }

    fun createBlankBitmap(
        width: Int,
        height: Int,
    ): Bitmap {
        return createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
    }

    private fun computeScalingType(
        maxWidth: Int,
        maxHeight: Int,
        targetWidth: Int,
        targetHeight: Int,
    ): ScalingType {
        if (maxWidth == maxHeight) {
            if (targetHeight > targetWidth) {
                return ScalingType.FILL_HEIGHT_ADJUST_WIDTH
            }

            return ScalingType.FILL_WIDTH_ADJUST_HEIGHT
        }

        if (targetWidth > maxWidth) {
            if (targetHeight > maxHeight) {
                return ScalingType.MAX
            }
            return ScalingType.FILL_WIDTH_ADJUST_HEIGHT
        }


        if (targetHeight > maxHeight) {
            return ScalingType.FILL_HEIGHT_ADJUST_WIDTH
        }

        if (maxHeight > maxWidth) {
            return ScalingType.FILL_WIDTH_ADJUST_HEIGHT
        }

        return ScalingType.FILL_HEIGHT_ADJUST_WIDTH
    }
}

private fun computeBitmapWidth(
    scalingType: ScalingType,
    targetWidth: Int,
    maxWidth: Int
): Int {
    return when (scalingType) {
        ScalingType.FILL_HEIGHT_ADJUST_WIDTH -> targetWidth
        ScalingType.FILL_WIDTH_ADJUST_HEIGHT, ScalingType.MAX -> return maxWidth
    }
}

private fun computeBitmapHeight(
    scalingType: ScalingType,
    targetHeight: Int,
    maxHeight: Int
): Int {
    return when (scalingType) {
        ScalingType.FILL_WIDTH_ADJUST_HEIGHT -> targetHeight
        ScalingType.FILL_HEIGHT_ADJUST_WIDTH, ScalingType.MAX -> return maxHeight
    }
}

fun computePageToMaxScalingFactor(
    maxWidth: Int,
    maxHeight: Int,
    pdfWidth: Int,
    pdfHeight: Int
): Float {
    return if (maxWidth < maxHeight) {
        maxWidth / pdfWidth.toFloat()
    } else {
        maxHeight / pdfHeight.toFloat()
    }
}
