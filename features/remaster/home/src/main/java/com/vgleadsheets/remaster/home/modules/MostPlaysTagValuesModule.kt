package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SmallTextListModel
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.model.history.TagValuePlayCount
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class MostPlaysTagValuesModule @Inject constructor(
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.HIGH,
    delayManager,
) {
    override fun loadingType() = LoadingType.SHEET

    override fun title() = stringProvider.getString(StringId.HOME_SECTION_MOST_PLAYS_TAG_VALUES)

    @Suppress("MagicNumber")
    override fun state() = songHistoryRepository
        .getMostPlaysTagValues()
        .map { list ->
            list.filter { it.first.playCount > 4 }
                .take(10)
                .distinctBy { it.second.id }
        }
        .map { pairs ->
            LCE.Content(
                HomeModuleState(
                    moduleName = "MostPlaysTagValuesModule",
                    shouldShow = shouldShow(pairs),
                    title = title(),
                    items = pairs
                        .map { it.second }
                        .map { tagValue ->
                            SmallTextListModel(
                                dataId = tagValue.id,
                                name = "${tagValue.tagKeyName}: ${tagValue.name}",
                                clickAction = Action.MostPlaysTagValueClicked(tagValue.id),
                            )
                        },
                )
            )
        }
        .withLoadingState()
        .withErrorState()

    @Suppress("ReturnCount")
    private fun shouldShow(pairs: List<Pair<TagValuePlayCount, TagValue>>): Boolean {
        return pairs.size >= MINIMUM_ITEMS
    }

    companion object {
        private const val MINIMUM_ITEMS = 5
    }
}
