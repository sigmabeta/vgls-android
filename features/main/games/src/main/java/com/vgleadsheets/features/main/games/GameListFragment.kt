package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.game.Game
import javax.inject.Inject

class GameListFragment : ListFragment<Game, GameListState>() {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    override val viewModel: GameListViewModel by fragmentViewModel()

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(GameListState::clickedListModel) {
            val clickedGameId = it?.dataId

            if (clickedGameId != null) {
                showSongList(clickedGameId)
                viewModel.clearClicked()
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
