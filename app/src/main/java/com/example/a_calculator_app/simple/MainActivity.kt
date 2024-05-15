package com.example.a_calculator_app.simple

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

val ENDPOINT: String = "http://192.168.0.17:8000/simple.html"
//val ENDPOINT: String = "http://192.168.0.17:8000/simple_calculator.html"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleBridgeWebView()
        }
    }
}
@Composable
fun SimpleBridgeWebView(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                this.settings.javaScriptEnabled = true
                this.webViewClient = WebViewClient()
                this.loadUrl(ENDPOINT)
            }
        },
        update = {}
    )
}
