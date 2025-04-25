package com.example.myfeaturelab.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import androidx.camera.core.ImageProxy
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.createBitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 프레임 재사용 버퍼를 단일화하기 위한 간단한 풀
 */
private object BufferPool {
    @Volatile
    private var bitmap: Bitmap? = null
    fun obtain(width: Int, height: Int): Bitmap =
        bitmap?.takeIf { it.width == width && it.height == height }
            ?: createBitmap(width, height).also { bitmap = it }
}

fun ImageProxy.toRgbBitmap(): Bitmap {
    // plane 0 == RGBA_8888 (OUTPUT_IMAGE_FORMAT_RGBA_8888 가정)
    val plane = planes[0]
    val bmp = BufferPool.obtain(width, height)
    plane.buffer.rewind()
    bmp.copyPixelsFromBuffer(plane.buffer)

    return if (imageInfo.rotationDegrees != 0) {
        val m = Matrix().apply { postRotate(imageInfo.rotationDegrees.toFloat()) }
        Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, m, true)
    } else bmp
}

fun cropAndCorrectBitmap(
    bmp: Bitmap,
    left: PointF,
    right: PointF,
    heightRatio: Float = FACE_HEIGHT_RATIO
): Bitmap {
    val w = bmp.width
    val h = bmp.height
    val p1 = Offset(left.x * w, left.y * h)
    val p2 = Offset(right.x * w, right.y * h)

    val rectH = abs(p2.x - p1.x) * heightRatio
    val crop = android.graphics.Rect(
        min(p1.x, p2.x).roundToInt(),
        (max(p1.y, p2.y) - rectH).roundToInt().coerceAtLeast(0),
        max(p1.x, p2.x).roundToInt(),
        max(p1.y, p2.y).roundToInt()
    )

    val face = Bitmap.createBitmap(bmp, crop.left, crop.top, crop.width(), crop.height())
    val cm = android.graphics.ColorMatrix().apply { setScale(1.2f, 1.2f, 1.2f, 1f) }
    val out = createBitmap(face.width, face.height)
    android.graphics.Canvas(out).drawBitmap(
        face, 0f, 0f,
        android.graphics.Paint()
            .apply { colorFilter = android.graphics.ColorMatrixColorFilter(cm) })
    face.recycle()
    return out
}

fun claheEnhance(src: Bitmap): Bitmap {
    // ① Bitmap → Mat
    val lab = Mat()
    Utils.bitmapToMat(src, lab)
    Imgproc.cvtColor(lab, lab, Imgproc.COLOR_RGB2Lab)

    // ② L 채널 CLAHE (clip↑ tile↓)
    val channels = ArrayList<Mat>(3).apply { Core.split(lab, this) }
    val clahe = Imgproc.createCLAHE(4.0, Size(4.0, 4.0))   // ★
    clahe.apply(channels[0], channels[0])

    // ③ L 채널 감마 보정 (γ=0.5)
    val l = channels[0]
    l.convertTo(l, CvType.CV_32F, 1.0 / 255.0)   // ★ float 정규화
    Core.pow(l, 0.2, l)                          // γ 적용
    l.convertTo(l, CvType.CV_8U, 255.0)          // ★ 다시 8-bit로

    // ④ 다시 합치고 RGBA 복귀
    Core.merge(channels, lab)
    Imgproc.cvtColor(lab, lab, Imgproc.COLOR_Lab2RGB)

    val out = createBitmap(src.width, src.height)
    Utils.matToBitmap(lab, out)
    channels.forEach { it.release() }; lab.release()
    return out
}