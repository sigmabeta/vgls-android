package com.vgleadsheets.features.main.viewer

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_ViewerAssistedModule::class])
abstract class ViewerAssistedModule
