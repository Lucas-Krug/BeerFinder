package de.lucas.beerfinder.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.lucas.beerfinder.R
import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.ui.NavItem.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

@Composable
fun Root() {
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = hiltViewModel()
    val context = LocalContext.current
    var showErrorMessage by remember { mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    var tabPage by remember { mutableStateOf(BEER_LIST) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AnimatedVisibility(visible = rootViewModel.showTabBar,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it }
            ) {
                TabHome(
                    selectedTabIndex = tabPage.ordinal,
                    onSelectedTab = {
                        tabPage = it
                        navController.navigate(tabPage.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BEER_LIST.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BEER_LIST.route) { stackEntry ->
                rootViewModel.showTabBar = BEER_LIST.showTabBar
                val model: BeerListViewModel = hiltViewModel(stackEntry)
                LaunchedEffect(model) {
                    if (model.beerList.isEmpty()) {
                        model.fetchBeerList(
                            onLoading = { rootViewModel.state = LoadingState.LOADING },
                            onFinished = { rootViewModel.state = LoadingState.FINISHED },
                            onError = { rootViewModel.state = LoadingState.ERROR }
                        )
                    }
                }
                BeerList(
                    beers = model.beerList,
                    state = rootViewModel.state,
                    onClickBeer = { beer ->
                        val jsonBeer =
                            URLEncoder.encode(Json.encodeToString(beer), "UTF-8").replace("+", " ")
                        navController.navigate("${BEER_DETAILS.route}/${jsonBeer}")
                    },
                    onClickLoad = {
                        model.fetchBeerList(
                            onLoading = { rootViewModel.state = LoadingState.LOADING },
                            onFinished = { rootViewModel.state = LoadingState.FINISHED },
                            onError = { rootViewModel.state = LoadingState.ERROR }
                        )
                    },
                    onError = { showErrorMessage = true },
                    onClickRandom = {
                        model.fetchRandomBeer(
                            onFinished = { beer ->
                                val jsonBeer =
                                    URLEncoder.encode(
                                        Json.encodeToString(beer),
                                        "UTF-8"
                                    )
                                        .replace("+", " ")
                                navController.navigate("${BEER_DETAILS.route}/${jsonBeer}")
                            },
                            onError = { showErrorMessage = true }
                        )
                    }
                )
            }
            composable(
                "${BEER_DETAILS.route}/{beer}",
                arguments = listOf(navArgument("beer")
                { type = NavType.StringType })
            ) { backStackEntry ->
                rootViewModel.showTabBar = BEER_DETAILS.showTabBar
                val model: BeerDetailsViewModel = hiltViewModel()
                backStackEntry.arguments?.getString("beer").let { json ->
                    val beer = Json.decodeFromString<Beer>(json!!)
                    LaunchedEffect(model) {
                        model.isFavorite(beer.id)
                    }
                    BeerDetails(
                        beer = beer,
                        isFavorite = model.isFavorite,
                        onClickFavorite = {
                            model.isFavorite = !model.isFavorite
                            if (model.isFavorite) {
                                model.addFavorite(beer)
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Added to Favorites",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else {
                                model.removeFavorite(beer.id)
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Removed from Favorites",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        onClickBack = { navController.popBackStack() }
                    )
                }
            }
            composable(BEER_FAVORITES.route) {
                rootViewModel.showTabBar = BEER_FAVORITES.showTabBar
                val model: BeerFavoriteViewModel = hiltViewModel()
                LaunchedEffect(model) {
                    model.getFavorites()
                }
                FavoriteBeers(
                    beers = model.favoriteBeers,
                    onClickBeer = { beer ->
                        val jsonBeer =
                            URLEncoder.encode(
                                Json.encodeToString(beer),
                                "UTF-8"
                            )
                                .replace("+", " ")
                        navController.navigate("${BEER_DETAILS.route}/${jsonBeer}")
                    }
                )
            }
        }
    }
    if (showErrorMessage) {
        LaunchedEffect(rootViewModel) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = context.getString(R.string.error_message),
                duration = SnackbarDuration.Short
            )
            showErrorMessage = false
            rootViewModel.state = LoadingState.FINISHED
        }
    }
}