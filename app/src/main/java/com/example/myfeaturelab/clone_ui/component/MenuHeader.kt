package com.example.myfeaturelab.clone_ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuHeader(
    modifier: Modifier = Modifier,
    category: String,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(text = "$category • ", fontSize = 10.sp)
        Text(text = "추천", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "")
    }
}