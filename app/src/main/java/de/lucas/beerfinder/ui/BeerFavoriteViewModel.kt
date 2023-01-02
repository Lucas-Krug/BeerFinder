package de.lucas.beerfinder.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.model.DatabaseController
import de.lucas.beerfinder.model.Rating
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

    fun updateListRating(rating: Rating) {
        val updatedBeerList = favoriteBeers.map { beer ->
            if (beer.id == rating.id) beer.copy(rating = rating.rating) else beer
        }
        favoriteBeers = updatedBeerList
    }
}