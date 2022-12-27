package de.lucas.beerfinder.model.api

import de.lucas.beerfinder.model.Beer
import retrofit2.http.GET

interface ApiService {

    @GET("beers")
    suspend fun fetchBeerList(): List<Beer>
}