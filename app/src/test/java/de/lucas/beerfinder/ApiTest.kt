package de.lucas.beerfinder

import de.lucas.beerfinder.model.Beer
import de.lucas.beerfinder.model.BeerController
import de.lucas.beerfinder.model.api.ApiClient
import de.lucas.beerfinder.ui.LoadingState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class ApiTest {
    private lateinit var apiClient: ApiClient
    private lateinit var beerController: BeerController

    @Before
    fun setUp() {
        Timber.plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println(message)
            }
        })
        apiClient = ApiClient(mockk())
        beerController = BeerController(apiClient)
    }

    @Test
    fun testFetchBeerList() = runBlocking {
        val beer = listOf(
            Beer(
                0,
                "Test Name",
                "Test Tag",
                "",
                "",
                "",
                Beer.Ingredients(listOf(), listOf(), ""),
                listOf()
            )
        )
        var state = LoadingState.FINISHED
        coEvery { apiClient.fetchBeerList() }.returns(beer)
        val response = beerController.fetchBeerList(
            onLoading = { state = LoadingState.LOADING },
            onFinished = { state = LoadingState.FINISHED },
            onError = { state = LoadingState.ERROR }
        )
        assertEquals(LoadingState.FINISHED, state)
        assertEquals(beer, response)
    }

    @Test
    fun testNoInternetConnection() = runBlocking {
        var state = LoadingState.FINISHED
        coEvery { apiClient.fetchBeerList() }.throws(Exception())
        beerController.fetchBeerList(
            onLoading = { state = LoadingState.LOADING },
            onFinished = { state = LoadingState.FINISHED },
            onError = { state = LoadingState.ERROR }
        )
        assertEquals(LoadingState.ERROR, state)
    }
}