package com.vgleadsheets.features.main.composers

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
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_composer_list.list_composers
import javax.inject.Inject

@Suppress("TooManyFunctions")
class ComposerListFragment : VglsFragment(), GiantBombImageNameCaptionListModel.EventHandler {
    @Inject
    lateinit var composerListViewModelFactory: ComposerListViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: ComposerListViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: GiantBombImageNameCaptionListModel) {
        showSongList(clicked.dataId)
    }

    override fun onGbModelNotChecked(vglsId: Long, name: String, type: String) {
        viewModel.onGbComposerNotChecked(vglsId, name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_composers.adapter = adapter
        list_composers.layoutManager = LinearLayoutManager(context)
        list_composers.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, composerListState ->
        hudViewModel.dontAlwaysShowBack()
        val selectedPart = hudState.parts?.first { it.selected }

        // TODO Let's make this non-null if we can.
        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        val composers = composerListState.composers
        if (composers is Fail) {
            showError(composers.error)
        }

        val listModels = constructList(composers, selectedPart)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_composer_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(
        composers: Async<List<Composer>>,
        selectedPart: PartSelectorItem
    ) = arrayListOf(createTitleListModel()) + createContentListModels(composers, selectedPart)

    private fun createTitleListModel() = TitleListModel(
        R.string.subtitle_composer.toLong(),
        getString(R.string.app_name),
        getString(R.string.subtitle_composer)
    )

    private fun createContentListModels(
        composers: Async<List<Composer>>,
        selectedPart: PartSelectorItem
    ) = when (composers) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(composers.error)
        is Success -> createSuccessListModels(composers(), selectedPart)
    }

    private fun createLoadingListModels(): List<ListModel> {
        val listModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            listModels.add(
                LoadingNameCaptionListModel(index)
            )
        }

        return listModels
    }

    private fun createErrorStateListModel(error: Throwable) =
        arrayListOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createSuccessListModels(
        composers: List<Composer>,
        selectedPart: PartSelectorItem
    ) = if (composers.isEmpty()) {
        arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No composers found at all. Check your internet connection?"
            )
        )
    } else {
        val availableComposers = filterComposers(composers, selectedPart)

        if (availableComposers.isEmpty()) arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No composers found with a ${selectedPart.apiId} part. Try another part?"
            )
        ) else availableComposers
            .map {
                GiantBombImageNameCaptionListModel(
                    it.id,
                    it.giantBombId,
                    it.name,
                    getString(R.string.label_sheet_count, it.songs?.size ?: 0),
                    it.photoUrl,
                    R.drawable.placeholder_composer,
                    this
                )
            }
    }

    private fun filterComposers(
        composers: List<Composer>,
        selectedPart: PartSelectorItem
    ) = composers.map { composer ->
        val availableSongs = composer.songs?.filter { song ->
            song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
        }

        composer.copy(songs = availableSongs)
    }.filter {
        it.songs?.isNotEmpty() ?: false
    }

    private fun showSongList(clickedComposerId: Long) {
        getFragmentRouter().showSongListForComposer(clickedComposerId)
    }

    companion object {
        const val LOADING_ITEMS = 15
        fun newInstance() = ComposerListFragment()
    }
}
