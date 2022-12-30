package de.lucas.beerfinder.model.api

import de.lucas.beerfinder.model.Beer
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchBeerList(page: Int): List<Beer> =
        apiService.fetchBeerList(page = page, perPage = 24)

    suspend fun fetchRandomBeer(): List<Beer> = apiService.fetchRandomBeer()
}