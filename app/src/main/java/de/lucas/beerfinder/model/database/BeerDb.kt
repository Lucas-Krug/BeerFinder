package de.lucas.beerfinder.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.lucas.beerfinder.model.Beer

@Database(entities = [Beer::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class, IngredientConverter::class)
abstract class BeerDb : RoomDatabase() {

    abstract fun beerDao(): BeerDao
}