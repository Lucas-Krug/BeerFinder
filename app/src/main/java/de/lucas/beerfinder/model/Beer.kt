package de.lucas.beerfinder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "beer_table")
@Serializable
data class Beer(
    @PrimaryKey
    val id: Int,
    val name: String,
    val tagline: String,
    @SerialName("first_brewed")
    val firstBrewed: String,
    val description: String,
    @SerialName("image_url")
    val imageUrl: String?,
    val ingredients: Ingredients,
    @SerialName("food_pairing")
    val foodPairing: List<String>,
    val rating: Float = 0f
) {
    @Serializable
    data class Ingredients(
        val malt: List<Malt>,
        val hops: List<Hops>,
        val yeast: String
    ) {
        @Serializable
        data class Malt(
            val name: String,
            val amount: Amount
        )

        @Serializable
        data class Hops(
            val name: String,
            val amount: Amount
        )

        @Serializable
        data class Amount(
            val value: Double,
            val unit: String
        )
    }
}