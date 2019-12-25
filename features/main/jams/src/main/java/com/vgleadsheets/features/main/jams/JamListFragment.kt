package com.vgleadsheets.features.main.jams

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
import com.vgleadsheets.components.NameCaptionCtaListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_jam_list.list_jams
import javax.inject.Inject

class JamListFragment : VglsFragment(), NameCaptionCtaListModel.EventHandler {
    @Inject
    lateinit var jamListViewModelFactory: JamListViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: JamListViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: NameCaptionCtaListModel) = withState(hudViewModel) {
        val activeJamId = it.activeJamId

        if (clicked.dataId != activeJamId) {
            hudViewModel.setActiveJam(clicked.dataId)
        } else {
            showError("Jam already active.")
        }
    }

    override fun onActionClicked(clicked: NameCaptionCtaListModel) {
        showError("CTA action unimplemented.")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_jams.adapter = adapter
        list_jams.layoutManager = LinearLayoutManager(context)
        list_jams.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() = withState(viewModel, hudViewModel) { jamListState, hudState ->
        hudViewModel.dontAlwaysShowBack()

        val jams = jamListState.jams
        if (jams is Fail) {
            showError(jams.error)
        }

        val activeJamId = hudState.activeJamId

        val listModels = constructList(jams, activeJamId)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_jam_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(
        jams: Async<List<Jam>>,
        activeJam: Long?
    ) = arrayListOf(createTitleListModel()) + createContentListModels(jams, activeJam)

    private fun createTitleListModel() = TitleListModel(
        R.string.title_jam.toLong(),
        getString(R.string.title_jam),
        ""
    )

    private fun createContentListModels(
        jams: Async<List<Jam>>,
        activeJam: Long?
    ) = when (jams) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(jams.error)
        is Success -> createSuccessListModels(jams(), activeJam)
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
        jams: List<Jam>,
        activeJam: Long?
    ) = if (jams.isEmpty()) {
        arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No jams found at all. Check your internet connection?"
            )
        )
    } else {
        jams.map {
            val iconId = if (it.id == activeJam) {
                R.drawable.ic_clear_black_24dp
            } else {
                R.drawable.ic_delete_black_24dp
            }

            NameCaptionCtaListModel(
                it.id,
                it.name,
                generateSubtitleText(it.currentSong),
                iconId,
                this
            )
        }
    }

    private fun generateSubtitleText(currentSong: Song) = getString(
        R.string.caption_jam,
        currentSong.name,
        currentSong.gameName
    )

    companion object {
        const val LOADING_ITEMS = 15

        fun newInstance() = JamListFragment()
    }
}
