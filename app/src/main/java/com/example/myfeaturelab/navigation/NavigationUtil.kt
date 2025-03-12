package com.example.myfeaturelab.navigation

import androidx.navigation.NavHostController

object NavigationUtil {
    fun navigate(
        controller: NavHostController,
        routeName: String,
        backStackRouteName: String? = null,
        isLaunchSingleTop: Boolean = true,
        needToRestoreState: Boolean = true
    ) {
        controller.navigate(routeName) {
            if (backStackRouteName != null) {
                popUpTo(backStackRouteName) {
                    saveState = true
                }
            }
            launchSingleTop = isLaunchSingleTop
            restoreState = needToRestoreState
        }
    }
}