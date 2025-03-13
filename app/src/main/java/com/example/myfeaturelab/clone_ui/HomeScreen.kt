package com.example.myfeaturelab.clone_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        MenuTabRow()
    }
}
