package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val composers: List<Composer> = emptyList()
) : ListState() {
    override val renderAsGrid = true
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_COMPOSERS)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return composers
            .map { composer ->
                SquareItemListModel(
                    dataId = composer.id,
                    name = composer.name,
                    sourceInfo = composer.photoUrl,
                    imagePlaceholder = Icon.ALBUM,
                    clickAction = Action.ComposerClicked(composer.id),
                )
            }
            .toImmutableList()
    }
}
