package de.lucas.beerfinder.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.lucas.beerfinder.R
import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.ui.NavItem.BEER_DETAILS
import de.lucas.beerfinder.ui.NavItem.BEER_LIST
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
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar {
                if (rootViewModel.showBackNavButton) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = ""
                        )
                    }
                }
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Text(
                        text = rootViewModel.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BEER_LIST.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BEER_LIST.route) { stackEntry ->
                rootViewModel.showBackNavButton = BEER_LIST.isNavBackEnabled
                rootViewModel.title = BEER_LIST.title
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
                rootViewModel.showBackNavButton = BEER_DETAILS.isNavBackEnabled
                rootViewModel.title = BEER_DETAILS.title
                backStackEntry.arguments?.getString("beer").let { json ->
                    val beer = Json.decodeFromString<Beer>(json!!)
                    BeerDetails(beer = beer)
                }
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