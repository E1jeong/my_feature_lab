package com.example.myfeaturelab.clone_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myfeaturelab.clone_ui.component.BigGamePager
import com.example.myfeaturelab.clone_ui.component.MenuTabRow

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        MenuTabRow()
        BigGamePager()
    }
}
