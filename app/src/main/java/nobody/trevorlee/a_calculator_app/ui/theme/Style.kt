package nobody.trevorlee.a_calculator_app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


data class AppStyle(
    val darkTheme: Boolean,
    val topBarTextStyle: TextStyle,
    val headerRowTextStyle: TextStyle,
    val digitTextStyle: TextStyle,
    val keyTextStyle: TextStyle,
    val historyRowTextStyle: TextStyle,
    val showAdditionalMessages: Boolean
)


private val CompactStyle = AppStyle(
    darkTheme = false,
    topBarTextStyle = TextStyle(fontSize = 18.sp, fontFamily = FontFamily.SansSerif),
    headerRowTextStyle = TextStyle(fontSize = 14.sp, fontFamily = FontFamily.Monospace),
    digitTextStyle = TextStyle(fontSize = 26.sp),
    keyTextStyle = TextStyle(fontSize = 20.sp),
    historyRowTextStyle = TextStyle(fontSize = 14.sp),
    showAdditionalMessages = false
)

private val ExpandedStyle = AppStyle(
    darkTheme = false,
    topBarTextStyle = TextStyle(fontSize = 24.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold),
    headerRowTextStyle = TextStyle(fontSize = 18.sp, fontFamily = FontFamily.Monospace),
    digitTextStyle = TextStyle(fontSize = 48.sp),
    keyTextStyle = TextStyle(fontSize = 24.sp),
    historyRowTextStyle = TextStyle(fontSize = 18.sp),
    showAdditionalMessages = true
)

fun getAppStyle(darkTheme: Boolean, screenWidthDp: Int): AppStyle {
    val appStyle = if (screenWidthDp <= 360) CompactStyle else ExpandedStyle
    return appStyle.copy(darkTheme = darkTheme)
//    return CompactStyle.copy(
//        topBarTextStyle = CompactStyle.topBarTextStyle.copy(fontSize = 16.sp),
//    )
}


// depends on device screen size ... see ACalculatorAppTheme.appStyle
val LocalAppStyle = staticCompositionLocalOf {
    CompactStyle
}
