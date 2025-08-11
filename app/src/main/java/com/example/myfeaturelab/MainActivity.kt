package com.example.myfeaturelab

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myfeaturelab.ui.theme.MyFeatureLabTheme
import com.tosspayments.paymentsdk.PaymentWidget
import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {

    private lateinit var paymentWidget: PaymentWidget

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OpenCVLoader.initLocal()

        paymentWidget = PaymentWidget(
            activity = this,
            clientKey = "test_ck_oEjb0gm23P5Oq0P6mpAjVpGwBJn5",   // 환경변수/BuildConfig로 빼도 됨
            customerKey = "anonymous"
        )

        setContent {
            MyFeatureLabTheme {
                MainNavHost(paymentWidget = paymentWidget)
            }
        }
    }
}
