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
import com.vgleadsheets.conversion.android.converter.SearchHistoryConverter
import com.vgleadsheets.conversion.android.converter.SongAliasConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.SongHistoryConverter
import com.vgleadsheets.conversion.android.converter.SongPlayCountConverter
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.android.converter.TagValuePlayCountConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ConverterModule {
    @Provides
    @Singleton
    fun composerAliasConverter() = ComposerAliasConverter()

    @Provides
    @Singleton
    fun composerComposerConverter() = ComposerConverter()

    @Provides
    @Singleton
    fun composerGameAliasConverter() = GameAliasConverter()

    @Provides
    @Singleton
    fun composerGameConverter() = GameConverter()

    @Provides
    @Singleton
    fun composerSongConverter() = SongConverter()

    @Provides
    @Singleton
    fun composerTagKeyConverter() = TagKeyConverter()

    @Provides
    @Singleton
    fun composerTagValueConverter() = TagValueConverter()

    @Provides
    @Singleton
    fun composerSongAliasConverter() = SongAliasConverter()

    @Provides
    @Singleton
    fun songHistoryConverter() = SongHistoryConverter()

    @Provides
    @Singleton
    fun gamePlayCountConverter() = GamePlayCountConverter()

    @Provides
    @Singleton
    fun composerPlayCountConverter() = ComposerPlayCountConverter()

    @Provides
    @Singleton
    fun tagValuePlayCountConverter() = TagValuePlayCountConverter()

    @Provides
    @Singleton
    fun songPlayCountConverter() = SongPlayCountConverter()

    @Provides
    @Singleton
    fun searchHistoryConverter() = SearchHistoryConverter()

    @Provides
    @Singleton
    fun favoriteSongConverter() = FavoriteSongConverter()

    @Provides
    @Singleton
    fun favoriteGameConverter() = FavoriteGameConverter()

    @Provides
    @Singleton
    fun favoriteComposerConverter() = FavoriteComposerConverter()
}
