package com.example.a_calculator_app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp


data class AppStyle(
    val headerRowTextStyle: TextStyle,
    val digitTextStyle: TextStyle,
    val keyTextStyle: TextStyle,
    val historyRowTextStyle: TextStyle
)

val CompactStyle = AppStyle(
    headerRowTextStyle = TextStyle(fontSize = 10.sp, fontFamily = FontFamily.Monospace),
    digitTextStyle = TextStyle(fontSize = 20.sp),
    keyTextStyle = TextStyle(fontSize = 12.sp),
    historyRowTextStyle = TextStyle(fontSize = 10.sp),
)

// Typography for tablets and foldables (Medium/Expanded)
val ExpandedStyle = AppStyle(
    headerRowTextStyle = TextStyle(fontSize = 18.sp, fontFamily = FontFamily.Monospace),
    digitTextStyle = TextStyle(fontSize = 48.sp),
    keyTextStyle = TextStyle(fontSize = 24.sp),
    historyRowTextStyle = TextStyle(fontSize = 18.sp),
)

// Create the Local provider ... see ACalculatorAppTheme.appStyle
val LocalAppStyle = staticCompositionLocalOf {
    CompactStyle
}
