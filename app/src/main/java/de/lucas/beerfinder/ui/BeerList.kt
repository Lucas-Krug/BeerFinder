package de.lucas.beerfinder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import de.lucas.beerfinder.R
import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.model.Beer.Ingredients
import de.lucas.beerfinder.ui.LoadingState.*
import de.lucas.beerfinder.ui.theme.Typography

@Composable
fun BeerList(
    beers: List<Beer>,
    state: LoadingState,
    onClickBeer: (beer: Beer) -> Unit,
    onClickLoad: () -> Unit,
    onError: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        when {
            beers.isNotEmpty() -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(all = 8.dp),
                    content = {
                        items(beers.size) { index ->
                            if (index == beers.lastIndex) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    BeerItem(beer = beers[index], onClickBeer)
                                    Button(
                                        onClick = onClickLoad,
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                                    ) {
                                        Text(text = stringResource(id = R.string.load_more))
                                    }
                                }
                            } else {
                                BeerItem(beer = beers[index], onClickBeer)
                            }
                        }
                    }
                )
            }
            state == ERROR -> {
                EmptyState(onClickRetry = onClickLoad)
            }
        }
        if (state == LOADING) {
            LoadingIndicator()
        }
        if (beers.isNotEmpty() && state == ERROR) {
            onError()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BeerItem(beer: Beer, onClickBeer: (beer: Beer) -> Unit) {
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(0.7f),
        onClick = { onClickBeer(beer) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(beer.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_downloading),
                contentDescription = "",
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = beer.name,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = beer.tagline,
                textAlign = TextAlign.Center,
                style = Typography.caption,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Color.Black)
    }
}

@Preview
@Composable
fun PreviewBeerList() {
    BeerList(
        listOf(
            Beer(
                id = 1,
                name = "Buzz",
                tagline = "A Real Bitter Experience.",
                firstBrewed = "09/2007",
                description = "",
                imageUrl = "",
                ingredients = Ingredients(
                    malt = listOf(),
                    hops = listOf(),
                    yeast = ""
                ),
                foodPairing = listOf()
            )
        ),
        FINISHED,
        {}, {}
    ) {}
}