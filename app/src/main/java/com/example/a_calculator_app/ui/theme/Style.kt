package com.example.a_calculator_app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp


data class AppStyle(
    val keyTextStyle: TextStyle = TextStyle(fontSize = 16.sp)
)

val CompactStyle = AppStyle(
    keyTextStyle = TextStyle(fontSize = 18.sp),
)

// Typography for tablets and foldables (Medium/Expanded)
val ExpandedStyle = AppStyle(
    keyTextStyle = TextStyle(fontSize = 24.sp),
)

// Create the Local provider
val LocalAppStyle = staticCompositionLocalOf {
    CompactStyle  // TODO: make it depends on adaptiveInfo
}
