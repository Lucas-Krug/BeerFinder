package de.lucas.beerfinder.model.api

import de.lucas.beerfinder.model.Beer
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchBeerList(): List<Beer> = apiService.fetchBeerList()
}