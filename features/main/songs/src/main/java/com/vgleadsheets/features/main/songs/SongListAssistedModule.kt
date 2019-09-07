package com.vgleadsheets.features.main.songs

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_SongListAssistedModule::class])
abstract class SongListAssistedModule
