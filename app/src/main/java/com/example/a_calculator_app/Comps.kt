package com.example.a_calculator_app

import BridgeUrl
import CALC_DISPLAY_WIDTH
import Key
import State
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import keyToKeyText
import onKeyPressed

@Composable
fun BridgeWebView(
    bridgeWebView: WebView,
    bridgeUrl: MutableState<BridgeUrl>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context -> bridgeWebView },
        update = {
            val url = bridgeUrl.value.url
            val loadUrl = if (url != null) {
                url
            } else {
                "https://appassets.androidplatform.net/assets/bridge/index.html"  // IMPORTANT: host appassets.androidplatform.net is required
            }
            if (it.url != loadUrl) {
                it.loadUrl(loadUrl)
            } else {
                it.reload()
            }
        },
        modifier = modifier
    )
}


@Composable
fun HeaderRow(
    bridgeWebView: WebView?,
    state: State
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(1.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val opIndicator = state.opIndicator.value.padStart(2, ' ')
        val bracketIndicator = state.bracketIndictaor.value.padStart(3, ' ')
        val memory = state.memory.value.let {
            if (it.isNotEmpty()) {
                "\uD83D\uDCBE${it}".padStart(15, ' ')
            } else ""
        }
        val indicators = opIndicator + " " + bracketIndicator
        KeyButton("reload", AnnotatedString("ϟ"), Color.Red, Color.Yellow, bridgeWebView, state)
        Text(
            text = memory,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Yellow,
            textAlign = TextAlign.Right,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .absolutePadding(0.dp, 0.dp, 10.dp, 5.dp)
                .align(Alignment.Bottom)
                .weight(1f)
        )
        Text(
            text = indicators,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.LightGray,
            textAlign = TextAlign.Right,
            softWrap = false,
            modifier = Modifier
                .absolutePadding(0.dp, 0.dp, 10.dp, 5.dp)
                .align(Alignment.Bottom)
            //.weight(1f)
        )
    }
}


@Composable
fun DisplayRow(state: State) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val digits = state.digits.value.let {
            val len = it.length
            if (len == CALC_DISPLAY_WIDTH) {
                it
            } else if (len < CALC_DISPLAY_WIDTH) {
                " ".repeat(CALC_DISPLAY_WIDTH - len) + it
            } else {
                it.substring(0, CALC_DISPLAY_WIDTH)
            }
        }
        for (digit in digits) {
            Text(
                text = digit.toString(),
                textAlign = TextAlign.Center,
                fontSize = 48.sp,
                modifier = Modifier
                    .background(Color.White)
                    .width(30.dp)
                    .border(1.dp, Color.Black)
                    .padding(1.dp)
                    .weight(1f)
            )
        }
    }
}


@Composable
fun KeyButton(
    key: String,
    text: AnnotatedString,
    color: Color,
    textColor: Color,
    bridgeWebView: WebView?,
    state: State,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            onKeyPressed(key, bridgeWebView, state)
        },
        //shape = RoundedCornerShape(30),
        colors = ButtonDefaults.buttonColors(color),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .background(Color.Black)
            //.border(2.dp, Color.Transparent)
            .padding(1.dp)
    ) {
        Text(
            text = text,
            softWrap = false,
            fontSize = 24.sp,
            color = textColor
        )
    }

}

@Composable
fun KeyRow(
    keys: List<Key>,
    bridgeWebView: WebView?,
    state: State
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        //.border(2.dp, Color.Green)
        //.padding(1.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (key in keys) {
            KeyButton(
                key.key,
                keyToKeyText(key.key),
                key.color,
                key.textColor,
                bridgeWebView,
                state,
                Modifier.weight(key.weight)
            )
        }
    }
}

@Composable
fun HistoryRow(
    bridgeWebView: WebView?,
    state: State
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(1.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //KeyButton("reload", AnnotatedString("ϟ"), Color.Red, bridgeWebView, state)
        Text(
            text = state.history.value,
            fontSize = 18.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Right,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .background(Color.Gray)
                //.border(10.dp, Color.Transparent)
                .absolutePadding(5.dp, 0.dp, 5.dp, 0.dp)
                .weight(1f)
        )
        KeyButton("undo", AnnotatedString("⬅"), Color.Red, Color.DarkGray, bridgeWebView, state)
//        Button(
//            onClick = { onKeyPressed("undo", bridgeWebView, state) },
//            colors = ButtonDefaults.buttonColors(Color.Red),
//            contentPadding = PaddingValues(0.dp),
//        ) {
//            Text(
//                text = "⬅",
//                softWrap = false,
//                fontSize = 24.sp,
//                color = Color.Blue
//            )
//        }
    }
}

@Composable
fun CalculatorView(
    bridgeWebView: WebView?,
    state: State
) {
    Column() {
        HeaderRow(bridgeWebView, state)
        Divider(color = Color.Gray, thickness = 1.dp)
        DisplayRow(state)
        Divider(color = Color.Gray, thickness = 1.dp)
        KeyRow(
            listOf(
                if (state.angleMode.value == "deg") {
                    Key("deg", color = Color.Magenta, textColor = Color.White)
                } else {
                    Key("rad", color = Color.Yellow, textColor = Color.Green)
                },
                Key("asin"),
                Key("acos"),
                Key("atan"),
                Key("mr", color = Color.Yellow),
                Key("ms", color = Color.Yellow),
            ),
            bridgeWebView,
            state,
        )
        KeyRow(
            listOf(
                Key("^"),
                Key("sin"),
                Key("cos"),
                Key("tan"),
                Key("PI", color = Color.Magenta),
                Key("E", color = Color.Magenta),
            ),
            bridgeWebView,
            state,
        )
        KeyRow(
            listOf(
                Key("square"),
                Key("sqrt"),
                Key("inv"),
                Key("abs"),
                Key("(", color = Color.Yellow),
                Key(")", color = Color.Yellow)
            ),
            bridgeWebView,
            state,
        )
        Divider(color = Color.Gray, thickness = 1.dp)
        KeyRow(
            listOf(
                Key("pow10"),
                Key("7"),
                Key("8"),
                Key("9"),
                Key("ac", color = Color.Red, weight = 2.0f)
            ),
            bridgeWebView,
            state,
        )
        KeyRow(
            listOf(Key("log"), Key("4"), Key("5"), Key("6"), Key("*"), Key("/")),
            bridgeWebView,
            state,
        )
        KeyRow(
            listOf(Key("ln"), Key("1"), Key("2"), Key("3"), Key("+"), Key("-")),
            bridgeWebView,
            state,
        )
        KeyRow(
            listOf(
                Key("%"),
                Key("neg"),
                Key("0"),
                Key("."),
                Key("=", color = Color.Green, weight = 2.0f)
            ),
            bridgeWebView,
            state,
        )
        Divider(color = Color.Gray, thickness = 1.dp)
        HistoryRow(bridgeWebView, state)
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorViewPreview() {
    val state = remember {
        val angle_mode = mutableStateOf("rad")
        val digits = mutableStateOf("preview")
        val history = mutableStateOf("history")
        val opIndicator = mutableStateOf("+")
        val bracketIndictaor = mutableStateOf("(1)")
        val memory = mutableStateOf("123.456")
        State(angle_mode, digits, history, opIndicator, bracketIndictaor, memory)
    }
    CalculatorView(null, state)
}


