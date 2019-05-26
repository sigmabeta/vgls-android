package com.vgleadsheets.android.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_GameListAssistedModule::class])
abstract class GameListAssistedModule