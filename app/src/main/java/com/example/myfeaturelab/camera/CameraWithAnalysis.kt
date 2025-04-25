package com.example.myfeaturelab.camera

import android.graphics.Bitmap
import android.graphics.PointF
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

const val FACE_HEIGHT_RATIO = 1.5f   // Overlay · Cropping 공통 사용

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraWithAnalysis(
    landmarkerHelper: PoseLandmarkerHelper,
    isPaused: Boolean,
    onShoulders: (PointF, PointF) -> Unit,
    onFrameBitmap: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val exec = remember { Executors.newSingleThreadExecutor() }

    val analysis = remember {
        ImageAnalysis.Builder()
            .setTargetResolution(Size(480, 640))
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    AndroidView(
        factory = { PreviewView(it).apply { scaleType = PreviewView.ScaleType.FILL_CENTER } },
        modifier = Modifier.fillMaxSize(),
    ) { previewView ->

        val providerFuture = ProcessCameraProvider.getInstance(context)
        providerFuture.addListener({
            val provider = providerFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis
            )
        }, ContextCompat.getMainExecutor(context))

        // → Analyzer는 PreviewView가 준비된 후에 등록
        analysis.setAnalyzer(exec) { proxy ->
            if (isPaused) {
                proxy.close(); return@setAnalyzer
            }

            landmarkerHelper.detect(proxy) { l, r -> onShoulders(l, r) }

            val bmp = proxy.toRgbBitmap()
            onFrameBitmap(bmp)             // 미리보기 콜백
            proxy.close()
        }
    }
}