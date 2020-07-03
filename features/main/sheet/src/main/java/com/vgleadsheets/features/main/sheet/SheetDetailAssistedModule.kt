package com.vgleadsheets.features.main.sheet

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_SheetDetailAssistedModule::class])
abstract class SheetDetailAssistedModule