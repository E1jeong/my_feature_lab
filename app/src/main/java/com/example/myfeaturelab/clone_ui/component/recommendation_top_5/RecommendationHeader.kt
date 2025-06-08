package com.example.myfeaturelab.clone_ui.component.recommendation_top_5

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun RecommendationHeader() {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "놓치지 마세요! 에디터 추천 게임 Top5", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "취향 저격! 화제의 게임들을 지금 바로 만나보세요!", fontSize = 12.sp)
    }
}