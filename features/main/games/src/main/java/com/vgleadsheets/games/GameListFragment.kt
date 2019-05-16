package com.vgleadsheets.games

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.airbnb.mvrx.*
import com.vgleadsheets.repository.Repository
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import java.lang.Error
import javax.inject.Inject

class GameListFragment : BaseMvRxFragment() {
    lateinit var repository: Repository
        @Inject set

    private val viewModel: GameListViewModel by fragmentViewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun invalidate() = withState(viewModel) {
        when (it.games) {
            is Uninitialized -> viewModel.fetchGames(repository)
            is Loading -> Timber.i("Loading...")
            is Error -> Timber.e("Error getting games: ${it.games.message}")
            is Success -> Timber.w("Found games list size: ${it.games()?.size}")
        }
    }

    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }
}
