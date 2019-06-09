package com.vgleadsheets.games

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.google.android.material.snackbar.Snackbar
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.recyclerview.ListView
import com.vgleadsheets.repository.*
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game.*
import timber.log.Timber
import javax.inject.Inject

class GameListFragment : BaseMvRxFragment(), ListView {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    private val viewModel: GameListViewModel by fragmentViewModel()

    private val adapter = GameListAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater
        .inflate(R.layout.fragment_game, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list_games.adapter = adapter
        list_games.layoutManager = LinearLayoutManager(context)
    }

    override fun onItemClick(position: Int) {
        viewModel.onItemClick(position)
    }

    override fun invalidate() = withState(viewModel) { state ->
        if (state.clickedGame != null) {
            showSheetList(state.clickedGame)
            viewModel.onSheetScreenLaunch()
            return@withState
        }

        when (state.data) {
            is Fail -> showError(state.data.error.message ?: state.data.error::class.simpleName ?: "Unknown Error")
            is Success -> showData(state.data())
        }
    }

    private fun showData(data: Data<List<Game>>?) {
        when (data) {
            is Empty -> showLoading()
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Network -> hideLoading()
            is Storage -> showGames(data())
        }
    }

    private fun showSheetList(clickedGame: Game) {
        (activity as FragmentRouter).showSheetListForGame(clickedGame.id)
    }

    private fun showLoading() {
        Timber.i("Loading...")
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

    private fun showError(message: String, action: View.OnClickListener? = null, actionLabel: Int = 0) {
        Timber.e("Error getting games: $message")
        showSnackbar(message, action, actionLabel)
    }

    private fun showSnackbar(message: String, action: View.OnClickListener?, actionLabel: Int) {
        val snackbar = Snackbar.make(constraint_content, message, Snackbar.LENGTH_LONG)

        if (action != null && actionLabel > 0) {
            snackbar.setAction(actionLabel, action)
        }

        snackbar.show()
    }

    companion object {
        fun newInstance() = GameListFragment()
    }
}
