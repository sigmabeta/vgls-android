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
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_jam_list.list_jams
import javax.inject.Inject

class JamListFragment : VglsFragment(), NameCaptionListModel.EventHandler {
    @Inject
    lateinit var jamListViewModelFactory: JamListViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: JamListViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: NameCaptionListModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun invalidate() = withState(hudViewModel, viewModel) { _, jamListState ->
        hudViewModel.dontAlwaysShowBack()

        val jams = jamListState.jams
        if (jams is Fail) {
            showError(jams.error)
        }

        val listModels = constructList(jams)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_jam_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(
        jams: Async<List<Jam>>
    ) = arrayListOf(createTitleListModel()) + createContentListModels(jams)

    private fun createTitleListModel() = TitleListModel(
        R.string.title_jam.toLong(),
        getString(R.string.title_jam),
        ""
    )

    private fun createContentListModels(
        jams: Async<List<Jam>>
    ) = when (jams) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(jams.error)
        is Success -> createSuccessListModels(jams())
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
        jams: List<Jam>
    ) = if (jams.isEmpty()) {
        arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No jams found at all. Check your internet connection?"
            )
        )
    } else {
        jams.map {
            NameCaptionListModel(
                it.id,
                it.name,
                it.currentSong.name,
                this
            )
        }
    }

    companion object {
        const val LOADING_ITEMS = 15

        fun newInstance() = JamListFragment()
    }
}
