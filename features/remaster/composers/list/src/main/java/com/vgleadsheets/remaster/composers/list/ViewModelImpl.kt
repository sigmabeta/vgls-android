package com.vgleadsheets.remaster.composers.list

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.Composer
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ViewModelImpl @AssistedInject constructor(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    @Assisted private val navigateTo: (String) -> Unit,
) : VglsViewModel<State, Event>() {
    init {
        repository.getAllComposers()
            .onEach(::onComposersLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    override fun initialState() = State()

    private fun onComposersLoaded(composers: List<Composer>) {
        internalUiState.update {
            it.copy(
                composers = composers
            )
        }
    }
}
