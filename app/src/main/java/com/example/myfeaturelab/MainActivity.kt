package com.example.myfeaturelab

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.myfeaturelab.ui.theme.MyFeatureLabTheme
import org.opencv.android.OpenCVLoader

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OpenCVLoader.initLocal()

        setContent {
            MyFeatureLabTheme {
                MainNavHost()
            }
        }
    }
}
