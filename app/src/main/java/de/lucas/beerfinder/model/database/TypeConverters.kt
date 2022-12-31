package de.lucas.beerfinder.model.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import de.lucas.beerfinder.model.Beer.Ingredients
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
class ListConverter {

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(list: String): List<String> {
        return Json.decodeFromString(list)
    }
}

@ProvidedTypeConverter
class IngredientConverter {

    @TypeConverter
    fun fromIngredient(ingredients: Ingredients): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredient(ingredients: String): Ingredients {
        return Json.decodeFromString(ingredients)
    }
}