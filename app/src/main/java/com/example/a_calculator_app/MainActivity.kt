package com.example.a_calculator_app

import BridgeUrl
import State
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewAssetLoader
import createBridgeWebView
import delayLoadBridge
import com.example.a_calculator_app.ui.theme.ACalculatorAppTheme

//val BRIDGE_URL: String? = "http://192.168.0.17:8000/"

val BRIDGE_URL: String? = null
val BRIDGE_NO_BUTTONS: Boolean = true


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ACalculatorAppTheme {
                Scaffold(
                    topBar = {
                        HomeTopAppBar()
                    },
                    modifier = Modifier
                        .border(8.dp, Color.Black)
                        .padding(10.dp)
                ) { innerPadding ->
                    MainView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                text = context.resources.getString(R.string.app_name),
            )
        },
        modifier = modifier
    )
}

@Composable
fun MainView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bridgeUrl = remember { mutableStateOf(BridgeUrl(BRIDGE_URL, 0)) }
    val state = remember {
        val angle_mode = mutableStateOf("deg")
        val digits = mutableStateOf("...")
        val history = mutableStateOf("")
        val opIndicator = mutableStateOf("")
        val bracketIndictaor = mutableStateOf("")
        val memory = mutableStateOf("")
        State(angle_mode, digits, history, opIndicator, bracketIndictaor, memory)
    }
    val bridgeWebView = remember {
        createBridgeWebView(context) { bridgeWebView: WebView ->
            delayLoadBridge(bridgeWebView, state, 0, BRIDGE_NO_BUTTONS)
        }
    }
    Column(modifier = modifier) {
        val url = bridgeUrl.value.url
        CalculatorView(bridgeWebView, state)
//        Row() {
//            Button(onClick = {
//                bridgeUrl.value = bridgeUrl.value.newUrl(BRIDGE_URL)
//                //counter.value += 1
//            }) {
//                Text("Reload")
//            }
//            Text(text = url ?: "assets")
//        }
        BridgeWebView(bridgeWebView, bridgeUrl)
    }
}


@Preview(showBackground = true)
@Composable
fun ACalculatorAppPreview() {
    val state = remember {
        val angle_mode = mutableStateOf("rad")
        val digits = mutableStateOf("preview")
        val history = mutableStateOf("history")
        val opIndicator = mutableStateOf("+")
        val bracketIndictaor = mutableStateOf("(1)")
        val memory = mutableStateOf("123.456")
        State(angle_mode, digits, history, opIndicator, bracketIndictaor, memory)
    }
    ACalculatorAppTheme {
        Scaffold(
            topBar = {
                HomeTopAppBar()
            },
            modifier = Modifier
                .border(8.dp, Color.Black)
                .padding(10.dp)
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                CalculatorView(null, state)
            }
        }
    }
}


val ENDPOINT: String = "http://192.168.0.17:8000/simple.html"
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SimpleBridgeWebView()
//        }
//    }
//}
@Composable
fun SimpleBridgeWebView(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.settings.javaScriptEnabled = true
                this.webViewClient = WebViewClient()
                this.loadUrl(ENDPOINT)
            }
        },
        update = {}
    )
}


@Composable
fun SimpleInternBridgeWebView() {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                val assetLoader = WebViewAssetLoader.Builder()
                    .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
                    .build()
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.settings.javaScriptEnabled = true
                this.webViewClient = object : WebViewClient() {
                    override fun shouldInterceptRequest(
                        view: WebView,
                        request: WebResourceRequest
                    ): WebResourceResponse? {
                        val host = request.url.host
                        if (host == "appassets.androidplatform.net") {
                            return assetLoader.shouldInterceptRequest(request.url)
                        } else {
                            return super.shouldInterceptRequest(view, request)
                        }
                    }
                }
            }
        },
        update = {}
    )
}
