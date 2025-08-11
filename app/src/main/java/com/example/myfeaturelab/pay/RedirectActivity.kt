package com.example.myfeaturelab.pay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class RedirectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ok = intent.data?.getQueryParameter("paymentKey") != null
        val orderId = intent.data?.getQueryParameter("orderId")

        /* ViewModel · NavController 등으로 결과 전달 */
        setResult(
            RESULT_OK,
            Intent().putExtra("ok", ok).putExtra("orderId", orderId)
        )
        finish()
    }
}