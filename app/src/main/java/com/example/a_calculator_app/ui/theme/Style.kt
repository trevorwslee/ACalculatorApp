package com.example.a_calculator_app.ui.theme

import android.graphics.Color
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class AppStyle(
    val topBarTextStyle: TextStyle,
    val headerRowTextStyle: TextStyle,
    val digitTextStyle: TextStyle,
    val keyTextStyle: TextStyle,
    val historyRowTextStyle: TextStyle
)

val CompactStyle = AppStyle(
    topBarTextStyle = TextStyle(fontSize = 18.sp, fontFamily = FontFamily.SansSerif),
    headerRowTextStyle = TextStyle(fontSize = 14.sp, fontFamily = FontFamily.Monospace),
    digitTextStyle = TextStyle(fontSize = 24.sp),
    keyTextStyle = TextStyle(fontSize = 18.sp),
    historyRowTextStyle = TextStyle(fontSize = 14.sp),
)

val ExpandedStyle = AppStyle(
    topBarTextStyle = TextStyle(fontSize = 24.sp, fontFamily = FontFamily.SansSerif),
    headerRowTextStyle = TextStyle(fontSize = 18.sp, fontFamily = FontFamily.Monospace),
    digitTextStyle = TextStyle(fontSize = 48.sp),
    keyTextStyle = TextStyle(fontSize = 24.sp),
    historyRowTextStyle = TextStyle(fontSize = 18.sp),
)

// depends on device screen size ... see ACalculatorAppTheme.appStyle
val LocalAppStyle = staticCompositionLocalOf {
    CompactStyle
}
