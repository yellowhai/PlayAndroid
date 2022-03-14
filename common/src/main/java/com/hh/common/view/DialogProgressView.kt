package com.hh.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.R
import com.hh.common.theme.HhfTheme

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/24  13:19
 */


var isShowProgressDialog by mutableStateOf(false)
var progressDialogText by mutableStateOf("")


@Composable
fun DialogProgress() {
    val dialogWidth = 160.dp
    val dialogHeight = 100.dp
    if (isShowProgressDialog) {
        Dialog(
            { isShowProgressDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = true),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(dialogWidth)
                    .height(dialogHeight)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Text(
                        progressDialogText,
                        Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
                        HhfTheme.colors.textColor
                    )
                }
            }
        }
    }
}

@Composable
fun BoxProgress() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = HhfTheme.colors.themeColor)
            Text(
                stringResource(id = R.string.loading),
                Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
                HhfTheme.colors.textColor
            )
        }
    }
}

fun showLd(title: String = context.resources.getString(R.string.loading)) {
    progressDialogText = title
    isShowProgressDialog = true
}

fun ldDismiss() {
    isShowProgressDialog = false
}