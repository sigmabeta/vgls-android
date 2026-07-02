package com.vgleadsheets.di

import com.vgleadsheets.remaster.home.HomeModuleProvider
import com.vgleadsheets.remaster.home.modules.MostPlaysComposerModule
import com.vgleadsheets.remaster.home.modules.MostPlaysGamesModule
import com.vgleadsheets.remaster.home.modules.MostPlaysSongsModule
import com.vgleadsheets.remaster.home.modules.MostPlaysTagValuesModule
import com.vgleadsheets.remaster.home.modules.MostSongsComposersModule
import com.vgleadsheets.remaster.home.modules.MostSongsGamesModule
import com.vgleadsheets.remaster.home.modules.NeverPlayedSongModule
import com.vgleadsheets.remaster.home.modules.NotifModule
import com.vgleadsheets.remaster.home.modules.RecentSongsModule
import com.vgleadsheets.remaster.home.modules.RngModule
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object HomeModuleModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideHomeModuleProviderLol(
        notifModule: NotifModule,
        mostSongsGamesModule: MostSongsGamesModule,
        mostSongsComposersModule: MostSongsComposersModule,
        neverPlayedSongModule: NeverPlayedSongModule,
        mostPlaysTagValuesModule: MostPlaysTagValuesModule,
        mostPlaysGamesModule: MostPlaysGamesModule,
        mostPlaysComposerModule: MostPlaysComposerModule,
        mostPlaysSongsModule: MostPlaysSongsModule,
        recentSongsModule: RecentSongsModule,
        rngModule: RngModule,
        dispatchers: SageDispatchers,
        coroutineScope: CoroutineScope,
    ): HomeModuleProvider = object : HomeModuleProvider {
        override val modules by lazy {
            val list = listOf(
                notifModule,
                neverPlayedSongModule,
                mostSongsGamesModule,
                mostSongsComposersModule,
                recentSongsModule,
                mostPlaysTagValuesModule,
                mostPlaysSongsModule,
                mostPlaysGamesModule,
                mostPlaysComposerModule,
                rngModule,
            )

            list.forEach {
                it.setup()
                    .flowOn(dispatchers.disk)
                    .launchIn(coroutineScope)
            }
            list
        }
    }
}
