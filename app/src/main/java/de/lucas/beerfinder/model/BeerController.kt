package de.lucas.beerfinder.model

import de.lucas.beerfinder.model.api.ApiClient
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class BeerController @Inject constructor(private val apiClient: ApiClient) {

    suspend fun fetchBeerList(
        page: Int,
        onLoading: () -> Unit,
        onFinished: (page: Int) -> Unit,
        onError: () -> Unit
    ): List<Beer>? {
        onLoading()
        return try {
            delay(1000)
            apiClient.fetchBeerList(page).apply {
                val nextPage = page + 1
                onFinished(nextPage)
            }
        } catch (exception: Exception) {
            onError()
            Timber.e(exception)
            null
        }
    }

    suspend fun fetchRandomBeer(onFinished: (Beer) -> Unit, onError: () -> Unit): Beer? {
        return try {
            apiClient.fetchRandomBeer().first().apply { onFinished(this) }
        } catch (exception: Exception) {
            onError()
            Timber.e(exception)
            null
        }
    }
}