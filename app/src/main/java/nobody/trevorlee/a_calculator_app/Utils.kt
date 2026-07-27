package nobody.trevorlee.a_calculator_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.webkit.WebViewAssetLoader


const val MAX_LOAD_BRIDGE_COUNT: Int = 5
const val CALC_JS_VAR: String = "app_calc"
const val CALC_DISPLAY_WIDTH: Int = 12


data class BridgeUrl(val url: String?, val counter: Int) {
    fun newUrl(url: String?): BridgeUrl {
        return BridgeUrl(url, counter + 1)
    }
}

data class State(
    val angleMode: MutableState<String>,
    val digits: MutableState<String>, val history: MutableState<String>,
    val opIndicator: MutableState<String>, val bracketIndicator: MutableState<String>,
    val memory: MutableState<String>
)

data class Key(
    val key: String,
    val color: Color = Color.Cyan,
    val textColor: Color = Color.Blue,
    val weight: Float = 1.0f
)

fun createBridgeWebView(context: Context, onLoadedCallback: (WebView) -> Unit): WebView {
    return WebView(context).apply {
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
            .build()
        this.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        this.settings.javaScriptEnabled = true
        if (true) {
            //this.settings.loadsImagesAutomatically = true
            this.settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
        }
        if (false) {
            this.settings.setDomStorageEnabled(true)
            this.settings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW)
        }
        if (false) {
            this.settings.setAllowContentAccess(true)
            this.settings.setAllowFileAccess(true)
        }
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
            override fun onPageFinished(view: WebView?, url: String?) {
                if (true) {
                    println("* WebView ... loaded URL: $url")
                }
                onLoadedCallback(view!!)
            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                println("* WebView ... request $request ... error ... $error")
            }
        }
        if (true) {
            this.addJavascriptInterface(object {
                @android.webkit.JavascriptInterface
                fun onClickedAdditionalMessage(message: String) {
                    if (true) {
                        println("*** clicked additional message: $message")
                    }
                    if (message == "project-github") {
                        // open a browser to the GitHub project page
                        // visit https://github.com/trevorwslee/ACalculatorApp
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/trevorwslee/ACalculatorApp"))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        this@apply.context.startActivity(intent)
                    }
                }
            }, "AndroidBridge")
        }
    }
}

fun delayLoadBridge(
    bridgeWebView: WebView,
    state: State,
    tried: Int,
    darkMode: Boolean,
    showAdditionalMessages: Boolean,
    systemInfo: String,
    deviceInfo: String,
    hideButtons: Boolean = false
) {
    bridgeWebView.postDelayed({
        bridgeWebView.evaluateJavascript("$CALC_JS_VAR = Calculator.new($CALC_DISPLAY_WIDTH)") {
            if (it != null && it != "null") {
                syncDisplay(bridgeWebView, state)
                if (darkMode) {
                    bridgeWebView.evaluateJavascript("document.getElementsByTagName('body')[0].classList.add('dark-mode')") {}
                }
                if (systemInfo.isNotEmpty()) {
                    bridgeWebView.evaluateJavascript("document.getElementById('system_info').style.display='block'") {}
                    bridgeWebView.evaluateJavascript("document.getElementById('system_info_text').innerText='${systemInfo}'") {}
                    if (darkMode) {
                        bridgeWebView.evaluateJavascript("document.getElementById('system_info_text').parentElement.classList.add('dark-mode')") {}
                    }
                }
                if (deviceInfo.isNotEmpty()) {
                    bridgeWebView.evaluateJavascript("document.getElementById('device_info').style.display='block'") {}
                    bridgeWebView.evaluateJavascript("document.getElementById('device_info_text').innerText='${deviceInfo}'") {}
                    if (darkMode) {
                        bridgeWebView.evaluateJavascript("document.getElementById('device_info_text').parentElement.classList.add('dark-mode')") {}
                    }
                }
                if (showAdditionalMessages) {
                    bridgeWebView.evaluateJavascript("document.getElementById('additional_messages').style.display='block'") {}
                }
            } else {
//                Toast.makeText(
//                    bridgeWebView.context,
//                    "failed to new calculator",
//                    Toast.LENGTH_SHORT
//                ).show()
                if (tried < MAX_LOAD_BRIDGE_COUNT) {
                    state.digits.value = "." + state.digits.value
                    delayLoadBridge(bridgeWebView, state, tried + 1, darkMode, showAdditionalMessages, systemInfo, deviceInfo, hideButtons)
                } else {
                    state.digits.value = "failed"
                }
            }
        }
    }, 200L * (tried + 1))
    if (hideButtons) {
        bridgeWebView.evaluateJavascript("document.getElementById('buttons').style.display='none'") {}
    }
}

