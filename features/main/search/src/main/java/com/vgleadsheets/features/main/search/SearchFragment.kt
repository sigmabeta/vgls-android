package com.vgleadsheets.features.main.search

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.SearchEmptyStateListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_search.list_results
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
            // TODO is this still necessary?
            // Sanity check - while exiting this screen, we might get an update due
            // to clearing the text box, to which we respond by clearing the text box.
            if (hudState.searchVisible) {
                val query = hudState.searchQuery
                if (!query.isNullOrEmpty()) {
                    viewModel.startQuery(query)
                } else {
                    adapter.submitList(
                        arrayListOf<ListModel>(
                            SearchEmptyStateListModel()
                        )
                    )
                    return@withState
                }

                val listModels = constructList(localState.songs, localState.games, localState.composers)
                adapter.submitList(listModels)
            }
        }
    }

    private fun constructList(
        songs: Async<List<SearchResult>>,
        games: Async<List<SearchResult>>,
        composers: Async<List<SearchResult>>
    ): List<ListModel> {
        val songModels = createSectionModels(
            R.string.section_header_songs,
            R.string.label_type_song,
            songs
        )
        val gameModels = createSectionModels(
            R.string.section_header_games,
            R.string.label_type_game,
            games
        )
        val composerModels = createSectionModels(
            R.string.section_header_composers,
            R.string.label_type_composer,
            composers
        )

        val listModels = songModels + gameModels + composerModels
        return if (listModels.isEmpty()) {
            arrayListOf(EmptyStateListModel(
                R.drawable.ic_description_black_24dp,
                getString(R.string.empty_search_no_results))
            )
        } else {
            listModels
        }
    }

    private fun createSectionModels(
        sectionId: Int,
        labelId: Int,
        games: Async<List<SearchResult>>
    ) = when (games) {
        is Loading, Uninitialized -> createLoadingListModels(sectionId)
        is Fail -> createErrorStateListModel(games.error)
        is Success -> createSuccessListModels(
            sectionId,
            labelId,
            games()
        )
    }

    private fun createErrorStateListModel(error: Throwable) =
        arrayListOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createLoadingListModels(sectionId: Int) = arrayListOf(
        SectionHeaderListModel(getString(sectionId)),
        LoadingNameCaptionListModel(sectionId)
    )

    private fun createSuccessListModels(
        sectionId: Int,
        resultTypeId: Int,
        results: List<SearchResult>
    ): List<ListModel> {
        return if (results.isEmpty()) {
            emptyList()
        } else {
            results
                .map {
                    NameCaptionListModel(
                        it.id,
                        it.name,
                        getString(resultTypeId),
                        this,
                        it.type.toString()
                    ) as ListModel
                }
                .toMutableList()
                .apply {
                    add(0, SectionHeaderListModel(getString(sectionId)))
                }
        }
    }

    override fun getVglsFragmentTag() = this.javaClass.simpleName

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
