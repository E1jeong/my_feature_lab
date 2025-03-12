package com.example.myfeaturelab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myfeaturelab.navigation.NavigationItem

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Surface {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = NavigationItem.MAIN
        ) {
            composable(route = NavigationItem.MAIN) {
                MainScreen()
            }
        }
    }
}