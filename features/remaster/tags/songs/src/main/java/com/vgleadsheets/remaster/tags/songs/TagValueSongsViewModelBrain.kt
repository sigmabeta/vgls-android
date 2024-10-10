package com.vgleadsheets.remaster.tags.songs

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class TagValueSongsViewModelBrain(
    private val tagRepository: TagRepository,
    private val songRepository: SongRepository,
    private val scheduler: VglsScheduler,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitWithId -> startLoading(action.id)
            is Action.SongClicked -> onSongClicked(action.id)
        }
    }

    private fun startLoading(id: Long) {
        showLoading()
        loadTagValue(id)
        loadSongs(id)
    }

    private fun loadTagValue(id: Long) {
        tagRepository.getTagValue(id)
            .onEach(::onTagValueLoaded)
            .catch { error -> showTagValueError(error) }
            .runInBackground()
    }

    private fun loadSongs(id: Long) {
        songRepository.getSongsForTagValue(id)
            .onEach(::onSongsLoaded)
            .catch { error -> showSongsError(error) }
            .runInBackground()
    }

    private fun onTagValueLoaded(tagValue: TagValue) {
        updateTagValue(LCE.Content(tagValue))
    }

    private fun showLoading() {
        updateTagValue(LCE.Loading(LOAD_OPERATION_VALUE))
        updateSongs(LCE.Loading(LOAD_OPERATION_SONGS))
    }

    private fun showTagValueError(error: Throwable) {
        updateTagValue(LCE.Error(LOAD_OPERATION_VALUE, error))
    }

    private fun showSongsError(error: Throwable) {
        updateSongs(LCE.Error(LOAD_OPERATION_SONGS, error))
    }

    private fun onSongsLoaded(songs: List<Song>) {
        updateSongs(LCE.Content(songs))
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.TAGS_VALUES_SONG_LIST.name
            )
        )
    }

    private fun updateTagValue(tagValue: LCE<TagValue>) {
        updateState {
            (it as State).copy(
                tagValue = tagValue
            )
        }
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            (it as State).copy(
                songs = songs
            )
        }
    }

    companion object {
        private const val LOAD_OPERATION_VALUE = "value.title"
        private const val LOAD_OPERATION_SONGS = "songs.list"
    }
}
