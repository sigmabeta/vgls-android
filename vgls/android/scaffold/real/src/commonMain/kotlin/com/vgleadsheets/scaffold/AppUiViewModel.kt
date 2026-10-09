package com.vgleadsheets.scaffold

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.model.SheetColorMode
import com.vgleadsheets.model.ThemeMode
import com.vgleadsheets.settings.display.SheetColorManager
import com.vgleadsheets.settings.display.ThemeManager
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import net.sigmabeta.sage.di.AppScope

/**
 * Exposes the shell-wide display settings as hot [StateFlow]s for the app root to apply: the app
 * theme drives `AppTheme`, and the sheet color scheme is provided via
 * [com.vgleadsheets.ui.theme.LocalSheetColorMode]. Lives at the shell (not a feature) because every
 * screen reads them.
 */
@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class AppUiViewModel @Inject constructor(
    sheetColorManager: SheetColorManager,
    themeManager: ThemeManager,
) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = themeManager.themeModeFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.DEFAULT)

    val sheetColorMode: StateFlow<SheetColorMode> = sheetColorManager.sheetColorModeFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, SheetColorMode.LIGHT)
}