fun keyToKeyText(key: String): AnnotatedString {
//    val s = "a" + buildAnnotatedString {
//        withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
//            append("Example")
//        }
//    }
    val builder = when (key) {
        "pow10" -> {
            AnnotatedString.Builder().apply {
                append("10")
                withStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 12.sp
                    )
                ) {
                    append("x")
                }
            }
        }

        "square" -> {
            AnnotatedString.Builder().apply {
                append("x")
                withStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 12.sp
                    )
                ) {
                    append("2")
                }
            }
        }

        "^" -> {
            AnnotatedString.Builder().apply {
                append("x")
                withStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 12.sp
                    )
                ) {
                    append("y")
                }
            }
        }

        "asin", "acos", "atan" -> {
            AnnotatedString.Builder().apply {
                append(key.substring(1))
                withStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 12.sp
                    )
                ) {
                    append("-1")
                }
            }
        }

        else -> null
    }
    if (builder != null) {
        return builder.toAnnotatedString()
    }
    val text = when (key) {
        "0" -> "0\uFE0F⃣" // 0️⃣
        "1" -> "1\uFE0F⃣" // 1️⃣
        "2" -> "2\uFE0F⃣" // 2️⃣
        "3" -> "3\uFE0F⃣" // 3️⃣
        "4" -> "4\uFE0F⃣" // 4️⃣
        "5" -> "5\uFE0F⃣" // 5️⃣
        "6" -> "6\uFE0F⃣" // 6️⃣
        "7" -> "7\uFE0F⃣" // 7️⃣
        "8" -> "8\uFE0F⃣" // 8️⃣
        "9" -> "9\uFE0F⃣" // 9️⃣
        "+" -> "➕"
        "-" -> "➖"
        "*" -> "✖️"
        "/" -> "➗"
        "." -> "•"
        "neg" -> "±"
        "sqrt" -> "√"
        "inv" -> "1/x"
        "abs" -> "|x|"
        "PI" -> "π"
        "E" -> "e"
        "deg", "rad", "ac", "mr", "ms" -> key.uppercase()
        else -> key
    }
    return AnnotatedString(text)
//        .Builder().apply {
//        append(text)
//        if (key == "pow10") {
//            withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
//                append("10")
//            }
//        }
//    }.toAnnotatedString()
}


fun syncDisplay(
    bridgeWebView: WebView,
    state: State,
) {
    bridgeWebView.evaluateJavascript("$CALC_JS_VAR.get_display()") {
        if (it != null) {
            state.digits.value = it.replace("\"", "")
        }
    }
    bridgeWebView.evaluateJavascript("$CALC_JS_VAR.get_history()") {
        if (it != null) {
            state.history.value = it.replace("\"", "")
        }
    }
    bridgeWebView.evaluateJavascript("$CALC_JS_VAR.get_indicators()") {
        if (it != null) {
            val indicators = it.replace("\"", "").split("|")
            if (indicators.size == 3) {
                val opIndicator = indicators[0]
                val bracketIndicator = indicators[1]
                val memory = indicators[2]
                state.opIndicator.value = opIndicator
                state.bracketIndicator.value = bracketIndicator
                state.memory.value = memory
            }
        }
    }
}

fun onKeyPressed(
    key: String,
    bridgeWebView: WebView?,
    state: State
) {
    if (bridgeWebView != null) {
        if (key == "deg" || key == "rad") {
            val newAngleMode = if (state.angleMode.value == "deg") "rad" else "deg"
            state.angleMode.value = newAngleMode
            bridgeWebView.evaluateJavascript("$CALC_JS_VAR.use_angle_mode(\"${newAngleMode}\")") {
                state.angleMode.value = newAngleMode
            }
            return
        }
        if (key == "reload") {
            state.digits.value = "..."
            state.history.value = "powering on ..."
            bridgeWebView.reload()
            return
        }
        val js = "$CALC_JS_VAR.push('${key}')"
//        val js = if (key == "undo") {
//            "$CALC_JS_VAR.undo()"
//        } else {
//            "$CALC_JS_VAR.push('${key}')"
//        }
        bridgeWebView.evaluateJavascript(js) {
            syncDisplay(bridgeWebView, state)
        }
    }
}





