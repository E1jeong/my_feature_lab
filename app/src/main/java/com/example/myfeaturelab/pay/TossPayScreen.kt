package com.example.myfeaturelab.pay

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myfeaturelab.R
import com.tosspayments.paymentsdk.PaymentWidget
import com.tosspayments.paymentsdk.model.PaymentCallback
import com.tosspayments.paymentsdk.model.TossPaymentResult
import com.tosspayments.paymentsdk.view.PaymentMethod
import java.util.UUID

@Composable
fun TossPayScreen(
    paymentWidget: PaymentWidget,
    amount: Long = 1_000L,
    onSuccess: (TossPaymentResult.Success) -> Unit = {},
    onFail: (TossPaymentResult.Fail) -> Unit = {}
) {
    var rendered by remember { mutableStateOf(false) }
    val ctx = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 360.dp),   // 최소 높이 확보
            factory = { context ->
                // 컨테이너를 부모로 두고 정상적인 LayoutParams로 붙인다
                FrameLayout(context).apply {
                    val root = LayoutInflater.from(context)
                        .inflate(R.layout.view_toss_payment, this, false)
                    addView(
                        root,
                        FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                    )

                    val methodView = root.findViewById<PaymentMethod>(R.id.payment_method)
//                    val agreementView = root.findViewById<Agreement>(R.id.agreement)

                    paymentWidget.renderPaymentMethods(
                        method = methodView,
                        amount = PaymentMethod.Rendering.Amount(
                            value = amount,
                            currency = PaymentMethod.Rendering.Currency.KRW
                        )
                    )
//                    paymentWidget.renderAgreement(agreementView)

                    // 렌더 호출이 끝났으니 활성화
                    rendered = true
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            enabled = rendered,   // 렌더 전 클릭 방지
            onClick = {
                // 선택 여부 확인(필요 없으면 이 블록은 제거해도 됨)
                val selected = runCatching { paymentWidget.getSelectedPaymentMethod() }.getOrNull()
                if (selected == null) {
                    Toast.makeText(ctx, "결제수단을 먼저 선택해 주세요.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                paymentWidget.requestPayment(
                    paymentInfo = PaymentMethod.PaymentInfo(
                        orderId = UUID.randomUUID().toString().replace("-", "").take(20),
                        orderName = "Test Order 0001" // ASCII 유지
                    ),
                    paymentCallback = object : PaymentCallback {
                        override fun onPaymentSuccess(s: TossPaymentResult.Success) = onSuccess(s)
                        override fun onPaymentFailed(f: TossPaymentResult.Fail) = onFail(f)
                    }
                )
            }
        ) { Text("결제하기") }
    }
}