package com.jh237.Greenspot.ItemUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jh237.Greenspot.database.Spot
import com.jh237.Greenspot.database.SpotRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class SpotDetailViewModel(spotId: UUID) : ViewModel() {

    private val spotRepo = SpotRepo.get()

    private val _spot: MutableStateFlow<Spot?> = MutableStateFlow(null)

    val spot: StateFlow<Spot?> = _spot.asStateFlow()

    init {
        viewModelScope.launch {
            _spot.value = spotRepo.getSpot(spotId)
        }
    }

    fun updateSpot(onUpdate: (Spot) -> Spot) {
        _spot.update { oldSpot ->
            oldSpot?.let { onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        spot.value?.let { spotRepo.updateSpot(it) }
    }

}

class SpotDetailViewModelFactory(private val spotId: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SpotDetailViewModel(spotId) as T
    }
}
