package com.vgleadsheets.features.main.jams

import android.annotation.SuppressLint
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.ListViewModel
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider
import java.util.Locale

class JamListViewModel @AssistedInject constructor(
    @Assisted initialState: JamListState,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : ListViewModel<Jam, JamListState>(initialState, resourceProvider),
    CtaListModel.EventHandler,
    NameCaptionListModel.EventHandler {
    init {
        fetchJams()
    }

    override val showDefaultEmptyState = false

    override fun onClicked(clicked: NameCaptionListModel) = setState {
        copy(
            clickedJamModel = clicked
        )
    }

    override fun clearClicked() = setState {
        copy(
            clickedJamModel = null,
            clickedCtaModel = null
        )
    }

    override fun onClicked(clicked: CtaListModel) = setState {
        copy(
            clickedCtaModel = clicked
        )
    }

    override fun createTitleListModel() = TitleListModel(
        resourceProvider.getString(R.string.title_jams),
        ""
    )

    override fun defaultLoadingListModel(index: Int): ListModel =
        LoadingNameCaptionListModel("allData", index)

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_list_black_24dp,
        "Unknown error occurred."
    )

    override fun createSuccessListModels(
        data: List<Jam>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ) = createCtaListModels() + if (data.isEmpty()) {
        arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_list_black_24dp,
                "You haven't followed any jams. Click above to search for one."
            )
        )
    } else {
        data.map {
            NameCaptionListModel(
                it.id,
                it.name.toTitleCase(),
                generateSubtitleText(it.currentSong),
                this
            )
        }
    }

    private fun fetchJams() = repository
        .getJams()
        .execute { jams ->
            copy(
                data = jams,
                listModels = constructList(
                    jams,
                    updateTime,
                    digest,
                    selectedPart
                )
            )
        }

    private fun createCtaListModels() = listOf(
        CtaListModel(
            R.drawable.ic_add_black_24dp,
            resourceProvider.getString(R.string.cta_find_jam),
            this
        )
    )

    private fun generateSubtitleText(currentSong: Song?) = resourceProvider.getString(
        R.string.caption_jam,
        currentSong?.name ?: "Unknown Song",
        currentSong?.gameName ?: "Unknown Game"
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

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: JamListState): JamListViewModel
    }

    companion object : MvRxViewModelFactory<JamListViewModel, JamListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: JamListState
        ): JamListViewModel? {
            val fragment: JamListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.jamListViewModelFactory.create(state)
        }
    }
}
