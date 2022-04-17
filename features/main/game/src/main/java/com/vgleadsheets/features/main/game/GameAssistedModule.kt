package com.vgleadsheets.features.main.game

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_GameAssistedModule::class])
@Suppress("UnnecessaryAbstractClass")
abstract class GameAssistedModule
