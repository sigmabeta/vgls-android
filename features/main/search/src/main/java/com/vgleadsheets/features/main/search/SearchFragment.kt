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
import com.vgleadsheets.components.GiantBombImageNameCaptionListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.SearchEmptyStateListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_search.list_results
import java.util.Locale
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SearchFragment : VglsFragment(),
    ImageNameCaptionListModel.EventHandler {
    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: SearchViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    private val gameHandler = object : GiantBombImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: GiantBombImageNameCaptionListModel) =
            onGameClicked(clicked.dataId)

        override fun onGbModelNotChecked(vglsId: Long, name: String, type: String) =
            viewModel.onGbGameNotChecked(vglsId, name)
    }

    private val composerHandler = object : GiantBombImageNameCaptionListModel.EventHandler {
        override fun onClicked(clicked: GiantBombImageNameCaptionListModel) =
            onComposerClicked(clicked.dataId)

        override fun onGbModelNotChecked(vglsId: Long, name: String, type: String) =
            viewModel.onGbComposerNotChecked(vglsId, name)
    }

    override fun onClicked(clicked: ImageNameCaptionListModel) {
        onSongClicked(clicked.dataId)
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

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, localState ->
        hudViewModel.alwaysShowBack()

        val selectedPart = hudState.parts?.first { it.selected }

        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        // TODO is this still necessary?
        // Sanity check - while exiting this screen, we might get an update due
        // to clearing the text box, to which we respond by clearing the text box.
        if (hudState.searchVisible) {
            val query = hudState.searchQuery

            if (query.isNullOrEmpty()) {
                adapter.submitList(
                    arrayListOf<ListModel>(
                        SearchEmptyStateListModel()
                    )
                )
                return@withState
            }

            if (query.toLowerCase(Locale.getDefault()).contains("stickerbr")) {
                throw IllegalArgumentException()
            }

            if (query != localState.query) {
                onSearchQueryEntered(query)
            }

            val listModels = constructList(
                localState.songs,
                localState.games,
                localState.composers,
                selectedPart
            )

            adapter.submitList(listModels)
        }
    }

    private fun onSearchQueryEntered(query: String) {
        tracker.logSearch(query)
        viewModel.startQuery(query)
    }

    private fun constructList(
        songs: Async<List<Song>>,
        games: Async<List<Game>>,
        composers: Async<List<Composer>>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val songModels = createSectionModels(
            R.string.section_header_songs,
            songs,
            selectedPart
        )
        val gameModels = createSectionModels(
            R.string.section_header_games,
            games,
            selectedPart
        )
        val composerModels = createSectionModels(
            R.string.section_header_composers,
            composers,
            selectedPart
        )

        val listModels = songModels + gameModels + composerModels
        return if (listModels.isEmpty()) {
            arrayListOf(
                EmptyStateListModel(
                    R.drawable.ic_description_24dp,
                    getString(R.string.empty_search_no_results)
                )
            )
        } else {
            listModels
        }
    }

    private fun createSectionModels(
        sectionId: Int,
        results: Async<List<Any>>,
        selectedPart: PartSelectorItem
    ) = when (results) {
        is Loading, Uninitialized -> createLoadingListModels(sectionId)
        is Fail -> createErrorStateListModel(results.error)
        is Success -> createSuccessListModels(
            sectionId,
            results(),
            selectedPart
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
        results: List<Any>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        return if (results.isEmpty()) {
            emptyList()
        } else {
            createSectionHeaderListModel(sectionId) + createSectionModels(
                results,
                selectedPart
            )
        }
    }

    private fun createSectionModels(
        results: List<Any>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        return results
            .map {
                when (it) {
                    is Song -> {
                        val thumbUrl = it
                            .parts
                            ?.first { part -> part.name == selectedPart.apiId }
                            ?.pages
                            ?.first()
                            ?.imageUrl

                        ImageNameCaptionListModel(
                            it.id,
                            it.name,
                            generateSheetCaption(it),
                            thumbUrl,
                            getPlaceholderId(it),
                            this
                        )
                    }
                    is Game -> GiantBombImageNameCaptionListModel(
                        it.id,
                        it.giantBombId,
                        it.name,
                        getString(R.string.label_sheet_count, it.songs?.size ?: 0),
                        it.photoUrl,
                        getPlaceholderId(it),
                        gameHandler
                    )
                    is Composer -> GiantBombImageNameCaptionListModel(
                        it.id,
                        it.giantBombId,
                        it.name,
                        getString(R.string.label_sheet_count, it.songs?.size ?: 0),
                        it.photoUrl,
                        getPlaceholderId(it),
                        composerHandler
                    )
                    else -> throw IllegalArgumentException(
                        "Bad model in search result list."
                    )
                }
            }
    }

    private fun createSectionHeaderListModel(sectionId: Int) =
        listOf(SectionHeaderListModel(getString(sectionId)))

    private fun getPlaceholderId(model: Any) = when (model) {
        is Song -> R.drawable.placeholder_sheet
        is Game -> R.drawable.placeholder_game
        is Composer -> R.drawable.placeholder_composer
        else -> R.drawable.ic_error_24dp
    }

    private fun generateSheetCaption(song: Song): String {
        return when (song.composers?.size) {
            1 -> song.composers?.firstOrNull()?.name ?: "Unknown Composer"
            else -> "Various Composers"
        }
    }

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun onGameClicked(id: Long) =
        withState(hudViewModel, viewModel) { hudState, state ->
            val game = state.games()?.first { it.id == id }

            if (game == null) {
                showError("Failed to show game.")
                return@withState
            }

            tracker.logGameView(
                game.name,
                hudState.searchQuery
            )

            hudViewModel.exitSearch()
            getFragmentRouter().showSongListForGame(id)
        }

    private fun onSongClicked(id: Long) =
        withState(hudViewModel, viewModel) { hudState, state ->
            val song = state.songs()?.first { it.id == id }

            if (song == null) {
                showError("Failed to show song.")
                return@withState
            }

            tracker.logSongView(
                song.name,
                song.gameName,
                hudState.parts?.first { it.selected }?.apiId ?: "C",
                hudState.searchQuery
            )

            hudViewModel.exitSearch()
            getFragmentRouter().showSongViewer(id)
        }

    private fun onComposerClicked(id: Long) =
        withState(hudViewModel, viewModel) { hudState, state ->
            val composer = state.composers()?.first { it.id == id }

            if (composer == null) {
                showError("Failed to show composer.")
                return@withState
            }

            tracker.logComposerView(
                composer.name,
                hudState.searchQuery
            )

            hudViewModel.exitSearch()
            getFragmentRouter().showSongListForComposer(id)
        }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
