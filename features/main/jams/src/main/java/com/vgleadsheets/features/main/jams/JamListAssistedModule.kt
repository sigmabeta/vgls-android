package com.vgleadsheets.features.main.jams

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_JamListAssistedModule::class])
abstract class JamListAssistedModule
