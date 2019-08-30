package com.vgleadsheets.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.recyclerview.ListView
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : VglsFragment(), ListView {
    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory

    private val viewModel: SearchViewModel by fragmentViewModel()

    private val adapter = SearchResultAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.subscribeToSearchTextEntry(getFragmentRouter())
    }

    override fun onItemClick(position: Int) {
        viewModel.onItemClick(position)
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
        super.invalidate()
        withState(mainViewModel) { state ->
            val query = state.searchQuery
            if (!query.isNullOrEmpty()) {
                viewModel.startQuery(query)
            }
        }

        withState(viewModel) {
            when (it.results) {
                is Fail -> showError(it.results.error.message ?: it.results.error::class.simpleName ?: "Unknown Error")
                is Loading -> showLoading()
                is Success -> showResults(it.results())
            }

            if (it.clickedId != null) {
                showSong(it.clickedId)
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
