package com.vgleadsheets.pdf

import android.graphics.Bitmap
import android.graphics.Matrix

interface AsyncRenderer {
    fun getActualDimensions(): Pair<Int, Int>

    suspend fun renderToBitmap(
        width: Int,
        height: Int,
        zoom: Float,
        dXPixels: Int,
        dYPixels: Int,
    ): Bitmap

    fun defaultTransformMatrix(scalingFactor: Float, ): Matrix {
        val transformMatrix = Matrix();

        transformMatrix.postScale(
            scalingFactor,
            scalingFactor,
        )

        return transformMatrix
    }
}
