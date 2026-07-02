package com.vgleadsheets.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.BigImageConstants
import com.vgleadsheets.composables.previews.FullScreenOf
import com.vgleadsheets.composables.previews.NotifConstants
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.previews.SquareConstants
import com.vgleadsheets.composables.previews.WideItemConstants
import com.vgleadsheets.composables.subs.ElevatedRoundRect
import com.vgleadsheets.composables.subs.Flasher
import com.vgleadsheets.model.generator.StringGenerator
import kotlinx.collections.immutable.toImmutableList
import com.vgleadsheets.bitmaps.SheetConstants
import net.sigmabeta.sage.components.HorizontalScrollerListModel
import net.sigmabeta.sage.components.LoadingItemListModel
import net.sigmabeta.sage.components.LoadingType
import java.util.Random
import kotlin.random.asKotlinRandom

@Composable
@Suppress("MagicNumber")
fun LoadingItem(
    seed: Long,
    loadingType: LoadingType,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val randomizer = Random(seed)
    val randomDelay = randomizer.nextInt(200)

    val (width, ratio) = when (loadingType) {
        LoadingType.PAGE -> SheetConstants.MIN_WIDTH.dp to SheetConstants.ASPECT_RATIO
        LoadingType.SQUARE -> SquareConstants.MIN_WIDTH to SquareConstants.ASPECT_RATIO
        LoadingType.NOTIF -> NotifConstants.MIN_WIDTH to NotifConstants.ASPECT_RATIO
        LoadingType.WIDE_ITEM -> WideItemConstants.MIN_WIDTH to WideItemConstants.ASPECT_RATIO
        LoadingType.BIG_IMAGE -> BigImageConstants.MIN_WIDTH to BigImageConstants.ASPECT_RATIO
        else -> return
    }

    ElevatedRoundRect(
        modifier = modifier
            .padding(paddingValues = padding)
            .defaultMinSize(minWidth = width)
            .aspectRatio(ratio),
    ) {
        Flasher(startDelay = randomDelay)
    }
}
