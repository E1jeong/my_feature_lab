package com.example.myfeaturelab.clone_ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class SmallGameData(
    val isNew: Boolean,
    val isEditorRecommendation: Boolean,
    val image: String,
    val name: String,
    val description: String,
    val starScore: String,
)

data class GroupGameData(
    val game1: SmallGameData,
    val game2: SmallGameData,
    val game3: SmallGameData,
)

@Composable
fun SmallThreeGamePager() {
    val gameDatas = listOf<GroupGameData>(
        GroupGameData(
            game1 = SmallGameData(true, false, "https://picsum.photos/200/200", "정보의 바다: 삼촌 서바이벌", "전략", "4.5"),
            game2 = SmallGameData(false, true, "https://picsum.photos/201/201", "WOB: 화이트 인", "전략 • 3X", "4"),
            game3 = SmallGameData(false, false, "https://picsum.photos/202/202", "카이뮨M", "롤플레잉 • 싱글플레이어", "2.7"),
        ),
        GroupGameData(
            game1 = SmallGameData(false, false, "https://picsum.photos/203/203", "에잇나이츠 프론트", "롤플레잉 • MMORPG", ""),
            game2 = SmallGameData(false, false, "https://picsum.photos/204/204", "전설급 귀속 아이템을 주웠다: 방치형RPG", "롤플레잉", ""),
            game3 = SmallGameData(true, true, "https://picsum.photos/210/210", "신검 키우기: 방치형RPG", "시뮬레이션", "3.7"),
        ),
        GroupGameData(
            game1 = SmallGameData(false, false, "https://picsum.photos/206/206", "슈터보이 모험", "카드", "3.8"),
            game2 = SmallGameData(true, true, "https://picsum.photos/207/207", "소울 테일즈", "롤플레잉", "4.4"),
            game3 = SmallGameData(false, false, "https://picsum.photos/208/208", "한판붙자", "롤플레잉 • 멀티플레이어", "2.9"),
        )
    )

    val pagerState = rememberPagerState(pageCount = { gameDatas.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        contentPadding = PaddingValues(end = 20.dp),
        pageSpacing = 8.dp,
    ) { index ->
        Column(modifier = Modifier.fillMaxWidth()) {
            GameItem(
                image = gameDatas[index].game1.image,
                name = gameDatas[index].game1.name,
                description = gameDatas[index].game1.description,
                starScore = gameDatas[index].game1.starScore,
                isNew = gameDatas[index].game1.isNew,
                isEditorRecommendation = gameDatas[index].game1.isEditorRecommendation,
            )
            Spacer(modifier = Modifier.height(8.dp))
            GameItem(
                image = gameDatas[index].game2.image,
                name = gameDatas[index].game2.name,
                description = gameDatas[index].game2.description,
                starScore = gameDatas[index].game2.starScore,
                isNew = gameDatas[index].game2.isNew,
                isEditorRecommendation = gameDatas[index].game2.isEditorRecommendation,
            )
            Spacer(modifier = Modifier.height(8.dp))
            GameItem(
                image = gameDatas[index].game3.image,
                name = gameDatas[index].game3.name,
                description = gameDatas[index].game3.description,
                starScore = gameDatas[index].game3.starScore,
                isNew = gameDatas[index].game3.isNew,
                isEditorRecommendation = gameDatas[index].game3.isEditorRecommendation,
            )
        }
    }
}

@Composable
fun GameItem(
    image: String,
    name: String,
    description: String,
    starScore: String,
    isNew: Boolean,
    isEditorRecommendation: Boolean,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = image,
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
                .padding(2.dp)
                .clip(shape = RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row {
                if (isNew) {
                    Text(text = "신규", color = Color.Cyan, fontSize = 10.sp)
                    Text(text = " • ", color = Color.DarkGray, fontSize = 10.sp)
                }

                Text(
                    text = description,
                    color = Color.DarkGray,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (starScore.isEmpty()) {
                    Text(text = "제공 예정", color = Color.DarkGray, fontSize = 10.sp)
                } else {
                    Text(text = starScore, color = Color.DarkGray, fontSize = 10.sp)
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "",
                        modifier = Modifier.size(10.dp),
                        tint = Color.DarkGray
                    )
                }

                if (isEditorRecommendation) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = "",
                        modifier = Modifier.size(10.dp),
                        tint = Color.DarkGray
                    )
                    Text(text = " 에디터 추천", color = Color.DarkGray, fontSize = 10.sp)
                }
            }
        }
    }
}