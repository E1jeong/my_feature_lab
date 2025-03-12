package com.example.myfeaturelab

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen() {
    Text("Main")
}

@Preview
@Composable
private fun MainScreenPreview() {
    Surface {
        MainScreen()
    }
}