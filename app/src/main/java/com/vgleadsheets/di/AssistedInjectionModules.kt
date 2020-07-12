package com.vgleadsheets.di

import com.vgleadsheets.features.main.composer.ComposerAssistedModule
import com.vgleadsheets.features.main.composers.ComposerListAssistedModule
import com.vgleadsheets.features.main.debug.DebugAssistedModule
import com.vgleadsheets.features.main.game.GameAssistedModule
import com.vgleadsheets.features.main.games.GameListAssistedModule
import com.vgleadsheets.features.main.hud.HudAssistedModule
import com.vgleadsheets.features.main.jam.JamAssistedModule
import com.vgleadsheets.features.main.jams.JamListAssistedModule
import com.vgleadsheets.features.main.search.SearchAssistedModule
import com.vgleadsheets.features.main.settings.SettingsAssistedModule
import com.vgleadsheets.features.main.sheet.SheetDetailAssistedModule
import com.vgleadsheets.features.main.songs.SongListAssistedModule
import com.vgleadsheets.features.main.tagkeys.TagKeyAssistedModule
import com.vgleadsheets.features.main.tagsongs.TagValueSongListAssistedModule
import com.vgleadsheets.features.main.tagvalues.TagValueListAssistedModule
import com.vgleadsheets.features.main.viewer.ViewerAssistedModule
import dagger.Module

@Module(
    includes = [
        ComposerListAssistedModule::class,
        ComposerAssistedModule::class,
        DebugAssistedModule::class,
        GameAssistedModule::class,
        GameListAssistedModule::class,
        JamAssistedModule::class,
        JamListAssistedModule::class,
        SearchAssistedModule::class,
        SettingsAssistedModule::class,
        SongListAssistedModule::class,
        TagKeyAssistedModule::class,
        TagValueSongListAssistedModule::class,
        TagValueListAssistedModule::class,
        SheetDetailAssistedModule::class,
        ViewerAssistedModule::class,
        HudAssistedModule::class
    ]
)
abstract class AssistedInjectionModules
