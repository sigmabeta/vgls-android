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
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry
import com.vgleadsheets.model.jam.SongHistoryEntry
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider
import java.util.Locale
import javax.inject.Named
import timber.log.Timber

@SuppressWarnings("TooManyFunctions")
class JamViewModel @AssistedInject constructor(
    @Assisted initialState: JamState,
    @Assisted val screenName: String,
    @Named("VglsImageUrl") val baseImageUrl: String,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val perfTracker: PerfTracker
) : AsyncListViewModel<JamData, JamState>(initialState),
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
        "Unknown error occurred.",
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
                        digest,
                        updateTime,
                        selectedPart
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
                        digest,
                        updateTime,
                        selectedPart
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
        selectedPart: Part
    ) = createTitleListModel(data.jam) +
        createCtaListModels(data.jam) +
        createJamListModels(data.jam, data.jamRefresh, selectedPart) +
        createSetlistListModels(data.setlist, data.setlistRefresh, selectedPart) +
        createSongHistoryListModels(data.jam, selectedPart)

    private fun createJamListModels(
        jam: Async<Jam>,
        jamRefresh: Async<ApiJam>,
        selectedPart: Part
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
        selectedPart: Part
    ): List<ListModel> {
        val currentSong = jam.currentSong

        return listOf(
            SectionHeaderListModel(
                resourceProvider.getString(R.string.jam_current_song)
            )
        ) + if (currentSong != null) {
            ImageNameCaptionListModel(
                Long.MAX_VALUE,
                currentSong.name,
                currentSong.gameName,
                Page.generateImageUrl(
                    baseImageUrl,
                    selectedPart,
                    currentSong.filename,
                    1
                ),
                R.drawable.placeholder_sheet,
                currentSongHandler,
                currentSong.id,
            )
        } else {
            generateSongLoadError()
        }
    }

    private fun createSetlistListModels(
        setlist: Async<List<SetlistEntry>>,
        setlistRefresh: Async<List<Long>>,
        selectedPart: Part
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
        selectedPart: Part
    ) = listOf(
        SectionHeaderListModel(
            resourceProvider.getString(R.string.jam_setlist)
        )
    ) + if (setlist.isEmpty()) {
        listOf(
            EmptyStateListModel(
                R.drawable.ic_list_black_24dp,
                resourceProvider.getString(R.string.empty_setlist),
            )
        )
    } else {
        setlist.map { entry ->
            val song = entry.song
                ?: return@map generateSongLoadError()

            val thumbUrl = Page.generateImageUrl(
                baseImageUrl,
                selectedPart,
                song.filename,
                1
            )

            ImageNameCaptionListModel(
                entry.id,
                entry.songName,
                entry.gameName,
                thumbUrl,
                R.drawable.placeholder_sheet,
                setlistSongHandler,
                song.id,
            )
        }
    }

    private fun generateSongLoadError() = ImageNameCaptionListModel(
        -1L,
        "Unknown Song",
        "An error occurred.",
        null,
        R.drawable.ic_error_24dp,
        setlistSongHandler,
        null,
    )

    private fun createSongHistoryListModels(
        jam: Async<Jam>,
        selectedPart: Part
    ) = when (jam) {
        is Loading, Uninitialized -> createLoadingListModels("SongHistory")
        is Fail -> createErrorStateListModel("history", jam.error)
        is Success -> createSuccessListModelsForSongHistory(jam().songHistory!!, selectedPart)
    }

    private fun createSuccessListModelsForSongHistory(
        songHistory: List<SongHistoryEntry>,
        selectedPart: Part
    ) = if (songHistory.isEmpty()) {
        emptyList()
    } else {
        val spec = PerfSpec.JAM

        perfTracker.onPartialContentLoad(spec)
        perfTracker.onFullContentLoad(spec)

        listOf(
            SectionHeaderListModel(
                resourceProvider.getString(R.string.jam_song_history)
            )
        ) + songHistory.map { entry ->
            val song = entry.song

            if (song != null) {
                ImageNameCaptionListModel(
                    entry.id,
                    song.name,
                    song.gameName,
                    Page.generateImageUrl(
                        baseImageUrl,
                        selectedPart,
                        song.filename,
                        1
                    ),
                    R.drawable.placeholder_sheet,
                    historyHandler,
                    song.id,
                )
            } else {
                generateSongLoadError()
            }
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
        listOf(
            ErrorStateListModel(
                failedOperationName,
                error.message ?: "Unknown Error",
            )
        )

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
                        digest,
                        updateTime,
                        selectedPart
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
                        digest,
                        updateTime,
                        selectedPart
                    )
                )
            }
    }

    private fun createTitleListModel(jam: Async<Jam>) = when (jam) {
        is Loading, Uninitialized -> listOf(LoadingTitleListModel())
        is Fail -> createErrorStateListModel("title", jam.error)
        is Success -> {
            val spec = PerfSpec.JAM

            perfTracker.onTitleLoaded(spec)

            listOf(
                TitleListModel(
                    jam().name.toTitleCase(),
                    resourceProvider.getString(R.string.subtitle_jam),
                    { perfTracker.onTransitionStarted(spec) },
                    { perfTracker.cancel(spec) },
                )
            )
        }
    }

    private fun String.toTitleCase() = this
        .replace("_", " ")
        .split(" ")
        .map {
            if (it != "the") {
                it.capitalize()
            } else {
                it
            }
        }
        .joinToString(" ")

    @SuppressLint("DefaultLocale")
    private fun String.capitalize() = replaceFirstChar { char ->
        if (char.isLowerCase()) {
            char.titlecase(Locale.getDefault())
        } else {
            char.toString()
        }
    }

    private val currentSongHandler = object : ImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: ImageNameCaptionListModel) = setState {
            copy(clickedCurrentSongModel = clicked)
        }

        override fun clearClicked() = setState { copy(clickedCurrentSongModel = null) }
    }

    private val historyHandler = object : ImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: ImageNameCaptionListModel) =
            setState { copy(clickedHistoryModel = clicked) }

        override fun clearClicked() = setState { copy(clickedHistoryModel = null) }
    }

    private val setlistSongHandler = object : ImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: ImageNameCaptionListModel) =
            setState { copy(clickedSetListModel = clicked) }

        override fun clearClicked() = setState { copy(clickedSetListModel = null) }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: JamState, screenName: String): JamViewModel
    }

    companion object : MvRxViewModelFactory<JamViewModel, JamState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: JamState
        ): JamViewModel? {
            val fragment: JamFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.jamViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
