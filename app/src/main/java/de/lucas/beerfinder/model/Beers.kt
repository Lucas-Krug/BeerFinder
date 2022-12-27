package de.lucas.beerfinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Beer(
    val id: Int,
    val name: String,
    val tagline: String,
    @SerialName("first_brewed")
    val firstBrewed: String,
    val description: String,
    @SerialName("image_url")
    val imageUrl: String,
    val ingredients: Ingredients,
    @SerialName("food_pairing")
    val foodPairing: List<String>
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