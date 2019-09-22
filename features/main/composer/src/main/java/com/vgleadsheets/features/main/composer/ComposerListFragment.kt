package com.vgleadsheets.features.main.composer

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
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.repository.Data
import com.vgleadsheets.repository.Empty
import com.vgleadsheets.repository.Error
import com.vgleadsheets.repository.Network
import com.vgleadsheets.repository.Storage
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_composer_list.*
import javax.inject.Inject

class ComposerListFragment : VglsFragment() {
    @Inject
    lateinit var composerListViewModelFactory: ComposerListViewModel.Factory

    private val hudViewModel: HudViewModel by activityViewModel()

    private val viewModel: ComposerListViewModel by fragmentViewModel()

    private val adapter = ComposerListAdapter(this)

    fun onItemClick(id: Long) {
        showSongList(id)
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

        when (val data = composerListState.data) {
            is Fail -> showError(
                data.error.message ?: data.error::class.simpleName ?: "Unknown Error"
            )
            is Loading -> showLoading()
            is Success -> showData(composerListState.data(), selectedPart)
        }
    }

    override fun getLayoutId() = R.layout.fragment_composer_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun showSongList(clickedComposerId: Long) {
        getFragmentRouter().showSongListForComposer(clickedComposerId)
    }

    private fun showLoading() {
        progress_loading.fadeInFromZero()
        list_composers.fadeOutPartially()
    }

    private fun hideLoading() {
        list_composers.fadeIn()
        progress_loading.fadeOutGone()
    }

    private fun showData(data: Data<List<Composer>>?, selectedPart: PartSelectorItem) {
        when (data) {
            is Empty -> showLoading()
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Network -> hideLoading()
            is Storage -> showComposers(data(), selectedPart)
        }
    }

    private fun showComposers(composers: List<Composer>, selectedPart: PartSelectorItem) {
        hideLoading()
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

        adapter.dataset = availableComposers
    }

    companion object {
        fun newInstance() = ComposerListFragment()
    }
}
