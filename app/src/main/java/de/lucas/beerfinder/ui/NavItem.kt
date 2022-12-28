package de.lucas.beerfinder.ui

enum class NavItem(val route: String, val title: String, val isNavBackEnabled: Boolean = false) {
    BEER_LIST("beer_list", "Beer List"),
    BEER_DETAILS("beer_details", "Beer Details", true)
}