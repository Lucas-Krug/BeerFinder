package de.lucas.beerfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
    onClickRandom: () -> Unit,
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
                            BeerItem(beer = beers[index], onClickBeer)
                        }
                        item(
                            span = { GridItemSpan(maxCurrentLineSpan) }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = onClickLoad,
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier
                                        .padding(top = 16.dp, bottom = 16.dp)
                                        .height(40.dp)
                                        .width(124.dp)
                                ) {
                                    Text(text = stringResource(id = R.string.load_more))
                                }
                                Text(
                                    text = stringResource(id = R.string.random_title),
                                    style = Typography.caption
                                )
                                Button(
                                    onClick = onClickRandom,
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier
                                        .padding(top = 4.dp, bottom = 16.dp)
                                        .height(40.dp)
                                        .width(124.dp)
                                ) {
                                    Text(text = stringResource(id = R.string.surprise_me))
                                }
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
            Box(Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(beer.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_downloading),
                    error = painterResource(id = R.drawable.ic_no_image),
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                )
                if (beer.rating > 0f) {
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
                        Text(text = beer.rating.toString().trim(), style = Typography.caption)
                        Image(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = ""
                        )
                    }
                }
            }
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
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.1f))
            .pointerInput(Unit) {}
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = Color.Black)
        }
    }
}

fun String.trim(): String = if (this.last() == '0') this.first().toString() else this

@Preview
@Composable
fun PreviewBeerList() {
    BeerList(
        beers = listOf(
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
                foodPairing = listOf(),
                rating = 1.0f
            )
        ),
        state = FINISHED,
        onClickBeer = {},
        onClickLoad = {},
        onClickRandom = {},
        onError = {}
    )
}