package com.example.myfeaturelab.camera

import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult

class FaceLandmarkerHelper(
    context: Context,
    maxFaces: Int = 1,
    delegate: Delegate = Delegate.GPU
) {

    private val landmarker: FaceLandmarker = FaceLandmarker.createFromOptions(
        context,
        FaceLandmarker.FaceLandmarkerOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath("face_landmarker.task")
                    .setDelegate(delegate)
                    .build()
            )
            .setNumFaces(maxFaces)
            .setMinFaceDetectionConfidence(0.3f)
            .setMinFacePresenceConfidence(0.3f)
            .setRunningMode(RunningMode.IMAGE)
            .build()
    )

    /** 🆕 Bitmap 버전 – Dialog 내부에서 바로 호출 */
    fun detect(bmp: Bitmap): FaceLandmarkerResult? {
        val mpImage = BitmapImageBuilder(bmp).build()
        val res = landmarker.detect(mpImage)      // IMAGE 모드용
        return if (res.faceLandmarks().isEmpty()) null else res
    }
}