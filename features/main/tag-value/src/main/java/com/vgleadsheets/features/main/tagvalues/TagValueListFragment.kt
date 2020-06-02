package com.vgleadsheets.features.main.tagvalues

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_tag_value_list.list_content
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
class TagValueListFragment : VglsFragment(),
    NameCaptionListModel.EventHandler {

    @Inject
    lateinit var tagValueViewModelFactory: TagValueListViewModel.Factory

    private val viewModel: TagValueListViewModel by fragmentViewModel()

    private val hudViewModel: HudViewModel by existingViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: NameCaptionListModel) {
        showSongsForTagValue(clicked.dataId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_content.adapter = adapter
        list_content.layoutManager = LinearLayoutManager(context)
        list_content.setInsetListenerForPadding(topOffset = topOffset, bottomOffset = bottomOffset)
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, state ->
        hudViewModel.alwaysShowBack()

        val selectedPart = hudState.parts.first { it.selected }

        val listModels = constructList(state.tagKey, state.tagValues, selectedPart)

        listModels.forEach {
            Timber.i("${it.javaClass.simpleName} with id ${it.dataId}")
        }
        adapter.submitList(listModels)
    }

    private fun constructList(
        tagKey: Async<TagKey>,
        tagValues: Async<List<TagValue>>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        if (tagKey !is Success) return emptyList()

        val valueModels = createContentListModels(tagValues, selectedPart)

        // Pass in `songs` so we know whether to show a sheet counter or not.
        val titleListModel =
            arrayListOf(createTitleListModel(tagKey(), tagValues, valueModels.size))

        return titleListModel + valueModels
    }

    private fun createTitleListModel(
        tagKey: TagKey,
        tagValues: Async<List<TagValue>>,
        optionCount: Int
    ) = TitleListModel(
        R.string.title.toLong(),
        tagKey.name,
        generateSubtitleText(tagValues, optionCount)
    )

    private fun generateSubtitleText(
        values: Async<List<TagValue>>,
        optionCount: Int
    ) = if (values is Success) getString(R.string.subtitle_options_count, optionCount) else ""

    private fun createContentListModels(
        tagValues: Async<List<TagValue>>,
        selectedPart: PartSelectorItem
    ) = when (tagValues) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(tagValues.error)
        is Success -> createSuccessListModels(tagValues(), selectedPart)
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

    private fun createSuccessListModels(
        tagValues: List<TagValue>,
        selectedPart: PartSelectorItem
    ) = if (tagValues.isEmpty()) {
        listOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No tag values found at all. Check your internet connection?"
            )
        )
    } else {
        val availableTagValues = filterTagValues(tagValues, selectedPart)

        if (availableTagValues.isEmpty()) {
            listOf(
                EmptyStateListModel(
                    R.drawable.ic_album_24dp,
                    "No songs found with a ${selectedPart.apiId} part. Try another part?"
                )
            )
        } else {
            availableTagValues.map {
                NameCaptionListModel(
                    it.id,
                    it.name,
                    generateSubtitleText(it.songs),
                    this
                )
            }
        }
    }

    private fun filterTagValues(
        tagValues: List<TagValue>,
        selectedPart: PartSelectorItem
    ) = tagValues.map { tagValue ->
        val availableSongs = tagValue.songs?.filter { song ->
            song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
        }

        tagValue.copy(songs = availableSongs)
    }.filter {
        it.songs?.isNotEmpty() ?: false
    }

    private fun generateSubtitleText(items: List<Song>?): String {
        if (items.isNullOrEmpty()) return "Error: no values found."

        val builder = StringBuilder()
        var numberOfOthers = items.size

        while (builder.length < MAX_LENGTH_SUBTITLE_CHARS) {
            val index = items.size - numberOfOthers

            if (index >= MAX_LENGTH_SUBTITLE_ITEMS) {
                break
            }

            if (numberOfOthers == 0) {
                break
            }

            if (index != 0) {
                builder.append(getString(R.string.subtitle_separator))
            }

            val stringToAppend = items[index].name
            builder.append(stringToAppend)
            numberOfOthers--
        }

        if (numberOfOthers != 0) {
            builder.append(getString(R.string.subtitle_suffix_others, numberOfOthers))
        }

        return builder.toString()
    }

    private fun showSongsForTagValue(clickedTagValueId: Long) {
        getFragmentRouter().showSongListForTagValue(clickedTagValueId)
    }

    override fun getLayoutId() = R.layout.fragment_tag_value_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        const val LOADING_ITEMS = 15

        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6

        fun newInstance(idArgs: IdArgs): TagValueListFragment {
            val fragment = TagValueListFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
