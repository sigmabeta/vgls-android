package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.history.ComposerPlayCount
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.strings.VglsStringId
import kotlinx.coroutines.flow.map
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.GridImageListModel
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.time.TimeUtils
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import dev.zacsweers.metro.Inject

class MostPlaysComposerModule @Inject constructor(
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.MID,
    delayManager,
) {
    override fun loadingType() = LoadingType.SQUARE

    override fun title() = stringProvider.getString(VglsStringId.HOME_SECTION_MOST_PLAYS_COMPOSERS)

    override fun state() = songHistoryRepository
        .getMostPlaysComposers()
        .map { list ->
            list.filter { it.first.playCount > 1 }
                .shuffled()
                .distinctBy { it.second.id }
        }
        .map { pairs ->
            LCE.Content(
                HomeModuleState(
                    moduleName = "MostPlaysComposerModule",
                    shouldShow = shouldShow(pairs),
                    title = title(),
                    items = pairs
                        .map { it.second }
                        .map { composer ->
                            GridImageListModel(
                                dataId = composer.id,
                                name = composer.name,
                                sourceInfo = composer.photoUrl,
                                imagePlaceholder = Icon.Album,
                                clickAction = Action.MostPlaysComposerClicked(composer.id)
                            )
                        },
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

    private fun List<Pair<ComposerPlayCount, Composer>>.areOldEnough() = none {
        val recordAge = TimeUtils.calculateAgeOf(Instant.ofEpochMilli(it.first.mostRecentPlay))
        val minimumAge = Duration.ofDays(MINIMUM_AGE_DAYS)
        recordAge < minimumAge
    }

    companion object {
        private const val MINIMUM_ITEMS = 5
        private const val MINIMUM_AGE_DAYS = 3L
    }
}
