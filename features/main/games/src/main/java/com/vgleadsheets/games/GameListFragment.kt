package com.vgleadsheets.games

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.vgleadsheets.recyclerview.ListView
import com.vgleadsheets.repository.Repository
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game.*
import timber.log.Timber
import java.lang.Error
import javax.inject.Inject

class GameListFragment : BaseMvRxFragment(), ListView {
    override fun onItemClick(position: Int) {
        Toast.makeText(context, "Unimplemented.", LENGTH_SHORT).show()
    }

    @Inject lateinit var repository: Repository
    @Inject lateinit var gameListViewModelFactory: GameListViewModel.Factory

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

    override fun invalidate() = withState(viewModel) { state ->
        when (state.games) {
            is Uninitialized -> Timber.i("No data loaded.mod")
            is Loading -> Timber.i("Loading...")
            is Error -> Timber.e("Error getting games: ${state.games.message}")
            is Success -> adapter.dataset = state.games()
        }
    }

    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }
}
