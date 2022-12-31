package de.lucas.beerfinder.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.model.DatabaseController
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerFavoriteViewModel @Inject constructor(private val dbController: DatabaseController) :
    ViewModel() {

    var favoriteBeers by mutableStateOf(listOf<Beer>())
        private set

    fun getFavorites() {
        viewModelScope.launch {
            favoriteBeers = dbController.getFavoriteBeers()
        }
    }
}