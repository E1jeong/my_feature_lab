package com.example.myfeaturelab.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraScreen() {
    Text("CameraScreen")
    CameraPermissionSample {
        CoroutineTextureCameraPreview()
    }
}

// TextureView에서 SurfaceTexture가 준비될 때까지 대기하는 suspend 유틸
private suspend fun TextureView.awaitSurfaceTexture(
    width: Int, height: Int
): SurfaceTexture = suspendCancellableCoroutine { cont ->
    if (isAvailable) {
        surfaceTexture!!.also {
            it.setDefaultBufferSize(width, height)
            cont.resume(it)
        }
    } else {
        surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(st: SurfaceTexture, w: Int, h: Int) {
                st.setDefaultBufferSize(width, height)
                cont.resume(st)
            }

            override fun onSurfaceTextureSizeChanged(st: SurfaceTexture, w: Int, h: Int) {}
            override fun onSurfaceTextureDestroyed(st: SurfaceTexture) = true
            override fun onSurfaceTextureUpdated(st: SurfaceTexture) {}
        }
    }
    cont.invokeOnCancellation {
        surfaceTextureListener = null
    }
}

// 카메라 열기 suspend 래퍼
@RequiresApi(Build.VERSION_CODES.P)
private suspend fun openCameraSuspend(
    context: Context, cameraId: String
): CameraDevice = suspendCancellableCoroutine { cont ->
    val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED
    ) {
        manager.openCamera(
            cameraId, ContextCompat.getMainExecutor(context),
            object : CameraDevice.StateCallback() {
                override fun onOpened(device: CameraDevice) = cont.resume(device)
                override fun onDisconnected(device: CameraDevice) {
                    device.close()
                    cont.resumeWithException(IllegalStateException("Camera disconnected"))
                }

                override fun onError(device: CameraDevice, error: Int) {
                    device.close()
                    cont.resumeWithException(RuntimeException("Open error: $error"))
                }
            })
        cont.invokeOnCancellation { /* 필요시 manager.closeCamera(...) */ }
    } else {
        // 권한 요청 로직 실행
    }
}

// 세션 생성 suspend 래퍼
private suspend fun CameraDevice.createSessionSuspend(
    previewSurface: Surface
): CameraCaptureSession = suspendCancellableCoroutine { cont ->
    createCaptureSession(
        listOf(previewSurface),
        object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) =
                cont.resume(session)

            override fun onConfigureFailed(session: CameraCaptureSession) =
                cont.resumeWithException(RuntimeException("Session config failed"))
        },
        null
    )
}

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("MissingPermission")
@Composable
fun CoroutineTextureCameraPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val textureView = remember { TextureView(context) }

    // ① 전용 스레드 + 핸들러
    val cameraThread = remember {
        HandlerThread("CameraBackground").apply { start() }
    }
    val cameraHandler = remember { Handler(cameraThread.looper) }

    var cameraDevice by remember { mutableStateOf<CameraDevice?>(null) }
    var session by remember { mutableStateOf<CameraCaptureSession?>(null) }

    // 뒤 카메라 ID
    val cameraManager = remember {
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    val cameraId = remember {
        cameraManager.cameraIdList.first {
            cameraManager.getCameraCharacteristics(it)
                .get(CameraCharacteristics.LENS_FACING) ==
                    CameraCharacteristics.LENS_FACING_BACK
        }
    }

    LaunchedEffect(textureView) {
        withContext(Dispatchers.IO) {
            // SurfaceTexture 준비
            val st = textureView.awaitSurfaceTexture(
                width = textureView.width.coerceAtLeast(1280),
                height = textureView.height.coerceAtLeast(720)
            )

            // 카메라 열기
            val device = openCameraSuspend(context, cameraId)
            cameraDevice = device

            val previewSurface = Surface(st)

            // ② handler를 넘겨 세션 생성
            val camSession = suspendCancellableCoroutine<CameraCaptureSession> { cont ->
                device.createCaptureSession(
                    listOf(previewSurface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(s: CameraCaptureSession) =
                            cont.resume(s)
                        override fun onConfigureFailed(s: CameraCaptureSession) =
                            cont.resumeWithException(RuntimeException("Session config failed"))
                    },
                    cameraHandler  // ← 반드시 Handler 전달
                )
            }
            session = camSession

            // Preview 요청
            val request = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                addTarget(previewSurface)
                set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            }.build()

            // ③ handler를 넘겨 반복 요청
            camSession.setRepeatingRequest(request, null, cameraHandler)
        }
    }

    DisposableEffect(textureView) {
        onDispose {
            session?.close()
            cameraDevice?.close()
            cameraThread.quitSafely()
        }
    }

    AndroidView(factory = { textureView }, modifier = modifier)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionSample(onGranted: @Composable () -> Unit) {
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