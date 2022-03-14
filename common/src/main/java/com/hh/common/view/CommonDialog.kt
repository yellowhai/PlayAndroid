package com.hh.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hh.common.theme.HhfTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogButtons
import com.vanpra.composematerialdialogs.MaterialDialogScope
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.view
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/31  14:32
 */
@Composable
fun DialogAndShowButton(
    buttonText: String,
    buttons: @Composable MaterialDialogButtons.() -> Unit = {},
    content: @Composable MaterialDialogScope.() -> Unit
) {
    val dialogState = rememberMaterialDialogState()

    MaterialDialog(dialogState = dialogState, buttons = buttons) {
        content()
    }

    TextButton(
        onClick = { dialogState.show() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colors.primaryVariant),
    ) {
        Text(
            buttonText,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            color = HhfTheme.colors.textColor
        )
    }
}

/**
 * @brief Add title to top of layout
 */
@Composable
fun DialogSection(title: String, content: @Composable () -> Unit) {
    Text(
        title,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
    )

    content()
}