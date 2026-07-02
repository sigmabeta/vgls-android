package com.vgleadsheets.di

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import kotlin.reflect.KClass
import net.sigmabeta.sage.di.AppScope

/**
 * VGLS's [MetroViewModelFactory] — the `androidx.lifecycle.ViewModelProvider.Factory` that Metro
 * plugs in to back `metroViewModel<T>()` / `assistedMetroViewModel<T, F>()` calls in composables
 * (and `by viewModels { ... }` in the Activity). It receives the three multibinding maps from the
 * graph, populated by `@ContributesIntoMap(AppScope::class)` on each `@ViewModelKey` VM / assisted
 * `Factory`. Contributed as a `MetroViewModelFactory` binding so `ViewModelGraph.metroViewModelFactory`
 * resolves it. Replaces Hilt's `hiltViewModel()` machinery.
 */
private typealias ManualAssistedFactoryProviders =
    Map<KClass<out ManualViewModelAssistedFactory>, () -> ManualViewModelAssistedFactory>

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class VglsMetroViewModelFactory(
    override val viewModelProviders: Map<KClass<out ViewModel>, () -> ViewModel>,
    override val assistedFactoryProviders: Map<KClass<out ViewModel>, () -> ViewModelAssistedFactory>,
    override val manualAssistedFactoryProviders: ManualAssistedFactoryProviders,
) : MetroViewModelFactory()
