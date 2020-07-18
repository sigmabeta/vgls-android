package com.vgleadsheets.features.main.jam

import android.annotation.SuppressLint
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.LoadingTitleListModel
import com.vgleadsheets.components.NetworkRefreshingListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.hud.parts.getPartMatchingSelection
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry
import com.vgleadsheets.model.jam.SongHistoryEntry
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider
import timber.log.Timber
import java.util.Locale

@SuppressWarnings("TooManyFunctions")
class JamViewModel @AssistedInject constructor(
    @Assisted initialState: JamState,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : AsyncListViewModel<JamData, JamState>(initialState, resourceProvider),
    CtaListModel.EventHandler {
    init {
        fetchJam()
    }

    override val showDefaultEmptyState = false

    override fun onClicked(clicked: CtaListModel) = setState { copy(clickedCtaModel = clicked) }

    override fun clearClicked() = setState {
        copy(
            clickedCtaModel = null,
            clickedCurrentSongModel = null,
            clickedHistoryModel = null,
            clickedSetListModel = null,
            refreshError = null
        )
    }

    override fun defaultLoadingListModel(index: Int): ListModel =
        LoadingNameCaptionListModel("allData", index)

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_list_black_24dp,
        "Unknown error occurred."
    )

    fun refreshJam() = withState { state ->
        Timber.i("Refreshing jam...")

        val name = state.data.jam()?.name

        if (name == null) {
            setState {
                copy(refreshError = "Cannot refresh jam without knowing its name.")
            }
            return@withState
        }

        repository
            .refreshJamState(name)
            .execute { newJamRefresh ->
                val newData = data.copy(
                    jamRefresh = newJamRefresh
                )
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }

        repository
            .refreshSetlist(state.jamId, name)
            .execute { newSetlistRefresh ->
                val newData = data.copy(
                    setlistRefresh = newSetlistRefresh
                )
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }
    }

    fun deleteJam() = withState {
        repository.removeJam(it.jamId)
            .execute { newDeletion ->
                copy(
                    deletion = newDeletion
                )
            }
    }

    override fun createSuccessListModels(
        data: JamData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ) = createTitleListModel(data.jam) +
            createCtaListModels(data.jam) +
            createJamListModels(data.jam, data.jamRefresh, selectedPart) +
            createSetlistListModels(data.setlist, data.setlistRefresh, selectedPart) +
            createSongHistoryListModels(data.jam, selectedPart)

    private fun createJamListModels(
        jam: Async<Jam>,
        jamRefresh: Async<ApiJam>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val refreshingListModels = if (jamRefresh is Loading) {
            listOf(NetworkRefreshingListModel("jam"))
        } else emptyList()

        val jamListModels = when (jam) {
            is Loading, Uninitialized -> createLoadingListModels("JamList")
            is Fail -> createErrorStateListModel("jam", jam.error)
            is Success -> createSuccessListModels(jam(), selectedPart)
        }

        return refreshingListModels + jamListModels
    }

    private fun createSuccessListModels(
        jam: Jam,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val thumbUrl = jam.currentSong
            ?.parts
            ?.getPartMatchingSelection(selectedPart)
            ?.pages
            ?.first()
            ?.imageUrl

        return listOf(
            SectionHeaderListModel(
                resourceProvider.getString(R.string.jam_current_song)
            ),
            ImageNameCaptionListModel(
                Long.MAX_VALUE,
                jam.currentSong?.name ?: "Unknown Song",
                jam.currentSong?.gameName ?: "Unknown Game",
                thumbUrl,
                R.drawable.placeholder_sheet,
                currentSongHandler,
                jam.currentSong?.id ?: -1L,
                perfHandler = perfHandler
            )
        )
    }

    private fun createSetlistListModels(
        setlist: Async<List<SetlistEntry>>,
        setlistRefresh: Async<List<Long>>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val refreshingListModels = if (setlistRefresh is Loading) {
            listOf(NetworkRefreshingListModel("setlist"))
        } else emptyList()

        if (setlistRefresh is Fail) {
            setState {
                copy(
                    refreshError = setlistRefresh.error.message
                )
            }
        }

        val jamListModels = when (setlist) {
            is Loading, Uninitialized -> createLoadingListModels("SetList")
            is Fail -> createErrorStateListModel("setlist", setlist.error)
            is Success -> createSuccessListModels(setlist(), selectedPart)
        }

        return refreshingListModels + jamListModels
    }

    private fun createSuccessListModels(
        setlist: List<SetlistEntry>,
        selectedPart: PartSelectorItem
    ) = listOf(
        SectionHeaderListModel(
            resourceProvider.getString(R.string.jam_setlist)
        )
    ) + if (setlist.isEmpty()) {
        listOf(
            EmptyStateListModel(
                R.drawable.ic_list_black_24dp,
                resourceProvider.getString(R.string.empty_setlist)
            )
        )
    } else {
        setlist.map { entry ->
            val thumbUrl = entry.song
                ?.parts
                ?.getPartMatchingSelection(selectedPart)
                ?.pages
                ?.first()
                ?.imageUrl

            ImageNameCaptionListModel(
                entry.id,
                entry.songName,
                entry.gameName,
                thumbUrl,
                R.drawable.placeholder_sheet,
                setlistSongHandler,
                entry.song?.id,
                perfHandler = perfHandler
            )
        }
    }

    private fun createSongHistoryListModels(
        jam: Async<Jam>,
        selectedPart: PartSelectorItem
    ) = when (jam) {
        is Loading, Uninitialized -> createLoadingListModels("SongHistory")
        is Fail -> createErrorStateListModel("history", jam.error)
        is Success -> createSuccessListModelsForSongHistory(jam().songHistory!!, selectedPart)
    }

    private fun createSuccessListModelsForSongHistory(
        songHistory: List<SongHistoryEntry>,
        selectedPart: PartSelectorItem
    ) = if (songHistory.isEmpty()) {
        emptyList()
    } else {
        listOf(
            SectionHeaderListModel(
                resourceProvider.getString(R.string.jam_song_history)
            )
        ) + songHistory.map { entry ->
            val thumbUrl = entry.song
                ?.parts
                ?.getPartMatchingSelection(selectedPart)
                ?.pages
                ?.first()
                ?.imageUrl

            ImageNameCaptionListModel(
                entry.id,
                entry.song?.name ?: "Unknown Song",
                entry.song?.gameName ?: "Unknown Game",
                thumbUrl,
                R.drawable.placeholder_sheet,
                historyHandler,
                entry.song?.id,
                perfHandler = perfHandler
            )
        }
    }

    private fun createLoadingListModels(sectionId: String) = listOf(
        LoadingNameCaptionListModel(sectionId, sectionId.hashCode())
    )

    private fun createCtaListModels(jam: Async<Jam>) = when (jam) {
        is Loading, Uninitialized -> createLoadingListModels("Cta")
        is Fail, is Success -> listOf(
            CtaListModel(
                R.drawable.ic_playlist_play_black_24dp,
                resourceProvider.getString(R.string.cta_follow_jam),
                this
            ),
            CtaListModel(
                R.drawable.ic_refresh_24dp,
                resourceProvider.getString(R.string.cta_refresh_jam),
                this
            ),
            CtaListModel(
                R.drawable.ic_delete_black_24dp,
                resourceProvider.getString(R.string.cta_delete_jam),
                this
            )
        )
    }

    private fun createErrorStateListModel(failedOperationName: String, error: Throwable) =
        listOf(ErrorStateListModel(failedOperationName, error.message ?: "Unknown Error"))

    private fun fetchJam() = withState { state ->
        val jamId = state.jamId

        repository.getJam(jamId, true)
            .execute { newJam ->
                val newData = data.copy(
                    jam = newJam
                )
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }

        repository.getSetlistForJam(jamId)
            .execute { newSetlist ->
                val newData = data.copy(
                    setlist = newSetlist
                )
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }
    }

    private fun createTitleListModel(jam: Async<Jam>) = when (jam) {
        is Loading, Uninitialized -> listOf(LoadingTitleListModel())
        is Fail -> createErrorStateListModel("title", jam.error)
        is Success -> listOf(
            TitleListModel(
                jam().name.toTitleCase(),
                resourceProvider.getString(R.string.subtitle_jam),
                perfHandler = perfHandler
            )
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("DefaultLocale")
    private fun String.toTitleCase() = this
        .replace("_", " ")
        .split(" ")
        .map {
            if (it != "the") {
                it.capitalize(Locale.getDefault())
            } else {
                it
            }
        }
        .joinToString(" ")

    private val currentSongHandler = object : ImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: ImageNameCaptionListModel) = setState {
            copy(clickedCurrentSongModel = clicked)
        }

        override fun clearClicked() = setState { copy(clickedCurrentSongModel = null) }
    }

    private val historyHandler = object : ImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: ImageNameCaptionListModel) = setState { copy(clickedHistoryModel = clicked) }

        override fun clearClicked() = setState { copy(clickedHistoryModel = null) }
    }

    private val setlistSongHandler = object : ImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: ImageNameCaptionListModel) = setState { copy(clickedSetListModel = clicked) }

        override fun clearClicked() = setState { copy(clickedSetListModel = null) }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: JamState): JamViewModel
    }

    companion object : MvRxViewModelFactory<JamViewModel, JamState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: JamState
        ): JamViewModel? {
            val fragment: JamFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.jamViewModelFactory.create(state)
        }
    }
}
