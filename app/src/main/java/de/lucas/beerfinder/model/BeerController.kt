package de.lucas.beerfinder.model

import de.lucas.beerfinder.model.api.ApiClient
import timber.log.Timber
import javax.inject.Inject

class BeerController @Inject constructor(private val apiClient: ApiClient) {

    suspend fun fetchBeerList(
        onLoading: () -> Unit,
        onFinished: () -> Unit,
        onError: () -> Unit
    ): List<Beer>? {
        onLoading()
        return try {
            apiClient.fetchBeerList().apply { onFinished() }
        } catch (exception: Exception) {
            onError()
            Timber.e(exception)
            null
        }
    }
}