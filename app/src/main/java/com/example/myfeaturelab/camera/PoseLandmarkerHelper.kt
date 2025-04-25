package com.example.myfeaturelab.camera

import android.content.Context
import android.graphics.PointF
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker

class PoseLandmarkerHelper(
    context: Context,
    private val isFront: Boolean = false
) {
    private val landmarker = PoseLandmarker.createFromOptions(
        context,
        PoseLandmarker.PoseLandmarkerOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath("pose_landmarker_lite.task")
                    .setDelegate(Delegate.GPU)
                    .build()
            )
            .setRunningMode(RunningMode.VIDEO)
            .build()
    )

    fun detect(proxy: ImageProxy, onShoulders: (PointF, PointF) -> Unit) {
        val mp = proxy.toMpImageRGBA()
        val list = landmarker.detectForVideo(mp, proxy.imageInfo.timestamp).landmarks()
        if (list.isEmpty()) return
        val lm = list[0]
        var lx = lm[11].x()
        var rx = lm[12].x()
        val ly = lm[11].y()
        val ry = lm[12].y()
        if (isFront) {
            lx = 1f - lx; rx = 1f - rx
        }
        onShoulders(PointF(lx, ly), PointF(rx, ry))
    }

    private fun ImageProxy.toMpImageRGBA(): MPImage {
        val bmp = toRgbBitmap()        // context 불필요
        return BitmapImageBuilder(bmp).build()
    }
}
