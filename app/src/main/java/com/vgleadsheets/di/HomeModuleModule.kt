package com.vgleadsheets.di

import com.vgleadsheets.remaster.home.HomeModuleProvider
import com.vgleadsheets.remaster.home.modules.MostPlaysComposerModule
import com.vgleadsheets.remaster.home.modules.MostPlaysGamesModule
import com.vgleadsheets.remaster.home.modules.MostPlaysSongsModule
import com.vgleadsheets.remaster.home.modules.MostSongsComposersModule
import com.vgleadsheets.remaster.home.modules.MostSongsGamesModule
import com.vgleadsheets.remaster.home.modules.NotifModule
import com.vgleadsheets.remaster.home.modules.RecentSongsModule
import com.vgleadsheets.remaster.home.modules.RngModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class HomeModuleModule {
    @Provides
    @ActivityScoped
    fun provideHomeModuleProviderLol(
        notifModule: NotifModule,
        mostSongsGamesModule: MostSongsGamesModule,
        mostSongsComposersModule: MostSongsComposersModule,
        mostPlaysGamesModule: MostPlaysGamesModule,
        mostPlaysComposerModule: MostPlaysComposerModule,
        mostPlaysSongsModule: MostPlaysSongsModule,
        recentSongsModule: RecentSongsModule,
        rngModule: RngModule,
    ): HomeModuleProvider = object : HomeModuleProvider {
        override val modules by lazy {
            val list = listOf(
                notifModule,
                mostSongsGamesModule,
                mostSongsComposersModule,
                recentSongsModule,
                mostPlaysSongsModule,
                mostPlaysGamesModule,
                mostPlaysComposerModule,
                rngModule,
            )

            list.forEach { it.setup() }
            list
        }
    }
}