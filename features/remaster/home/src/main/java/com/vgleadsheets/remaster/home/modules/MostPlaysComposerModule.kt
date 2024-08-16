package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.history.ComposerPlayCount
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.map

class MostPlaysComposerModule @Inject constructor(
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
) : HomeModule(
    priority = Priority.MID,
) {
    override fun state() = songHistoryRepository
        .getMostPlaysComposers()
        .map { list ->
            list.filter { it.first.playCount > 1 }
        }
        .map { pairs ->
            LCE.Content(
                HomeModuleState(
                    moduleName = this.javaClass.simpleName,
                    shouldShow = shouldShow(pairs),
                    priority = priority,
                    title = stringProvider.getString(StringId.HOME_SECTION_MOST_PLAYS_COMPOSERS),
                    items = pairs
                        .map { it.second }
                        .map { composer ->
                            SquareItemListModel(
                                dataId = composer.id,
                                name = composer.name,
                                sourceInfo = composer.photoUrl,
                                imagePlaceholder = Icon.ALBUM,
                                clickAction = Action.MostPlaysComposerClicked(composer.id)
                            )
                        }
                )
            )
        }
        .withLoadingState()
        .withErrorState()

    @Suppress("ReturnCount")
    private fun shouldShow(pairs: List<Pair<ComposerPlayCount, Composer>>): Boolean {
        if (pairs.size < MINIMUM_ITEMS) {
            return false
        }

        if (!pairs.areOldEnough()) {
            return false
        }

        return true
    }

    private fun List<Pair<ComposerPlayCount, Composer>>.areOldEnough(): Boolean {
        val currentTime = System.currentTimeMillis()
        return !none {
            (it.first.mostRecentPlay - currentTime) > MINIMUM_AGE_DAYS.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        }
    }

    companion object {
        private const val MINIMUM_ITEMS = 5
        private const val MINIMUM_AGE_DAYS = 3
    }
}
