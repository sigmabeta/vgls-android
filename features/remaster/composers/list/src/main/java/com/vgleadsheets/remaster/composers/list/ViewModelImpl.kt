package com.vgleadsheets.remaster.composers.list

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.Composer
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ViewModelImpl @AssistedInject constructor(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    @Assisted private val navigateTo: (String) -> Unit,
) : VglsViewModel<State, Event>(
    initialState = State(persistentListOf())
) {
    init {
        repository.getAllComposers()
            .onEach(::onComposersLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun onComposersLoaded(composers: List<Composer>) {
        _uiState.update {
            it.copy(
                listItems = composers
                    .map { composer ->
                        WideItemListModel(
                            dataId = composer.id,
                            name = composer.name,
                            imageUrl = composer.photoUrl,
                            imagePlaceholder = com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
                            onClick = { navigateTo("composers/${composer.id}") }
                        )
                    }
                    .toPersistentList()
            )
        }
    }
}
