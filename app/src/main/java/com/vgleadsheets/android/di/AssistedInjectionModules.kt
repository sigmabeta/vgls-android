package com.vgleadsheets.android.di

import dagger.Module

@Module(
    includes = [
        GameListAssistedModule::class,
        SheetListAssistedModule::class
    ]
)
abstract class AssistedInjectionModules