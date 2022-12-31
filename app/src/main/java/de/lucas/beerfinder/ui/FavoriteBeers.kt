package de.lucas.beerfinder.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.lucas.beerfinder.model.Beer

@Composable
fun FavoriteBeers(beers: List<Beer>, onClickBeer: (Beer) -> Unit) {
    if (beers.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(all = 8.dp),
            content = {
                items(beers.size) { index ->
                    BeerItem(beer = beers[index], onClickBeer)
                }
            }
        )
    } else {
        NoFavorites()
    }
}