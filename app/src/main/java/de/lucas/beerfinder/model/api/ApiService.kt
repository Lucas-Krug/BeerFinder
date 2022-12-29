package de.lucas.beerfinder.model.api

import de.lucas.beerfinder.model.Beer
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("beers")
    suspend fun fetchBeerList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<Beer>
}