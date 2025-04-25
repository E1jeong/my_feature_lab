package com.example.myfeaturelab.camera

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@Composable
fun OverlayView(
    left: PointF?,
    right: PointF?,
    dotRadius: Dp = 12.dp,
    strokeWidth: Dp = 4.dp,
    colorLeft: Color = Color.Red,
    colorRight: Color = Color.Blue,
    rectHeightRatio: Float = FACE_HEIGHT_RATIO,
    rectColor: Color = Color.Green
) {
    Canvas(Modifier.fillMaxSize()) {
        val stroke = Stroke(strokeWidth.toPx())

        // 1) 좌표를 화면 픽셀로 변환
        if (left != null && right != null) {
            val p1 = Offset(left.x * size.width, left.y * size.height)
            val p2 = Offset(right.x * size.width, right.y * size.height)

            // 2) 사각형 높이 결정
            val shoulderWidth = (p2.x - p1.x).absoluteValue
            val rectHeight = shoulderWidth * rectHeightRatio

            // 3) top-left, top-right 계산
            val topY = max(p1.y, p2.y) - rectHeight
            val bottomY = max(p1.y, p2.y)

            // 4) 사각형 그리기
            drawRect(
                color = rectColor,
                topLeft = Offset(min(p1.x, p2.x), topY),
                size = androidx.compose.ui.geometry.Size(shoulderWidth, rectHeight),
                style = stroke
            )
        }

        left?.let {
            drawCircle(
                colorLeft,
                radius = dotRadius.toPx(),
                center = Offset(it.x * size.width, it.y * size.height),
                style = stroke
            )
        }
        right?.let {
            drawCircle(
                colorRight,
                radius = dotRadius.toPx(),
                center = Offset(it.x * size.width, it.y * size.height),
                style = stroke
            )
        }
    }
}
