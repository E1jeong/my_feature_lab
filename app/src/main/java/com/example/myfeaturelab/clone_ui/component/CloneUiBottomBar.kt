package com.example.myfeaturelab.clone_ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.myfeaturelab.navigation.NavigationItem
import com.example.myfeaturelab.navigation.NavigationUtil

sealed class ScreenNav(
    val route: String,
    val icon: ImageVector,
    val label: String,
) {
    object Home : ScreenNav(NavigationItem.HOME, Icons.Filled.Home, "Home")
    object Email : ScreenNav(NavigationItem.EMAIL, Icons.Filled.Email, "Email")
    object Search : ScreenNav(NavigationItem.SEARCH, Icons.Filled.Search, "Search")
    object Settings : ScreenNav(NavigationItem.SETTINGS, Icons.Filled.Settings, "Settings")
}

@Composable
fun CloneUiBottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    val bottomNavigationItems = listOf(
        ScreenNav.Home,
        ScreenNav.Email,
        ScreenNav.Search,
        ScreenNav.Settings,
    )

    NavigationBar(containerColor = Color.White) {
        bottomNavigationItems.forEach {
            NavigationBarItem(
                icon = { Icon(imageVector = it.icon, contentDescription = "") },
                label = { Text(text = it.label, textAlign = TextAlign.Center) },
                selected = currentRoute == it.route,
                onClick = {
                    NavigationUtil.navigate(
                        navController,
                        it.route,
                        navController.graph.startDestinationRoute
                    )
                }
            )
        }
    }
}