package com.vgleadsheets.remaster.tags.songs

import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

@AssistedInject
class TagValueSongsViewModel(
    @Assisted private val idArg: Long,
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val tagRepository: TagRepository,
    private val songRepository: SongRepository,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.LIST_TAG_VALUE_SONG

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitWithId(idArg))
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitWithId -> startLoading(action.id)
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
            SageEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.TAGS_VALUES_SONG_LIST.name
            )
        )
    }

    private fun updateTagValue(tagValue: LCE<TagValue>) {
        updateState {
            it.copy(
                tagValue = tagValue
            )
        }
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            it.copy(
                songs = songs
            )
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted idArg: Long): TagValueSongsViewModel
    }

    companion object {
        private const val LOAD_OPERATION_VALUE = "value.title"
        private const val LOAD_OPERATION_SONGS = "songs.list"
    }
}
