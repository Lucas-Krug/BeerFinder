package de.lucas.beerfinder.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.lucas.beerfinder.ui.NavItem.BEER_LIST

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Root() {
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = hiltViewModel()

    Scaffold(topBar = {
        TopAppBar {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(
                    text = rootViewModel.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }) {
        NavHost(navController = navController, startDestination = BEER_LIST.route) {
            composable(BEER_LIST.route) { stackEntry ->
                rootViewModel.title = BEER_LIST.title
                val model: BeerListViewModel = hiltViewModel(stackEntry)
                LaunchedEffect(model) {
                    model.fetchBeerList(
                        onLoading = { rootViewModel.state = LoadingState.LOADING },
                        onFinished = { rootViewModel.state = LoadingState.FINISHED },
                        onError = { rootViewModel.state = LoadingState.ERROR }
                    )
                }
                BeerList(
                    beers = model.beerlist,
                    state = rootViewModel.state,
                    onClickRetry = {
                        model.fetchBeerList(
                            onLoading = { rootViewModel.state = LoadingState.LOADING },
                            onFinished = { rootViewModel.state = LoadingState.FINISHED },
                            onError = { rootViewModel.state = LoadingState.ERROR }
                        )
                    }
                )
            }
        }
    }
    if (rootViewModel.state == LoadingState.LOADING) {
        LoadingIndicator()
    }
}