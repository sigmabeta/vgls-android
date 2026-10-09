package com.vgleadsheets.scaffold

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.model.SheetColorMode
import com.vgleadsheets.settings.display.SheetColorManager
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import net.sigmabeta.sage.di.AppScope

/**
 * Exposes the persisted sheet color scheme as a hot [StateFlow] for the app root to provide via
 * [com.vgleadsheets.ui.theme.LocalSheetColorMode]. Lives at the shell (not a feature) because every
 * screen that draws a sheet reads the same value.
 */
@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class SheetColorViewModel @Inject constructor(
    sheetColorManager: SheetColorManager,
) : ViewModel() {
    val sheetColorMode: StateFlow<SheetColorMode> = sheetColorManager.sheetColorModeFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, SheetColorMode.LIGHT)
}
