package com.vgleadsheets.remaster.tags.songs

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
        loadTagValue(id)
        loadSong(id)
    }

    private fun loadTagValue(id: Long) {
        tagRepository.getTagValue(id)
            .onEach(::onTagValueLoaded)
            .runInBackground()
    }

    private fun loadSong(id: Long) {
        songRepository.getSongsForTagValue(id)
            .onEach(::onSongsLoaded)
            .runInBackground()
    }

    private fun onTagValueLoaded(tagValue: TagValue) {
        updateState {
            (it as State).copy(
                tagValue = tagValue
            )
        }
    }

    private fun onSongsLoaded(songs: List<Song>) {
        updateState {
            (it as State).copy(
                songs = songs
            )
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.TAGS_VALUES_SONG_LIST.name
            )
        )
    }
}
