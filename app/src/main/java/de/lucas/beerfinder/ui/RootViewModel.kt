package de.lucas.beerfinder.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(LoadingState.LOADING)
    var showTabBar by mutableStateOf(true)
}

enum class LoadingState {
    FINISHED,
    LOADING,
    ERROR
}