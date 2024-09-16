package com.vgleadsheets.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.IconNameCaptionListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.LabelRatingStarListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.NotifListModel
import com.vgleadsheets.components.SearchHistoryListModel
import com.vgleadsheets.components.SearchResultListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.components.SmallTextListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.SubsectionHeaderListModel
import com.vgleadsheets.components.SubsectionListModel
import com.vgleadsheets.components.WideItemListModel

@Suppress("MaxLineLength")
@Composable
fun ListModel.Content(
    sink: ActionSink,
    debug: Boolean,
    mod: Modifier,
    pad: PaddingValues,
) {
    when (this) {
        is CheckableListModel -> LabelCheckboxItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is CtaListModel -> ActionItem(model = this, actionSink = sink, modifier = mod)
        is DropdownSettingListModel -> LabelDropdownListItem(model = this, modifier = mod, padding = pad)
        is EmptyStateListModel -> EmptyListIndicator(model = this, modifier = mod)
        is ErrorStateListModel -> EmptyListIndicator(model = this, showDebug = debug, modifier = mod)
        is HeroImageListModel -> BigImage(model = this, actionSink = sink, modifier = mod, padding = pad)
        is HorizontalScrollerListModel -> HorizontalScroller(model = this, actionSink = sink, showDebug = debug, modifier = mod, padding = pad)
        is IconNameCaptionListModel -> IconNameCaptionListItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is ImageNameCaptionListModel -> ImageNameCaptionListItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is ImageNameListModel -> ImageNameListItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is LabelRatingStarListModel -> LabelRatingListItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is LabelValueListModel -> LabelValueListItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is MenuItemListModel -> MenuItem(model = this, actionSink = sink, modifier = mod)
        is NameCaptionListModel -> NameCaptionListItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is NotifListModel -> NotifListItem(model = this, actionSink = sink, modifier = mod)
        is SheetPageCardListModel -> SheetPageCard(model = this.sheetPageModel, actionSink = sink, showDebug = debug, modifier = mod, padding = pad)
        is SearchResultListModel -> ImageNameCaptionListItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is SectionHeaderListModel -> SectionHeader(name = title, modifier = mod, padding = pad)
        is SearchHistoryListModel -> SearchHistoryListItem(model = this, modifier = mod, actionSink = sink, padding = pad)
        is SheetPageListModel -> SheetPageItem(model = this, actionSink = sink, fillMaxWidth = true, showDebug = debug, modifier = mod, padding = pad)
        is SingleTextListModel -> LabelNoThingyItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is SmallTextListModel -> SmallText(model = this, actionSink = sink, modifier = mod, padding = pad)
        is SquareItemListModel -> SquareItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is SubsectionHeaderListModel -> SubsectionHeader(model = this, modifier = mod)
        is SubsectionListModel -> Subsection(model = this, actionSink = sink, modifier = mod, padding = pad)
        is WideItemListModel -> WideItem(model = this, actionSink = sink, modifier = mod, padding = pad)
        is LoadingItemListModel -> {
            when (loadingType) {
                LoadingType.SHEET, LoadingType.SQUARE, LoadingType.NOTIF, LoadingType.WIDE_ITEM, LoadingType.BIG_IMAGE -> LoadingItem(
                    seed = dataId,
                    loadingType = loadingType,
                    modifier = mod,
                    padding = pad
                )

                LoadingType.SECTION_HEADER -> LoadingSectionHeader(seed = dataId, modifier = mod, padding = pad)
                else -> LoadingTextItem(seed = dataId, loadingType = loadingType, modifier = mod, padding = pad)
            }
        }

        else -> throw IllegalArgumentException("No composable exists for item type: ${this.javaClass.simpleName}")
    }
}
