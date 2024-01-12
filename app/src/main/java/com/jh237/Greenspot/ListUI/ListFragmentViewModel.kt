package com.jh237.Greenspot.ListUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jh237.Greenspot.database.Spot
import com.jh237.Greenspot.database.SpotRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
This class is a ViewModel. It is used to manage the state of the UI. As a ViewModel, it persists through configuration changes by caching its state.

It communicates with the database through the SpotRepo.
 */

class ListFragmentViewModel : ViewModel() {

    //spotRepo is given the value of SpotRepo.get() which returns the current instance of the database.

    private val spotRepo = SpotRepo.get()

    //_spots is a MutableStateFlow of type List<Spot>. It is initialized with an empty list.

    private val _spots: MutableStateFlow<List<Spot>> = MutableStateFlow(emptyList())

    //spots is a StateFlow of type List<Spot> that is initialized with the value of _spots.

    val spots: StateFlow<List<Spot>>
        get() = _spots.asStateFlow()

    //init is used to initialize the _spots flow with the value of spotRepo.getSpots().

    init {
        viewModelScope.launch { spotRepo.getSpots().collect { _spots.value = it } }
    }

    suspend fun loadSpots(): Flow<List<Spot>> {
        return spotRepo.getSpots()
    }

    //addSpot is used to add a new spot to the database.

    suspend fun addSpot(spot: Spot) {
        spotRepo.addSpot(spot)
    }
}