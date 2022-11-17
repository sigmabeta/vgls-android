package com.vgleadsheets.conversion.android.di

import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.android.converter.JamConverter
import com.vgleadsheets.conversion.android.converter.SetlistEntryConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.SongHistoryEntryConverter
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ConverterModule {
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
    fun composerJamConverter() = JamConverter()

    @Provides
    @Singleton
    fun composerSetlistEntryConverter() = SetlistEntryConverter()

    @Provides
    @Singleton
    fun composerSongConverter() = SongConverter()

    @Provides
    @Singleton
    fun composerSongHistoryEntryConverter() = SongHistoryEntryConverter()

    @Provides
    @Singleton
    fun composerTagKeyConverter() = TagKeyConverter()

    @Provides
    @Singleton
    fun composerTagValueConverter() = TagValueConverter()
}
