package com.vgleadsheets.android.di

import dagger.Module

@Module(
    includes = [
        GameListAssistedModule::class,
        SongListAssistedModule::class,
        ViewerAssistedModule::class
    ]
)
abstract class AssistedInjectionModules