package com.vgleadsheets.features.main.games

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
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider
import timber.log.Timber

class GameListViewModel @AssistedInject constructor(
    @Assisted initialState: GameListState,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : MvRxViewModel<GameListState>(initialState),
    GiantBombImageNameCaptionListModel.EventHandler {
    init {
        fetchGames()
    }

    override fun onClicked(clicked: GiantBombImageNameCaptionListModel) = setState {
        copy(
            clickedGbListModel = clicked
        )
    }

    override fun onGbApiNotAvailable() = setState {
        copy(
            gbApiNotAvailable = true
        )
    }

    override fun onGbModelNotChecked(vglsId: Long, name: String, type: String) {
        repository.searchGiantBombForGame(vglsId, name)
    }

    fun onSelectedPartUpdate(part: PartSelectorItem?) {
        setState {
            copy(
                selectedPart = part,
                listModels = constructList(
                    games,
                    updateTime,
                    digest,
                    selectedPart
                )
            )
        }
    }

    fun onDigestUpdate(digest: Async<List<VglsApiGame>>) {
        setState {
            copy(
                digest = digest,
                listModels = constructList(
                    games,
                    updateTime,
                    digest,
                    selectedPart
                )
            )
        }
    }

    fun onTimeUpdate(time: Async<Long>) {
        setState {
            copy(
                updateTime = time,
                listModels = constructList(
                    games,
                    updateTime,
                    digest,
                    selectedPart
                )
            )
        }
    }

    private fun fetchGames() {
        repository.getGames()
            .execute { games ->
                copy(
                    games = games,
                    listModels = constructList(
                        games,
                        updateTime,
                        digest,
                        selectedPart
                    )
                )
            }
    }

    private fun constructList(
        games: Async<List<Game>>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?
    ): List<ListModel> {
        Timber.v("Constructing list...")

        return arrayListOf(createTitleListModel()) +
                createContentListModels(
                    games,
                    updateTime,
                    digest,
                    selectedPart
                )
    }

    private fun createTitleListModel() = TitleListModel(
        R.string.subtitle_game.toLong(),
        resourceProvider.getString(R.string.app_name),
        resourceProvider.getString(R.string.subtitle_game)
    )

    private fun createContentListModels(
        games: Async<List<Game>>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?
    ) = when (games) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(games.error)
        is Success ->
            if (selectedPart == null) {
                createErrorStateListModel(
                    IllegalArgumentException("No part selected.")
                )
            } else {
                createSuccessListModels(
                    games(),
                    updateTime,
                    digest,
                    selectedPart
                )
            }
    }

    private fun createLoadingListModels(): List<ListModel> {
        val listModels = ArrayList<ListModel>(GameListFragment.LOADING_ITEMS)

        for (index in 0 until GameListFragment.LOADING_ITEMS) {
            listModels.add(
                LoadingNameCaptionListModel(index)
            )
        }

        return listModels
    }

    private fun createErrorStateListModel(error: Throwable) =
        arrayListOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createSuccessListModels(
        games: List<Game>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ) = if (games.isEmpty()) {
        if (digest is Loading || updateTime is Loading) {
            createLoadingListModels()
        } else {
            arrayListOf(
                EmptyStateListModel(
                    R.drawable.ic_album_24dp,
                    "No games found at all. Check your internet connection?"
                )
            )
        }
    } else {
        val availableGames = filterGames(games, selectedPart)

        if (availableGames.isEmpty()) arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No games found with a ${selectedPart.apiId} part. Try another part?"
            )
        ) else availableGames
            .map {
                GiantBombImageNameCaptionListModel(
                    it.id,
                    it.giantBombId,
                    it.name,
                    generateSubtitleText(it.songs),
                    it.photoUrl,
                    R.drawable.placeholder_game,
                    this@GameListViewModel
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

        while (builder.length < GameListFragment.MAX_LENGTH_SUBTITLE_CHARS) {
            val index = items.size - numberOfOthers

            if (index >= GameListFragment.MAX_LENGTH_SUBTITLE_ITEMS) {
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
