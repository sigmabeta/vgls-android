package com.vgleadsheets.jvm.di

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import kotlin.reflect.KClass
import net.sigmabeta.sage.di.AppScope

private typealias ManualAssistedFactoryProviders =
    Map<KClass<out ManualViewModelAssistedFactory>, () -> ManualViewModelAssistedFactory>

/** JVM-side MetroViewModelFactory backing metroViewModel<T>() in Compose Desktop screens. */
@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class JvmMetroViewModelFactory(
    override val viewModelProviders: Map<KClass<out ViewModel>, () -> ViewModel>,
    override val assistedFactoryProviders: Map<KClass<out ViewModel>, () -> ViewModelAssistedFactory>,
    override val manualAssistedFactoryProviders: ManualAssistedFactoryProviders,
) : MetroViewModelFactory()
