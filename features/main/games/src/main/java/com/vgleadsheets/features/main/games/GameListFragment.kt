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
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.song.Song
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

    private var apiAvailableErrorShown = false

    override fun onClicked(clicked: GiantBombImageNameCaptionListModel) {
        showSongList(clicked.dataId)
    }

    override fun onGbModelNotChecked(vglsId: Long, name: String, type: String) {
        viewModel.onGbGameNotChecked(vglsId, name)
    }

    override fun onGbApiNotAvailable() {
        if (!apiAvailableErrorShown) {
            apiAvailableErrorShown = true
            showError(getString(R.string.error_no_gb_api))
        }
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
        hudViewModel.dontAlwaysShowBack()
        val selectedPart = hudState.parts?.firstOrNull { it.selected }

        // TODO Let's make this non-null if we can.
        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        val games = gameListState.games
        if (games is Fail) {
            showError(games.error)
        }

        val listModels = constructList(
            games,
            hudState.updateTime,
            hudState.digest,
            selectedPart
        )
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_games

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(
        games: Async<List<Game>>,
        updateTime: Async<Long>,
        digest: Async<List<VglsApiGame>>,
        selectedPart: PartSelectorItem
    ) = arrayListOf(createTitleListModel()) +
            createContentListModels(
                games,
                updateTime,
                digest,
                selectedPart
            )

    private fun createTitleListModel() = TitleListModel(
        R.string.subtitle_game.toLong(),
        getString(R.string.app_name),
        getString(R.string.subtitle_game)
    )

    private fun createContentListModels(
        games: Async<List<Game>>,
        updateTime: Async<Long>,
        digest: Async<List<VglsApiGame>>,
        selectedPart: PartSelectorItem
    ) = when (games) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(games.error)
        is Success -> createSuccessListModels(
            games(),
            updateTime,
            digest,
            selectedPart
        )
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
        updateTime: Async<Long>,
        digest: Async<List<VglsApiGame>>,
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
                builder.append(getString(R.string.subtitle_separator))
            }

            val stringToAppend = items[index].name
            builder.append(stringToAppend)
            numberOfOthers--
        }

        if (numberOfOthers != 0) {
            builder.append(getString(R.string.subtitle_suffix_others, numberOfOthers))
        }

        return builder.toString()
    }

    companion object {
        const val LOADING_ITEMS = 15

        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6

        fun newInstance() = GameListFragment()
    }
}
