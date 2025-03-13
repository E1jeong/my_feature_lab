package com.example.myfeaturelab.clone_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class GameData(
    val isUpdate: Boolean,
    val gameComment: String,
    val bigImage: String,
    val image: String,
    val name: String,
    val company: String,
    val limitAge: String,
    val startPoint: String,
)

@Composable
fun BigGamePager() {
    val gameDatas = listOf<GameData>(
        GameData(
            true,
            "미래를 위하여",
            "https://picsum.photos/300/200",
            "https://picsum.photos/50/50",
            "Future",
            "future company",
            "12",
            "4.5"
        ),
        GameData(
            false,
            "언제 대답해줌",
            "https://picsum.photos/300/201",
            "https://picsum.photos/50/51",
            "When",
            "repeat co.ltd",
            "8",
            "1.5"
        ),
        GameData(
            true,
            "떠나자",
            "https://picsum.photos/300/202",
            "https://picsum.photos/50/52",
            "Where",
            "go away",
            "19",
            "4.9"
        ),
        GameData(
            false,
            "하하 호호",
            "https://picsum.photos/300/199",
            "https://picsum.photos/50/53",
            "Why",
            "haha family",
            "all",
            "3.5"
        ),
        GameData(
            false,
            "개발자 키우기",
            "https://picsum.photos/300/198",
            "https://picsum.photos/50/54",
            "How",
            "Dev info",
            "12",
            "2.5"
        ),
    )

    val pagerState = rememberPagerState(pageCount = { gameDatas.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        pageSpacing = 8.dp,
    ) { page ->
        Column(modifier = Modifier.fillMaxWidth()) {
            //큰 이미지
            Box(Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = gameDatas[page].bigImage,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                if (gameDatas[page].isUpdate) {
                    Text(
                        text = "업데이트 가능",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.TopStart)
                            .background(Color.LightGray.copy(alpha = 0.5f))
                            .padding(4.dp)
                    )
                }

                Text(
                    text = gameDatas[page].gameComment,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
                        .background(color = Color.Blue.copy(alpha = 0.2f))
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.size(4.dp))

            //하단부 게임 설명
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(60.dp)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = gameDatas[page].image,
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(2.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                )

                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = gameDatas[page].name,
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "${gameDatas[page].company} • ${gameDatas[page].limitAge} • ${gameDatas[page].startPoint}",
                            color = Color.DarkGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.Blue, shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp)
                            .clickable(onClick = {})
                    ) {
                        Text("설치", fontSize = 12.sp, color = Color.White)
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "",
                            tint = Color.White,
                        )
                    }

                    Text("인앱구매", fontSize = 8.sp)
                }
            }
        }
    }
}