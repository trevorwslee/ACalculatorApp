package com.example.a_calculator_app

import BridgeUrl
import State
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a_calculator_app.R
import createBridgeWebView
import delayLoadBridge
import com.example.a_calculator_app.ui.theme.DumbCalculatorTheme

//val REMOTE_URL: String? = "http://192.168.0.17:8000/"
val REMOTE_URL: String? = null
//val REMOTE_URL: String? = "https://trevorwslee.github.io/DumbCalculator/"

//const val MAC_LOAD_BRIDEG_COUNT: Int = 5
//const val CALC_JS_VAR: String = "app_calc"
//const val CALC_DISPLAY_WIDTH: Int = 12


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DumbCalculatorTheme {
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
    val bridgeUrl = remember { mutableStateOf(BridgeUrl(REMOTE_URL, 0)) }
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
            delayLoadBridge(bridgeWebView, state)
        }
    }
    Column(modifier = modifier) {
        val url = bridgeUrl.value.url
        CalculatorView(bridgeWebView, state)
        Row() {
            Button(onClick = {
                bridgeUrl.value = bridgeUrl.value.newUrl(REMOTE_URL)
                //counter.value += 1
            }) {
                Text("Reload")
            }
            Text(text = url ?: "assets")
        }
        BridgeWebView(bridgeWebView, bridgeUrl)
    }
}


@Preview(showBackground = true)
@Composable
fun DumbCalculatorPreview() {
    val state = remember {
        val angle_mode = mutableStateOf("rad")
        val digits = mutableStateOf("preview")
        val history = mutableStateOf("history")
        val opIndicator = mutableStateOf("+")
        val bracketIndictaor = mutableStateOf("(1)")
        val memory = mutableStateOf("123.456")
        State(angle_mode, digits, history, opIndicator, bracketIndictaor, memory)
    }
    DumbCalculatorTheme {
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
