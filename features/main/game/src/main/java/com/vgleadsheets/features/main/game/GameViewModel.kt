package com.vgleadsheets.features.main.game

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
import com.vgleadsheets.components.GiantBombTitleListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

@SuppressWarnings("TooManyFunctions")
class GameViewModel @AssistedInject constructor(
    @Assisted initialState: GameState,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : AsyncListViewModel<GameData, GameState>(initialState, resourceProvider),
    GiantBombTitleListModel.EventHandler,
    ImageNameCaptionListModel.EventHandler {
    init {
        fetchGame()
        fetchSongs()
    }

    override fun onGbModelNotChecked(vglsId: Long, name: String) {
        repository.searchGiantBombForGame(vglsId, name)
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

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No songs found at all. Check your internet connection?"
    )

    override fun createSuccessListModels(
        data: GameData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val title = createTitleListModel(data.game, data.songs)
        val content = createContentListModels(data.songs, selectedPart)

        return title + content
    }

    private fun fetchGame() = withState { state ->
        repository
            .getGame(state.gameId)
            .execute { game ->
                val newData = GameData(game, data.songs)
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }
    }

    private fun fetchSongs() = withState { state ->
        repository
            .getSongsForGame(state.gameId)
            .execute { songs ->
                val newData = GameData(data.game, songs)
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }
    }

    private fun createTitleListModel(game: Async<Game>, songs: Async<List<Song>>): List<ListModel> =
        when (game) {
            is Success -> listOf(
                GiantBombTitleListModel(
                    game().id,
                    game().giantBombId,
                    game().name,
                    generateSheetCountText(songs),
                    game().photoUrl,
                    R.drawable.placeholder_game,
                    this@GameViewModel
                )
            )
            is Fail -> createErrorStateListModel(game.error)
            is Uninitialized, is Loading -> listOf(
                LoadingNameCaptionListModel("title", 0)
            )
            else -> createErrorStateListModel(IllegalStateException("Unhandled title state."))
        }

    private fun generateSheetCountText(
        songs: Async<List<Song>>
    ) = if (songs is Success) {
        resourceProvider.getString(R.string.subtitle_sheets_count, songs().size)
    } else ""

    private fun createContentListModels(
        songs: Async<List<Song>>,
        selectedPart: PartSelectorItem
    ) = when (songs) {
        is Success -> createSongListModels(songs(), selectedPart)
        is Fail -> createErrorStateListModel(songs.error)
        is Uninitialized, is Loading -> createLoadingListModels()
    }

    private fun createSongListModels(
        songs: List<Song>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val availableSongs = filterSongs(songs, selectedPart)

        return if (availableSongs.isEmpty()) {
            arrayListOf(
                EmptyStateListModel(
                    R.drawable.ic_album_24dp,
                    "No songs found with a ${selectedPart.apiId} part. Try another part?"
                )
            )
        } else {
            availableSongs.map {
                val thumbUrl = it
                    .parts
                    ?.first { part -> part.name == selectedPart.apiId }
                    ?.pages
                    ?.first()
                    ?.imageUrl

                ImageNameCaptionListModel(
                    it.id,
                    it.name,
                    generateSheetCaption(it),
                    thumbUrl,
                    R.drawable.placeholder_sheet,
                    this@GameViewModel
                )
            }
        }
    }

    private fun filterSongs(
        songs: List<Song>,
        selectedPart: PartSelectorItem
    ) = songs.filter { song ->
        song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
    }

    private fun generateSheetCaption(song: Song): String {
        return when (song.composers?.size) {
            1 -> song.composers?.firstOrNull()?.name ?: "Unknown Composer"
            else -> "Various Composers"
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: GameState): GameViewModel
    }

    companion object : MvRxViewModelFactory<GameViewModel, GameState> {
        override fun create(viewModelContext: ViewModelContext, state: GameState): GameViewModel? {
            val fragment: GameFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.gameViewModelFactory.create(state)
        }
    }
}
