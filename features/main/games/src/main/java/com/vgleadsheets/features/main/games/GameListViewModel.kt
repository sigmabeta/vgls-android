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
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.ListViewModel
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

class GameListViewModel @AssistedInject constructor(
    @Assisted initialState: GameListState,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : ListViewModel<Game, GameListState>(initialState, resourceProvider),
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

    override fun createTitleListModel() = TitleListModel(
        resourceProvider.getString(R.string.app_name),
        resourceProvider.getString(R.string.subtitle_game),
        perfHandler = perfHandler
    )

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No games found at all. Check your internet connection?"
    )

    override fun createSuccessListModels(
        data: List<Game>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val availableGames = filterGames(data, selectedPart)

        return if (availableGames.isEmpty()) listOf(
            EmptyStateListModel(
                com.vgleadsheets.features.main.list.R.drawable.ic_album_24dp,
                "No games found with a ${selectedPart.apiId} part. Try another part?"
            )
        ) else availableGames
            .map {
                ImageNameCaptionListModel(
                    it.id,
                    it.name,
                    generateSubtitleText(it.songs),
                    it.photoUrl,
                    com.vgleadsheets.features.main.list.R.drawable.placeholder_game,
                    this@GameListViewModel,
                    perfHandler = perfHandler
                )
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
        selectedPart: PartSelectorItem
    ) = games.map { game ->
        val availableSongs = game.songs?.filter { song ->
            song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
        }

        game.copy(songs = availableSongs)
    }.filter {
        it.songs?.isNotEmpty() ?: false
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
                resourceProvider.getString(R.string.subtitle_suffix_others, numberOfOthers)
            )
        }

        return builder.toString()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: GameListState): GameListViewModel
    }

    companion object : MvRxViewModelFactory<GameListViewModel, GameListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: GameListState
        ): GameListViewModel? {
            val fragment: GameListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.gameListViewModelFactory.create(state)
        }
    }
}
