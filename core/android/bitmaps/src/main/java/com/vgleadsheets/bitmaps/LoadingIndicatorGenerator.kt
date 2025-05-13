package com.vgleadsheets.bitmaps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import com.vgleadsheets.logging.Hatchet
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class LoadingIndicatorGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hatchet: Hatchet,
    @Named("VglsUrl") private val vglsUrl: String?
) {
    private val textPaint =
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            typeface = ResourcesCompat.getFont(context, com.vgleadsheets.fonts.R.font.musejazz_text)
        }

    private val clearPaint =
        Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    private val blankStaffBitmap by lazy {
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.img_leadsheet_single_system_blank,
            BitmapFactory.Options().apply {
                inScaled = false
            }
        )
    }

    @Synchronized
    fun generateLoadingSheet(
        title: String,
        gameName: String,
        composers: List<String>,
        maxWidth: Int,
        maxHeight: Int,
    ): Bitmap {
        var resultBitmap: Bitmap
        val renderProcessTime = measureTimeMillis {
            hatchet.d("Generating loading sheet bitmap for song $gameName - $title")

            val largeBitmap = createBitmap(
                title,
                gameName,
                composers,
                maxWidth,
                maxHeight,
            )

            resultBitmap = largeBitmap.copy(Bitmap.Config.ALPHA_8, false)
            largeBitmap.recycle()

            hatchet.v("Result bitmap size: ${resultBitmap.byteCount / 1_024 / 1_024f} MiB.")
        }

        hatchet.v("Full PDF process took $renderProcessTime ms.")
        return resultBitmap
    }

    private fun createBitmap(
        title: String,
        gameName: String,
        composers: List<String>,
        maxWidth: Int,
        maxHeight: Int,
    ): Bitmap {
        val newBitmap: Bitmap

        val pdfRenderTime = measureTimeMillis {
            val pageAspectRatio = SheetConstants.ASPECT_RATIO
            val maxDimenAspectRatio = maxWidth.toFloat() / maxHeight

            val pdfWidth = DEFAULT_SHEET_WIDTH.toInt()
            val pdfHeight = DEFAULT_SHEET_HEIGHT.toInt()

            val pageToMaximumScalingFactor = computePageToMaxScalingFactor(maxWidth, maxHeight, pdfWidth, pdfHeight)

            val zoomedWidth = (pdfWidth * pageToMaximumScalingFactor).toInt()
            val zoomedHeight = (pdfHeight * pageToMaximumScalingFactor).toInt()

            val scalingType = computeScalingType(maxWidth, maxHeight, zoomedWidth, zoomedHeight)

            val bitmapWidth = computeBitmapWidth(scalingType, zoomedWidth, maxWidth)
            val bitmapHeight = computeBitmapHeight(scalingType, zoomedHeight, maxHeight)

            hatchet.v("Loader  aspect ratio: $pageAspectRatio")
            hatchet.v("MaxDimen aspect ratio: $maxDimenAspectRatio")

            hatchet.v("Loader to max scaling factor: $pageToMaximumScalingFactor")

            hatchet.v("Maximum      dimensions: $maxWidth x $maxHeight")
            hatchet.v("Specced page dimensions: $pdfWidth x $pdfHeight")
            hatchet.v("Zoomed  page dimensions: $zoomedWidth x $zoomedHeight")
            hatchet.v("Bitmap  page dimensions: $bitmapWidth x $bitmapHeight")
            hatchet.v("Scaling type: $scalingType")

            newBitmap = createNewTemplateBitmap(
                width = bitmapWidth,
                height = bitmapHeight,
                pageToMaximumScalingFactor
            )

            val uniqueText = measureTimeMillis {
                renderUniqueText(
                    newBitmap,
                    pageToMaximumScalingFactor,
                    title,
                    gameName,
                    composers,
                )
            }

            hatchet.v("Unique text rendering took $uniqueText ms.")
        }

        hatchet.v("PDF page rendering took $pdfRenderTime ms.")
        return newBitmap
    }

    private fun computePageToMaxScalingFactor(
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

    private fun computeScalingType(
        maxWidth: Int,
        maxHeight: Int,
        targetWidth: Int,
        targetHeight: Int,
    ): ScalingType {
        return if (targetWidth > maxWidth) {
            if (targetHeight > maxHeight) {
                ScalingType.MAX
            } else {
                ScalingType.FILL_WIDTH_ADJUST_HEIGHT
            }
        } else {
            if (targetHeight > maxHeight) {
                ScalingType.FILL_HEIGHT_ADJUST_WIDTH
            } else {
                ScalingType.NONE
            }
        }
    }

    private fun computeBitmapWidth(
        scalingType: ScalingType,
        targetWidth: Int,
        maxWidth: Int
    ): Int {
        return when (scalingType) {
            ScalingType.NONE, ScalingType.FILL_HEIGHT_ADJUST_WIDTH -> targetWidth
            ScalingType.FILL_WIDTH_ADJUST_HEIGHT, ScalingType.MAX -> return maxWidth
        }
    }

    private fun computeBitmapHeight(
        scalingType: ScalingType,
        targetHeight: Int,
        maxHeight: Int
    ): Int {
        return when (scalingType) {
            ScalingType.NONE, ScalingType.FILL_WIDTH_ADJUST_HEIGHT -> targetHeight
            ScalingType.FILL_HEIGHT_ADJUST_WIDTH, ScalingType.MAX -> return maxHeight
        }
    }

    private fun createNewTemplateBitmap(
        width: Int,
        height: Int,
        scalingFactor: Float
    ): Bitmap {
        hatchet.v("Creating blank bitmap with dimensions $width x $height")
        val newBitmap = createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(newBitmap)
        val centerXpos = (canvas.width / 2) / scalingFactor

        val stavesRenderingMillis = measureTimeMillis {
            renderBlankStaves(
                canvas,
                scalingFactor
            )
        }

        val commonTextRenderingMillis = measureTimeMillis {
            renderCommonText(
                canvas,
                scalingFactor,
                centerXpos
            )
        }

        hatchet.v("Common text rendering took $commonTextRenderingMillis ms")
        hatchet.v("Staff rendering took $stavesRenderingMillis ms")
        return newBitmap
    }

    @Suppress("LongParameterList")
    private fun renderUniqueText(
        bitmap: Bitmap,
        scalingFactor: Float,
        title: String,
        gameName: String,
        composers: List<String>,
    ) {
        val canvas = Canvas(bitmap)
        val centerXpos = (canvas.width / 2) / scalingFactor

        val textRenderingMillis = measureTimeMillis {
            canvas.drawRect(
                Rect(
                    (LEFT_CLEAR_BOX * scalingFactor).toInt(),
                    (TOP_CLEAR_BOX * scalingFactor).toInt(),
                    (RIGHT_CLEAR_BOX * scalingFactor).toInt(),
                    (BOTTOM_CLEAR_BOX * scalingFactor).toInt(),
                ),
                clearPaint
            )

            canvas.drawText(
                text = title,
                xPos = centerXpos,
                yPos = Y_POS_SHEET_TITLE,
                scalingFactor = scalingFactor,
                textSize = TEXT_SIZE_SHEET_TITLE,
                textAlign = Paint.Align.CENTER
            )

            canvas.drawText(
                text = gameName.prependIndent(PREFIX_GAME_NAME),
                xPos = centerXpos,
                yPos = Y_POS_GAME_NAME,
                scalingFactor = scalingFactor,
                textSize = TEXT_SIZE_GAME_NAME,
                textAlign = Paint.Align.CENTER
            )

            canvas.drawText(
                text = "C",
                xPos = X_POS_TRANSPOSITION,
                yPos = Y_POS_TRANSPOSITION,
                scalingFactor = scalingFactor,
                textSize = TEXT_SIZE_TRANSPOSITION,
                textAlign = Paint.Align.LEFT
            )

            canvas.drawText(
                text = composers
                    .take(2)
                    .joinToString(", ")
                    .prependIndent(PREFIX_COMPOSERS),
                xPos = X_POS_COMPOSERS,
                yPos = Y_POS_COMPOSERS,
                scalingFactor = scalingFactor,
                textSize = TEXT_SIZE_COMPOSERS,
                textAlign = Paint.Align.RIGHT
            )
        }

        hatchet.v("Text rendering took $textRenderingMillis ms")
    }

    private fun renderCommonText(
        canvas: Canvas,
        scalingFactor: Float,
        centerXpos: Float
    ) {
        canvas.drawText(
            text = TEXT_NOW_LOADING,
            xPos = X_POS_COMPOSERS,
            yPos = Y_POS_TRANSCRIBER,
            scalingFactor = scalingFactor,
            textSize = TEXT_SIZE_COMPOSERS,
            textAlign = Paint.Align.RIGHT
        )

        canvas.drawText(
            text = vglsUrl ?: "Fake VGLS Api",
            xPos = centerXpos,
            yPos = Y_POS_COPYRIGHT,
            scalingFactor = scalingFactor,
            textSize = TEXT_SIZE_COPYRIGHT,
            textAlign = Paint.Align.CENTER
        )
    }

    private fun renderBlankStaves(
        canvas: Canvas,
        scalingFactor: Float
    ) {
        val firstStaffXPosition = (X_POS_FIRST_STAFF * scalingFactor).toInt()
        val firstStaffYPosition = (Y_POS_FIRST_STAFF * scalingFactor).toInt()
        val staffYDisplacement = (Y_DISPLACEMENT_STAFF * scalingFactor).toInt()

        val scaledStaffWidth = (WIDTH_STAFF * scalingFactor).toInt()
        val scaledStaffHeight = (HEIGHT_STAFF * scalingFactor).toInt()

        for (staffNumber in 0 until STAFF_COUNT) {
            val staffYPos = firstStaffYPosition + (staffNumber * staffYDisplacement)

            val destRect = Rect(
                firstStaffXPosition,
                staffYPos,
                (firstStaffXPosition + scaledStaffWidth),
                (staffYPos + scaledStaffHeight)
            )

            canvas.drawBitmap(
                blankStaffBitmap,
                null,
                destRect,
                null
            )
        }
    }

    @Suppress("LongParameterList")
    private fun Canvas.drawText(
        text: String,
        xPos: Float,
        yPos: Float,
        scalingFactor: Float,
        textSize: Float,
        textAlign: Paint.Align
    ) {
        textPaint.textSize = textSize * scalingFactor
        textPaint.textAlign = textAlign

        drawText(
            text,
            xPos * scalingFactor,
            yPos * scalingFactor,
            textPaint
        )
    }

    companion object {
        const val PREFIX_GAME_NAME = "from "
        const val PREFIX_COMPOSERS = "Composed by "

        const val TEXT_NOW_LOADING = "Please wait, now loading..."

        const val DEFAULT_SHEET_WIDTH = 2550.0f
        const val DEFAULT_SHEET_HEIGHT = 3300.0f

        // const val

        const val TEXT_SIZE_SHEET_TITLE = 116.0f
        const val TEXT_SIZE_GAME_NAME = 58.0f
        const val TEXT_SIZE_TRANSPOSITION = 80.0f

        const val TEXT_SIZE_COMPOSERS = 50.0f
        const val TEXT_SIZE_COPYRIGHT = 34.0f

        const val LEFT_CLEAR_BOX = 100
        const val TOP_CLEAR_BOX = 100
        const val RIGHT_CLEAR_BOX = 2442
        const val BOTTOM_CLEAR_BOX = 380

        const val Y_POS_SHEET_TITLE = 210.0f
        const val Y_POS_GAME_NAME = 282.0f
        const val Y_POS_TRANSPOSITION = 175.0f

        const val Y_POS_COMPOSERS = 360.0f
        const val Y_POS_TRANSCRIBER = 435.0f
        const val Y_POS_COPYRIGHT = 3232.0f

        const val X_POS_COMPOSERS = 2430.0f
        const val X_POS_TRANSPOSITION = 120.0f

        const val X_POS_FIRST_STAFF = 118.0f
        const val Y_POS_FIRST_STAFF = 540.0f

        const val Y_DISPLACEMENT_STAFF = 270

        const val WIDTH_STAFF = 2313
        const val HEIGHT_STAFF = 178

        const val STAFF_COUNT = 10
    }
}
