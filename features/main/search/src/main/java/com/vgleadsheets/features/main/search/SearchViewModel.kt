package com.vgleadsheets.features.main.search

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
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.GiantBombImageNameCaptionListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.SearchEmptyStateListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.Locale
import java.util.concurrent.TimeUnit

class SearchViewModel @AssistedInject constructor(
    @Assisted initialState: SearchState,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : AsyncListViewModel<SearchData, SearchState>(initialState, resourceProvider) {
    private val searchOperations = CompositeDisposable()

    private val songHandler = object : ImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: ImageNameCaptionListModel) = setState {
            copy(
                clickedSong = clicked
            )
        }

        override fun clearClicked() = setState {
            copy(
                clickedSong = null
            )
        }
    }

    private val gameHandler = object : GiantBombImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: GiantBombImageNameCaptionListModel) = setState {
            copy(clickedGame = clicked)
        }

        override fun clearClicked() = setState {
            copy(clickedGame = null)
        }

        override fun onGbModelNotChecked(vglsId: Long, name: String, type: String) =
            onGbGameNotChecked(vglsId, name)

        override fun onGbApiNotAvailable() = setState {
            copy(gbApiNotAvailable = true)
        }
    }

    private val composerHandler = object : GiantBombImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: GiantBombImageNameCaptionListModel) = setState {
            copy(clickedComposer = clicked)
        }

        override fun clearClicked() = setState {
            copy(clickedComposer = null)
        }

        override fun onGbModelNotChecked(vglsId: Long, name: String, type: String) =
            onGbComposerNotChecked(vglsId, name)

        override fun onGbApiNotAvailable() = setState {
            copy(gbApiNotAvailable = true)
        }
    }

    fun startQuery(searchQuery: String) {
        withState { state ->
            if (state.data.query != searchQuery) {
                setState {
                    updateSearchState(
                        query = searchQuery
                    )
                }

                searchOperations.clear()

                val gameSearch = repository.searchGamesCombined(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute { newGames ->
                        updateSearchState(games = newGames)
                    }

                val songSearch = repository.searchSongs(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute { newSongs ->
                        updateSearchState(songs = newSongs)
                    }

                val composerSearch = repository.searchComposersCombined(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute { newComposers ->
                        updateSearchState(composers = newComposers)
                    }

                searchOperations.addAll(gameSearch, songSearch, composerSearch)
            }
        }
    }

    fun onQueryClear() {
        setState {
            updateSearchState(
                null,
                Uninitialized,
                Uninitialized,
                Uninitialized
            )
        }
    }

    fun onGbGameNotChecked(vglsId: Long, name: String) {
        repository.searchGiantBombForGame(vglsId, name)
    }

    fun onGbComposerNotChecked(vglsId: Long, name: String) {
        repository.searchGiantBombForComposer(vglsId, name)
    }

    fun clearClickedSong() {
        songHandler.clearClicked()
    }

    fun clearClickedGame() {
        gameHandler.clearClicked()
    }

    fun clearClickedComposer() {
        composerHandler.clearClicked()
    }

    override fun createTitleListModel() = null

    override fun createFullEmptyStateListModel() = SearchEmptyStateListModel()

    override fun createSuccessListModels(
        data: SearchData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val query = data.query

        if (query?.toLowerCase(Locale.getDefault())?.contains("stickerbr") == true) {
            return listOf(
                ErrorStateListModel(
                    "stickerbrush",
                    resourceProvider.getString(R.string.error_search_stickerbrush)
                )
            )
        }

        val songModels = createSectionModels(
            R.string.section_header_songs,
            data.songs,
            selectedPart
        )
        val gameModels = createSectionModels(
            R.string.section_header_games,
            data.games,
            selectedPart
        )
        val composerModels = createSectionModels(
            R.string.section_header_composers,
            data.composers,
            selectedPart
        )

        val listModels = songModels + gameModels + composerModels
        return if (listModels.isEmpty()) {
            listOf(
                EmptyStateListModel(
                    R.drawable.ic_description_24dp,
                    resourceProvider.getString(R.string.empty_search_no_results)
                )
            )
        } else {
            listModels
        }
    }

    private fun createSectionModels(
        sectionId: Int,
        results: Async<List<Any>>,
        selectedPart: PartSelectorItem
    ) = when (results) {
        is Loading, Uninitialized -> createLoadingListModels(sectionId)
        is Fail -> createErrorStateListModel(resourceProvider.getString(sectionId), results.error)
        is Success -> createSectionSuccessModels(
            sectionId,
            results(),
            selectedPart
        )
    }

    private fun createSectionSuccessModels(
        sectionId: Int,
        results: List<Any>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        return if (results.isEmpty()) {
            emptyList()
        } else {
            val filteredResults = filterResults(results, selectedPart)

            if (filteredResults.isEmpty()) {
                emptyList()
            } else {
                createSectionHeaderListModel(sectionId) + createSectionModels(
                    filteredResults,
                    selectedPart
                )
            }
        }
    }

    private fun createSectionHeaderListModel(sectionId: Int) =
        listOf(SectionHeaderListModel(resourceProvider.getString(sectionId)))

    private fun createSectionModels(
        results: List<Any>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        return results
            .map {
                when (it) {
                    is Song -> {
                        val thumbUrl = it
                            .parts
                            ?.firstOrNull() { part -> part.name == selectedPart.apiId }
                            ?.pages
                            ?.firstOrNull()
                            ?.imageUrl

                        ImageNameCaptionListModel(
                            it.id,
                            it.name,
                            it.gameName,
                            thumbUrl,
                            getPlaceholderId(it),
                            songHandler
                        )
                    }
                    is Game -> GiantBombImageNameCaptionListModel(
                        it.id,
                        it.giantBombId,
                        it.name,
                        generateSubtitleText(it.songs),
                        it.photoUrl,
                        getPlaceholderId(it),
                        gameHandler
                    )
                    is Composer -> GiantBombImageNameCaptionListModel(
                        it.id,
                        it.giantBombId,
                        it.name,
                        generateSubtitleText(it.songs),
                        it.photoUrl,
                        getPlaceholderId(it),
                        composerHandler
                    )
                    else -> throw IllegalArgumentException(
                        "Bad model in search result list."
                    )
                }
            }
    }

    private fun filterResults(results: List<Any>, selectedPart: PartSelectorItem) = results
        .performMappingStep(selectedPart)
        .performFilteringStep(selectedPart)

    private fun List<Any>.performMappingStep(selectedPart: PartSelectorItem) = map {
        when (it) {
            is Song -> it
            is Game -> it.performMappingStep(selectedPart)
            is Composer -> it.performMappingStep(selectedPart)
            else -> throw IllegalArgumentException("ListModel filtering not supported!")
        }
    }

    private fun Game.performMappingStep(selectedPart: PartSelectorItem): Game {
        val availableSongs = songs?.filter { song ->
            song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
        }

        return copy(songs = availableSongs)
    }

    private fun Composer.performMappingStep(selectedPart: PartSelectorItem): Composer {
        val availableSongs = songs?.filter { song ->
            song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
        }

        return copy(songs = availableSongs)
    }

    private fun List<Any>.performFilteringStep(selectedPart: PartSelectorItem) = filter {
        when (it) {
            is Song -> it.performFilteringStep(selectedPart)
            is Game -> it.performFilteringStep()
            is Composer -> it.performFilteringStep()
            else -> throw IllegalArgumentException("ListModel filtering not supported!")
        }
    }

    private fun Song.performFilteringStep(selectedPart: PartSelectorItem) = parts
        ?.firstOrNull { part -> part.name == selectedPart.apiId } != null

    private fun Game.performFilteringStep() = songs
        ?.isNotEmpty() ?: false

    private fun Composer.performFilteringStep() = songs
        ?.isNotEmpty() ?: false

    private fun createLoadingListModels(sectionId: Int) = listOf(
        LoadingNameCaptionListModel(resourceProvider.getString(sectionId), sectionId)
    )

    private fun createErrorStateListModel(failedOperationName: String, error: Throwable) = listOf(
        ErrorStateListModel(
            failedOperationName,
            error.message ?: "Unknown Error"
        )
    )

    private fun getPlaceholderId(model: Any) = when (model) {
        is Song -> R.drawable.placeholder_sheet
        is Game -> R.drawable.placeholder_game
        is Composer -> R.drawable.placeholder_composer
        else -> R.drawable.ic_error_24dp
    }

    private fun generateSheetCaption(song: Song): String {
        return when (song.composers?.size) {
            1 -> song.composers?.firstOrNull()?.name ?: "Unknown Composer"
            else -> "Various Composers"
        }
    }

    private fun generateSubtitleText(items: List<Song>?): String {
        if (items.isNullOrEmpty()) return "Error: no values found."

        val builder = StringBuilder()
        var numberOfOthers = items.size

        while (builder.length < MAX_LENGTH_SUBTITLE_CHARS) {
            val index = items.size - numberOfOthers

            if (index >= MAX_LENGTH_SUBTITLE_ITEMS) {
                break
            }

            if (numberOfOthers == 0) {
                break
            }

            if (index != 0) {
                builder.append(resourceProvider.getString(R.string.subtitle_separator))
            }

            val stringToAppend = items[index].name
            builder.append(stringToAppend)
            numberOfOthers--
        }

        if (numberOfOthers != 0) {
            builder.append(
                resourceProvider.getString(
                    R.string.subtitle_suffix_others,
                    numberOfOthers
                )
            )
        }

        return builder.toString()
    }

    private fun SearchState.updateSearchState(
        query: String? = data.query,
        songs: Async<List<Song>> = data.songs,
        composers: Async<List<Composer>> = data.composers,
        games: Async<List<Game>> = data.games
    ): SearchState {
        val newData = SearchData(query, songs, composers, games)
        return updateListState(
            data = newData,
            listModels = constructList(
                newData,
                this
            )
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SearchState): SearchViewModel
    }

    companion object : MvRxViewModelFactory<SearchViewModel, SearchState> {
        const val RESULT_DEBOUNCE_THRESHOLD = 250L

        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6

        override fun create(
            viewModelContext: ViewModelContext,
            state: SearchState
        ): SearchViewModel? {
            val fragment: SearchFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.searchViewModelFactory.create(state)
        }
    }
}
