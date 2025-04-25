package com.example.myfeaturelab.camera

import android.Manifest
import android.graphics.Bitmap
import android.graphics.PointF
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val landmarker = remember { PoseLandmarkerHelper(context) }

    var isPaused by remember { mutableStateOf(false) }   // 카메라 일시정지 여부

    var left by remember { mutableStateOf<PointF?>(null) }
    var right by remember { mutableStateOf<PointF?>(null) }

    var captured by remember { mutableStateOf<ImageBitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val latestBmp = remember { AtomicReference<Bitmap?>(null) }

    val cameraScope = rememberCoroutineScope()

    CameraPermission {
        Box(Modifier.fillMaxSize()) {
            CameraWithAnalysis(
                landmarkerHelper = landmarker,
                isPaused = isPaused,
                onShoulders = { l, r ->
                    left = l
                    right = r
                },
                onFrameBitmap = { bmp ->
                    latestBmp.getAndSet(bmp)?.recycle()   // 이전 버퍼 해제
                }
            )

            OverlayView(left = left, right = right)

            Button(
                enabled = left != null && right != null,
                onClick = {
                    cameraScope.launch {
                        val bmp = latestBmp.get()         // Bitmap 캐시 사용
                        if (bmp == null) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "프레임 대기 중…", Toast.LENGTH_SHORT).show()
                            }
                            return@launch
                        }
                        val faceBmp = cropAndCorrectBitmap(bmp, left!!, right!!)
                        val enhanced = claheEnhance(faceBmp)
                        withContext(Dispatchers.Main) {
                            captured = enhanced.asImageBitmap()
                            showDialog = true
                        }
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) { Text("얼굴 추출") }
        }

        if (showDialog && captured != null) {
            isPaused = true

            FaceDialog(
                bmpRaw = captured!!.asAndroidBitmap(),
                helper = remember { FaceLandmarkerHelper(context) },
                onDismiss = {
                    showDialog = false
                    captured?.asAndroidBitmap()?.recycle(); captured = null
                    isPaused = false
                }
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(onGranted: @Composable () -> Unit) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    when {
        cameraPermissionState.status.isGranted -> {
            onGranted()
        }

        cameraPermissionState.status.shouldShowRationale -> {
            Text("카메라 권한이 필요합니다.")
        }

        else -> {
            Text("권한을 요청 중입니다...")
        }
    }
}