package de.lucas.beerfinder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rating_table")
data class Rating(
    @PrimaryKey
    val id: Int,
    val rating: Float
)