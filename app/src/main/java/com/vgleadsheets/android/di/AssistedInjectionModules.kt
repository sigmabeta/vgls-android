package com.vgleadsheets.di

import dagger.Module

@Module(
    includes = [
        GameListAssistedModule::class,
        SongListAssistedModule::class,
        ViewerAssistedModule::class
    ]
)
abstract class AssistedInjectionModules