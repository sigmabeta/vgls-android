package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.model.Composer
import com.vgleadsheets.strings.StringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SquareItemListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ColumnType
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

@Suppress("MagicNumber")
data class State(
    val composers: LCE<List<Composer>> = LCE.Uninitialized,
) : ListState() {
    override val columnType = ColumnType.Regular(160)

    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_COMPOSERS)
    )

    override fun toListItems(stringProvider: StringProvider) = composers.withStandardErrorAndLoading(
        loadingType = LoadingType.SQUARE,
        loadingWithHeader = false,
    ) {
        data.map { composer ->
            SquareItemListModel(
                dataId = composer.id,
                name = composer.name,
                sourceInfo = composer.photoUrl,
                imagePlaceholder = Icon.PERSON,
                clickAction = Action.ComposerClicked(composer.id),
            )
        }
    }
}
