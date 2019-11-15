package com.vgleadsheets.di

import com.vgleadsheets.features.main.composer.ComposerAssistedModule
import com.vgleadsheets.features.main.composers.ComposerListAssistedModule
import com.vgleadsheets.features.main.game.GameAssistedModule
import com.vgleadsheets.features.main.games.GameListAssistedModule
import com.vgleadsheets.features.main.hud.HudAssistedModule
import com.vgleadsheets.features.main.search.SearchAssistedModule
import com.vgleadsheets.features.main.settings.SettingsAssistedModule
import com.vgleadsheets.features.main.songs.SongListAssistedModule
import com.vgleadsheets.features.main.tag_key.TagKeyAssistedModule
import com.vgleadsheets.features.main.tag_songs.TagValueSongListAssistedModule
import com.vgleadsheets.features.main.tag_value_list.TagValueListAssistedModule
import com.vgleadsheets.features.main.viewer.ViewerAssistedModule
import dagger.Module

@Module(
    includes = [
        ComposerListAssistedModule::class,
        ComposerAssistedModule::class,
        GameAssistedModule::class,
        GameListAssistedModule::class,
        SearchAssistedModule::class,
        SettingsAssistedModule::class,
        SongListAssistedModule::class,
        TagKeyAssistedModule::class,
        TagValueSongListAssistedModule::class,
        TagValueListAssistedModule::class,
        ViewerAssistedModule::class,
        HudAssistedModule::class
    ]
)
abstract class AssistedInjectionModules
