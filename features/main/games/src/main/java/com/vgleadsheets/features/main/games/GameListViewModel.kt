package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.list.ListViewModel
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

class GameListViewModel @AssistedInject constructor(
    @Assisted initialState: GameListState,
    @Assisted val screenName: String,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val perfTracker: PerfTracker
) : ListViewModel<Game, GameListState>(initialState),
    ImageNameCaptionListModel.EventHandler {
    init {
        fetchGames()
    }

    override fun onClicked(clicked: ImageNameCaptionListModel) = setState {
        copy(
            clickedListModel = clicked
        )
    }

    override fun clearClicked() = setState {
        copy(
            clickedListModel = null
        )
    }

    override fun createTitleListModel(): TitleListModel {
        val spec = PerfSpec.GAMES

        perfTracker.onTitleLoaded(spec)
        perfTracker.onTransitionStarted(spec)

        return TitleListModel(
            resourceProvider.getString(R.string.app_name),
            resourceProvider.getString(R.string.subtitle_game),
            {},
            {}
        )
    }

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No games found at all. Check your internet connection?",
    )

    override fun createSuccessListModels(
        data: List<Game>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part
    ): List<ListModel> {
        val availableGames = filterGames(data, selectedPart)

        return if (availableGames.isEmpty()) listOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No games found with a ${selectedPart.apiId} part. Try another part?",
            )
        ) else {
            val spec = PerfSpec.GAMES

            perfTracker.onPartialContentLoad(spec)
            perfTracker.onFullContentLoad(spec)

            availableGames
                .map {
                    ImageNameCaptionListModel(
                        it.id,
                        it.name,
                        generateSubtitleText(it.songs),
                        it.photoUrl,
                        R.drawable.placeholder_game,
                        this@GameListViewModel,
                    )
                }
        }
    }

    private fun fetchGames() {
        repository.getGames()
            .execute { games ->
                updateListState(
                    data = games,
                    listModels = constructList(
                        games,
                        updateTime,
                        digest,
                        selectedPart
                    )
                )
            }
    }

    private fun filterGames(
        games: List<Game>,
        selectedPart: Part
    ) = games.map { game ->
        val availableSongs = game.songs?.filteredForVocals(selectedPart.apiId)

        game.copy(songs = availableSongs)
    }.filter {
        it.songs?.isNotEmpty() ?: false
    }

    @Suppress("LoopWithTooManyJumpStatements")
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
                resourceProvider.getString(R.string.subtitle_suffix_others, numberOfOthers)
            )
        }

        return builder.toString()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: GameListState, screenName: String): GameListViewModel
    }

    companion object : MvRxViewModelFactory<GameListViewModel, GameListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: GameListState
        ): GameListViewModel? {
            val fragment: GameListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.gameListViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
