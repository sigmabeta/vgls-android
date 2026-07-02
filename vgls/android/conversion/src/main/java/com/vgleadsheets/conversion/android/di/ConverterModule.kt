package com.vgleadsheets.conversion.android.di

import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.ComposerPlayCountConverter
import com.vgleadsheets.conversion.android.converter.FavoriteComposerConverter
import com.vgleadsheets.conversion.android.converter.FavoriteGameConverter
import com.vgleadsheets.conversion.android.converter.FavoriteSongConverter
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.android.converter.GamePlayCountConverter
import com.vgleadsheets.conversion.android.converter.OfflineComposerConverter
import com.vgleadsheets.conversion.android.converter.OfflineGameConverter
import com.vgleadsheets.conversion.android.converter.OfflineSongConverter
import com.vgleadsheets.conversion.android.converter.OfflineUpdateResultConverter
import com.vgleadsheets.conversion.android.converter.SearchHistoryConverter
import com.vgleadsheets.conversion.android.converter.SongAliasConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.SongHistoryConverter
import com.vgleadsheets.conversion.android.converter.SongPlayCountConverter
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.android.converter.TagValuePlayCountConverter
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object ConverterModule {
    @Provides
    @SingleIn(AppScope::class)
    fun composerAliasConverter(): ComposerAliasConverter = ComposerAliasConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun composerComposerConverter(): ComposerConverter = ComposerConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun composerGameAliasConverter(): GameAliasConverter = GameAliasConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun composerGameConverter(): GameConverter = GameConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun composerSongConverter(): SongConverter = SongConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun composerTagKeyConverter(): TagKeyConverter = TagKeyConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun composerTagValueConverter(): TagValueConverter = TagValueConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun composerSongAliasConverter(): SongAliasConverter = SongAliasConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun songHistoryConverter(): SongHistoryConverter = SongHistoryConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun gamePlayCountConverter(): GamePlayCountConverter = GamePlayCountConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun composerPlayCountConverter(): ComposerPlayCountConverter = ComposerPlayCountConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun tagValuePlayCountConverter(): TagValuePlayCountConverter = TagValuePlayCountConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun songPlayCountConverter(): SongPlayCountConverter = SongPlayCountConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun searchHistoryConverter(): SearchHistoryConverter = SearchHistoryConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun favoriteSongConverter(): FavoriteSongConverter = FavoriteSongConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun favoriteGameConverter(): FavoriteGameConverter = FavoriteGameConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun favoriteComposerConverter(): FavoriteComposerConverter = FavoriteComposerConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun offlineSongConverter(): OfflineSongConverter = OfflineSongConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun offlineComposerConverter(): OfflineComposerConverter = OfflineComposerConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun offlineGameConverter(): OfflineGameConverter = OfflineGameConverter()

    @Provides
    @SingleIn(AppScope::class)
    fun offlineUpdateResultConverter(): OfflineUpdateResultConverter = OfflineUpdateResultConverter()
}
