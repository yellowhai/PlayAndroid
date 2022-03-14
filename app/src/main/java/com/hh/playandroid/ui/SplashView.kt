package com.hh.playandroid.ui

import android.window.SplashScreenView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.hh.common.theme.HhfTheme
import kotlinx.coroutines.delay

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/22  14:19
 */

@Composable
fun SplashView(startMain: () -> Unit) {
    var enabled by remember { mutableStateOf(false) }
    val bgColor: Color by animateColorAsState(
        if (enabled) HhfTheme.colors.themeColor
        else HhfTheme.colors.themeColor.copy(alpha = 0.3f),
        animationSpec = tween(durationMillis = 2000)
    )
    val textColor: Color by animateColorAsState(
        if (enabled) Color.White
        else Color.White.copy(alpha = 0.3f),
        animationSpec = tween(durationMillis = 2000)
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "PlayAndroid", color = textColor,style = MaterialTheme.typography.h5)
        }

    }
    LaunchedEffect(Unit) {
        enabled = true
        delay(2000)
        startMain.invoke()
    }
}