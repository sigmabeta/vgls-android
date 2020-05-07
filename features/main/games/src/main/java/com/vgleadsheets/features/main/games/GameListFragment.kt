package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.game.Game
import javax.inject.Inject

class GameListFragment : ListFragment<Game, GameListState>() {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    override val viewModel: GameListViewModel by fragmentViewModel()

    private var apiAvailableErrorShown = false

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(GameListState::clickedGbListModel, deliveryMode = UniqueOnly("clicked")) {
            val clickedGameId = it?.dataId
            if (clickedGameId != null) {
                showSongList(clickedGameId)
            }
        }

        viewModel.selectSubscribe(GameListState::gbApiNotAvailable, deliveryMode = UniqueOnly("api")) {
            if (it == true) {
                if (!apiAvailableErrorShown) {
                    apiAvailableErrorShown = true
                    showError(getString(R.string.error_no_gb_api))
                }
            }
        }
    }

    private fun showSongList(clickedGameId: Long) {
        getFragmentRouter().showSongListForGame(clickedGameId)
    }

    companion object {
        fun newInstance() = GameListFragment()
    }
}
