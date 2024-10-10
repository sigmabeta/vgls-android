package com.vgleadsheets.viewmodel.list

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vgleadsheets.nav.Destination
import dagger.assisted.AssistedFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@AssistedFactory
internal interface Factory {
    fun create(
        destination: Destination,
        idArg: Long,
        stringArg: String?,
    ): ListViewModel
}

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface Provider {
    fun listViewModelFactory(): Factory
}

@Suppress("UNCHECKED_CAST")
internal fun provideFactory(
    assistedFactory: Factory,
    destination: Destination,
    idArg: Long,
    stringArg: String?,
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(
            destination,
            idArg,
            stringArg,
        ) as T
    }
}

@Composable
fun listViewModel(
    destination: Destination,
    idArg: Long,
    stringArg: String?,
): ListViewModel {
    val activity = LocalContext.current as Activity
    val entryPoint = EntryPointAccessors.fromActivity(activity, Provider::class.java)
    val factory = entryPoint.listViewModelFactory()

    return viewModel(
        factory = provideFactory(
            factory,
            destination,
            idArg,
            stringArg,
        )
    )
}
