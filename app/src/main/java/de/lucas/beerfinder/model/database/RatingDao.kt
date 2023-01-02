package de.lucas.beerfinder.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.lucas.beerfinder.model.Rating

@Dao
interface RatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setRating(rating: Rating)

    @Query("SELECT * FROM rating_table WHERE id = :id")
    suspend fun hasRating(id: Int): Rating?
}