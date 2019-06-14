package com.vgleadsheets.games

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.recyclerview.ListView
import com.vgleadsheets.repository.*
import kotlinx.android.synthetic.main.fragment_game.*
import javax.inject.Inject

class GameListFragment : VglsFragment(), ListView {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    private val viewModel: GameListViewModel by fragmentViewModel()

    private val adapter = GameListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list_games.adapter = adapter
        list_games.layoutManager = LinearLayoutManager(context)
    }

    override fun onItemClick(position: Int) {
        viewModel.onItemClick(position)
    }

    override fun invalidate() = withState(viewModel) { state ->
        if (state.clickedGameId != null) {
            showSongList(state.clickedGameId)
            viewModel.onSongListLaunch()
            return@withState
        }

        when (state.data) {
            is Fail -> showError(state.data.error.message ?: state.data.error::class.simpleName ?: "Unknown Error")
            is Success -> showData(state.data())
        }
    }

    override fun getLayoutId() = R.layout.fragment_game

    private fun showData(data: Data<List<Game>>?) {
        when (data) {
            is Empty -> showLoading()
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Network -> hideLoading()
            is Storage -> showGames(data())
        }
    }

    private fun showSongList(clickedGameId: Long) {
        (activity as FragmentRouter).showSongListForGame(clickedGameId)
    }

    private fun showLoading() {
        progress_loading.fadeInFromZero()
        list_games.fadeOutPartially()
    }

    private fun hideLoading() {
        list_games.fadeIn()
        progress_loading.fadeOutGone()
    }

    private fun showGames(games: List<Game>) {
        adapter.dataset = games
    }

    companion object {
        fun newInstance() = GameListFragment()
    }
}
