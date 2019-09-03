package com.vgleadsheets.features.main.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.mainstate.MainActivityViewModel
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SearchFragment : VglsFragment() {
    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory

    private val mainViewModel: MainActivityViewModel by activityViewModel()

    private val viewModel: SearchViewModel by fragmentViewModel()

    private val adapter = SearchResultAdapter(this)

    fun onGameClicked(id: Long) {
        getFragmentRouter().showSongListForGame(id)
    }

    fun onSongClicked(id: Long) {
        getFragmentRouter().showSongViewer(id)
    }

    fun onComposerClicked(id: Long) {
        showSnackbar("Clicked composer id $id.")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.subscribeToSearchTextEntry(getFragmentRouter())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()

        list_results.adapter = adapter
        list_results.layoutManager = LinearLayoutManager(context)
        list_results.setInsetListenerForPadding(topOffset = topOffset)
    }

    override fun getLayoutId() = R.layout.fragment_search

    override fun invalidate() {
        withState(mainViewModel, viewModel) { activityState, localState ->
            val query = activityState.searchQuery
            if (!query.isNullOrEmpty()) {
                viewModel.startQuery(query)
            }

            when (localState.results) {
                is Fail -> showError(
                        localState.results.error.message ?: localState.results.error::class.simpleName
                        ?: "Unknown Error"
                )
                is Loading -> showLoading()
                is Success -> showResults(localState.results())
            }

            if (localState.clickedId != null) {
                showSong(localState.clickedId)
            }
        }
    }

    private fun showSong(clickedId: Long) {
        getFragmentRouter().showSongViewer(clickedId)
        viewModel.onSongLaunch()
    }

    private fun showLoading() {
        progress_loading.fadeInFromZero()
        list_results.fadeOutPartially()
    }

    private fun hideLoading() {
        list_results.fadeIn()
        progress_loading.fadeOutGone()
    }

    private fun showResults(results: List<SearchResult>?) {
        adapter.dataset = results
        hideLoading()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
