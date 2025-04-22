package com.example.myfeaturelab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myfeaturelab.camera.CameraScreen
import com.example.myfeaturelab.clone_ui.CloneUiNavHost
import com.example.myfeaturelab.navigation.NavigationItem

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = NavigationItem.MAIN
        ) {
            composable(route = NavigationItem.MAIN) {
                MainScreen(navController = navController)
            }
            composable(route = NavigationItem.CLONE_UI_NAV_HOST) {
                CloneUiNavHost()
            }
            composable(route = NavigationItem.CAMERA) {
                CameraScreen()
            }
        }
    }
}