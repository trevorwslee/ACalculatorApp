package nobody.trevorlee.a_calculator_app

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nobody.trevorlee.a_calculator_app.ui.theme.ACalculatorAppTheme
import nobody.trevorlee.a_calculator_app.ui.theme.LocalAppStyle

//val BRIDGE_URL: String? = "http://192.168.0.17:8000/"

val BRIDGE_URL: String? = null
val BRIDGE_NO_BUTTONS: Boolean = true


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (false) enableEdgeToEdge()
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
                style = LocalAppStyle.current.topBarTextStyle,
//                modifier = Modifier.
//                    border(2.dp, Color.Green)
            )
        },
        //windowInsets = WindowInsets(0.dp),
        modifier = modifier/*.
            border(2.dp, Color.Red).
            padding(0.dp)*/
    )
}

@Composable
fun MainView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bridgeUrl = remember { mutableStateOf(BridgeUrl(BRIDGE_URL, 0)) }
    val showAdditionalMessages = LocalAppStyle.current.showAdditionalMessages
    val state = remember {
        val angleMode = mutableStateOf("deg")
        val digits = mutableStateOf("...")
        val history = mutableStateOf("")
        val opIndicator = mutableStateOf("")
        val bracketIndicator = mutableStateOf("")
        val memory = mutableStateOf("")
        State(angleMode, digits, history, opIndicator, bracketIndicator, memory)
    }
    val bridgeWebView = remember {
        createBridgeWebView(context) { bridgeWebView: WebView ->
            delayLoadBridge(bridgeWebView, state, 0, showAdditionalMessages = showAdditionalMessages, hideButtons = BRIDGE_NO_BUTTONS)
        }
    }
    if (true) {
        Column(modifier = modifier) {
            CalculatorView(bridgeWebView, state)
            Box(
                modifier = Modifier.
                    border(1.dp, Color.Blue).
                    weight(1f),
            ) {
                BridgeWebView(bridgeWebView, bridgeUrl)
            }
        }
    } else {
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
}


//@Preview(showBackground = true)
//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Preview(showBackground = true, widthDp = 411, heightDp = 914)
@Composable
fun ACalculatorAppPreview() {
    val state = remember {
        val angleMode = mutableStateOf("rad")
        val digits = mutableStateOf("preview")
        val history = mutableStateOf("history")
        val opIndicator = mutableStateOf("+")
        val bracketIndicator = mutableStateOf("(1)")
        val memory = mutableStateOf("123.456")
        State(angleMode, digits, history, opIndicator, bracketIndicator, memory)
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
            Box(modifier = Modifier.
                padding(innerPadding)) {
                if (false) {
                    CalculatorView(null, state)
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),  // add gaps between items
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CalculatorView(null, state)
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(text = "~~~ Preview End ~~~"                            )
                        }
                    }
                }
            }
        }
    }
}
