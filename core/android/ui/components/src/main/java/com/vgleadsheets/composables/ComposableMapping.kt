package com.vgleadsheets.composables

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
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.NetworkRefreshingListModel
import com.vgleadsheets.components.SearchResultListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.SubsectionHeaderListModel
import com.vgleadsheets.components.SubsectionListModel
import com.vgleadsheets.components.WideItemListModel

@Suppress("MaxLineLength")
@Composable
fun ListModel.Content(
    actionSink: ActionSink,
    modifier: Modifier,
) {
    when (this) {
        is CheckableListModel -> LabelCheckboxItem(model = this, actionSink = actionSink, modifier = modifier)
        is CtaListModel -> ActionItem(model = this, actionSink = actionSink, modifier = modifier)
        is DropdownSettingListModel -> LabelDropdownListItem(model = this, modifier = modifier)
        is EmptyStateListModel -> EmptyListIndicator(model = this, modifier = modifier)
        is ErrorStateListModel -> EmptyListIndicator(model = this, modifier = modifier)
        is HeroImageListModel -> BigImage(model = this, actionSink = actionSink, modifier = modifier)
        is HorizontalScrollerListModel -> HorizontalScroller(model = this, actionSink = actionSink, modifier = modifier,)
        is IconNameCaptionListModel -> IconNameCaptionListItem(model = this, actionSink = actionSink, modifier = modifier)
        is ImageNameCaptionListModel -> ImageNameCaptionListItem(model = this, actionSink = actionSink, modifier = modifier)
        is ImageNameListModel -> ImageNameListItem(model = this, actionSink = actionSink, modifier = modifier)
        is LabelRatingStarListModel -> LabelRatingListItem(model = this, actionSink = actionSink, modifier = modifier)
        is LabelValueListModel -> LabelValueListItem(model = this, actionSink = actionSink, modifier = modifier)
        is MenuItemListModel -> MenuItem(model = this, actionSink = actionSink, modifier = modifier)
        is NameCaptionListModel -> NameCaptionListItem(model = this, actionSink = actionSink, modifier = modifier)
        is SheetPageCardListModel -> SheetPageCard(model = this.sheetPageModel, actionSink = actionSink, modifier = modifier)
        is SearchResultListModel -> ImageNameCaptionListItem(model = this, actionSink = actionSink, modifier = modifier)
        is SectionHeaderListModel -> SectionHeader(name = title, modifier = modifier)
        is SheetPageListModel -> SheetPageItem(model = this, actionSink = actionSink, modifier = modifier)
        is SingleTextListModel -> LabelNoThingyItem(model = this, modifier = modifier)
        is SquareItemListModel -> SquareItem(model = this, actionSink = actionSink, modifier = modifier)
        is SubsectionHeaderListModel -> SubsectionHeader(model = this, modifier = modifier)
        is SubsectionListModel -> Subsection(model = this, actionSink = actionSink, modifier = modifier)
        is WideItemListModel -> WideItem(model = this, actionSink = actionSink, modifier = modifier)
        is LoadingImageNameCaptionListModel -> LoadingListItem(
            withImage = true,
            seed = dataId,
            modifier = modifier
        )

        is LoadingNameCaptionListModel -> LoadingListItem(
            withImage = false,
            seed = dataId,
            modifier = modifier
        )

        is NetworkRefreshingListModel -> LoadingListItem(
            withImage = true,
            seed = dataId,
            modifier = modifier,
        )

        else -> throw IllegalArgumentException("No composable exists for this item type.")
    }
}
