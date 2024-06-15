package com.vgleadsheets.ui.viewer

import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class ViewerState(
    val song: Song? = null,
    val partApiId: String? = null,
    val initialPage: Int = 0,
    val buttonsVisible: Boolean = true,
) : VglsState {
    fun title(stringProvider: StringProvider): TitleBarModel {
        val gameName = song?.gameName
        return TitleBarModel(
            title = song?.name,
            subtitle = gameName?.let { stringProvider.getStringOneArg(StringId.SCREEN_SUBTITLE_SONG_DETAIL, it) } ?: "",
        )
    }

    fun pages(): ImmutableList<SheetPageListModel> = if (song != null && partApiId != null) {
        List(song.pageCount(partApiId)) { pageNumber ->
            SheetPageListModel(
                PdfConfigById(
                    songId = song.id,
                    pageNumber = pageNumber
                ),
                song.name,
                song.gameName,
                song.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
                pageNumber,
                Action.PageClicked(pageNumber),
            )
        }
    } else {
        emptyList()
    }.toImmutableList()
}
