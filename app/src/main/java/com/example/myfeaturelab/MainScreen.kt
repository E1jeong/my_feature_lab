package com.example.myfeaturelab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.myfeaturelab.navigation.NavigationItem
import com.example.myfeaturelab.navigation.NavigationUtil

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { NavigationUtil.navigate(navController, NavigationItem.CLONE_UI_NAV_HOST) }
        ) {
            Text(text = "Go to Clone UI")
        }

        Button(
            onClick = { NavigationUtil.navigate(navController, NavigationItem.CAMERA) }
        ) {
            Text(text = "Camera")
        }

        Button(
            onClick = { NavigationUtil.navigate(navController, NavigationItem.CUSTOM_KEYBOARD) }
        ) {
            Text(text = "Custom keyboard")
        }
    }
}

//@Preview
//@Composable
//private fun MainScreenPreview() {
//    Surface {
//        MainScreen()
//    }
//}