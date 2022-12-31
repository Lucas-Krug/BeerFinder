package de.lucas.beerfinder.ui

import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun TabHome(selectedTabIndex: Int, onSelectedTab: (NavItem) -> Unit) {
    TabRow(selectedTabIndex = selectedTabIndex) {
        NavItem.values().forEachIndexed { index, navItem ->
            if (index == 0 || index == 1) {
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { onSelectedTab(navItem) },
                    text = { Text(navItem.title) }
                )
            }
        }
    }
}