package com.vgleadsheets.viewmodel.list

import androidx.compose.runtime.Composable
import com.vgleadsheets.nav.Destination
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

/**
 * Obtains a [ListViewModel] for the given nav args via Metro's assisted-VM support. Replaces the
 * former Hilt `@EntryPoint` + Dagger `@AssistedFactory` plumbing; the factory now lives inside
 * [ListViewModel.Factory] and is resolved through the `LocalMetroViewModelFactory` the Activity
 * installs in composition.
 */
@Composable
fun listViewModel(
    destination: Destination,
    idArg: Long,
    stringArg: String?,
): ListViewModel = assistedMetroViewModel<ListViewModel, ListViewModel.Factory> {
    create(destination, idArg, stringArg)
}
