package de.lucas.beerfinder.ui

enum class NavItem(val route: String, val title: String, val showTabBar: Boolean = true) {
    BEER_LIST("beer_list", "Beers"),
    BEER_FAVORITES("beer_favorites", "Favorites"),
    BEER_DETAILS("beer_details", "Details", false)
}