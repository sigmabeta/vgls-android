package com.vgleadsheets.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_ViewerAssistedModule::class])
abstract class ViewerAssistedModule
