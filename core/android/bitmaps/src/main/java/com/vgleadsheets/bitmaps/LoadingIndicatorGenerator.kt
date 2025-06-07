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
import com.vgleadsheets.logging.Hatchet
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.roundToInt
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

    @Suppress("MagicNumber")
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
            val docWidth = DEFAULT_SHEET_WIDTH.toInt()
            val docHeight = DEFAULT_SHEET_HEIGHT.toInt()

            val bitmapSizeInfo = BitmapUtils.computeBitmapSize(
                hatchet,
                pageCount = 1,
                maxWidth,
                maxHeight,
                docWidth,
                docHeight,
                1.0f
            )

            newBitmap = BitmapUtils.createBlankBitmap(
                width = bitmapSizeInfo.pageWidth,
                height = bitmapSizeInfo.pageHeight,
            )

            val canvas = Canvas(newBitmap)
            val centerXpos = (canvas.width / 2) / bitmapSizeInfo.pageToMaximumScalingFactor

            val stavesRenderingMillis = measureTimeMillis {
                renderBlankStaves(
                    canvas,
                    bitmapSizeInfo.pageToMaximumScalingFactor,
                )
            }

            val commonTextRenderingMillis = measureTimeMillis {
                renderCommonText(
                    canvas,
                    bitmapSizeInfo.pageToMaximumScalingFactor,
                    centerXpos
                )
            }

            hatchet.v("Common text rendering took $commonTextRenderingMillis ms")
            hatchet.v("Staff rendering took $stavesRenderingMillis ms")
            val uniqueText = measureTimeMillis {
                renderUniqueText(
                    newBitmap,
                    bitmapSizeInfo.pageToMaximumScalingFactor,
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
                    (LEFT_CLEAR_BOX * scalingFactor).roundToInt(),
                    (TOP_CLEAR_BOX * scalingFactor).roundToInt(),
                    (RIGHT_CLEAR_BOX * scalingFactor).roundToInt(),
                    (BOTTOM_CLEAR_BOX * scalingFactor).roundToInt(),
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

        const val DEFAULT_SHEET_WIDTH = 612.0f
        const val DEFAULT_SHEET_HEIGHT = 792.0f

        const val TEXT_SIZE_SHEET_TITLE = 27.84f
        const val TEXT_SIZE_GAME_NAME = 13.92f
        const val TEXT_SIZE_TRANSPOSITION = 19.2f

        const val TEXT_SIZE_COMPOSERS = 12.0f
        const val TEXT_SIZE_COPYRIGHT = 8.16f

        const val LEFT_CLEAR_BOX = 24
        const val TOP_CLEAR_BOX = 24
        const val RIGHT_CLEAR_BOX = 585.84
        const val BOTTOM_CLEAR_BOX = 91.2

        const val Y_POS_SHEET_TITLE = 50.4f
        const val Y_POS_GAME_NAME = 67.68f
        const val Y_POS_TRANSPOSITION = 42.0f

        const val Y_POS_COMPOSERS = 86.4f
        const val Y_POS_TRANSCRIBER = 104.4f
        const val Y_POS_COPYRIGHT = 775.68f

        const val X_POS_COMPOSERS = 583.2f
        const val X_POS_TRANSPOSITION = 28.8f

        const val X_POS_FIRST_STAFF = 28.32f
        const val Y_POS_FIRST_STAFF = 129.6f

        const val Y_DISPLACEMENT_STAFF = 64.8

        const val WIDTH_STAFF = 555.12
        const val HEIGHT_STAFF = 42.72

        const val STAFF_COUNT = 10
    }
}
