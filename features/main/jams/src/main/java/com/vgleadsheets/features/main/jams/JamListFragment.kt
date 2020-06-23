package com.vgleadsheets.features.main.jams

import android.annotation.SuppressLint
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
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_jam_list.list_content
import java.util.Locale
import javax.inject.Inject

@Suppress("TooManyFunctions")
class JamListFragment : VglsFragment(), NameCaptionListModel.EventHandler,
    CtaListModel.EventHandler {
    @Inject
    lateinit var jamListViewModelFactory: JamListViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: JamListViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: NameCaptionListModel) {
        getFragmentRouter().showJamDetailViewer(clicked.dataId)
    }

    override fun onClicked(clicked: CtaListModel) {
        getFragmentRouter().showFindJamDialog()
    }

    override fun clearClicked() {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_content.adapter = adapter
        list_content.layoutManager = LinearLayoutManager(context)
        list_content.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() = withState(viewModel) { jamListState ->
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

    private fun constructList(jams: Async<List<Jam>>) = arrayListOf(createTitleListModel()) +
            createContentListModels(jams)

    private fun createTitleListModel() = TitleListModel(
        R.string.title_jams.toLong(),
        getString(R.string.title_jams),
        ""
    )

    private fun createContentListModels(jams: Async<List<Jam>>) = when (jams) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createCtaListModels() + createErrorStateListModel(jams.error)
        is Success -> createCtaListModels() + createSuccessListModels(jams())
    }

    private fun createLoadingListModels(): List<ListModel> {
        val listModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            listModels.add(
                LoadingNameCaptionListModel("allData", index)
            )
        }

        return listModels
    }

    private fun createCtaListModels() = listOf(
        CtaListModel(
            R.drawable.ic_add_black_24dp,
            getString(R.string.cta_find_jam),
            this
        )
    )

    private fun createErrorStateListModel(error: Throwable) =
        arrayListOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createSuccessListModels(jams: List<Jam>) = if (jams.isEmpty()) {
        arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_list_black_24dp,
                "You haven't followed any jams. Click above to search for one."
            )
        )
    } else {
        jams.map {

            NameCaptionListModel(
                it.id,
                it.name.toTitleCase(),
                generateSubtitleText(it.currentSong),
                this
            )
        }
    }

    private fun generateSubtitleText(currentSong: Song) = getString(
        R.string.caption_jam,
        currentSong.name,
        currentSong.gameName
    )

    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("DefaultLocale")
    private fun String.toTitleCase() = this
        .replace("_", " ")
        .split(" ")
        .map {
            if (it != "the") {
                it.capitalize(Locale.getDefault())
            } else {
                it
            }
        }
        .joinToString(" ")

    companion object {
        const val LOADING_ITEMS = 15

        fun newInstance() = JamListFragment()
    }
}
