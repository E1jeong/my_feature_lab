package com.example.myfeaturelab.clone_ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myfeaturelab.clone_ui.dummy_screen.EmailScreen
import com.example.myfeaturelab.clone_ui.dummy_screen.SearchScreen
import com.example.myfeaturelab.clone_ui.dummy_screen.SettingsScreen
import com.example.myfeaturelab.navigation.NavigationItem

@Composable
fun CloneUiNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface {
        Scaffold(
            topBar = {},
            content = {
                NavHost(
                    modifier = Modifier.padding(it),
                    navController = navController,
                    startDestination = NavigationItem.HOME
                ) {
                    composable(route = NavigationItem.HOME) {
                        HomeScreen()
                    }
                    composable(route = NavigationItem.EMAIL) {
                        EmailScreen()
                    }
                    composable(route = NavigationItem.SEARCH) {
                        SearchScreen()
                    }
                    composable(route = NavigationItem.SETTINGS) {
                        SettingsScreen()
                    }
                }
            },
            bottomBar = {
                CloneUiBottomBar(
                    navController = navController,
                    currentRoute = currentRoute,
                )
            }
        )
    }
}