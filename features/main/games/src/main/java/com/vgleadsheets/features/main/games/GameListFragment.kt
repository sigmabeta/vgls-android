package com.vgleadsheets.features.main.games

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.GiantBombImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_games.list_games
import javax.inject.Inject

@Suppress("TooManyFunctions")
class GameListFragment : VglsFragment(), GiantBombImageNameCaptionListModel.EventHandler {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: GameListViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: GiantBombImageNameCaptionListModel) {
        showSongList(clicked.dataId)
    }

    override fun onGbGameNotChecked(vglsId: Long, name: String, type: String) {
        viewModel.onGbGameNotChecked(vglsId, name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_games.adapter = adapter
        list_games.layoutManager = LinearLayoutManager(context)
        list_games.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, gameListState ->
        val selectedPart = hudState.parts?.first { it.selected }

        // TODO Let's make this non-null if we can.
        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        val games = gameListState.games
        if (games is Fail) {
            showError(games.error)
        }

        val listModels = constructList(games, selectedPart)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_games

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(
        games: Async<List<Game>>,
        selectedPart: PartSelectorItem
    ) = arrayListOf(createTitleListModel()) + createContentListModels(games, selectedPart)

    private fun createTitleListModel() = TitleListModel(
        R.string.subtitle_game.toLong(),
        getString(R.string.app_name),
        getString(R.string.subtitle_game)
    )

    private fun createContentListModels(
        games: Async<List<Game>>,
        selectedPart: PartSelectorItem
    ) = when (games) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(games.error)
        is Success -> createSuccessListModels(games(), selectedPart)
    }

    private fun createLoadingListModels(): List<ListModel> {
        val listModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
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
        selectedPart: PartSelectorItem
    ) = if (games.isEmpty()) {
        arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_black_24dp,
                "No games found at all. Check your internet connection?"
            )
        )
    } else {
        val availableGames = filterGames(games, selectedPart)

        if (availableGames.isEmpty()) arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_black_24dp,
                "No games found with a ${selectedPart.apiId} part. Try another part?"
            )
        ) else availableGames
            .map {
                GiantBombImageNameCaptionListModel(
                    it.id,
                    it.giantBombId,
                    it.name,
                    getString(R.string.label_sheet_count, it.songs?.size ?: 0),
                    it.photoUrl,
                    this
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

    private fun showSongList(clickedGameId: Long) {
        getFragmentRouter().showSongListForGame(clickedGameId)
    }

    companion object {
        const val LOADING_ITEMS = 15
        fun newInstance() = GameListFragment()
    }
}
