package com.vgleadsheets.features.main.composer

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_ComposerListAssistedModule::class])
abstract class ComposerListAssistedModule
