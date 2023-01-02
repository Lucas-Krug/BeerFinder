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
class BeerDetailsViewModel @Inject constructor(private val dbController: DatabaseController) :
    ViewModel() {

    var isFavorite by mutableStateOf(false)

    fun addFavorite(beer: Beer) {
        viewModelScope.launch {
            dbController.addBeer(beer)
        }
    }

    fun removeFavorite(id: Int) {
        viewModelScope.launch {
            dbController.removeBeer(id)
        }
    }

    fun isFavorite(id: Int) {
        viewModelScope.launch {
            isFavorite = dbController.isBeerFavorite(id)
        }
    }

    fun setRating(rating: Rating, beer: Beer) {
        viewModelScope.launch {
            dbController.setRating(rating)
            if (isFavorite) dbController.addBeer(beer.copy(rating = rating.rating))
        }
    }
}