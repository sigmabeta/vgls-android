package com.vgleadsheets.bitmaps

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import net.sigmabeta.sage.logging.BluntHatchet

class BitmapUtilsTest {
    private val hatchet = BluntHatchet()

    @Test
    fun `unfolded Pixel Fold portrait scales by height without clipping bottom`() {
        val result = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 1,
            maxWidth = PIXEL_FOLD_UNFOLDED_PORTRAIT_WIDTH,
            maxHeight = PIXEL_FOLD_UNFOLDED_PORTRAIT_HEIGHT,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )

        assertEquals(2208f / 792f, result.pageToMaximumScalingFactor)
        assertEquals(1706, result.pageWidth)
        assertEquals(2208, result.pageHeight)
        assertEquals(1706, result.zoomedPageWidth)
        assertEquals(2208, result.zoomedPageHeight)
        assertTrue(result.zoomedPageHeight <= result.pageHeight)

        // Subsequent tile renders using getActualDimensions() output must preserve exact dimensions and scale
        for (zoom in listOf(1f, 2f, 4f, 8f)) {
            val tileResult = BitmapUtils.computeBitmapSize(
                hatchet = hatchet,
                pageCount = 1,
                maxWidth = result.pageWidth,
                maxHeight = result.pageHeight,
                docWidth = PDF_PAGE_WIDTH,
                docHeight = PDF_PAGE_HEIGHT,
                zoom = zoom,
            )
            assertEquals(result.pageWidth, tileResult.pageWidth)
            assertEquals(result.pageHeight, tileResult.pageHeight)
            assertEquals(result.pageToMaximumScalingFactor, tileResult.pageToMaximumScalingFactor)
            assertEquals(result.pageToMaximumScalingFactor * zoom, tileResult.zoomedScalingFactor)
        }
    }

    @Test
    fun `unfolded Pixel 9 Pro Fold portrait scales by height without clipping bottom`() {
        val result = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 1,
            maxWidth = PIXEL_9_PRO_FOLD_UNFOLDED_PORTRAIT_WIDTH,
            maxHeight = PIXEL_9_PRO_FOLD_UNFOLDED_PORTRAIT_HEIGHT,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )

        assertEquals(2152f / 792f, result.pageToMaximumScalingFactor)
        assertEquals(1663, result.pageWidth)
        assertEquals(2152, result.pageHeight)
        assertEquals(1663, result.zoomedPageWidth)
        assertEquals(2152, result.zoomedPageHeight)
    }

    @Test
    fun `weird portrait aspect ratio wider than sheet fits height without clipping`() {
        val result = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 1,
            maxWidth = 720,
            maxHeight = 800,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )

        assertEquals(800f / 792f, result.pageToMaximumScalingFactor)
        assertEquals(618, result.pageWidth)
        assertEquals(800, result.pageHeight)
        assertEquals(618, result.zoomedPageWidth)
        assertEquals(800, result.zoomedPageHeight)
    }

    @Test
    fun `standard portrait phone scales by width without clipping`() {
        val result = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 1,
            maxWidth = 1080,
            maxHeight = 2400,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )

        assertEquals(1080f / 612f, result.pageToMaximumScalingFactor)
        assertEquals(1080, result.pageWidth)
        assertEquals(1398, result.pageHeight)
        assertEquals(1080, result.zoomedPageWidth)
        assertEquals(1398, result.zoomedPageHeight)

        for (zoom in listOf(1f, 2f, 4f, 8f)) {
            val zoomedResult = BitmapUtils.computeBitmapSize(
                hatchet = hatchet,
                pageCount = 1,
                maxWidth = 1080,
                maxHeight = 2400,
                docWidth = PDF_PAGE_WIDTH,
                docHeight = PDF_PAGE_HEIGHT,
                zoom = zoom,
            )
            assertEquals(result.pageToMaximumScalingFactor, zoomedResult.pageToMaximumScalingFactor)
            assertEquals(result.pageToMaximumScalingFactor * zoom, zoomedResult.zoomedScalingFactor)
        }
    }

    @Test
    fun `unfolded Pixel Fold portrait scales multi-page doc by height across zoom levels`() {
        val result = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 3,
            maxWidth = PIXEL_FOLD_UNFOLDED_PORTRAIT_WIDTH,
            maxHeight = PIXEL_FOLD_UNFOLDED_PORTRAIT_HEIGHT,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )

        assertEquals(2208f / 792f, result.pageToMaximumScalingFactor)
        assertEquals(1706, result.pageWidth)
        assertEquals(5118, result.docWidth)
        assertEquals(2208, result.pageHeight)

        for (zoom in listOf(0.5f, 1f, 2f, 4f, 8f)) {
            val zoomedResult = BitmapUtils.computeBitmapSize(
                hatchet = hatchet,
                pageCount = 3,
                maxWidth = PIXEL_FOLD_UNFOLDED_PORTRAIT_WIDTH,
                maxHeight = PIXEL_FOLD_UNFOLDED_PORTRAIT_HEIGHT,
                docWidth = PDF_PAGE_WIDTH,
                docHeight = PDF_PAGE_HEIGHT,
                zoom = zoom,
            )
            assertEquals(result.pageToMaximumScalingFactor, zoomedResult.pageToMaximumScalingFactor)
            assertEquals(result.pageToMaximumScalingFactor * zoom, zoomedResult.zoomedScalingFactor)
        }
    }

    @Test
    fun `unfolded Pixel Fold landscape scales by height for single and multi-page docs`() {
        val result = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 2,
            maxWidth = PIXEL_FOLD_UNFOLDED_PORTRAIT_HEIGHT,
            maxHeight = PIXEL_FOLD_UNFOLDED_PORTRAIT_WIDTH,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )

        assertEquals(1840f / 792f, result.pageToMaximumScalingFactor)
        assertEquals(1422, result.pageWidth)
        assertEquals(2844, result.docWidth)
        assertEquals(1840, result.pageHeight)
        assertEquals(1422, result.zoomedPageWidth)
        assertEquals(1840, result.zoomedPageHeight)
    }

    @Test
    fun `square thumbnail scales by height to fit within square bounds`() {
        val result = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 1,
            maxWidth = 48,
            maxHeight = 48,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )

        assertEquals(48f / 792f, result.pageToMaximumScalingFactor)
        assertEquals(37, result.pageWidth)
        assertEquals(48, result.pageHeight)
        assertEquals(37, result.zoomedPageWidth)
        assertEquals(48, result.zoomedPageHeight)
    }

    @Test
    fun `rounding boundary height preserves exact height on second pass`() {
        // 1010 * 612 / 792 = 780.4545 -> rounds down to 780; 780 * 792 / 612 = 1009.41
        val initial = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 1,
            maxWidth = 1200,
            maxHeight = 1010,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )
        assertEquals(780, initial.pageWidth)
        assertEquals(1010, initial.pageHeight)

        val secondPass = BitmapUtils.computeBitmapSize(
            hatchet = hatchet,
            pageCount = 1,
            maxWidth = initial.pageWidth,
            maxHeight = initial.pageHeight,
            docWidth = PDF_PAGE_WIDTH,
            docHeight = PDF_PAGE_HEIGHT,
            zoom = 1f,
        )
        assertEquals(780, secondPass.pageWidth)
        assertEquals(1010, secondPass.pageHeight)
        assertEquals(initial.pageToMaximumScalingFactor, secondPass.pageToMaximumScalingFactor)
    }

    companion object {
        private const val PDF_PAGE_WIDTH = 612
        private const val PDF_PAGE_HEIGHT = 792

        private const val PIXEL_FOLD_UNFOLDED_PORTRAIT_WIDTH = 1840
        private const val PIXEL_FOLD_UNFOLDED_PORTRAIT_HEIGHT = 2208

        private const val PIXEL_9_PRO_FOLD_UNFOLDED_PORTRAIT_WIDTH = 2076
        private const val PIXEL_9_PRO_FOLD_UNFOLDED_PORTRAIT_HEIGHT = 2152
    }
}
