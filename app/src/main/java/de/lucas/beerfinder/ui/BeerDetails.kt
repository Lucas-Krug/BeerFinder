package de.lucas.beerfinder.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.lucas.beerfinder.R
import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.model.Beer.Ingredients
import de.lucas.beerfinder.ui.theme.LightBrown
import de.lucas.beerfinder.ui.theme.Typography
import kotlin.text.Typography.bullet

@Composable
fun BeerDetails(
    beer: Beer,
    isFavorite: Boolean,
    onClickFavorite: () -> Unit,
    onClickBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(LightBrown)
    ) {
        AsyncImage(
            model = beer.imageUrl,
            error = painterResource(id = R.drawable.ic_no_image),
            contentDescription = "",
            modifier = Modifier
                .weight(1f)
                .padding(top = 8.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.White)
                .weight(3f)
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    IconButton(onClick = { onClickBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = ""
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        onClickFavorite()
                    }) {
                        Icon(
                            painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite_added else R.drawable.ic_not_favorite),
                            contentDescription = ""
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = beer.name,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = beer.tagline,
                        style = Typography.caption,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    Text(
                        text = beer.description,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.brewed_since, beer.firstBrewed),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    ExpandableList(foodPairing = beer.foodPairing, ingredients = null)
                    ExpandableList(foodPairing = null, ingredients = beer.ingredients)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ExpandableList(foodPairing: List<String>?, ingredients: Ingredients?) {
    var expandableState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandableState) 180f else 0f)
    Card(
        onClick = { expandableState = !expandableState },
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    delayMillis = 100,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(bottom = 16.dp),
        true,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = LightBrown,
        contentColor = contentColorFor(SnackbarDefaults.backgroundColor),
        border = BorderStroke(1.dp, Color.Gray),
        interactionSource = remember { MutableInteractionSource() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = if (foodPairing != null) R.string.food_pairings else R.string.ingredients),
                    modifier = Modifier.weight(4f)
                )
                IconButton(modifier = Modifier
                    .weight(1f)
                    .rotate(rotationState),
                    onClick = {
                        expandableState = !expandableState
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = ""
                    )
                }
            }
            if (expandableState) {
                if (foodPairing != null) {
                    Column {
                        foodPairing.forEach { pairing ->
                            Text(text = "$bullet $pairing")
                        }
                    }
                } else if (ingredients != null) {
                    IngredientList(ingredients)
                }
            }
        }
    }
}

@Composable
private fun IngredientList(ingredients: Ingredients) {
    Column {
        Text(text = stringResource(id = R.string.malt), style = Typography.h6)
        ingredients.malt.forEach { malt ->
            Text(text = malt.name, modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "${malt.amount.value} ${malt.amount.unit}",
                style = Typography.caption
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(text = stringResource(id = R.string.hops), style = Typography.h6)
        ingredients.hops.forEach { hops ->
            Text(text = hops.name, modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "${hops.amount.value} ${hops.amount.unit}",
                style = Typography.caption
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(text = stringResource(id = R.string.yeast), style = Typography.h6)
        Text(text = ingredients.yeast)
    }
}