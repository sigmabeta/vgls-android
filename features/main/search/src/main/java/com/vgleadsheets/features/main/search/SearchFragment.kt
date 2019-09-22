package com.vgleadsheets.features.main.search

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SearchFragment : VglsFragment() {
    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory

    private val hudViewModel: HudViewModel by activityViewModel()

    private val viewModel: SearchViewModel by fragmentViewModel()

    private val adapter = SearchResultAdapter(this)

    fun onGameClicked(id: Long) {
        hudViewModel.exitSearch()
        getFragmentRouter().showSongListForGame(id)
    }

    fun onSongClicked(id: Long) {
        hudViewModel.exitSearch()
        getFragmentRouter().showSongViewer(id)
    }

    fun onComposerClicked(id: Long) {
        hudViewModel.exitSearch()
        getFragmentRouter().showSongListForComposer(id)
    }

    override fun onBackPress(): Boolean {
        hudViewModel.exitSearch()
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_results.adapter = adapter
        list_results.layoutManager = LinearLayoutManager(context)
        list_results.setInsetListenerForPadding(topOffset = topOffset, bottomOffset = bottomOffset)
    }

    override fun getLayoutId() = R.layout.fragment_search

    override fun invalidate() {
        withState(hudViewModel, viewModel) { hudState, localState ->
            // Sanity check - while exiting this screen, we might get an update due
            // to clearing the text box, to which we respond by clearing the text box.
            if (hudState.searchVisible) {
                val query = hudState.searchQuery
                if (!query.isNullOrEmpty()) {
                    viewModel.startQuery(query)
                } else {
                    viewModel.clearResults()
                }
            }

            when (localState.results) {
                is Fail -> showError(
                    localState.results.error.message ?: localState.results.error::class.simpleName
                    ?: "Unknown Error"
                )
                is Loading -> showLoading()
                is Success -> showResults(localState.results())
            }
        }
    }

    override fun getVglsFragmentTag() = this.javaClass.simpleName

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
