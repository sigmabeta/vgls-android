package com.vgleadsheets.games

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.repository.Data
import com.vgleadsheets.repository.Empty
import com.vgleadsheets.repository.Error
import com.vgleadsheets.repository.Network
import com.vgleadsheets.repository.Storage
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_game.*
import javax.inject.Inject

class GameListFragment : VglsFragment() {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    private val viewModel: GameListViewModel by fragmentViewModel()

    private val adapter = GameListAdapter(this)

    fun onItemClick(clickedGameId: Long) {
        showSongList(clickedGameId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
            resources.getDimension(R.dimen.margin_large).toInt()

        list_games.adapter = adapter
        list_games.layoutManager = LinearLayoutManager(context)
        list_games.setInsetListenerForPadding(topOffset = topOffset)
    }

    override fun invalidate() {
        withState(viewModel) { state ->
            when (state.data) {
                is Fail -> showError(state.data.error.message ?: state.data.error::class.simpleName ?: "Unknown Error")
                is Success -> showData(state.data())
            }
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
        getFragmentRouter().showSongListForGame(clickedGameId)
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
