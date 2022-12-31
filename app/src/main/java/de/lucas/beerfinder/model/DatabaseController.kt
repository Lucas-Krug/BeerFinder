package de.lucas.beerfinder.model

import de.lucas.beerfinder.model.database.BeerDao
import javax.inject.Inject

class DatabaseController @Inject constructor(private val beerDao: BeerDao) {

    suspend fun addBeer(beer: Beer) = beerDao.addBeer(beer)

    suspend fun getFavoriteBeers() = beerDao.getBeerList()

    suspend fun isBeerFavorite(id: Int) = beerDao.isBeerFavorite(id)

    suspend fun removeBeer(id: Int) = beerDao.removeBeer(id)
}