package com.example.myfeaturelab.clone_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myfeaturelab.clone_ui.component.BigGamePager
import com.example.myfeaturelab.clone_ui.component.MenuHeader
import com.example.myfeaturelab.clone_ui.component.MenuTabRow
import com.example.myfeaturelab.clone_ui.component.SmallThreeGamePager
import com.example.myfeaturelab.clone_ui.component.recommendation_top_5.RecomendationTop5

@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MenuTabRow()
        BigGamePager()
        MenuHeader(modifier = Modifier.padding(top = 48.dp), category = "스폰서")
        SmallThreeGamePager()
        RecomendationTop5()
    }
}
