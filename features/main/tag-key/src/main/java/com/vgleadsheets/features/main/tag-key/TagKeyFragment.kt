package com.vgleadsheets.features.main.tag_key

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
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_tag_key.list_tag_key
import javax.inject.Inject

class TagKeyFragment : VglsFragment(), NameCaptionListModel.EventHandler {
    @Inject
    lateinit var tagKeyViewModelFactory: TagKeyViewModel.Factory

    private val viewModel: TagKeyViewModel by fragmentViewModel()

    private val hudViewModel: HudViewModel by existingViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: NameCaptionListModel) {
        showError("Unimplemented.")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_tag_key.adapter = adapter
        list_tag_key.layoutManager = LinearLayoutManager(context)
        list_tag_key.setInsetListenerForPadding(topOffset = topOffset, bottomOffset = bottomOffset)
    }

    override fun invalidate() = withState(viewModel) { state ->
        hudViewModel.dontAlwaysShowBack()

        val listModels = constructList(state.tags)
        adapter.submitList(listModels)
    }

    private fun constructList(tags: Async<List<TagKey>>) =
        listOf(createTitleListModel()) + createContentListModels(tags)

    private fun createTitleListModel() = TitleListModel(
        R.string.subtitle_tags.toLong(),
        getString(R.string.app_name),
        getString(R.string.subtitle_tags)
    )

    private fun createContentListModels(tags: Async<List<TagKey>>) = when (tags) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(tags.error)
        is Success -> createSuccessListModels(tags())
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
        listOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createSuccessListModels(tags: List<TagKey>) = if (tags.isEmpty()) {
        arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No tags found at all. Check your internet connection?"
            )
        )
    } else {
        tags.map {
            NameCaptionListModel(
                it.id,
                it.name,
                "Show the first few values here",
                this
            )
        }
    }

    override fun getLayoutId() = R.layout.fragment_tag_key

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        const val LOADING_ITEMS = 15
        fun newInstance() = TagKeyFragment()
    }
}
