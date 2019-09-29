package com.vgleadsheets.features.main.composer

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_composer_list.*
import javax.inject.Inject

class ComposerListFragment : VglsFragment(), NameCaptionListModel.ClickListener {
    @Inject
    lateinit var composerListViewModelFactory: ComposerListViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: ComposerListViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: NameCaptionListModel) {
        showSongList(clicked.dataId)
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
        val selectedPart = hudState.parts?.first { it.selected }

        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        when (val data = composerListState.composers) {
            is Fail -> showError(
                data.error.message ?: data.error::class.simpleName ?: "Unknown Error"
            )
            is Loading -> showLoading()
            is Success -> showComposers(composerListState.composers(), selectedPart)
        }
    }

    override fun getLayoutId() = R.layout.fragment_composer_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun showSongList(clickedComposerId: Long) {
        getFragmentRouter().showSongListForComposer(clickedComposerId)
    }

    private fun showLoading() {
        progress_loading.fadeIn()
        list_composers.fadeOutPartially()
    }

    private fun hideLoading() {
        list_composers.fadeIn()
        progress_loading.fadeOutGone()
    }

    private fun showComposers(composers: List<Composer>?, selectedPart: PartSelectorItem) {
        hideLoading()

        if (composers?.isEmpty() != false) {
            showEmptyState()
            return
        }

        val availableComposers = composers.map { composer ->
            val availableSongs = composer.songs?.filter { song ->
                val parts = song.parts
                val firstAvailableSong = parts?.firstOrNull { part ->
                    part.name == selectedPart.apiId
                }
                firstAvailableSong != null
            }

            composer.copy(songs = availableSongs)
        }.filter {
            it.songs?.isNotEmpty() ?: false
        }

        val listComponents = availableComposers.map {
                NameCaptionListModel(
                    it.id,
                    it.name,
                    getString(R.string.label_sheet_count, it.songs?.size ?: 0),
                    this
                ) as ListModel
            }.toMutableList()

        listComponents.add(
            0,
            TitleListModel(
                R.string.subtitle_composer.toLong(),
                getString(R.string.app_name),
                getString(R.string.subtitle_composer)
            )
        )

        adapter.submitList(listComponents)
    }

    private fun showEmptyState() {
        showError("No data found!")
    }

    companion object {
        fun newInstance() = ComposerListFragment()
    }
}