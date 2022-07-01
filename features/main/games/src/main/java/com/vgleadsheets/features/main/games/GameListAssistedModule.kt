package com.vgleadsheets.features.main.games

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_GameListAssistedModule::class])
@Suppress("UnnecessaryAbstractClass")
abstract class GameListAssistedModule
