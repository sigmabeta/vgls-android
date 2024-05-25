package com.vgleadsheets.topbar

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vgleadsheets.appcomm.EventDispatcher
import dagger.assisted.AssistedFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@AssistedFactory
internal interface Factory {
    fun create(
        eventDispatcher: EventDispatcher,
    ): TopBarViewModel
}

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface Provider {
    fun topBarViewModelFactory(): Factory
}

@Suppress("UNCHECKED_CAST")
internal fun provideFactory(
    assistedFactory: Factory,
    eventDispatcher: EventDispatcher,
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(
            eventDispatcher,
        ) as T
    }
}

@Composable
fun topBarViewModel(
    eventDispatcher: EventDispatcher,
): TopBarViewModel {
    val activity = LocalContext.current as Activity
    val entryPoint = EntryPointAccessors.fromActivity(activity, Provider::class.java)
    val factory = entryPoint.topBarViewModelFactory()

    return viewModel(
        factory = provideFactory(
            factory,
            eventDispatcher,
        )
    )
}
