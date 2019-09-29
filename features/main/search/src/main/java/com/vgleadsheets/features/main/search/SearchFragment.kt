package com.vgleadsheets.features.main.search

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SearchFragment : VglsFragment(), NameCaptionListModel.ClickListener {
    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: SearchViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: NameCaptionListModel) {
        when (clicked.type) {
            SearchResultType.GAME.toString() -> onGameClicked(clicked.dataId)
            SearchResultType.SONG.toString() -> onSongClicked(clicked.dataId)
            SearchResultType.COMPOSER.toString() -> onComposerClicked(clicked.dataId)
        }
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

            val games = localState.games()
            val songs = localState.songs()
            val composers = localState.composers()

            showResults(games, songs, composers)
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

    @Suppress("LongMethod")
    private fun showResults(
        games: List<SearchResult>?,
        songs: List<SearchResult>?,
        composers: List<SearchResult>?
    ) {
        val gameComponents = games?.map {
            val stringId = R.string.label_type_game

            NameCaptionListModel(
                it.id,
                it.name,
                getString(stringId),
                this,
                it.type.toString()
            ) as ListModel
        }.orEmpty().toMutableList()

        if (gameComponents.isNotEmpty()) {
            gameComponents.add(
                0,
                SectionHeaderListModel(
                    R.string.section_header_games.toLong(),
                    getString(R.string.section_header_games)
                )
            )
        }

        val songComponents = songs?.map {
            val stringId = R.string.label_type_song

            NameCaptionListModel(
                it.id,
                it.name,
                getString(stringId),
                this,
                it.type.toString()
            ) as ListModel
        }.orEmpty().toMutableList()

        if (songComponents.isNotEmpty()) {
            songComponents.add(
                0,
                SectionHeaderListModel(
                    R.string.section_header_songs.toLong(),
                    getString(R.string.section_header_songs)
                )
            )
        }

        val composerComponents = composers?.map {
            val stringId = R.string.label_type_composer

            NameCaptionListModel(
                it.id,
                it.name,
                getString(stringId),
                this,
                it.type.toString()
            ) as ListModel
        }.orEmpty().toMutableList()

        if (composerComponents.isNotEmpty()) {
            composerComponents.add(
                0,
                SectionHeaderListModel(
                    R.string.section_header_composers.toLong(),
                    getString(R.string.section_header_composers)
                )
            )
        }

        val listComponents = songComponents + gameComponents + composerComponents

        adapter.submitList(listComponents)
        hideLoading()
    }

    private fun onGameClicked(id: Long) {
        hudViewModel.exitSearch()
        getFragmentRouter().showSongListForGame(id)
    }

    private fun onSongClicked(id: Long) {
        hudViewModel.exitSearch()
        getFragmentRouter().showSongViewer(id)
    }

    private fun onComposerClicked(id: Long) {
        hudViewModel.exitSearch()
        getFragmentRouter().showSongListForComposer(id)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
