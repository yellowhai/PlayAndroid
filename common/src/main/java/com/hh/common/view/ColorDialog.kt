package com.hh.common.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hh.common.R
import com.vanpra.composematerialdialogs.MaterialDialogButtons
import com.vanpra.composematerialdialogs.color.ARGBPickerState
import com.vanpra.composematerialdialogs.color.ColorPalette
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.title

/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.view
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/31  14:29
 */
@Composable
fun ColorDialogDemo() {
    var waitForPositiveButton by remember { mutableStateOf(false) }

    Row(Modifier.padding(8.dp)) {
        Switch(
            checked = waitForPositiveButton,
            onCheckedChange = { waitForPositiveButton = it }
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "Wait for positive button",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
    }

    DialogAndShowButton(
        buttonText = "Color Picker Dialog",
        buttons = { defaultColorDialogButtons() }
    ) {
        title("Select a Color")
        colorChooser(colors = ColorPalette.Primary, waitForPositiveButton = waitForPositiveButton) {
            println(it)
        }
    }

    DialogAndShowButton(
        buttonText = "Color Picker Dialog With Sub Colors",
        buttons = { defaultColorDialogButtons() }
    ) {
        title("Select a Sub Color")
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            waitForPositiveButton = waitForPositiveButton
        ) {
            println(it)
        }
    }

    DialogAndShowButton(
        buttonText = "Color Picker Dialog With Initial Selection",
        buttons = { defaultColorDialogButtons() }
    ) {
        title("Select a Sub Color")
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            waitForPositiveButton = waitForPositiveButton,
            initialSelection = 5
        ) {
            println(it)
        }
    }

    DialogAndShowButton(
        buttonText = "Color Picker Dialog With RGB Selector",
        buttons = { defaultColorDialogButtons() }
    ) {
        title("Custom RGB")
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            argbPickerState = ARGBPickerState.WithoutAlphaSelector,
            waitForPositiveButton = waitForPositiveButton
        ) {
            println(it)
        }
    }

    DialogAndShowButton(
        buttonText = "Color Picker Dialog With ARGB Selector",
        buttons = { defaultColorDialogButtons() }
    ) {
        title("Custom ARGB")
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            argbPickerState = ARGBPickerState.WithAlphaSelector,
            waitForPositiveButton = waitForPositiveButton
        ) {
//            SettingUtil.setColor(it.toArgb())
        }
    }
}

@Composable
fun MaterialDialogButtons.defaultColorDialogButtons() {
    positiveButton(stringResource(R.string.confirm))
    negativeButton(stringResource(R.string.cancel))
}