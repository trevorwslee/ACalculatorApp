package com.example.a_calculator_app.internal_ui

import android.content.Context
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewAssetLoader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val webView = createSimpleInternBridgeWebView(this)
            Column() {
                val greeting = remember { mutableStateOf("...") }
                SimpleInternBridgeWebView(webView)
                Text(text = greeting.value)
                Button(onClick = {
                    webView.evaluateJavascript("get_greeting('Android')") {
                        greeting.value = it
                    }
                }) {
                    Text("Get Greeting")
                }
            }
        }
    }
}
@Composable
fun SimpleInternBridgeWebView(webView: WebView, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context -> webView },
        update = { webView.loadUrl("https://appassets.androidplatform.net/assets/bridge/simple.html") }
    )
}
fun createSimpleInternBridgeWebView(context: Context): WebView {
    val assetLoader = WebViewAssetLoader.Builder()
        .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
        .build()
    return WebView(context).apply {
        this.settings.javaScriptEnabled = true
        this.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }
        }
        this.loadUrl("https://appassets.androidplatform.net/assets/bridge/simple.html")
    }
}

