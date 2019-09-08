package com.vgleadsheets.di

import com.vgleadsheets.features.main.games.GameListAssistedModule
import com.vgleadsheets.features.main.search.SearchAssistedModule
import com.vgleadsheets.features.main.songs.SongListAssistedModule
import com.vgleadsheets.features.main.viewer.ViewerAssistedModule
import dagger.Module

@Module(
    includes = [
        GameListAssistedModule::class,
        SearchAssistedModule::class,
        SongListAssistedModule::class,
        ViewerAssistedModule::class
    ]
)
abstract class AssistedInjectionModules
