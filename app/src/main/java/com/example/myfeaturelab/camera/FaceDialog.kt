package com.example.myfeaturelab.camera

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed interface FaceUiState {
    data object Loading : FaceUiState
    data class Success(val bmp: Bitmap, val result: FaceLandmarkerResult) : FaceUiState
    data class Fail(val bmp: Bitmap) : FaceUiState
}

@Composable
fun FaceDialog(
    bmpRaw: Bitmap,                         // CLAHE+감마 직후 Bitmap
    helper: FaceLandmarkerHelper,
    onDismiss: () -> Unit
) {
    var uiState by remember { mutableStateOf<FaceUiState>(FaceUiState.Loading) }

    // ① 분석 시작 – Dialog 가 뜬 뒤 단 한 번
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            val res = helper.detect(bmpRaw)
            uiState = if (res != null)
                FaceUiState.Success(bmpRaw, res)
            else FaceUiState.Fail(bmpRaw)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            when (uiState) {
                FaceUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }

                is FaceUiState.Fail -> {
                    Image(
                        bitmap = (uiState as FaceUiState.Fail).bmp.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(bmpRaw.width / bmpRaw.height.toFloat())
                    )
                    Text(
                        "얼굴을 찾지 못했어요", color = Color.Red,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }

                is FaceUiState.Success -> {
                    val s = uiState as FaceUiState.Success
                    val landmarks = s.result.faceLandmarks()[0] // 468점

                    Image(
                        bitmap = s.bmp.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(bmpRaw.width / bmpRaw.height.toFloat())
                    )

                    // ② 오버레이
                    Canvas(Modifier.matchParentSize()) {
                        val w = size.width
                        val h = size.height

                        val idx = intArrayOf(
                            10, 338, 297, 332, 284, 251, 389, 356, 454,
                            323, 361, 288, 397, 365, 379, 378, 400, 377,
                            152, 148, 176, 149, 150, 136, 172, 58, 132,
                            93, 234, 127, 162, 21, 54, 103, 67, 109, 10
                        )

                        val path = androidx.compose.ui.graphics.Path().apply {
                            val first = landmarks[idx[0]]
                            moveTo(first.x() * w, first.y() * h)
                            for (i in 1 until idx.size) {
                                val lm = landmarks[idx[i]]
                                lineTo(lm.x() * w, lm.y() * h)
                            }
                            close()
                        }

                        drawPath(
                            path,
                            color = Color.Cyan,
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // 필요하면 랜드마크 점 추가
                        landmarks.forEach { pt ->
                            drawCircle(
                                Color.Cyan,
                                radius = 2.dp.toPx(),
                                center = Offset(pt.x() * size.width, pt.y() * size.height)
                            )
                        }
                    }
                }
            }
        }
    }
}
