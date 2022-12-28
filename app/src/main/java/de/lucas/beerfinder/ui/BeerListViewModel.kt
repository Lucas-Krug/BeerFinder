package de.lucas.beerfinder.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.model.BeerController
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerListViewModel @Inject constructor(
    private val beerController: BeerController
) : ViewModel() {

    var beerList by mutableStateOf(listOf<Beer>())
        private set

    fun fetchBeerList(
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            beerList = beerController.fetchBeerList(onLoading, onFinished, onError) ?: listOf()
        }
    }
}