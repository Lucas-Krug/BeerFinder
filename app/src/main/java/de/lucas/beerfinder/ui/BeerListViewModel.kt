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

    var beerList by mutableStateOf(mutableListOf<Beer>())
        private set

    var page by mutableStateOf(1)
        private set

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
                    )?.toMutableList() ?: mutableListOf()
            } else {
                val newBeerList = beerController.fetchBeerList(
                    page = page,
                    onLoading = onLoading,
                    onFinished = { nextPage ->
                        page = nextPage
                        onFinished()
                    },
                    onError = onError
                )?.toMutableList() ?: mutableListOf()
                beerList.addAll(newBeerList)
            }
        }
    }

    fun fetchRandomBeer(onFinished: (Beer) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            beerController.fetchRandomBeer(
                onFinished = { beer -> onFinished(beer) },
                onError = onError
            )
        }
    }
}