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
import kotlin.system.measureTimeMillis

@Singleton
class SheetGenerator @Inject constructor(
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

    private var prevWidth = 0

    private var prevBitmap: Bitmap? = null

    fun generateLoadingSheet(
        width: Int,
        title: String,
        transposition: String,
        gameName: String,
        composers: List<String>,
    ): Bitmap {
        hatchet.v(
            "Generating $width px wide loading image for song $title from $gameName"
        )

        val scalingFactor = width / DEFAULT_SHEET_WIDTH
        val scaledHeight = scalingFactor * DEFAULT_SHEET_HEIGHT

        if (width != prevWidth) {
            prevWidth = width
            prevBitmap = createNewTemplateBitmap(width, scaledHeight, scalingFactor)
        }

        val bitmap = prevBitmap!!
        val uniqueText = measureTimeMillis {
            renderUniqueText(
                bitmap,
                scalingFactor,
                title,
                gameName,
                composers,
                transposition,
            )
        }

        hatchet.v("Unique text rendering took $uniqueText ms")
        return bitmap
    }

    private fun createNewTemplateBitmap(
        width: Int,
        scaledHeight: Float,
        scalingFactor: Float
    ): Bitmap? {
        val newBitmap = Bitmap.createBitmap(
            width,
            scaledHeight.toInt(),
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

    private fun renderUniqueText(
        bitmap: Bitmap,
        scalingFactor: Float,
        title: String,
        gameName: String,
        composers: List<String>,
        transposition: String,
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
                text = transposition,
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

        for (staffNumber in 0 until 10) {
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
    }
}
