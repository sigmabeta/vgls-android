package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class State(
    val songs: LCE<List<Song>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_ALL)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return when (songs) {
            is LCE.Content -> content(songs.data)
            is LCE.Error -> error(songs.operationName, songs.error)
            is LCE.Loading -> loading(songs.operationName)
            LCE.Uninitialized -> persistentListOf()
        }
    }

    private fun loading(operationName: String) = List(20) { index ->
        LoadingListModel(
            withImage = true,
            withCaption = true,
            loadOperationName = operationName,
            loadPositionOffset = index
        )
    }.toImmutableList()

    private fun content(songs: List<Song>) = songs
        .map { song ->
            ImageNameCaptionListModel(
                dataId = song.id,
                name = song.name,
                caption = song.gameName,
                sourceInfo = PdfConfigById(
                    songId = song.id,
                    pageNumber = 0
                ),
                imagePlaceholder = Icon.DESCRIPTION,
                clickAction = Action.SongClicked(song.id),
            )
        }
        .toImmutableList()

    private fun error(operationName: String, error: Throwable) = persistentListOf(
        ErrorStateListModel(
            failedOperationName = operationName,
            errorString = error.message ?: "Unknown error."
        )
    )
}
