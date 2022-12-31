package de.lucas.beerfinder.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.lucas.beerfinder.model.Beer

@Dao
interface BeerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBeer(beer: Beer)

    @Query("SELECT * FROM beer_table")
    suspend fun getBeerList(): List<Beer>

    @Query("SELECT EXISTS(SELECT * FROM beer_table WHERE `id` = :id)")
    suspend fun isBeerFavorite(id: Int): Boolean

    @Query("DELETE FROM beer_table WHERE `id` = :id")
    suspend fun removeBeer(id: Int)
}