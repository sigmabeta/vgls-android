package com.vgleadsheets.ui.viewer

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.model.Part
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
    val isAltSelected: LCE<Boolean> = LCE.Uninitialized,
    val isSongHistoryEntryRecorded: Boolean = false,
) : VglsState {
    fun title(stringProvider: StringProvider): TitleBarModel {
        val gameName = song?.gameName
        return TitleBarModel(
            title = song?.name,
            subtitle = gameName?.let { stringProvider.getStringOneArg(StringId.SCREEN_SUBTITLE_SONG_DETAIL, it) } ?: "",
        )
    }

    fun pages(): ImmutableList<SheetPageListModel> = if (song != null && partApiId != null) {
        val pageCount = song.pageCount(partApiId, false)
        val actualPartApiId = if (pageCount > 0) {
            partApiId
        } else {
            Part.C.apiId
        }

        val (actualPageCount, altSelection) = if (isAltSelected !is LCE.Content) {
            0 to false
        } else {
            song.pageCount(actualPartApiId, isAltSelected.data) to isAltSelected.data
        }

        List(actualPageCount) { pageNumber ->
            SheetPageListModel(
                SourceInfo(
                    PdfConfigById(
                        songId = song.id,
                        pageNumber = pageNumber,
                        isAltSelected = altSelection,
                    )
                ),
                song.name,
                song.gameName,
                song.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
                pageNumber,
                beeg = true,
                VglsAction.Noop,
            )
        }
    } else {
        listOf(
            SheetPageListModel(
                sourceInfo = SourceInfo(null),
                title = song?.name.orEmpty(),
                gameName = song?.gameName.orEmpty(),
                composers = song?.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
                pageNumber = 0,
                beeg = true,
                clickAction = VglsAction.Noop,
            )
        )
    }.toImmutableList()

    fun shouldShowLyricsWarning(): Boolean {
        return if (song != null && partApiId != null) {
            song.pageCount(partApiId, false) <= 0
        } else {
            false
        }
    }
}
