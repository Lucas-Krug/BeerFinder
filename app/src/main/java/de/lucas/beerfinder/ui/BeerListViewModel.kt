package de.lucas.beerfinder.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.model.BeerController
import de.lucas.beerfinder.model.DatabaseController
import de.lucas.beerfinder.model.Rating
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerListViewModel @Inject constructor(
    private val beerController: BeerController,
    private val dbController: DatabaseController
) :
    ViewModel() {

    var beerList by mutableStateOf(listOf<Beer>())
        private set

    private var page by mutableStateOf(1)

    fun fetchBeerList(
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            if (beerList.isEmpty()) {
                beerList =
                    beerController.fetchBeerList(
                        page = page,
                        onLoading = onLoading,
                        onFinished = { nextPage ->
                            page = nextPage
                            onFinished()
                        },
                        onError = onError
                    )?.map { beer ->
                        beer.copy(rating = dbController.hasRating(beer.id)?.rating ?: 0f)
                    } ?: listOf()
            } else {
                val newBeerLists = beerController.fetchBeerList(
                    page = page,
                    onLoading = onLoading,
                    onFinished = { nextPage ->
                        page = nextPage
                        onFinished()
                    },
                    onError = onError
                )?.map { beer ->
                    beer.copy(rating = dbController.hasRating(beer.id)?.rating ?: 0f)
                } ?: listOf()
                beerList = beerList + newBeerLists
            }
        }
    }

    fun updateListRating(rating: Rating) {
        val updatedBeerList = beerList.map { beer ->
            if (beer.id == rating.id) beer.copy(rating = rating.rating) else beer
        }
        beerList = updatedBeerList.toMutableList()
    }

    fun fetchRandomBeer(
        onLoading: () -> Unit,
        onFinished: (Beer) -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            beerController.fetchRandomBeer(
                onLoading = onLoading,
                onFinished = { beer ->
                    viewModelScope.launch {
                        onFinished(
                            beer.copy(
                                rating = dbController.hasRating(beer.id)?.rating ?: 0f
                            )
                        )
                    }
                },
                onError = onError
            )
        }
    }
}